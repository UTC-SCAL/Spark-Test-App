package com.example.injuries.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.injuries.R;
import com.example.injuries.TestResultShowerActivity;

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

    public static void showDialogue(String results_has_been_saved_permanently, Context context) {

    }
}
