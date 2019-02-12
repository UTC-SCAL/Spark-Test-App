package com.example.injuries.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSamplesContainer implements Iterable<TestSample>, Parcelable {
    private List<TestSample> testSamples;

    public TestSamplesContainer(int maxSize) {
        this.testSamples = new ArrayList<>(maxSize);
        for(int i = 0 ; i < maxSize; i++){
            testSamples.add(i, new TestSample());
        }
    }

    protected TestSamplesContainer(Parcel in) {
        testSamples = in.createTypedArrayList(TestSample.CREATOR);
    }

    public static final Creator<TestSamplesContainer> CREATOR = new Creator<TestSamplesContainer>() {
        @Override
        public TestSamplesContainer createFromParcel(Parcel in) {
            return new TestSamplesContainer(in);
        }

        @Override
        public TestSamplesContainer[] newArray(int size) {
            return new TestSamplesContainer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(testSamples);
    }

}
