package com.example.beachfitlogin.Models;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DailyFitnessLogModel {

    private Date date;
    private Map<String, Map<String, Object>> exerciseLog;

    public DailyFitnessLogModel() { }

    public DailyFitnessLogModel(Date date, Map<String, Map<String, Object>> exerciseLog) {
        this.date = date;
        this.exerciseLog = new HashMap<>(exerciseLog);
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<LoggedExerciseModel> getExerciseLog() {

        ArrayList<LoggedExerciseModel> myExerciseLog = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> parentPair : exerciseLog.entrySet()) {

            LoggedExerciseModel exerciseModel = new LoggedExerciseModel(
                    Objects.requireNonNull(parentPair.getValue().get("name")).toString(),
                    Objects.requireNonNull(parentPair.getValue().get("equipment")).toString(),
                    Objects.requireNonNull(parentPair.getValue().get("level")).toString(),
                    Objects.requireNonNull(parentPair.getValue().get("muscle")).toString(),
                    Objects.requireNonNull(parentPair.getValue().get("type")).toString(),
                    (Map<String, Double>)parentPair.getValue().get("exerciseStats")
            );
            myExerciseLog.add(exerciseModel);
        }
        return myExerciseLog;
    }

    public void setExerciseLog(Map<String, Map<String, Object>> exerciseLog) {
        this.exerciseLog = exerciseLog;
    }
}