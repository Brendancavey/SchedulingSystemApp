package controller;

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

public class ReportsPage implements Initializable {

    public RadioButton viewContactSchedule;
    public RadioButton viewTotalCustomers;
    public VBox viewByBox;
    public RadioButton custViewByType;
    public RadioButton custViewByMonth;
    public RadioButton custViewByCustom;
    public Label totalCount;
    public Label viewLabel;
    public Label yearLabel;
    public ComboBox optionsComboBox;
    public TableView contactScheduleTableView;
    public TableColumn conApptId;
    public TableColumn conTitle;
    public TableColumn conDesc;
    public TableColumn conType;
    public TableColumn conStart;
    public TableColumn conEnd;
    public TableColumn conCustId;
    public TableView customerTableView;
    public TableColumn custCustId;
    public TableColumn custName;
    public TableColumn custAddress;
    public TableColumn custPostal;
    public TableColumn custPhone;
    public TableColumn custProvince;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //////////INITIALIZING CONTACT TABLE VIEW//////////////////
        conApptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        conTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        conDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        conType.setCellValueFactory(new PropertyValueFactory<>("type"));
        conStart.setCellValueFactory(new PropertyValueFactory<>("startDateReadableFormat"));
        conEnd.setCellValueFactory(new PropertyValueFactory<>("endDateReadableFormat"));
        conCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        ////////////////////////////////////////////////////////////
        ///////////INITIALIZING CUSTOMER TABLE VIEW/////////////////
        custCustId.setCellValueFactory(new PropertyValueFactory<>("id"));
        custName.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        custProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        //////////////////////////////////////////////////////////////
        ///////////INITIALIZING COMBO BOX SELECTION FOR DEFAULT CONTACT SCHEDULE/////
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }
        /////////////////////////////////////////////////////////////////////////////
        yearLabel.setText(String.valueOf(LocalDate.now().getYear())); //setting year label to current year
    }
    public void onViewContactSchedule(ActionEvent actionEvent) {
        contactScheduleTableView.getItems().clear();
        contactScheduleTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        yearLabel.setOpacity(0);
        viewByBox.setOpacity(0);
        customerTableView.setDisable(true);
        customerTableView.setOpacity(0);
        contactScheduleTableView.setOpacity(1);
        contactScheduleTableView.setDisable(false);
        viewLabel.setText("Contact");
        totalCount.setText("Total Apppointments: ");
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }
    }
    /** LOGICAL ERROR: The combo box containing view by type displayed multiple copies of the same type. To
     * fix this, I verified to not add to the combo box if it already exists within the combo box selection.*/
    public void onViewTotalCustomers(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        viewByBox.setOpacity(1);
        customerTableView.setDisable(false);
        customerTableView.setOpacity(1);
        contactScheduleTableView.setOpacity(0);
        contactScheduleTableView.setDisable(true);

        if(custViewByType.isSelected()) {
            viewLabel.setText("Type");
            fillByType(); //filling combo box by type
        }
        else if(custViewByMonth.isSelected()){
            viewLabel.setText("Month");
            yearLabel.setOpacity(1);
            fillByMonth(); //filling combo box by month
        }
        else{
            viewLabel.setText("Custom");
        }
        totalCount.setText("Total Customers: ");
    }
    /////////////////////VIEW BY RADIO BUTTONS///////////////////////
    public void onViewByType(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        viewLabel.setText("Type");
        yearLabel.setOpacity(0);
        fillByType(); //filling combo box by type
    }
    public void onViewByMonth(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        viewLabel.setText("Month");
        yearLabel.setOpacity(1);
        fillByMonth(); //filling combo box by month
    }
    public void onViewByCustom(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        viewLabel.setText("Custom");
        yearLabel.setOpacity(0);
        //////////////////////////////////////////////////////////////

    }
    ////////////////////////COMBO BOX SELECTIONS//////////////////////////////////////////
    public void onOptionsSelection(ActionEvent actionEvent) {
        ////////////////////VIEW CONTACT SCHEDULE SELECTED///////////////////////////////
        if(viewContactSchedule.isSelected()){
            Contact selectedContact = (Contact)optionsComboBox.getSelectionModel().getSelectedItem();
            ObservableList<Appointment> list = DBAppointments.getAppointmentsByContactId(selectedContact.getContactId()); //getting list of appointments that match contact id
            contactScheduleTableView.setItems(list); //fill table view with appropriate list
            totalCount.setText("Total Appointments: " + list.size()); //get total number of appointments with filter
        }
        ///////////////////////////////////////////////////////////////////////
        else{/////////////////////////VIEW TOTAL CUSTOMERS SELECTED////////////////////////////////////////////////
            /////////////////////////////VIEW BY TYPE COMBO BOX SELECTION///////////////////////////////////////////////////////
            if(custViewByType.isSelected()){
                String selectedType = (String)(optionsComboBox.getSelectionModel().getSelectedItem());
                ObservableList<Appointment> listByType = DBAppointments.getAppointmentsByType(selectedType); //getting list of appointments by type
                ObservableList<Customer> listByTypeByCustId = FXCollections.observableArrayList(); //initializing customer list
                for (Appointment appointments : listByType){
                    //for each appointment in appointment by type, add to the customer list each customer obtained from the matching
                    // customer ID from the appointment
                    listByTypeByCustId.add(DBCustomers.selectCustomerById(appointments.getCustId()));
                }
                customerTableView.setItems(listByTypeByCustId); //fill table view with appropriate list
                totalCount.setText("Total Customers: " + listByTypeByCustId.size()); //get total number of customers with filter
            }
            //////////////////////////////////////////////////////////////////////////////////////
            //////////////////////VIEW BY MONTH COMBO BOX SELECTION//////////////////////////////////////////////////
            else if(custViewByMonth.isSelected()){
                Month selectedMonth = (Month)(optionsComboBox.getSelectionModel().getSelectedItem());
                ObservableList<Appointment> listOfAppointments = DBAppointments.getAllAppointments();
                ObservableList<Customer> listByMonthByCustId = FXCollections.observableArrayList();
                for (Appointment appointments : listOfAppointments){
                    if(appointments.getStartDate().getMonth().equals(selectedMonth) && appointments.getStartDate().getYear() == LocalDate.now().getYear()){
                        Customer customerToAdd = DBCustomers.selectCustomerById(appointments.getCustId());
                        listByMonthByCustId.add(customerToAdd);
                    }
                }
                customerTableView.setItems(listByMonthByCustId);
                totalCount.setText("Total Customers: " + listByMonthByCustId.size());
            }
            //////////////////////////////////////////////////////////////////////////////////////
            else if (custViewByCustom.isSelected()){
            }
        }
    }
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }
    ///////HELPER METHODS////////////
    public void setUpScreen(){
        //reduces redundancy across multiple widgets
    }
    public void fillByType(){
        for(Appointment appointments : DBAppointments.getAllAppointments()){
            if(!optionsComboBox.getItems().contains(appointments.getType())) {
                optionsComboBox.getItems().add(appointments.getType());
            }
        }
    }
    public void fillByMonth(){
        for (Month months : Month.values()){
            optionsComboBox.getItems().add(months);
        }
    }
}
