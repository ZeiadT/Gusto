package com.example.gustozo.presentation.auth.splash;

import android.content.Context;

public interface SplashContract {
    interface View {
        void showInitialAnimations();
        void startOverlayAnimation();
        void navigateToBoarding();
        void navigateToHome();
        void navigateToLogin();
        void cleanupOverlay();
        Context getAppContext();
    }

    interface Presenter {
        void onViewStarted();
        void onNavigationAnimationFinished();
    }
}