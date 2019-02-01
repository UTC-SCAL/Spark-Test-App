package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.injuries.databinding.ActivityMainBinding;
import com.example.injuries.databinding.ActivityShowTestBinding;

public class ShowTestActivity extends AppCompatActivity {
    ActivityShowTestBinding binding;
    private int remaining_tests = 5;
    private String arrow_combinations[] = {
            "▶ ▶ ▶ ◀ ◀",
            "▶ ▶ ▶ ▶ ▶",
            "◀ ◀ ▶ ◀ ◀",
            "▶ ▶ ◀ ◀ ◀",
            "◀ ◀ ▶ ◀ ◀"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_test);
        setTimerSettings();
        binding.seeResults.setOnClickListener(view -> {
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
        int sample_number = (int) Math.ceil(Math.random() * arrow_combinations.length);
        binding.testArea.setText(arrow_combinations[sample_number % arrow_combinations.length]);
        new CountDownTimer(2000, 100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                remaining_tests --;
                long waiting_time = (long) (Math.random() * 2000 + 2000); //between two and four seconds
                binding.testArea.setText("Waiting for the user response ...");
                new Handler().postDelayed(() -> {
                    if(remaining_tests != 0){
                        show_test_sample();
                    }
                    else{
                        binding.testArea.setVisibility(View.GONE);
                        binding.seeResults.setVisibility(View.VISIBLE);

                    }
                }, waiting_time);

            }
        }.start();
    }

}
