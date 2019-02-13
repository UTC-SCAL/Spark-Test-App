package com.example.injuries;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.injuries.databinding.SingleScoreFileBinding;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.pojos.TestSamplesContainer;

public class ShowResultAdapter extends RecyclerView.Adapter<TestResultViewHolder> {

    private TestSamplesContainer container;

    public ShowResultAdapter(TestSamplesContainer container) {
        this.container = container;
    }


    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        SingleScoreFileBinding binding = SingleScoreFileBinding.inflate(inflater);
        return new TestResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder testResultViewHolder, int i) {
        TestSample testSample = container.getAt(i);
        testResultViewHolder.bind(testSample);

    }

    @Override
    public int getItemCount() {
        if(container != null)
            return container.getSize();
        return 0;
    }
}
