package main;

import DAO.DBConnection;
import DAO.DBCustomers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.setTitle("Login Page");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    public static void main(String[] args){
        DBConnection.openConnection();
        /*int rowsAffected = DBCustomers.insertCustomer("Bear", 5);
        System.out.println(rowsAffected);
        int rowsaffected = DBCustomers.updateCustomer(1, "Woobert");
        System.out.println(rowsaffected);*/
        //int rowsaffected1 = DBCustomers.deleteCustomer();
        DBCustomers.selectCustomer(103);
        launch(args);
        DBConnection.closeConnection();
    }


}
