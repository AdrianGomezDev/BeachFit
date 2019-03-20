package com.example.beachfitlogin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class ExerciseIndexHolder extends RecyclerView.ViewHolder {
    private View view;

    ExerciseIndexHolder(@NonNull final View itemView) {
        super(itemView);
        view = itemView;
    }

    void setExerciseName(String ExerciseName) {
        TextView textView = view.findViewById(R.id.exercise_index_text_view);
        textView.setText(ExerciseName);
    }
}