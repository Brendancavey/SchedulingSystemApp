package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.Province;

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


                Province customerProvince = DBProvinces.selectProvinceById(provinceId);
                Customer newCustomer = new Customer(customerId, customerName, address, postalCode, phoneNumber, customerProvince);
                customersList.add(newCustomer);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customersList;
    }

    public static int insertCustomer(String customerName, String address, String postalCode, String phoneNumber, int provinceId){
        int rowsAffected = 0;
        try {
            String sqlQuery = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_Id) Values(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, provinceId);
            rowsAffected = preparedStatement.executeUpdate();

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return rowsAffected;
    }
    public static int updateCustomer(int custId, String custName){
        int rowsAffected = 0;
        try {
            String sqlQuery = "UPDATE CUSTOMERS SET Customer_Name = ? WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, custName);
            preparedStatement.setInt(2, custId);
            rowsAffected = preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return rowsAffected;
    }
    public static int deleteCustomer(int custId){
        int rowsAffected = 0;
        try {
            String sqlQuery = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, custId);
            rowsAffected = preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return rowsAffected;

    }
    public static void selectCustomer(){
        try {
            String sqlQuery = "SELECT * FROM CUSTOMERS";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                System.out.println(customerId + " | " + customerName);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void selectCustomer(int divisionId){
        try {
            String sqlQuery = "SELECT * FROM CUSTOMERS WHERE DIVISION_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, divisionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                int divisionIdFK = resultSet.getInt("Division_ID");
                System.out.println(customerId + " | " + customerName + " | " + divisionIdFK);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /*public static void checkDateConversion(){
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
