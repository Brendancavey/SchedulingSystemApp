package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;
import model.Province;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {
    public static ObservableList<Contact> getAllContacts(){
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();
        try{
            String sqlQuery = "SELECT * from contacts"; //sql query
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery); //prepare data from database
            ResultSet resultSet = preparedStatement.executeQuery(); //execute query and place results into resultSet
            while (resultSet.next()){//for each result that came up from the query, create a Country Object and add to countryList
                int id = resultSet.getInt("Contact_ID");
                String name = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                Contact newContact = new Contact(id, name, email);
                contactsList.add(newContact);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return contactsList;
    }

    public static Contact selectContactById(int contactId){
        Contact contact = null;
        try {
            String sqlQuery = "SELECT * FROM contacts WHERE Contact_ID = ?";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("Contact_ID");
                String name = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                contact = new Contact(id, name, email);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return contact;
    }
}
