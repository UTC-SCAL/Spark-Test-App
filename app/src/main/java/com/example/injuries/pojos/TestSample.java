package com.example.injuries.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class TestSample implements Parcelable {
    private long response_time; //time in msec
    private boolean isResultCorrect = false;

    public TestSample() {
        this.response_time = -1;
        this.isResultCorrect = false;
    }

    protected TestSample(Parcel in) {
        response_time = in.readLong();
        isResultCorrect = in.readByte() != 0;
    }

    public static final Creator<TestSample> CREATOR = new Creator<TestSample>() {
        @Override
        public TestSample createFromParcel(Parcel in) {
            return new TestSample(in);
        }

        @Override
        public TestSample[] newArray(int size) {
            return new TestSample[size];
        }
    };

    public long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(long response_time) {
        this.response_time = response_time;
    }

    public boolean isResultCorrect() {
        return isResultCorrect;
    }

    public void setResultCorrect(boolean resultCorrect) {
        isResultCorrect = resultCorrect;
    }

    public void showInfo() {
        Log.i("test_sample", "(time, res) = (" + response_time + ", " + isResultCorrect + ")");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(response_time);
        dest.writeByte((byte) (isResultCorrect? 1:0));
    }
}
