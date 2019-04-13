package com.example.injuries.apis;

import android.view.View;

import com.example.injuries.TestResultShowerActivity;
import com.example.injuries.global.Keys;
import com.example.injuries.pojos.TestSample;
import com.example.injuries.utils.AndroidUtils;
import com.example.injuries.utils.Preferences;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestData {

    @SerializedName("candidate_id")
    @Expose
    private String id;

    @SerializedName("test_instance_id")
    @Expose
    private int testInstanceId;

    @SerializedName("test_time")
    @Expose
    private long  testTime;


    @SerializedName("test_id")
    @Expose
    private String test_id;

    @SerializedName("test_entries")
    @Expose
    private List<TestSample> testSamples;


    public TestData(){
        this.testTime = System.currentTimeMillis();
    }
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        return ((TestData) obj).testInstanceId == this.testInstanceId;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }



}
