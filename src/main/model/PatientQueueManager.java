package model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.QueueDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PatientQueueManager {

    //EFFECTS: View application description
    public static class AppDescription extends AbstractAction {
        public AppDescription() {
            super("View Description");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame("ERQueue Description");
            JPanel panel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea(
                    "\nERQueue is a Java desktop application with a graphical user interface designed to triage patients in the Emergency Room. \n" +
                            "In Metro Vancouver, unexpectedly long wait times in the Emergency Room are common, and patients often have no clear idea of their expected wait time or their place in the queue. \n" +
                            "To streamline the prioritization process and keep patients informed this application generates a prioritized queue based on patient information. \n" +
                            "The queue ranks patients according to their age, level of severity, and time of arrival.\n" +
                            "Patients/guardians can view their place in the queue on the GUI panel, while the triage nurse can use the menu and buttons");
            textArea.setFont(new Font("Arial", Font.PLAIN, 16));
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setMargin(new Insets(10, 10, 10, 10));
            panel.add(textArea, BorderLayout.CENTER);
            frame.add(panel);
            frame.setSize(500,350);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    //EFFECTS: View patients' detailed information with a password
    public static class PatientDetailAction extends AbstractAction {
        private static final String PASSWORD = "ERQueue";
        private static final String JSON_STORE = "./data/patientQueue.json";

        public PatientDetailAction() {
            super("View Saved Patient Information");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JPasswordField passwordField = new JPasswordField(10);
            int option = JOptionPane.showConfirmDialog(
                    null,
                    passwordField,
                    "Enter Password",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            //Check if the user pressed OK
            if (option == JOptionPane.OK_OPTION) {
                char[] input = passwordField.getPassword();
                if (isPasswordCorrect(input)) {
                    try {
                        showPatientDetails();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid password. Try again.",
                            "Error Message",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

                //Clear the password field
                Arrays.fill(input, '0');
            }
        }

        private boolean isPasswordCorrect(char[] input) {
            char[] correctPassword = PASSWORD.toCharArray();
            boolean isCorrect = Arrays.equals(input, correctPassword);

            //Zero out the password
            Arrays.fill(correctPassword, '0');
            return isCorrect;
        }

        private void showPatientDetails() throws FileNotFoundException {
            JFrame frame = new JFrame("Patient Details");
            //JPanel panel = new JPanel(new BorderLayout());

            StringBuilder jsonString = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(JSON_STORE))) {
                JSONTokener tokener = new JSONTokener(br);
                JSONObject jsonObject = new JSONObject(tokener);

                jsonString.append(jsonObject.toString(4));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            JTextArea textArea = new JTextArea();
            textArea.setText(jsonString.toString());
            textArea.setFont(new Font("Arial", Font.PLAIN, 16));
            textArea.setCaretPosition(0);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(600,400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    //EFFECTS: Close the application
    public static class ExitAction extends AbstractAction {
        private static final String JSON_STORE = "./data/patientQueue.json";

        public ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    //EFFECTS: Loads saved patient queue
    public static class LoadQueueAction extends AbstractAction {
        JsonReader jsonReader;
        private static final String JSON_STORE = "./data/patientQueue.json";

        public LoadQueueAction() {
            super("Load Saved Queue");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                QueueDemo queueDemo = new QueueDemo();
                queueDemo.viewPatientQueue();
            } catch (IOException exception) {
                System.out.println("Error loading queue from file: " + JSON_STORE);
            }
        }
    }

    //EFFECTS: Saves current patient queue
    public static class SaveQueueAction extends AbstractAction {
        JsonWriter jsonWriter;
        private static final String JSON_STORE = "./data/patientQueue.json";

        public SaveQueueAction() {
            super("Save Current Queue");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            jsonWriter = new JsonWriter(JSON_STORE);
            PatientQueue patientQueue = QueueDemo.getPatientQueue();

            try {
                jsonWriter.open();
                jsonWriter.write(patientQueue);
                jsonWriter.close();
            } catch (FileNotFoundException exception) {
                System.out.println("Error saving queue to file: " + JSON_STORE);
            }
        }
    }

    //EFFECTS: Clears the current patient queue
    public static class ClearQueueAction extends AbstractAction {
        private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        public ClearQueueAction() {
            super("Clear Current Queue");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultListModel<String> patientListModel = QueueDemo.getPatientListModel();
            patientListModel.removeAllElements();
            PatientQueue patientqueue = new PatientQueue(date);
            QueueDemo.setPatientQueue(patientqueue);
        }
    }

}
