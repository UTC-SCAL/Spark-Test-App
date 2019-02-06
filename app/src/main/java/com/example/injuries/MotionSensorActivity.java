package com.example.injuries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.injuries.base.BaseActivity;

@SuppressLint("Registered")
public class MotionSensorActivity extends BaseActivity implements SensorEventListener {

    public static final int SAMPLING_PERIOD_US = 1000000;
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mRotationVectorSensor, SAMPLING_PERIOD_US);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float cos_theta = event.values[3];
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2] ;
            double theta = Math.acos(cos_theta);
            double sin_theta = Math.sin(theta);
            theta = Math.toDegrees(theta);
            onRotationChanged(x / sin_theta, y / sin_theta, z / sin_theta, theta);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onRotationChanged(double x, double y, double z, double angle){
        Log.i("rotation_values", "(" + x  + ", " + y + ", " + z + ", " + angle + ")");
    }
}
