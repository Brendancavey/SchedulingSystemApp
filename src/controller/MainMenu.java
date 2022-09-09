package controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBProvinces;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Province;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {

    /////////////////////BUTTONS/////////////////////////////
    public Button addButton;
    public Button modifyButton;
    public Button deleteButton;
    public Button logoutButton;
    public Button reportsButton;
    ///////////////////////////////////////////////////////
    //////////////////APPOINTMENTS TABLIEVIEW///////////////
    public TableView apptTableView;
    public TableColumn apptIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateCol;
    public TableColumn endDateCol;
    public TableColumn custIdCol;
    public TableColumn userIdCol;
    //////////////////////////////////////////////////////////
    ///////////////CUSTOMERS TABLEVIEW/////////////////////////
    public TableView<Customer> customerTableView;
    public TableColumn<Customer, Integer> custId;
    public TableColumn<Customer, String> custName;
    public TableColumn<Customer, String> custAddress;
    public TableColumn<Customer, String> custPostalCode;
    public TableColumn<Customer, String> custPhoneNumber;
    public TableColumn<Customer, String> custCreateDate;
    public TableColumn<Customer, String> custCreateBy;
    public TableColumn<Customer, String> custLastUpdate;
    public TableColumn<Customer, String> custLastUpdateBy;
    public TableColumn<Customer, Integer> custProvince;
    //////////////////////////////////////////////////////////
    //////////////VIEWBY RADIO BUTTONS///////////////////////
    public RadioButton viewCustomers;
    public RadioButton viewAppointments;
    public RadioButton viewApptAll;
    public RadioButton viewApptMonth;
    public RadioButton viewApptWeek;
    public VBox viewByApptBox;
    /////////////////////////////////////////////////////
    /////////////OTHER//////////////////////////////////
    public Label timezoneText;
    ////////////////////////////////////////////////////


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ////////////////CHECKING FOR TOGGLE VIEW///////////////
        if (Helper.viewAllCustomersToggle){
            viewCustomers.fire();
        }
        else{
            viewAppointments.fire();
        }
        /////////////////////////////////////////////////////
        //////////////DISPLAYING TIMEZONE////////////////
        timezoneText.setText(Helper.getTimeZone());
        ////////////////////////////////////////////////
        /////////////////SETTING UP CUSTOMER TABLEVIEW///////////////////////////
        customerTableView.setItems(DBCustomers.getAllCustomers());
        custId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        custName.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        custProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        /////////////////////////////////////////////////////////////////////////////
    }

    ///////////////////////BUTTONS//////////////////////////////////////////////
    public void onAdd(ActionEvent actionEvent) throws IOException {
        if (Helper.viewAllCustomersToggle) {
            Helper.goToAddCustomer(actionEvent);
        }
        else{

            Helper.goToAddAppointment(actionEvent);
        }
    }

    public void onModify(ActionEvent actionEvent) throws IOException {
        if(Helper.viewAllCustomersToggle) {
            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
            System.out.println(selectedCustomer.getName());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainMenu.class.getResource("/view/CustomerPage.fxml"));
            loader.load();
            CustomerPage customerPageController = loader.getController();
            customerPageController.sendCustomerInformation(selectedCustomer);
            //Helper.goToModifyCustomer(actionEvent);
            //Go to modify customer page
            Parent root = loader.getRoot();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Customer");
            stage.setScene(new Scene(root, 600, 450));
            stage.show();
        }
        else{
            Helper.goToModifyAppointment(actionEvent);
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        if(Helper.viewAllCustomersToggle){
            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem(); //getting selected customer from tableview
            DBCustomers.deleteCustomer((selectedCustomer.getId())); //deleting customer from the database
            customerTableView.setItems(DBCustomers.getAllCustomers()); //updating the table view
        }
        else if(!Helper.viewAllCustomersToggle){ //if the toggle is set to view all appointments, then the delete button deletes from appointment view table
            System.out.println("No appointment to delete");
        }
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
        customerTableView.setDisable(false);
        apptTableView.setDisable(true);
        apptTableView.setOpacity(0);
        viewByApptBox.setOpacity(0);
        Helper.viewAllCustomersToggle = true;
    }
    public void onViewAppointments(ActionEvent actionEvent) {
        addButton.setText("Add Appointment");
        modifyButton.setText("Modify Appointment");
        deleteButton.setText("Delete Appointment");
        customerTableView.setOpacity(0);
        customerTableView.setDisable(true);
        apptTableView.setDisable(false);
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
