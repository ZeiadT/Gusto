package iti.mad.gusto.presentation.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.auth.login.LoginFragment;
import iti.mad.gusto.presentation.common.component.PrimaryLoadableButton;
import iti.mad.gusto.presentation.common.component.SecondaryIconButton;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToastWithVibration;

public class RegisterFragment extends Fragment implements RegisterContract.View {
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private PrimaryLoadableButton btnRegister;
    private SecondaryIconButton btnGoogle;
    private SecondaryIconButton btnFacebook;
    private TextView btnGuest;
    private TextView btnSignIn;
    private RegisterPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        presenter = new RegisterPresenter(this, requireContext());
        setListeners();

    }

    private void initUI(View view) {
        etEmail = view.findViewById(R.id.emailEditText);
        etPassword = view.findViewById(R.id.passwordEditText);
        etConfirmPassword = view.findViewById(R.id.confirmPasswordEditText);
        btnSignIn = view.findViewById(R.id.btn_sign_in);

        btnRegister = new PrimaryLoadableButton(view.findViewById(R.id.btn_login),
                getString(R.string.register),
                getString(R.string.please_wait));

        btnGoogle = view.findViewById(R.id.btn_google);
        btnFacebook = view.findViewById(R.id.btn_facebook);
        btnGuest = view.findViewById(R.id.btn_guest);
    }

    private void setListeners() {

        btnRegister.setOnClickListener((v) -> presenter.registerWithEmailAndPassword(
                Objects.requireNonNull(etEmail.getText()).toString(),
                Objects.requireNonNull(etPassword.getText()).toString(),
                Objects.requireNonNull(etConfirmPassword.getText()).toString()
        ));
        btnGoogle.setOnClickListener((v) -> showGoogleAuthSheet());
        btnGuest.setOnClickListener((v) -> presenter.signInAnonymously());
        btnSignIn.setOnClickListener(v -> navigateLogin());

    }

    private void showGoogleAuthSheet() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager.create(requireContext()).getCredentialAsync(
                requireContext(),
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(requireContext()),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        Log.d("TAG", "onResult: " + result.getCredential());
                        presenter.registerWithGoogle(result.getCredential());
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("ErrorInLoginFragment", "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());
                    }
                }
        );
    }

    @Override
    public void disableButtons() {
        btnGoogle.setEnabled(false);
        btnFacebook.setEnabled(false);
        btnGuest.setEnabled(false);
        btnSignIn.setEnabled(false);
        btnRegister.setEnabled(false);
    }

    @Override
    public void enableButtons() {
        btnGoogle.setEnabled(true);
        btnFacebook.setEnabled(true);
        btnGuest.setEnabled(true);
        btnSignIn.setEnabled(true);
        btnRegister.setEnabled(true);
    }


    @Override
    public void setRegisterButtonLoading() {
        btnRegister.setLoading();
    }

    @Override
    public void setRegisterButtonIdle() {
        btnRegister.setFinished();
    }

    @Override
    public void setRegisterButtonIdleWithVibration(Runnable vibrationCallback) {
        btnRegister.setFinishedWithVibration(requireContext(), vibrationCallback);
    }

    @Override
    public void showError(String errMsg) {
        ThemeAwareIconToastWithVibration.error(requireContext(), errMsg);
    }

    @Override
    public void navigateHome() {
        //todo navigate home
    }

    @Override
    public void navigateLogin() {
        Log.d("TAG", "navigateLogin: ");
        safelyNavigateFragment(new LoginFragment());
    }

    private void safelyNavigateFragment(Fragment fragment) {
        FragmentManager parentManager;
        try {
            parentManager = getParentFragmentManager();
        } catch (IllegalStateException ex) {
            return;
        }

        if (!parentManager.isDestroyed()) {
            parentManager.beginTransaction()
                    .replace(R.id.frag_container_auth, fragment)
                    .commitNow();
        }
    }
}