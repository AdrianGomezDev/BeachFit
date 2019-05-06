package com.example.beachfitlogin.Models;

public class LoggedFoodModel {
    private String foodName;
    private String calsServing;
    private String servingsEaten;
    private String totalCalories;

    public LoggedFoodModel(String foodName, String calsServing, String servingsEaten, String totalCalories) {
        this.foodName = foodName;
        this.calsServing = calsServing;
        this.servingsEaten = servingsEaten;
        this.totalCalories = totalCalories;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalsServing() {
        return calsServing;
    }

    public void setCalsServing(String calsServing) {
        this.calsServing = calsServing;
    }

    public String getServingsEaten() {
        return servingsEaten;
    }

    public void setServingsEaten(String servingsEaten) {
        this.servingsEaten = servingsEaten;
    }

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }
}
