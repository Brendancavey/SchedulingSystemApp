package controller;

import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
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
    public Label reportsPageTitle;
    public Button backButton;
    public Label totalCustomersExtraLabel;
    public RadioButton custByTypeAndMonth;
    public TableView typeAndMonthTableView;
    public TableColumn moAndTypeMonth;
    public TableColumn moAndTypeType;
    public TableColumn moAndTypeAppts;

    /** This is the initialize method.
     * This method gets called when first starting this scene. It checks for the
     * locale default if it set to a supported language in the resource bundle, and changes
     * the appropriate labels and text fields to the supported language. This method also
     * initializes the table views with appropriate data and initializes the options combo box to
     * the default selected view by type, and fills the combo box with the appropriate information.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //////////////////CHECKING FOR FRENCH TRANSLATION/////////
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            reportsPageTitle.setText(rb.getString("ReportsPage"));
            totalCount.setText(rb.getString("TotalAppointments"));
            optionsComboBox.setPromptText(rb.getString("Options"));
            viewContactSchedule.setText(rb.getString("ContactSchedule"));
            viewTotalCustomers.setText(rb.getString("TotalCustomers"));
            totalCustomersExtraLabel.setText(rb.getString("ThatHaveAnAppointment"));
            backButton.setText(rb.getString("Back"));
            totalCount.setText(rb.getString("TotalAppointments"));
            custViewByType.setText(rb.getString("ByType"));
            custViewByMonth.setText(rb.getString("ByMonth"));
            custViewByCustom.setText(rb.getString("ByCountry"));
            conApptId.setText(rb.getString("ApptId"));
            conCustId.setText(rb.getString("CustomerId"));
            conTitle.setText(rb.getString("Title"));
            conDesc.setText(rb.getString("Description"));
            conType.setText(rb.getString("Type"));
            conStart.setText(rb.getString("Start"));
            conEnd.setText(rb.getString("End"));
            custCustId.setText(rb.getString("CustomerId"));
            custName.setText(rb.getString("Name"));
            custAddress.setText(rb.getString("Address"));
            custPostal.setText(rb.getString("PostalCode"));
            custPhone.setText(rb.getString("PhoneNumber"));
            custProvince.setText(rb.getString("Province"));

        }
        ////////////////////////////////////////////////////////////
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
        /////////INITIALIZING MONTH AND TYPE TABLE VIEW/////////////
        ///////////////ONLY SHOWING NON DUPLICATE LIST///////////////
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        ObservableList<Appointment> noDuplicates = FXCollections.observableArrayList();
        for (int i = 0; i < allAppointments.size(); i++){
            Appointment a = allAppointments.get(i);
            boolean foundMatch = false;
            for (int j = i+1; j < allAppointments.size(); j++){ //iterate through list again to check to see if any appointments in the list match appointment a appointment month and type
               Appointment b = allAppointments.get(j);
               if (a.getAppointmentMonth().equals(b.getAppointmentMonth()) && a.getType().equals(b.getType())){
                   foundMatch = true; //if true set boolean to true
               }
            }
            if(foundMatch == false){ //only add the appointment to no duplicate list if a duplicate was not found
                noDuplicates.add(a);
            }
        }
        System.out.println(noDuplicates);
        typeAndMonthTableView.setItems(noDuplicates);
        moAndTypeMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        moAndTypeType.setCellValueFactory(new PropertyValueFactory<>("type"));
        moAndTypeAppts.setCellValueFactory(new PropertyValueFactory<>("allApptWithSameMonthAndType"));
        ///////////INITIALIZING COMBO BOX SELECTION FOR DEFAULT CONTACT SCHEDULE/////
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }
        /////////////////////////////////////////////////////////////////////////////
        yearLabel.setText(String.valueOf(LocalDate.now().getYear())); //setting year label to current year
    }
    /** This is the onViewContactSchedule method.
     * This method gets called when selecting on the contact schedule radio button. It
     * makes changes to the appropriate widgets/labels to display the appropriate information. Then,
     * the method iterates through all of the contacts from the contacts data base and adds them to the options
     * combo box.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onViewContactSchedule(ActionEvent actionEvent) {
        contactScheduleTableView.getItems().clear();
        contactScheduleTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        optionsComboBox.setOpacity(1);
        viewLabel.setOpacity(1);
        totalCount.setOpacity(1);
        yearLabel.setOpacity(0);
        viewByBox.setOpacity(0);
        customerTableView.setDisable(true);
        customerTableView.setOpacity(0);
        contactScheduleTableView.setOpacity(1);
        contactScheduleTableView.setDisable(false);
        typeAndMonthTableView.setOpacity(0);
        typeAndMonthTableView.setDisable(true);
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            totalCount.setText(rb.getString("TotalAppointments"));
            viewLabel.setText(rb.getString("Contact"));
        }
        else{
            totalCount.setText("Total Appointments: ");
            viewLabel.setText("Contact");
        }
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }
    }
    /** This is the onViewTotalCustomers method.
     *  This method gets called when selecting on the total customers radio button. It
     *   makes changes to the appropriate widgets/labels to display the appropriate information. Then,
     *  the method calls the appropriate method ("fillBy") to fill the options combo box corresponding to the
     *  viewBy radio button (by type, month, or country) selection.
     * LOGICAL ERROR: The combo box containing view by type displayed multiple copies of the same type. To
     * fix this, I verified to not add to the combo box if it already exists within the combo box selection.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onViewTotalCustomers(ActionEvent actionEvent) {
        //////////////SETTING WIDGETS TO APPROPRIATE VALUES/////////
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        optionsComboBox.setOpacity(1);
        viewLabel.setOpacity(1);
        totalCount.setOpacity(1);
        viewByBox.setOpacity(1);
        customerTableView.setDisable(false);
        customerTableView.setOpacity(1);
        contactScheduleTableView.setOpacity(0);
        contactScheduleTableView.setDisable(true);
        typeAndMonthTableView.setOpacity(0);
        typeAndMonthTableView.setDisable(true);

        ///////////////////////////////////////////

        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());

        //////////FILL COMO BOX DEPENDING ON THE SELECTION OF THE VIEW-BY RADIO BUTTONS////////////
        if(custViewByType.isSelected()) {

            if (Locale.getDefault().getLanguage().equals("fr")) {
                viewLabel.setText(rb.getString("Type"));
            }
            else {
                viewLabel.setText("Type");
            }
            fillByType(); //filling combo box by type
        }
        else if(custViewByMonth.isSelected()){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                viewLabel.setText(rb.getString("Month"));
            }
            else {
                viewLabel.setText("Month");
            }
            yearLabel.setOpacity(1);
            fillByMonth(); //filling combo box by month
        }
        else{
            if (Locale.getDefault().getLanguage().equals("fr")) {
                viewLabel.setText(rb.getString("Country"));
            }
            else {
                viewLabel.setText("Countries");
            }
            fillByCountry(); //filling combo box by country
        }
        if (Locale.getDefault().getLanguage().equals("fr")) {
            totalCount.setText(rb.getString("TotalCustomers"));
        }
        else{
            totalCount.setText("Total Customers: ");
        }
        /////////////////////////////////////////////////////////////////////////////

    }
    /////////////////////VIEW BY RADIO BUTTONS///////////////////////
    /** This is the onViewByType method.
     * This method gets called when selecting the by Type radio button.
     * It removes and clears the options combo box and table view, then fills the options combo box with
     * the appropriate information
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onViewByType(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            viewLabel.setText(rb.getString("Type"));
        }
        else {
            viewLabel.setText("Type");
        }
        yearLabel.setOpacity(0);
        fillByType(); //filling combo box by type
    }
    /** This is the onViewByMonth method.
     * This method gets called when selecting the by Month radio button.
     * It removes and clears the options combo box and table view, then fills the options combo box with
     * the appropriate information
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onViewByMonth(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            viewLabel.setText(rb.getString("Month"));
        }
        else {
            viewLabel.setText("Month");
        }
        yearLabel.setOpacity(1);
        fillByMonth(); //filling combo box by month
    }
    /** This is the onViewByCustom method.
     * This method gets called when selecting the by Country radio button.
     * It removes and clears the options combo box and table view, then fills the options combo box with
     * the appropriate information
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onViewByCustom(ActionEvent actionEvent) {
        customerTableView.getItems().clear();
        customerTableView.getItems().removeAll();
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            viewLabel.setText(rb.getString("Country"));
        }
        else {
            viewLabel.setText("Countries");
        }
        yearLabel.setOpacity(0);
        fillByCountry();//filling combo box by country
        //////////////////////////////////////////////////////////////

    }
    ////////////////////////COMBO BOX SELECTIONS//////////////////////////////////////////
    /** This is the onOptionsSelection method.
     * This method is called whenever the user makes a selection from the options combo box. This method
     * contains control flow statements to check which radio buttons the user selected and makes the appropriate changes to the scene
     * depending on the selection - such as displaying the correct table view, what is filled in the table view, and label changes.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onOptionsSelection(ActionEvent actionEvent) {
        ////////////////////VIEW CONTACT SCHEDULE SELECTED///////////////////////////////
        if(viewContactSchedule.isSelected()){
            Contact selectedContact = (Contact)optionsComboBox.getSelectionModel().getSelectedItem();
            ObservableList<Appointment> list = DBAppointments.getAppointmentsByContactId(selectedContact.getContactId()); //getting list of appointments that match contact id
            contactScheduleTableView.setItems(list); //fill table view with appropriate list
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                totalCount.setText(rb.getString("TotalAppointments") + ": " + list.size());
            }
            else{
                totalCount.setText("Total Appointments: " + list.size()); //get total number of appointments with filter
            }
        }
        ///////////////////////////////////////////////////////////////////////
        else{/////////////////////////VIEW TOTAL CUSTOMERS SELECTED////////////////////////////////////////////////
            /////////////////////////////VIEW BY TYPE COMBO BOX SELECTION///////////////////////////////////////////////////////
            if(custViewByType.isSelected()){
                String selectedType = (String)(optionsComboBox.getSelectionModel().getSelectedItem());
                ObservableList<Appointment> listByType = DBAppointments.getAppointmentsByType(selectedType); //getting list of appointments by type
                ObservableList<Customer> listByTypeByCustId = FXCollections.observableArrayList(); //initializing customer list
                for (Appointment appointments : listByType){
                    boolean customerExistsInList = false; //boolean to check if customer already exists in list
                    Customer customerToAdd = DBCustomers.selectCustomerById(appointments.getCustId());
                    /////////////////Checking for duplicates////////////
                    for(Customer customer : listByTypeByCustId){
                        if (customer.getId() == customerToAdd.getId()){
                            customerExistsInList = true;
                        }
                    }
                    /////////////////////////////////////////////////////
                    /////////////Adding customer to list///////////////////
                    if(!customerExistsInList) {
                        //for each appointment in appointment by type, add to the customer list each customer obtained from the matching
                        // customer ID from the appointment
                        listByTypeByCustId.add(customerToAdd);
                    }
                    ////////////////////////////////////////////////////
                }
                customerTableView.setItems(listByTypeByCustId); //fill table view with appropriate list
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    totalCount.setText(rb.getString("TotalCustomers") + ": " + listByTypeByCustId.size()); //get total number of customers with filter
                }
                else{
                    totalCount.setText("Total Customers: " + listByTypeByCustId.size()); //get total number of customers with filter
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////
            //////////////////////VIEW BY MONTH COMBO BOX SELECTION//////////////////////////////////////////////////
            else if(custViewByMonth.isSelected()){
                Month selectedMonth = (Month)(optionsComboBox.getSelectionModel().getSelectedItem());
                ObservableList<Appointment> listOfAppointments = DBAppointments.getAllAppointments();
                ObservableList<Customer> listOfCustomerById = FXCollections.observableArrayList();
                for (Appointment appointments : listOfAppointments){
                    boolean customerExistsInList = false; //boolean to check if customer already exists in list
                    Customer customerToAdd = DBCustomers.selectCustomerById(appointments.getCustId());
                    /////////////////Checking for duplicates////////////
                    for(Customer customer : listOfCustomerById){
                        if (customer.getId() == customerToAdd.getId()){
                            customerExistsInList = true;
                        }
                    }
                    /////////////////////////////////////////////////////////
                    //////////////////Adding customer to list///////////////
                    if(!customerExistsInList) {
                        //if the appointment month is equal to the selected month, and appointment year matches the current year,
                        //then add the customer to the list
                        if (appointments.getStartDate().getMonth().equals(selectedMonth) && appointments.getStartDate().getYear() == LocalDate.now().getYear()) {
                            listOfCustomerById.add(customerToAdd);
                        }
                    }
                    ////////////////////////////////////////////////////////
                }
                customerTableView.setItems(listOfCustomerById);
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    totalCount.setText(rb.getString("TotalCustomers") + ": " + listOfCustomerById.size()); //get total number of customers with filter
                }
                else{
                    totalCount.setText("Total Customers: " + listOfCustomerById.size()); //get total number of customers with filter
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////VIEW BY COUNTRY COMBO BOX SELECTION LIST/////////////////////
            else if (custViewByCustom.isSelected()){
                Country selectedCountry = (Country)(optionsComboBox.getSelectionModel().getSelectedItem());
                ObservableList<Appointment> listOfAppointments = DBAppointments.getAllAppointments();
                ObservableList<Customer> listOfCustomersByCountry = FXCollections.observableArrayList();
                for (Appointment appointments : listOfAppointments) {
                    boolean customerExistsInList = false; //boolean to check if customer already exists within list
                    Customer customerToAdd = DBCustomers.selectCustomerById(appointments.getCustId());
                    Country customerCountry = customerToAdd.getCountry();

                    /////////////////Checking for duplicates////////////
                    for (Customer customers : listOfCustomersByCountry){
                        if(customers.getId() == customerToAdd.getId()){
                            customerExistsInList = true;
                        }
                    }
                    ///////////////////////////////////////////////////
                    ///////////////Adding customer to list////////////
                    if (!customerExistsInList) {
                        if (customerCountry.getId() == (selectedCountry.getId())) {
                            listOfCustomersByCountry.add(customerToAdd);
                        }
                    }
                    ///////////////////////////////////////////////
                }
                System.out.println(listOfCustomersByCountry);
                customerTableView.setItems(listOfCustomersByCountry);
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    totalCount.setText(rb.getString("TotalCustomers") + ": " + listOfCustomersByCountry.size()); //get total number of customers with filter
                }
                else{
                    totalCount.setText("Total Customers: " + listOfCustomersByCountry.size()); //get total number of customers with filter
                }
            }
        }
    }
    /** This is the onCancel method.
     * This method is called when selecting the back button. This method takes
     * the user back to the main menu page.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }
    ///////HELPER METHODS////////////
    /** This is the fillByType method.
     * This is a helper method to reduce redundancy across multiple methods.
     * This method fills the options combo box with the appropriate information by iterating through
     * all appointments, and getting the appropriate information from the appointment and adding it to the combo box.
     * */
    public void fillByType(){
        for(Appointment appointments : DBAppointments.getAllAppointments()){
            if(!optionsComboBox.getItems().contains(appointments.getType())) {
                optionsComboBox.getItems().add(appointments.getType());
            }
        }
    }
    /** This is the fillByMonth method.
     * This is a helper method to reduce redundancy across multiple methods.
     * This method fills the options combo box with the appropriate information by iterating through
     * all months of the year and adding it to the options combo box.
     * */
    public void fillByMonth(){
        for (Month months : Month.values()){
            optionsComboBox.getItems().add(months);
        }
    }
    /** This is the fillByType method.
     * This is a helper method to reduce redundancy across multiple methods.
     * This method fills the options combo box with the appropriate information by iterating through
     * all countries, and adding it to the options combo box.
     * */
    public void fillByCountry(){
        for (Country countries : DBCountries.getAllCountries()){
            if(!optionsComboBox.getItems().contains(countries)){
                optionsComboBox.getItems().add(countries);
            }
        }
    }

    public void onViewByTypeAndMonth(ActionEvent actionEvent) {
        optionsComboBox.setOpacity(0);
        viewLabel.setOpacity(0);
        yearLabel.setOpacity(0);
        totalCount.setOpacity(0);
        viewByBox.setOpacity(0);
        typeAndMonthTableView.setOpacity(1);
        typeAndMonthTableView.setDisable(false);
        customerTableView.setDisable(true);
        customerTableView.setOpacity(0);
        contactScheduleTableView.setOpacity(0);
        contactScheduleTableView.setDisable(true);
    }
}
