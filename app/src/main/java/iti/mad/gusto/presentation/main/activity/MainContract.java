package iti.mad.gusto.presentation.main.activity;

public interface MainContract {
    interface View {
        void animateIntro();
        void skipIntro();
        void navigateToSection(int destinationId, boolean isForward);
    }

    interface Presenter {
        void animateIntro(boolean isRecreation, boolean shouldSkipOverlayAnimation);
        boolean onBottomNavItemSelected(int newItemId, int newItemOrder, int currentItemId, int currentItemOrder);

        boolean isGuestUser();
    }
}