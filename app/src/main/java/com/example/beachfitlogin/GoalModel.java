package com.example.beachfitlogin;


import java.io.Serializable;

public class GoalModel implements Serializable {
    private String name;
    private String exerciseName;
    private boolean complete;
    private int pointsVal;

    public GoalModel() {}

    public GoalModel(String name, String exerciseName, boolean complete, int pointsVal) {
        this.name = name;
        this.exerciseName = exerciseName;
        this.complete = complete;
        this.pointsVal = pointsVal;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getPointsVal() {
        return pointsVal;
    }

    public void setPointsVal(int pointsVal) {
        this.pointsVal = pointsVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}