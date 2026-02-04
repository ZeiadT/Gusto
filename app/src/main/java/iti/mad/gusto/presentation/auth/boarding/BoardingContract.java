package iti.mad.gusto.presentation.auth.boarding;

import iti.mad.gusto.data.model.BoardingItem;

public interface BoardingContract {
    interface View {
        // UI Updates
        void renderPage(BoardingItem item, boolean animate, boolean isMovingForward);
        void updateButtonState(boolean isFirstPage, boolean isLastPage);

        // Navigation
        void navigateToLogin();
    }

    interface Presenter {
        void init(int initialPosition);
        void onNextClicked();
        void onPrevClicked();
        void onSkipClicked();
        int getCurrentPosition();
        int getCount();
    }
}