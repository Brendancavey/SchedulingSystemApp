package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable {


    public ComboBox startTimeComboBox;
    public ComboBox endTimeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login page initialized!");
        LocalTime start = LocalTime.of(6, 0);
        LocalTime end = LocalTime.NOON;
        while(start.isBefore(end.plusSeconds(1))){
            startTimeComboBox.getItems().add(start);
            start = start.plusMinutes(30);
        }

    }


    public void onSave(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);

    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }
}
