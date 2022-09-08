package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginPage implements Initializable {

    public Label timezoneText;
    public ComboBox languageChoice;
    public Button resetButton;
    public Button loginButton;
    public PasswordField passwordTextfield;
    public TextField usernameTextfield;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login page initialized!");
        timezoneText.setText(Helper.getTimeZone());
    }

    public void onReset(ActionEvent actionEvent) {
    }

    public void onLogin(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }


}
