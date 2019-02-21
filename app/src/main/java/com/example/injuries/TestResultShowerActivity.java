package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.injuries.base.BaseActivity;
import com.example.injuries.databinding.ActivityReportingResultBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.TestSample;
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
        showTestData();
        setEvents();

    }

    private void showTestData() {
        binding.correctedResponsesPercentageValue.setText(getTestAccuracy() + "%");
        binding.responseTimeValue.setText((int)getAvgResTime() + " ms");
    }

    private double getAvgResTime(){
        double res = 0;
        for(TestSample testSample: container){
            res += testSample.getResponse_time();
        }
        return res / container.getSize();
    }

    private int getTestAccuracy(){
        double res = 0;
        for(TestSample testSample: container){
            if(testSample.isResultCorrect())
                res ++;
        }
        if(res != 0)
            return (int) (100 * res / container.getSize());
        return 0;
    }
    private void setEvents() {
        binding.goToHome.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }


}
