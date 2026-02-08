package iti.mad.gusto.data.model;

import java.util.List;

public class IngredientResponse {
    private List<IngredientModel> meals;

    public List<IngredientModel> getIngredients(){
        return meals;
    }
}