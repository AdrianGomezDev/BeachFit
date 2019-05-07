package com.example.beachfitlogin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beachfitlogin.Models.LoggedExerciseModel;
import com.example.beachfitlogin.R;

import java.util.List;
import java.util.Map;

public class LoggedExerciseAdapter extends ArrayAdapter<LoggedExerciseModel> {
    private Context context;
    private List<LoggedExerciseModel> exerciseList;

    public LoggedExerciseAdapter(Context context, List<LoggedExerciseModel> exerciseList) {
        super(context, 0, exerciseList);
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_logged_exercise, parent,false);

        LoggedExerciseModel loggedExerciseModel = exerciseList.get(position);

        TextView exerciseName = listItem.findViewById(R.id.exerciseColumnEntry);
        exerciseName.setText(loggedExerciseModel.getName());

        TextView exerciseStats = listItem.findViewById(R.id.exerciseStatsColumnEntry);
        StringBuilder statsString = new StringBuilder();
        for (Map.Entry<String, Double> entry : loggedExerciseModel.getExerciseStats().entrySet()) {
            statsString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        exerciseStats.setText(statsString.toString().trim());

        return listItem;
    }
}