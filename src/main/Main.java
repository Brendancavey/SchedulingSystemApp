package main;

import DAO.DBConnection;
import DAO.DBCustomers;
import DAO.DBProvinces;
import controller.Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.setTitle("Login Page");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }
    /** Functionality Improvements: I would add a register user screen and add users to the database. Instead of storing
     * passwords in User objects, I would implement a hashing algorithm to store passwordHashes for security. Then store
     * the password hash into a hashmap with the password hash as the key, and password as the value. Add a checkbox "remember me"
     * on the login menu so that user does not have enter username and password every single time.*/
    public static void main(String[] args){
        DBConnection.openConnection();
        ///////////////ADDING SUPPORTED LANGUAGES TO LANGUAGE LIST//////////////////
        Helper.languageList.add(Helper.english);
        Helper.languageList.add(Helper.french);
        //////////////////////////////////////////////////////////////////////////////
        /*int rowsAffected = DBCustomers.insertCustomer("Bear", 5);
        System.out.println(rowsAffected);
        int rowsaffected = DBCustomers.updateCustomer(1, "Woobert");
        System.out.println(rowsaffected);*/
        //int rowsaffected1 = DBCustomers.deleteCustomer();
        DBCustomers.selectCustomer(103);

        ////////////////////////////////////////////////////////////
        //////////////////////TESTING TIMEZONE/////////////////////
        System.out.println(ZoneId.systemDefault());
        ZoneId.getAvailableZoneIds().stream().filter(z->z.contains("America")).sorted().forEach((System.out::println));
        ///////////////////TESTING ZONE DATE TIME////////////////////////////
        LocalDate localDate = LocalDate.of(2022, 9, 8);
        LocalTime localTime = LocalTime.of(11, 2);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        System.out.println(zonedDateTime);
        //////////////////////////////////////////
        /////////////////////TESTING EXTRACTING ZONE DATE TIME COMPONENTS////////
        System.out.println(zonedDateTime.toLocalDate());
        System.out.println(zonedDateTime.toLocalTime());
        System.out.println(zonedDateTime.toLocalDate().toString() + " " + zonedDateTime.toLocalTime().toString());
        ///////////////////////////////////////////////////////////////////////
        ///////////////////TESTING CREATING ZonedDateTime OBJECT/////////////////
        System.out.println("User time: " + zonedDateTime);
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(zonedDateTime.toInstant(), utcZoneId);
        System.out.println("User time to UTC time: " + utcZDT);
        zonedDateTime = ZonedDateTime.ofInstant(utcZDT.toInstant(), zoneId);
        System.out.println("UTC Time to user local time: " + zonedDateTime);
        ////////////////////////////////////////////////////////////////////////
        launch(args);
        DBConnection.closeConnection();
    }


}
