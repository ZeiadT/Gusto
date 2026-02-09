package iti.mad.gusto.presentation.auth.register;

import androidx.credentials.Credential;

public interface RegisterContract {
    interface Presenter {
        void registerWithEmailAndPassword(String email, String password, String confirmPassword);
        void registerWithGoogle(Credential credential);
        void signInAnonymously();
        void onDetach();
    }

    interface View {
        void disableButtons();
        void enableButtons();
        void setRegisterButtonLoading();
        void setRegisterButtonIdle();

        void setRegisterButtonIdleWithVibration(Runnable vibrationCallback);

        void showError(String errMsg);
        void navigateHome();
        void navigateLogin();

        void startLoadingBar();
        void stopLoadingBar();
    }
}
