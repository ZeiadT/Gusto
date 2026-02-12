package iti.mad.gusto.data.repo;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import iti.mad.gusto.data.source.FavouriteLocalDatasource;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;

public class FavouriteRepository {
    private final FavouriteLocalDatasource localDatasource;

    private FavouriteRepository(Context context) {
        this.localDatasource = FavouriteLocalDatasource.getInstance(context);
    }

    private static FavouriteRepository instance;

    public static FavouriteRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FavouriteRepository(context);
        }
        return instance;
    }

    public Flowable<List<FavouriteMealEntity>> getAllFavourites() {
        return localDatasource.getAllFavourites();
    }

    public Completable deleteFavouriteById(FavouriteMealEntity plan) {
        return localDatasource.deleteFavouriteById(plan);
    }

    public Completable addFavourite(FavouriteMealEntity plan) {
        return localDatasource.addFavourite(plan);
    }

    public Single<Boolean> isFavourite(String mealId) {
        return localDatasource.isFavourite(mealId);
    }

    public Completable dropFavMeals() {
        return localDatasource.dropFavMeals();
    }
}
