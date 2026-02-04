package iti.mad.gusto.presentation.common.component;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.os.SystemClock;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ShaderGradientView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private int programId;
    private int uResolutionHandle;
    private int uTimeHandle;
    private int aPositionHandle;

    private FloatBuffer vertexBuffer;
    private long startTime;

    // A simple Quad (2 triangles) to fill the view surface
    private final float[] vertexData = {
            -1f, -1f,   // Bottom Left
            1f, -1f,   // Bottom Right
            -1f,  1f,   // Top Left
            -1f,  1f,   // Top Left
            1f, -1f,   // Bottom Right
            1f,  1f    // Top Right
    };

    private final String vertexShaderCode =
            "attribute vec4 aPosition;" +
                    "void main() {" +
                    "  gl_Position = aPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision highp float;" +
                    "uniform vec2 uResolution;" +
                    "uniform float uTime;" +

                    "void main() {" +
                    // 1. Setup the 320x200 Retro Board
                    // We map the actual screen pixels (gl_FragCoord) to a tiny 320x200 grid
                    "    vec2 targetRes = vec2(320.0, 200.0);" +
                    "    vec2 uv = floor(gl_FragCoord.xy * (targetRes / uResolution.xy)) / targetRes;" +

                    // Time variable for animation speed
                    "    float t = uTime * 1.5;" +

                    // 2. Define Animated Corner Colors
                    // Top-Left: Lighter Mint (High Brightness)
                    "    vec3 colTopLeft = vec3(0.15 + 0.05*sin(t)," +
                    "                           0.65 + 0.1*sin(t * 0.7)," +
                    "                           0.40 + 0.05*cos(t));" +

                    // Top-Right: The Primary Color #1E8F57 (Pure)
                    "    vec3 colTopRight = vec3(0.12," +
                    "                            0.56 + 0.05*cos(t * 0.8)," +
                    "                            0.34 + 0.05*sin(t * 0.5));" +

                    // Bottom-Left: Deep Forest Green (Darker Shade)
                    "    vec3 colBotLeft = vec3(0.05 + 0.02*sin(t * 1.1)," +
                    "                           0.30 + 0.1*cos(t * 0.3)," +
                    "                           0.18 + 0.05*sin(t * 0.9));" +

                    // Bottom-Right: Cool Emerald (Slightly Blue-ish)
                    "    vec3 colBotRight = vec3(0.10 * sin(t * 0.6)," +
                    "                            0.50 + 0.1*cos(t * 1.2)," +
                    "                            0.45 + 0.1*sin(t));" +

                    // 3. Bilinear Interpolation
                    "    vec3 topMix = mix(colTopLeft, colTopRight, uv.x);" +
                    "    vec3 botMix = mix(colBotLeft, colBotRight, uv.x);" +
                    "    vec3 finalColor = mix(botMix, topMix, uv.y);" +

                    // 4. Output
                    "    gl_FragColor = vec4(finalColor, 1.0);" +
                    "}";

    public ShaderGradientView(Context context) {
        super(context);
        init();
    }

    public ShaderGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 1. Prepare Vertex Data
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexData);
        vertexBuffer.position(0);

        // 2. Compile Shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // 3. Create Program
        programId = GLES20.glCreateProgram();
        GLES20.glAttachShader(programId, vertexShader);
        GLES20.glAttachShader(programId, fragmentShader);
        GLES20.glLinkProgram(programId);

        // 4. Clean up
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        // 5. Get Handle Locations
        GLES20.glUseProgram(programId);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uResolutionHandle = GLES20.glGetUniformLocation(programId, "uResolution");
        uTimeHandle = GLES20.glGetUniformLocation(programId, "uTime");

        startTime = SystemClock.uptimeMillis();

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        GLES20.glUseProgram(programId);
        GLES20.glUniform2f(uResolutionHandle, (float) width, (float) height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Time in seconds
        float time = (SystemClock.uptimeMillis() - startTime) / 1000f;

        GLES20.glUseProgram(programId);
        GLES20.glUniform1f(uTimeHandle, time);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}