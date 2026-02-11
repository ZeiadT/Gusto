package iti.mad.gusto.presentation.main.discover;

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
    }

    interface Presenter {
        void onViewCreated();
        void getFeaturedMeal();
        void getCategories();
        void getCountries();
        void onTagClicked(CategoryEntity category);
        void onFeaturedMealAddToPlan(String date, MealType type);
        void onFeaturedMealAddToFavourite(MealEntity meal);
        void onDetach();

    }
}
