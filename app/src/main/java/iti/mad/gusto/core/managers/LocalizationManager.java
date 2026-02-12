package iti.mad.gusto.core.managers;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class LocalizationManager {

    private static LocalizationManager instance;
    private LocaleStorage storage;

    private static final String SYSTEM = "system";

    private LocalizationManager() {
    }

    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }

    public void setStorage(LocaleStorage storage) {
        this.storage = storage;
    }

    public String getSelectedLanguage() {
        return storage != null ? storage.getSavedLanguage() : SYSTEM;
    }

    public void setLanguage(String languageTag) {
        if (storage != null) {
            storage.saveLanguage(languageTag);
        }

        applyLanguage(languageTag);
    }

    public void setEnglish() {
        setLanguage("en");
    }

    public void setArabic() {
        setLanguage("ar");
    }

    public void setFollowSystem() {
        setLanguage(SYSTEM);
    }

    public void applySavedLanguage() {
        if (storage == null) return;
        applyLanguage(storage.getSavedLanguage());
    }

    private void applyLanguage(String languageTag) {

        if (Build.VERSION.SDK_INT >= 33) {
            if (SYSTEM.equals(languageTag)) {
                AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.getEmptyLocaleList()
                );
            } else {
                AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(languageTag)
                );
            }
        }
    }

    public Context attachBaseContext(Context context) {

        String lang = getSelectedLanguage();

        if (SYSTEM.equals(lang)) {
            return context;
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        return context.createConfigurationContext(config);
    }

    public interface LocaleStorage {
        void saveLanguage(String languageTag);

        String getSavedLanguage();
    }

}
