package iti.mad.gusto.presentation.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.R;

public class ColorUtil {
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        if (Color.alpha(color) == 255) {
            alpha = Math.round(255 * factor);
        }

        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static Single<Integer> getColorFromBitmap(Context context, Bitmap bitmap) {
        return Single.<Integer>create(
                emitter -> Palette.from(bitmap).generate(palette -> {
                    int defaultColor =
                            ContextCompat.getColor(context, R.color.orange_500_faded);

                    int dominantColor = defaultColor;
                    if (palette != null)
                        dominantColor = palette.getDominantColor(defaultColor);

                    emitter.onSuccess(dominantColor);
                })).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
}
