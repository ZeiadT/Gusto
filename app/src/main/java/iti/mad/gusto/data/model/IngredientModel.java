package iti.mad.gusto.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientModel implements Parcelable {

    private String name;
    private String measure;

    public IngredientModel(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    protected IngredientModel(Parcel in) {
        name = in.readString();
        measure = in.readString();
    }

    public static final Creator<IngredientModel> CREATOR = new Creator<>() {
        @Override
        public IngredientModel createFromParcel(Parcel in) {
            return new IngredientModel(in);
        }

        @Override
        public IngredientModel[] newArray(int size) {
            return new IngredientModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(measure);
    }

    @Override
    public String toString() {
        return measure + " " + name;
    }
}