package com.example.injuries;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.example.injuries.databinding.ActivityDeviceCaliberationBinding;

public class DeviceCaliberation extends MotionSensorActivity{

    private ActivityDeviceCaliberationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_caliberation);
        binding.toolbar.setTitle(R.string.frank_test);
    }

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        Log.i("rotation_vector_sensor", "(" + x + "," + y + ", " + z + ", " + angle  + ")");
    }
}