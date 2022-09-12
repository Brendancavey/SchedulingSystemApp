package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

///////////HELPER METHODS////////////////////
public class Helper {
    //////////////////STATIC VARIABLES/////////////////////
    public static boolean viewAllCustomersToggle = false;
    public static boolean userClickedAddCustomer = false;
    public static boolean userClickedAddAppointment = false;
    public static User userWhoLoggedIn = null;
    public static Locale french = new Locale("fr", "FR");
    public static Locale english = new Locale("en", "US");
    public static Locale language = english;
    public static ObservableList<Locale> languageList = FXCollections.observableArrayList();
    ////////////////////////////////////////////////////////


    //////////////SWITCHING BETWEEN SCENES/////////////////////////////
    public static void goToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 1100, 500));
        stage.show();
    }
    public static void goToLogin(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/LoginPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Login Page");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }
    public static void goToAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/AppointmentPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }
    public static void goToModifyAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/AppointmentPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Modify Appointment");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }
    public static void goToAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }
    public static void goToModifyCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Modify Customer");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }
    public static void goToReportsPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/ReportsPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports Page");
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
    /////////////////////////////////////////////////////////////////////////
    ////////////////////DISPLAYING TIMEZONE////////////////////////////////
    public static String getTimeZone(){
        String timeZone = String.valueOf(ZoneId.systemDefault());
        return timeZone;
    }
    /////////////////////////////////////////////////////////////////////////
    //////////////////CONVERTING LOCAL TIME TO UTC///////////////////////////
    public static ZonedDateTime convertToUtc(LocalDateTime dateTime){
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId myZoneId = ZoneId.systemDefault();

        ZonedDateTime myZonedDateTime = ZonedDateTime.of(dateTime, myZoneId);
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZonedDateTime.toInstant(), utcZoneId);
        return utcZDT;
    }
    ///////////////////CONVERTING UTC TO LOCAL TIME//////////////////////
    public static ZonedDateTime convertFromUtcToLocal(LocalDateTime dateTime){
        ZoneId myZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        ZonedDateTime utcZDT = ZonedDateTime.of(dateTime, utcZoneId);
        ZonedDateTime myZonedDateTime = ZonedDateTime.ofInstant(utcZDT.toInstant(), myZoneId);
        System.out.println(myZonedDateTime);
        return myZonedDateTime;
    }
    /////////////////CONVERTING LOCAL TO EST///////////////////////////////
    public static ZonedDateTime convertToEst(LocalDateTime dateTime){
        ZoneId estZoneId = ZoneId.of("US/Eastern"); //eastern time zone
        ZoneId myZoneId = ZoneId.systemDefault();

        ZonedDateTime myZonedDateTime = ZonedDateTime.of(dateTime, myZoneId);
        ZonedDateTime estZDT = ZonedDateTime.ofInstant(myZonedDateTime.toInstant(), estZoneId);
        return estZDT;
    }
    //////////////////////CONVERTING EST TO LOCAL///////////////////////////
    public static ZonedDateTime convertFromEstToLocal(LocalDateTime dateTime){
        ZoneId estZoneId = ZoneId.of("US/Eastern"); //eastern time zone
        ZoneId myZoneId = ZoneId.systemDefault();

        ZonedDateTime estZDT = ZonedDateTime.of(dateTime, estZoneId);
        ZonedDateTime myZDT = ZonedDateTime.ofInstant(estZDT.toInstant(), myZoneId);
        return myZDT;
    }



}
