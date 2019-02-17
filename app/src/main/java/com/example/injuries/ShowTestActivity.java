package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.injuries.databinding.ActivityShowTestBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.RotationVector;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.pojos.TestSamplesContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.injuries.utils.AndroidUtils.vibrate;

public class ShowTestActivity extends MotionSensorActivity{

    public static final int MAX_TESTS_NUMBER = 20;
    public static final int THRESHOLD = 20;
    public static final int GROUP_SHOWING_TIME_MS = 300;
    public static final int WAITING_TIME_RANDOMIZATION_STEP = 500;
    public static final int MSC_PER_SEC = 1000;
    private List<Integer> indices;
    public static final int STARTING_WAITING_TIME = 6000;
    private static final double ONE_SEC = 1000;
    RotationVector initial_position;
    private int remaining_tests = MAX_TESTS_NUMBER;
    private boolean within_test_period = false;
    private long sample_starting_time = 0;
    private int current_sample_number;
    private TestSamplesContainer testSamplesContainer;
    List<RotationVector> last_rotation_vectors;



    ActivityShowTestBinding binding;



    private String arrow_combinations[] = {
            "< < < < <", //left cong.
            "> > > > >", //right cong.
            "> > < > >", //left inc.
            "< < > > >", //right inc.
    };

    private boolean[] isLeft = {
      true,
      false,
      true,
      false,
    };


    private void initialize_samples_order(){
        indices = new ArrayList<>();
        for(int i = 0; i < MAX_TESTS_NUMBER; i++)
            indices.add(i % arrow_combinations.length);
        Collections.shuffle(indices);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_test);
        binding.toolbar.setTitle(R.string.frank_test);
        testSamplesContainer = new TestSamplesContainer(MAX_TESTS_NUMBER);
        initial_position = getIntent().getExtras().getParcelable(Keys.INITIAL_POSITIOIN);
        last_rotation_vectors = new ArrayList<>();
        initialize_samples_order();
        setTimerSettings();
        setListeners();
    }


    private void setListeners() {
        binding.performTestAgain.setOnClickListener(view -> {
            binding.performTestAgain.setVisibility(View.GONE);
            remaining_tests = MAX_TESTS_NUMBER;
            show_test_sample();
        });
    }

    private void setTimerSettings() {
        new CountDownTimer(STARTING_WAITING_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.timer.setText("" + millisUntilFinished / MSC_PER_SEC);
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
        current_sample_number = indices.get(remaining_tests -1);
        indices.remove(remaining_tests -1);
        int groupIndex = remaining_tests - 1;
        testSamplesContainer.setGroup(groupIndex, arrow_combinations[current_sample_number]);
        within_test_period = true;
        sample_starting_time = System.currentTimeMillis();
        binding.testArea.setText(arrow_combinations[current_sample_number]);
        new CountDownTimer(GROUP_SHOWING_TIME_MS, 100){

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
                        Log.i("testing_activity", "__________________________");
                        show_test_sample();
                    }
                    else{
                        for(TestSample testSample: testSamplesContainer){
                            Log.i("testing_activity", "" + testSample.getResponse_time() + " " + testSample.isResultCorrect());
                        }
                        showResult();
                    }
                }, waiting_time);

            }
        }.start();
    }

    private void showResult() {

        Intent resultIntent = new Intent(ShowTestActivity.this, TestResultShowerActivity.class);
        resultIntent.putExtra(Keys.SAMPLES_CONTAINER, testSamplesContainer);
        startActivity(resultIntent);
    }

    private long get_random_waiting_time() {
        return (long) (Math.random() * ONE_SEC + WAITING_TIME_RANDOMIZATION_STEP);
    }

    private int get_random_sample_number() {
        return (int) Math.floor(Math.random() * arrow_combinations.length);
    }

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {

            double x_diff = initial_position.getX() - x;
            double y_diff = initial_position.getY() - y;
            double z_diff = initial_position.getZ() - z;

            last_rotation_vectors.add(new RotationVector(x, y, z, angle));
            double corrected_x_diff = get_corrected_diff(last_rotation_vectors);


            //TODO solve the calibration problem

        Log.i("testing_activity", "" + x_diff +  "," + y_diff  + ", " + z_diff);
            if(!within_test_period)
                return;
            if((corrected_x_diff > THRESHOLD) || corrected_x_diff < -THRESHOLD){
                vibrate(this);
                long response_time = System.currentTimeMillis() - sample_starting_time;
                testSamplesContainer.setResponseTime(remaining_tests-1, response_time);
                boolean testResult = (isLeft[current_sample_number] && (corrected_x_diff > THRESHOLD)) ||
                        !isLeft[current_sample_number] && (corrected_x_diff < -THRESHOLD);
                testSamplesContainer.setResultCorrect(remaining_tests-1, testResult);
                Log.i("testing_activity", "" + testResult +  "," + isLeft[current_sample_number]);
                within_test_period = false;
                last_rotation_vectors.clear();
            }
        }

    private double get_corrected_diff(List<RotationVector> last_rotation_vectors) {

        List<Double> last_five_differences = new ArrayList<>(last_rotation_vectors.size());
        if(last_rotation_vectors.size() < 5)
            return 0;
        for(int i = last_rotation_vectors.size() - 1; i > (last_rotation_vectors.size() - 6) ; i--) {
            last_five_differences.add(initial_position.getX() - last_rotation_vectors.get(i).getX());
        }
        double average = calculate_average(last_five_differences);
        for(Double diff : last_five_differences){
            if(Math.abs(diff) > Math.abs(average) + THRESHOLD) {
                last_five_differences.remove(diff);
                return 0;
            }
        }
        return calculate_average(last_five_differences);
    }

    private double calculate_average(List<Double> differences) {
        double sum = 0;
        for(Double value: differences)
            sum += value;
        return sum / differences.size();
    }
}