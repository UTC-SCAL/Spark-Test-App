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
import com.example.injuries.utils.AndroidUtils;
import com.example.injuries.utils.Preferences;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    }

    private void showSendingDataDialogue() {
        Type listOfTestItems = new TypeToken<List<TestData>>(){}.getType();
        List<TestData> testData = Preferences.getInstance(this).getSavedItem(Keys.TEST_DATA, listOfTestItems);
        if(testData == null || testData.size() == 0)
            return;
        final Dialog sendDataDialog = new Dialog(this);
        SendDataAgainBinding binding = SendDataAgainBinding.inflate(LayoutInflater.from(this));
        binding.send.setOnClickListener(v -> {
            sendOldData(testData);
            sendDataDialog.dismiss();
        });
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
                            testDataList.remove(testData);
                            inc_and_check(testDataList);

                        }
                    }
                    else{
                        synchronized (this){
                            inc_and_check(testDataList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    synchronized (this){
                        inc_and_check(testDataList);
                    }
                }
            });
        }
    }

    private void inc_and_check(List<TestData> testDataList) {
        savedTestDataCount++;
        checkIfFinished(totalTests, savedTestDataCount, testDataList);
    }

    private void checkIfFinished(int totalTests, int savedTestDataCount, List<TestData> testDataList) {
        if(savedTestDataCount != totalTests)
            return;
        Type listOfTestItems = new TypeToken<List<TestData>>(){}.getType();
        Preferences.getInstance(this).saveItem(Keys.TEST_DATA, testDataList, listOfTestItems);
        if(!testDataList.isEmpty())
            AndroidUtils.showDialogue("Network problem!\nData will be saved later", binding.getRoot());
        else
            AndroidUtils.showDialogue("All test data has been saved!", binding.getRoot());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.frank_test);
        initial_position = new RotationVector();
        setButtonEvents();
        showSendingDataDialogue();
    }

    private void setButtonEvents() {
        binding.startTest.setOnClickListener(view -> startTest(false));
        binding.practiceTest.setOnClickListener(view -> startTest(true));

    }


    private void startTest(boolean isPractice) {
        if((binding.candidateId.getText().length() != ID_LENGTH) && ! isPractice){
            binding.candidateId.setError(getString(R.string.id_message));
            return;
        }
        Intent show_test_intent = new Intent(this, ShowTestActivity.class);
        show_test_intent.putExtra(Keys.INITIAL_POSITIOIN, initial_position);
        show_test_intent.putExtra(Keys.CANDIDATE_ID, binding.candidateId.getText().toString());
        show_test_intent.putExtra(Keys.IS_PRACTICE, isPractice);
        startActivity(show_test_intent);
    }

}
