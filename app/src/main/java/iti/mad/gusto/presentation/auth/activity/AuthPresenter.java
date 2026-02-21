package iti.mad.gusto.presentation.auth.activity;

import iti.mad.gusto.data.repo.SettingsRepository;

public class AuthPresenter implements AuthContract.Presenter {
    AuthContract.View view;
    SettingsRepository settingsRepository;
    AuthPresenter(AuthContract.View view, SettingsRepository settingsRepository){
        this.view = view;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void initAuth(boolean shouldSkipOverlayAnimation) {
        if (settingsRepository.shouldSkipBoarding())
            view.navigateLogin();

        if (shouldSkipOverlayAnimation)
            view.skipIntro();
        else
            view.animateIntro();
    }
}
