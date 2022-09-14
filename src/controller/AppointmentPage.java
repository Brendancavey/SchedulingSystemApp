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
    public LocalDateTime start; //used to calculate a start date time
    public LocalDateTime end; //used to calculate an end date time
    LocalDate endDate; //used to get a selected end date
    LocalTime endTime; //used to get a selected end time
    LocalDate startDate; // used to get selected start date
    LocalTime startTime; // used to get selected start time
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

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
    /** LOGICAL ERROR: Found logical error when making appointment times overlap one another with the same customer. Fixed this
     * issue by creating a control flow statement with multiple and/or statements to check for these conditions.*/
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            boolean conflictExists = false;
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
                        System.out.println(String.valueOf(aCustomer) + " = " + String.valueOf(customer.getId()));
                        System.out.println("CONFLICT WITH " + aCustomer);

                        conflictedTimeStart = aStart;
                        conflictedTimeEnd = aEnd;
                        conflictExists = true;
                        break;
                    }
                }
            }
            if(conflictExists){
                System.out.println(customer + " already has an appointment at " + Helper.toReadableTime((conflictedTimeStart.toLocalTime())) + " to " +
                        Helper.toReadableTime(conflictedTimeEnd.toLocalTime()) + ".\nCannot make an appointment at " + Helper.toReadableTime(startTime) + " to " + Helper.toReadableTime(endTime));
                System.out.println(conflictedTimeStart + " and " + conflictedTimeEnd + " is in conflict with " + startDateTime + " and " + endDateTime);
            }
            ////////////////////////////////////////////////////////////////////////
            ///////////////////ADDING/UPDATING APPOINTMENT////////////////////////////
            //if no time conflicts exist, then add/update appointment
            if (!conflictExists) {
                ///////////////CHECKING TO SEE IF SELECTED TIME FALLS INTO NEXT DAY//////////////////
                getDateTimeSelection(); //updating selected start/end date times
                end = LocalDateTime.of(endDate, endTime); //no need to convert since conversion to local time was already done on date selection
                start = LocalDateTime.of(startDate, startTime); //no need to convert since conversion local time was already done on date selection
                if (end.isBefore(start)) {
                    //if end values are selected where the calculated value is true, then it must be that the end time selected falls onto the next day.
                    //So add 1 day to end date time
                    endDateTime = endDateTime.plusDays(1); //add 1 to end date time
                    Helper.displayMessage("End time chosen falls into the next day. End date has been changed to reflect this.");
                }
                ///////////////////////////////////////////////////////////////////////////////////

                if (Helper.userClickedAddAppointment) { //if user clicked on add appointment from the main menu page, then insert appointment into data base
                    DBAppointments.insertAppointment(title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId());
                } else { //else the user must have clicked on modify appointment, therefore userClickedAddAppointment is false
                    int appointmentId = Integer.valueOf(apptIdText.getText());
                    DBAppointments.updateAppointment(appointmentId, title, description, location, type, startDateTime, endDateTime, customer.getId(), user.getUserId(), contact.getContactId());
                }

                //whatever the result of userClickedAddAppointment, set value back to false (default)
                Helper.userClickedAddAppointment = false;
                /////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////
                Helper.goToMainMenu(actionEvent);
            }

        } catch (Exception e) {
            Helper.displayMessage("Fill all fields fields");
        }
    }

        /////////////////////////////////////////////////////////////////////////
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
        endTimeComboBox.setDisable(false); //make end time combo box picker usable
        endTimeComboBox.setPromptText("Select End Time");
        endTimeComboBox.getItems().clear(); //clearing end time combo box selection if any
        endTimeComboBox.getItems().removeAll(); //removing all items from end time combo box to reflect updated start time
        updateEndTimes();

        System.out.println("end time dictionary: " + Helper.timeDictionaryEnd);

    }
    public void onEndTimeSelection(ActionEvent actionEvent) {


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
        disablePreviousEndDates(); //disabling previous end dates so that user cannot have end date that is previous from the start date
        startTimeComboBox.getItems().clear(); //clear selection
        startTimeComboBox.getItems().removeAll(); //removing all selections from start time combo box
        Helper.timeDictionaryStart.clear();
        Helper.timeDictionaryEnd.clear();
        updateStartTimes();

        //updateComboBoxSelection(); //updating combobox selection with new information from selected values
        if(!Helper.displayApptTimeConversionMssgOnce) { //display warning message once due to it being annoying
            Helper.displayMessage("Appointment times shown under appointment start and end times have been converted from EST to your time zone: " +
                    Helper.getTimeZone() + ". The establishment is open from 8AM - 10PM EST");
            Helper.displayApptTimeConversionMssgOnce = true;
        }
        if(startTimeComboBox.getItems().isEmpty()){
            Helper.displayMessage("The date you selected has no available appointment times. Select another date.");
        }
        System.out.println("start time dictionary: " + Helper.timeDictionaryStart);
    }
    public void onEndDateSelection(ActionEvent actionEvent) {
        //updateComboBoxSelection();
    }
    ////////////////////////////////////////////////////////////////
    /////////////////HELPER METHODS/////////////////////////////////
    public void updateStartTimes(){
        //start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
        //end = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
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
       LocalDateTime start = LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 0, 0);
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
    public void updateComboBoxSelection(){
        start = null;
        end = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
        LocalDateTime startTemp;
        LocalDateTime endTemp;



        ///////////////////////////////////////////////INITIALIZING START TIME COMBO BOX IF DATE IS TODAY///////////////////////////////////////////
        if(startDatePicker.getValue().equals(LocalDate.now())) {
            endTimeComboBox.getItems().clear(); //clear choice selection
            endTimeComboBox.getItems().removeAll(); //remove all end time choices
            //if the date for the appointment is the current date, then the selection times must be times after the current time, but before closing time
            if (LocalTime.now().getMinute() < 30) { //if the local time is before 30 minutes, then set the start time interval at 30 minutes
                start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), LocalTime.now().getHour(), 30)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            }else { //else if the local time is after 30 minutes, then set the start time interval to the next hour at 0 minutes.
                start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), LocalTime.now().getHour() + 1, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            }
            while (start.isBefore(end)) { //add all of the time intervals between start and end times
                String readableTime = Helper.toReadableTime(start.toLocalTime());
                System.out.println(readableTime);
                /*if (!startTimeComboBox.getItems().contains(readableTime)) {

                }*/
                Helper.timeDictionaryStart.put(readableTime, start);
                startTimeComboBox.getItems().add(readableTime); //add all if the time intervals between start and end into start time box
                start = start.plusMinutes(30);
            }
        } else { //else if the date selected is after the current date, then any time between 8AM and 10PM EST - the operating times of the establishment - can be selected
            start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
            String readableTime = Helper.toReadableTime(start.toLocalTime());

            while (start.isBefore(end)) { //add all of the time intervals between start and end times
                readableTime = Helper.toReadableTime(start.toLocalTime());

                Helper.timeDictionaryStart.put(readableTime, start);
                startTimeComboBox.getItems().add(readableTime); //add all if the time intervals between start and end into start time box
                start = start.plusMinutes(30);

            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////INITIALIZING END TIME COMBO BOX////////////////////////////////////////////////////////

        if(!startTimeComboBox.getSelectionModel().isEmpty()) {
            int startTimeFromComboBox = Helper.convertFromEstToLocal(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getHour(), 0)).toLocalDateTime().getHour();
            try {
                endTimeComboBox.getSelectionModel().selectFirst(); //select the added time. 30 min appointments are most appropriate, but for sake of project requirements, any time can be selected.

                if (endDatePicker.getValue().isAfter(startDatePicker.getValue())) {

                    start = Helper.convertToEst(LocalDateTime.of(endDatePicker.getValue().getYear(), endDatePicker.getValue().getMonth(), endDatePicker.getValue().getDayOfMonth(), 8, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
                    end = Helper.convertToEst(LocalDateTime.of(endDatePicker.getValue().getYear(), endDatePicker.getValue().getMonth(), endDatePicker.getValue().getDayOfMonth(), 22, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est

                    while (start.isBefore(end)) { //add all of the time intervals between start and end times
                        String readableTime = Helper.toReadableTime(start.toLocalTime());
                        if(!endTimeComboBox.getItems().contains(readableTime)) {
                            endTimeComboBox.getItems().add(readableTime);
                            Helper.timeDictionaryEnd.put(readableTime, start);
                            start = start.plusMinutes(30);
                        }
                    }
                }
                else{
                    if (LocalTime.of(Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getHour(), Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).getMinute()).getMinute() == 30) { //check to see if the start time selection minute is 30. If so, then set the start to 30 min after for 30 min appointment intervals.
                        start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), startTimeFromComboBox + 1, 0)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
                    } else {
                        //check to see if the start time selection minute is on the hour. If so, then set the start to 30 min after for 30 min appointment intervals
                        start = Helper.convertToEst(LocalDateTime.of(startDatePicker.getValue().getYear(), startDatePicker.getValue().getMonth(), startDatePicker.getValue().getDayOfMonth(), startTimeFromComboBox, 30)).toLocalDateTime(); //converting from local to est for establishment operating hour requirements in est
                    }
                    startTemp = start; //saving the value of start into temp value to use for end time combo box

                    while (start.isBefore(end)) {
                        String readableTime = Helper.toReadableTime(start.toLocalTime());
                        if (!startTimeComboBox.getItems().contains(readableTime)) {
                            readableTime = Helper.toReadableTime(start.toLocalTime());
                            Helper.timeDictionaryStart.put(readableTime, start);
                            startTimeComboBox.getItems().add(readableTime); //add all if the time intervals between start and end into start time box
                        }
                        start = start.plusMinutes(30);
                    }
                    while (startTemp.isBefore(end.plusSeconds(1))) { //add all of the time intervals between start and end into end time box
                        String readableTime = Helper.toReadableTime(startTemp.toLocalTime());
                        if(!endTimeComboBox.getItems().contains(readableTime)) {
                            readableTime = Helper.toReadableTime(startTemp.toLocalTime());
                            endTimeComboBox.getItems().add(readableTime);
                            Helper.timeDictionaryEnd.put(readableTime, startTemp);
                            startTemp = startTemp.plusMinutes(30);
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Do nothing because end time combobox selection was cleared");
            }
        }

        }
    public void getDateTimeSelection(){
        //helper method to reduce redundancy across multiple widgets and readability of code
        endDate = endDatePicker.getValue();
        //endTime = endTimeComboBox.getValue().toLocalTime();
        endTime = Helper.timeDictionaryEnd.get(endTimeComboBox.getValue()).toLocalTime();
        startDate = startDatePicker.getValue();
        //startTime = startTimeComboBox.getValue().toLocalTime();
        startTime = Helper.timeDictionaryStart.get(startTimeComboBox.getValue()).toLocalTime();
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
    public void disablePreviousEndDates(){
        //used to allow for more readable code
        //Source: stack overflow. Setting date picker so that previous dates from current day cannot be chosen for an appointment
        endDatePicker.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate selectedDate = startDatePicker.getValue();
                setDisable(empty || date.compareTo(selectedDate) < 0);
            }
        });
    }
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
