package iti.mad.gusto.presentation.main.activity;

public interface MainContract {
    interface View {
        void showIntroAnimation(); // Replaces setup + animate
        void removeOverlay();
        void navigateToSection(int destinationId, boolean isForward);
    }

    interface Presenter {
        void onViewCreated(boolean isRecreation);
        boolean onBottomNavItemSelected(int newItemId, int newItemOrder, int currentItemId, int currentItemOrder);
    }
}