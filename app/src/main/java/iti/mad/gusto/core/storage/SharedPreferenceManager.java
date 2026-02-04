package iti.mad.gusto.core.storage;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;


public class SharedPreferenceManager implements IPreferenceManager {
    private static final String PREFS_NAME = "gusto_prefs";

    private final SharedPreferences sharedPreferences;
    private static SharedPreferenceManager instance;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPreferenceManager getInstance(Context context) {

        synchronized (SharedPreferenceManager.class) {
            if (instance == null) {
                instance = new SharedPreferenceManager(context);
            }

            return instance;
        }
    }

    @Override
    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();

    }

    @Override
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    @Override
    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void putStringSet(String key, Set<String> values) {
        sharedPreferences.edit().putStringSet(key, values).apply();
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        Set<String> values = sharedPreferences.getStringSet(key, defaultValues);
        values = values == null ? Collections.emptySet() : values;

        return Set.copyOf(values);
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);

    }

    @Override
    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();

    }

    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();

    }
}
