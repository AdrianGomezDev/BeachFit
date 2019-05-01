package com.example.beachfitlogin.Models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DailyDietLogModel {

    private String date;
    private Map<String, Map<String, Object>> foodLog;

    public DailyDietLogModel() { }

    public DailyDietLogModel(String date, Map<String, Map<String, Object>> foodLog) {
        this.date = date;
        this.foodLog = foodLog;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<FoodModel> getFoodLog() {
        ArrayList<FoodModel> myFoodLog = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> parentPair : foodLog.entrySet()) {
            FoodModel foodModel = new FoodModel(
                    parentPair.getValue().get("foodName").toString(),
                    Uri.parse(Objects.requireNonNull(parentPair.getValue().get("photoThumb")).toString()),
                    Double.parseDouble(parentPair.getValue().get("servingQuantity").toString()),
                    parentPair.getValue().get("servingUnit").toString(),
                    Double.parseDouble(parentPair.getValue().get("servingWeight").toString()),
                    Double.parseDouble(parentPair.getValue().get("calories").toString()),
                    Double.parseDouble(parentPair.getValue().get("totalFat").toString()),
                    Double.parseDouble(parentPair.getValue().get("saturatedFat").toString()),
                    Double.parseDouble(parentPair.getValue().get("cholesterol").toString()),
                    Double.parseDouble(parentPair.getValue().get("sodium").toString()),
                    Double.parseDouble(parentPair.getValue().get("totalCarbs").toString()),
                    Double.parseDouble(parentPair.getValue().get("dietaryFiber").toString()),
                    Double.parseDouble(parentPair.getValue().get("sugars").toString()),
                    Double.parseDouble(parentPair.getValue().get("protein").toString()),
                    Double.parseDouble(parentPair.getValue().get("servingsConsumed").toString()),
                    Double.parseDouble(parentPair.getValue().get("totalCalsConsumed").toString())
            );
            myFoodLog.add(foodModel);
        }

        return myFoodLog;
    }

    public void setFoodLog(Map<String, Map<String, Object>> foodLog) {
        this.foodLog = foodLog;
    }

}
