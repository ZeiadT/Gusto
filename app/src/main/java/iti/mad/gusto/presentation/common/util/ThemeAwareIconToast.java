package iti.mad.gusto.presentation.common.util;

import static iti.mad.gusto.presentation.common.util.IconToastKt.errorToast;
import static iti.mad.gusto.presentation.common.util.IconToastKt.errorToastDark;
import static iti.mad.gusto.presentation.common.util.IconToastKt.informationToast;
import static iti.mad.gusto.presentation.common.util.IconToastKt.informationToastDark;
import static iti.mad.gusto.presentation.common.util.IconToastKt.warningToast;
import static iti.mad.gusto.presentation.common.util.IconToastKt.warningToastDark;

import android.content.Context;
import android.content.res.Configuration;

import iti.mad.gusto.core.managers.VibrationManager;

public class ThemeAwareIconToast {
    public static void info(Context context, String message) {
        if (isDarkMode(context)) {
            informationToast(context, message);
        } else {
            informationToastDark(context, message);
        }
    }

    public static void success(Context context, String message) {
        if (isDarkMode(context)) {
            errorToastDark(context, message);
        } else {
            errorToast(context, message);
        }
    }

    public static void warning(Context context, String message) {
        if (isDarkMode(context)) {
            warningToastDark(context, message);
        } else {
            warningToast(context, message);
        }
    }

    public static void error(Context context, String message) {
        if (isDarkMode(context)) {
            errorToastDark(context, message);
        } else {
            errorToast(context, message);
        }
    }


    private static boolean isDarkMode(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}
