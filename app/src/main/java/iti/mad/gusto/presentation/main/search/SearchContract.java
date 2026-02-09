package iti.mad.gusto.presentation.main.search;

import java.util.List;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public interface SearchContract {
    interface View{
        void showSearchTags(List<SearchTagEntity> results);
        List<SearchTagEntity> getSelectedTags();
        void showSelectedTags(List<SearchTagEntity> results);
        void showMeals(List<MealEntity> meals);


    }

    interface Presenter{
        void searchForTag(String query);
        void searchForMeals(String query);

        void onDetach();

    }
}
