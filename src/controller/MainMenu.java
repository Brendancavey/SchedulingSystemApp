package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {


    public Button addButton;
    public Button modifyButton;
    public Button deleteButton;
    public Button logoutButton;
    public Button reportsButton;
    public RadioButton viewCustomers;
    public RadioButton viewAppointments;
    public TableColumn apptIdCol;
    public TableColumn nameCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateCol;
    public TableColumn endDateCol;
    public TableColumn custIdCol;
    public TableColumn userIdCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Helper.viewAllCustomersToggle){
            viewCustomers.fire();
        }
        else{
            viewAppointments.fire();
        }
        System.out.println("Login page initialized!");
    }

    ///////////////////////BUTTONS//////////////////////////////////////////////
    public void onAdd(ActionEvent actionEvent) throws IOException {
        if (!Helper.viewAllCustomersToggle) {
            Helper.goToAddAppointment(actionEvent);
        }
        else{
            Helper.goToAddCustomer(actionEvent);
        }
    }

    public void onModify(ActionEvent actionEvent) throws IOException {
        if(!Helper.viewAllCustomersToggle) {
            Helper.goToModifyAppointment(actionEvent);
        }
        else{
            Helper.goToModifyCustomer(actionEvent);
        }
    }

    public void onDelete(ActionEvent actionEvent) {
    }

    public void onReport(ActionEvent actionEvent) {
    }

    public void onLogout(ActionEvent actionEvent) throws IOException{
        Helper.goToLogin(actionEvent);
    }
    //////////////////////////////////////////////////////////////////////////
    /////////////////////////RADIO BUTTONS///////////////////////////////////

    public void onViewAllCustomers(ActionEvent actionEvent) {
        addButton.setText("Add Customer");
        modifyButton.setText("Modify Customer");
        deleteButton.setText("Delete Customer");
        Helper.viewAllCustomersToggle = true;
    }

    public void onViewAllAppointments(ActionEvent actionEvent) {
        addButton.setText("Add Appointment");
        modifyButton.setText("Modify Appointment");
        deleteButton.setText("Delete Appointment");
        Helper.viewAllCustomersToggle = false;
    }
    //////////////////////////////////////////////////////////////////////
}
