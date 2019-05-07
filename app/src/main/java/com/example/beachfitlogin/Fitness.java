package com.example.beachfitlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.beachfitlogin.Adapters.DailyFitnessLogAdapter;
import com.example.beachfitlogin.Adapters.ExerciseAdapter;
import com.example.beachfitlogin.Interfaces.OnFragmentInteractionListener;
import com.example.beachfitlogin.Models.DailyFitnessLogModel;
import com.example.beachfitlogin.Models.ExerciseModel;
import com.example.beachfitlogin.ViewHolders.DailyFitnessLogViewHolder;
import com.example.beachfitlogin.ViewHolders.ExerciseViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class Fitness extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private DailyFitnessLogAdapter.OnDailyFitnessLogClickListener onDailyFitnessLogClickListener;

    private FirestoreRecyclerAdapter<ExerciseModel, ExerciseViewHolder> exerciseAdapter;
    private FirestoreRecyclerAdapter<DailyFitnessLogModel, DailyFitnessLogViewHolder> fitnessAdapter;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Set up exerciseRecycler
        final RecyclerView exerciseRecycler = layout.findViewById(R.id.exerciseRecycler);
        exerciseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up Fitness Diet Logs recycler
        final RecyclerView fitnessLogsRecycler = layout.findViewById(R.id.fitnessLogsRecycler);
        fitnessLogsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up fitness logs query
        Query query = db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("Fitness Logs")
                .orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<DailyFitnessLogModel> options = new FirestoreRecyclerOptions.Builder<DailyFitnessLogModel>()
                .setQuery(query, DailyFitnessLogModel.class)
                .build();

        // TODO: can place actions in here on fitness log click
        onDailyFitnessLogClickListener = new DailyFitnessLogAdapter.OnDailyFitnessLogClickListener() {
            @Override
            public void onDailyFitnessLogClick(int position) {
                //Do nothing for now
                Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            }
        };
        fitnessAdapter = new DailyFitnessLogAdapter(options, onDailyFitnessLogClickListener);
        fitnessLogsRecycler.setAdapter(fitnessAdapter);

        final FloatingActionButton addExerciseFAB = layout.findViewById(R.id.fitnessFAB);
        addExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fitnessLogsRecycler.setVisibility(View.GONE);
                addExerciseFAB.hide();
                exerciseRecycler.setVisibility(View.VISIBLE);
            }
        });

        //Fetch Exercises from FireStore to populate exerciseRecycler
        query = db.collection("Exercises")
                .orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ExerciseModel> exerciseOptions = new FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel.class)
                .build();
        exerciseAdapter = new ExerciseAdapter(exerciseOptions, mListener);
        exerciseRecycler.setAdapter(exerciseAdapter);

        return layout; // Inflate the layout for this fragment
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
        exerciseAdapter.startListening();
        fitnessAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        exerciseAdapter.stopListening();
        fitnessAdapter.stopListening();
    }
}