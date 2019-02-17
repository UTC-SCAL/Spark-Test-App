package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.injuries.base.BaseActivity;
import com.example.injuries.databinding.ActivityReportingResultBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.TestSamplesContainer;

public class TestResultShowerActivity extends BaseActivity {

    ActivityReportingResultBinding binding;
    TestSamplesContainer container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO check if it's null
        container = getIntent().getExtras().getParcelable(Keys.SAMPLES_CONTAINER);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reporting_result);
        binding.toolbar.setTitle(R.string.frank_test_result);
        setEvents();

    }

    private void setEvents() {
        binding.goToHome.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }


}
