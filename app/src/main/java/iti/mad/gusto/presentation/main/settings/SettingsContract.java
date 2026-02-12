package iti.mad.gusto.presentation.main.settings;

public interface SettingsContract {
    interface View {
        void showError(String message);
        void navigateAuth();
    }

    interface Presenter {
        void setThemeDark();
        void setThemeLight();
        void setThemeSystem();
        void setLanguageAr();
        void setLanguageEn();
        void signOut();

        void onDetach();
    }
}
