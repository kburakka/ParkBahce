package com.example.msi.demoo.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPrefs {
    //We had to use the commit() method to commit our changes.
    // There’s another method to do the same which is apply() but that is asynchronous and won’t report failures.

    public static boolean hasShared(Activity activity,String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//activity.getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.contains(key))
            return true;
        else
            return false;
    }

    public static String getShared(Activity activity,String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key,null);
    }

    public static void setShared(Activity activity, String key, String val){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.apply();
//        editor.commit();
    }

    public static void removeShared(Activity activity,String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());//activity.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().remove(key).apply();
    }
}