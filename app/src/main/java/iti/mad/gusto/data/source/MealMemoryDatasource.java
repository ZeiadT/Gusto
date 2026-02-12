package iti.mad.gusto.data.source;

import java.util.List;

import iti.mad.gusto.data.model.CategoryModel;
import iti.mad.gusto.data.model.CountryModel;
import iti.mad.gusto.data.model.MealModel;

public class MealMemoryDatasource {
    private static MealMemoryDatasource instance;

    private MealMemoryDatasource() {
    }

    public static MealMemoryDatasource getInstance() {
        if (instance == null)
            instance = new MealMemoryDatasource();

        return instance;
    }

    private MealModel randomMeal;

    public MealModel getRandomMeal() {
        return randomMeal;
    }

    public void setRandomMeal(MealModel randomMeal) {
        this.randomMeal = randomMeal;
    }

    public void clearRandomMeal() {
        this.randomMeal = null;
    }

    private List<CategoryModel> categories;

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public void clearCategories() {
        this.categories = null;
    }

    private List<CountryModel> areas;

    public List<CountryModel> getAreas() {
        return areas;
    }

    public void setAreas(List<CountryModel> areas) {
        this.areas = areas;
    }

    public void clearAreas() {
        this.areas = null;
    }
}
