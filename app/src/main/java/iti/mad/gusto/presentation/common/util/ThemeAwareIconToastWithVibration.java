package iti.mad.gusto.presentation.common.util;

import android.content.Context;

import iti.mad.gusto.core.managers.VibrationManager;

public class ThemeAwareIconToastWithVibration {
    public static void info(Context context, String message) {
        ThemeAwareIconToast.info(context, message);
        VibrationManager.successVibration(context);
    }

    public static void success(Context context, String message) {
        ThemeAwareIconToast.success(context, message);
        VibrationManager.successVibration(context);
    }

    public static void warning(Context context, String message) {
        ThemeAwareIconToast.warning(context, message);
        VibrationManager.errorVibration(context);
    }

    public static void error(Context context, String message) {
        ThemeAwareIconToast.error(context, message);
        VibrationManager.errorVibration(context);
    }
}
