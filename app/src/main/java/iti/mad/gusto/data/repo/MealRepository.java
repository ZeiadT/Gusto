package iti.mad.gusto.data.repo;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.data.source.MealRemoteDatasource;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.IngredientEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.mapper.ModelToEntityMapper;

public class MealRepository {

    private final MealRemoteDatasource remoteDataSource;

    public MealRepository() {
        this.remoteDataSource = new MealRemoteDatasource();
    }

    public Single<List<MealEntity>> getMeals() {
        return remoteDataSource.getMeals().map(ModelToEntityMapper::mapList);
    }

    public Single<MealEntity> getRandomMeal() {
        return remoteDataSource.getRandomMeal().map(ModelToEntityMapper::map);
    }

    public Single<List<MealEntity>> searchByName(String query) {
        return remoteDataSource.searchByName(query).map(ModelToEntityMapper::mapList);
    }

    public Single<MealEntity> getById(String query) {
        return remoteDataSource.getById(query).map(ModelToEntityMapper::map);
    }

    public Single<List<MealEntity>> searchByCategory(String query) {
        return remoteDataSource.searchByCategory(query).map(ModelToEntityMapper::mapList);
    }

    public Single<List<MealEntity>> searchByArea(String query) {
        return remoteDataSource.searchByArea(query).map(ModelToEntityMapper::mapList);
    }

    public Single<List<MealEntity>> searchByIngredient(String query) {
        return remoteDataSource.searchByIngredient(query).map(ModelToEntityMapper::mapList);
    }

    public Single<List<CategoryEntity>> getCategories() {
        return remoteDataSource.getCategories().map(ModelToEntityMapper::mapCategories);
    }

    public Single<List<CountryEntity>> getAreas() {
        return remoteDataSource.getAreas().map(ModelToEntityMapper::mapCountries);
    }

    public Single<List<IngredientEntity>> getIngredients() {
        return remoteDataSource.getIngredients().map(ModelToEntityMapper::mapIngredientsFromModel);
    }

}
