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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment Page initialized!");
        if(LocalTime.now().getMinute() < 30){ //if the local time is before 30 minutes, then set the start time interval at 30 minutes
            start = LocalTime.of(LocalTime.now().getHour(), 30);
        }
        else{ //else if the local time is after 30 minutes, then set the start time interval to the next hour at 0 minutes.
            start = LocalTime.of((LocalTime.now().getHour()) + 1, 0);

        }
        LocalTime end = LocalTime.of(20, 0); //requirements for when office is closed is at 10PM EST so no more appointments after that.

        while(start.isBefore(end)){
            startTimeComboBox.getItems().add(start);
            start = start.plusMinutes(30);
        }
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

    public void onStartTimeSelection(ActionEvent actionEvent) {
        //appointments are 30 minute intervals. End time is chosen automatically after start time box is chosen.
        endTimeComboBox.getItems().clear(); //clear choice selection
        endTimeComboBox.getItems().removeAll(); //remove all end time choices
        endTimeComboBox.getItems().add(startTimeComboBox.getSelectionModel().getSelectedItem().plusMinutes(30)); //add an option for end time 30 minutes after start time
        endTimeComboBox.getSelectionModel().selectFirst(); //select the added time
    }

    public void onStartDateSelection(ActionEvent actionEvent) {
        endDatePicker.setValue(startDatePicker.getValue());
    }
}
