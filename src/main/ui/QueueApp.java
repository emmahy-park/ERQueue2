package ui;

import model.Patient;
import model.PatientQueue;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class QueueApp {
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;

    private Patient nextPatient;
    private PatientQueue newPatients;

    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/patientQueue.json";
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    //EFFECTS: Runs the Queue Application
    public QueueApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        newPatients = new PatientQueue(date);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runQueue();
    }

    //MODIFIES: this
    //EFFECTS: processes user input
    private void runQueue() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    //MODIFIES: this
    //EFFECTS: initializes queue
    private void init() {
        this.patient1 = new Patient("Alice", 27, "Mild", "14:00");
        this.patient2 = new Patient("Bob", 72, "Moderate", "14:00");
        this.patient3 = new Patient("Carl", 27, "Severe", "14:00");

        this.newPatients = new PatientQueue(date);
        newPatients.addPatient(patient1);
        newPatients.addPatient(patient2);
        newPatients.addPatient(patient3);

        this.input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    //EFFECTS: Displays menu
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add a new patient");
        System.out.println("\tv -> View a patient queue");
        System.out.println("\tr -> Remove the first patient in queue");
        System.out.println("\ts -> Save the patient queue");
        System.out.println("\tl -> Load the latest saved patient queue");
        System.out.println("\tq -> Quit");
    }

    //MODIFIES: this
    //EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "a":
                doAddPatient();
                break;
            case "v":
                doViewQueue();
                break;
            case "r":
                doRemovePatient();
                break;
            case "s":
                savePatientQueue();
                break;
            case "l":
                loadPatientQueue();
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: Add patient
    private void doAddPatient() {
        System.out.print("Enter patient information: \n");
        // Patient Name
        System.out.print("Patient name: ");
        String name = input.next();
        // Patient Age
        System.out.print("Patient age: ");
        int age = input.nextInt();
        // Level of Severity
        System.out.print("Level of Severity (Mild/Moderate/Severe): ");
        String severity = input.next();
        // Registered Time
        System.out.print("Current Time in 24hour format (09:05): ");
        String time = input.next();

        Patient patient = new Patient(name, age, severity, time);
        newPatients.addPatient(patient);

        System.out.println("Patient has been added successfully!");

        doViewQueue();
    }

    //MODIFIES: this
    //EFFECTS: View Patients in the Queue
    private void doViewQueue() {
        System.out.print("\n");
        for (int i = 0; i < newPatients.getTotalNumberOfPatients(); i++) {
            System.out.println(newPatients.getPatients().get(i).getName());
        }
        nextPatient = newPatients.getPatients().get(0);
        System.out.printf("\nNext in queue: %s\n", nextPatient.getName());
        System.out.printf("In total, there are %s number of patients in the queue.\n", newPatients.getTotalNumberOfPatients());
    }

    //MODIFIES: this
    //EFFECTS: Remove patient
    private void doRemovePatient() {
        newPatients.removePatient();
        System.out.println("Patient first in queue has seen a doctor.\n");
        doViewQueue();
    }

    //EFFECTS: Saves the patient queue to file
    private void savePatientQueue() {
        try {
            jsonWriter.open();
            jsonWriter.write(newPatients);
            jsonWriter.close();
            System.out.println("Patient queue " + newPatients.getQueueName() + " has been saved successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //MODIFIES: this
    //EFFECTS: Loads patient queue from the file
    private void loadPatientQueue() {
        try {
            newPatients = jsonReader.read();
            System.out.println("Loaded " + newPatients.getQueueName() + " from file: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
