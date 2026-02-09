package iti.mad.gusto.data.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import iti.mad.gusto.R;
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
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.domain.mapper.ModelToEntityMapper;

public class MealRemoteDatasource {
    private final MealService mealService;
    private static MealRemoteDatasource instance;

    private MealRemoteDatasource() {
        this.mealService = RetrofitClient.getInstance().getMealsService();
    }

    public static MealRemoteDatasource getInstance() {
        if (instance == null)
            instance = new MealRemoteDatasource();

        return instance;
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

    public Observable<List<MealModel>> searchByNameAndTags(String query, List<SearchTagEntity> tags) {

        List<Single<List<MealModel>>> searchTasks = new ArrayList<>();

        if(query != null && !query.isEmpty())
            searchTasks.add(searchByName(query));

        if (tags != null && !tags.isEmpty()) {
            for (SearchTagEntity tag : tags) {
                switch (tag.getTagType()) {
                    case COUNTRY:
                        searchTasks.add(searchByArea(tag.getTagName()));
                        break;
                    case INGREDIENT:
                        searchTasks.add(searchByIngredient(tag.getTagName()));
                        break;
                    default:
                        searchTasks.add(searchByCategory(tag.getTagName()));
                        break;
                }
            }
        }

        return Single.merge(searchTasks) // Merges all Singles into one Observable stream
                .observeOn(Schedulers.io())
                .reduce((oldList, newList) -> {

                    List<MealModel> intersection = new ArrayList<>(oldList);
                    intersection.retainAll(newList);
                    return intersection;
                })
                .toObservable();
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

    public Observable<List<SearchTagEntity>> searchForTags(String query) {
        final String normalizedQuery = prepareToCompare(query);

        if (normalizedQuery.isEmpty()) {
            return Observable.just(Collections.emptyList());
        }

        Single<List<SearchTagEntity>> categoryTags = getCategories()
                .map(list -> {
                    List<SearchTagEntity> tags = new ArrayList<>();
                    for (CategoryModel category : list) {
                        String itemValue = prepareToCompare(category.getStrCategory());
                        if (itemValue.contains(normalizedQuery)) {
                            tags.add(ModelToEntityMapper.mapTag(category));
                        }
                    }
                    return tags;
                })
                .compose(ioScheduler());

        Single<List<SearchTagEntity>> countryTags = getAreas()
                .map(list -> {
                    List<SearchTagEntity> tags = new ArrayList<>();
                    for (CountryModel country : list) {
                        String itemValue = prepareToCompare(country.getStrArea());
                        if (itemValue.contains(normalizedQuery)) {
                            tags.add(ModelToEntityMapper.mapTag(country));
                        }
                    }
                    return tags;
                })
                .compose(ioScheduler());

        Single<List<SearchTagEntity>> ingredientTags = getIngredients()
                .map(list -> {
                    List<SearchTagEntity> tags = new ArrayList<>();
                    for (IngredientModel ingredient : list) {
                        String itemValue = prepareToCompare(ingredient.getStrIngredient());
                        if (itemValue.contains(normalizedQuery)) {
                            tags.add(ModelToEntityMapper.mapTag(ingredient));
                        }
                    }
                    return tags;
                })
                .compose(ioScheduler());

        return Single.zip(categoryTags, countryTags, ingredientTags,
                        (categories, countries, ingredients) -> {
                            List<SearchTagEntity> allTags = new ArrayList<>();
                            allTags.addAll(categories);
                            allTags.addAll(countries);
                            allTags.addAll(ingredients);
                            return allTags;
                        })
                .compose(computationScheduler())
                .toObservable();
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

    private <T> SingleTransformer<T, T> computationScheduler() {
        return upstream -> upstream
                .subscribeOn(Schedulers.computation());
    }

    private String prepareToCompare(String text) {
        return text.toLowerCase().trim();
    }
}