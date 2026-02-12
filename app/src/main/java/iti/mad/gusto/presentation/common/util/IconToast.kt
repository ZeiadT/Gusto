package iti.mad.gusto.presentation.common.util

import iti.mad.gusto.R

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import www.sanju.motiontoast.R as TR

fun errorToast(context: Context, message: String) {
    MotionToast.setErrorColor(R.color.colorError)
    MotionToast.createColorToast(
        context as Activity,
        context.getString(R.string.error),
        message,
        MotionToastStyle.ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )

}

fun errorToastDark(context: Context, message: String) {
    MotionToast.setErrorColor(R.color.colorError)
    MotionToast.darkToast(
        context as Activity,
        context.getString(R.string.error),
        message,
        MotionToastStyle.ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context,R.font.montserrat_semibold)
    )

}

fun successToast(context: Context, message: String) {
    MotionToast.setSuccessColor(R.color.colorPrimary)
    MotionToast.createColorToast(
        context as Activity,
        context.getString(R.string.success),
        message,
        MotionToastStyle.SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}
fun successToastDark(context: Context, message: String) {
    MotionToast.setSuccessColor(R.color.colorPrimary)
    MotionToast.darkToast(
        context as Activity,
        context.getString(R.string.success),
        message,
        MotionToastStyle.SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}

fun warningToast(context: Context, message: String) {
    MotionToast.setWarningColor(R.color.colorWarning)
    MotionToast.createColorToast(
        context as Activity,
        context.getString(R.string.warning),
        message,
        MotionToastStyle.WARNING,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}

fun warningToastDark(context: Context, message: String) {
    MotionToast.setWarningColor(R.color.colorWarning)
    MotionToast.darkToast(
        context as Activity,
        context.getString(R.string.warning),
        message,
        MotionToastStyle.WARNING,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.SHORT_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}

fun informationToast(context: Context, message: String) {
    MotionToast.createColorToast(
        context as Activity,
        context.getString(R.string.info),
        message,
        MotionToastStyle.INFO,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}
fun informationToastDark(context: Context, message: String) {
    MotionToast.darkToast(
        context as Activity,
        context.getString(R.string.info),
        message,
        MotionToastStyle.INFO,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    )
}