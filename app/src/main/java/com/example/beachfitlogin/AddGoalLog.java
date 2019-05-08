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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.beachfitlogin.Util.isEmpty;
import static com.example.beachfitlogin.Util.setTimeToZero;

public class AddGoalLog extends DialogFragment {
    private static final String GOAL_MODEL_KEY = "GoalModel";
    private Date timestamp;

    public static AddGoalLog newInstance(GoalModel goal) {
        AddGoalLog dialogFragment = new AddGoalLog();
        Bundle args = new Bundle();
        args.putSerializable(GOAL_MODEL_KEY, goal);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_fragment_add_goal_log, null);
        Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);
        final List<String> exercises = new ArrayList<>();


        final GoalModel goalModel = (GoalModel) Objects.requireNonNull(getArguments()).getSerializable(GOAL_MODEL_KEY);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final EditText goalNameInput = layout.findViewById(R.id.goalNameEditText);
        final Spinner spinnerInput = layout.findViewById(R.id.spinner);
        final EditText pointsValInput = layout.findViewById(R.id.pointsValNameEditText);

        // Get date of the goal from user, default is the current date.
        final TextView dateInput = layout.findViewById(R.id.dateOfGoalEditText);
        final Calendar myCalendar;



        final ArrayAdapter<String> adapterE = new ArrayAdapter<String>(getContext().getApplicationContext(), android.R.layout.simple_spinner_item, exercises);
        adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterE);
        FirebaseFirestore.getInstance().collection("Exercises")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String exercise = document.getString("Name");
                        exercises.add(exercise);
                    }
                    adapterE.notifyDataSetChanged();
                }
            }
        });



        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        setTimeToZero(myCalendar);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTimeToZero(myCalendar);
                timestamp = new Date(myCalendar.getTimeInMillis());
                String myFormat = "MMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateInput.setText(sdf.format(myCalendar.getTime()));
            }
        };
        timestamp = new Date(myCalendar.getTimeInMillis());
        dateInput.setText(DateFormat.getDateInstance().format(timestamp));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateInput.setText(DateFormat.getDateInstance().format(new Date()));
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        builder.setView(layout);
        builder.setTitle("Set a new goal ")
                .setPositiveButton("Add to Log", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isEmpty(goalNameInput)){
                            goalNameInput.setError("Please type a goal name");
                        }
                        else{

                            if(isEmpty(pointsValInput)){
                                pointsValInput.setText("100");
                            }

                            Map<String, Object> goalDoc = new HashMap<>();
                            Map<String, Object> nestedgoalData = new HashMap<>();
                            Map<String, Object> date = new HashMap<>();
                            date.put("date", timestamp);

                            // Update FireStore Goal Log collection with date of consumption
                            db.collection("users")
                                    .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                    .collection("Goal Logs")
                                    .document(goalNameInput.getText().toString())
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

                        /*nestedFoodData.put("serving quantity", Objects.requireNonNull(foodModel).getServingQuantity());
                        nestedFoodData.put("serving unit", foodModel.getServingUnit());
                        nestedFoodData.put("serving weight", foodModel.getServingWeight());
                        nestedFoodData.put("calories", foodModel.getCalories());
                        nestedFoodData.put("total fat", foodModel.getTotalFat());
                        nestedFoodData.put("saturated fat", foodModel.getSaturatedFat());
                        nestedFoodData.put("cholesterol", foodModel.getCholesterol());
                        nestedFoodData.put("sodium", foodModel.getSodium());
                        nestedFoodData.put("total carbohydrates", foodModel.getTotalCarbohydrates());
                        nestedFoodData.put("dietary fiber", foodModel.getDietaryFiber());
                        nestedFoodData.put("sugars", foodModel.getSugars());
                        nestedFoodData.put("protein", foodModel.getProtein());

                        Double servingsConsumed = Double.parseDouble(servingsInput.getText().toString());
                        nestedFoodData.put("servings consumed", servingsConsumed);

                        Double totalCalsConsumed = servingsConsumed * foodModel.getCalories();
                        nestedFoodData.put("total calories consumed", totalCalsConsumed);*/

                            //goalDoc.put(Objects.requireNonNull(goalModel).getGoalName(), nestedgoalData);
                            goalDoc.put("name",goalNameInput.getText().toString());
                            goalDoc.put("exerciseName", spinnerInput.getSelectedItem().toString());
                            goalDoc.put("pointsVal", Integer.parseInt(pointsValInput.getText().toString()));
                            goalDoc.put("complete", false);

                            // Update FireStore database
                        /*db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .collection("Goal Logs").document(goalNameInput.getText().toString())
                                .set(goalDoc, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("FIRESTORE", "Error writing document", e);
                                    }
                                });*/

                            db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                    .collection("Goal Logs").document(goalNameInput.getText().toString())
                                    .set(goalDoc, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FIRESTORE", "Error writing document", e);
                                        }
                                    });
                        }}

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        AddGoalLog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
