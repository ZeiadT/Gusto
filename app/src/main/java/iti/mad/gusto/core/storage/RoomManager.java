package iti.mad.gusto.core.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.domain.entity.PlanMealEntity;

@Database(entities = {PlanMealEntity.class, FavouriteMealEntity.class}, version = 1)
public abstract class RoomManager extends RoomDatabase {
    public abstract PlanDao planDao();
    public abstract FavouriteDao favouriteDao();

    private static final String DB_NAME = "gusto.db";

    private static RoomManager instance;

    public static RoomManager getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomManager.class, DB_NAME).build();
        }
        return instance;
    }
}


