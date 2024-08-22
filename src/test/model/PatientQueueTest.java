package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class PatientQueueTest {
    private PatientQueue testQueue;
    public Patient patient1;
    public Patient patient2;
    public Patient patient3;
    public Patient patient4;
    public Patient patient5;
    public Patient patient6;
    public Patient patient7;
    public Patient patient8;
    public Patient patient9;
    public Patient patient10;
    public Patient patient11;
    public Patient patient12;
    public Patient patient13;
    public Patient patient14;
    public Patient patient15;
    public Patient patient16;
    public Patient patient17;
    public Patient patient18;
    public ArrayList<Patient> testList;
    private PatientQueue jsonQueue;

    @BeforeEach
    public void setUp() {
        this.testQueue = new PatientQueue("2024-08-19");
        this.patient1 = new Patient("A", 5, "Mild", "04:30");
        this.patient2 = new Patient("B", 5, "Mild", "04:00");
        this.patient3 = new Patient("C", 5, "Moderate", "04:30");
        this.patient4 = new Patient("D", 5, "Moderate", "04:00");
        this.patient5 = new Patient("E", 5, "Severe", "04:30");
        this.patient6 = new Patient("F", 5, "Severe", "04:00");
        this.patient7 = new Patient("G", 50, "Mild", "04:30");
        this.patient8 = new Patient("H", 50, "Mild", "04:00");
        this.patient9 = new Patient("I", 50, "Moderate", "04:30");
        this.patient10 = new Patient("J", 50, "Moderate", "04:00");
        this.patient11 = new Patient("K", 50, "Severe", "04:30");
        this.patient12 = new Patient("L", 50, "Severe", "04:00");
        this.patient13 = new Patient("M", 85, "Mild", "04:30");
        this.patient14 = new Patient("N", 85, "Mild", "04:00");
        this.patient15 = new Patient("O", 85, "Moderate", "04:30");
        this.patient16 = new Patient("P", 85, "Moderate", "04:00");
        this.patient17 = new Patient("Q", 85, "Severe", "04:30");
        this.patient18 = new Patient("R", 85, "Severe", "04:00");

        testQueue.addPatient(patient1);
        testQueue.addPatient(patient2);
        testQueue.addPatient(patient3);
        testQueue.addPatient(patient4);
        testQueue.addPatient(patient5);
        testQueue.addPatient(patient6);
        testQueue.addPatient(patient7);
        testQueue.addPatient(patient8);
        testQueue.addPatient(patient9);
        testQueue.addPatient(patient10);
        testQueue.addPatient(patient11);
        testQueue.addPatient(patient12);
        testQueue.addPatient(patient13);
        testQueue.addPatient(patient14);
        testQueue.addPatient(patient15);
        testQueue.addPatient(patient16);
        testQueue.addPatient(patient17);
        testQueue.addPatient(patient18);
    }

    //Test the constructor
    @Test
    void testConstructor() {
        assertEquals(18, testQueue.getTotalNumberOfPatients());
        assertEquals("2024-08-19", testQueue.getQueueName());
    }

    //Test Sorting
    @Test
    void testSortQueue() {
        testList = testQueue.getPatients();
        assertEquals("F", testList.get(0).getName());
        assertEquals(5, testList.get(0).getAge());
        assertEquals("R", testList.get(1).getName());
        assertEquals(85, testList.get(1).getAge());
        assertEquals("Q", testList.get(2).getName());
        assertEquals(85, testList.get(2).getAge());
        assertEquals("E", testList.get(3).getName());
        assertEquals(5, testList.get(3).getAge());
        assertEquals("L", testList.get(4).getName());
        assertEquals(50, testList.get(4).getAge());
        assertEquals("K", testList.get(5).getName());
        assertEquals(50, testList.get(5).getAge());
        assertEquals("P", testList.get(6).getName());
        assertEquals(85, testList.get(6).getAge());
        assertEquals("D", testList.get(7).getName());
        assertEquals(5, testList.get(7).getAge());
        assertEquals("C", testList.get(8).getName());
        assertEquals(5, testList.get(8).getAge());
        assertEquals("O", testList.get(9).getName());
        assertEquals(85, testList.get(9).getAge());
        assertEquals("J", testList.get(10).getName());
        assertEquals(50, testList.get(10).getAge());
        assertEquals("I", testList.get(11).getName());
        assertEquals(50, testList.get(11).getAge());
        assertEquals("N", testList.get(12).getName());
        assertEquals(85, testList.get(12).getAge());
        assertEquals("B", testList.get(13).getName());
        assertEquals(5, testList.get(13).getAge());
        assertEquals("M", testList.get(14).getName());
        assertEquals(85, testList.get(14).getAge());
        assertEquals("A", testList.get(15).getName());
        assertEquals(5, testList.get(15).getAge());
        assertEquals("H", testList.get(16).getName());
        assertEquals(50, testList.get(16).getAge());
        assertEquals("G", testList.get(17).getName());
        assertEquals(50, testList.get(17).getAge());
    }

    @Test
    void testRemovePatient() {
        assertEquals(18, testQueue.getTotalNumberOfPatients());
        testQueue.removePatient();
        assertEquals(17, testQueue.getTotalNumberOfPatients());
    }

    @Test
    void testJson() {
        this.jsonQueue = new PatientQueue("Json Queue");
        jsonQueue.addPatient(patient1);
        assertEquals("Json Queue", jsonQueue.toJson().get("name"));
    }
}
