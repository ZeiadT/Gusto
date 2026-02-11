package iti.mad.gusto.data.source;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.core.storage.PlanDao;
import iti.mad.gusto.core.storage.RoomManager;
import iti.mad.gusto.domain.entity.PlanMealEntity;

public class PlanLocalDatasource {
    private final PlanDao planDao;

    private PlanLocalDatasource(Context context) {
        this.planDao = RoomManager.getInstance(context).planDao();
    }

    private static PlanLocalDatasource instance;

    public static PlanLocalDatasource getInstance(Context context) {
        if (instance == null) {
            instance = new PlanLocalDatasource(context);
        }
        return instance;
    }

    public Flowable<List<PlanMealEntity>> getAllPlansByDate(String date) {
        return planDao.getAllPlansByDate(date).subscribeOn(Schedulers.io());
    }

    public Completable deletePlanById(PlanMealEntity plan) {
        return planDao.deletePlanById(plan).subscribeOn(Schedulers.io());
    }

    public Completable addPlan(PlanMealEntity plan) {
        return planDao.addPlan(plan).subscribeOn(Schedulers.io());
    }
}
