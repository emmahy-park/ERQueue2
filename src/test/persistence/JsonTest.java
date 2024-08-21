package persistence;

import model.Patient;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkPatient(String name, int age, String severity, String admitTime, Patient patient) {
        assertEquals(name, patient.getName());
        assertEquals(age, patient.getAge());
        assertEquals(severity, patient.getSeverityLevel());

        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int waitTimeHour = hour - Integer.parseInt(admitTime.substring(0,2));
        int waitTimeMinute = minute - Integer.parseInt(admitTime.substring(3,5));

        assertEquals((waitTimeHour * 60) + waitTimeMinute, patient.getWaitTime());
    }
}
