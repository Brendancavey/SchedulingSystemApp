package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Province;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProvinces {
    public static ObservableList<Province> getAllProvinces(){
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from first_level_divisions"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int provinceId = resultSet.getInt("Division_ID");
                String provinceName = resultSet.getString("Division");
                //DateTime createdDate = resultSet.getString("Address");
                //String createdBy = resultSet.getString("Postal_Code");
                //String lastUpdated = resultSet.getString("Phone");
                //String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int countryId = resultSet.getInt("Country_ID");


                Province province = new Province(provinceId, provinceName, countryId);
                provinceList.add(province);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return provinceList;
    }

    public static int insertCustomer(String customerName, int provinceId){
        int rowsAffected = 0;
        try {
            String sqlQuery = "INSERT INTO CUSTOMERS (Customer_Name, Division_Id) Values(?, ?)";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, provinceId);
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
    public static ObservableList<Province> selectProvinceByCountryId(int countryId){
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        try {
            String sqlQuery = "SELECT * FROM first_level_divisions WHERE country_id = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int provinceId = resultSet.getInt("Division_ID");
                String provinceName = resultSet.getString("Division");
                int countryIdFk = resultSet.getInt("Country_ID");
                provinceList.add(new Province(provinceId, provinceName, countryIdFk));
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return provinceList;
    }
    public static Province selectProvinceById(int provinceId){
        Province province = null;
        try {
            String sqlQuery = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, provinceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("Division_ID");
                String provinceName = resultSet.getString("Division");
                int countryIdFk = resultSet.getInt("Country_ID");
                province = new Province(provinceId, provinceName, countryIdFk);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return province;
    }
    public static int selectCountryIdByProvinceId(int provinceId){
        int countryIdFk = 0;
        try {
            String sqlQuery = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, provinceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                countryIdFk = resultSet.getInt("Country_ID");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return countryIdFk;
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
