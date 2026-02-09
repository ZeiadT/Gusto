package iti.mad.gusto.presentation.mealdetails;

import iti.mad.gusto.domain.entity.MealEntity;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(MealEntity meal);

        void showError(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {
        void getMealDetails(String mealId);

        void onDetach();


    }
}
