package iti.mad.gusto.presentation.common.util;

import android.animation.ArgbEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

/**
 * Utility class for handling common View animations, including property animations,
 * vector drawables, color transitions, and text/image swapping effects.
 */
public class AnimationUtil {

    /**
     * Helper method to configure a {@link ViewPropertyAnimator} with standard parameters.
     *
     * @param view         The view to animate.
     * @param onStart      Runnable to execute when the animation starts.
     * @param onEnd        Runnable to execute when the animation ends.
     * @param startDelay   The delay before the animation begins (in ms).
     * @param duration     The duration of the animation (in ms).
     * @param interpolator The time interpolator to define the rate of change.
     * @return The configured ViewPropertyAnimator instance.
     */
    private static ViewPropertyAnimator animate(View view, Runnable onStart, Runnable onEnd, long startDelay, long duration, TimeInterpolator interpolator) {
        if (view == null) return null;
        return view.animate()
                .setStartDelay(startDelay)
                .setDuration(duration)
                .setInterpolator(interpolator)
                .withStartAction(onStart)
                .withEndAction(onEnd);
    }

    /**
     * Animates the vertical translation (Y-axis) of a view.
     *
     * @param view         The view to translate.
     * @param translationY The target Y translation value.
     * @param onStart      Action to run at the start (e.g., set visibility).
     * @param onEnd        Action to run at the end (e.g., navigation).
     * @param startDelay   Delay before starting (in ms).
     * @param duration     Animation duration (in ms).
     * @param interpolator Interpolator for the movement curve.
     */
    public static void animateTranslationY(View view, float translationY, Runnable onStart, Runnable onEnd, long startDelay, long duration, TimeInterpolator interpolator) {
        if (view == null) return;
        ViewPropertyAnimator animator = animate(view, onStart, onEnd, startDelay, duration, interpolator);
        animator.translationY(translationY).start();
    }

    /**
     * Starts an {@link AnimatedVectorDrawable} or {@link AnimatedVectorDrawableCompat}
     * if the provided ImageView's drawable supports it.
     *
     * @param imageView The ImageView containing the vector drawable.
     */
    public static void startVectorAnimation(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) drawable).start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) drawable).start();
        }
    }

    /**
     * Smoothly transitions the background color of a view between two colors using {@link ArgbEvaluator}.
     *
     * @param view      The view to update.
     * @param colorFrom The starting color integer.
     * @param colorTo   The target color integer.
     * @param duration  The duration of the color transition (in ms).
     */
    public static void animateBackgroundColor(View view, int colorFrom, int colorTo, long duration) {
        if (view == null) return;
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnim.setDuration(duration);
        colorAnim.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnim.start();
    }

    /**
     * Delays visibility change and applies an XML animation resource.
     *
     * @param view      The view to animate.
     * @param animResId The resource ID of the animation XML (e.g., R.anim.fade_in).
     * @param delay     The delay before making the view visible and starting animation (in ms).
     */
    public static void animateVisibilityWithResource(View view, int animResId, long delay) {
        if (view == null) return;
        view.postDelayed(() -> {
            view.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), animResId);
            view.startAnimation(animation);
        }, delay);
    }

    /**
     * Performs a cross-fade animation to update TextView content.
     * The text fades out (50% duration), updates the string, and fades back in (50% duration).
     *
     * @param textView      The TextView to update.
     * @param newText       The new text string to display.
     * @param totalDuration The total duration for the fade-out and fade-in cycle.
     */
    public static void animateTextFadeChange(TextView textView, String newText, long totalDuration) {
        if (textView == null) return;
        long duration = totalDuration / 2;
        textView.animate().alpha(0f).setDuration(duration).withEndAction(() -> {
            textView.setText(newText);
            textView.animate().alpha(1f).setDuration(duration).start();
        }).start();
    }

    /**
     * Orchestrates a slide-out/slide-in transition for an ImageView to swap its drawable.
     * <p>
     * The image slides out (fading to 0), changes the resource, resets position off-screen,
     * and slides back in (fading to 1).
     *
     * @param imageView     The ImageView to update.
     * @param newImageResId The resource ID of the new image.
     * @param isNext        If true, slides to the left (next item); otherwise slides to the right (previous item).
     * @param duration      The total duration for the exit and enter phases.
     */
    public static void animateImageFadeSlideChange(ImageView imageView, int newImageResId, boolean isNext, long duration) {
        if (imageView == null) return;

        float slideDistance = 100f;
        float exitX = isNext ? -slideDistance : slideDistance;

        Runnable onEnd = () -> {
            imageView.setImageResource(newImageResId);

            // Position image on the opposite side for re-entry
            float enterStartX = isNext ? slideDistance : -slideDistance;
            imageView.setTranslationX(enterStartX);

            animateImageFadeSlide(imageView, 1f, 0f, duration / 2, null);
        };

        // Phase 1: Slide out
        animateImageFadeSlide(imageView, 0f, exitX, duration / 2, onEnd);
    }

    /**
     * Helper to execute a single phase of the fade-slide animation.
     */
    private static void animateImageFadeSlide(ImageView imageView, float alpha, float slideDistance, long duration, Runnable onEnd) {
        imageView.animate()
                .translationX(slideDistance)
                .alpha(alpha)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(onEnd).start();
    }
}