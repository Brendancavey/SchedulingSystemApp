package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerPage implements Initializable {


    public TextField nameText;
    public TextField addressText;
    public TextField postalCodeText;
    public TextField phoneNumberText;
    public ComboBox countryBox;
    public ComboBox DivisionBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login page initialized!");
    }


    public void onSave(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }
}
