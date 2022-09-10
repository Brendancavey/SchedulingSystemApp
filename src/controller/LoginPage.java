package controller;

import DAO.DBUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.User;

import java.io.IOException;
import java.net.URL;
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
                Helper.userWhoLoggedIn = users; //capturing the user who logged in to be used to determine who made modifications to database if any
                Helper.goToMainMenu(actionEvent);
                break;
            }
        }
        if(loginSuccessful == false){
            System.out.println("Login unsuccessful.");
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
