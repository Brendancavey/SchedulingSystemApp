/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
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
    /** This is the selectProvinceByCountryId method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to select the provinces with a country_Id that matches the parameter.
     * This method creates province object from the database adds it into a list to return.
     * @return provinceList Returns a list of provinces created from the database that matches the sql condition.*/
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
    /** This is the selectProvinceById method.
     * This method gets a connection to the database, and executes a sqlQuery on that database where
     * the parameter is used as a filter to only select the province with a province_ID that matches the parameter.
     * This method creates a province object from the database and returns it.
     * @return province Returns a Province object created from the database that matches the sql condition.*/
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
    /** This is the selectCountryIdByProvinceId method.
     * This method gets a connection to the database, and executes a sqlQuery on that database,
     * where the parameter is used a filter to only select the country where the division_id matches the parameter.
     * return countryIdFk Returns an integer - country_id  to be used to find the matching country in a separate sql query.*/
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

    //UPDATE, DELETE, INSERT not necessary for project requirements.
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
