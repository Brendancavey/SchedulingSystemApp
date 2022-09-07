package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCustomers {
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from customers"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phoneNumber = resultSet.getString("Phone");
                int provinceId = resultSet.getInt("Division_ID");


                Customer newCustomer = new Customer(customerId, customerName, address, postalCode, phoneNumber, provinceId);
                customersList.add(newCustomer);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customersList;
    }

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
    }
}
