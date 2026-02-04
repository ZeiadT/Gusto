package com.example.gustozo.presentation.common.util;

import android.graphics.Color;

public class ColorUtil {
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        if (Color.alpha(color) == 255) {
            alpha = Math.round(255 * factor);
        }

        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
