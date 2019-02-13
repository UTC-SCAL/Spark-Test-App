package com.example.injuries;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.injuries.databinding.SingleScoreFileBinding;
import com.example.injuries.pojos.TestSample;

public class TestResultViewHolder extends RecyclerView.ViewHolder {
    SingleScoreFileBinding binding;
    public TestResultViewHolder(SingleScoreFileBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(TestSample testSample){
        binding.testGroup.setText(testSample.getGroup());
        if(!testSample.isResultCorrect()){
            binding.isAnswerCorrect.setImageResource(R.mipmap.cross);
            binding.isAnswerCorrect.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(),
                    android.R.color.holo_green_light), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else{
            binding.isAnswerCorrect.setImageResource(R.mipmap.check);
            binding.isAnswerCorrect.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(),
                    android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        binding.responseTime.setText(testSample.getResponse_time() + " ms");
    }
}
