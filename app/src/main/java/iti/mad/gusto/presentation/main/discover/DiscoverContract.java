package iti.mad.gusto.presentation.main.discover;

import java.util.List;

import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.MealEntity;

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
        void onCountryClicked(CountryEntity country);
        void onCategoryClicked(CategoryEntity category);
        void onFeaturedMealAddToPlan(MealEntity meal);
        void onFeaturedMealAddToFavourite(MealEntity meal);
        void onDetach();

    }
}
