package com.example.beachfitlogin;

import java.util.ArrayList;
import java.util.Arrays;

public class ExerciseModel {
    private String Name;
    private String Equipment;
    private String Level;
    private String Muscle;
    private String Type;
    private String Video;

    private ArrayList<String> Instructions;

    public ExerciseModel() { }

    public ExerciseModel(String Name, String Equipment, String Level, String Muscle, String Type,
                         String Video, String[] Instructions) {
        this.Name = Name;
        this.Equipment = Equipment;
        this.Level = Level;
        this.Muscle = Muscle;
        this.Type = Type;
        this.Video = Video;
        this.Instructions = new ArrayList<String>(Arrays.asList(Instructions));
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEquipment() {
        return Equipment;
    }

    public void setEquipment(String equipment) {
        Equipment = equipment;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getMuscle() {
        return Muscle;
    }

    public void setMuscle(String muscle) {
        Muscle = muscle;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getVideo() { return Video; }

    public void setVideo(String video) { Video = video; }

    public ArrayList<String> getInstructions() {
        return Instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        Instructions = instructions;
    }
}
