package com.example.beachfitlogin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beachfitlogin.Models.ExerciseModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.beachfitlogin.Util.isEmpty;
import static com.example.beachfitlogin.Util.setTimeToZero;

public class AddFitnessLog extends DialogFragment {
    private static final String EXERCISE_MODEL_KEY = "ExerciseModel";

    private ExerciseModel exerciseModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText repsInput;
    private EditText setsInput;
    private EditText weightInput;
    private EditText distanceInput;
    private EditText durationInput;
    private TextView dateInput;
    private Calendar myCalendar;
    private Date timestamp;

    public static AddFitnessLog newInstance(ExerciseModel model) {
        AddFitnessLog dialogFragment = new AddFitnessLog();
        Bundle args = new Bundle();
        args.putSerializable(EXERCISE_MODEL_KEY, model);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_add_fitness_log,null);

        exerciseModel = (ExerciseModel) Objects.requireNonNull(getArguments()).getSerializable(EXERCISE_MODEL_KEY);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Strength exercises' input fields and labels
        TextView repsLabel = layout.findViewById(R.id.repsLabel);
        TextView setsLabel = layout.findViewById(R.id.setsLabel);
        TextView weightLabel = layout.findViewById(R.id.weightLabel);
        repsInput = layout.findViewById(R.id.repsInput);
        setsInput = layout.findViewById(R.id.setsInput);
        weightInput = layout.findViewById(R.id.weightInput);

        // Cardio exercises' input fields and labels
        TextView distanceLabel = layout.findViewById(R.id.distanceLabel);
        TextView durationLabel = layout.findViewById(R.id.durationLabel);
        distanceInput = layout.findViewById(R.id.distanceInput);
        durationInput = layout.findViewById(R.id.durationInput);


        // Hide the unneeded input fields and labels
        if(exerciseModel.getType().equals("Strength")){
            weightLabel.setVisibility(View.VISIBLE);
            weightInput.setVisibility(View.VISIBLE);
            repsLabel.setVisibility(View.VISIBLE);
            repsInput.setVisibility(View.VISIBLE);
            setsLabel.setVisibility(View.VISIBLE);
            setsInput.setVisibility(View.VISIBLE);

            distanceLabel.setVisibility(View.GONE);
            distanceInput.setVisibility(View.GONE);
            durationLabel.setVisibility(View.GONE);
            durationInput.setVisibility(View.GONE);
        }
        else if(exerciseModel.getType().equals("Cardio")){
            weightLabel.setVisibility(View.GONE);
            weightInput.setVisibility(View.GONE);
            repsLabel.setVisibility(View.GONE);
            repsInput.setVisibility(View.GONE);
            setsLabel.setVisibility(View.GONE);
            setsInput.setVisibility(View.GONE);

            distanceLabel.setVisibility(View.VISIBLE);
            distanceInput.setVisibility(View.VISIBLE);
            durationLabel.setVisibility(View.VISIBLE);
            durationInput.setVisibility(View.VISIBLE);
        }

        // Get date of the meal from user, default is the current date.
        dateInput = layout.findViewById(R.id.dateOfExerciseInput);

        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        setTimeToZero(myCalendar);
        final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTimeToZero(myCalendar);
                timestamp = new Date(myCalendar.getTimeInMillis());
                dateInput.setText(sdf.format(myCalendar.getTime()));
            }
        };
        timestamp = new Date(myCalendar.getTimeInMillis());
        dateInput.setText(sdf.format(timestamp));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(layout)
                .setTitle("Add " + exerciseModel.getName() + " to Fitness Log")
                .setCancelable(false)
                .setPositiveButton("Add to Log", null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkForEmptyInputs(exerciseModel.getType())){

                            Map<String, Object> date = new HashMap<>();
                            date.put("date", timestamp);

                            // Update FireStore Fitness Logs collection with date of exercise
                            db.collection("users")
                                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                    .collection("Fitness Logs")
                                    .document(dateInput.getText().toString())
                                    .set(date, SetOptions.merge())
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

                            Map<String, Object> exerciseLog = new HashMap<>();
                            Map<String, Object> exerciseDoc = new HashMap<>();
                            Map<String, Object> nestedExerciseData = new HashMap<>();
                            Map<String, Double> nestedExerciseStats = new HashMap<>();

                            nestedExerciseData.put("name", exerciseModel.getName());
                            nestedExerciseData.put("level", exerciseModel.getName());
                            nestedExerciseData.put("type", exerciseModel.getName());
                            nestedExerciseData.put("muscle", exerciseModel.getName());
                            nestedExerciseData.put("equipment", exerciseModel.getName());

                            if(exerciseModel.getType().equals("Strength")) {
                                nestedExerciseStats.put("weight", Double.parseDouble(weightInput.getText().toString()));
                                nestedExerciseStats.put("repetitions", Double.parseDouble(repsInput.getText().toString()));
                                nestedExerciseStats.put("sets", Double.parseDouble(setsInput.getText().toString()));
                            }
                            else if(exerciseModel.getType().equals("Cardio")){
                                nestedExerciseStats.put("distance", Double.parseDouble(distanceInput.getText().toString()));
                                nestedExerciseStats.put("duration", Double.parseDouble(durationInput.getText().toString()));
                            }
                            nestedExerciseData.put("exerciseStats", nestedExerciseStats);

                            exerciseDoc.put(exerciseModel.getName(), nestedExerciseData);
                            exerciseLog.put("exerciseLog", exerciseDoc);

                            // Update FireStore foodLog collection for that specific date
                            db.collection("users")
                                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                    .collection("Fitness Logs")
                                    .document(dateInput.getText().toString())
                                    .set(exerciseLog, SetOptions.merge())
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
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        // Create the AlertDialog object and return it
        return alertDialog;
    }

    /**
     * Checks the input
     * @param type determines which inputs are necessary to collect
     *             to properly log the exercise in the fitness log
     * @return false if input(s) are empty, else true
     */
    private Boolean checkForEmptyInputs(String type){
        boolean flag = true;

        switch(type){
            case "Strength":
                if(isEmpty(weightInput)){
                    weightInput.setError("Enter value greater than 0!");
                    flag = false;
                }
                else if(isEmpty(repsInput)){
                    repsInput.setError("Enter value greater than 0!");
                    flag = false;
                }
                else if(isEmpty(setsInput)){
                    setsInput.setError("Enter value greater than 0!");
                    flag = false;
                }
                break;
            case "Cardio":
                if(isEmpty(distanceInput)){
                    distanceInput.setError("Enter value greater than 0!");
                    flag = false;
                }
                else if(isEmpty(durationInput)){
                    durationInput.setError("Enter value greater than 0!");
                    flag = false;
                }
                break;
        }
        return flag;
    }
}
