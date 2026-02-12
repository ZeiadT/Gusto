package iti.mad.gusto;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

import iti.mad.gusto.core.managers.LocalizationManager;
import iti.mad.gusto.core.managers.ThemeManager;
import iti.mad.gusto.data.repo.SettingsRepository;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

        Log.d("TAG", "onCreate: Application");
        SettingsRepository settingsRepository = SettingsRepository.getInstance(getApplicationContext());

        LocalizationManager languageManager = LocalizationManager.getInstance();
        languageManager.setStorage(new LocalizationManager.LocaleStorage() {
            @Override
            public void saveLanguage(String languageTag) {
                settingsRepository.setLang(languageTag);
            }

            @Override
            public String getSavedLanguage() {
                return settingsRepository.getLang();
            }
        });

        ThemeManager themeManager = ThemeManager.getInstance();
        themeManager.setStorage(new ThemeManager.ThemeStorage() {
            @Override
            public void saveThemeMode(int themeMode) {
                settingsRepository.setTheme(themeMode);
            }

            @Override
            public int getSavedThemeMode() {
                return settingsRepository.getTheme();
            }
        });

    }
}
