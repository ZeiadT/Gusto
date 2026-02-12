package iti.mad.gusto.domain.entity;

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

    public SearchTagEntity toTag() {
        return new SearchTagEntity(name, TagType.CATEGORY);
    }



}