package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.injuries.databinding.ActivityShowTestBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.RotationVector;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.pojos.TestSamplesContainer;
import com.example.injuries.utils.VectorsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.injuries.utils.AndroidUtils.playSound;

public class ShowTestActivity extends MotionSensorActivity {

    public static final int MAX_TESTS_NUMBER = 7;
    public static final int THRESHOLD = 15; // in degrees
    public static final int GROUP_SHOWING_TIME_MS = 300;
    public static final int WAITING_TIME_RANDOMIZATION_STEP = 1000;
    public static final int MSC_PER_SEC = 1000;
    private static final int TEST_ACCURACY_SIZE = 2;
    private List<Integer> indices;
    public static final int STARTING_WAITING_TIME = 6000;
    private static final double ONE_SEC = 1000;
    RotationVector initial_position;
    private int remaining_tests = MAX_TESTS_NUMBER;
    private boolean within_test_period = false;
    private long sample_starting_time = 0;
    private int current_sample_number;
    private TestSamplesContainer testSamplesContainer;
    VectorsList last_rotation_vectors = new VectorsList(TEST_ACCURACY_SIZE);


    ActivityShowTestBinding binding;


    private String arrow_combinations[] = {
            "< < < < <", //left cong.
            "> > > > >", //right cong.
            "> > < > >", //left inc.
            "< < > > >", //right inc.
    };

//    private boolean[] isLeft = {
//            true,
//            false,
//            true,
//            false,
//    };

//    this is wrong
    private boolean[] isLeft = {
            true,
            true,
            true,
            true,
    };

    private boolean[] isCongurent = {
      true,
      true,
      false,
      false
    };


    private void initialize_samples_order() {
        indices = new ArrayList<>();
        for (int i = 0; i < MAX_TESTS_NUMBER; i++)
            indices.add(i % arrow_combinations.length);
        Collections.shuffle(indices);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_test);
        testSamplesContainer = new TestSamplesContainer(MAX_TESTS_NUMBER);
        initial_position = getIntent().getExtras().getParcelable(Keys.INITIAL_POSITIOIN);
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

    private void show_test_sample() {
        binding.testArea.setVisibility(View.VISIBLE);
        int sample_index = remaining_tests - 1;
        current_sample_number = indices.get(sample_index);
        indices.remove(sample_index);
        testSamplesContainer.setGroup(sample_index, arrow_combinations[current_sample_number]);

        within_test_period = true;
        sample_starting_time = System.currentTimeMillis();
        binding.testArea.setText(arrow_combinations[current_sample_number]);
        new CountDownTimer(GROUP_SHOWING_TIME_MS, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                long waiting_time = get_random_waiting_time();
                binding.testArea.setText("");
                new Handler().postDelayed(() -> {
                    within_test_period = false;
                    remaining_tests--;
                    if (remaining_tests != 0) {
                        show_test_sample();
                    } else {
                        for (TestSample testSample : testSamplesContainer) {
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
        finish();
    }

    private long get_random_waiting_time() {
        return (long) (Math.random() * ONE_SEC + WAITING_TIME_RANDOMIZATION_STEP);
    }


    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        last_rotation_vectors.add(new RotationVector(x, y, z, angle));
        double corrected_x_diff = get_corrected_diff(last_rotation_vectors);

        if (!within_test_period)
            return;
        if (Math.abs(corrected_x_diff) > THRESHOLD) {
            playSound(this);
            Log.i("corrected_x_diff", " = " + corrected_x_diff);
            boolean testResult = (isLeft[current_sample_number] && (corrected_x_diff > 0)) ||
                    !isLeft[current_sample_number] && (corrected_x_diff < 0);
            setTestSampleValues(testResult);
            within_test_period = false;
            last_rotation_vectors.clear();

        }
    }

    private void setTestSampleValues(boolean testResult) {
        int groupIndex = remaining_tests - 1;
        long response_time = System.currentTimeMillis() - sample_starting_time;
        testSamplesContainer.setResponseTime( groupIndex, response_time);
        testSamplesContainer.setResultCorrect(groupIndex, testResult);
        testSamplesContainer.setCongruent(groupIndex, isCongurent[current_sample_number]);
        testSamplesContainer.setLeft(groupIndex, isLeft[current_sample_number]);
    }

    private double get_corrected_diff(List<RotationVector> last_rotation_vectors) {

        List<Double> N_differences = new ArrayList<>(TEST_ACCURACY_SIZE);
        if (last_rotation_vectors.size() < TEST_ACCURACY_SIZE)
            return 0;
        for (RotationVector vector: last_rotation_vectors)
            N_differences.add(vector.getX() - initial_position.getX());
        double average = calculate_average(N_differences);
        if(Math.abs(average) > 3 * THRESHOLD)
            return 0;
        return average;
    }

    private double calculate_average(List<Double> differences) {
        double sum = 0;
        for (double value : differences)
            sum += value;
        return sum / differences.size();
    }
}