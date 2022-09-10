package controller;

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomers;
import DAO.DBUsers;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public ComboBox<LocalTime> startTimeComboBox;
    public ComboBox<LocalTime> endTimeComboBox;
    public LocalTime start;
    public LocalTime end = LocalTime.of(20, 0); //requirements for when office is closed is at 10PM EST so no more appointments after that.



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment Page initialized!");


        contactBox.setItems(DBContacts.getAllContacts());
        customerBox.setItems(DBCustomers.getAllCustomers());
        userBox.setItems(DBUsers.getAllUsers());
    }
    public void onSave(ActionEvent actionEvent) throws IOException {
        String title = titleText.getText();
        String description = descText.getText();
        String location = locationText.getText();
        String type = typeText.getText();
        Contact contact = contactBox.getValue();
        Customer customer = customerBox.getValue();
        User user = userBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime startTime =startTimeComboBox.getValue();
        LocalTime endTime = endTimeComboBox.getValue();
        LocalDateTime start = LocalDateTime.of(startDate, startTime); //converting start date and start time into local date time for appointment object
        LocalDateTime end = LocalDateTime.of(endDate, endTime); //converting end date and end time into local date time for appointment object

        if (Helper.userClickedAddAppointment){ //if user clicked on add appointment from the main menu page, then insert appointment into data base
            DBAppointments.insertAppointment(title, description, location, type, start, end, customer.getId(), user.getUserId(), contact.getContactId());
        }
        else{ //else the user must have clicked on modify appointment, therefore userClickedAddAppointment is false

        }
        //whatever the result of userClickedAddAppointment, set value back to false (default)
        Helper.userClickedAddAppointment = false;
        Helper.goToMainMenu(actionEvent);

    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }
    /** LOGICAL ERROR: When selecting a start time, the user could still select an end time that occurs previous
     * to the start time. To correct this, I verified that the end time must be 30 mins after the start time by
     * only adding that option. I solidified this condition by disabling the end time drop down menu so that the user
     * cannot change it.*/
    public void onStartTimeSelection(ActionEvent actionEvent) {
        try {
            //appointments are 30 minute intervals. End time is chosen automatically after start time box is chosen.
            endTimeComboBox.getItems().clear(); //clear choice selection
            endTimeComboBox.getItems().removeAll(); //remove all end time choices
            endTimeComboBox.getItems().add(startTimeComboBox.getSelectionModel().getSelectedItem().plusMinutes(30)); //add an option for end time 30 minutes after start time
            endTimeComboBox.getSelectionModel().selectFirst(); //select the added time
        }catch(NullPointerException e ){
            System.out.println("Do nothing because end time combobox selection was cleared");
        }
    }
    /** LOGICAL ERROR: When initializing the start time options, start time could equal the end time. Since you
     * can't make an appointment at closing of an establishment, I corrected this by making a condition where
     * the start time increments get added if the start time does not equal the end time, within 30 min intervals.
     * Additionally, start times on dates that haven't occurred should still be able to pick any time between opening
     * and closing of the establishment. To correct this, I created another control flow statement to check for this condition.*/
    public void onStartDateSelection(ActionEvent actionEvent) {
        try {
            endDatePicker.setValue(startDatePicker.getValue()); // setting end date to be the same as the start date since day long appointments are most likely unacceptable
            startTimeComboBox.getItems().clear(); //clear selection within start time box every time user selects new date
            startTimeComboBox.getItems().removeAll(); //remove all selections within start time box every time user selects on new date
            if (startDatePicker.getValue().equals(LocalDate.now())) {
                //if the date for the appointment is the current date, then the selection times must be times after the current time, but before closing time
                if (LocalTime.now().getMinute() < 30) { //if the local time is before 30 minutes, then set the start time interval at 30 minutes
                    start = LocalTime.of(LocalTime.now().getHour(), 30);
                } else { //else if the local time is after 30 minutes, then set the start time interval to the next hour at 0 minutes.
                    start = LocalTime.of((LocalTime.now().getHour()) + 1, 0);

                }

                while (start.isBefore(end)) {
                    startTimeComboBox.getItems().add(start);
                    start = start.plusMinutes(30);
                }
            } else { //else if the date selected is after the current date, then any time between 8AM and 10PM - the operating times of the establishment - can be selected
                start = LocalTime.of(8, 0);
                while (start.isBefore(end)) {
                    startTimeComboBox.getItems().add(start);
                    start = start.plusMinutes(30);
                }
            }
        }catch(NullPointerException e){
            System.out.println("Do nothing because the start time combo box selection was cleared.");
        }
    }
}
