/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
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
    /** This is the getAllAppointments method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method creates appointment objects from the database and adds it into a list to be returned.
     * @return appointmentList Returns a list of all appointments from the database.*/
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
    /** This is the getAllAppointmentsByConactId method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to only select Contact_ID that matches the parameter.
     * This method creates appointment objects from the database and adds it into a list to be returned.
     * This method is primarily used when a list of appointments that correspond to a particular contact Id is needed (in the reports page view by contact schedule)
     * @return appointmentList Returns a list of appointments from the database that matches the sql condition.*/
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
    /** This is the getAllAppointmentsByType method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to only select Type that matches the parameter.
     * This method creates appointment objects from the database and adds it into a list to be returned.
     * This method is primarily used when a list of appointments that correspond to a particular string Type is needed (in the reports page view by Type)
     * @return appointmentList Returns a list of appointments from the database that matches the sql condition.*/
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
    /** This is the insertAppointment method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes in appointment parameters that correspond to the data base columns of appointment, and inserts
     * the values to the appropriate columns of the database. This method is primarily used in the appointment page
     * when the user wants to add an appointment to the database. */
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
    /** This is the updateAppointment method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes in appointment parameters that correspond to the data base columns of appointment, and updates
     * the values to the appropriate columns of the database where the appointment id parameter matches the appointment id column.
     * This method is primarily used in the appointment page when the user wants to modify an appointment in the database. */
    public static void updateAppointment(int apptId, String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate, int custId, int userId, int contactId){
        try{
            String sqlQuery = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type= ?, Start= ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(Helper.convertToUtc(startDate).toLocalDateTime())); //converting local date time to UTC for database storage requirements
            preparedStatement.setTimestamp(6, Timestamp.valueOf(Helper.convertToUtc(endDate).toLocalDateTime())); //converting local date time to UTC for database storage requirements
            //preparedStatement.setTimestamp(7, Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting create date and converting to UTC for database storage requirements
            //preparedStatement.setString(7, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName());
            preparedStatement.setTimestamp(7,  Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting last updated datetime and converting to UTC for database storage requirements
            preparedStatement.setString(8, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName()); //setting user who last updated this appointment
            preparedStatement.setInt(9, custId);
            preparedStatement.setInt(10, userId);
            preparedStatement.setInt(11, contactId);
            preparedStatement.setInt(12, apptId);

            preparedStatement.executeUpdate();
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
    /** This is the deleteAppointment method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes the parameter appointment id and uses it within the sql query to find the matching
     * appointment id within the database to delete. This method is primarily used in the main menu when
     * the user wants to delete an appointment from the database. */
    public static void deleteAppointment(int apptId){
        //int rowsAffected = 0;
        try {
            String sqlQuery = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, apptId);
            preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        //return rowsAffected;

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
