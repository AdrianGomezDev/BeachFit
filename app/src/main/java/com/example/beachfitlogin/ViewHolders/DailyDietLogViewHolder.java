package com.example.beachfitlogin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.beachfitlogin.Models.FoodModel;
import com.example.beachfitlogin.R;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DailyDietLogViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public DailyDietLogViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setDate(String date){
        TextView textView =  view.findViewById(R.id.dailyDietLogDateLabel);
        textView.setText(date);
    }

    public void setFoodLog(ArrayList<FoodModel> foodLog){
        StringBuilder formattedString = new StringBuilder();
        Double calorieSum = 0.0;

        for(FoodModel foodModel : foodLog){
            formattedString.append(foodModel.getFoodName()).append(" ");
            formattedString.append("Servings: ").append(foodModel.getServingsConsumed()).append(" + ");
            formattedString.append("Calories per serving: ").append(foodModel.getCalories()).append(" = ");
            formattedString.append(foodModel.getTotalCalsConsumed()).append("\n");
            calorieSum += foodModel.getTotalCalsConsumed();
        }
        formattedString.append("Total Calories Consumed: ").append(calorieSum);

        TextView textView = view.findViewById(R.id.foodLogLabel);
        textView.setText(formattedString.toString());
    }
}
