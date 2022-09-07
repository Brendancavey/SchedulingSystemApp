package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable {



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
