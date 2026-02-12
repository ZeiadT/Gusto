package iti.mad.gusto.data.repo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.data.source.MealMemoryDatasource;
import iti.mad.gusto.data.source.MealRemoteDatasource;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.IngredientEntity;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.domain.mapper.ModelToEntityMapper;

public class MealRepository {

    private final MealRemoteDatasource remoteDataSource;
    private final MealMemoryDatasource memoryDatasource;

    private static MealRepository instance;
    private MealRepository() {
        this.remoteDataSource = MealRemoteDatasource.getInstance();
        memoryDatasource = MealMemoryDatasource.getInstance();
    }
    public static MealRepository getInstance() {
        if (instance == null)
            instance = new MealRepository();

        return instance;
    }


    public Single<List<MealEntity>> getMeals() {
        return remoteDataSource.getMeals().map(ModelToEntityMapper::mapList);
    }

    public Single<MealEntity> getRandomMeal() {
        if (memoryDatasource.getRandomMeal() != null) {
            return Single.just(memoryDatasource.getRandomMeal()).map(ModelToEntityMapper::map);
        }

        return remoteDataSource.getRandomMeal().map(meal -> {
            memoryDatasource.setRandomMeal(meal);
            return ModelToEntityMapper.map(meal);
        });
    }

    public Observable<List<SearchTagEntity>> searchForTag(String query) {
        return remoteDataSource.searchForTags(query);
    }

    public Single<List<MealEntity>> searchByName(String query) {
        return remoteDataSource.searchByName(query).map(ModelToEntityMapper::mapList);
    }

    public Observable<List<MealEntity>> searchByNameAndTags(String query, List<SearchTagEntity> tags) {
        return remoteDataSource.searchByNameAndTags(query, tags).map(ModelToEntityMapper::mapList);
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
        if (memoryDatasource.getCategories() != null) {
            return Single.just(memoryDatasource.getCategories()).map(ModelToEntityMapper::mapCategories);
        }

        return remoteDataSource.getCategories().map(categories -> {
            memoryDatasource.setCategories(categories);
            return ModelToEntityMapper.mapCategories(categories);
        });
    }

    public Single<List<CountryEntity>> getAreas() {
        if (memoryDatasource.getAreas() != null) {
            return Single.just(memoryDatasource.getAreas()).map(ModelToEntityMapper::mapCountries);
        }

        return remoteDataSource.getAreas().map(areas -> {
            memoryDatasource.setAreas(areas);
            return ModelToEntityMapper.mapCountries(areas);
        });
    }

    public Single<List<IngredientEntity>> getIngredients() {
        return remoteDataSource.getIngredients().map(ModelToEntityMapper::mapIngredientsFromModel);
    }

}
