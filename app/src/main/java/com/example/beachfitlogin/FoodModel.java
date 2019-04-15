package com.example.beachfitlogin;

import android.net.Uri;

public class FoodModel {
    private String foodName;
    private Uri photoThumb;

    public FoodModel() {}

    public FoodModel(String foodName, Uri photoThumb) {
        this.foodName = foodName;
        this.photoThumb = photoThumb;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Uri getPhotoThumb() {
        return photoThumb;
    }

    public void setPhotoThumb(Uri photoThumb) {
        this.photoThumb = photoThumb;
    }
}