package iti.mad.gusto.presentation.main.favourite;

import android.content.Context;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.data.repo.FavouriteRepository;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;

public class FavouritePresenter implements FavouriteContract.Presenter{

    private final FavouriteContract.View view;
    private final FavouriteRepository repository;
    private final CompositeDisposable disposables;

    public FavouritePresenter(Context context, FavouriteContract.View view) {
        this.view = view;
        this.repository = FavouriteRepository.getInstance(context);
        disposables = new CompositeDisposable();
    }

    @Override
    public void getMeals() {
        Disposable d = repository.getAllFavourites()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showMeals,
                        throwable -> view.showError(throwable.getMessage())
                );

        disposables.add(d);
    }

    @Override
    public void deleteMeal(FavouriteMealEntity meal) {
        Disposable d = repository.deleteFavouriteById(meal).observeOn(AndroidSchedulers.mainThread()).subscribe(
                this::getMeals,
                throwable -> view.showError(throwable.getMessage())
        );

        disposables.add(d);
    }



    @Override
    public void onDetach() {
        disposables.clear();
    }
}
