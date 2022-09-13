package controller;

import DAO.DBAppointments;
import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBProvinces;
import javafx.collections.FXCollections;
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
import model.Appointment;
import model.Country;
import model.Customer;
import model.Province;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    public TableView<Appointment> apptTableView;
    public TableColumn<Appointment, Integer> apptIdCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> descriptionCol;
    public TableColumn<Appointment, String> locationCol;
    public TableColumn<Appointment, String> contactCol;
    public TableColumn<Appointment, String> typeCol;
    public TableColumn<Appointment, LocalDateTime> startDateCol;
    public TableColumn<Appointment, LocalDateTime> endDateCol;
    public TableColumn<Appointment, Integer> custIdCol;
    public TableColumn<Appointment, Integer> userIdCol;
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
    //////////////VIEWBY OPTIONS///////////////////////
    public RadioButton viewCustomers;
    public RadioButton viewAppointments;
    public RadioButton viewApptAll;
    public RadioButton viewApptMonth;
    public RadioButton viewApptWeek;
    public VBox viewByApptBox;
    public DatePicker displayByCalendarPicker;
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
        ///////////SETTING UP APPOINTMENT TABLEVIEW///////////////////////////////
        apptTableView.setItems(DBAppointments.getAllAppointments());
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDateReadableFormat"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDateReadableFormat"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        /////////////////////////////////////////////////////////////////////////////////
    }
    ///////////////////////BUTTONS//////////////////////////////////////////////
    public void onAdd(ActionEvent actionEvent) throws IOException {
        if (Helper.viewAllCustomersToggle) {
            Helper.userClickedAddCustomer = true; //determining which button user clicked since customer page is the same for add and modify.
            Helper.goToAddCustomer(actionEvent);
        }
        else{
            Helper.userClickedAddAppointment= true; //determining which button user clicked since appointment page is the same for add and modify
            Helper.goToAddAppointment(actionEvent);
        }
    }
    public void onModify(ActionEvent actionEvent) throws IOException {
        try {
            if (Helper.viewAllCustomersToggle) {
                ///////////////////PREPARING SELECTED CUSTOMER INFORMATION///////////////////////
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                //Since Country is not an object imported from the SQL database, need to set Country of the selected customer manually
                //by matching the foreign keys to the corresponding Ids.
                int selectedCustomerProvinceId = selectedCustomer.getProvince().getProvinceId(); //getting selection province id
                int selectedCustomerCountryId = DBProvinces.selectCountryIdByProvinceId(selectedCustomerProvinceId); //using province id to get country id
                selectedCustomer.setCountry(DBCountries.selectCountryById(selectedCustomerCountryId)); //setting country of selected customer to the matching country id
                ////////////////////////////////////////////////////////////////////////////////////
                ///////////////GETTING CustomerPage CONTROLLER TO SEND INFORMATION TO NEXT SCENE///////
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainMenu.class.getResource("/view/CustomerPage.fxml"));
                loader.load();
                CustomerPage customerPageController = loader.getController();
                customerPageController.sendCustomerInformation(selectedCustomer);
                ////////////////////////////////////////////////////////////////////////////////////
                ///////////////////SETTING SCENE TO MODIFY CUSTOMER PAGE////////////////////////////
                Parent root = loader.getRoot();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Modify Customer");
                stage.setScene(new Scene(root, 600, 450));
                stage.show();
                ///////////////////////////////////////////////////////////////////////////////
            } else {
                ///////////////////////PREPARING SELECTED APPOINTMENT INFORMATION//////////////
                Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();
                //////////////////////////////////////////////////////////////////////////////////////////
                ///////////GETTING AppointmentPage CONTROLLER TO SEND INFORMATION TO NEXT SCENE/////////////
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainMenu.class.getResource("/view/AppointmentPage.fxml"));
                loader.load();
                AppointmentPage appointmentPageController = loader.getController();
                appointmentPageController.sendAppointmentInformation(selectedAppointment);
                /////////////////////////SETTING SCENE TO MODIFY APPOINTMENT PAGE//////////////////////////
                Parent root = loader.getRoot();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Modify Appointment");
                stage.setScene(new Scene(root, 600, 600));
                stage.show();
            }
        }catch(NullPointerException e){
            Helper.displayMessage("Make a selection to modify.");
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        try {
            if (Helper.viewAllCustomersToggle) {
                boolean selectedCustomerHasAppt = false;
                Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem(); //getting selected customer from tableview
                /////////////Checking to see if selected customer has an appointment////////
                for (Appointment appointments : DBAppointments.getAllAppointments()) {
                    if (appointments.getCustId() == selectedCustomer.getId()) {
                        selectedCustomerHasAppt = true;
                    }
                }
                if (!selectedCustomerHasAppt) {
                    DBCustomers.deleteCustomer((selectedCustomer.getId())); //deleting customer from the database
                    customerTableView.setItems(DBCustomers.getAllCustomers()); //updating the table view
                    Helper.displayMessage("Customer successfully removed.");
                    //selecting the next item in the list for easier deletion of multiple customers so that user does not have
                    //to select - delete for each deletion.
                    if (customerTableView.getSelectionModel().isEmpty()) {
                        customerTableView.getSelectionModel().selectFirst();
                    }
                } else {
                    Helper.displayMessage("The selected customer has appointments. Cannot delete this customer until all of their " +
                            "appointments have been deleted.");
                }

            } else if (!Helper.viewAllCustomersToggle) { //if the toggle is set to view all appointments, then the delete button deletes from appointment view table
                Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem(); //getting selected appointment from tableview
                DBAppointments.deleteAppointment(selectedAppointment.getApptId()); //deleting appointment from database
                apptTableView.setItems(DBAppointments.getAllAppointments()); //updating table view
                //selecting the next item in the list for easier deletion of multiple appointments so that user does not have
                //to select - delete for each deletion.
                if (apptTableView.getSelectionModel().isEmpty()) {
                    apptTableView.getSelectionModel().selectFirst();
                }
            }
        }catch(NullPointerException e){
            Helper.displayMessage("Make a selection to delete.");
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
        toggleWidgets(); //control flow statement within toggle widget method
    }
    public void onViewAppointments(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
    }
    public void onViewByWeek(ActionEvent actionEvent) {
        //Source used: Stackoverflow, and Oracle to find week number, temporal field object, and weekfields
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(filterApptByWeek()); //set appt table view to the filtered list
    }

    public void onViewByMonth(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(filterApptByMonth()); //set appt table view to the filtered list
    }

    public void onViewAll(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(DBAppointments.getAllAppointments()); //set appt table view to all appointments*/
    }

    public void onSelectViewByDate(ActionEvent actionEvent) {
        if(viewApptMonth.isSelected()){ //if view by month, then filter by month
            apptTableView.setItems(filterApptByMonth()); //set appt table view to the selected date and filter by month
        }
        else if (viewApptWeek.isSelected()){//else if view by week, filter by week
            apptTableView.setItems(filterApptByWeek()); //set appt table view to the selected date and filter by week
        }
        else{ //else the toggle must be set to view all
            apptTableView.setItems(DBAppointments.getAllAppointments()); //set appt table view to all appointments
        }
    }
    //////////////////////////////////////////////////////////////////////
    ///////////////////HELPER METHODS///////////////////////////////////
    /** LOIGCAL ERROR: When adding to the filtered list, appointments with the same month but different year were being
     * added to the filtered list. To correct this, I made another comparator to the selected year to verify the month selected year matches with
     * the appointment year and month*/
    public ObservableList<Appointment> filterApptByMonth(){
        //this method is used twice within onSelectViewByDate, and onViewByMonth.
        //Placed into method to reduce redundancy.
        int selectedDateYear = displayByCalendarPicker.getValue().getYear(); //getting selected year
        Month selectedDateMonth = displayByCalendarPicker.getValue().getMonth(); //getting selected month
        ObservableList<Appointment> appointmentList = DBAppointments.getAllAppointments(); //getting all appointments
        ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList(); //initializing filtered appointment list
        //iterate through all appointments and add to filtered appointment list the appointment where the start date month matches the selected month
        //and the start date year matches the selected year
        for (Appointment appointment: appointmentList){
            if(appointment.getStartDate().getMonth() == selectedDateMonth && appointment.getStartDate().getYear() == selectedDateYear){
                filteredAppointmentList.add(appointment);
            }
        }
        return filteredAppointmentList;
    }
    public ObservableList<Appointment> filterApptByWeek(){
        //this method is used twice within onSelectViewByDate, and onViewByWeek. Placed into method to reduce redundancy
        TemporalField tf = WeekFields.of(Locale.getDefault()).weekOfMonth(); //getting week of month and placing into temporal field object
        int weekNumber = displayByCalendarPicker.getValue().get(tf); //getting week number by using temporal field object as a parameter within LocalDate.now.get()

        int selectedDateYear = displayByCalendarPicker.getValue().getYear(); //getting selected year
        Month selectedDateMonth = displayByCalendarPicker.getValue().getMonth(); //getting selected month
        ObservableList<Appointment> appointmentList = DBAppointments.getAllAppointments(); //getting all appointments
        ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList(); //initializing filtered appointment list
        //iterate through all appointments and add to filtered appointment list the appointment where the start date month matches the selected month
        //the start date year matches the selected year, and the start date week matches with the selected date week
        for (Appointment appointment: appointmentList){
            if(appointment.getStartDate().getMonth() == selectedDateMonth && appointment.getStartDate().getYear() == selectedDateYear
                    && appointment.getStartDate().get(tf) == weekNumber){
                filteredAppointmentList.add(appointment);
            }
        }
        return filteredAppointmentList;
    }
    public void toggleWidgets(){
        //to reduce redundancy, made a helper toggle method to turn widgets on and off depending on
        //radio button selection within control flow statements.
        if(viewCustomers.isSelected()){
            addButton.setText("Add Customer");
            modifyButton.setText("Modify Customer");
            deleteButton.setText("Delete Customer");
            customerTableView.setOpacity(1); //making customer table visible
            customerTableView.setDisable(false); //making customer table usable
            apptTableView.setDisable(true); //making appointment table unusable
            apptTableView.setOpacity(0); //making appointment table invisible
            viewByApptBox.setOpacity(0); //making container containing radio buttons invisible
            Helper.viewAllCustomersToggle = true;
        }
        else if(viewAppointments.isSelected()){
            addButton.setText("Add Appointment");
            modifyButton.setText("Modify Appointment");
            deleteButton.setText("Delete Appointment");
            customerTableView.setOpacity(0); //making customer table invisible
            customerTableView.setDisable(true); //making customer table unusable
            apptTableView.setDisable(false); //making appointment table usable
            apptTableView.setOpacity(1); //making appointment table visible
            viewByApptBox.setOpacity(1); //making container containing radio buttons visible
            Helper.viewAllCustomersToggle = false;
        }
        if (viewApptAll.isSelected() || viewCustomers.isSelected()) { //turn off calendar widget if these options are selected
            displayByCalendarPicker.setOpacity(0); //making widget invisible
            displayByCalendarPicker.setDisable(true); //making widget unusable
        }
        else{ //all else conditions then calendar widget is on
            displayByCalendarPicker.setValue(LocalDate.now()); //setting value of calendar picker to current day
            displayByCalendarPicker.setOpacity(1); //making widget visible
            displayByCalendarPicker.setDisable(false); //making widget usable
        }
    }
    /////////////////////////////////////////////////////////////////
}
