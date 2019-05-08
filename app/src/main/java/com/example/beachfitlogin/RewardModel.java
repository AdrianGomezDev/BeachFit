package com.example.beachfitlogin;

import android.net.Uri;

import java.io.Serializable;

public class RewardModel implements Serializable {

    private String rewardName;

    public RewardModel() {}

    public RewardModel(String rewardName, Uri photoThumb) {
        this.rewardName = rewardName;

    }

    public RewardModel(String rewardName) {
        this.rewardName = rewardName;

    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    @Override
    public String toString() {
        /*return  "Serving Quantity: " + servingQuantity    +  "\n" +
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
                "         Protein: " + protein            + "g\n";*/
        return "Reward Name: " + rewardName + "\n";
    }
}