package iti.mad.gusto.presentation.auth.login;

import androidx.credentials.Credential;

public interface LoginContract {
    interface Presenter{

        void signInWithEmailAndPassword(String email, String password, boolean rememberMe);

        void signInWithGoogle(Credential credential);
        void signInAnonymously();

        void onDetach();
    }

    interface View{
        void disableButtons();
        void enableButtons();
        void setLoginButtonLoading();
        void setLoginButtonIdle();

        void setLoginButtonIdleWithVibration(Runnable vibrationCallback);

        void showError(String errMsg);
        void navigateHome();
        void navigateRegister();

        void startLoadingBar();
        void stopLoadingBar();
    }
}
