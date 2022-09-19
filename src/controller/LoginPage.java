/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
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
    public Label loginLabel;
    public Label titleLabel;
    public Label languageLabel;
    public Label timeZoneLabel;

    /** This is the initialize method.
     * This method gets called when first starting this scene. It checks for the
     * locale default if it set to a supported language in the resource bundle, and changes
     * the appropriate labels and text fields to the supported language.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //////////////////CHECKING FOR LANGUAGE//////////////////////////////////
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText(rb.getString("AppointmentScheduler"));
            loginLabel.setText(rb.getString("SignOn"));
            languageLabel.setText(rb.getString("Language"));
            timeZoneLabel.setText(rb.getString("TimeZone"));
            loginButton.setText(rb.getString("Login"));
            resetButton.setText(rb.getString("Reset"));
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));
        }

        ///////////TIMEZONE////////////////////////
        timezoneText.setText(Helper.getTimeZone());
        //////////////////////////////////////////

        /////////////LANGUAGE CHOICE/////////////////////
        languageChoice.setItems(Helper.languageList);
        if(Locale.getDefault().getLanguage() == "fr"){
            Helper.language = Helper.french;
        }
        else if(Locale.getDefault().getLanguage() == "en"){
            Helper.language = Helper.english;
        }
        //////////////Changing language selection to correct choice///////
        if(Helper.language == Helper.english){
            languageChoice.getSelectionModel().select(Helper.english);
        }
        else if (Helper.language == Helper.french){
            languageChoice.getSelectionModel().select(Helper.french);
        }
        //////////////////////////////////////////////////
    }

    public void onReset(ActionEvent actionEvent) {
        usernameTextfield.clear();
        passwordTextfield.clear();
    }

    /** This is the OnLogin method.
     * This method checks to see if the user entered a valid username and password into the appropriate textfields.
     * If successful, a the user is sent to the main menu and a message displays if there are any upcoming appointments.
     * If unsuccessful, a message is displayed signaling to the user that an incorrect username or password was entered.
     * This method also outputs to a text file of any successful or unsuccessful login attempts.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     */
    public void onLogin(ActionEvent actionEvent) throws IOException {
        ObservableList<User> usersList = DBUsers.getAllUsers();
        boolean loginSuccessful = false;
        //iterate through entire users list to see if user exist
        for (User users : usersList) {
            //if the name and password text field matches what is contained in the user object, then login successful
            if (usernameTextfield.getText().equals(users.getUserName()) && passwordTextfield.getText().equals(users.getPassword())) {
                System.out.println("Login successful");
                loginSuccessful = true;
                incorrectLabel.setOpacity(0);
                Helper.userWhoLoggedIn = users; //capturing the user who logged in to be used to determine who made modifications to database if any
                Helper.goToMainMenu(actionEvent);
                /////////////CHECKING TO SEE IF ANY APPOINTMENTS ARE COMING UP//////////
                LocalTime startTime;
                LocalDate startDate;
                LocalTime currentTime = LocalTime.now();
                ArrayList<String> listOfAppts = new ArrayList<String>();
                for (Appointment appointment : DBAppointments.getAllAppointments()) {
                    startTime = appointment.getStartDate().toLocalTime();
                    startDate = appointment.getStartDate().toLocalDate(); //verifying that the date of appointment matches local date.now
                    long timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);
                    //System.out.println((timeDifference));
                    if (timeDifference > 0 && timeDifference <= 15 && startDate.equals(LocalDate.now())) {
                        listOfAppts.add(String.valueOf("ID: " + appointment.getApptId()) + " | " + appointment.getStartDateReadableFormat() + "\n");
                    }
                }
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (listOfAppts.isEmpty()) {
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        Helper.displayMessage(rb.getString("NoUpcomingAppointments"));
                    } else {
                        Helper.displayMessage("No upcoming appointments");
                    }
                } else {
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        Helper.displayMessage(rb.getString("AppointmentsComingUpWithin15Minutes") + "\n" + listOfAppts);
                    } else {
                        Helper.displayMessage("You have these appointments coming up within 15 minutes! \n" + listOfAppts);
                    }
                }
                ////////////////////////////////////////////////////////////////////
                break;
            }
        }
        if (loginSuccessful == false) {
            try {
                /////////////////////Outputting message for incorrect information/////
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    incorrectLabel.setText(rb.getString("IncorrectUserNameOrPassword"));
                } else {
                    incorrectLabel.setText("Incorrect username/password");
                }
                incorrectLabel.setOpacity(1); //display message to user to indicate login user/pass incorrect
            } catch (MissingResourceException e) {
                incorrectLabel.setText("Incorrect username/password");
                incorrectLabel.setOpacity(1); //display message to user to indicate login user/pass incorrect
            }
        }
        /////////////////OUTPUTTING TO FILE LOGIN ATTEMPT///////////////////
        String success;
        if(loginSuccessful){success = "Success";}else{success = "Fail";}
        FileWriter fw = new FileWriter(("login_activity.txt"), true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("Log-in attempt at " + LocalDate.now() + " | " + Helper.toReadableTime(LocalTime.now()) + " | " + Helper.getTimeZone() + " | Log-in attempt: " + success);
        pw.close();
        /////////////////////////////////////////////////////////////////
    }

    /** This is the OnLanguageSelection method.
     * This method corresponds to a combo box that allows the user to select their desired language which
     * would change the language of the program to the chosen language. For the purpose of the project, this
     * drop down menu is disabled. For future enhancements, this method will be further worked on.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onLanguageSelection(ActionEvent actionEvent) {
        if(languageChoice.getValue().getLanguage() == "fr"){
            Helper.language = Helper.french;
            Locale.setDefault(Helper.french);
        }
        else if (languageChoice.getValue().getLanguage() == "en"){
            Helper.language = Helper.english;
            Locale.setDefault(Helper.english);
        }
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText(rb.getString("AppointmentScheduler"));
            loginLabel.setText(rb.getString("SignOn"));
            languageLabel.setText(rb.getString("Language"));
            timeZoneLabel.setText(rb.getString("TimeZone"));
            loginButton.setText(rb.getString("Login"));
            resetButton.setText(rb.getString("Reset"));
            System.out.println(rb.getString("hello") + " " + rb.getString("world"));
        }
    }

}
