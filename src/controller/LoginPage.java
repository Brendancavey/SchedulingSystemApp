package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    }

    public void onLogin(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
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
