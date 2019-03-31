package com.example.injuries.apis;

import com.example.injuries.pojos.TestSample;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestData {

    @SerializedName("candidate_id")
    @Expose
    private String id;

    @SerializedName("test_id")
    @Expose
    private String test_id;

    @SerializedName("test_entries")
    @Expose
    private List<TestSample> testSamples;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setTestSamples(List<TestSample> testSamples) {
        this.testSamples = testSamples;
    }

    public List<TestSample> getTestSamples() {
        return testSamples;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }
}
