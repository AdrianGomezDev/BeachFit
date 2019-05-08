package com.example.beachfitlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Exercise extends Fragment{

    private static final String ARG_EXERCISE_NAME = "Exercise Name";
    private static final String TAG = "Exercise";

    private OnFragmentInteractionListener mListener;

    private TextView exerciseText;
    private TextView typeText;
    private TextView levelText;
    private TextView muscleText;
    private TextView equipmentText;
    private ListView instructionsList;

    private ExerciseModel exerciseModel;

    public Exercise() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param exerciseName Name of the exercise to query.
     * @return A new instance of fragment Exercise.
     */
    // TODO: Rename and change types and number of parameters
    public static Exercise newInstance(String exerciseName) {
        Exercise fragment = new Exercise();
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_NAME, exerciseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //if we pass an argument to exercise fragment, handle it here
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_exercise, container, false);

        if (getArguments() != null) {
            String exerciseName = getArguments().getString(ARG_EXERCISE_NAME);
            readData(exerciseName, new FirestoreCallback() {
                @Override
                public void onCallBack(ExerciseModel exerciseModel) {

                    // Populate TextViews
                    exerciseText.setText(exerciseModel.getName());
                    typeText.setText(exerciseModel.getType());
                    levelText.setText(exerciseModel.getLevel());
                    muscleText.setText(exerciseModel.getMuscle());
                    equipmentText.setText(exerciseModel.getEquipment());

                    //Populate instructions in ListView
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                            Objects.requireNonNull(getContext()),
                            android.R.layout.simple_list_item_1,
                            exerciseModel.getInstructions()
                    );
                    instructionsList.setAdapter(arrayAdapter);
                }
            });

        }

        exerciseText = layout.findViewById(R.id.goal_name_text_view);
        typeText = layout.findViewById(R.id.exercise_text_view);
        levelText = layout.findViewById(R.id.level_text_view);
        muscleText = layout.findViewById(R.id.muscle_text_view);
        equipmentText = layout.findViewById(R.id.equipment_text_view);
        instructionsList = layout.findViewById(R.id.instructions_list_view);
        Button playButton = layout.findViewById(R.id.playVideoButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(exerciseModel.getVideo());
            }
        });

        // Inflate the layout for this fragment
        return layout;
    }

    public void onButtonPressed(String videoID) {
        if (mListener != null) {
            mListener.onFragmentMessage(TAG, videoID);
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

    private void readData(String exerciseName, final FirestoreCallback firestoreCallback){
        // Fetch the exercise from Firestore
        FirebaseFirestore.getInstance().collection("Exercises")
                .document(exerciseName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        exerciseModel = documentSnapshot.toObject(ExerciseModel.class);
                        firestoreCallback.onCallBack(exerciseModel);
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(ExerciseModel exerciseModel);
    }
}
