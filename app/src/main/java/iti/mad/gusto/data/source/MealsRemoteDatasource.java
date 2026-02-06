package iti.mad.gusto.data.source;

import io.reactivex.rxjava3.core.Single;
import iti.mad.gusto.core.restclient.RetrofitClient;
import iti.mad.gusto.data.model.MealResponse;
import iti.mad.gusto.data.service.MealsService;

public class MealsRemoteDatasource {
    private MealsRemoteDatasource instance;
    public MealsRemoteDatasource getInstance() {
        if (instance == null)
            instance = new MealsRemoteDatasource();
        return instance;
    }
    private final MealsService mealsService;
    private MealsRemoteDatasource() {
        mealsService = RetrofitClient.getInstance().getMealsService();
    }

//    public Single<MealResponse> getRandomMeal() {
//
//    }


}
