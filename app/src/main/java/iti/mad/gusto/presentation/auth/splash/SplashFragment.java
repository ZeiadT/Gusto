package iti.mad.gusto.presentation.auth.splash;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.auth.activity.AuthActivityCommunicator;
import iti.mad.gusto.presentation.common.component.PolygonView;
import iti.mad.gusto.presentation.common.util.AnimationUtil;
import iti.mad.gusto.presentation.main.activity.MainActivity;

public class SplashFragment extends Fragment implements SplashContract.View {
    private NavController navController;
    private PolygonView overlayView;
    private ImageView imgLogo;
    private TextView tvAppName;
    private TextView tvSlogan;
    private int screenHeight;
    private SplashContract.Presenter presenter;
    private AuthActivityCommunicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        navController = NavHostFragment.findNavController(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        presenter = new SplashPresenter(this);

        Activity parentActivity = requireActivity();
        if (parentActivity instanceof AuthActivityCommunicator)
            communicator = (AuthActivityCommunicator) parentActivity;

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
        navController.navigate(R.id.navigate_splash_to_boarding);
    }

    @Override
    public void navigateToHome() {
        if (communicator != null)
            communicator.navigateReplacementToAnotherActivity(MainActivity.class);
    }

    @Override
    public void navigateToLogin() {
        navController.navigate(R.id.navigate_splash_to_login);
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
                new AccelerateDecelerateInterpolator()
        );
    }


    private void detachOverlay() {
        if (overlayView != null && overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
    }

    @Override
    public Context getAppContext() {
        return requireContext().getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}