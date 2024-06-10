package com.vikram.xlauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

  private static final String PREF_NAME = "xlauncher.preference";
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  public PreferenceManager(Context context) {
    sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  // Store a String value
  public void put(String key, String value) {
    editor.putString(key, value);
    editor.apply();
  }

  // Retrieve a String value
  public String get(String key, String defaultValue) {
    return sharedPreferences.getString(key, defaultValue);
  }

  // Store an int value
  public void put(String key, int value) {
    editor.putInt(key, value);
    editor.apply();
  }

  // Retrieve an int value
  public int get(String key, int defaultValue) {
    return sharedPreferences.getInt(key, defaultValue);
  }

  // Store a boolean value
  public void put(String key, boolean value) {
    editor.putBoolean(key, value);
    editor.apply();
  }

  // Retrieve a boolean value
  public boolean get(String key, boolean defaultValue) {
    return sharedPreferences.getBoolean(key, defaultValue);
  }

  // Store a float value
  public void put(String key, float value) {
    editor.putFloat(key, value);
    editor.apply();
  }

  // Retrieve a float value
  public float get(String key, float defaultValue) {
    return sharedPreferences.getFloat(key, defaultValue);
  }

  // Store a long value
  public void put(String key, long value) {
    editor.putLong(key, value);
    editor.apply();
  }

  // Retrieve a long value
  public long get(String key, long defaultValue) {
    return sharedPreferences.getLong(key, defaultValue);
  }

  // Clear all data in SharedPreferences
  public void clear() {
    editor.clear();
    editor.apply();
  }

  // Remove a specific key-value pair
  public void remove(String key) {
    editor.remove(key);
    editor.apply();
  }
}
