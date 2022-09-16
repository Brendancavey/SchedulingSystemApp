package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Province;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCountries {
    /** This is the getAllCountries method.
     * This method gets a connection to the database, and executes a sqlQuery on that database.
     * This method creates Country objects from the database and adds it into a list to be returned.
     * @return appointmentList Returns a list of all countries from the database.*/
    public static ObservableList<Country> getAllCountries(){
        ObservableList<Country> countriesList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from countries"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                Country newCountryObject = new Country(countryId, countryName);
                countriesList.add(newCountryObject);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return countriesList;
    }
    /** This is the selectCountryById method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to only select the Country with a country_ID that matches the parameter.
     * This method creates a Country object from the database and returns it.
     * @return country Returns a Country object created from the database that matches the sql condition.*/
    public static Country selectCountryById(int countryId){
        Country country = null;
        try {
            String sqlQuery = "SELECT * FROM Countries WHERE Country_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("Country_ID");
                String countryeName = resultSet.getString("Country");
                country = new Country(id, countryeName);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return country;
    }
    //CREATE, UPDATE, DELETE not necessary for project requirements
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
