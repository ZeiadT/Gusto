package com.example.gustozo.controller;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.gustozo.R;
import com.example.gustozo.view.WormDotIndicator;

import java.util.ArrayList;
import java.util.List;

public class BoardingFragment extends Fragment {

    private static final String POSITION_KEY = "position";

    // UI Components
    private Button btnNext;
    private TextView btnPrev;
    private TextView btnSkip;
    private WormDotIndicator wormDotIndicator;
    private TextView titleTV;
    private TextView subTitleTV;
    private ImageView illustrationTV;
    private ConstraintLayout backgroundContainer;

    // Data
    private final List<BoardingItem> boardingItems = new ArrayList<>();
    private int currentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        initUI(view);
        initListeners();

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION_KEY, 0);
        }
        updateUI(currentPosition, false);
    }

    private void loadData() {
        boardingItems.clear();

        if (getContext() == null) return;

        // ITEM 1
        boardingItems.add(new BoardingItem(
                getString(R.string.onboarding_title_plan),
                getString(R.string.onboarding_desc_plan),
                R.drawable.calender_boarding,
                ContextCompat.getColor(requireContext(), R.color.boarding_plan)
        ));

        // ITEM 2
        boardingItems.add(new BoardingItem(
                getString(R.string.onboarding_title_discover),
                getString(R.string.onboarding_desc_discover),
                R.drawable.menu_boarding,
                ContextCompat.getColor(requireContext(), R.color.boarding_discover)
        ));

        // ITEM 3
        boardingItems.add(new BoardingItem(
                getString(R.string.onboarding_title_save),
                getString(R.string.onboarding_desc_save),
                R.drawable.hamburger_boarding,
                ContextCompat.getColor(requireContext(), R.color.boarding_never_lose)
        ));
    }

    private void initUI(View view) {
        btnNext = view.findViewById(R.id.btn_next);
        btnPrev = view.findViewById(R.id.btn_prev);
        btnSkip = view.findViewById(R.id.btn_skip);
        wormDotIndicator = view.findViewById(R.id.dots_indicator);
        titleTV = view.findViewById(R.id.tv_title);
        subTitleTV = view.findViewById(R.id.tv_subtitle);
        illustrationTV = view.findViewById(R.id.iv_boarding);
        backgroundContainer = view.findViewById(R.id.bg_container);

        wormDotIndicator.setCount(boardingItems.size());
    }

    private void initListeners() {
        btnNext.setOnClickListener(v -> {
            if (currentPosition < boardingItems.size() - 1) {
                updateUI(currentPosition + 1, true);
            } else {
                finishBoarding();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPosition > 0) {
                updateUI(currentPosition - 1, true);
            }
        });

        btnSkip.setOnClickListener(v -> finishBoarding());
    }

    private void finishBoarding() {
        // Handle navigation to Login or Main Activity
        Toast.makeText(getContext(), "Boarding Finished", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(int newPosition, boolean animate) {

        if (newPosition < 0 || newPosition >= boardingItems.size()) return;

        BoardingItem newItem = boardingItems.get(newPosition);
        BoardingItem oldItem = boardingItems.get(currentPosition);

        wormDotIndicator.selectDot(newPosition, animate);

        btnPrev.setVisibility(newPosition == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setText(newPosition == boardingItems.size() - 1 ? getString(R.string.join_now) : getString(R.string.next));

        if (animate) {
            animatePageChange(newPosition, currentPosition, newItem, oldItem);
            currentPosition = newPosition;
            return;
        }

        titleTV.setText(newItem.title);
        subTitleTV.setText(newItem.description);
        illustrationTV.setImageResource(newItem.imageResId);
        backgroundContainer.setBackgroundColor(newItem.backgroundColor);

        currentPosition = newPosition;
    }

    private void animatePageChange(int newPos, int oldPos, BoardingItem newItem, BoardingItem oldItem) {
        long duration = 300;
        boolean isNext = newPos > oldPos;
        float slideDistance = 100f;

        // --- 1. Background Gradient ---
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), oldItem.backgroundColor, newItem.backgroundColor);
        colorAnim.setDuration(duration);
        colorAnim.addUpdateListener(animator -> backgroundContainer.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnim.start();

        // --- 2. Text Fade ---
        titleTV.animate().alpha(0f).setDuration(duration / 2).withEndAction(() -> {
            titleTV.setText(newItem.title);
            titleTV.animate().alpha(1f).setDuration(duration / 2).start();
        }).start();
        subTitleTV.animate().alpha(0f).setDuration(duration / 2).withEndAction(() -> {
            subTitleTV.setText(newItem.description);
            subTitleTV.animate().alpha(1f).setDuration(duration / 2).start();
        }).start();


        // --- 3. Image Slide & Fade ---
        float exitX = isNext ? -slideDistance : slideDistance;

        illustrationTV
                .animate()
                .translationX(exitX)
                .alpha(0f)
                .setDuration(duration / 2)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {

            illustrationTV.setImageResource(newItem.imageResId);
            float enterStartX = isNext ? slideDistance : -slideDistance;
            illustrationTV.setTranslationX(enterStartX);

            illustrationTV
                    .animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(duration / 2)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }).start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, currentPosition);
    }

    static class BoardingItem {
        String title;
        String description;
        int imageResId;
        int backgroundColor;

        public BoardingItem(String title, String description, int imageResId, int backgroundColor) {
            this.title = title;
            this.description = description;
            this.imageResId = imageResId;
            this.backgroundColor = backgroundColor;
        }
    }
}