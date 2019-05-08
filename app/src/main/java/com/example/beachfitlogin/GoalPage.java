package com.example.beachfitlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GoalPage extends Fragment{

    private static final String ARG_GOAL_NAME = "Exercise Name";
    private static final String TAG = "Exercise";

    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private OnFragmentInteractionListener mListener;

    private TextView name;
    private TextView date;
    private TextView exerciseName;
    private TextView complete;
    private TextView pointsVal;

    private GoalModel goalModel;

    public GoalPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param goalName Name of the exercise to query.
     * @return A new instance of fragment Exercise.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalPage newInstance(String goalName) {
        GoalPage fragment = new GoalPage();
        Bundle args = new Bundle();
        args.putString(ARG_GOAL_NAME, goalName);
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

        View layout = inflater.inflate(R.layout.fragment_goalpage, container, false);

        if (getArguments() != null) {
            final String goalName = getArguments().getString(ARG_GOAL_NAME);
            readData(goalName, new FirestoreCallback() {
                @Override
                public void onCallBack(GoalModel goalModel) {

                    if(goalModel.isComplete()){
                        complete.setText("Completed");
                    }
                    else{
                        complete.setText("Incomplete");
                    }
                    // Populate TextViews
                    name.setText(goalModel.getName());
                    exerciseName.setText(goalModel.getExerciseName());
                    pointsVal.setText(Integer.toString(goalModel.getPointsVal()));



                }
            });

        }

        name = layout.findViewById(R.id.goal_name_text_view);
        exerciseName = layout.findViewById(R.id.exercise_text_view);
        pointsVal = layout.findViewById(R.id.level_text_view);
        complete = layout.findViewById(R.id.muscle_text_view);
        Button completeButton = layout.findViewById(R.id.completeButton);
        Button backButton = layout.findViewById(R.id.backButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("users")
                        .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                        .update("points", FieldValue.increment(goalModel.getPointsVal()));

                FirebaseFirestore.getInstance().collection("users")
                        .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                        .collection("Goal Logs")
                        .document(name.getText().toString())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FireStore", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FireStore", "Error writing document", e);
                            }
                        });

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /*playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(exerciseModel.getVideo());
            }
        });*/

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

    private void readData(String goalName, final FirestoreCallback firestoreCallback){
        // Fetch the exercise from Firestore
        FirebaseFirestore.getInstance().collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("Goal Logs")
                .document(goalName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        goalModel = documentSnapshot.toObject(GoalModel.class);
                        firestoreCallback.onCallBack(goalModel);
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(GoalModel goalModel);
    }
}
