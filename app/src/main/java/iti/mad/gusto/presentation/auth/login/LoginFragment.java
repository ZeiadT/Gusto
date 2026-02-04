package iti.mad.gusto.presentation.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.common.component.PrimaryLoadableButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    TextInputEditText etEmail;
    TextInputEditText etPassword;
    CheckBox cbRememberMe;
    TextView btnForgetPassword;
    PrimaryLoadableButton btnLogin;
    TextView btnSignIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setListeners();
    }

    private void initUI(View view) {
        etEmail = view.findViewById(R.id.emailEditText);
        etPassword = view.findViewById(R.id.passwordEditText);
        cbRememberMe = view.findViewById(R.id.cb_remember_me);
        btnForgetPassword = view.findViewById(R.id.btn_forget_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);

        btnLogin = new PrimaryLoadableButton(view.findViewById(R.id.btn_login),
                getString(R.string.login),
                getString(R.string.please_wait));
    }

    private void setListeners() {

        btnLogin.setOnClickListener((v) -> {
            btnLogin.setLoading();
            new Handler().postDelayed(() -> {
                btnLogin.setFinishedWithVibration(getContext(), 300);
            }, 2000);
        });
    }


    @Override
    public void onStart() {
        super.onStart();

    }


}