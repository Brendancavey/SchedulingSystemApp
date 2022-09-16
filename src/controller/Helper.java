package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

///////////HELPER METHODS////////////////////
public class Helper {
    //////////////////STATIC VARIABLES/////////////////////
    public static boolean displayApptTimeConversionMssgOnce = false;
    public static boolean viewAllCustomersToggle = false;
    public static boolean userClickedAddCustomer = false;
    public static boolean userClickedAddAppointment = false;
    public static boolean userClickedModifyAppointment = false;
    public static User userWhoLoggedIn = null;
    public static Locale french = new Locale("fr", "FR");
    public static Locale english = new Locale("en", "US");
    public static Locale language = english;
    public static ObservableList<Locale> languageList = FXCollections.observableArrayList();
    public static HashMap<String, LocalDateTime> timeDictionaryStart = new HashMap<>();
    public static HashMap<String, LocalDateTime> timeDictionaryEnd = new HashMap<>();

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
    /** This is getTimeZone method.
     * This method returns the system default time zone is primarily
     * used for better readability.
     * @return timeZone Returns the timezone in String datatype.*/
    public static String getTimeZone(){
        String timeZone = String.valueOf(ZoneId.systemDefault());
        return timeZone;
    }
    /////////////////////////////////////////////////////////////////////////
    //////////////////CONVERTING LOCAL TIME TO UTC///////////////////////////
    /** This is the convertToUtc method.
     * This method takes in a local date time and convert it to
     * UTC time. This method is primarily used to store local date time
     * in the SQL database in UTC.
     * @return utcZDT Returns utc zoned date time*/
    public static ZonedDateTime convertToUtc(LocalDateTime dateTime){
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId myZoneId = ZoneId.systemDefault();

        ZonedDateTime myZonedDateTime = ZonedDateTime.of(dateTime, myZoneId);
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZonedDateTime.toInstant(), utcZoneId);
        return utcZDT;
    }
    ///////////////////CONVERTING UTC TO LOCAL TIME//////////////////////
    /** This is the convertFromUtcToLocal method.
     * This method takes in a UTC date time and converts it to local date time
     * to be displayed for the user in their local date time. This method is primarily used
     * when taking datetime objects from the database, and they need to be displayed in
     * the user's local date time.
     * @return myZonedDateTime Returns the users zoned date time*/
    public static ZonedDateTime convertFromUtcToLocal(LocalDateTime dateTime){
        ZoneId myZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        ZonedDateTime utcZDT = ZonedDateTime.of(dateTime, utcZoneId);
        ZonedDateTime myZonedDateTime = ZonedDateTime.ofInstant(utcZDT.toInstant(), myZoneId);
        return myZonedDateTime;
    }
    /////////////////CONVERTING LOCAL TO EST///////////////////////////////
    /** This is the convertToEst method.
     * This method takes in a local date time and converts it to EST. This
     * method is primarily used to compare local date time hours to EST since
     * the establishment hours are in EST.
     * @return estZDT Returns EST zoned date time.
     *  */
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
    //////////////////////////////////////////////////////////////////////////
    ////////////////////ALERT MESSAGES///////////////////////////////////////
    /** This is the displayMessage method.
     * This is a helper method to reduce redundancy across multiple classes and methods.
     * This method takes in a string and displays that string in the form of an alert pop up window.*/
    public static void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            alert.setTitle(rb.getString("InformationMessage"));
        }
        else {
            alert.setTitle("Information Message");
        }
        alert.showAndWait();
        //Optional<ButtonType> buttonClicked = alert.showAndWait();
    }
    //////////////////////////////////////////////////////////////////////
    //////////////////////CONVERT LOCAL TIME INTO READABLE FORMAT////////
    /** This is the toReadableTime method.
     * This is a helper method to reduce redundancy across multiple classes and methods.
     * This method takes in a local time object and converts it into a  formatted string that is easier
     * to read for the user.
     * @return formattedTime Returns a local time string that has been formatted for easier readability. */
    public static String toReadableTime(LocalTime time){
        String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mm a"));
        //DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a");
        return formattedTime;
    }



}
