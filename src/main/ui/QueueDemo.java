package ui;


import model.ButtonManager;
import model.Patient;
import model.PatientQueue;
import model.PatientQueueManager;
import persistence.JsonReader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QueueDemo extends JFrame implements ListSelectionListener, ActionListener {
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private static final String JSON_STORE = "./data/patientQueue.json";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static PatientQueue patientQueue;
    private JFrame controlPanel;
    private static JList list;
    private static DefaultListModel listModel = new DefaultListModel();
    private JButton addButton;
    private JButton removeButton;
    private JButton nextButton;
    private JsonReader jsonReader;
    private static QueueDemo instance;

    public QueueDemo() throws IOException {
        //Loads saved patient queue
        jsonReader = new JsonReader(JSON_STORE);
        patientQueue = jsonReader.read();

        //Construct the control panel
        controlPanel = new JFrame("Patient Queue");
        controlPanel.setLayout(new BorderLayout());
        setTitle("Patient Queue - " + date);
        setSize(WIDTH, HEIGHT);

        viewPatientQueue();
        addButtons();
        addMenu();

//        list = new JList(listModel);
//        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        list.addListSelectionListener(this);

        //Add scroll pane
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);

    }

    //EFFECTS: Getter method to provide access to the list model
    public static DefaultListModel<String> getPatientListModel() {
        return listModel;
    }

    //EFFECTS: Getter method to provide access to the list
    public static JList getPatientList() {
        return list;
    }

    //EFFECTS: Setter method to get the updated patient queue
    public static void setPatientQueue(PatientQueue queue) {
        patientQueue = queue;
    }

    //EFFECTS: Getter method to provide access to updated patient queue
    public static PatientQueue getPatientQueue() {
        return patientQueue;
    }

    //MODIFIES: this
    //EFFECTS: Resets listModel, then add patients' names in listModel.
    public void viewPatientQueue() {
        listModel.removeAllElements();

        for (Patient patient : patientQueue.getPatients()) {
            listModel.addElement(patient.getName());
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
    }

    //EFFECTS: Adds a menu bar at the top
    private void addMenu() {
        //Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        //Create a file menu that contains the app description, print/export, and exit actions
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, new PatientQueueManager.AppDescription());
        addMenuItem(fileMenu, new PatientQueueManager.PatientDetailAction());
        addMenuItem(fileMenu, new PatientQueueManager.ExitAction());

        //Create a menu that contains loading, saving and clearing patient queue
        JMenu queueMenu = new JMenu("Menu");
        addMenuItem(queueMenu, new PatientQueueManager.LoadQueueAction());
        addMenuItem(queueMenu, new PatientQueueManager.SaveQueueAction());
        addMenuItem(queueMenu, new PatientQueueManager.ClearQueueAction());

        //Add the menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(queueMenu);

        setJMenuBar(menuBar);
    }

    //EFFECTS: Adds on item with given handler to the given menu
    private void addMenuItem(JMenu menu, AbstractAction action) {
        JMenuItem menuItem = new JMenuItem(action);
        menu.add(menuItem);
    }

    //EFFECTS: Add buttons
    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        addButton = new JButton(new ButtonManager.AddPatientAction());
        removeButton = new JButton(new ButtonManager.RemovePatientAction());
        nextButton = new JButton(new ButtonManager.NextPatientAction());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(nextButton);

        addButton.setToolTipText("Click this button to add a new patient");
        removeButton.setToolTipText("Click this button to remove a patient");
        nextButton.setToolTipText("Click this button to view the next patient in line");

        add(buttonPanel, BorderLayout.EAST);
    }

    //MODIFIES: this
    //EFFECTS: Disables the remove button when a patient is not selected
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) { //no selection, disable the remove button
                removeButton.setEnabled(false);
            } else {
                addButton.setEnabled(true);
                removeButton.setEnabled(true);
                nextButton.setEnabled(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //ignore
    }

}