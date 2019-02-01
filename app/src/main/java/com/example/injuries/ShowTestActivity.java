package com.example.injuries;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.injuries.databinding.ActivityMainBinding;
import com.example.injuries.databinding.ActivityShowTestBinding;

public class ShowTestActivity extends AppCompatActivity {
    ActivityShowTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test);
        binding = ActivityShowTestBinding.inflate(getLayoutInflater());


    }

}
