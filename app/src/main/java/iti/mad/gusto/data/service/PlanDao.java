package iti.mad.gusto.data.service;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import iti.mad.gusto.domain.entity.PlanMealEntity;

@Dao
public interface PlanDao {
    @Query("SELECT * FROM plans WHERE date = :date")
    Flowable<List<PlanMealEntity>> getAllPlansByDate(String date);

    @Delete
    Completable deletePlanById(PlanMealEntity plan);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addPlan(PlanMealEntity plan);


    @Query("DELETE FROM plans")
    Completable dropPlanMeals();
}
