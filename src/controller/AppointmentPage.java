package controller;

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomers;
import DAO.DBUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable {

    public Label timeZoneText;
    public Label conflictExistsLabel;
    public TextField apptIdText;
    public TextField titleText;
    public TextField descText;
    public TextField locationText;
    public TextField typeText;
    public ComboBox<Contact> contactBox;
    public ComboBox<Customer> customerBox;
    public ComboBox<User> userBox;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ComboBox<String> startTimeComboBox;
    public ComboBox<String> endTimeComboBox;
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    public Button saveButton;
    public Label apptIdLabel;
    public Label titleLabel;
    public Label descriptionLabel;
    public Label locationLabel;
    public Label typeLabel;
    public Label contactLabel;
    public Label customerLabel;
    public Label userLabel;
    public Label startDateLabel;
    public Label startTimeLabel;
    public Label endDateLabel;
    public Label endTimeLabel;
    public Button cancelButton;

    /** This is the initialize method.
     * This method gets called when first starting this scene. It checks for the
     * locale default if it set to a supported language in the resource bundle, and changes
     * the appropriate labels and text fields to the supported language. It also checks on a
     * a global variable to see if the user selected to modify appointment, and if so make the appropriate
     * changes to the scene. Additionally, this method initializes all of combo boxes to contain the appropriate
     * data.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //////////////////CHECKING FOR FRENCH TRANSLATION/////////
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            apptIdLabel.setText(rb.getString("ApptId"));
            titleLabel.setText(rb.getString("Title"));
            descriptionLabel.setText(rb.getString("Description"));
            locationLabel.setText(rb.getString("Location"));
            typeLabel.setText(rb.getString("Type"));
            contactLabel.setText(rb.getString("Contact"));
            customerLabel.setText(rb.getString("Customer"));
            startDateLabel.setText(rb.getString("StartDate"));
            endDateLabel.setText(rb.getString("EndDate"));
            startTimeLabel.setText(rb.getString("StartTime"));
            endTimeLabel.setText(rb.getString("EndTime"));
            userLabel.setText(rb.getString("User"));
            saveButton.setText(rb.getString("Save"));
            cancelButton.setText(rb.getString("Cancel"));
        }
        ////////////////////////////////////////////////////////////
        if(Helper.userClickedModifyAppointment == true){ //if the user selected to modify appointment, make sure nothing is disabled
            startDatePicker.setDisable(false);
            startTimeComboBox.setDisable(false);
            endTimeComboBox.setDisable(false);

            //updateStartTimes(); these methods are called at main menu to get startDatePicker and endDatePicker value to update times
            //updateEndTimes();
        }
        //////SETTING THE USER COMBO BOX VALUE TO THE USER WHO LOGGED IN/////
        userBox.setValue(Helper.userWhoLoggedIn);
        //////////////////////////////////////////
        System.out.println("Appointment Page initialized!");
        timeZoneText.setText(Helper.getTimeZone()); //displaying timezone
        disablePreviousStartDates();//disabling all previous dates from current day
        ////////INITIALIZING COMBO BOXES/////////
        contactBox.setItems(DBContacts.getAllContacts());
        customerBox.setItems(DBCustomers.getAllCustomers());
        userBox.setItems(DBUsers.getAllUsers());
    }
    /** This is onCustomerSelection method.
     * This method is called whenever the user makes a selection on the customer combo box. It allows
     * for the start date picker to be usable and make appropriate changes to the prompt text when a customer
     * selection is made if applicable. The method also checks for a time conflict by calling checkForConflict(). Please see
     * checkForConflict() for more details.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onCustomerSelection(ActionEvent actionEvent) {
        startDatePicker.setDisable(false); //make start date selection usable
        startDatePicker.setPromptText("Select Start Date");
        checkForConflict(); //check for conflict with new customer selection
    }
    /** This is the onSave Method.
     * This method is called when the user presses the save button. This method takes all input from the text fields and
     * combo box selections and saves them into a variable. The method also displays fail safe messages to verify if the
     * user did not leave any fields empty/blank. The method then checks a global variable to see which toggle was selected
     * (add or modify), and makes changes to the database depending on the selection (insert or update the appointment).
     * LOGICAL ERROR: Found logical error when making appointment times overlap one another with the same customer. Fixed this
     * issue by creating a control flow statement with multiple and/or statements to check for these conditions. This
     * is found under checkForConflict() method.
     * RUNTIME ERROR: Found runtime error that when user saved without filling out all fields, a nullpointer expcetion
     * would appear. Fixed by wrapping in try catch block
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            allAppointments = DBAppointments.getAllAppointments();
            //////////////////////GETTING FIELD INPUTS//////////////////////////
            String title = titleText.getText();
            String description = descText.getText();
            String location = locationText.getText();
            String type = typeText.getText();
            Contact contact = contactBox.getValue();
            Customer customer = customerBox.getValue();
            User user = userBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            //using time dictionary to get local date time object stored in dictionary since start time combo box stores string values better readability
            LocalTime startTime = Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).toLocalTime();
            //using time dictionary to get local date time object stored in dictionary since end time combo box stores string values for better readability
            LocalTime endTime = Helper.timeDictionaryEnd.get(endTimeComboBox.getValue()).toLocalTime();
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime); //converting start date and start time into local date time for appointment object
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime); //converting end date and end time into local date time for appointment object
            ////////////////////////////////////////////////////////////////////
            /////////////////////CHECKING FOR LOGICAL ISSUES////////////////////
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            //Language l
            if(title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()){
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Helper.displayMessage(rb.getString("MakeSureToEnterAValidName"));
                }
                else{
                    Helper.displayMessage("Make sure no fields are left empty.");
                }
            }
            else if(startDate.isBefore(LocalDate.now())){
                if(Locale.getDefault().getLanguage().equals("fr")) {
                    Helper.displayMessage(rb.getString("DateIsNoLongerValid"));
                }
                else{
                    Helper.displayMessage("Chosen start date is no longer valid. Today is " + LocalDate.now() + ". Please select a different start date.");
                }
                startDatePicker.getEditor().clear(); //clearing selection
            }
            else {
                ///////////////////ADDING/UPDATING APPOINTMENT////////////////////////////
                if (Helper.userClickedAddAppointment) { //if user clicked on add appointment from the main menu page, then insert appointment into data base
                    DBAppointments.insertAppointment(title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId());
                } else { //else the user must have clicked on modify appointment, therefore userClickedAddAppointment is false
                    int appointmentId = Integer.valueOf(apptIdText.getText());
                    DBAppointments.updateAppointment(appointmentId, title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId()); //modify the appointment
                }
                //whatever the result of userClickedAddAppointment, set value back to false (default)
                Helper.userClickedAddAppointment = false;
                Helper.userClickedModifyAppointment = false;
                Helper.goToMainMenu(actionEvent);
                ////////////////////////////////////////////////////////////////////////////
            }
        } catch (Exception e) {
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Helper.displayMessage(rb.getString("FillAllFieldsWithValidInformation"));
            }
            else{
                Helper.displayMessage("Fill all fields with valid information.");
            }
        }
    }
    /** This is the onCancel method.
     * This method changes the appropriate global variables back to default,
     * and takes the user back to the main menu.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.userClickedAddAppointment = false;
        Helper.userClickedModifyAppointment = false;
        Helper.goToMainMenu(actionEvent);
    }
    /** This is the onStartTimeSelection method.
     * This method is called when the user selects a start time from the start time combo box. This method makes changes
     * to the appropriate widgets/labels and then adds all of the appropriate end times to the end time combo box by
     * calling updateEndTimes() method.
     * LOGICAL ERROR: When selecting a start time, the user could still select an end time that occurs previous
     * to the start time. To correct this, I verified that all times added to the end time box occurs 30 mins after
     * the selected start time. This is found under updateEndTimes() method.
     * RUNTIME ERROR: When selecting start time, the end time needed to be within 30 min intervals after the start time and
     * not from the current time. Using current time would exceed the 60 min hour window and cause a runtime error. To correct
     * this, I created a control flow statement to check if the selected time is at 30 mins or on the hour. Found within updateEndTimes() method.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding widget.
     * */
    public void onStartTimeSelection(ActionEvent actionEvent) {
        try {
            conflictExistsLabel.setOpacity(0);
            saveButton.setDisable(false);
            endTimeComboBox.setDisable(false); //make end time combo box picker usable
            endTimeComboBox.setPromptText("Select End Time");
            endTimeComboBox.getItems().clear(); //clearing end time combo box selection if any
            endTimeComboBox.getItems().removeAll(); //removing all items from end time combo box to reflect updated start time
            updateEndTimes(); //update end times to reflect start times without logical errors
            System.out.println("end time dictionary: " + Helper.timeDictionaryEnd);
        }catch(NullPointerException e){
            System.out.println("Exception...");
        }
    }
    /** This is the onEndTimesElection.
     * This method verifies that user selected an end time and a start time that does not conflict
     * with the establishment operating hours or a current existing appointment of the same customer. This
     * is found under checkForConflict method().
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding widget.*/
    public void onEndTimeSelection(ActionEvent actionEvent) {
        checkForConflict();
    }
    /** This is the onStartDateSelection.
     * This method gets called when the user selects a start date from the start date calendar widget. This method makes
     * changes to the appropriate widgets, and updates the start times within the start time combo box by calling the
     * updateStartTimes() method. A message is also displayed once indicating to the user that the times selected are in
     * their time zone, and they must make a selection that corresponds to EST time zone due to establishment hours.
     * LOGICAL ERROR: When initializing the start time options, start time could equal the end time. Since you
     * can't make an appointment at closing of an establishment, I corrected this by making a condition where
     * the start time increments get added if the start time does not equal the end time, within 30 min intervals.
     * Furthermore, if the user selected the current day for an appointment, then start times could be chosen that were previous
     * to the current local time. To correct this, I disabled the end date selection, and auto picked the same date for the end date as the start date.
     * Additionally, the user could select a start date that occurs in the past. To fix this, I included a method that disables
     * all previous dates from the current date when this scene is initialized.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onStartDateSelection(ActionEvent actionEvent) {
        try {
            startTimeComboBox.setDisable(false); //make start time combo box selection usable
            startTimeComboBox.setPromptText("Select Start Time");
            endDatePicker.setValue(startDatePicker.getValue()); // setting end date to be the same as the start date since day long appointments are most likely unacceptable.
            startTimeComboBox.getItems().clear(); //clear selection
            startTimeComboBox.getItems().removeAll(); //removing all selections from start time combo box
            Helper.timeDictionaryStart.clear(); //clearing start time dictionary to reflect new date chosen
            Helper.timeDictionaryEnd.clear(); //clearing start time dictionary to reflect new date chosen
            updateStartTimes(); //update start time selections

            if (!Helper.displayApptTimeConversionMssgOnce) { //display info message once due to it being annoying
                LocalDateTime estOpeningTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
                LocalDateTime estClosingTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime();
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Helper.displayMessage(rb.getString("AppointmentTimesDisplayedInYourTimeZone.PleasePickBetween") + "\n" + Helper.toReadableTime(estOpeningTime.toLocalTime()) +
                            " - " + Helper.toReadableTime(estClosingTime.toLocalTime()) + " " + rb.getString("ForEstablishmentHours"));
                }
                else {
                    Helper.displayMessage("Appointment times shown under appointment start and end times are displayed in your time zone: " +
                            Helper.getTimeZone() + ". The establishment is open from 8AM - 10PM EST.\n" +
                            "Your time zone converted to EST establishment hours are: \n" +
                            Helper.toReadableTime(estOpeningTime.toLocalTime()) + " to " + Helper.toReadableTime(estClosingTime.toLocalTime()) +
                            "\nPlease select options that are between these times.");
                }
                Helper.displayApptTimeConversionMssgOnce = true;
            }
            if (startTimeComboBox.getItems().isEmpty()) {
                Helper.displayMessage("The date you selected has no available appointment times. Select another date.");
            }
            System.out.println("start time dictionary: " + Helper.timeDictionaryStart);
        }catch(NullPointerException e){
            System.out.println("Exception..");
        }
    }
    public void onEndDateSelection(ActionEvent actionEvent) {
        //disabled and auto pick based on start date selection
    }
    ////////////////////////////////////////////////////////////////
    /////////////////HELPER METHODS/////////////////////////////////
    /** This is the updateStartTimes method.
     * This is a helper method to reduce redundancy across multiple methods.
     * This method gets all hours of the day and calls the toReadableTime() method located
     * within Helper.java to convert the times into a string format that is easier to read for the user. This
     * method then places the string into a hashmap that maps the string value as the key, and the LocalDateTime Object
     * corresponding to that string as the value. This hashmap used primarily to make dateTime comparisons within checkForConflict() method.
     * The method adds all of the readableTime Strings into the start time combo box.*/
    public void updateStartTimes(){
        LocalDateTime start = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 0, 0);
        LocalDateTime end = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 23, 30);

        while(start.isBefore(end)){
            String readableTime = Helper.toReadableTime(start.toLocalTime());
            Helper.timeDictionaryStart.put(readableTime, start);
            startTimeComboBox.getItems().add(readableTime);
            start = start.plusMinutes(30);
        }
    }
    /** This is the updateEndTimes method.
     * This is a helper method to reduce redundancy across multiple methods.
     * This method checks to see which start time the user selected, and adds only times that are after the selected start time. This method performs the
     * same actions as the updateStartTimes() method in terms of converting times into a readable format, and storing them into a hashmap for timeConflict use.*/
    public void updateEndTimes(){
       LocalDateTime start;
       LocalDateTime end = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 23, 30);
       //using start time dictionary to find local date time of the the selected string from the start time combo box.
        int startTimeFromComboBox = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getHour(), 0).getHour();

        if (LocalTime.of(Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getHour(), Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getMinute()).getMinute() == 30) { //check to see if the start time selection minute is 30. If so, then set the start to 30 min after for 30 min appointment intervals.
            start = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), startTimeFromComboBox + 1, 0);
        } else {
            //check to see if the start time selection minute is on the hour. If so, then set the start to 30 min after for 30 min appointment intervals
            start = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), startTimeFromComboBox, 30);
        }
        while (start.isBefore(end.plusSeconds(1))) {
            String readableTime = Helper.toReadableTime((start.toLocalTime()));
            Helper.timeDictionaryEnd.put(readableTime, start);
            endTimeComboBox.getItems().add(readableTime);
            start = start.plusMinutes(30);
        }
    }
    /** This is the checkForConflict method.
     * This is a helper method to reduce redundancy across multiple methods. This method checks for a time conflict for if the
     * user selected start and end times that occurred outside of the establishment hours (8AM-10PM EST) or the user selected start and end times
     * that overlap with an existing appointment with the same customer. If a conflict exists, then a message is displayed indicating to the user that
     * the appointment cannot be made/modified to the selected time/date. The save button gets disabled until an appropriate change has been made to the selected start/end times.
     * LOGICAL ERROR: WHen attempting to modify an appointment, a conflict would occur when changing start and end times to be around the same time as the modifying times.
     * To correct this, I checked for the condition of when looping through all of the appointments to skip the appointment of the modifying appointment.*/
    public void checkForConflict(){
        try {
            saveButton.setDisable(false);
            conflictExistsLabel.setOpacity(0);
            boolean conflictExists = false;
            boolean estConflict = false;
            allAppointments = DBAppointments.getAllAppointments(); //getting all appointments
            LocalDateTime estOpeningTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            LocalDateTime estClosingTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            Customer customer = customerBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            //using time dictionary to get local date time object stored in dictionary since start time combo box stores string values for better readability
            LocalTime startTime = Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).toLocalTime();
            //using time dictionary to get local date time object stored in dictionary since end time combo box stores string values for better readability
            LocalTime endTime = Helper.timeDictionaryEnd.get(endTimeComboBox.getValue()).toLocalTime();
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime); //converting start date and start time into local date time for appointment object
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime); //converting end date and end time into local date time for appointment object

            /////////////////CHECKING FOR TIME OVERLAP CONFLICT//////////////////
            LocalDateTime conflictedTimeStart = null;
            LocalDateTime conflictedTimeEnd = null;
            for (Appointment a : allAppointments) {
                if(Helper.userClickedModifyAppointment && a.getApptId() == MainMenu.getSelectedAppointment().getApptId()){
                    continue; //skip this iteration of for loop if the user clicked on modify and the selected appointment to modify matches the current appointment loop.
                    //this is so that a conflict does not arise when selecting times around the selected appointment
                }
                LocalDateTime aStart = a.getStartDate();
                LocalDateTime aEnd = a.getEndDate();
                int aCustomer = a.getCustId();
                if (aCustomer == customer.getId()) {                                                                             //found customer that is a match
                    if ((startDateTime.isEqual(aStart)) ||                                                                       //start times cannot be the same
                            (startDateTime.isAfter(aStart) && startDateTime.isBefore(aEnd)) ||                                  //start cannot start between start and end time
                            (startDateTime.isBefore(aStart)) && (endDateTime.isAfter(aStart) && endDateTime.isBefore(aEnd)) ||  //end cannot end between start and end time
                            (startDateTime.isBefore(aStart)) && (endDateTime.isAfter(aEnd)) ||                                 //start cannot be before start AND have end be after end
                            (startDateTime.isBefore(aStart)) && (endDateTime.isEqual(aEnd))                                    //start cannot be before start AND have end be equal to end
                    ) {
                        conflictedTimeStart = aStart;
                        conflictedTimeEnd = aEnd;
                        conflictExists = true;
                    }
                }
            }
            if(conflictExists) {
                saveButton.setDisable(true);
                conflictExistsLabel.setOpacity(1);
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    conflictExistsLabel.setText(rb.getString("AppointmentOverlap"));
                } else {
                    conflictExistsLabel.setText(customer + " already has an appointment at " + Helper.toReadableTime((conflictedTimeStart.toLocalTime())) + " to " +
                            Helper.toReadableTime(conflictedTimeEnd.toLocalTime()) + ".\nCannot make an appointment at " + Helper.toReadableTime(startTime) + " to " + Helper.toReadableTime(endTime));
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////CHECKING FOR OPERATING HOURS IN EST TIME CONFLICT///////////////////////////////////////////////////////////
            if (startDateTime.isBefore(estOpeningTime) || endDateTime.isAfter(estClosingTime)) {
                estConflict = true;
            }
            if (estConflict) {
                saveButton.setDisable(true);
                conflictExistsLabel.setOpacity(1);
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    conflictExistsLabel.setText(rb.getString("OutsideEstablishmentHours"));
                } else {
                    conflictExistsLabel.setText("Times chosen is outside of establishment operating hours. Operating hours of establishment are: " +
                            Helper.toReadableTime(estOpeningTime.toLocalTime()) + " to " + Helper.toReadableTime(estClosingTime.toLocalTime()) + " " + Helper.getTimeZone() + " or \n" +
                            "8AM TO 10PM EST");
                }
            }
        }catch(NullPointerException e){
            System.out.println("Exception.");
        }
    }
    /** This is the disablePreviousStartDates method.
     * This is a method to increase readability to the code so that programmers will be able to understand what this section of code does.
     * This method disables all previous dates from the current date within the start date calendar widget. This is to fix a logical error
     * where the user could make an appointment in the past. */
    public void disablePreviousStartDates() {
        //used to allow for more readable code
        //Source: stack overflow. Setting date picker so that previous dates from current day cannot be chosen for an appointment
        startDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate currentDate = LocalDate.now();
                setDisable(empty || date.compareTo(currentDate) < 0);
            }
        });
    }
    /*public void disablePreviousEndDates(){
        //used to allow for more readable code
        //Source: stack overflow. Setting date picker so that previous dates from current day cannot be chosen for an appointment
        endDatePicker.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate selectedDate = startDatePicker.getValue();
                setDisable(empty || date.compareTo(selectedDate) < 0);
            }
        });
    }*/
    /** This is the sendAppointmentInformation method.
     * This method is primarily used within the main menu page and is called when the user clicks on
     * modify appointment. This method sends the selected appointment to appointment page scene
     * and sets the appropriate text fields and combo box selections to the corresponding values of the selected
     * appointment. */
    public void sendAppointmentInformation(Appointment appointment){
        apptIdText.setText(String.valueOf(appointment.getApptId()));
        titleText.setText(appointment.getTitle());
        descText.setText(appointment.getDescription());
        locationText.setText(appointment.getLocation());
        typeText.setText(appointment.getType());
        contactBox.setValue(appointment.getContact());
        customerBox.setValue(appointment.getCustomer());
        //userBox.setValue(appointment.getUser()); this is determined upon who logged in
        startDatePicker.setValue(appointment.getStartDate().toLocalDate());
        endDatePicker.setValue(appointment.getEndDate().toLocalDate());
        startTimeComboBox.setValue(Helper.toReadableTime(appointment.getStartDate().toLocalTime()));
        endTimeComboBox.setValue(Helper.toReadableTime(appointment.getEndDate().toLocalTime()));
    }
    //////////////////////////////////////////////////////////////
}
