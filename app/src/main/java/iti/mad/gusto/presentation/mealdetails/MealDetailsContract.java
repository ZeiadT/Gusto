package iti.mad.gusto.presentation.mealdetails;

import android.content.Context;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(MealEntity meal);
        void setFavouriteIcon(boolean isFavourite);

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

        void onFavoriteClicked(boolean isFavorite);

        void addConnectivityListener(Context context);

        void removeConnectivityListener(Context context);

        void onDetach();


    }
}
