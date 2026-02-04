package iti.mad.gusto.core.managers;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class VibrationManager {
    private static final String TAG = "Vibrator";

    public static void vibrate(Context context, int duration){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));

            Log.d(TAG, "vibrated using api >= 26");
        } else {
            Log.d(TAG, "vibrated using api < 26");
            v.vibrate(500);
        }
    }
}
