package com.example.injuries;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.injuries.databinding.ActivityMainBinding;

import static java.lang.Math.acos;
import static java.lang.Math.toDegrees;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final int SAMPLING_PERIOD_US = 1000000;
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor, mLinearAcceleration;
    private Sensor mAccelerometer, mGyroscopeSensor;

    private double last_x = 0;
    ActivityMainBinding binding;


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        startSensors();
    }

    private void startSensors() {
        mSensorManager.registerListener(this, mRotationVectorSensor, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, mLinearAcceleration, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, mAccelerometer, SAMPLING_PERIOD_US);
        mSensorManager.registerListener(this, mGyroscopeSensor, SAMPLING_PERIOD_US);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.startTest.setOnClickListener(view -> {
            startActivity(new Intent(this, ShowTestActivity.class));
            Log.i("clicked", "clicked");
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float cos_theta = event.values[3];
            double theta = acos(cos_theta);
            double x = event.values[0] / theta;
            double y = event.values[1] / theta;
            double z = event.values[2] / theta;
            theta = toDegrees(theta);
            Log.i("rotation_vector_sensor", "(" + x + "," + y + ", " + z + ", " + theta  + ")");
        }

        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
//            Log.i("accelerometer_data", "" + ((y - last_x) > 0) );
//            last_x  = y;
        }

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            double gx = event.values[0];
            double gy = event.values[1];
            double gz = event.values[2];

            Log.i("accelerometer_raw_data", "" + ((gx - last_x) > 0) );
            last_x  = gx;

        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            double gx = event.values[0];
            double gy = event.values[1];
            double gz = event.values[2];
            Log.i("gyroscope_data", "(" + gx + "," + gy + ", " + gz + ")");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
