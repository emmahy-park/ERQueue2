package model;

import java.time.LocalTime;

import org.json.JSONObject;
import persistence.Writable;

public class Patient implements Writable {
    private String name;
    private int age;
    private String severityLevel;
    private String admitTime;

    //EFFECTS: construct a patient with associated information such as
    //name, age, level of severity, and wait time
    public Patient(String name, int age, String severityLevel, String admitTime) {
        this.name = name;
        this.age = age;
        this.admitTime = admitTime;

        if ((severityLevel.equals("Severe") || severityLevel.equals("A")) && (age <= 5 || age >= 80)) {
            this.severityLevel = "A";
        } else if (severityLevel.equals("Severe") || severityLevel.equals("B")) {
            this.severityLevel = "B";
        } else if ((severityLevel.equals("Moderate") || severityLevel.equals("C")) && (age <= 5 || age >= 80)) {
            this.severityLevel = "C";
        } else if (severityLevel.equals("Moderate") || severityLevel.equals("D")) {
            this.severityLevel = "D";
        } else if ((severityLevel.equals("Mild") || severityLevel.equals("E")) && (age <= 5 || age >= 80)) {
            this.severityLevel = "E";
        } else {
            this.severityLevel = "F";
        }
    }

    //EFFECTS: Get patient name
    public String getName() {
        return name;
    }

    //EFFECTS: Get severity level
    public String getSeverityLevel() {
        return severityLevel;
    }

    //EFFECTS: Get patient age
    public int getAge() {
        return age;
    }

    //EFFECTS: Calculate and get wait time
    public int getWaitTime() {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int waitTimeHour = hour - Integer.parseInt(admitTime.substring(0,2));
        int waitTimeMinute = minute - Integer.parseInt(admitTime.substring(3,5));

        return (waitTimeHour * 60) + waitTimeMinute;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("age", age);
        json.put("severityLevel", severityLevel);
        json.put("admitTime", admitTime);
        return json;
    }

}
