package iti.mad.gusto.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class InstructionEntity implements Parcelable {
    private final String step;

    public InstructionEntity(String step) {
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "step='" + step + '\'' +
                '}' + "\n";
    }


    public static final Creator<InstructionEntity> CREATOR = new Creator<>() {
        @Override
        public InstructionEntity createFromParcel(Parcel in) {
            return new InstructionEntity(in);
        }

        @Override
        public InstructionEntity[] newArray(int size) {
            return new InstructionEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    InstructionEntity(Parcel in) {
        step = in.readString();
    }


    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.step);
    }
}