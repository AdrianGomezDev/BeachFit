package com.example.beachfitlogin.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.beachfitlogin.R;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public ExerciseViewHolder(@NonNull final View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setExerciseName(String ExerciseName) {
        TextView textView = view.findViewById(R.id.exercise_index_text_view);
        textView.setText(ExerciseName);
    }
}