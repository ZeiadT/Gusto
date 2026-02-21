package iti.mad.gusto.presentation.main.search;

import android.content.Context;

import java.util.List;
import java.util.Set;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public interface SearchContract {
    interface View {
        void showSearchTags(List<SearchTagEntity> results);
        void showSelectedTags(List<SearchTagEntity> tags);
        void showMeals(List<MealEntity> meals, Set<String> favouriteIds);
        void showError(String errMsg);
        void showWarning(String msg);
        void navigateToMealDetails(String mealId);
        void clearTagSearchBar();
        void refreshFavouriteStates(Set<String> favouriteIds);

        void onNetworkDisconnected();

        void onNetworkReconnected();
    }

    interface Presenter {
        void searchForTag(String query);
        void searchForMeals(String query);
        void onTagSelected(SearchTagEntity tag);
        void onTagRemoved(SearchTagEntity tag);
        void onClearTagsClicked();
        void onMealClicked(MealEntity meal);
        void onMealFavClicked(MealEntity meal, boolean isFavorite);
        void refreshFavouriteStates();
        void restoreState(List<SearchTagEntity> tags, String query, List<MealEntity> meals);
        void addConnectivityListener(Context context);
        void removeConnectivityListener(Context context);
        boolean isNetworkDisconnected(Context context);
        void onDetach();
    }
}