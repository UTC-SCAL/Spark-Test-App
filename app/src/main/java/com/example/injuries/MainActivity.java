package com.example.injuries;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.injuries.apis.NetworkCaller;
import com.example.injuries.apis.TestData;
import com.example.injuries.databinding.ActivityMainBinding;
import com.example.injuries.databinding.SendDataAgainBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.RotationVector;
import com.example.injuries.utils.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends MotionSensorActivity{


    private static final int ID_LENGTH = 5;
    ActivityMainBinding binding;
    RotationVector initial_position;
    float position_update_rate = 0.8f;

    int  savedTestDataCount, totalTests;

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        initial_position.update(position_update_rate, x, y, z, angle);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        showSendingDataDialogue();
    }

    private void showSendingDataDialogue() {
        List<TestData> testData = Preferences.getInstance(this).getSavedItem(Keys.TEST_DATA, List.class);
        if(testData == null)
            return;
        final Dialog sendDataDialog = new Dialog(this);
        SendDataAgainBinding binding = SendDataAgainBinding.inflate(LayoutInflater.from(this));
        binding.send.setOnClickListener(v -> sendOldData(testData));
        sendDataDialog.setContentView(binding.getRoot());
        sendDataDialog.show();

    }

    private void sendOldData(List<TestData> testDataList) {
        totalTests = testDataList.size();
        savedTestDataCount = 0;
        for(TestData testData: testDataList){
            NetworkCaller.getAPIs().SaveTest(testData).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {

                        synchronized (this){
                            savedTestDataCount ++;
                            testDataList.remove(testData);
                            checkIfFinished(totalTests, savedTestDataCount, testDataList);
                        }
                    }
                    else{
                        synchronized (this){
                            savedTestDataCount ++;
                            checkIfFinished(totalTests, savedTestDataCount, testDataList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    synchronized (this){
                        savedTestDataCount ++;
                        checkIfFinished(totalTests, savedTestDataCount, testDataList);

                    }
                }
            });
        }
    }

    private void checkIfFinished(int totalTests, int savedTestDataCount, List<TestData> testDataList) {
        if(savedTestDataCount != totalTests)
            return;
        Preferences.getInstance(this).saveItem(Keys.TEST_DATA, testDataList, List.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.frank_test);
        initial_position = new RotationVector();
        setButtonEvents();


    }

    private void setButtonEvents() {
        binding.startTest.setOnClickListener(view -> startTest());
    }


    private void startTest() {
        if(binding.candidateId.getText().length() != ID_LENGTH){
            binding.candidateId.setError(getString(R.string.id_message));
            return;
        }
        Intent show_test_intent = new Intent(this, ShowTestActivity.class);
        show_test_intent.putExtra(Keys.INITIAL_POSITIOIN, initial_position);
        show_test_intent.putExtra(Keys.CANDIDATE_ID, binding.candidateId.getText().toString());
        startActivity(show_test_intent);
    }

}
