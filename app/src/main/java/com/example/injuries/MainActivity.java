package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        binding.startTest.setOnClickListener(view -> startTest());
        binding.deviceCalibration.setOnClickListener(view -> {
            startActivity(new Intent(this, DeviceCaliberation.class));
        });


    }

    private void startTest() {
        startActivity(new Intent(this, ShowTestActivity.class));
    }



}
