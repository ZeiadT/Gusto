package iti.mad.gusto.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class MealEntity implements Parcelable {

    private String id;
    private String name;
    private String image;
    private String category;
    private String area;
    private String youtube;
    private List<IngredientEntity> ingredients;
    private List<InstructionEntity> instructions;

    public MealEntity() {
    }

    protected MealEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        category = in.readString();
        area = in.readString();
        youtube = in.readString();

        ingredients = in.createTypedArrayList(IngredientEntity.CREATOR);
        instructions = in.createTypedArrayList(InstructionEntity.CREATOR);
    }

    public static final Creator<MealEntity> CREATOR = new Creator<>() {
        @Override
        public MealEntity createFromParcel(Parcel in) {
            return new MealEntity(in);
        }

        @Override
        public MealEntity[] newArray(int size) {
            return new MealEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.category);
        dest.writeString(this.area);
        dest.writeString(this.youtube);

        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.instructions);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<InstructionEntity> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<InstructionEntity> instructions) {
        this.instructions = instructions;
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