package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Helper {
    ///////////HELPER METHODS////////////////////
    public static void goToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 900, 500));
        stage.show();
    }
    public static void goToLogin(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Helper.class.getResource("/view/LoginPage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Login Page");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

}
