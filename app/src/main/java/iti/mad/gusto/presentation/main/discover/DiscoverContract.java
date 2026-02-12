package iti.mad.gusto.presentation.main.discover;

import android.content.Context;

import java.util.List;

import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;

public interface DiscoverContract {
    interface View {
        void setFeaturedMeal(MealEntity meal);
        void setCategories(List<CategoryEntity> categories);
        void setCountries(List<CountryEntity> countries);
        void showError(String errMsg);
        void showWarning(String msg);
        void onNetworkDisconnected();
        void onNetworkReconnected();
    }

    interface Presenter {
        void onViewCreated();
        void getFeaturedMeal();
        void getCategories();
        void getCountries();
        void onTagClicked(CategoryEntity category);
        void onFeaturedMealAddToPlan(String date, MealType type);
        void onFeaturedMealAddToFavourite();
        void addConnectivityListener(Context context);
        boolean isNetworkDisconnected(Context context);
        void onDetach();

    }
}
