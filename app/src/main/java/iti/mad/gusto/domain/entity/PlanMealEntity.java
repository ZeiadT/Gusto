package iti.mad.gusto.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "plans", primaryKeys = {"type", "date"})
public class PlanMealEntity implements Parcelable {

    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "area")

    private String area;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;
    private String youtube;
    @NonNull
    @ColumnInfo(name = "type")
    private MealType type;
    @Ignore
    private List<IngredientEntity> ingredients;
    @Ignore
    private List<InstructionEntity> instructions;

    public PlanMealEntity() {
    }

    public PlanMealEntity(@NonNull String id, String name, String image, String category, String area, String date, String youtube, MealType type, List<IngredientEntity> ingredients, List<InstructionEntity> instructions) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.area = area;
        this.date = date;
        this.youtube = youtube;
        this.type = type;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public PlanMealEntity(MealEntity meal, String date, MealType type){
        this.id = meal.getId();
        this.name = meal.getName();
        this.image = meal.getImage();
        this.category = meal.getCategory();
        this.area = meal.getArea();
        this.date = date;
        this.youtube = meal.getYoutube();
        this.type = type;
        this.ingredients = meal.getIngredients();
        this.instructions = meal.getInstructions();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
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
        return "MealEntity{" + "name='" + name + '\'' + ", category='" + category + '\'' + ", area='" + area + '\'' + ", image='" + image + '\'' + ", youtube='" + youtube + '\'' + ", ingredients=" + ingredients + ", instructions=" + instructions + '}';
    }


    public static final Creator<SearchTagEntity> CREATOR = new Creator<>() {
        @Override
        public SearchTagEntity createFromParcel(Parcel in) {
            return new SearchTagEntity(in);
        }

        @Override
        public SearchTagEntity[] newArray(int size) {
            return new SearchTagEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    PlanMealEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.area = in.readString();
        this.image = in.readString();
        this.youtube = in.readString();
        this.date = in.readString();
        this.type = MealType.values()[in.readInt()];
        in.readList(this.ingredients, IngredientEntity.class.getClassLoader());
        in.readList(this.instructions, InstructionEntity.class.getClassLoader());

    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.area);
        dest.writeString(this.image);
        dest.writeString(this.youtube);
        dest.writeString(this.date);
        dest.writeInt(this.type.ordinal());
        dest.writeList(this.ingredients);
        dest.writeList(this.instructions);
    }
}