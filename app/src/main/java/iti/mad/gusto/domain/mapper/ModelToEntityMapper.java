package iti.mad.gusto.domain.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.data.model.IngredientModel;
import iti.mad.gusto.data.model.MealModel;
import iti.mad.gusto.data.model.CategoryModel;
import iti.mad.gusto.data.model.CountryModel;

import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.domain.entity.CountryEntity;
import iti.mad.gusto.domain.entity.IngredientEntity;
import iti.mad.gusto.domain.entity.InstructionEntity;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.domain.entity.TagType;

public class ModelToEntityMapper {
    public static SearchTagEntity mapTag(CategoryModel category) {

        return new SearchTagEntity(category.getStrCategory(), TagType.CATEGORY);
    }
    public static SearchTagEntity mapTag(CountryModel country) {

        return new SearchTagEntity(country.getStrArea(), TagType.COUNTRY);
    }
    public static SearchTagEntity mapTag(IngredientModel ingredient) {

        return new SearchTagEntity(ingredient.getStrIngredient(), TagType.INGREDIENT);
    }

    public static MealEntity map(MealModel item) {
        MealEntity mealEntity = new MealEntity();

        mealEntity.setName(item.getStrMeal());
        mealEntity.setImage(item.getStrMealThumb());
        mealEntity.setCategory(item.getStrCategory());
        mealEntity.setArea(item.getStrArea());
        mealEntity.setYoutube(item.getStrYoutube());

        mealEntity.setIngredients(mapIngredients(item));
        mealEntity.setInstructions(mapInstructions(item.getStrInstructions()));

        return mealEntity;
    }

    public static List<MealEntity> mapList(List<MealModel> items) {
        List<MealEntity> meals = new ArrayList<>();
        if (items == null) return meals;

        for (MealModel item : items) {
            meals.add(map(item));
        }
        return meals;
    }


    public static CategoryEntity map(CategoryModel item) {
        // Assuming CategoryEntity constructor takes (name, image, desc, icon)
        return new CategoryEntity(
                item.getStrCategory(),
                item.getStrCategoryThumb(),
                item.getStrCategoryDescription(),
                "🍽️"
        );
    }

    public static List<CategoryEntity> mapCategories(List<CategoryModel> items) {
        List<CategoryEntity> categories = new ArrayList<>();
        if (items == null) return categories;
        for (CategoryModel item : items) {
            categories.add(map(item));
        }
        return categories;
    }


    public static CountryEntity map(CountryModel item) {
        return new CountryEntity(item.getStrArea());
    }

    public static List<CountryEntity> mapCountries(List<CountryModel> items) {
        List<CountryEntity> countries = new ArrayList<>();
        if (items == null) return countries;
        for (CountryModel item : items) {
            countries.add(map(item));
        }
        return countries;
    }


    public static List<IngredientEntity> mapIngredients(MealModel item) {
        List<IngredientEntity> ingredients = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            try {
                Field ingredientField =
                        MealModel.class.getDeclaredField("strIngredient" + i);
                Field measureField =
                        MealModel.class.getDeclaredField("strMeasure" + i);

                ingredientField.setAccessible(true);
                measureField.setAccessible(true);

                String ingredient = (String) ingredientField.get(item);
                String measure = (String) measureField.get(item);

                if (ingredient != null && !ingredient.trim().isEmpty()) {
                    String image = "https://www.themealdb.com/images/ingredients/" + ingredient.toLowerCase() + ".png";
                    ingredients.add(
                            new IngredientEntity(ingredient, measure, image)
                    );
                }

            } catch (Exception ignored) {
            }
        }

        return ingredients;
    }


    public static List<IngredientEntity> mapIngredientsFromModel(List<IngredientModel> items) {

        List<IngredientEntity> ingredients = new ArrayList<>();
        if (items == null) return ingredients;

        for (IngredientModel item : items) {
            IngredientEntity entity = new IngredientEntity(
                    item.getStrIngredient(),
                    "",
                    item.getStrThumb()
            );

            ingredients.add(entity);
        }

        return ingredients;
    }

    private static List<InstructionEntity> mapInstructions(String instructionsText) {
        List<InstructionEntity> instructions = new ArrayList<>();

        if (instructionsText == null) return instructions;

        // Split by period to create steps
        String[] steps = instructionsText.split("\\.");

        for (String step : steps) {
            if (!step.trim().isEmpty()) {
                instructions.add(new InstructionEntity(step.trim()));
            }
        }
        return instructions;
    }

}