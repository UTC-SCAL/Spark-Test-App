package com.example.injuries;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.example.injuries.databinding.ActivityShowTestBinding;
import com.example.injuries.pojos.TestSample;

public class ShowTestActivity extends MotionSensorActivity{
    public static final int MAX_TESTS_NUMBER = 5;
    private static final double CUM_MAX = 4.0;
    ActivityShowTestBinding binding;


    private int remaining_tests = MAX_TESTS_NUMBER;
    private boolean within_test_period = false;
    private double previous_angle;
    private double cum_diff = 0;
    private long sample_starting_time = 0;
    private TestSample[] testSamples = new TestSample[5];

    private double MIN_ANGLE = 50;
    private double MAX_ANGLE = 70;


    private String arrow_combinations[] = {
            "▶ ▶ ◀ ◀ ◀",
            "▶ ▶ ▶ ▶ ▶",
            "◀ ◀ ▶ ◀ ◀",
            "▶ ▶ ◀ ◀ ◀",
            "◀ ◀ ▶ ◀ ◀"
    };

    private boolean[] isLeft = {
      true,
      false,
      false,
      true,
      false
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_test);
        binding.toolbar.setTitle(R.string.frank_test);
        initlizeTestSamples();
        setTimerSettings();
        setListeners();



    }

    private void initlizeTestSamples() {
        for(int i = 0; i < testSamples.length; i++){
            testSamples[i] = new TestSample();
        }
    }

    private void setListeners() {
        binding.performTestAgain.setOnClickListener(view -> {
            binding.performTestAgain.setVisibility(View.GONE);
            remaining_tests = 5;
            show_test_sample();
        });
    }

    private void setTimerSettings() {
        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                binding.timer.setVisibility(View.GONE);
                binding.testArea.setVisibility(View.VISIBLE);
                show_test_sample();
            }
        }.start();

    }

    private void show_test_sample(){
        binding.testArea.setVisibility(View.VISIBLE);
        int sample_number = get_random_sample_number();
        within_test_period = true;
        sample_starting_time = System.currentTimeMillis();
        binding.testArea.setText(arrow_combinations[sample_number % arrow_combinations.length]);
        new CountDownTimer(300, 100){

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                long waiting_time = get_random_waiting_time();
                binding.testArea.setText("Waiting for the user response ...");
                new Handler().postDelayed(() -> {
                    within_test_period = false;
                    remaining_tests --;
                    if(remaining_tests != 0){
                        show_test_sample();
                    }
                    else{
                        binding.testArea.setVisibility(View.GONE);
                        binding.performTestAgain.setVisibility(View.VISIBLE);
                        for(TestSample testSample: testSamples){
                            testSample.showInfo();
                        }

                    }
                }, waiting_time);

            }
        }.start();
    }

    private long get_random_waiting_time() {
        return (long) (Math.random() * 2000 + 2000);
    }

    private int get_random_sample_number() {
        return (int) Math.ceil(Math.random() * arrow_combinations.length);
    }

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        if(!within_test_period){
            cum_diff = 0;
            previous_angle = angle;
        }
        else{
            double angle_change = angle - previous_angle;
            cum_diff += angle_change;
            Log.i("testing_activity", "" + angle_change + "," + angle);
            if((cum_diff > CUM_MAX && angle > MAX_ANGLE) ||
                    (cum_diff < - CUM_MAX && angle < MIN_ANGLE)){
                vibrate();
                long response_time = System.currentTimeMillis() - sample_starting_time;
                int testSampleIndex = remaining_tests-1;
                Log.i("vibration_bug", testSampleIndex + "");
                testSamples[testSampleIndex].setResponse_time(response_time);
                boolean testResult = (isLeft[testSampleIndex] && (cum_diff < - CUM_MAX)) ||
                        !isLeft[testSampleIndex] && (cum_diff > CUM_MAX);
                testSamples[testSampleIndex].setResultCorrect(testResult);
                previous_angle = angle;
                cum_diff = 0;
            }
        }
    }

    private void vibrate() {
        Log.i("testing_activity", "vibration");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
    }
}
