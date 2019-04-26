package com.example.injuries.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.injuries.R;

public class AndroidUtils {
    private static Context context;
    private static MediaPlayer mediaPlayer;


    public static void playSound(Context context){
        if(context != AndroidUtils.context || AndroidUtils.mediaPlayer == null) {
            AndroidUtils.context = context;
            mediaPlayer = MediaPlayer.create(context, R.raw.beep_07);
        }
        mediaPlayer.start();

    }

    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }

    public static void showDialogue(String message, View view) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("close", v -> snackbar.dismiss());
        snackbar.show();
    }
}
