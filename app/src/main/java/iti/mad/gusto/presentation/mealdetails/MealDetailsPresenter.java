package iti.mad.gusto.presentation.mealdetails;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.MealRepository;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private MealRepository mealRepository;
    private MealDetailsContract.View view;
    private CompositeDisposable compositeDisposable;

    public MealDetailsPresenter(MealDetailsContract.View view) {
        mealRepository = MealRepository.getInstance();
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
                        },
                        t -> {
                            view.hideLoading();
                            view.showError(t.getMessage());
                        }
                );
        compositeDisposable.add(d);
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
    }
}
