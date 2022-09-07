package controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.Country;
import model.Customer;

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
    public TableView apptTableView;
    public TableView customerTableView;
    public RadioButton viewApptAll;
    public RadioButton viewApptMonth;
    public RadioButton viewApptWeek;
    public VBox viewByApptBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Helper.viewAllCustomersToggle){
            viewCustomers.fire();
        }
        else{
            viewAppointments.fire();
        }
        System.out.println("Login page initialized!");
        /////////////////TESTING//////////////
        ObservableList<Country> countryList = DBCountries.getAllCountries();
        for (Country C: countryList){
            System.out.println(C.getName());
        }
        DBCountries.checkDateConversion();
        ObservableList<Customer> customerslist = DBCustomers.getAllCustomers();
        for(Customer c: customerslist){
            System.out.println(c.getName());
        }
        ////////////////////////////////////
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

    public void onReport(ActionEvent actionEvent) throws IOException{
        Helper.goToReportsPage(actionEvent);
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
        customerTableView.setOpacity(1);
        apptTableView.setOpacity(0);
        viewByApptBox.setOpacity(0);
        Helper.viewAllCustomersToggle = true;
    }

    public void onViewAppointments(ActionEvent actionEvent) {
        addButton.setText("Add Appointment");
        modifyButton.setText("Modify Appointment");
        deleteButton.setText("Delete Appointment");
        customerTableView.setOpacity(0);
        apptTableView.setOpacity(1);
        viewByApptBox.setOpacity(1);
        Helper.viewAllCustomersToggle = false;
    }

    public void onViewByWeek(ActionEvent actionEvent) {
    }

    public void onViewByMonth(ActionEvent actionEvent) {
    }

    public void onViewAll(ActionEvent actionEvent) {
    }
    //////////////////////////////////////////////////////////////////////
}
