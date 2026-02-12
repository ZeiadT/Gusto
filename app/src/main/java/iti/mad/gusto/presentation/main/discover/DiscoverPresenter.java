package iti.mad.gusto.presentation.main.discover;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.NetworkManager;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.FavouriteRepository;
import iti.mad.gusto.data.repo.MealRepository;
import iti.mad.gusto.data.repo.PlanRepository;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.domain.entity.MealType;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class DiscoverPresenter implements DiscoverContract.Presenter {

    DiscoverContract.View view;
    MealRepository mealRepository;
    AuthRepository authRepository;
    PlanRepository planRepository;
    FavouriteRepository favouriteRepository;
    CompositeDisposable disposables;
    Context context;

    public DiscoverPresenter(Context context, DiscoverContract.View view) {
        this.view = view;
        this.context = context;
        mealRepository = MealRepository.getInstance();
        planRepository = PlanRepository.getInstance(context);
        favouriteRepository = FavouriteRepository.getInstance(context);
        authRepository = AuthRepository.getInstance(context);
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
        if (authRepository.isAnonymousUser()) {
            String pleaseSignIn = context.getString(R.string.please_sign_in);
            view.showWarning(pleaseSignIn);
            return;
        }

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
    public void onFeaturedMealAddToFavourite() {
        if (authRepository.isAnonymousUser()) {
            String pleaseSignIn = context.getString(R.string.please_sign_in);
            view.showWarning(pleaseSignIn);
            return;
        }

        Disposable d = mealRepository.getRandomMeal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealEntity -> {
                            Disposable dd = favouriteRepository.addFavourite(new FavouriteMealEntity(mealEntity.getId(), mealEntity.getName(), mealEntity.getImage(), mealEntity.getCategory(), mealEntity.getArea()))
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
    public void addConnectivityListener(Context context) {
        if (NetworkManager.isNetworkDisconnected(context)){
            view.onNetworkDisconnected();
        }

        NetworkManager.addConnectivityListener(context, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                view.onNetworkReconnected();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                view.onNetworkDisconnected();
            }
        });
    }

    @Override
    public boolean isNetworkDisconnected(Context context) {
        return NetworkManager.isNetworkDisconnected(context);
    }

    @Override
    public void onDetach() {
        disposables.dispose();

    }
}
