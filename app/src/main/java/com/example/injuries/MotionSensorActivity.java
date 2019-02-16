package com.example.injuries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.injuries.base.BaseActivity;

@SuppressLint("Registered")
public class MotionSensorActivity extends BaseActivity implements SensorEventListener {

    public static final int SECONDS_PER_MSC = 1000;
    public static final int SAMPLING_PERIOD_US = 10000 * SECONDS_PER_MSC;
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor, Gyroscope, mOrientation, accelerometer, magnaticField;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private float azimuth;
    private float pitch;
    private float roll;

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
        Gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnaticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mRotationVectorSensor, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, Gyroscope, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, mOrientation, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, accelerometer, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, magnaticField, SAMPLING_PERIOD_US);

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
//            onRotationChanged(convert_to_euler(x / sin_theta),
//                    convert_to_euler(y / sin_theta),
//                    convert_to_euler(z / sin_theta), theta);
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation contains: azimuth, pitch and roll
                pitch = (float) Math.toDegrees(orientation[1]);
                roll = (float) Math.toDegrees(orientation[2]);

                onRotationChanged(angle_abs(azimuth), angle_abs(pitch) , angle_abs(roll), azimuth);

            }
        }
    }

    private double angle_abs(float angle) {
        return (angle + 360) % 360;
    }


    private double convert_to_euler(double value){
        return Math.toDegrees(Math.acos(value));
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onRotationChanged(double x, double y, double z, double angle){
        Log.i("rotation_values", "(" + x  + ", " + y + ", " + z + ", " + angle + ")");
    }

    protected void onGyroscopeChanged(double x, double y, double z){

    }
}
