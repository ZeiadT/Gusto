package iti.mad.gusto.presentation.main.favourite;

import android.content.Context;

import java.util.List;

import iti.mad.gusto.domain.entity.FavouriteMealEntity;

public interface FavouriteContract {
    interface View {
        void showMeals(List<FavouriteMealEntity> meals);

        void showError(String message);
    }

    interface Presenter {
        void getMeals();

        void deleteMeal(FavouriteMealEntity meal);

        void onDetach();
    }
}
