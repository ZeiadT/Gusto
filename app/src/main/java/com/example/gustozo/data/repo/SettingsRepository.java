package com.example.gustozo.data.repo;

import android.content.Context;

import com.example.gustozo.data.source.SettingsLocalDatasource;

public class SettingsRepository {
    private final SettingsLocalDatasource settingsLocalDatasource;

    private static SettingsRepository instance;

    private SettingsRepository(Context context) {
        settingsLocalDatasource = SettingsLocalDatasource.getInstance(context);
    }

    public static SettingsRepository getInstance(Context context) {
        synchronized (SettingsRepository.class) {

            if (instance == null) {
                instance = new SettingsRepository(context);
            }
            return instance;
        }
    }

    public void setRememberMe(boolean rememberMe) {
        settingsLocalDatasource.setRememberMe(rememberMe);
    }

    public boolean getRememberMe() {
        return settingsLocalDatasource.getRememberMe();
    }

    public void setLang(String lang) {
        settingsLocalDatasource.setLang(lang);
    }

    public String getLang() {
        return settingsLocalDatasource.getLang();
    }

    public void setTheme(int theme) {
        settingsLocalDatasource.setTheme(theme);
    }

    public int getTheme() {
        return settingsLocalDatasource.getTheme();
    }

    public void setSkipBoarding(boolean shouldSkipBoarding) {
        settingsLocalDatasource.setSkipBoarding(shouldSkipBoarding);
    }

    public boolean shouldSkipBoarding() {
        return settingsLocalDatasource.shouldSkipBoarding();
    }
}
