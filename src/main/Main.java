package main;

import DAO.DBConnection;
import DAO.DBCustomers;
import controller.Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.*;

public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.setTitle("Login Page");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }
    /** Functionality Improvements: I would add a register user screen and add users to the database. Instead of storing
     * passwords in User objects, I would implement a hashing algorithm to store passwordHashes for security. Then store
     * the password hash into a hashmap with the password hash as the key, and password as the value. Add a checkbox "remember me"
     * on the login menu so that user does not have enter username and password every single time.*/
    public static void main(String[] args){
        DBConnection.openConnection();
        ///////////////ADDING SUPPORTED LANGUAGES TO LANGUAGE LIST//////////////////
        Helper.languageList.add(Helper.english);
        Helper.languageList.add(Helper.french);
        /////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        launch(args);
        DBConnection.closeConnection();
    }


}
