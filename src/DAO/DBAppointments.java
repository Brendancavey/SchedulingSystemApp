package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.Province;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DBAppointments {
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from appointments"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startDate = resultSet.getTimestamp("Start"); //getting timestamp from database
                LocalDateTime startDateLdt = startDate.toLocalDateTime(); //converting timestamp to local datetime
                Timestamp endDate = resultSet.getTimestamp("End"); //getting timestamp from database
                LocalDateTime endDateLdt = endDate.toLocalDateTime(); //converting timestamp to local datetime
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");


                Contact contact = DBContacts.selectContactById(contactId); //getting contact from contact id
                Appointment newAppointment = new Appointment(appointmentId, title, description, location, type, startDateLdt, endDateLdt, customerId, userId, contact);
                appointmentList.add(newAppointment);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentList;
    }
}
