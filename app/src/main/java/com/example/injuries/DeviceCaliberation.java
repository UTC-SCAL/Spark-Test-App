package com.example.injuries;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.injuries.databinding.ActivityDeviceCaliberationBinding;

public class DeviceCaliberation extends AppCompatActivity implements SensorEventListener {

    private ActivityDeviceCaliberationBinding binding;
    public static final int SAMPLING_PERIOD_US = 1000000;
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;

    @Override
    protected void onStart() {
        super.onStart();
        startSensors();
    }

    private void startSensors() {
        mSensorManager.registerListener(this, mRotationVectorSensor, SAMPLING_PERIOD_US);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_caliberation);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float cos_theta = event.values[3];
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2] ;
            Log.i("rotation_vector_sensor", "(" + x + "," + y + ", " + z + ", " + cos_theta  + ")");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
