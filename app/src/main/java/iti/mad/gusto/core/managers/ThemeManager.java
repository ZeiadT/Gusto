package iti.mad.gusto.core.managers;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static ThemeManager instance;

    private ThemeStorage storage;

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void setStorage(ThemeStorage storage) {
        this.storage = storage;
    }

    public void setThemeMode(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);

        if (storage != null) {
            storage.saveThemeMode(mode);
        }
    }

    public void applySavedTheme() {
        if (storage == null) return;

        int mode = storage.getSavedThemeMode();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public int getSelectedThemeMode() {
        return AppCompatDelegate.getDefaultNightMode();
    }

    public boolean isDarkModeActive(Configuration configuration) {
        int nightModeFlags =
                configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public void setLight() {
        setThemeMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void setDark() {
        setThemeMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void setFollowSystem() {
        setThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public interface ThemeStorage {
        int getSavedThemeMode();

        void saveThemeMode(int themeMode);
    }
}
