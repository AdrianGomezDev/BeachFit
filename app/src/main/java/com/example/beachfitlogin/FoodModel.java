package com.example.beachfitlogin;

import android.net.Uri;

import java.io.Serializable;

public class FoodModel implements Serializable {
    private String foodName;
    private Uri photoThumb;

    //Food Nutrients Attributes
    private Double servingQuantity;
    private String servingUnit;
    private Double servingWeight;
    private Double calories;
    private Double totalFat;
    private Double saturatedFat;
    private Double cholesterol;
    private Double sodium;
    private Double totalCarbohydrates;
    private Double dietaryFiber;
    private Double sugars;
    private Double protein;

    public FoodModel() {}

    public FoodModel(String foodName, Uri photoThumb) {
        this.foodName = foodName;
        this.photoThumb = photoThumb;
        this.servingQuantity = null;
        this.servingUnit = null;
        this.servingWeight = null;
        this.calories = null;
        this.totalFat = null;
        this.saturatedFat = null;
        this.cholesterol = null;
        this.sodium = null;
        this.totalCarbohydrates = null;
        this.dietaryFiber = null;
        this.sugars = null;
        this.protein = null;
    }

    public FoodModel(String foodName, Uri photoThumb, Double servingQuantity, String servingUnit,
                     Double servingWeight, Double calories, Double totalFat, Double saturatedFat,
                     Double cholesterol, Double sodium, Double totalCarbohydrates, Double dietaryFiber,
                     Double sugars, Double protein) {
        this.foodName = foodName;
        this.photoThumb = photoThumb;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.servingWeight = servingWeight;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.totalCarbohydrates = totalCarbohydrates;
        this.dietaryFiber = dietaryFiber;
        this.sugars = sugars;
        this.protein = protein;
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

    public Double getServingQuantity() { return servingQuantity; }

    public void setServingQuantity(Double servingQuantity) { this.servingQuantity = servingQuantity; }

    public String getServingUnit() { return servingUnit; }

    public void setServingUnit(String servingUnit) { this.servingUnit = servingUnit; }

    public Double getServingWeight() { return servingWeight; }

    public void setServingWeight(Double servingWeight) { this.servingWeight = servingWeight; }

    public Double getCalories() { return calories; }

    public void setCalories(Double calories) { this.calories = calories; }

    public Double getTotalFat() { return totalFat; }

    public void setTotalFat(Double totalFat) { this.totalFat = totalFat; }

    public Double getSaturatedFat() { return saturatedFat; }

    public void setSaturatedFat(Double saturatedFat) { this.saturatedFat = saturatedFat; }

    public Double getCholesterol() { return cholesterol; }

    public void setCholesterol(Double cholesterol) { this.cholesterol = cholesterol; }

    public Double getSodium() { return sodium; }

    public void setSodium(Double sodium) { this.sodium = sodium; }

    public Double getTotalCarbohydrates() { return totalCarbohydrates; }

    public void setTotalCarbohydrates(Double totalCarbohydrates) { this.totalCarbohydrates = totalCarbohydrates; }

    public Double getDietaryFiber() { return dietaryFiber; }

    public void setDietaryFiber(Double dietaryFiber) { this.dietaryFiber = dietaryFiber; }

    public Double getSugars() { return sugars; }

    public void setSugars(Double sugars) { this.sugars = sugars; }

    public Double getProtein() { return protein; }

    public void setProtein(Double protein) { this.protein = protein; }

    @Override
    public String toString() {
        return  "Serving Quantity: " + servingQuantity    +  "\n" +
                "    Serving Unit: " + servingUnit        +  "\n" +
                "  Serving Weight: " + servingWeight      + "g\n" +
                "        Calories: " + calories           + "g\n" +
                "       Total Fat: " + totalFat           + "g\n" +
                "   Saturated Fat: " + saturatedFat       + "g\n" +
                "     Cholesterol: " + cholesterol        + "mg\n" +
                "          Sodium: " + sodium             + "mg\n" +
                "     Total Carbs: " + totalCarbohydrates + "g\n" +
                "   Dietary Fiber: " + dietaryFiber       + "g\n" +
                "          Sugars: " + sugars             + "g\n" +
                "         Protein: " + protein            + "g\n";
    }
}