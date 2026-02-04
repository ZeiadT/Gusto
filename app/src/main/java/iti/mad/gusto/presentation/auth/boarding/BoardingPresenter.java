package iti.mad.gusto.presentation.auth.boarding;

import iti.mad.gusto.R;
import iti.mad.gusto.data.model.BoardingItem;

import java.util.ArrayList;
import java.util.List;

public class BoardingPresenter implements BoardingContract.Presenter {

    private final BoardingContract.View view;
    private final List<BoardingItem> items = new ArrayList<>();
    private int currentPosition = 0;

    public BoardingPresenter(BoardingContract.View view) {
        this.view = view;
        loadStaticData();
    }

    private void loadStaticData() {
        items.add(new BoardingItem(R.string.onboarding_title_plan, R.string.onboarding_desc_plan, R.drawable.calender_boarding, R.color.boarding_plan));
        items.add(new BoardingItem(R.string.onboarding_title_discover, R.string.onboarding_desc_discover, R.drawable.fruit_salad, R.color.boarding_discover));
        items.add(new BoardingItem(R.string.onboarding_title_save, R.string.onboarding_desc_save, R.drawable.hamburger_boarding, R.color.boarding_never_lose));
    }

    @Override
    public void init(int initialPosition) {
        this.currentPosition = initialPosition;
        updateView(false, true);
    }

    @Override
    public void onNextClicked() {
        if (currentPosition < items.size() - 1) {
            currentPosition++;
            updateView(true, true);
        } else {
            view.navigateToLogin();
        }
    }

    @Override
    public void onPrevClicked() {
        if (currentPosition > 0) {
            currentPosition--;
            updateView(true, false);
        }
    }

    @Override
    public void onSkipClicked() {
        view.navigateToLogin();
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    private void updateView(boolean animate, boolean movingForward) {
        if (items.isEmpty()) return;

        view.renderPage(items.get(currentPosition), animate, movingForward);
        view.updateButtonState(currentPosition == 0, currentPosition == items.size() - 1);
    }
}