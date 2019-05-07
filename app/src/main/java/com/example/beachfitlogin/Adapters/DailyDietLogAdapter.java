package com.example.beachfitlogin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beachfitlogin.Models.DailyDietLogModel;
import com.example.beachfitlogin.Models.FoodModel;
import com.example.beachfitlogin.Models.LoggedFoodModel;
import com.example.beachfitlogin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.beachfitlogin.Util.roundDouble;

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
        ListView foodLog = holder.foodLogListView;
        TextView totalCalsForDay = holder.totalCalsForDay;
        Context context = holder.date.getContext();
        List<LoggedFoodModel> loggedFoodList= holder.setFoodLogListViewAdapter(dailyDietLogModel.getFoodLog());

        date.setText(dailyDietLogModel.getDate());
        LoggedFoodAdapter adapter = new LoggedFoodAdapter(context, loggedFoodList);
        Double totalCals = 0.0;
        for(LoggedFoodModel model: loggedFoodList){
            totalCals += Double.parseDouble(model.getTotalCalories());
        }
        totalCalsForDay.setText(roundDouble(totalCals));
        foodLog.setAdapter(adapter);
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
        ListView foodLogListView;
        TextView totalCalsForDay;
        OnDailyDietLogClickListener onDailyDietLogClickListener;

        ViewHolder(View itemView, OnDailyDietLogClickListener onDailyDietLogClickListener) {
            super(itemView);

            this.date = itemView.findViewById(R.id.dailyDietLogDateLabel);
            this.foodLogListView = itemView.findViewById(R.id.foodLogListView);
            this.totalCalsForDay = itemView.findViewById(R.id.totalCalsForDayEntry);
            this.onDailyDietLogClickListener = onDailyDietLogClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDailyDietLogClickListener.onDailyDietLogClick(getAdapterPosition());
        }

        private List<LoggedFoodModel> setFoodLogListViewAdapter(ArrayList<FoodModel> foodLog){
            List<LoggedFoodModel> loggedFoodList = new ArrayList<>();
            for(FoodModel foodModel : foodLog){
                loggedFoodList.add(
                        new LoggedFoodModel(
                                foodModel.getFoodName(),
                                foodModel.getCalories().toString(),
                                foodModel.getServingsConsumed().toString(),
                                roundDouble(foodModel.getTotalCalsConsumed())
                        )
                );
            }
            return loggedFoodList;
        }
    }

    public interface OnDailyDietLogClickListener{
        void onDailyDietLogClick(int position);
    }
}
