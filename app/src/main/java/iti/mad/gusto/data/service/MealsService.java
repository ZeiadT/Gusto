package iti.mad.gusto.data.service;

import io.reactivex.rxjava3.core.Single;
import iti.mad.gusto.data.model.MealResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsService {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();
    @GET("search.php")
    Single<MealResponse> searchByName(@Query("s") String query);
    @GET("lookup.php")
    Single<MealResponse> searchById(@Query("i") String query);
    @GET("filter.php")
    Single<MealResponse> searchByCategory(@Query("c") String query);
    @GET("filter.php")
    Single<MealResponse> searchByArea(@Query("a") String query);
    @GET("filter.php")
    Single<MealResponse> searchByIngredient(@Query("i") String query);
    @GET("list.php?c=list")
    Single<MealResponse> getCategories();
    @GET("list.php?a=list")
    Single<MealResponse> getAreas();
    @GET("list.php?i=list")
    Single<MealResponse> getIngredients();
}
