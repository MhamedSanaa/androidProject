package com.example.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Collection;
import java.util.HashSet;

import models.User;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "Session";
    String SESSION_KEY = "UserId";
    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user,String id){
        editor.putString(SESSION_KEY,id).commit();
    }

    public String getSession(){
        return sharedPreferences.getString(SESSION_KEY, "NaN");
    }
}
