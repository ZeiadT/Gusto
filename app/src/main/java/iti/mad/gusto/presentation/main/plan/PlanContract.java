package iti.mad.gusto.presentation.main.plan;

import java.util.List;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public interface PlanContract {
    interface View {
        void showMeals(List<PlanMealEntity> plannedMeals);
        void showEmptyState();
        void hideEmptyState();
        void showError(String message);
        void navigateToMealDetails(String mealId);
        void removeMealFromAdapter(int position);
    }

    interface Presenter {
        void getMealsForToday();
        void getMealsByDate(int year, int month, int day);
        void deleteMeal(PlanMealEntity meal, int position);
        void onMealClicked(PlanMealEntity meal);
        void onDestroy();
    }
}