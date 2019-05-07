package com.example.beachfitlogin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beachfitlogin.Adapters.DailyFitnessLogAdapter.OnDailyFitnessLogClickListener;
import com.example.beachfitlogin.Models.LoggedExerciseModel;
import com.example.beachfitlogin.R;

import java.util.ArrayList;
import java.util.List;

public class DailyFitnessLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView date;
    private ListView fitnessLogListView;
    private OnDailyFitnessLogClickListener onDailyFitnessLogClickListener;

    public DailyFitnessLogViewHolder(@NonNull View itemView, OnDailyFitnessLogClickListener onDailyFitnessLogClickListener) {
        super(itemView);

        this.date = itemView.findViewById(R.id.dailyFitnessLogDateLabel);
        this.fitnessLogListView = itemView.findViewById(R.id.exerciseLogListView);
        this.onDailyFitnessLogClickListener = onDailyFitnessLogClickListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onDailyFitnessLogClickListener.onDailyFitnessLogClick(getAdapterPosition());
    }

    public List<LoggedExerciseModel> setExerciseLogListViewAdapter(ArrayList<LoggedExerciseModel> exerciseLog){
        List<LoggedExerciseModel> loggedExerciseList = new ArrayList<>();
        for(LoggedExerciseModel model : exerciseLog){
            loggedExerciseList.add(
                    new LoggedExerciseModel(
                            model.getName(),
                            model.getEquipment(),
                            model.getLevel(),
                            model.getMuscle(),
                            model.getType(),
                            model.getExerciseStats()
                    )
            );
        }
        return loggedExerciseList;
    }

    public TextView getDate() {
        return date;
    }

    public ListView getFitnessLogListView() {
        return fitnessLogListView;
    }
}