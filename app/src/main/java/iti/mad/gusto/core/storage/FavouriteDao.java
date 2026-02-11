package iti.mad.gusto.core.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;

@Dao
public interface FavouriteDao {
    @Query("SELECT * FROM favourites")
    Flowable<List<FavouriteMealEntity>> getAllFavourites();

    @Delete
    Completable deleteFavouriteById(FavouriteMealEntity plan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addFavourite(FavouriteMealEntity plan);
}
