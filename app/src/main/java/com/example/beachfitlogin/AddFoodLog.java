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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beachfitlogin.Models.FoodModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddFoodLog extends DialogFragment {
    private static final String FOOD_MODEL_KEY = "FoodModel";

    private FoodModel foodModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText servingsInput;
    private TextView dateInput;
    private Calendar myCalendar;

    public static AddFoodLog newInstance(FoodModel food) {
        AddFoodLog dialogFragment = new AddFoodLog();
        Bundle args = new Bundle();
        args.putSerializable(FOOD_MODEL_KEY, food);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_add_food_log, null);

        foodModel = (FoodModel) Objects.requireNonNull(getArguments()).getSerializable(FOOD_MODEL_KEY);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        servingsInput = layout.findViewById(R.id.servingSizeEditText);

        // Get date of the meal from user, default is the current date.
        dateInput = layout.findViewById(R.id.dateOfMealEditText);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMM dd, yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateInput.setText(sdf.format(myCalendar.getTime()));
            }
        };
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
        builder.setTitle("When and how much did you eat?")
                .setPositiveButton("Add to Log", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Map<String, Object> date = new HashMap<>();
                        date.put("date", dateInput.getText().toString());

                        // Update FireStore Diet Log collection with date of consumption
                        db.collection("users")
                                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .collection("Diet Logs")
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

                        Map<String, Object> foodLog = new HashMap<>();
                        Map<String, Object> foodDoc = new HashMap<>();
                        Map<String, Object> nestedFoodData = new HashMap<>();

                        nestedFoodData.put("foodName", Objects.requireNonNull(foodModel).getFoodName());
                        nestedFoodData.put("photoThumb", foodModel.getPhotoThumb().toString());
                        nestedFoodData.put("servingQuantity", Objects.requireNonNull(foodModel).getServingQuantity());
                        nestedFoodData.put("servingUnit", foodModel.getServingUnit());
                        nestedFoodData.put("servingWeight", foodModel.getServingWeight());
                        nestedFoodData.put("calories", foodModel.getCalories());
                        nestedFoodData.put("totalFat", foodModel.getTotalFat());
                        nestedFoodData.put("saturatedFat", foodModel.getSaturatedFat());
                        nestedFoodData.put("cholesterol", foodModel.getCholesterol());
                        nestedFoodData.put("sodium", foodModel.getSodium());
                        nestedFoodData.put("totalCarbs", foodModel.getTotalCarbs());
                        nestedFoodData.put("dietaryFiber", foodModel.getDietaryFiber());
                        nestedFoodData.put("sugars", foodModel.getSugars());
                        nestedFoodData.put("protein", foodModel.getProtein());

                        Double servingsConsumed = Double.parseDouble(servingsInput.getText().toString());
                        nestedFoodData.put("servingsConsumed", servingsConsumed);

                        Double totalCalsConsumed = servingsConsumed * foodModel.getCalories();
                        nestedFoodData.put("totalCalsConsumed", totalCalsConsumed);

                        foodDoc.put(foodModel.getFoodName(), nestedFoodData);
                        foodLog.put("foodLog", foodDoc);

                        // Update FireStore foodLog collection for that specific date
                        db.collection("users")
                                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .collection("Diet Logs")
                                .document(dateInput.getText().toString())
                                .set(foodLog, SetOptions.merge())
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
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        AddFoodLog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}