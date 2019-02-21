package com.example.injuries;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.injuries.databinding.ActivityMainBinding;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.RotationVector;

import static com.example.injuries.utils.AndroidUtils.playSound;


public class MainActivity extends MotionSensorActivity{


    ActivityMainBinding binding;
    RotationVector initial_position;
    float position_update_rate = 0.8f;

    @Override
    protected void onRotationChanged(double x, double y, double z, double angle) {
        initial_position.update(position_update_rate, x, y, z, angle);
        binding.textHolder.setText((int)x + "     " + (int)y + "     " + (int)z + "     ");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
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
        Intent show_test_intent = new Intent(this, ShowTestActivity.class);
        show_test_intent.putExtra(Keys.INITIAL_POSITIOIN, initial_position);
        startActivity(show_test_intent);
    }

}
