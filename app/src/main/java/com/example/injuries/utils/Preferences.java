package com.example.injuries.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

public class Preferences {
    public static final String APP_KEY = "APP_KEY";
    private static Preferences preferences;
    private SharedPreferences sharedPreferences;
    public Preferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context){
        if(preferences == null)
            preferences = new Preferences(context);
        return preferences;
    }


    public <T> void saveItem(String key, T item, Class itemClass){

        Gson gson = new Gson();
        String serialized = gson.toJson(item, itemClass);
        preferences.sharedPreferences.edit().putString(key, serialized).apply();
    }

    public <T> T getSavedItem(String key, Class itemClass){
        Gson gson = new Gson();
        String serialized = preferences.sharedPreferences.getString(key, "");
        if(TextUtils.isEmpty(serialized))
            return null;
        return (T) gson.fromJson(serialized, itemClass);
    }

    public void clearCache(){
        sharedPreferences.edit().clear().apply();
    }
}

