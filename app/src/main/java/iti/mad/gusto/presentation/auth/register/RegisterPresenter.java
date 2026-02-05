package iti.mad.gusto.presentation.auth.register;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Context;
import android.os.Bundle;

import androidx.credentials.Credential;
import androidx.credentials.CustomCredential;

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.VibrationManager;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.SettingsRepository;
import iti.mad.gusto.presentation.common.util.ValidationUtil;

public class RegisterPresenter implements RegisterContract.Presenter {
    AuthRepository authRepository;
    SettingsRepository settingsRepository;
    RegisterContract.View view;
    private final Context context;
    private final CompositeDisposable disposables;

    public RegisterPresenter(RegisterContract.View view, Context context) {
        disposables = new CompositeDisposable();
        this.view = view;
        this.context = context;
        authRepository = AuthRepository.getInstance(context.getApplicationContext());
        settingsRepository = SettingsRepository.getInstance(context.getApplicationContext());
    }


    @Override
    public void registerWithEmailAndPassword(String email, String password, String confirmPassword) {
        view.disableButtons();
        view.setRegisterButtonLoading();

        boolean isValidEmail = ValidationUtil.isValidEmail(email);
        boolean isValidPassword = ValidationUtil.isValidPassword(password);

        if (!isValidEmail) {
            view.showError(context.getString(R.string.invalid_email));
            view.enableButtons();
            view.setRegisterButtonIdle();
            return;
        }
        if (!isValidPassword) {
            view.showError(context.getString(R.string.invalid_password));
            view.enableButtons();
            view.setRegisterButtonIdle();
            return;
        }
        if (!Objects.equals(password, confirmPassword)) {
            view.showError(context.getString(R.string.passwords_mismatch));
            view.enableButtons();
            view.setRegisterButtonIdle();
            return;
        }

        Disposable d = authRepository.signInWithEmailAndPassword(email, password)
                .subscribe(
                        (user) -> {
                            view.enableButtons();
                            view.setRegisterButtonIdleWithVibration(() -> VibrationManager.successVibration(context));
                            settingsRepository.setRememberMe(true);
                            view.navigateHome();
                        },
                        (t) -> {
                            view.showError(t.getMessage());
                            view.enableButtons();
                            view.setRegisterButtonIdle();
                        }
                );

        disposables.add(d);

    }

    @Override
    public void registerWithGoogle(Credential credential) {
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
        view = null;
    }
}
