package model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.URL;

import ui.QueueDemo;
import ui.SpringUtilities;

public class ButtonManager {

    //EFFECTS: Add a new patient to the queue
    public static class AddPatientAction extends AbstractAction {

        public AddPatientAction() {
            super("Add Patient");
        }

        //EFFECTS: Creates and set up a new frame to add text
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame("Add Patient");
            frame.add(new TextInput());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private static class TextInput extends JPanel implements ActionListener, FocusListener {

        JTextField patientName;
        JTextField patientAge;
        String selectedSeverity;
        JTextField admitTime;
        JComboBox<String> severityDropdown;
        boolean addPatient = false;
        JButton addButton;

        public TextInput() {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

            JPanel entryPanel = new JPanel() {
                //@Override
                public Dimension getMaximumSize() {
                    Dimension pref = getPreferredSize();
                    return new Dimension(Integer.MAX_VALUE, pref.height);
                }
            };
            entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.PAGE_AXIS));
            entryPanel.add(createEntryField());
            addButton = new JButton("Add");
            addButton.setEnabled(false);
            addButton.addActionListener(this);
            entryPanel.add(createButtons(addButton));
            add(entryPanel);
        }

        //EFFECTS: Creates entry field to add patients
        protected JComponent createEntryField() {
            JPanel panel = new JPanel(new SpringLayout());
            String[] labelStrings = {"Patient Name: ", "Patient Age: ",
                    "Level of Severity (Mild/Moderate/Severe): ", "AdmitTime (i.e. 17:35): "};

            JComponent[] fields = new JComponent[labelStrings.length];
            int fieldNum = 0;

            patientName = new JTextField(20);
            fields[fieldNum++] = patientName;

            patientAge = new JTextField(20);
            fields[fieldNum++] = patientAge;

            String[] severityStrings = getSeverity();
            severityDropdown = new JComboBox<>(severityStrings);
            fields[fieldNum++] = severityDropdown;

            admitTime = new JTextField(20);
            fields[fieldNum++] = admitTime;

            JLabel[] labels = new JLabel[labelStrings.length];
            entryFieldForLoop(labelStrings, labels, panel, fields);

            SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, 10, 10, 10, 5);
            return panel;
        }

        //EFFECTS: Lets users choose between "Mild", "Moderate", and "Severe"
        public String[] getSeverity() {
            return new String[]{
                    "Mild", "Moderate", "Severe",
            };
        }

        //EFFECTS: Displays a button icon next to "Add"
        protected JComponent createButtons(JButton button) {
            ImageIcon buttonIcon = createImageIcon("images/right.gif");
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            button.setIcon(buttonIcon);
            buttonPanel.add(button);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return buttonPanel;
        }

        //EFFECTS: Dynamically create and add labels and input fields to the panel
        private void entryFieldForLoop(String[] labelStrings, JLabel[] labels, JPanel panel, JComponent[] fields) {
            for (int i = 0; i < labelStrings.length; i++) {
                labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
                labels[i].setLabelFor(fields[i]);
                panel.add(labels[i]);
                panel.add(fields[i]);

                if (fields[i] instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) fields[i];
                    comboBox.addActionListener(e -> {
                        selectedSeverity = (String) comboBox.getSelectedItem();
                        updateAddButtonState(addButton);
                    });
                } else {
                    JTextField tf = (JTextField) fields[i];
                    tf.addActionListener(this);
                    tf.addFocusListener(this);

                    //Add document listener to update the Add button state
                    tf.getDocument().addDocumentListener(new DocumentListener() {

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            updateAddButtonState(addButton);
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            updateAddButtonState(addButton);
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            updateAddButtonState(addButton);
                        }
                    });
                }
            }
        }

        private void updateAddButtonState(JButton addButton) {
            addButton.setEnabled(areFieldsFilled());
        }

        private boolean areFieldsFilled() {
            return !patientName.getText().trim().isEmpty() &&
                    !patientAge.getText().trim().isEmpty() &&
                    severityDropdown.getSelectedItem() != null &&
                    !admitTime.getText().trim().isEmpty();
        }

        // REQUIRES: Valid image path
        // EFFECTS: Returns an ImageIcon, or null if the path is invalid
        protected static ImageIcon createImageIcon(String path) {
            URL imgURL = QueueDemo.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if ("clear".equals(e.getActionCommand())) {
                addPatient = false;
                patientName.setText("");
                patientAge.setText("");
                admitTime.setText("");
            } else {
                addPatient = true;
            }
            try {
                updateDisplays();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

        //EFFECTS: Updates patient queue when patient is added to the queue and resets the text field
        protected void updateDisplays() throws IOException {
            PatientQueue patientQueue = QueueDemo.getPatientQueue();
            String selectedSeverity = (String) severityDropdown.getSelectedItem();

            if (addPatient) {
                assert selectedSeverity != null;
                Patient newPatient = new Patient(patientName.getText(), Integer.parseInt(patientAge.getText()),
                        selectedSeverity, admitTime.getText());

                patientQueue.addPatient(newPatient);

                DefaultListModel<String> listModel = QueueDemo.getPatientListModel();
                listModel.removeAllElements();

                for (Patient patient : patientQueue.getPatients()) {
                    listModel.addElement(patient.getName());
                }

                JList list = QueueDemo.getPatientList();
                list.setModel(listModel);

                //Reset the text field
                patientName.setText("");
                patientAge.setText("");
                admitTime.setText("");
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            Component c = e.getComponent();
            if (c instanceof  JFormattedTextField) {
                selectItLater(c);
            } else if (c instanceof JTextField) {
                ((JTextField)c).selectAll();
            }
        }

        protected void selectItLater(Component c) {
            if (c instanceof JFormattedTextField) {
                final JFormattedTextField ftf = (JFormattedTextField)c;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ftf.selectAll();
                    }
                });
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            //ignore
        }
    }

    //EFFECTS: Select and remove the patient in the queue
    public static class RemovePatientAction extends AbstractAction {

        public RemovePatientAction() {
            super("Remove Patient");
        }

        //MODIFIES: listModel, patientQueue
        //EFFECTS: Remove selected patient from listModel and patientQueue
        @Override
        public void actionPerformed(ActionEvent e) {
            JList patientList = QueueDemo.getPatientList();
            int index = patientList.getSelectedIndex();

            if (index != -1) {
                DefaultListModel patientListModel = QueueDemo.getPatientListModel();
                patientListModel.remove(index);

                PatientQueue patientQueue = QueueDemo.getPatientQueue();
                patientQueue.removePatient(index);
                QueueDemo.setPatientQueue(patientQueue);
            }
        }
    }

    //EFFECTS: View the next patient to see the doctor (first patient in queue)
    //and the total number of patients in the queue
    public static class NextPatientAction extends AbstractAction {

        public NextPatientAction() {
            super("Next Patient");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame("Next Patient");
            frame.add(new NextPatient().createUI());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private static class NextPatient extends JPanel {

        //REQUIRES: at least one patient in patient queue
        //EFFECTS: Creates a panel that displays the next patient's name and total number of patients in queue
        public JPanel createUI() {
            JPanel panel = new JPanel();
            DefaultListModel<String> listModel = QueueDemo.getPatientListModel();
            if (listModel.isEmpty()) {
                JLabel nextPatient = new JLabel("There is no patient in queue");
                panel.add(nextPatient);
                panel.add(Box.createVerticalStrut(5));
                panel.add(Box.createVerticalStrut(10));

            } else {
                String name = listModel.getElementAt(0);
                String num = Integer.toString(listModel.size());

                JLabel nextPatient = new JLabel("Next patient: " + name);
                JLabel patientNum = new JLabel("Total number of patients in queue: " + num);

                nextPatient.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                patientNum.setAlignmentX(JComponent.CENTER_ALIGNMENT);

                panel.add(nextPatient);
                panel.add(patientNum);


            }

            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalStrut(5));
            panel.add(Box.createVerticalStrut(10));
            return panel;
        }
    }
}
