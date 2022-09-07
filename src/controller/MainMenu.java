package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import controller.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {


    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public Button logoutButton;
    public Button reportsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login page initialized!");
    }


    public void onAddAppointment(ActionEvent actionEvent) throws IOException {

    }

    public void onModifyAppointment(ActionEvent actionEvent) {
    }

    public void onDeleteAppointment(ActionEvent actionEvent) {
    }

    public void onReport(ActionEvent actionEvent) {
    }

    public void onLogout(ActionEvent actionEvent) throws IOException{
        Helper.goToLogin(actionEvent);
    }
}
