/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
package DAO;

import controller.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBUsers {
    /** This is the getAllUsers method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method creates User objects from the database and adds it into a list to be returned.
     * @return usersList Returns a list of all users from the database.*/
    public static ObservableList<User> getAllUsers(){
        ObservableList<User> usersList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from users"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");

                User newUser = new User(userId, userName, password);
                usersList.add(newUser);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return usersList;
    }
    /** This is the selectUserById method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to only select the User that matches the parameter.
     * This method creates a User object from the database and returns it.
     * @return user Returns a user object from the database that matches the sql condition.*/
    public static User selectUserById(int userId){
        User user = null;
        try {
            String sqlQuery = "SELECT * FROM Users WHERE User_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String userPassword = resultSet.getString("Password");
                user = new User(id, userName, userPassword);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return user;
    }
    //UPDATE, DELETE, INSERT not necessary for project requirements.
    /*
    public static void checkDateConversion(){
        String sqlQuery = "SELECT Create_Date from countries"; //sql query
        try{
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while(resultSet.next()){ //for each result that came up from query, create a timestamp object
                Timestamp timestamp = resultSet.getTimestamp("Create_Date");
                System.out.println("CreateDate: " + timestamp.toLocalDateTime().toString());
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }*/
}
