package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import static java.util.Objects.isNull;

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

    private Button playButton;

    private ExerciseObject exerciseObject;

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_exercise, container, false);

        if (getArguments() != null) {
            String exerciseName = getArguments().getString(ARG_EXERCISE_NAME);
            readData(exerciseName, new FirestoreCallback() {
                @Override
                public void onCallBack(ExerciseObject exerciseObject) {

                    // Populate TextViews
                    exerciseText.setText(exerciseObject.getName());
                    typeText.setText(exerciseObject.getType());
                    levelText.setText(exerciseObject.getLevel());
                    muscleText.setText(exerciseObject.getMuscle());
                    equipmentText.setText(exerciseObject.getEquipment());

                    //Populate instructions in ListView
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getContext(),
                            android.R.layout.simple_list_item_1,
                            exerciseObject.getInstructions()
                    );
                    instructionsList.setAdapter(arrayAdapter);
                }
            });

        }

        exerciseText = layout.findViewById(R.id.exercise_name_text_view);
        typeText = layout.findViewById(R.id.type_text_view);
        levelText = layout.findViewById(R.id.level_text_view);
        muscleText = layout.findViewById(R.id.muscle_text_view);
        equipmentText = layout.findViewById(R.id.equipment_text_view);
        instructionsList = layout.findViewById(R.id.instructions_list_view);
        playButton = layout.findViewById(R.id.playVideoButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(exerciseObject.getVideo());
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
                        exerciseObject = documentSnapshot.toObject(ExerciseObject.class);
                        firestoreCallback.onCallBack(exerciseObject);
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(ExerciseObject exerciseObject);
    }
}
