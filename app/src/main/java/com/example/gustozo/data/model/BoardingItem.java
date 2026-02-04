package com.example.gustozo.data.model;

public class BoardingItem {
    private int titleRes;
    private int descRes;
    private int imageRes;
    private int backgroundColorRes;

    public BoardingItem(int titleRes, int descRes, int imageRes, int backgroundColorRes) {
        this.titleRes = titleRes;
        this.descRes = descRes;
        this.imageRes = imageRes;
        this.backgroundColorRes = backgroundColorRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public int getDescRes() {
        return descRes;
    }

    public void setDescRes(int descRes) {
        this.descRes = descRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getBackgroundColorRes() {
        return backgroundColorRes;
    }

    public void setBackgroundColorRes(int backgroundColorRes) {
        this.backgroundColorRes = backgroundColorRes;
    }
}
