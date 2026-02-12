package iti.mad.gusto.presentation.auth.splash;

import com.google.firebase.auth.FirebaseUser;

import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.SettingsRepository;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;
    private final SettingsRepository settingsRepo;
    private final AuthRepository authRepository;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
        this.settingsRepo = SettingsRepository.getInstance(view.getAppContext());
        this.authRepository = AuthRepository.getInstance(view.getAppContext());
    }

    @Override
    public void onViewStarted() {
        view.showInitialAnimations();
        view.startOverlayAnimation();
    }

    @Override
    public void onNavigationAnimationFinished() {

        boolean shouldSkipBoarding = settingsRepo.shouldSkipBoarding();

        if (!shouldSkipBoarding) {
            view.navigateToBoarding();
            view.cleanupOverlay();
            return;
        }


        boolean rememberMe = settingsRepo.getRememberMe();
        FirebaseUser user = authRepository.getCurrentUser();

        if (!rememberMe) {
            authRepository.signOut();
        }

        if (user != null && rememberMe) {
            view.navigateToHome();
        } else {
            view.navigateToLogin();
            view.cleanupOverlay();
        }
    }
}