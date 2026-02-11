package iti.mad.gusto.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class IngredientEntity implements Parcelable{
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

    @NonNull
    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", measure='" + measure + '\'' +
                ", image='" + image + '\'' +
                '}' + "\n";
    }



    public static final Parcelable.Creator<IngredientEntity> CREATOR = new Parcelable.Creator<>() {
        @Override
        public IngredientEntity createFromParcel(Parcel in) {
            return new IngredientEntity(in);
        }

        @Override
        public IngredientEntity[] newArray(int size) {
            return new IngredientEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    IngredientEntity(Parcel in){
        name = in.readString();
        measure = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.measure);
        dest.writeString(this.image);
    }
}