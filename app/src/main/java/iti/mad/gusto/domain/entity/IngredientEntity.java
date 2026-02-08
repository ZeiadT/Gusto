package iti.mad.gusto.domain.entity;

public class IngredientEntity {
    private final String name;
    private final String measure;
    private final String image;

    public IngredientEntity(String name, String measure, String image) {
        this.name = name;
        this.measure = measure;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", measure='" + measure + '\'' +
                ", image='" + image + '\'' +
                '}' + "\n";
    }
}