package com.example.gustozo.legacy;

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

public class LavaView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private int programId;
    private int uResolutionHandle;
    private int uTimeHandle;
    private int aPositionHandle;

    private FloatBuffer vertexBuffer;
    private long startTime;

    private int frameCount = 0;
    private long lastFrameTime = SystemClock.uptimeMillis();
    private long currentTime = SystemClock.uptimeMillis();



    // A simple Quad (2 triangles) to fill the view surface
    // Coordinates range from -1.0 to 1.0
    private final float[] vertexData = {
            -1f, -1f,   // Bottom Left
            1f, -1f,   // Bottom Right
            -1f,  1f,   // Top Left
            -1f,  1f,   // Top Left
            1f, -1f,   // Bottom Right
            1f,  1f    // Top Right
    };

    // --- SHADER CODE STRINGS ---

    private final String vertexShaderCode =
            "attribute vec4 aPosition;" +
                    "void main() {" +
                    "  gl_Position = aPosition;" +
                    "}";

    // 3D Rotating Cube Wave Shader
    private final String fragmentShaderCode =
            "precision highp float;" +
                    "uniform vec2 uResolution;" +
                    "uniform float uTime;" +

                    // 1. Helper: Rotation Matrix
                    "mat2 rot(float a) {" +
                    "    float c = cos(a);" +
                    "    float s = sin(a);" +
                    "    return mat2(c, -s, s, c);" +
                    "}" +

                    // 2. Helper: Distance to Line Segment
                    "float segment(vec2 p, vec2 a, vec2 b) {" +
                    "    p -= a;" +
                    "    b -= a;" +
                    "    float lenSq = dot(b, b);" +
                    "    if (lenSq < 0.001) return length(p);" +
                    "    return length(p - b * clamp(dot(p, b) / lenSq, 0.0, 1.0));" +
                    "}" +

                    // 3. Helper: 3D Projection
                    "vec2 T(vec3 p, float t_val) {" +
                    "    p.xy = rot(-t_val) * p.xy;" +
                    "    p.xz = rot(0.785) * p.xz;" +
                    "    p.yz = rot(-0.625) * p.yz;" +
                    "    return p.xy;" +
                    "}" +

                    "void main() {" +
                    "    if (uResolution.y < 1.0) return;" + // Safety check

                    "    vec2 R = uResolution.xy;" +
                    "    vec2 u = gl_FragCoord.xy;" +

                    "    vec2 U = 10.0 * u / R.y;" +
                    "    vec2 M = vec2(2.0, 2.3);" +
                    "    vec2 I = floor(U / M) * M;" +
                    "    U = mod(U, M);" +

                    "    vec3 color = vec3(0.0);" +

                    "    for (float k = 0.0; k < 4.0; k += 1.0) {" +
                    "        vec2 X = vec2(mod(k, 2.0), floor(k / 2.0)) * M;" +
                    "        vec2 J = I + X;" +

                    "        float colIdx = floor(J.x / M.x + 0.1);" +
                    "        if (mod(colIdx, 2.0) > 0.5) {" +
                    "            X.y += 1.15;" +
                    "        }" +

                    // --- THE FIX IS HERE ---
                    // Old Code: used mod() causing a snap.
                    // New Code: uses sin() for a smooth, endless loop.

                    "        float waveInput = -0.2 * (J.x + J.y) + uTime * 2.0;" +
                    "        float t_val = sin(waveInput) * 0.785;" +

                    // -----------------------

                    "        for (float a = 0.0; a < 6.0; a += 1.5708) {" +
                    "            vec3 A = vec3(cos(a), sin(a), 0.7);" +
                    "            vec3 B = vec3(-A.y, A.x, 0.7);" +

                    "            float thickness = 20.0 / R.y;" +

                    "            float d1 = segment(U - X, T(A, t_val), T(B, t_val));" +
                    "            color += smoothstep(thickness, 0.0, d1);" +

                    "            vec3 A_back = A * vec3(1.0, 1.0, -1.0);" +
                    "            float d2 = segment(U - X, T(A, t_val), T(A_back, t_val));" +
                    "            color += smoothstep(thickness, 0.0, d2);" +

                    "            vec3 B_back = B * vec3(1.0, 1.0, -1.0);" +
                    "            float d3 = segment(U - X, T(A_back, t_val), T(B_back, t_val));" +
                    "            color += smoothstep(thickness, 0.0, d3);" +
                    "        }" +
                    "    }" +

                    "    gl_FragColor = vec4(color, 1.0);" +
                    "}";

    public LavaView(Context context) {
        super(context);
        init();
    }

    public LavaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2); // Request OpenGL ES 2.0
        setRenderer(this);


        // OPTIONAL: Allows UI elements (TextViews, Buttons) to sit on top of this view correctly
        // setZOrderMediaOverlay(true);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        android.util.Log.e("LavaView", "onSurfaceCreated called! programId=" + programId);
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


        // 4. Clean up Shaders
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
        // Pass the width/height to the shader
        GLES20.glUniform2f(uResolutionHandle, (float) width, (float) height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        currentTime = SystemClock.uptimeMillis();

        // Clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Calculate elapsed time in seconds
        float time = (currentTime - startTime) / 1500f;

        long frameDelta = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        frameCount++;
        if (frameCount % 60 == 0) {
            android.util.Log.d("LavaView", "Frame " + frameCount +
                    ", time=" + ((currentTime - startTime) / 600f) +
                    ", frameDelta=" + frameDelta + "ms");
        }

        // Log any frame that takes unusually long
        if (frameDelta > 20) {  // More than 20ms = noticeable stutter
            android.util.Log.w("LavaView", "SLOW FRAME! Delta: " + frameDelta + "ms at frame " + frameCount);
        }

        GLES20.glUseProgram(programId);

        // Update Time Uniform
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