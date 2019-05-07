package com.example.beachfitlogin.Models;

import java.util.Map;

public class LoggedExerciseModel {
    private String name;
    private String equipment;
    private String level;
    private String muscle;
    private String type;
    private Map<String, Double> exerciseStats;


    public LoggedExerciseModel(String name, String equipment, String level, String muscle, String type, Map<String, Double> exerciseStats) {
        this.name = name;
        this.equipment = equipment;
        this.level = level;
        this.muscle = muscle;
        this.type = type;
        this.exerciseStats = exerciseStats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Double> getExerciseStats() {
        return exerciseStats;
    }

    public void setExerciseStats(Map<String, Double> exerciseStats) {
        this.exerciseStats = exerciseStats;
    }
}
