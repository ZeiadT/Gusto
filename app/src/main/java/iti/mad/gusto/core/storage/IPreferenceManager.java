package iti.mad.gusto.core.storage;

import java.util.Set;

public interface IPreferenceManager {
    // String
    void putString(String key, String value);
    String getString(String key, String defaultValue);

    // Integer
    void putInt(String key, int value);
    int getInt(String key, int defaultValue);

    // Long
    void putLong(String key, long value);
    long getLong(String key, long defaultValue);

    // Float
    void putFloat(String key, float value);
    float getFloat(String key, float defaultValue);

    // Boolean
    void putBoolean(String key, boolean value);
    boolean getBoolean(String key, boolean defaultValue);

    // String Set
    void putStringSet(String key, Set<String> values);
    Set<String> getStringSet(String key, Set<String> defaultValues);

    // Maintenance
    boolean contains(String key);
    void remove(String key);
    void clear();
}
