package com.example.injuries;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.injuries.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.frank_test);
        setButtonEvents();


    }

    private void setButtonEvents() {
        binding.startTest.setOnClickListener(view -> startTest());
        binding.deviceCalibration.setOnClickListener(view -> startCalibration());
    }

    private void startCalibration() {
        startActivity(new Intent(this, DeviceCaliberation.class));
    }

    private void startTest() {
        startActivity(new Intent(this, ShowTestActivity.class));
    }



    private void vibrate() {
        Log.i("testing_activity", "vibration");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }
}
