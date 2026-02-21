package iti.mad.gusto.presentation.splash;

import com.google.firebase.auth.FirebaseUser;

import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.SettingsRepository;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;
    private final SettingsRepository settingsRepo;
    private final AuthRepository authRepository;

    public SplashPresenter(SplashContract.View view, SettingsRepository settingsRepo, AuthRepository authRepository) {
        this.view = view;
        this.settingsRepo = settingsRepo;
        this.authRepository = authRepository;
    }
    @Override
    public void navigate() {

        boolean rememberMe = settingsRepo.getRememberMe();
        FirebaseUser user = authRepository.getCurrentUser();

        if (!rememberMe) {
            authRepository.signOut();
        }

        if (user != null && rememberMe) {
            view.navigateMain();
        } else {
            view.navigateAuth();
        }
    }
}