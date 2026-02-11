package iti.mad.gusto.presentation.main.plan;

import java.util.List;

import iti.mad.gusto.domain.entity.PlanMealEntity;

public interface PlanContract {
    interface View {
        void showMeals(List<PlanMealEntity> plannedMeals);
        void showMealDetails(String mealId);
        void showError(String message);
    }

    interface Presenter {
        void getMealsByDate(String date);
        void addMeal(PlanMealEntity meal);
        void deletePlan(PlanMealEntity meal);
        void onDateChanged(String newDate);
        void onDetach();
    }
}
