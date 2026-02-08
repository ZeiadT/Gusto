package iti.mad.gusto.data.source;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.core.restclient.RetrofitClient;
import iti.mad.gusto.data.model.CategoryModel;
import iti.mad.gusto.data.model.CategoryResponse;
import iti.mad.gusto.data.model.CountryModel;
import iti.mad.gusto.data.model.CountryResponse;
import iti.mad.gusto.data.model.IngredientModel;
import iti.mad.gusto.data.model.IngredientResponse;
import iti.mad.gusto.data.model.MealModel;
import iti.mad.gusto.data.model.MealResponse;
import iti.mad.gusto.data.service.MealService;

public class MealRemoteDatasource {
    private final MealService mealService;

    public MealRemoteDatasource() {
        this.mealService = RetrofitClient.getInstance().getMealsService();
    }

    public Single<List<MealModel>> getMeals() {
        return mealService.getMeals().map(MealResponse::getMeals).compose(ioScheduler());
    }

    public Single<MealModel> getRandomMeal() {
        return mealService.getRandomMeal().map(response -> response.getMeals().get(0)).compose(ioScheduler());
    }

    public Single<List<MealModel>> searchByName(String query) {
        return mealService.searchByName(query).map(MealResponse::getMeals).compose(ioScheduler());
    }

    public Single<MealModel> getById(String id) {
        return mealService.getById(id).map(response -> response.getMeals().get(0)).compose(ioScheduler());
    }

    public Single<List<MealModel>> searchByCategory(String query) {
        return mealService.searchByCategory(query).map(MealResponse::getMeals).compose(ioScheduler());
    }

    public Single<List<MealModel>> searchByArea(String query) {
        return mealService.searchByArea(query).map(MealResponse::getMeals).compose(ioScheduler());
    }

    public Single<List<MealModel>> searchByIngredient(String query) {
        return mealService.searchByIngredient(query).map(MealResponse::getMeals).compose(ioScheduler());
    }

    public Single<List<CategoryModel>> getCategories() {
        return mealService.getCategories().map(CategoryResponse::getCategories).compose(ioScheduler());
    }

    public Single<List<CountryModel>> getAreas() {
        return mealService.getAreas().map(CountryResponse::getCountries).compose(ioScheduler());
    }

    public Single<List<IngredientModel>> getIngredients() {
        return mealService.getIngredients().map(IngredientResponse::getIngredients).compose(ioScheduler());
    }

    private <T> SingleTransformer<T, T> ioScheduler() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io());
    }
}