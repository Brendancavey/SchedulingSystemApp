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
