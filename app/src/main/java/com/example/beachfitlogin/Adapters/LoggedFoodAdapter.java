package com.example.beachfitlogin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beachfitlogin.Models.LoggedFoodModel;
import com.example.beachfitlogin.R;

import java.util.List;

public class LoggedFoodAdapter extends ArrayAdapter<LoggedFoodModel>{

    private Context context;
    private List<LoggedFoodModel> foodList;

    public LoggedFoodAdapter(Context context, List<LoggedFoodModel> foodList) {
        super(context, 0, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_logged_food,parent,false);

        LoggedFoodModel loggedFoodModel = foodList.get(position);

        TextView foodName = listItem.findViewById(R.id.foodColumnEntry);
        TextView calsServing = listItem.findViewById(R.id.calsServingColumnEntry);
        TextView servingsEaten = listItem.findViewById(R.id.servingsEatenColumnEntry);
        TextView totalCalories = listItem.findViewById(R.id.totalCalsColumnEntry);

        foodName.setText(loggedFoodModel.getFoodName());
        calsServing.setText(loggedFoodModel.getCalsServing());
        servingsEaten.setText(loggedFoodModel.getServingsEaten());
        totalCalories.setText(loggedFoodModel.getTotalCalories());

        return listItem;
    }
}
