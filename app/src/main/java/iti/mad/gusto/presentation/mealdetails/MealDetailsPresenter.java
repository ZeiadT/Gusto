package iti.mad.gusto.presentation.mealdetails;

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
import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.MealType;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private final MealDetailsContract.View view;
    private final Context context;
    private final AuthRepository authRepository;
    private final MealRepository mealRepository;
    private final PlanRepository planRepository;
    private final FavouriteRepository favouriteRepository;
    private final CompositeDisposable compositeDisposable;
    private MealEntity meal;

    public MealDetailsPresenter(Context context, MealDetailsContract.View view) {
        this.view = view;
        this.context = context;
        authRepository = AuthRepository.getInstance(context);
        mealRepository = MealRepository.getInstance();
        planRepository = PlanRepository.getInstance(context);
        favouriteRepository = FavouriteRepository.getInstance(context);
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

        if(authRepository.isAnonymousUser()){
            String pleaseSignIn = context.getString(R.string.please_sign_in);
            view.showWarning(pleaseSignIn);
            return;
        }

        Disposable d = planRepository.addPlan(new PlanMealEntity(meal, date, type))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, t -> {
                    view.showError(t.getMessage());
                });

        compositeDisposable.add(d);
    }

    @Override
    public void onFavoriteClicked() {

        if(authRepository.isAnonymousUser()){
            String pleaseSignIn = context.getString(R.string.please_sign_in);
            view.showWarning(pleaseSignIn);
            return;
        }

        Disposable dd = favouriteRepository.addFavourite(new FavouriteMealEntity(meal.getId(), meal.getName(), meal.getImage(), meal.getCategory(), meal.getArea()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                        },
                        t -> view.showError(t.getMessage())
                );

        compositeDisposable.add(dd);
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
    public void onDetach() {
        compositeDisposable.dispose();
    }
}
