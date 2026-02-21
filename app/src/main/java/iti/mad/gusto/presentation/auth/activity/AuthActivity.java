package iti.mad.gusto.presentation.auth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import iti.mad.gusto.data.repo.SettingsRepository;
import iti.mad.gusto.presentation.common.component.PolygonView;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.common.constant.NavigationKey;
import iti.mad.gusto.presentation.common.util.AnimationUtil;

public class AuthActivity extends AppCompatActivity implements AuthContract.View, AuthActivityCommunicator {
    private PolygonView overlayView;
    private NavController navController;
    boolean shouldSkipOverlayAnimation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        overlayView = findViewById(R.id.overlayView);
        AuthPresenter presenter = new AuthPresenter(this, SettingsRepository.getInstance(getApplicationContext()));

        Intent intent = getIntent();
        if (intent != null) {
            shouldSkipOverlayAnimation = intent.getBooleanExtra(NavigationKey.SHOULD_SKIP_OVERLAY_ANIMATION, true);
        }

        initNavigationController();
        presenter.initAuth(shouldSkipOverlayAnimation);
    }

    @Override
    public void navigateReplacementToAnotherActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);

        startActivity(intent);
        overridePendingTransition(
                0,
                0
        );

        finish();
        overridePendingTransition(
                0,
                0
        );
    }

    @Override
    public void navigateReplacementToAnotherActivityWithAnimation(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    @Override
    public void animateIntro() {
        overlayView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        overlayView.getViewTreeObserver().removeOnPreDrawListener(this);
                        float gapOffset = overlayView.getHeight() * overlayView.getGapOffsetPercentage();
                        overlayView.setTranslationY(gapOffset);
                        animateOverlayRemoval();
                        return true;
                    }
                });
    }

    @Override
    public void skipIntro() {
        removeOverlay();

    }

    private void animateOverlayRemoval() {
        float currentTranslation = overlayView.getTranslationY();
        float targetTranslation = -(overlayView.getHeight() + currentTranslation);

        AnimationUtil.animateTranslationY(
                overlayView,
                targetTranslation,
                null,
                this::removeOverlay,
                0,
                600,
                new AccelerateDecelerateInterpolator()
        );
    }

    private void removeOverlay() {
        if (overlayView != null && overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
    }

    private void initNavigationController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_container_auth);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    @Override
    public void navigateLogin() {
        navController.navigate(R.id.navigate_boarding_to_login);
    }
}