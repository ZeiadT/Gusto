package iti.mad.gusto.domain.entity;

import java.util.List;

public class CategoryEntity {
    private final String name;
    private final String image;
    private final String imageIcon;
    private final String description;

    public CategoryEntity(String name, String image, String description, String imageIcon) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.imageIcon = imageIcon;
    }

    public String getName() {
        return name;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public static List<CategoryEntity> homeCategoryList() {
        return List.of(
                new CategoryEntity(
                        "Beef",
                        "https://www.themealdb.com/images/category/beef.png",
                        "Beef is the culinary name for meat from cattle, particularly skeletal muscle.",
                        "🥩"
                ),
                new CategoryEntity(
                        "Chicken",
                        "https://www.themealdb.com/images/category/chicken.png",
                        "Chicken is a type of domesticated fowl and one of the most common food sources worldwide.",
                        "🍗"
                ),
                new CategoryEntity(
                        "Dessert",
                        "https://www.themealdb.com/images/category/dessert.png",
                        "Dessert is a course that concludes a meal, usually consisting of sweet foods.",
                        "🍰"
                ),
                new CategoryEntity(
                        "Lamb",
                        "https://www.themealdb.com/images/category/lamb.png",
                        "Lamb, hogget, and mutton are the meat of domestic sheep at different ages.",
                        "🐑"
                ),
                new CategoryEntity(
                        "Miscellaneous",
                        "https://www.themealdb.com/images/category/miscellaneous.png",
                        "General foods that don't fit into another category.",
                        "🥙"
                ),
                new CategoryEntity(
                        "Pasta",
                        "https://www.themealdb.com/images/category/pasta.png",
                        "Pasta is a staple food of traditional Italian cuisine.",
                        "🍝"
                ),
                new CategoryEntity(
                        "Pork",
                        "https://www.themealdb.com/images/category/pork.png",
                        "Pork is the culinary name for meat from a domestic pig.",
                        "🐖"
                ),
                new CategoryEntity(
                        "Seafood",
                        "https://www.themealdb.com/images/category/seafood.png",
                        "Seafood is any form of sea life regarded as food by humans.",
                        "🦞"
                ),
                new CategoryEntity(
                        "Side",
                        "https://www.themealdb.com/images/category/side.png",
                        "A side dish accompanies the main course of a meal.",
                        "🥗"
                ),
                new CategoryEntity(
                        "Starter",
                        "https://www.themealdb.com/images/category/starter.png",
                        "A dish served before the main course of a meal.",
                        "🥣"
                ),
                new CategoryEntity(
                        "Vegan",
                        "https://www.themealdb.com/images/category/vegan.png",
                        "Veganism excludes all animal products from the diet.",
                        "🍅"
                ),
                new CategoryEntity(
                        "Vegetarian",
                        "https://www.themealdb.com/images/category/vegetarian.png",
                        "Vegetarianism abstains from consuming meat.",
                        "🥦"
                ),
                new CategoryEntity(
                        "Breakfast",
                        "https://www.themealdb.com/images/category/breakfast.png",
                        "Breakfast is the first meal of the day.",
                        "🍳"
                ),
                new CategoryEntity(
                        "Goat",
                        "https://www.themealdb.com/images/category/goat.png",
                        "Goat meat and milk are used widely across the world.",
                        "🍖"
                )
        );

    }
}