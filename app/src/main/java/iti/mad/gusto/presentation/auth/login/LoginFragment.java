package iti.mad.gusto.presentation.auth.login;

import android.app.Activity;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.auth.activity.AuthActivityCommunicator;
import iti.mad.gusto.presentation.common.component.PrimaryLoadableButton;
import iti.mad.gusto.presentation.common.component.SecondaryIconButton;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToastWithVibration;
import iti.mad.gusto.presentation.main.activity.MainActivity;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment implements LoginContract.View {
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private CheckBox cbRememberMe;
    private TextView btnForgetPassword;
    private PrimaryLoadableButton btnLogin;
    private SecondaryIconButton btnGoogle;
    private SecondaryIconButton btnFacebook;
    private TextView btnGuest;
    private TextView btnSignUp;

    private LoginPresenter presenter;
    private AuthActivityCommunicator communicator;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        navController = NavHostFragment.findNavController(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        presenter = new LoginPresenter(requireActivity(), this);
        setListeners();

        Activity parentActivity = requireActivity();
        if (parentActivity instanceof AuthActivityCommunicator)
            communicator = (AuthActivityCommunicator) parentActivity;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    private void initUI(View view) {
        etEmail = view.findViewById(R.id.emailEditText);
        etPassword = view.findViewById(R.id.passwordEditText);
        cbRememberMe = view.findViewById(R.id.cb_remember_me);
        btnForgetPassword = view.findViewById(R.id.btn_forget_password);
        btnSignUp = view.findViewById(R.id.btn_sign_up);

        btnLogin = new PrimaryLoadableButton(view.findViewById(R.id.btn_login),
                getString(R.string.login),
                getString(R.string.please_wait));

        btnGoogle = view.findViewById(R.id.btn_google);
        btnFacebook = view.findViewById(R.id.btn_facebook);
        btnGuest = view.findViewById(R.id.btn_guest);
    }

    private void setListeners() {

        btnLogin.setOnClickListener((v) -> presenter.signInWithEmailAndPassword(
                Objects.requireNonNull(etEmail.getText()).toString(),
                Objects.requireNonNull(etPassword.getText()).toString(),
                cbRememberMe.isChecked()
        ));
        btnGoogle.setOnClickListener((v) -> showGoogleAuthSheet());
        btnGuest.setOnClickListener((v) -> presenter.signInAnonymously());
        btnSignUp.setOnClickListener(v -> navigateRegister());
        btnForgetPassword.setOnClickListener(v -> ThemeAwareIconToastWithVibration.info(requireContext(), getString(R.string.coming_soon)));


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
                        presenter.signInWithGoogle(result.getCredential());
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
        btnForgetPassword.setEnabled(false);
        btnSignUp.setEnabled(false);
        btnLogin.setEnabled(false);
    }

    @Override
    public void enableButtons() {
        btnGoogle.setEnabled(true);
        btnFacebook.setEnabled(true);
        btnGuest.setEnabled(true);
        btnForgetPassword.setEnabled(true);
        btnSignUp.setEnabled(true);
        btnLogin.setEnabled(true);
    }

    @Override
    public void setLoginButtonLoading() {
        btnLogin.setLoading();
    }

    @Override
    public void setLoginButtonIdle() {
        btnLogin.setFinished();
    }

    @Override
    public void setLoginButtonIdleWithVibration(Runnable vibrationCallback) {
        btnLogin.setFinishedWithVibration(getContext(), vibrationCallback);
    }

    @Override
    public void showError(String errMsg) {
        ThemeAwareIconToastWithVibration.error(requireContext(), errMsg);

    }

    @Override
    public void navigateHome() {
        communicator.navigateReplacementToAnotherActivityWithAnimation(MainActivity.class);
    }

    @Override
    public void navigateRegister() {
        navController.navigate(R.id.navigate_login_to_register);
    }
}