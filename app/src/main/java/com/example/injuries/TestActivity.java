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
import com.example.injuries.pojos.TestSamplesContainer;
import com.example.injuries.utils.VectorsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.injuries.utils.AndroidUtils.playSound;
import static com.example.injuries.utils.AndroidUtils.vibrate;

public class TestActivity extends MotionSensorActivity {

    public static int maxTestsNumber = 20;
    public static final int THRESHOLD = 2;
    public static final int GROUP_SHOWING_TIME_MS = 300;
    public static final int WAITING_TIME_RANDOMIZATION_STEP = 1000;
    public static final int MSC_PER_SEC = 1000;
    private static final int TEST_ACCURACY_SIZE = 3;
    public static final float INITIAL_POSITION_UPDATE_RATE = 1f;
    public static final String FIRST_TEST = "1";
    private List<Integer> indices;
    public static final int STARTING_WAITING_TIME = 6000;
    private static final long ONE_SEC = 1000;
    RotationVector initial_position;
    RotationVector updated_initial_position;
    private int remaining_tests;
    private long sample_starting_time = 0;
    private int current_sample_number;
    private TestSamplesContainer testSamplesContainer;
    VectorsList last_rotation_vectors = new VectorsList(TEST_ACCURACY_SIZE);


    ActivityShowTestBinding binding;


    private String arrow_combinations[] = {
            "<<<<<", //left con.
            ">>>>>", //right con.
            ">><>>", //left inc.
            "<<><<", //right inc.
    };

    private boolean[] isRight = {
            false,
            true,
            false,
            true,
    };


    private boolean[] isCongurent = {
      true,
      true,
      false,
      false
    };
    private String candidate_id;
    private Handler response_limit_handler;
    private Runnable maxTimeRunnable;
    private boolean withinTest = false;
    private boolean isPractice;
    private boolean testDisabled = false;


    private void initialize_samples_order() {
        indices = new ArrayList<>();
        for (int i = 0; i < maxTestsNumber; i++)
            indices.add(i % arrow_combinations.length);
        Collections.shuffle(indices);
    }


    @Override
    protected void onStart() {
        super.onStart();
        initial_position = getIntent().getExtras().getParcelable(Keys.INITIAL_POSITIOIN);
        candidate_id = getIntent().getExtras().getString(Keys.CANDIDATE_ID);
        isPractice = getIntent().getExtras().getBoolean(Keys.IS_PRACTICE);
        updated_initial_position = new RotationVector(initial_position);
        if(isPractice)
            maxTestsNumber = 10;
        else
            maxTestsNumber = 20;
        testSamplesContainer = new TestSamplesContainer(maxTestsNumber);
        remaining_tests = maxTestsNumber;
        initialize_samples_order();
        setListeners();
        setTimerSettings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_test);
    }


    private void setListeners() {
        binding.performTestAgain.setOnClickListener(view -> {
            binding.performTestAgain.setVisibility(View.GONE);
            remaining_tests = maxTestsNumber;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        killHandler();
        testDisabled = true;
        finish();
    }

    private void show_test_sample() {
        if(testDisabled)
            return;
        withinTest = true;
        binding.testArea.setVisibility(View.VISIBLE);
        int sample_index = remaining_tests - 1;
        current_sample_number = indices.get(sample_index);
        indices.remove(sample_index);
        sample_starting_time = System.currentTimeMillis();
        binding.testArea.setText(arrow_combinations[current_sample_number]);
        initial_position = new RotationVector(updated_initial_position);
        Log.i("init_pos", "" + initial_position.getX());

        response_limit_handler = new Handler();
        maxTimeRunnable = maxTimeRunnable();
        //run the vibration after 2 seconds from hiding the group
        response_limit_handler.postDelayed(maxTimeRunnable, 2 * ONE_SEC + GROUP_SHOWING_TIME_MS);
        //hide the group after GROUP_SHOWING_TIME_MS
        new Handler().postDelayed(this::emptyTestArea, GROUP_SHOWING_TIME_MS);
    }

    private Runnable maxTimeRunnable() {
        return () -> {
            vibrate(this);
            setTestSampleValues(false);
            applyUserResponse();
        };
    }


    private void applyUserResponse() {
        withinTest = false;
        emptyTestArea();
        remaining_tests--;
        if (remaining_tests != 0)
            new Handler().postDelayed(this::show_test_sample, get_random_waiting_time());
        else
            showResult();
    }

    private void emptyTestArea() {
        binding.testArea.setText("");
    }

    private void showResult() {
        Intent resultIntent = new Intent(TestActivity.this, ResultShowerActivity.class);
        resultIntent.putExtra(Keys.SAMPLES_CONTAINER, testSamplesContainer);
        resultIntent.putExtra(Keys.CANDIDATE_ID, candidate_id);
        resultIntent.putExtra(Keys.TEST_ID, FIRST_TEST);
        resultIntent.putExtra(Keys.IS_PRACTICE, isPractice);
        startActivity(resultIntent);
    }



    private long get_random_waiting_time() {
        return (long) (WAITING_TIME_RANDOMIZATION_STEP  * Math.random() + 500 );
    }


    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        super.onRotationChanged(x, y, z, angle);
        updated_initial_position.update(INITIAL_POSITION_UPDATE_RATE, x, y, z, angle);
        last_rotation_vectors.add(new RotationVector(x, y, z, angle));
        if(!withinTest)
            return;
        double corrected_x_diff = get_corrected_diff(last_rotation_vectors);

        if (Math.abs(corrected_x_diff) > THRESHOLD) {
            playSound(this);
            Log.i("corrected_x_diff", " = " + corrected_x_diff);
            boolean testResult = (isRight[current_sample_number] && (corrected_x_diff > 0)) ||
                    !isRight[current_sample_number] && (corrected_x_diff < 0);
            Log.i("rotation_values", "res = " + testResult + ", diff= " + corrected_x_diff);
            setTestSampleValues(testResult);
            last_rotation_vectors.clear();
            killHandler();
            applyUserResponse();
        }
    }

    private void killHandler() {
        if(response_limit_handler != null && maxTimeRunnable != null)
            response_limit_handler.removeCallbacks(maxTimeRunnable);
    }

    private void setTestSampleValues(boolean testResult) {
        int groupIndex = remaining_tests - 1;
        long response_time = System.currentTimeMillis() - sample_starting_time;
        testSamplesContainer.setResponseTime( groupIndex, response_time);
        testSamplesContainer.setResultCorrect(groupIndex, testResult);
        testSamplesContainer.setCongruent(groupIndex, isCongurent[current_sample_number]);
        testSamplesContainer.setLeft(groupIndex, isRight[current_sample_number]);
    }

    private double get_corrected_diff(List<RotationVector> last_rotation_vectors) {

        List<Double> N_differences = new ArrayList<>(TEST_ACCURACY_SIZE);
        if (last_rotation_vectors.size() < TEST_ACCURACY_SIZE)
            return 0;
        for (RotationVector vector: last_rotation_vectors)
            N_differences.add(vector.getX() - initial_position.getX());
        return calculate_average(N_differences);
    }

    private double calculate_average(List<Double> differences) {
        double sum = 0;
        for (double value : differences)
            sum += value;
        return sum / differences.size();
    }
}