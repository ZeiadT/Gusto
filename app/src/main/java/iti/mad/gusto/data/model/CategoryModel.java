package iti.mad.gusto.data.model;

public class CategoryModel {

    private String strCategory;
    private String strCategoryDescription;
    private String idCategory;
    private String strCategoryThumb;

    public String getStrCategory(){
        return strCategory;
    }

    public String getStrCategoryDescription(){
        return strCategoryDescription;
    }

    public String getIdCategory(){
        return idCategory;
    }

    public String getStrCategoryThumb(){
        return strCategoryThumb;
    }

    @Override
    public String toString() {
        return "CategoriesItem{" +
                "strCategory='" + safe(strCategory) + '\'' +
                ", strCategoryDescription='" + safe(strCategoryDescription) + '\'' +
                ", idCategory='" + safe(idCategory) + '\'' +
                ", strCategoryThumb='" + safe(strCategoryThumb) + '\'' +
                '}';
    }
    private String safe(String value) {
        return value == null ? "" : value;
    }
}
