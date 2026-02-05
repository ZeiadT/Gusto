package iti.mad.gusto.presentation.auth.login;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Context;
import android.os.Bundle;

import androidx.credentials.Credential;
import androidx.credentials.CustomCredential;

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.VibrationManager;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.SettingsRepository;
import iti.mad.gusto.presentation.common.util.ValidationUtil;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final AuthRepository authRepository;
    private final SettingsRepository settingsRepository;
    private final CompositeDisposable disposables;
    private final Context context;

    public LoginPresenter(Context applicationContext, LoginContract.View view) {
        this.context = applicationContext.getApplicationContext();
        this.view = view;
        this.authRepository = AuthRepository.getInstance(context);
        this.settingsRepository = SettingsRepository.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password, boolean rememberMe) {
        view.disableButtons();
        view.setLoginButtonLoading();

        boolean isValidEmail = ValidationUtil.isValidEmail(email);
        boolean isValidPassword = ValidationUtil.isValidPassword(password);

        if (!isValidEmail) {
            view.showError(context.getString(R.string.invalid_email));
            view.enableButtons();
            view.setLoginButtonIdle();
            return;
        }
        if (!isValidPassword) {
            view.showError(context.getString(R.string.invalid_password));
            view.enableButtons();
            view.setLoginButtonIdle();
            return;
        }


        Disposable d = authRepository.signInWithEmailAndPassword(email, password)
                .subscribe(
                        (user) -> {
                            view.enableButtons();
                            view.setLoginButtonIdleWithVibration(() -> VibrationManager.successVibration(context));
                            settingsRepository.setRememberMe(rememberMe);
                            view.navigateHome();
                        },
                        (t) -> {
                            view.showError(t.getMessage());
                            view.enableButtons();
                            view.setLoginButtonIdle();
                        }
                );

        disposables.add(d);
    }

    @Override
    public void signInWithGoogle(Credential credential) {
        view.disableButtons();

        if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {

            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            handleGoogleAuth(googleIdTokenCredential.getIdToken());
        } else {
            view.enableButtons();
            view.showError("Credential is not of type Google ID!");
        }

    }

    private void handleGoogleAuth(String idToken) {

        Disposable disposable = authRepository.authenticateWithGoogle(idToken)
                .subscribe(
                        user -> {
                            view.enableButtons();
                            view.navigateHome();
                        },
                        throwable -> {
                            view.enableButtons();
                            view.showError(throwable.getMessage());
                        }
                );

        disposables.add(disposable);
    }

    @Override
    public void signInAnonymously() {

        view.disableButtons();
        Disposable d = authRepository.signInAnonymously()
                .subscribe(
                        (user) -> {
                            view.enableButtons();
                            view.navigateHome();
                        },
                        (t) -> {
                            view.showError(t.getMessage());
                            view.enableButtons();
                        }
                );

        disposables.add(d);
    }

    @Override
    public void onDetach() {
        disposables.dispose();
    }

}
