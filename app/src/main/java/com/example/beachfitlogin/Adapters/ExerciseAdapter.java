package com.example.beachfitlogin.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beachfitlogin.Interfaces.OnFragmentInteractionListener;
import com.example.beachfitlogin.Models.ExerciseModel;
import com.example.beachfitlogin.R;
import com.example.beachfitlogin.ViewHolders.ExerciseViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ExerciseAdapter extends FirestoreRecyclerAdapter<ExerciseModel, ExerciseViewHolder> {

    private OnFragmentInteractionListener mListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ExerciseAdapter(@NonNull FirestoreRecyclerOptions<ExerciseModel> options, OnFragmentInteractionListener mListener) {
        super(options);
        this.mListener = mListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull final ExerciseModel model) {
        holder.setExerciseName(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFragmentMessage("Fitness", model.getName());
                }
            }
        });
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_exercise, viewGroup, false);

        return new ExerciseViewHolder(view);
    }
}
