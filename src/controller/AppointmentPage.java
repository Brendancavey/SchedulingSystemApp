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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment Page initialized!");
        timeZoneText.setText(Helper.getTimeZone()); //displaying timezone
        disablePreviousStartDates();//disabling all previous dates from current day
        ////////INITIALIZING COMBO BOXES/////////
        contactBox.setItems(DBContacts.getAllContacts());
        customerBox.setItems(DBCustomers.getAllCustomers());
        userBox.setItems(DBUsers.getAllUsers());
    }
    public void onCustomerSelection(ActionEvent actionEvent) {
        startDatePicker.setDisable(false); //make start date selection usable
        startDatePicker.setPromptText("Select Start Date");
        checkForConflict();
    }
    /** LOGICAL ERROR: Found logical error when making appointment times overlap one another with the same customer. Fixed this
     * issue by creating a control flow statement with multiple and/or statements to check for these conditions. This
     * is found under onSelectEndTime.
     * RUNTIME ERROR: Found runtime error that when user saved without filling out all fields, a nullpointer expcetion
     * would appear. Fixed by wrapping in try catch block*/
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
            ///////////////////ADDING/UPDATING APPOINTMENT////////////////////////////
            if (Helper.userClickedAddAppointment) { //if user clicked on add appointment from the main menu page, then insert appointment into data base
                DBAppointments.insertAppointment(title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId());
            } else { //else the user must have clicked on modify appointment, therefore userClickedAddAppointment is false
                int appointmentId = Integer.valueOf(apptIdText.getText());
                DBAppointments.updateAppointment(appointmentId, title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId());
            }
            //whatever the result of userClickedAddAppointment, set value back to false (default)
            Helper.userClickedAddAppointment = false;
            Helper.goToMainMenu(actionEvent);
            ////////////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            Helper.displayMessage("Fill all fields with valid information.");
        }
    }
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.userClickedAddAppointment = false;
        Helper.goToMainMenu(actionEvent);
    }
    /** LOGICAL ERROR: When selecting a start time, the user could still select an end time that occurs previous
     * to the start time. To correct this, I verified that all times added to the end time box occurs 30 mins after
     * the selected start time.
     * RUNTIME ERROR: When selecting start time, the end time needed to be within 30 min intervals after the start time and
     * not from the current time. Using current time would exceed the 60 min hour window and cause a runtime error. To correct
     * this, I created a control flow statement to check if the selected time is at 30 mins or on the hour.*/
    public void onStartTimeSelection(ActionEvent actionEvent) {
        //checkForConflict();
        conflictExistsLabel.setOpacity(0);
        saveButton.setDisable(false);
        endTimeComboBox.setDisable(false); //make end time combo box picker usable
        endTimeComboBox.setPromptText("Select End Time");
        endTimeComboBox.getItems().clear(); //clearing end time combo box selection if any
        endTimeComboBox.getItems().removeAll(); //removing all items from end time combo box to reflect updated start time
        updateEndTimes(); //update end times to reflect start times without logical errors
        System.out.println("end time dictionary: " + Helper.timeDictionaryEnd);
    }
    public void onEndTimeSelection(ActionEvent actionEvent) {
        checkForConflict();
    }
    /** LOGICAL ERROR: When initializing the start time options, start time could equal the end time. Since you
     * can't make an appointment at closing of an establishment, I corrected this by making a condition where
     * the start time increments get added if the start time does not equal the end time, within 30 min intervals.
     * Furthermore, if the user selected the current day for an appointment, then start times could be chosen that were previous
     * to the current local time. To correct this, I placed control flow statements for this condition.
     * Additionally, start times on dates that haven't occurred should still be able to be picked at any time between opening
     * and closing of the establishment. To correct this, I created another control flow statement to check for this condition.
     * Also, the user could select end dates that were previous from the start date. To correct this, I disabled previous dates
     * from the selected start date.
     * */
    public void onStartDateSelection(ActionEvent actionEvent) {
        startTimeComboBox.setDisable(false); //make start time combo box selection usable
        startTimeComboBox.setPromptText("Select Start Time");
        endDatePicker.setValue(startDatePicker.getValue()); // setting end date to be the same as the start date since day long appointments are most likely unacceptable.
        startTimeComboBox.getItems().clear(); //clear selection
        startTimeComboBox.getItems().removeAll(); //removing all selections from start time combo box
        Helper.timeDictionaryStart.clear(); //clearing start time dictionary to reflect new date chosen
        Helper.timeDictionaryEnd.clear(); //clearing start time dictionary to reflect new date chosen
        updateStartTimes(); //update start time selections

        if(!Helper.displayApptTimeConversionMssgOnce) { //display info message once due to it being annoying
            LocalDateTime estOpeningTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            LocalDateTime estClosingTime = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime();
            Helper.displayMessage("Appointment times shown under appointment start and end times are displayed in your time zone: " +
                    Helper.getTimeZone() + ". The establishment is open from 8AM - 10PM EST.\n" +
                    "Your time zone converted to EST establishment hours are: \n" +
                    Helper.toReadableTime(estOpeningTime.toLocalTime()) + " to " + Helper.toReadableTime(estClosingTime.toLocalTime()) +
                    "\nPlease select options that are between these times.");
            Helper.displayApptTimeConversionMssgOnce = true;
        }
        if(startTimeComboBox.getItems().isEmpty()){
            Helper.displayMessage("The date you selected has no available appointment times. Select another date.");
        }
        System.out.println("start time dictionary: " + Helper.timeDictionaryStart);
    }
    public void onEndDateSelection(ActionEvent actionEvent) {
        //disabled and auto pick based on start date selection
    }
    ////////////////////////////////////////////////////////////////
    /////////////////HELPER METHODS/////////////////////////////////
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
    public void checkForConflict(){
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
        //using time dictionary to get local date time object stored in dictionary since start time combo box stores string values better readability
        LocalTime startTime = Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).toLocalTime();
        //using time dictionary to get local date time object stored in dictionary since end time combo box stores string values for better readability
        LocalTime endTime = Helper.timeDictionaryEnd.get(endTimeComboBox.getValue()).toLocalTime();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime); //converting start date and start time into local date time for appointment object
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime); //converting end date and end time into local date time for appointment object

        /////////////////CHECKING FOR TIME OVERLAP CONFLICT//////////////////
        LocalDateTime conflictedTimeStart = null;
        LocalDateTime conflictedTimeEnd = null;
        for (Appointment a : allAppointments) {
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
        if(conflictExists){
            saveButton.setDisable(true);
            conflictExistsLabel.setOpacity(1);
            conflictExistsLabel.setText(customer + " already has an appointment at " + Helper.toReadableTime((conflictedTimeStart.toLocalTime())) + " to " +
                    Helper.toReadableTime(conflictedTimeEnd.toLocalTime()) + ".\nCannot make an appointment at " + Helper.toReadableTime(startTime) + " to " + Helper.toReadableTime(endTime));
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////CHECKING FOR OPERATING HOURS IN EST TIME CONFLICT///////////////////////////////////////////////////////////
        if(startDateTime.isBefore(estOpeningTime) || endDateTime.isAfter(estClosingTime)){
            estConflict = true;
        }
        if(estConflict){
            saveButton.setDisable(true);
            conflictExistsLabel.setOpacity(1);
            conflictExistsLabel.setText("Times chosen is outside of establishment operating hours. Operating hours of establishment are: \n" +
                    Helper.toReadableTime(estOpeningTime.toLocalTime()) + " to " + Helper.toReadableTime(estClosingTime.toLocalTime()) + " " + Helper.getTimeZone() + " or \n" +
                    "8AM TO 10PM EST");
        }
    }
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
    public void sendAppointmentInformation(Appointment appointment){
        apptIdText.setText(String.valueOf(appointment.getApptId()));
        titleText.setText(appointment.getTitle());
        descText.setText(appointment.getDescription());
        locationText.setText(appointment.getLocation());
        typeText.setText(appointment.getType());
        contactBox.setValue(appointment.getContact());
        customerBox.setValue(appointment.getCustomer());
        userBox.setValue(appointment.getUser());
        startDatePicker.setValue(appointment.getStartDate().toLocalDate());
        endDatePicker.setValue(appointment.getEndDate().toLocalDate());
        startTimeComboBox.setValue(Helper.toReadableTime(appointment.getStartDate().toLocalTime()));
        endTimeComboBox.setValue(Helper.toReadableTime(appointment.getEndDate().toLocalTime()));
    }


    //////////////////////////////////////////////////////////////
}
