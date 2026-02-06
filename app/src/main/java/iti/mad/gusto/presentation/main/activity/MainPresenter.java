package iti.mad.gusto.presentation.main.activity;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    // MainPresenter.java
    @Override
    public void onViewCreated(boolean isRecreation) {
        if (isRecreation) {
            view.removeOverlay();
        } else {
            view.showIntroAnimation();
        }
    }

    @Override
    public boolean onBottomNavItemSelected(int newItemId, int currentItemId) {
        if (newItemId == currentItemId) {
            return false;
        }

        view.navigateToSection(newItemId);
        return true;
    }
}