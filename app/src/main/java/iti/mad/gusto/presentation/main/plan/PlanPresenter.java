package iti.mad.gusto.presentation.main.plan;

import android.content.Context;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class PlanPresenter implements PlanContract.Presenter {

    private final PlanContract.View view;
    private final PlanRepository repository;
    private final CompositeDisposable disposables;

    public PlanPresenter(PlanContract.View view, Context context) {
        this.view = view;
        this.repository = PlanRepository.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void getMealsForToday() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        getMealsByDate(currentYear, currentMonth, currentDay);
    }

    @Override
    public void getMealsByDate(int year, int month, int day) {
        String formattedDate = formatDate(year, month, day);

        Disposable d = repository.getAllPlansByDate(formattedDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            if (meals == null || meals.isEmpty()) {
                                view.showEmptyState();
                            } else {
                                view.hideEmptyState();
                                view.showMeals(meals);
                            }
                        },
                        t -> view.showError(t.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void deleteMeal(PlanMealEntity meal, int position) {
        Disposable d = repository.deletePlanById(meal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.removeMealFromAdapter(position);
                        },
                        t -> view.showError("Failed to delete: " + t.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void onMealClicked(PlanMealEntity meal) {
        if (meal != null) {
            view.navigateToMealDetails(meal.getId());
        }
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    private String formatDate(int year, int month, int day) {
        return String.format(Locale.US, "%02d/%02d/%d", day, (month + 1), year);
    }
}