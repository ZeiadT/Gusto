package iti.mad.gusto.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MealResponse implements Parcelable {

    @SerializedName("meals")
    private List<MealModel> meals;

    public MealResponse() {
        meals = new ArrayList<>();
    }

    protected MealResponse(Parcel in) {
        meals = in.createTypedArrayList(MealModel.CREATOR);
    }

    public static final Creator<MealResponse> CREATOR = new Creator<MealResponse>() {
        @Override
        public MealResponse createFromParcel(Parcel in) {
            return new MealResponse(in);
        }

        @Override
        public MealResponse[] newArray(int size) {
            return new MealResponse[size];
        }
    };

    public List<MealModel> getMeals() {
        return meals;
    }

    public void setMeals(List<MealModel> meals) {
        this.meals = meals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(meals);
    }
}