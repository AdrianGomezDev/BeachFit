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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddFoodLog extends DialogFragment {
    private static final String FOOD_MODEL_KEY = "FoodModel";

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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_fragment_add_food_log, null);

        final FoodModel foodModel = (FoodModel) Objects.requireNonNull(getArguments()).getSerializable(FOOD_MODEL_KEY);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final EditText servingsInput = layout.findViewById(R.id.servingSizeEditText);

        // Get date of the meal from user, default is the current date.
        final TextView dateInput = layout.findViewById(R.id.dateOfMealEditText);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMM dd, yyyy"; //In which you need put here
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

                        Map<String, Object> foodDoc = new HashMap<>();
                        Map<String, Object> nestedFoodData = new HashMap<>();

                        nestedFoodData.put("serving quantity", Objects.requireNonNull(foodModel).getServingQuantity());
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
                        nestedFoodData.put("total calories consumed", totalCalsConsumed);

                        foodDoc.put(Objects.requireNonNull(foodModel).getFoodName(), nestedFoodData);

                        // Update FireStore database
                        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .collection("Diet Logs").document(dateInput.getText().toString())
                                .set(foodDoc, SetOptions.merge())
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
