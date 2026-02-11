package iti.mad.gusto.presentation.main.plan;

import android.content.Context;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class PlanPresenter implements PlanContract.Presenter {

    private final PlanContract.View view;
    private final PlanRepository repository;

    private final CompositeDisposable disposables;

    public PlanPresenter(Context context, PlanContract.View view) {
        this.view = view;
        this.repository = PlanRepository.getInstance(context);
        disposables = new CompositeDisposable();
    }


    @Override
    public void getMealsByDate(String date) {

        Disposable d = repository.getAllPlansByDate(date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showMeals, t ->
                        view.showError(t.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void addMeal(PlanMealEntity meal) {
        Disposable d = repository.addPlan(meal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        }, t ->
                                view.showError(t.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void deletePlan(PlanMealEntity meal) {
        Disposable d = repository.deletePlanById(meal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, t -> view.showError(t.getMessage()));

        disposables.add(d);
    }

    @Override
    public void onDateChanged(String newDate) {


    }

    @Override
    public void onDetach() {
        disposables.clear();
    }
}
