package com.example.injuries.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("test_entries")
    @Expose
    private List<>


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
