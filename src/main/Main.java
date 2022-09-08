package main;

import DAO.DBConnection;
import DAO.DBCustomers;
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

    public static void main(String[] args){
        DBConnection.openConnection();
        /*int rowsAffected = DBCustomers.insertCustomer("Bear", 5);
        System.out.println(rowsAffected);
        int rowsaffected = DBCustomers.updateCustomer(1, "Woobert");
        System.out.println(rowsaffected);*/
        //int rowsaffected1 = DBCustomers.deleteCustomer();
        DBCustomers.selectCustomer(103);
        /////////////////TESTING LANGUAGE/////////////////////////////
        Locale france = new Locale("fr", "FR");
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter fr to switch to french: ");
        String languageCode = keyboard.nextLine();
        if(languageCode.equals("fr")){
            Locale.setDefault(france);
        }
        else{
            System.out.println("Default set to english");
        }
        try {
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
            }
        }catch(MissingResourceException e){
            System.out.println("Locale does not match any resource bundle properties within resourceBundles folder.");
        }
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
