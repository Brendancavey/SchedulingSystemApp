package DAO;

import controller.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
                LocalDateTime startDateLdt = Helper.convertFromUtcToLocal(startDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
                Timestamp endDate = resultSet.getTimestamp("End"); //getting timestamp from database
                LocalDateTime endDateLdt = Helper.convertFromUtcToLocal(endDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
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
    public static ObservableList<Appointment> getAppointmentsByContactId(int contactId){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from appointments WHERE Contact_ID = ?"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startDate = resultSet.getTimestamp("Start"); //getting timestamp from database
                LocalDateTime startDateLdt = Helper.convertFromUtcToLocal(startDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
                Timestamp endDate = resultSet.getTimestamp("End"); //getting timestamp from database
                LocalDateTime endDateLdt = Helper.convertFromUtcToLocal(endDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");


                Contact contact = DBContacts.selectContactById(contactId); //getting contact from contact id
                Appointment newAppointment = new Appointment(appointmentId, title, description, location, type, startDateLdt, endDateLdt, customerId, userId, contact);
                appointmentList.add(newAppointment);

            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentList;
    }
    public static ObservableList<Appointment> getAppointmentsByType(String type){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from appointments WHERE Type = ?"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                //String type = resultSet.getString("Type");
                Timestamp startDate = resultSet.getTimestamp("Start"); //getting timestamp from database
                LocalDateTime startDateLdt = Helper.convertFromUtcToLocal(startDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
                Timestamp endDate = resultSet.getTimestamp("End"); //getting timestamp from database
                LocalDateTime endDateLdt = Helper.convertFromUtcToLocal(endDate.toLocalDateTime()).toLocalDateTime(); //converting timestamp to local datetime and converting from UTC to local date time to display in local date time
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

    public static void insertAppointment(String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate, int custId, int userId, int contactId){
        try{
            String sqlQuery = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(Helper.convertToUtc(startDate).toLocalDateTime())); //converting local date time to UTC for database storage requirements
            preparedStatement.setTimestamp(6, Timestamp.valueOf(Helper.convertToUtc(endDate).toLocalDateTime())); //converting local date time to UTC for database storage requirements
            preparedStatement.setTimestamp(7, Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting create date and converting to UTC for database storage requirements
            preparedStatement.setString(8, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName());
            preparedStatement.setTimestamp(9,  Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting last updated datetime and converting to UTC for database storage requirements
            preparedStatement.setString(10, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName()); //setting user who last updated this appointment
            preparedStatement.setInt(11, custId);
            preparedStatement.setInt(12, userId);
            preparedStatement.setInt(13, contactId);

            preparedStatement.executeUpdate();
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void updateAppointment(int apptId, String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate, int custId, int userId, int contactId){
        try{
            String sqlQuery = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type= ?, Start= ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(endDate));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); //setting create date
            preparedStatement.setString(8, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName());
            preparedStatement.setTimestamp(9,  Timestamp.valueOf(LocalDateTime.now())); //setting last updated datetime
            preparedStatement.setString(10, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName()); //setting user who last updated this appointment
            preparedStatement.setInt(11, custId);
            preparedStatement.setInt(12, userId);
            preparedStatement.setInt(13, contactId);
            preparedStatement.setInt(14, apptId);

            preparedStatement.executeUpdate();
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static int deleteAppointment(int apptId){
        int rowsAffected = 0;
        try {
            String sqlQuery = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, apptId);
            rowsAffected = preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return rowsAffected;

    }
    public static ObservableList<Appointment> selectAppointmentByThisMonth(LocalDateTime dateTime){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        Timestamp dateTimeStamp = Timestamp.valueOf(dateTime);
        try {
            String sqlQuery = "SELECT * FROM APPOINTMENTS WHERE START = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setTimestamp(1, dateTimeStamp);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
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

        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentList;
    }
}
