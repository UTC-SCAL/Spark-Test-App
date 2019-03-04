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
}
