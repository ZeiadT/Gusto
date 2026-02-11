package iti.mad.gusto.data.repo;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import iti.mad.gusto.data.source.PlanLocalDatasource;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class PlanRepository {
    private final PlanLocalDatasource localDatasource;

    private PlanRepository(Context context) {
        this.localDatasource = PlanLocalDatasource.getInstance(context);
    }

    private static PlanRepository instance;
    public static PlanRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PlanRepository(context);
        }
        return instance;
    }

    public Flowable<List<PlanMealEntity>> getAllPlansByDate(String date) {
        return localDatasource.getAllPlansByDate(date);
    }

    public Completable deletePlanById(PlanMealEntity plan) {
        return localDatasource.deletePlanById(plan);
    }

    public Completable addPlan(PlanMealEntity plan) {
        return localDatasource.addPlan(plan);
    }
}
