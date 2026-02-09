package iti.mad.gusto.presentation.main.search;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.MealRepository;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public class SearchPresenter implements SearchContract.Presenter {
    SearchContract.View view;

    MealRepository mealRepository;
    CompositeDisposable disposables;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
        mealRepository = MealRepository.getInstance();
        disposables = new CompositeDisposable();
    }


    @Override
    public void searchForTag(String query) {
        Disposable d = mealRepository.searchForTag(query)
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribe(view::showSearchTags);

        disposables.add(d);
    }

    @Override
    public void searchForMeals(String query) {
        List<SearchTagEntity> selectedTags = view.getSelectedTags();

        Disposable d = mealRepository.searchByNameAndTags(query, selectedTags)
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribe(meals -> {
                    view.showMeals(meals);
                });
    }

    @Override
    public void onDetach() {
        disposables.dispose();
    }
}
