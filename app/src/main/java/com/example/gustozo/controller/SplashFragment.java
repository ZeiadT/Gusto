package com.example.gustozo.controller;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gustozo.R;
import com.example.gustozo.view.PolygonView;

public class SplashFragment extends Fragment {
    private PolygonView overlayView;
    ImageView imgLogo;
    TextView tvAppName;
    TextView tvSlogan;
    Handler mainHandler;
    int screenHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenHeight = getResources().getDisplayMetrics().heightPixels;


        imgLogo = view.findViewById(R.id.imgLogo);
        tvAppName = view.findViewById(R.id.tvAppName);
        tvSlogan = view.findViewById(R.id.tvSlogan);
        overlayView = view.getRootView().findViewById(R.id.overlayView);
    }

    @Override
    public void onStart() {
        super.onStart();

        mainHandler = new Handler(Looper.getMainLooper());

        animateLogo();
        animateName();
        animateSlogan();

        // setup navigation animation overlay
        overlayView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                overlayView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                overlayView.setTranslationY(overlayView.getHeight());
                animateNavigation();
            }
        });

    }

    private void navigate() {

        //todo add logic to navigate -> boarding, login, home. based on user state
        FragmentManager parentManager;

        try {
            parentManager = getParentFragmentManager();
        } catch (IllegalStateException ex)
        {
            return;
        }
        if (parentManager.isDestroyed()) return;

        parentManager.beginTransaction()
                .replace(R.id.frag_container_auth, new BoardingFragment())
                .commitNow();

        endAnimateNavigation();
    }

    private void animateLogo() {
        Drawable drawable = imgLogo.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) drawable).start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            ((AnimatedVectorDrawable) drawable).start();
        }
    }

    private void animateName() {

        mainHandler.postDelayed(() -> {
            tvAppName.setVisibility(View.VISIBLE);
            Animation slideUp = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_up_fade);
            tvAppName.startAnimation(slideUp);
        }, 1000);
    }

    private void animateSlogan() {

        mainHandler.postDelayed(() -> {
            tvSlogan.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in);
            tvSlogan.startAnimation(fadeIn);
        }, 1500);
    }

    private void animateNavigation() {
        int viewHeight = overlayView.getHeight();
        float gapOffset = viewHeight * overlayView.getGapOffsetPercentage();
        overlayView.animate()
                .translationY(gapOffset)
                .setDuration(600)
                .setInterpolator(new androidx.interpolator.view.animation.FastOutSlowInInterpolator())
                .setStartDelay(3000)
                .withEndAction(this::navigate)
                .start();
    }

    private void endAnimateNavigation() {
        overlayView.animate()
                .translationY(-screenHeight)
                .setDuration(600)
                .setStartDelay(0)
                .withEndAction(this::detachOverlay)
                .start();
    }

    private void detachOverlay() {
        if (overlayView.getParent() instanceof android.view.ViewGroup) {
            ((android.view.ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
    }

}