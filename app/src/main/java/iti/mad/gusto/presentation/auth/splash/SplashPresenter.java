package iti.mad.gusto.presentation.auth.splash;

import iti.mad.gusto.data.repo.SettingsRepository;

public class SplashPresenter implements SplashContract.Presenter {
    private final SplashContract.View view;
    private final SettingsRepository settingsRepo;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
        this.settingsRepo = SettingsRepository.getInstance(view.getAppContext());
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

        //todo if logged in go to home else go to login
        view.navigateToLogin();
        view.cleanupOverlay();
    }
}