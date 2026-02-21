package iti.mad.gusto.presentation.auth.activity;

public interface AuthContract {
    interface View {
        void animateIntro();
        void skipIntro();
        void navigateLogin();
    }

    interface Presenter {
        void initAuth(boolean shouldSkipOverlayAnimation);

    }
}
