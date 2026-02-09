package iti.mad.gusto.domain.entity;

public class SearchTagEntity {
    private String tagName;
    private TagType tagType;


    public SearchTagEntity() {
    }

    public SearchTagEntity(String tagName, TagType tagType) {
        this.tagName = tagName;
        this.tagType = tagType;
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
}
