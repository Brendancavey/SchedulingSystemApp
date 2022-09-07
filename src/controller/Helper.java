package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Helper {
    public static boolean viewAllCustomersToggle = false;

    ///////////HELPER METHODS////////////////////
    public static void goToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 1000, 500));
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


}
