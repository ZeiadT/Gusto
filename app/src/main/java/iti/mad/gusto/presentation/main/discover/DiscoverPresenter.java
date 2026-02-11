package iti.mad.gusto.presentation.main.discover;


import android.content.Context;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.MealRepository;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class DiscoverPresenter implements DiscoverContract.Presenter {

    DiscoverContract.View view;
    MealRepository mealRepository;
    PlanRepository planRepository;
    CompositeDisposable disposables;

    public DiscoverPresenter(Context context, DiscoverContract.View view) {
        this.view = view;
        mealRepository = MealRepository.getInstance();
        planRepository = PlanRepository.getInstance(context);
        disposables = new CompositeDisposable();
    }

    @Override
    public void onViewCreated() {
        getFeaturedMeal();
        getCategories();
        getCountries();
    }

    @Override
    public void getFeaturedMeal() {
        Disposable disposable = mealRepository.getRandomMeal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealEntity -> view.setFeaturedMeal(mealEntity),
                        t -> view.showError(t.getMessage())
                );

        disposables.add(disposable);
    }

    @Override
    public void getCategories() {

        Disposable disposable = mealRepository.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.setCategories(categories),
                        t -> view.showError(t.getMessage())
                );

        disposables.add(disposable);

    }

    @Override
    public void getCountries() {

        Disposable disposable = mealRepository.getAreas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries -> view.setCountries(countries.subList(0, 10)),
                        t -> view.showError(t.getMessage())
                );

        disposables.add(disposable);

    }

    @Override
    public void onTagClicked(CategoryEntity category) {

    }

    @Override
    public void onFeaturedMealAddToPlan(String date, MealType meal) {
        Disposable d = mealRepository.getRandomMeal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealEntity -> {
                            Disposable dd = planRepository.addPlan(new PlanMealEntity(mealEntity, date, meal))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                            },
                                            t -> view.showError(t.getMessage())
                                    );

                            disposables.add(dd);
                        },
                        t -> view.showError(t.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void onFeaturedMealAddToFavourite(MealEntity meal) {

    }

    @Override
    public void onDetach() {
        disposables.dispose();

    }
}
