package main;

import DAO.DBConnection;
import DAO.DBCustomers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;

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
        Locale france = new Locale("fr", "FR");
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter fr to switch to french: ");
        String languageCode = keyboard.nextLine();
        if(languageCode.equals("fr")){
            Locale.setDefault(france);
        }
        else{
            System.out.println("Default set to english");
        }
        try {
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
            }
        }catch(MissingResourceException e){
            System.out.println("Locale does not match any resource bundle properties within resourceBundles folder.");
        }
        launch(args);
        DBConnection.closeConnection();
    }


}
