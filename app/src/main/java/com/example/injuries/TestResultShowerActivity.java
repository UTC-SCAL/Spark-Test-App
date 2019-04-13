package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.injuries.apis.NetworkCaller;
import com.example.injuries.apis.TestData;
import com.example.injuries.base.BaseActivity;
import com.example.injuries.databinding.ActivityReportingResultBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.pojos.TestSamplesContainer;
import com.example.injuries.utils.AndroidUtils;
import com.example.injuries.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

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
        binding.responseTimeValue.setText((int) getAvgResTime() + " ms");
    }

    private double getAvgResTime() {
        double res = 0;
        for (TestSample testSample : container) {
            res += testSample.getResponse_time();
        }
        return res / container.getSize();
    }

    private int getTestAccuracy() {
        double res = 0;
        for (TestSample testSample : container) {
            if (testSample.isCorrect())
                res++;
        }
        if (res != 0)
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

                if (response.isSuccessful()) {
                    AndroidUtils.showDialogue("Results have been saved permanently",
                            binding.getRoot());
                } else {
                    saveDataLocally(testData, binding.getRoot());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                saveDataLocally(testData, binding.getRoot());
            }
        });
    }

    private void saveDataLocally(TestData testData, View parent) {
        List<TestData> items = new ArrayList<>();
        items.add(testData);
        AndroidUtils.showDialogue("Network problem!, Data will be saved later", binding.getRoot());
        saveDataLocally(items, parent);
    }

    private void saveDataLocally(List<TestData> testDataList, View parent) {
        AndroidUtils.showDialogue("Network problem!, Data will be saved later", binding.getRoot());
        Preferences preferences = Preferences.getInstance(parent.getContext());
        List<TestData> oldList = preferences.getSavedItem(Keys.TEST_DATA, List.class);
        if (oldList == null)
            oldList = new ArrayList<>();
        for(TestData testData: testDataList) {
            if(!oldList.contains(testData))
                oldList.add(testData);
        }
        preferences.saveItem(Keys.TEST_DATA, oldList, List.class);
    }

}
