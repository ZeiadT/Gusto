package iti.mad.gusto.data.source;

import android.content.Context;

import iti.mad.gusto.core.storage.IPreferenceManager;
import iti.mad.gusto.core.storage.SharedPreferenceManager;

public class SettingsLocalDatasource {
    private final IPreferenceManager preferenceManager;
    private static SettingsLocalDatasource instance;
    private static final String REMEMBER_ME_KEY = "rememberMe";
    private static final String SKIP_BOARDING_KEY = "skipBoarding";
    private static final String LANG_KEY = "lang";
    private static final String THEME_KEY = "theme";


    private SettingsLocalDatasource(Context context) {
        preferenceManager = SharedPreferenceManager.getInstance(context);
    }

    public static SettingsLocalDatasource getInstance(Context context) {
        synchronized (SettingsLocalDatasource.class) {

            if (instance == null) {
                instance = new SettingsLocalDatasource(context);
            }
            return instance;
        }
    }

    public void setRememberMe(boolean rememberMe) {
        preferenceManager.putBoolean(REMEMBER_ME_KEY, rememberMe);
    }

    public boolean getRememberMe() {
        return preferenceManager.getBoolean(REMEMBER_ME_KEY, false);
    }

    public void setLang(String lang) {
        preferenceManager.putString(LANG_KEY, lang);
    }

    public String getLang() {
        return preferenceManager.getString(LANG_KEY, "en");
    }

    public void setTheme(int theme) {
        preferenceManager.putInt(THEME_KEY, theme);
    }

    public int getTheme() {
        return preferenceManager.getInt(THEME_KEY, 0);
    }

    public void setSkipBoarding(boolean shouldSkipBoarding) {
        preferenceManager.putBoolean(SKIP_BOARDING_KEY, shouldSkipBoarding);
    }

    public boolean shouldSkipBoarding() {
        return preferenceManager.getBoolean(SKIP_BOARDING_KEY, false);
    }


}
