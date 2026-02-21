package iti.mad.gusto.presentation.splash;

public interface SplashContract {
    interface View {
        void navigateMain();
        void navigateAuth();
    }

    interface Presenter {
        void navigate();

    }
}