package iti.mad.gusto.presentation.common.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A fallback GLSurfaceView responsible for rendering a full-screen ripple effect
 * on devices running Android 12 and below. It acts as a transparent overlay
 * that intercepts the trigger coordinates and runs an OpenGL-based fragment shader.
 */
public class RippleOverlayView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private int programId;
    private int uResolutionHandle, uTimeHandle, uCenterHandle, aPositionHandle;

    private FloatBuffer vertexBuffer;
    private float animTime = 0f;
    private float centerX = 0.5f;
    private float centerY = 0.5f;

    private final float[] vertexData = {
            -1f, -1f,  1f, -1f,  -1f, 1f,
            -1f,  1f,  1f, -1f,   1f, 1f
    };

    /**
     * The OpenGL fragment shader code used to calculate and render the ripple.
     * * Shader logic breakdown:
     * - Coordinates: Uses OpenGL's bottom-left origin for gl_FragCoord.
     * - Radius & Width: Features an increased max radius (2.5) to clear all screen corners
     * and a thicker wave (0.25) for better visibility at larger sizes.
     * - Ripples: Spreads out inner ripples to accommodate slower animation speeds.
     * - Opacity: Retains full opacity until exiting screen bounds (no time-based fade).
     * - Color: Applies an orange color (#FF6B35) with alpha capped at 0.6 to ensure
     * the underlying UI remains visible.
     */
    private final String fragmentShaderCode =
            "precision highp float;\n" +
                    "uniform vec2 uResolution;\n" +
                    "uniform vec2 uCenter;\n" +
                    "uniform float uTime;\n" +
                    "void main() {\n" +
                    "    vec2 uv = gl_FragCoord.xy / uResolution.xy;\n" +
                    "    float aspect = uResolution.x / uResolution.y;\n" +
                    "    vec2 correctedUV = vec2(uv.x * aspect, uv.y);\n" +
                    "    vec2 correctedCenter = vec2(uCenter.x * aspect, uCenter.y);\n" +
                    "    \n" +
                    "    float dist = distance(correctedUV, correctedCenter);\n" +
                    "    \n" +
                    "    float maxRadius = 2.5;\n" +
                    "    float currentRadius = uTime * maxRadius;\n" +
                    "    \n" +
                    "    float waveWidth = 0.25;\n" +
                    "    float wave = smoothstep(currentRadius - waveWidth, currentRadius, dist)\n" +
                    "               - smoothstep(currentRadius, currentRadius + waveWidth, dist);\n" +
                    "    \n" +
                    "    float ripples = sin((dist - currentRadius) * 20.0) * 0.5 + 0.5;\n" +
                    "    float intensity = wave * ripples;\n" +
                    "    \n" +
                    "    vec3 color = vec3(1.0, 0.42, 0.21);\n" +
                    "    \n" +
                    "    gl_FragColor = vec4(color * intensity, intensity * 0.6);\n" +
                    "}";

    public RippleOverlayView(Context context) {
        super(context);
        init();
    }

    public RippleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initializes the OpenGL surface view by setting it on top of the window,
     * configuring a translucent pixel format, and attaching the renderer.
     */
    private void init() {
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /**
     * Triggers the ripple animation from specific normalized coordinates.
     * The animation is configured to run slowly (2500ms) for a majestic effect,
     * utilizing a specific interpolator to control the expansion speed.
     *
     * @param normX The normalized X coordinate (0.0 to 1.0).
     * @param normY The normalized Y coordinate (0.0 to 1.0, with 0 at the bottom-left).
     */
    public void fireRipple(float normX, float normY) {
        this.centerX = normX;
        this.centerY = normY;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2500);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            animTime = (float) animation.getAnimatedValue();
            requestRender();
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animTime = 0f;
                requestRender();
            }
        });

        animator.start();
    }

    /**
     * Prepares the vertex buffer and compiles/links the OpenGL shaders
     * when the surface is initially created.
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexData);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                "attribute vec4 aPosition;\n void main() { gl_Position = aPosition; }");
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        programId = GLES20.glCreateProgram();
        GLES20.glAttachShader(programId, vertexShader);
        GLES20.glAttachShader(programId, fragmentShader);
        GLES20.glLinkProgram(programId);

        GLES20.glUseProgram(programId);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uResolutionHandle = GLES20.glGetUniformLocation(programId, "uResolution");
        uTimeHandle = GLES20.glGetUniformLocation(programId, "uTime");
        uCenterHandle = GLES20.glGetUniformLocation(programId, "uCenter");

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }

    /**
     * Updates the OpenGL viewport and resolution uniforms whenever the
     * surface dimensions change.
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        GLES20.glUseProgram(programId);
        GLES20.glUniform2f(uResolutionHandle, (float) width, (float) height);
    }

    /**
     * Called continuously during the animation to render the frame.
     * It clears the canvas with a transparent color and applies blending
     * rules to draw the updated ripple graphics.
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (animTime > 0 && animTime < 1) {
            GLES20.glUseProgram(programId);
            GLES20.glUniform1f(uTimeHandle, animTime);
            GLES20.glUniform2f(uCenterHandle, centerX, centerY);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        }

        Log.d("TAG", "onDrawFrame: called");
    }

    /**
     * Helper method to compile an OpenGL shader from source code.
     *
     * @param type       The type of shader (e.g., GLES20.GL_VERTEX_SHADER).
     * @param shaderCode The raw shader string.
     * @return The compiled shader reference ID.
     */
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}