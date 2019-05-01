package com.example.beachfitlogin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.beachfitlogin.R;

class FoodViewHolder extends RecyclerView.ViewHolder {
    private View view;

    FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    void setFoodName(String foodName){
        TextView textView =  view.findViewById(R.id.foodNameLabel);
        textView.setText(foodName);
    }

    void setServingsConsumed(String servingsConsumed){
        TextView textView = view.findViewById(R.id.servingsConsumedLabel);
        textView.setText(servingsConsumed);
    }

    void setTotalCalsConsumed(String totalCalsConsumed){
        TextView textView = view.findViewById(R.id.totalCaloriesLabel);
        textView.setText(totalCalsConsumed);
    }
}
