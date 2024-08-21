package persistence;

import model.Patient;
import model.PatientQueue;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader jsonReader = new JsonReader("./data/nonExistentFile.json");
        try {
            PatientQueue patientQueue = jsonReader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    void testReaderEmptyFile() {
        JsonReader jsonReader = new JsonReader("./data/emptyFile.json");
        try {
            PatientQueue patientQueue = jsonReader.read();
            assertEquals("2024-08-21", patientQueue.getQueueName());
            assertEquals(0, patientQueue.getTotalNumberOfPatients());
        } catch (IOException e) {
            fail("IOException expected");
        }
    }

    @Test
    void testReaderPatientQueue() {
        JsonReader jsonReader = new JsonReader("./data/testReaderPatientQueue.json");
        try {
            PatientQueue patientQueue = jsonReader.read();
            assertEquals("2024-08-21", patientQueue.getQueueName());
            List<Patient> patientsList = patientQueue.getPatients();
            assertEquals(3, patientsList.size());
            checkPatient("Emma", 27, "B", "10:00", patientsList.get(0));
            checkPatient("Ray", 80, "C", "10:00", patientsList.get(1));
            checkPatient("Jen", 10, "F", "10:00", patientsList.get(2));
        } catch (IOException e) {
            fail("IOException expected");
        }
    }
}
