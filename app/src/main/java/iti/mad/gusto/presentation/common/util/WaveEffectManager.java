package iti.mad.gusto.presentation.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.RenderEffect;
import android.graphics.RuntimeShader;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;

import org.intellij.lang.annotations.Language;

import iti.mad.gusto.presentation.common.component.RippleOverlayView;

/**
 * Manages rendering and execution of wave or ripple effects across the UI.
 * It handles compatibility by using Modern AGSL (Android Graphics Shading Language)
 * for devices running Android 13 (Tiramisu) and above, while providing a fallback
 * mechanism using OpenGL for older versions.
 */
public class WaveEffectManager {

    /**
     * The AGSL shader code responsible for calculating the wave distortion and color blending.
     * * Shader logic breakdown:
     * - Coordinates: AGSL uses Top-Left as (0,0), matching standard Android Views.
     * - Bounding Mask: Smoothstep is used to create a band/mask for the wave's current radius.
     * - True Refraction: Calculates the 3D curve (slope) via the derivative of the wave phase
     * and physically displaces UI pixels to create a distortion effect.
     * - Color Application: Applies a pure orange color (#FF6B35) and blends it with the
     * underlying refracted UI using a lowered alpha so the distortion remains visible.
     */
    @Language("AGSL")
    private static final String AGSL_SHADER_CODE =
            "uniform float2 uResolution;\n" +
                    "uniform float2 uCenter;\n" +
                    "uniform float uTime;\n" +
                    "uniform shader compositingNode;\n" +
                    "\n" +
                    "half4 main(float2 fragCoord) {\n" +
                    "    vec2 uv = fragCoord / uResolution;\n" +
                    "    float aspect = uResolution.x / uResolution.y;\n" +
                    "    vec2 correctedUV = vec2(uv.x * aspect, uv.y);\n" +
                    "    vec2 correctedCenter = vec2(uCenter.x * aspect, uCenter.y);\n" +
                    "    \n" +
                    "    vec2 delta = correctedUV - correctedCenter;\n" +
                    "    float dist = length(delta);\n" +
                    "    vec2 waveDir = normalize(delta);\n" +
                    "    \n" +
                    "    float maxRadius = 2.5;\n" +
                    "    float currentRadius = uTime * maxRadius;\n" +
                    "    float waveWidth = 0.25;\n" +
                    "    float wavePhase = (dist - currentRadius) * 20.0;\n" +
                    "    \n" +
                    "    float mask = smoothstep(currentRadius - waveWidth, currentRadius, dist)\n" +
                    "               - smoothstep(currentRadius, currentRadius + waveWidth, dist);\n" +
                    "    \n" +
                    "    float slope = -cos(wavePhase) * mask;\n" +
                    "    \n" +
                    "    vec2 refractedCoord = fragCoord - (waveDir * slope * 40.0);\n" +
                    "    half4 uiColor = compositingNode.eval(refractedCoord);\n" +
                    "    \n" +
                    "    vec3 baseColor = vec3(1.0, 0.42, 0.21);\n" +
                    "    \n" +
                    "    float alpha = mask * 0.35;\n" +
                    "    vec3 blended = mix(vec3(uiColor.rgb), baseColor, alpha);\n" +
                    "    \n" +
                    "    return half4(blended, uiColor.a);\n" +
                    "}";

    /**
     * Triggers the wave effect, routing to the appropriate rendering pipeline based on the OS version.
     * For API 33+ (Tiramisu), it uses AGSL and normalizes coordinates without a Y-flip
     * (Top-Left origin). For older versions, it falls back to OpenGL which requires
     * normalization with a Y-flip (Bottom-Left origin).
     *
     * @param targetView The view clicked (e.g., CheckBox)
     * @param rootView   The root layout of your screen (needed for Android 13+)
     * @param fallbackGL The RippleOverlayView (needed for Android 12 and below)
     */
    public static void fireWave(View targetView, View rootView, RippleOverlayView fallbackGL) {
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);

        float absoluteX = location[0] + (targetView.getWidth() / 2f);
        float absoluteY = location[1] + (targetView.getHeight() / 2f);

        float screenWidth = rootView.getContext().getResources().getDisplayMetrics().widthPixels;
        float screenHeight = rootView.getContext().getResources().getDisplayMetrics().heightPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            float normX = absoluteX / screenWidth;
            float normY = absoluteY / screenHeight;
            fireModernAGSLWave(rootView, screenWidth, screenHeight, normX, normY);
        } else {
            float normX = absoluteX / screenWidth;
            float normY = 1.0f - (absoluteY / screenHeight);
            if (fallbackGL != null) {
                fallbackGL.fireRipple(normX, normY);
            }
        }
    }

    /**
     * Executes the modern AGSL wave animation on devices running Android 13 or higher.
     * It sets up a RuntimeShader with the calculated uniforms (resolution, center, and time)
     * and applies a RenderEffect to the root view. The effect is updated continuously
     * during the animation and removed entirely once the animation finishes to restore
     * normal rendering performance.
     *
     * @param rootView The root layout to apply the RenderEffect onto.
     * @param width    The screen width in pixels.
     * @param height   The screen height in pixels.
     * @param normX    The normalized X coordinate of the wave's center (0.0 to 1.0).
     * @param normY    The normalized Y coordinate of the wave's center (0.0 to 1.0).
     */
    private static void fireModernAGSLWave(View rootView, float width, float height, float normX, float normY) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RuntimeShader shader = new RuntimeShader(AGSL_SHADER_CODE);
            shader.setFloatUniform("uResolution", width, height);
            shader.setFloatUniform("uCenter", normX, normY);

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(2500);
            animator.setInterpolator(new LinearInterpolator());

            animator.addUpdateListener(animation -> {
                float time = (float) animation.getAnimatedValue();
                shader.setFloatUniform("uTime", time);

                RenderEffect effect = RenderEffect.createRuntimeShaderEffect(shader, "compositingNode");
                rootView.setRenderEffect(effect);
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootView.setRenderEffect(null);
                }
            });

            animator.start();
        }
    }
}