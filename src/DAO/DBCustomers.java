/** Author: Brendan Thoeung | Date: 9/19/2022
 * */
package DAO;

import controller.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.Province;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBCustomers {
    /** This is the getAllCustomers method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method creates Customer objects from the information obtained from the database, and
     * adds it into a list to be returned.
     * @return customersList Returns a list of all customers from the database*/
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


                Province customerProvince = DBProvinces.selectProvinceById(provinceId); //getting province from province id
                Customer newCustomer = new Customer(customerId, customerName, address, postalCode, phoneNumber, customerProvince);
                customersList.add(newCustomer);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customersList;
    }

    /** This is the insertCustomer method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes in parameters to be used within the SQL query, and inserts into the
     * sql database the corresponding information from the parameters into the appropriate
     * database columns.  */
    public static void insertCustomer(String customerName, String address, String postalCode, String phoneNumber, int provinceId){
        //int rowsAffected = 0;
        try {
            String sqlQuery = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_Id, Create_Date, Created_By, Last_Update, Last_Updated_By ) Values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, provinceId);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting create date and converting to UTC for database storage requirements);
            preparedStatement.setString(7, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName());
            preparedStatement.setTimestamp(8,  Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting last updated datetime and converting to UTC for database storage requirements
            preparedStatement.setString(9, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName()); //setting user who last updated this customer
            preparedStatement.executeUpdate();

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        //return rowsAffected;
    }
    /** This is the updateCustomer method.
     * This method gets a connection to the database, and executes a sqlQuery on that database. This
     * method takes in parameters to be used within the Sql query, and updates the sql database that corresponds
     * to the condition of the sql query (where customer_id matches the parameter custID). */
    public static void updateCustomer(int custId, String custName, String address, String postalCode, String phoneNumber, int provinceID){
        //int rowsAffected = 0;
        try {
            String sqlQuery = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, Last_Update = ?, Last_Updated_By = ?  WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, custName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setInt(5, provinceID);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(Helper.convertToUtc(LocalDateTime.now()).toLocalDateTime())); //setting last updated datetime and converting to UTC for database storage requirements);
            preparedStatement.setString(7, String.valueOf(Helper.userWhoLoggedIn.getUserId()) + " | " + Helper.userWhoLoggedIn.getUserName()); //setting user who last updated this customer)
            preparedStatement.setInt(8, custId);
           preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        //return rowsAffected;
    }
    /** This is the deleteCustomer method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes in a parameter to be used as a condition within the sql query (where customer_id
     * matches custId). If a match is found, then that customer is deleted from the database.*/
    public static void deleteCustomer(int custId){
        //int rowsAffected = 0;
        try {
            String sqlQuery = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, custId);
            preparedStatement.executeUpdate();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        //return rowsAffected;

    }
    public static Customer selectCustomerByProvinceId(int divisionId){
        Customer selectedCustomer = null;
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
        return selectedCustomer;
    }
    /** This is the selectCustomerById method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method takes in a parameter to be used within the sql query as a condition to find the matching
     * customer_ID to the parameter. This method creates a customer object from the matching customer ID and its
     * corresponding column information from the database to be returned.
     * @return Returns a customer object that matches to the customer_ID. */
    public static Customer selectCustomerById(int custId){
        Customer newCustomer = null;
        try {
            String sqlQuery = "SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, custId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phoneNumber = resultSet.getString("Phone");
                int provinceId = resultSet.getInt("Division_ID");

                Province customerProvince = DBProvinces.selectProvinceById(provinceId); //getting province from province id
                newCustomer = new Customer(customerId, customerName, address, postalCode, phoneNumber, customerProvince);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return newCustomer;
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
