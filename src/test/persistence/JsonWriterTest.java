package persistence;

import model.Patient;
import model.PatientQueue;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {
    private PatientQueue patientQueue;

    @Test
    void testWriterInvalidFile() {
        try {
            patientQueue = new PatientQueue("2024-08-21");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:filename.json");
            writer.open();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFile() {
        try {
            patientQueue = new PatientQueue("2024-08-21");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPatientQueue.json");
            writer.open();
            writer.write(patientQueue);
            writer.close();

            JsonReader jsonReader = new JsonReader("./data/testWriterEmptyPatientQueue.json");
            patientQueue = jsonReader.read();
            assertEquals("2024-08-21", patientQueue.getQueueName());
            assertEquals(0, patientQueue.getTotalNumberOfPatients());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterPatientQueue() {
        try {
            patientQueue = new PatientQueue("2024-08-21");
            patientQueue.addPatient(new Patient("Emma", 27, "Severe", "12:00"));
            patientQueue.addPatient(new Patient("Mike", 80, "Moderate", "12:00"));
            patientQueue.addPatient(new Patient("John", 10, "Mild", "12:00"));
            patientQueue.addPatient(new Patient("Sarah", 3, "Severe", "12:00"));
            patientQueue.addPatient(new Patient("Mary", 20, "Moderate", "12:00"));
            patientQueue.addPatient(new Patient("Jane", 90, "Mild", "12:00"));

            JsonWriter writer = new JsonWriter("./data/testWriterPatientQueue.json");
            writer.open();
            writer.write(patientQueue);
            writer.close();

            JsonReader jsonReader = new JsonReader("./data/testWriterPatientQueue.json");
            patientQueue = jsonReader.read();
            assertEquals("2024-08-21", patientQueue.getQueueName());
            assertEquals(6, patientQueue.getTotalNumberOfPatients());
            List<Patient> patientList = patientQueue.getPatients();
            checkPatient("Emma", 27, "B", "12:00", patientList.get(1));
            checkPatient("Mike", 80, "C", "12:00", patientList.get(2));
            checkPatient("John", 10, "F", "12:00", patientList.get(5));
            checkPatient("Sarah", 3, "A", "12:00", patientList.get(0));
            checkPatient("Mary", 20, "D", "12:00", patientList.get(3));
            checkPatient("Jane", 90, "E", "12:00", patientList.get(4));
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}
