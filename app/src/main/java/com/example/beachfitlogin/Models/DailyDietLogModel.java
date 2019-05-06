package com.example.beachfitlogin.Models;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DailyDietLogModel {

    private Date date;
    private Map<String, Map<String, Object>> foodLog;

    public DailyDietLogModel() { }

    public DailyDietLogModel(Date date, Map<String, Map<String, Object>> foodLog) {
        this.date = date;
        this.foodLog = foodLog;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<FoodModel> getFoodLog() {
        ArrayList<FoodModel> myFoodLog = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> parentPair : foodLog.entrySet()) {

            FoodModel foodModel = new FoodModel(
                    Objects.requireNonNull(parentPair.getValue().get("foodName")).toString(),
                    Uri.parse(Objects.requireNonNull(parentPair.getValue().get("photoThumb")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("servingQuantity")).toString()),
                    Objects.requireNonNull(parentPair.getValue().get("servingUnit")).toString(),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("servingWeight")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("calories")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("totalFat")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("saturatedFat")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("cholesterol")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("sodium")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("totalCarbs")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("dietaryFiber")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("sugars")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("protein")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("servingsConsumed")).toString()),
                    Double.parseDouble(Objects.requireNonNull(parentPair.getValue().get("totalCalsConsumed")).toString())
            );
            myFoodLog.add(foodModel);
        }

        return myFoodLog;
    }

    public void setFoodLog(Map<String, Map<String, Object>> foodLog) {
        this.foodLog = foodLog;
    }

}
