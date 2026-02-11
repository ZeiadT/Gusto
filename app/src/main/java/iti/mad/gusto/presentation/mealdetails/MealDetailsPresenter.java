package iti.mad.gusto.presentation.mealdetails;

import android.content.Context;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.MealRepository;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private MealRepository mealRepository;
    private PlanRepository planRepository;
    private MealDetailsContract.View view;
    private CompositeDisposable compositeDisposable;
    private MealEntity meal;

    public MealDetailsPresenter(Context context, MealDetailsContract.View view) {
        mealRepository = MealRepository.getInstance();
        planRepository = PlanRepository.getInstance(context);
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMealDetails(String mealId) {
        view.showLoading();
        Disposable d = mealRepository.getById(mealId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meal -> {
                            view.hideLoading();
                            view.showMealDetails(meal);
                            this.meal = meal;
                        },
                        t -> {
                            view.hideLoading();
                            view.showError(t.getMessage());
                        }
                );
        compositeDisposable.add(d);
    }

    @Override
    public void onFeaturedMealAddToPlan(String date, MealType type) {
        Disposable d = planRepository.addPlan(new PlanMealEntity(meal, date, type))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, t -> {
                    view.showError(t.getMessage());
                });

        compositeDisposable.add(d);
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
    }
}
