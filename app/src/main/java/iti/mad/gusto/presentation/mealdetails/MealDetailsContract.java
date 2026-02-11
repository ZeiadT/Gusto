package iti.mad.gusto.presentation.mealdetails;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(MealEntity meal);

        void showError(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {
        void getMealDetails(String mealId);
        void onFeaturedMealAddToPlan(String date, MealType type);

        void onDetach();


    }
}
