package com.example.gustozo.presentation.auth.splash;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.gustozo.R;
import com.example.gustozo.presentation.auth.boarding.BoardingFragment;
import com.example.gustozo.presentation.auth.login.LoginFragment;
import com.example.gustozo.presentation.common.component.PolygonView;
import com.example.gustozo.presentation.common.util.AnimationUtil;

public class SplashFragment extends Fragment implements SplashContract.View {
    private PolygonView overlayView;
    private ImageView imgLogo;
    private TextView tvAppName;
    private TextView tvSlogan;
    private int screenHeight;
    private SplashContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        presenter = new SplashPresenter(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewStarted();
    }

    private void initUI(View view) {
        imgLogo = view.findViewById(R.id.imgLogo);
        tvAppName = view.findViewById(R.id.tvAppName);
        tvSlogan = view.findViewById(R.id.tvSlogan);
        overlayView = view.getRootView().findViewById(R.id.overlayView);
    }


    @Override
    public void showInitialAnimations() {
        AnimationUtil.startVectorAnimation(imgLogo);
        AnimationUtil.animateVisibilityWithResource(tvAppName, R.anim.slide_up_fade, 1000);
        AnimationUtil.animateVisibilityWithResource(tvSlogan, R.anim.fade_in, 1500);
    }

    @Override
    public void startOverlayAnimation() {
        overlayView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                overlayView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                overlayView.setTranslationY(overlayView.getHeight());
                animateNavigationSequence();
            }
        });
    }

    private void animateNavigationSequence() {
        int viewHeight = overlayView.getHeight();
        float gapOffset = viewHeight * overlayView.getGapOffsetPercentage();

        AnimationUtil.animateTranslationY(
                overlayView,
                gapOffset,
                null,
                presenter::onNavigationAnimationFinished,
                3000,
                600,
                new FastOutSlowInInterpolator()
        );
    }

    @Override
    public void navigateToBoarding() {
        safelyNavigateFragment(new BoardingFragment());
    }

    @Override
    public void navigateToHome() {
        // safelyNavigate(new HomeFragment());
    }

    @Override
    public void navigateToLogin() {
        safelyNavigateFragment(new LoginFragment());
    }

    @Override
    public void cleanupOverlay() {
        AnimationUtil.animateTranslationY(
                overlayView,
                -screenHeight,
                null,
                this::detachOverlay,
                0,
                600,
                null
        );
    }


    private void detachOverlay() {
        if (overlayView != null && overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
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

    @Override
    public Context getAppContext() {
        return requireContext().getApplicationContext();
    }
}