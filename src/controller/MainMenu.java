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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
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
        if(Helper.viewAllCustomersToggle) {
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
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Customer");
            stage.setScene(new Scene(root, 600, 450));
            stage.show();
            ///////////////////////////////////////////////////////////////////////////////
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
        displayByCalendarPicker.setValue(LocalDate.now()); //setting value of calendar picker to current day
        displayByCalendarPicker.setOpacity(1); //making widget visible
        displayByCalendarPicker.setDisable(false); //making widget usable
        int currentDateWeek = (LocalDate.now().getDayOfMonth())/7; //divide day of month by 7 to get week of month
        System.out.println(currentDateWeek);
    }

    public void onViewByMonth(ActionEvent actionEvent) {
        displayByCalendarPicker.setValue(LocalDate.now()); //setting value of calendar picker to current day
        displayByCalendarPicker.setOpacity(1); //making widget visible
        displayByCalendarPicker.setDisable(false); //making widget usable

        apptTableView.setItems(filterApptByMonth()); //set appt table view to the filtered list
    }

    public void onViewAll(ActionEvent actionEvent) {
        displayByCalendarPicker.setOpacity(0); //making widget invisible
        displayByCalendarPicker.setDisable(true); //making widget unusable
        apptTableView.setItems(DBAppointments.getAllAppointments()); //set appt table view to all appointments
    }

    public void onSelectViewByDate(ActionEvent actionEvent) {
        if(viewApptMonth.isSelected()){ //if view by month, then filter by month
            apptTableView.setItems(filterApptByMonth()); //set appt table view to the selected month
        }
        else if (viewApptWeek.isSelected()){//else if view by week, filter by week

        }
        else{ //else the toggle must be set to view all

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
}
