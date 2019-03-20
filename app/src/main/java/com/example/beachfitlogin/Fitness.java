package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Fitness extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirestoreRecyclerAdapter<ExerciseObject, ExerciseIndexHolder> adapter;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Fitness");
        View layout = inflater.inflate(R.layout.fragment_fitness, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        // Set up RecyclerView
        RecyclerView recyclerView = layout.findViewById(R.id.fitnessRecyclerView);
        // Add dividers to recyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        //Fetch Exercises from Firestore
        Query query = FirebaseFirestore.getInstance().collection("Exercises")
                .orderBy("Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ExerciseObject> options = new FirestoreRecyclerOptions.Builder<ExerciseObject>()
                .setQuery(query, ExerciseObject.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ExerciseObject, ExerciseIndexHolder>(options) {

            @Override
            public void onBindViewHolder(ExerciseIndexHolder holder, int position, ExerciseObject exerciseModel) {
                final String exerciseName = exerciseModel.getName();
                holder.setExerciseName(exerciseName);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onButtonPressed(exerciseName);
                    }
                });
            }

            @Override
            public ExerciseIndexHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.exercise_index, group, false);

                return new ExerciseIndexHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
