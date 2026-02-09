package iti.mad.gusto.domain.entity;

import androidx.annotation.NonNull;

import java.util.List;

public class MealEntity {
    private String name;
    private String image;
    private String category;
    private String area;
    private String youtube;
    private List<IngredientEntity> ingredients;
    private List<InstructionEntity> instructions;

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<InstructionEntity> instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getYoutube() {
        return youtube;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public List<InstructionEntity> getInstructions() {
        return instructions;
    }

    @NonNull
    @Override
    public String toString() {
        return "MealEntity{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", area='" + area + '\'' +
                ", image='" + image + '\'' +
                ", youtube='" + youtube + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}