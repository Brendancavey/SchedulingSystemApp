package controller;

import DAO.DBAppointments;
import DAO.DBUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class LoginPage implements Initializable {

    public Label timezoneText;
    public ComboBox<Locale> languageChoice;
    public Button resetButton;
    public Button loginButton;
    public PasswordField passwordTextfield;
    public TextField usernameTextfield;
    public Label incorrectLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login page initialized!");

        ///////////TIMEZONE////////////////////////
        timezoneText.setText(Helper.getTimeZone());
        //////////////////////////////////////////

        /////////////LANGUAGE CHOICE/////////////////////
        languageChoice.setItems(Helper.languageList);

        if(Helper.language == Helper.english){
            languageChoice.getSelectionModel().select(Helper.english);
        }
        else if (Helper.language == Helper.french){
            languageChoice.getSelectionModel().select(Helper.french);
        }
        /*else{
            languageChoice.getSelectionModel().selectFirst();
        }*/
        //languageChoice.setPromptText(languageChoice.getValue().getDisplayLanguage());
        //////////////////////////////////////////////////
    }

    public void onReset(ActionEvent actionEvent) {
        usernameTextfield.clear();
        passwordTextfield.clear();
    }

    public void onLogin(ActionEvent actionEvent) throws IOException {
        ObservableList<User> usersList = DBUsers.getAllUsers();
        boolean loginSuccessful = false;
        //iterate through entire users list to see if user exist
        for(User users : usersList){
            //if the name and password text field matches what is contained in the user object, then login successful
            if (usernameTextfield.getText().equals(users.getUserName()) && passwordTextfield.getText().equals(users.getPassword())){
                System.out.println("Login successful");
                loginSuccessful = true;
                incorrectLabel.setOpacity(0);
                Helper.userWhoLoggedIn = users; //capturing the user who logged in to be used to determine who made modifications to database if any
                Helper.goToMainMenu(actionEvent);
                /////////////CHECKING TO SEE IF ANY APPOINTMENTS ARE COMING UP//////////
                LocalTime startTime;
                LocalTime currentTime = LocalTime.now();
                ArrayList<String> listOfAppts = new ArrayList<String>();
                for (Appointment appointment : DBAppointments.getAllAppointments()) {
                    startTime = appointment.getStartDate().toLocalTime();
                    long timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);
                    System.out.println((timeDifference));
                    if (timeDifference > 0 && timeDifference <= 15) {
                        listOfAppts.add(String.valueOf("ID: " + appointment.getApptId()) + " | " + appointment.getStartDateReadableFormat() + "\n");
                    }
                }
                if (listOfAppts.isEmpty()) {
                    Helper.displayMessage("No upcoming appointments");
                }
                else {
                    Helper.displayMessage("You have these appointments coming up within 15 minutes! \n" + listOfAppts);
                }
                ////////////////////////////////////////////////////////////////////
                break;
            }
        }
        if(loginSuccessful == false){
            incorrectLabel.setOpacity(1); //display message to user to indicate login user/pass incorrect
        }
    }

    public void onLanguageSelection(ActionEvent actionEvent) {
        if(languageChoice.getValue().getLanguage() == "fr"){
            Helper.language = Helper.french;
            Locale.setDefault(Helper.french);
        }
        else if (languageChoice.getValue().getLanguage() == "en"){
            Helper.language = Helper.english;
            Locale.setDefault(Helper.english);
        }
        try {
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
            }
        }catch(MissingResourceException e){
            System.out.println("Locale does not match any resource bundle properties within resourceBundles folder.");
        }
    }
}
