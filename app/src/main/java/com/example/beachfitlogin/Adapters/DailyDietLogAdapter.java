package com.example.beachfitlogin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beachfitlogin.Models.DailyDietLogModel;
import com.example.beachfitlogin.Models.FoodModel;
import com.example.beachfitlogin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class DailyDietLogAdapter extends FirestoreRecyclerAdapter<DailyDietLogModel, DailyDietLogAdapter.ViewHolder> {

    private OnDailyDietLogClickListener onDailyDietLogClickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options contains the query needed to fetch the information for binding
     */
    public DailyDietLogAdapter(@NonNull FirestoreRecyclerOptions<DailyDietLogModel> options, OnDailyDietLogClickListener onDailyDietLogClickListener) {
        super(options);
        this.onDailyDietLogClickListener = onDailyDietLogClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyDietLogAdapter.ViewHolder holder, int position, @NonNull DailyDietLogModel dailyDietLogModel) {
        TextView date = holder.date;
        TextView foodLog = holder.foodLog;

        date.setText(dailyDietLogModel.getDate());
        foodLog.setText(holder.setFoodLog(dailyDietLogModel.getFoodLog()));

    }

    @NonNull
    @Override
    public DailyDietLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_diet_log, group, false);

        return new ViewHolder(view, onDailyDietLogClickListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView date;
        TextView foodLog;
        OnDailyDietLogClickListener onDailyDietLogClickListener;

        ViewHolder(View itemView, OnDailyDietLogClickListener onDailyDietLogClickListener) {
            super(itemView);

            this.date = itemView.findViewById(R.id.dailyDietLogDateLabel);
            this.foodLog = itemView.findViewById(R.id.foodLogLabel);
            this.onDailyDietLogClickListener = onDailyDietLogClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDailyDietLogClickListener.onDailyDietLogClick(getAdapterPosition());
        }

        private String setFoodLog(ArrayList<FoodModel> foodLog){
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

            return formattedString.toString();
        }
    }

    public interface OnDailyDietLogClickListener{
        void onDailyDietLogClick(int position);
    }
}
