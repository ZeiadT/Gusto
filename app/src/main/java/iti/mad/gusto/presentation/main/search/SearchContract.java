package iti.mad.gusto.presentation.main.search;

import java.util.List;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public interface SearchContract {
    interface View {
        void showSearchTags(List<SearchTagEntity> results);
        void showSelectedTags(List<SearchTagEntity> tags);
        void showMeals(List<MealEntity> meals);
        void showError(String errMsg);
        void navigateToMealDetails(String mealId);
        void clearTagSearchBar();
    }

    interface Presenter {
        void searchForTag(String query);
        void searchForMeals(String query);
        void onTagSelected(SearchTagEntity tag);
        void onTagRemoved(SearchTagEntity tag);
        void onClearTagsClicked();
        void onMealClicked(MealEntity meal);
        void restoreState(List<SearchTagEntity> tags, String query, List<MealEntity> meals);
        void onDetach();
    }
}