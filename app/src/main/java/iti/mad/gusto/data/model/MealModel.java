package iti.mad.gusto.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MealModel implements Parcelable {

    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String strMeal;

    @SerializedName("strMealAlternate")
    private String strMealAlternate;

    @SerializedName("strCategory")
    private String strCategory;

    @SerializedName("strArea")
    private String strArea;

    @SerializedName("strInstructions")
    private String strInstructions;

    @SerializedName("strMealThumb")
    private String strMealThumb;

    @SerializedName("strTags")
    private String strTags;

    @SerializedName("strYoutube")
    private String strYoutube;

    @SerializedName("strSource")
    private String strSource;

    @SerializedName("strImageSource")
    private String strImageSource;

    @SerializedName("strCreativeCommonsConfirmed")
    private String strCreativeCommonsConfirmed;

    @SerializedName("dateModified")
    private String dateModified;

    // Ingredient fields (raw JSON fields)
    @SerializedName("strIngredient1")
    private String strIngredient1;
    @SerializedName("strIngredient2")
    private String strIngredient2;
    @SerializedName("strIngredient3")
    private String strIngredient3;
    @SerializedName("strIngredient4")
    private String strIngredient4;
    @SerializedName("strIngredient5")
    private String strIngredient5;
    @SerializedName("strIngredient6")
    private String strIngredient6;
    @SerializedName("strIngredient7")
    private String strIngredient7;
    @SerializedName("strIngredient8")
    private String strIngredient8;
    @SerializedName("strIngredient9")
    private String strIngredient9;
    @SerializedName("strIngredient10")
    private String strIngredient10;
    @SerializedName("strIngredient11")
    private String strIngredient11;
    @SerializedName("strIngredient12")
    private String strIngredient12;
    @SerializedName("strIngredient13")
    private String strIngredient13;
    @SerializedName("strIngredient14")
    private String strIngredient14;
    @SerializedName("strIngredient15")
    private String strIngredient15;
    @SerializedName("strIngredient16")
    private String strIngredient16;
    @SerializedName("strIngredient17")
    private String strIngredient17;
    @SerializedName("strIngredient18")
    private String strIngredient18;
    @SerializedName("strIngredient19")
    private String strIngredient19;
    @SerializedName("strIngredient20")
    private String strIngredient20;

    // Measure fields (raw JSON fields)
    @SerializedName("strMeasure1")
    private String strMeasure1;
    @SerializedName("strMeasure2")
    private String strMeasure2;
    @SerializedName("strMeasure3")
    private String strMeasure3;
    @SerializedName("strMeasure4")
    private String strMeasure4;
    @SerializedName("strMeasure5")
    private String strMeasure5;
    @SerializedName("strMeasure6")
    private String strMeasure6;
    @SerializedName("strMeasure7")
    private String strMeasure7;
    @SerializedName("strMeasure8")
    private String strMeasure8;
    @SerializedName("strMeasure9")
    private String strMeasure9;
    @SerializedName("strMeasure10")
    private String strMeasure10;
    @SerializedName("strMeasure11")
    private String strMeasure11;
    @SerializedName("strMeasure12")
    private String strMeasure12;
    @SerializedName("strMeasure13")
    private String strMeasure13;
    @SerializedName("strMeasure14")
    private String strMeasure14;
    @SerializedName("strMeasure15")
    private String strMeasure15;
    @SerializedName("strMeasure16")
    private String strMeasure16;
    @SerializedName("strMeasure17")
    private String strMeasure17;
    @SerializedName("strMeasure18")
    private String strMeasure18;
    @SerializedName("strMeasure19")
    private String strMeasure19;
    @SerializedName("strMeasure20")
    private String strMeasure20;

    // Processed ingredients list
    private List<IngredientModel> ingredients;

    public MealModel() {
    }

    protected MealModel(Parcel in) {
        idMeal = in.readString();
        strMeal = in.readString();
        strMealAlternate = in.readString();
        strCategory = in.readString();
        strArea = in.readString();
        strInstructions = in.readString();
        strMealThumb = in.readString();
        strTags = in.readString();
        strYoutube = in.readString();
        strSource = in.readString();
        strImageSource = in.readString();
        strCreativeCommonsConfirmed = in.readString();
        dateModified = in.readString();

        strIngredient1 = in.readString();
        strIngredient2 = in.readString();
        strIngredient3 = in.readString();
        strIngredient4 = in.readString();
        strIngredient5 = in.readString();
        strIngredient6 = in.readString();
        strIngredient7 = in.readString();
        strIngredient8 = in.readString();
        strIngredient9 = in.readString();
        strIngredient10 = in.readString();
        strIngredient11 = in.readString();
        strIngredient12 = in.readString();
        strIngredient13 = in.readString();
        strIngredient14 = in.readString();
        strIngredient15 = in.readString();
        strIngredient16 = in.readString();
        strIngredient17 = in.readString();
        strIngredient18 = in.readString();
        strIngredient19 = in.readString();
        strIngredient20 = in.readString();

        strMeasure1 = in.readString();
        strMeasure2 = in.readString();
        strMeasure3 = in.readString();
        strMeasure4 = in.readString();
        strMeasure5 = in.readString();
        strMeasure6 = in.readString();
        strMeasure7 = in.readString();
        strMeasure8 = in.readString();
        strMeasure9 = in.readString();
        strMeasure10 = in.readString();
        strMeasure11 = in.readString();
        strMeasure12 = in.readString();
        strMeasure13 = in.readString();
        strMeasure14 = in.readString();
        strMeasure15 = in.readString();
        strMeasure16 = in.readString();
        strMeasure17 = in.readString();
        strMeasure18 = in.readString();
        strMeasure19 = in.readString();
        strMeasure20 = in.readString();

        ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
    }

    public static final Creator<MealModel> CREATOR = new Creator<MealModel>() {
        @Override
        public MealModel createFromParcel(Parcel in) {
            return new MealModel(in);
        }

        @Override
        public MealModel[] newArray(int size) {
            return new MealModel[size];
        }
    };

    /**
     * Builds and returns a list of valid ingredients with their measures
     */
    public List<IngredientModel> getIngredients() {
        if (ingredients == null) {
            ingredients = buildIngredientsList();
        }
        return ingredients;
    }

    /**
     * Internal method to combine ingredient and measure fields into Ingredient objects
     */
    private List<IngredientModel> buildIngredientsList() {
        List<IngredientModel> list = new ArrayList<>();

        addIngredient(list, strIngredient1, strMeasure1);
        addIngredient(list, strIngredient2, strMeasure2);
        addIngredient(list, strIngredient3, strMeasure3);
        addIngredient(list, strIngredient4, strMeasure4);
        addIngredient(list, strIngredient5, strMeasure5);
        addIngredient(list, strIngredient6, strMeasure6);
        addIngredient(list, strIngredient7, strMeasure7);
        addIngredient(list, strIngredient8, strMeasure8);
        addIngredient(list, strIngredient9, strMeasure9);
        addIngredient(list, strIngredient10, strMeasure10);
        addIngredient(list, strIngredient11, strMeasure11);
        addIngredient(list, strIngredient12, strMeasure12);
        addIngredient(list, strIngredient13, strMeasure13);
        addIngredient(list, strIngredient14, strMeasure14);
        addIngredient(list, strIngredient15, strMeasure15);
        addIngredient(list, strIngredient16, strMeasure16);
        addIngredient(list, strIngredient17, strMeasure17);
        addIngredient(list, strIngredient18, strMeasure18);
        addIngredient(list, strIngredient19, strMeasure19);
        addIngredient(list, strIngredient20, strMeasure20);

        return list;
    }

    /**
     * Helper method to add ingredient if valid
     */
    private void addIngredient(List<IngredientModel> list, String ingredient, String measure) {
        if (!TextUtils.isEmpty(ingredient) && !ingredient.trim().isEmpty()) {
            list.add(new IngredientModel(ingredient.trim(),
                    measure != null ? measure.trim() : ""));
        }
    }

    // Getters and Setters
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrMealAlternate() {
        return strMealAlternate;
    }

    public void setStrMealAlternate(String strMealAlternate) {
        this.strMealAlternate = strMealAlternate;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public String getStrSource() {
        return strSource;
    }

    public void setStrSource(String strSource) {
        this.strSource = strSource;
    }

    public String getStrImageSource() {
        return strImageSource;
    }

    public void setStrImageSource(String strImageSource) {
        this.strImageSource = strImageSource;
    }

    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public void setStrCreativeCommonsConfirmed(String strCreativeCommonsConfirmed) {
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMeal);
        dest.writeString(strMeal);
        dest.writeString(strMealAlternate);
        dest.writeString(strCategory);
        dest.writeString(strArea);
        dest.writeString(strInstructions);
        dest.writeString(strMealThumb);
        dest.writeString(strTags);
        dest.writeString(strYoutube);
        dest.writeString(strSource);
        dest.writeString(strImageSource);
        dest.writeString(strCreativeCommonsConfirmed);
        dest.writeString(dateModified);

        dest.writeString(strIngredient1);
        dest.writeString(strIngredient2);
        dest.writeString(strIngredient3);
        dest.writeString(strIngredient4);
        dest.writeString(strIngredient5);
        dest.writeString(strIngredient6);
        dest.writeString(strIngredient7);
        dest.writeString(strIngredient8);
        dest.writeString(strIngredient9);
        dest.writeString(strIngredient10);
        dest.writeString(strIngredient11);
        dest.writeString(strIngredient12);
        dest.writeString(strIngredient13);
        dest.writeString(strIngredient14);
        dest.writeString(strIngredient15);
        dest.writeString(strIngredient16);
        dest.writeString(strIngredient17);
        dest.writeString(strIngredient18);
        dest.writeString(strIngredient19);
        dest.writeString(strIngredient20);

        dest.writeString(strMeasure1);
        dest.writeString(strMeasure2);
        dest.writeString(strMeasure3);
        dest.writeString(strMeasure4);
        dest.writeString(strMeasure5);
        dest.writeString(strMeasure6);
        dest.writeString(strMeasure7);
        dest.writeString(strMeasure8);
        dest.writeString(strMeasure9);
        dest.writeString(strMeasure10);
        dest.writeString(strMeasure11);
        dest.writeString(strMeasure12);
        dest.writeString(strMeasure13);
        dest.writeString(strMeasure14);
        dest.writeString(strMeasure15);
        dest.writeString(strMeasure16);
        dest.writeString(strMeasure17);
        dest.writeString(strMeasure18);
        dest.writeString(strMeasure19);
        dest.writeString(strMeasure20);

        dest.writeTypedList(getIngredients());
    }
}