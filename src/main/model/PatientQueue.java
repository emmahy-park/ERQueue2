package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PatientQueue implements Writable {
    private PriorityQueue<Patient> patientQueue;
    private String name;
    private ArrayList<Patient> patientList;

    //EFFECTS: Construct patient queue with a name and empty queue of patients
    public PatientQueue(String name) {
        this.patientQueue = new PriorityQueue<>(new Comparator<Patient>() {
            @Override
            public int compare(Patient p1, Patient p2) {
                int losCompare = p1.getSeverityLevel().compareTo(p2.getSeverityLevel());
                int waitTimeCompare = (p2.getWaitTime() - p1.getWaitTime());

                if (losCompare == 0) return waitTimeCompare;
                else return losCompare;
            }
        });
        this.name = name;
    }

    //EFFECTS: Get name of the queue
    public String getQueueName() {
        return name;
    }

    //MODIFIES: this
    //EFFECTS: Adds a new patient in the queue
    public void addPatient(Patient patient) {
        patientQueue.add(patient);
    }

    //EFFECTS: Get total number of patients in the queue
    public int getTotalNumberOfPatients() {
        return patientQueue.size();
    }

    //REQUIRES: the queue should not be empty
    //MODIFIES: this
    //EFFECTS: Remove a patient first in the queue
    public void removePatient() {
        patientQueue.remove();
    }

    //EFFECTS: Create a patient list and add the patient list to the queue
    public ArrayList<Patient> getPatients() {
        patientList = new ArrayList<>();
        while (!patientQueue.isEmpty()) {
            patientList.add(patientQueue.poll());
        }
        patientQueue.addAll(patientList);
        return patientList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("patients", patientsToJson());
        return json;
    }

    //EFFECTS: returns patients in this patient queue as a JSON array
    private JSONArray patientsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Patient p : patientQueue) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }
}
