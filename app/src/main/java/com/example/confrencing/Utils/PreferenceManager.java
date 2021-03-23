package com.example.confrencing.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences preferences;

    public PreferenceManager(Context context){
        preferences = context.getSharedPreferences(Constants.PREFERENCE_MANAGER,Context.MODE_PRIVATE);
    }


    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBoolean(String key , Boolean defValue){
        return preferences.getBoolean(key,false);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key , String defValue){
        return preferences.getString(key,null);
    }

    public void clearPreferences(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
