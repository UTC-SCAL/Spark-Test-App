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

import com.example.injuries.base.BaseActivity;
import com.example.injuries.databinding.ActivityMainBinding;
import com.example.injuries.pojos.RotationVector;


public class MainActivity extends MotionSensorActivity{


    ActivityMainBinding binding;
    RotationVector initial_position;
    float position_update_rate = 0.4f;

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        initial_position.update(position_update_rate, x, y, z, angle);
    }

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
        initial_position = new RotationVector();
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


}
