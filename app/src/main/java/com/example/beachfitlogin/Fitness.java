package com.example.beachfitlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class Fitness extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private FirestoreRecyclerAdapter<ExerciseModel, ExerciseIndexHolder> adapter;

    public Fitness() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fitness.
     */
    // TODO: Rename and change types and number of parameters
    public static Fitness newInstance(String param1, String param2) {
        Fitness fragment = new Fitness();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Fitness");
        View layout = inflater.inflate(R.layout.fragment_fitness, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        // Set up RecyclerView
        RecyclerView recyclerView = layout.findViewById(R.id.fitnessRecyclerView);
        // Add dividers to recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        //Fetch Exercises from FireStore
        Query query = FirebaseFirestore.getInstance().collection("Exercises")
                .orderBy("Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ExerciseModel> options = new FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ExerciseModel, ExerciseIndexHolder>(options) {

            @Override
            public void onBindViewHolder(@NonNull ExerciseIndexHolder holder, int position, @NonNull ExerciseModel exerciseModel) {
                final String exerciseName = exerciseModel.getName();
                holder.setExerciseName(exerciseName);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onButtonPressed(exerciseName);
                    }
                });
            }

            @NonNull
            @Override
            public ExerciseIndexHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.exercise_index, group, false);

                return new ExerciseIndexHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return layout;
    }

    public void onButtonPressed(String exerciseName) {
        if (mListener != null) {
            mListener.onFragmentMessage("Fitness", exerciseName);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}