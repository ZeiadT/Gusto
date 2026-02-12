package iti.mad.gusto.presentation.mealdetails;

import android.content.Context;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(MealEntity meal);

        void showError(String message);

        void showWarning(String message);

        void showLoading();

        void hideLoading();

        void onNetworkDisconnected();

        void onNetworkReconnected();
    }

    interface Presenter {
        void getMealDetails(String mealId);

        void onFeaturedMealAddToPlan(String date, MealType type);

        void onFavoriteClicked();

        void addConnectivityListener(Context context);

        void onDetach();


    }
}
