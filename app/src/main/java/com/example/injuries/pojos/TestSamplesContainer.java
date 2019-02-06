package com.example.injuries.pojos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSamplesContainer implements Iterable<TestSample>{
    private List<TestSample> testSamples;

    public TestSamplesContainer(int maxSize) {
        this.testSamples = new ArrayList<>(maxSize);
        for(int i = 0 ; i < maxSize; i++){
            testSamples.set(i, new TestSample());
        }
    }

    public void setResponseTime(int index, long response_time){
        testSamples.get(index).setResponse_time(response_time);
    }

    public void setResultCorrect(int index, boolean isResultCorrect){
        testSamples.get(index).setResultCorrect(isResultCorrect);
    }

    @Override
    public Iterator<TestSample> iterator() {
        return testSamples.iterator();
    }
}
