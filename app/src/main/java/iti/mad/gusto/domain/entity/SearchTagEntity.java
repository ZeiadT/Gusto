package iti.mad.gusto.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SearchTagEntity implements Parcelable {
    private String tagName;
    private TagType tagType;


    public SearchTagEntity() {
    }

    public SearchTagEntity(String tagName, TagType tagType) {
        this.tagName = tagName;
        this.tagType = tagType;
    }

    protected SearchTagEntity(Parcel in) {
        tagName = in.readString();
        int tagTypeOrdinal = in.readInt();
        if (tagTypeOrdinal == -1) {
            tagType = null;
        } else {
            tagType = TagType.values()[tagTypeOrdinal];
        }
    }
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
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

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.tagName);
        dest.writeInt(this.tagType == null ? -1 : this.tagType.ordinal());
    }
}
