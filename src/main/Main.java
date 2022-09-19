/** Author: Brendan Thoeung | ID: 007494550 | Date: 9/19/2022
 * */
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
    /** This is the main Method.
     * This method opens a connection to the SQL database, and runs the program. Once complete, the connection is closed.
     * Functionality Improvements: I would add a register user screen and add users to the database. Instead of storing
     * passwords in User objects, I would implement a hashing algorithm to store passwordHashes for security. Add a checkbox "remember me"
     * on the login menu so that user does not have enter username and password every single time.
     * LAMBDA USAGE: Lambda usage can be found under MainMenu.java under methods onModify() and onDelete().
     * JAVADOCS: Zip folder containing JavaDocs can be found as an attached file or in the root folder under JavaDocs. */
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
