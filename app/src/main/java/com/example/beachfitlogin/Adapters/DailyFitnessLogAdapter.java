package com.example.beachfitlogin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beachfitlogin.Models.DailyFitnessLogModel;
import com.example.beachfitlogin.Models.LoggedExerciseModel;
import com.example.beachfitlogin.R;
import com.example.beachfitlogin.ViewHolders.DailyFitnessLogViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class DailyFitnessLogAdapter extends FirestoreRecyclerAdapter<DailyFitnessLogModel, DailyFitnessLogViewHolder>
{
    private OnDailyFitnessLogClickListener onDailyFitnessLogClickListener;

    public DailyFitnessLogAdapter(@NonNull FirestoreRecyclerOptions<DailyFitnessLogModel> options, OnDailyFitnessLogClickListener onDailyFitnessLogClickListener) {
        super(options);
        this.onDailyFitnessLogClickListener = onDailyFitnessLogClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull DailyFitnessLogViewHolder holder, int position, @NonNull DailyFitnessLogModel model) {
        Context context = holder.getDate().getContext();
        ListView exerciseLog = holder.getFitnessLogListView();
        holder.getDate().setText(model.getDate());
        List<LoggedExerciseModel> loggedExerciseList= holder.setExerciseLogListViewAdapter(model.getExerciseLog());
        LoggedExerciseAdapter adapter = new LoggedExerciseAdapter(context,loggedExerciseList);
        exerciseLog.setAdapter(adapter);
    }

    @NonNull
    @Override
    public DailyFitnessLogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_fitness_log, viewGroup, false);

        return new DailyFitnessLogViewHolder(view, onDailyFitnessLogClickListener);
    }

    public interface OnDailyFitnessLogClickListener{
        void onDailyFitnessLogClick(int position);
    }
}
