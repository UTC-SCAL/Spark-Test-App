package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.injuries.apis.NetworkCaller;
import com.example.injuries.apis.TestData;
import com.example.injuries.base.BaseActivity;
import com.example.injuries.databinding.ActivityReportingResultBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.pojos.TestSamplesContainer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultShowerActivity extends BaseActivity {

    ActivityReportingResultBinding binding;
    TestSamplesContainer container;

    String testId, candidateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO check if it's null
        container = getIntent().getExtras().getParcelable(Keys.SAMPLES_CONTAINER);
        testId = getIntent().getExtras().getString(Keys.TEST_ID);
        candidateId = getIntent().getExtras().getString(Keys.CANDIDATE_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reporting_result);
        binding.toolbar.setTitle(R.string.frank_test_result);
        showTestData();
        setEvents();
        sendResult();

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
            if(testSample.isCorrect())
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

    private void sendResult() {
        TestData testData = new TestData();
        testData.setId(candidateId);
        testData.setTest_id(testId);
        testData.setTestSamples(container.getTestSamples());
        NetworkCaller.getAPIs().SaveTest(testData).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()){
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if(t.getMessage().equals("")){

                }
            }
        });
//        todo finish should be at the end of the function
    }
}
