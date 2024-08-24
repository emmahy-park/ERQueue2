# Emergency Room Queue Management

ERQueue is a Java desktop application with a graphical user interface designed to triage patients in the Emergency Room. 
In Metro Vancouver, unexpectedly long wait times in the Emergency Room are common, 
and patients often have no clear idea of their expected wait time or their place in the queue. 
To streamline the prioritization process and keep patients informed, 
this application generates a prioritized queue based on patient information. 
The queue ranks patients according to their age, level of severity, and time of arrival.
Patients/guardians can view their place in the queue on the GUI panel, 
while the triage nurse can use the menu and buttons to perform the following actions:

* View the application description
* View saved patient information upon correct password entry
* Exit the application
* Load a saved queue
* Save the current queue
* Clear the current queue
* Add a new patient to the queue
* Remove a selected patient
* View the next patient in the queue

## Application Feature
### View Description
A brief description of the application as shown below.

![alt text](/src/main/ui/images/description.png)
### View Saved Patient Information
A password is required to view the detailed patients' information.

![alt text](/src/main/ui/images/password.png)

Upon correct password entry, the patients' name, age, severity level, and admitted time are shown.

![alt text](/src/main/ui/images/patientInfo.png)
### Exit
Exits the application.
### Load/Save/Clear Queue
In the Menu option, there are three functions (load/save/clear).

Load Saved Queue: Loads the saved patient queue
Save Current Queue: Saves the current queue
Clear Current Queue: Clears the current queue

![alt text](/src/main/ui/images/menu.png)
### Add Patient
The Add Patient button allows the triage nurse to add a new patient to the queue.
Patient's name, age, level of severity, and admitted time should be entered to enable the add button.

![alt text](/src/main/ui/images/addPatient.png)
### Remove Patient
This allows the triage nurse to update the current queue when the patient sees the doctor.
### Next Patient
This allows to view the first patient in queue and the total number of patients in the queue.
If there is no patient in the queue, it shows 'There is no patient in queue' as shown in the second figure below.

![alt text](/src/main/ui/images/nextPatient.png)
![alt text](/src/main/ui/images/noPatient.png)

