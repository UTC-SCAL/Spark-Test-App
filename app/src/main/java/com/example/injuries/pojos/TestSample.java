package com.example.injuries.pojos;

public class TestSample {
    private long response_time; //time in msec
    private boolean isResultCorrect = false;

    public TestSample() {
        this.response_time = -1;
        this.isResultCorrect = false;
    }

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
}
