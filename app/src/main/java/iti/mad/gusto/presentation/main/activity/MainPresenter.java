package iti.mad.gusto.presentation.main.activity;

import android.content.Context;

import iti.mad.gusto.data.repo.AuthRepository;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;
    private final AuthRepository authRepository;

    public MainPresenter(Context context, MainContract.View view) {
        this.view = view;
        authRepository = AuthRepository.getInstance(context);
    }

    @Override
    public void animateIntro(boolean isRecreation, boolean shouldSkipOverlayAnimation) {
        if (isRecreation || shouldSkipOverlayAnimation) {
            view.skipIntro();
        } else {
            view.animateIntro();
        }
    }

    @Override
    public boolean onBottomNavItemSelected(int newItemId, int newItemOrder, int currentItemId, int currentItemOrder) {
        if (newItemId == currentItemId) {
            return false;
        }

        view.navigateToSection(newItemId, newItemOrder > currentItemOrder);
        return true;
    }

    @Override
    public boolean isGuestUser() {
        return authRepository.isAnonymousUser();
    }
}