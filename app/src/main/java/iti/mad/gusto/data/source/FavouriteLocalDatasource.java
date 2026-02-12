package iti.mad.gusto.data.source;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.data.service.FavouriteDao;
import iti.mad.gusto.core.storage.RoomManager;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;

public class FavouriteLocalDatasource {
    private final FavouriteDao favouriteDao;

    private static FavouriteLocalDatasource instance;
    private FavouriteLocalDatasource(Context context) {
        this.favouriteDao = RoomManager.getInstance(context.getApplicationContext()).favouriteDao();
    }
    public static FavouriteLocalDatasource getInstance(Context context) {
        if (instance == null) {
            instance = new FavouriteLocalDatasource(context);
        }
        return instance;
    }

    public Flowable<List<FavouriteMealEntity>> getAllFavourites() {
        return favouriteDao.getAllFavourites().subscribeOn(Schedulers.io());
    }

    public Completable deleteFavouriteById(FavouriteMealEntity plan) {
        return favouriteDao.deleteFavouriteById(plan).subscribeOn(Schedulers.io());
    }

    public Completable addFavourite(FavouriteMealEntity plan) {
        return favouriteDao.addFavourite(plan).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isFavourite(String mealId) {
        return favouriteDao.isFavourite(mealId).subscribeOn(Schedulers.io());
    }

    public Completable dropFavMeals() {
        return favouriteDao.dropFavMeals().subscribeOn(Schedulers.io());
    }

}
