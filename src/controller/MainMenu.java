package controller;

import DAO.DBAppointments;
import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBProvinces;
import interfaces.Loader;
import interfaces.Message;
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
    public TableColumn<Customer, String> custCountry;
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
    public static Appointment selectedAppointment;
    public Label titleLabel;
    public Label timeZoneLabel;
    ////////////////////////////////////////////////////
    /////////////INITIALIZING INTERFACE MESSAGE TO BE USED FOR LAMBDA/////////////
    Message m = (alert, message) -> {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    };

    /** This is the initialize method.
     * This method gets called when first starting this scene. It checks for the
     * locale default if it set to a supported language in the resource bundle, and changes
     * the appropriate labels and text fields to the supported language. It also checks on a
     * a global variable to see if the user selected to view customers or view appointments and sets
     * the radio button to the appropriate option. It also initializes all table views with appropriate values.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //////////////////CHECKING FOR FRENCH TRANSLATION/////////
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText(rb.getString("AppointmentSchedule"));
            addButton.setText(rb.getString("AddAppointment"));
            modifyButton.setText(rb.getString("ModifyAppointment"));
            deleteButton.setText(rb.getString("DeleteAppointment"));
            timeZoneLabel.setText(rb.getString("YourTimeZone"));
            reportsButton.setText(rb.getString("Reports"));
            logoutButton.setText(rb.getString("Logout"));
            viewAppointments.setText(rb.getString("ViewAppointments"));
            viewCustomers.setText(rb.getString("ViewCustomers"));
            viewApptAll.setText(rb.getString("ViewAll"));
            viewApptMonth.setText(rb.getString("ViewByMonth"));
            viewApptWeek.setText(rb.getString("ViewByWeek"));
            apptIdCol.setText(rb.getString("ApptId"));
            titleCol.setText(rb.getString("Title"));
            descriptionCol.setText(rb.getString("Description"));
            locationCol.setText(rb.getString("Location"));
            contactCol.setText(rb.getString("Contact"));
            typeCol.setText(rb.getString("Type"));
            startDateCol.setText(rb.getString("Start"));
            endDateCol.setText(rb.getString("End"));
            custIdCol.setText(rb.getString("CustomerId"));
            userIdCol.setText(rb.getString("UserId"));
            custId.setText(rb.getString("CustomerId"));
            custName.setText(rb.getString("Name"));
            custAddress.setText(rb.getString("Address"));
            custPostalCode.setText(rb.getString("PostalCode"));
            custPhoneNumber.setText(rb.getString("PhoneNumber"));
            custCreateDate.setText(rb.getString("CreatedDate"));
            custCreateBy.setText(rb.getString("CreatedBy"));
            custLastUpdate.setText(rb.getString("LastUpdate"));
            custLastUpdateBy.setText(rb.getString("LastUpdatedBy"));
            custProvince.setText(rb.getString("Province"));

        }
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
        custCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
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
    /** This is the onAdd method.
     * This method checks to see if a global variable is set to view all customers or to view appointments.
     *  Depending on which option the global variable is set to, the on add button sends the user to the appropriate page,
     *  and toggles the appropriate booleans.
     *  @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
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
    /** This is the onModify method.
     * This method checks to see if a global variable is set to view all customers or to view appointments. This method then
     * gets the appropriate controller and uses the sendInformation method from that controller to send the selected
     * option (appointment/customer) information to the next scene to modify.  Depending on which option the global variable is set to,
     * the on modify button sends the user to the appropriate page.
     *  LAMBDA USE: A lambda is used within this method to display an error message. The use of lambda to display a window with
     *      * an error message will be more succinct. It will not require that I declare a new Alert type object everytime I require it, and I can set the warning message
     *      * to the alertType for each scenario. Initialization of the interface is at line 89.
     * LAMBDA USE 2: Another lambda is used within this method to load the controller of another controller scene. This allows for the interface Loader to be used for different
     * scenarios and different scenes with different controllers. It allows for the code to be more succinct without the requirements of initializing
     * a new fxml loader, and instead use the lambda expression that calls the loader by the specified name. In this case, customerPage, and appointmentPage - which allows for better
     * readability of code and what the segment of code does.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
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
                Loader customerPage = (loader, resource) ->{
                  loader.setLocation(MainMenu.class.getResource(resource));
                  loader.load();
                  CustomerPage customerPageController = loader.getController();
                  customerPageController.sendCustomerInformation(selectedCustomer);

                  Parent root = loader.getRoot();
                  Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                  stage.setTitle("Modify Customer");
                  stage.setScene(new Scene(root, 600, 450));
                  stage.show();
                };
                customerPage.loadPage(new FXMLLoader(), "/view/CustomerPage.fxml");
                /*FXMLLoader loader = new FXMLLoader();
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
                stage.show();*/
                ///////////////////////////////////////////////////////////////////////////////
            } else {
                Helper.userClickedModifyAppointment = true;
                ///////////////////////PREPARING SELECTED APPOINTMENT INFORMATION//////////////
                selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();
                //////////////////////////////////////////////////////////////////////////////////////////
                ///////////GETTING AppointmentPage CONTROLLER TO SEND INFORMATION TO NEXT SCENE/////////////
                Loader appointmentPage = (loader, resource) ->{
                    loader.setLocation(MainMenu.class.getResource(resource));
                    loader.load();
                    AppointmentPage appointmentPageController = loader.getController();
                    appointmentPageController.sendAppointmentInformation(selectedAppointment);
                    appointmentPageController.updateStartTimes(); //initializing start time combo box
                    appointmentPageController.updateEndTimes(); //initializing end time combo box

                    Parent root = loader.getRoot();
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Modify Appointment");
                    stage.setScene(new Scene(root, 600, 600));
                    stage.show();
                };
                appointmentPage.loadPage(new FXMLLoader(), "/view/AppointmentPage.fxml");
                /*FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainMenu.class.getResource("/view/AppointmentPage.fxml"));
                loader.load();
                AppointmentPage appointmentPageController = loader.getController();
                appointmentPageController.sendAppointmentInformation(selectedAppointment);
                appointmentPageController.updateStartTimes(); //initializing start time combo box
                appointmentPageController.updateEndTimes(); //initializing end time combo box
                /////////////////////////SETTING SCENE TO MODIFY APPOINTMENT PAGE//////////////////////////
                Parent root = loader.getRoot();
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Modify Appointment");
                stage.setScene(new Scene(root, 600, 600));
                stage.show();*/
            }
        }catch(NullPointerException e){
            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("fr")) {
                m.displayMessage(new Alert(Alert.AlertType.NONE), rb.getString("MakeASelectionToModify"));
                //Helper.displayMessage(rb.getString("MakeASelectionToModify"));
            }
            else{
                m.displayMessage(new Alert(Alert.AlertType.NONE), "Make a selection to modify.");
                //Helper.displayMessage("Make a selection to modify.");
            }
        }
    }
    /** This is the onDelete method.
     * This method checks to see if a global variable is set to view all customers or to view appointments. Depending on
     * which option the global variable is set to, the delete button deletes the appropriate selection upon pressing the
     * delete button. Additionally, if a customer is chosen to be deleted, a message will display to signal to the user
     * that the customer cannot be deleted if the customer is associated with an appointment and the corresponding appointment must be
     * deleted first.
     * LAMBDA USE: A lambda is used within this method to display an error message. The use of lambda to display a window with
     * an error message will be more succinct. It will not require that I declare a new Alert type object everytime I require it, and I can set the warning message
     * to the alertType for each scenario. Initialization of the interface is at line 89.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
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
                    ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                    if(Locale.getDefault().getLanguage().equals("fr")) {
                        Helper.displayMessage(rb.getString("CustomerSuccessfullyDeleted"));
                    }
                    else{

                        Helper.displayMessage("Customer successfully removed.");
                    }
                    //selecting the next item in the list for easier deletion of multiple customers so that user does not have
                    //to select - delete for each deletion.
                    if (customerTableView.getSelectionModel().isEmpty()) {
                        customerTableView.getSelectionModel().selectFirst();
                    }
                } else {
                    ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                    if(Locale.getDefault().getLanguage().equals("fr")) {
                        Helper.displayMessage(rb.getString("CannotDeleteUnlessNoAppointment"));
                    }
                    else{
                        Helper.displayMessage("The selected customer has appointments. Cannot delete this customer until all of their " +
                                "appointments have been deleted.");
                    }
                }

            } else if (!Helper.viewAllCustomersToggle) { //if the toggle is set to view all appointments, then the delete button deletes from appointment view table
                Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem(); //getting selected appointment from tableview
                DBAppointments.deleteAppointment(selectedAppointment.getApptId()); //deleting appointment from database
                ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
                if(Locale.getDefault().getLanguage().equals("fr")) {
                    Helper.displayMessage(selectedAppointment.getApptId() + " | " + selectedAppointment.getType() + ": " + rb.getString("AppointmentSuccessfullyDeleted"));
                }
                else{
                    Helper.displayMessage("Appointment " + selectedAppointment.getApptId() + " with type: " + selectedAppointment.getType() + " was successfully deleted.");
                }
                apptTableView.setItems(DBAppointments.getAllAppointments()); //updating table view
                //selecting the next item in the list for easier deletion of multiple appointments so that user does not have
                //to select - delete for each deletion.
                if (apptTableView.getSelectionModel().isEmpty()) {
                    apptTableView.getSelectionModel().selectFirst();
                }
            }
        }catch(NullPointerException e){

            ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("fr")) {
                m.displayMessage(new Alert(Alert.AlertType.NONE), rb.getString("MakeASelectionToDelete"));
                //Helper.displayMessage(rb.getString("MakeASelectionToDelete"));
            }
            else{
                m.displayMessage(new Alert(Alert.AlertType.NONE), "Make a selection to delete.");
                //Helper.displayMessage("Make a selection to delete.");
            }
        }
    }
    /** This is the onReport Method.
     * Takes the user to the reports page.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onReport(ActionEvent actionEvent) throws IOException{
        Helper.goToReportsPage(actionEvent);
    }
    /** This is the onLogout method.
     * Takes the user to the logout page.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onLogout(ActionEvent actionEvent) throws IOException{
        Helper.goToLogin(actionEvent);
    }
    //////////////////////////////////////////////////////////////////////////
    /////////////////////////RADIO BUTTONS///////////////////////////////////
    /** This is the onViewAllCustomers method.
     * This method corresponds to a radio button and when pressed, calls a helper method
     * called toggleWidgets. ToggleWidgets is a method containing appropriate control flow statements
     * to check for various conditions. Please see toggleWidgets
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onViewAllCustomers(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
    }
    /** This is the onViewAppointments method.
     * This method corresponds to a radio button and when pressed, calls a helper method
     * called toggleWidgets. ToggleWidgets is a method containing appropriate control flow statements
     * to check for various conditions. Please see toggleWidgets
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onViewAppointments(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
    }
    /** This is the onViewByWeek method.
     * This method corresponds to a radio button and when pressed, calls a helper method
     * called toggleWidgets. ToggleWidgets is a method containing appropriate control flow statements
     * to check for various conditions. Please see toggleWidgets. Additionally, this method sets the appointment
     * table view to only list appointments by week. See filterByWeek method.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onViewByWeek(ActionEvent actionEvent) {
        //Source used: Stackoverflow, and Oracle to find week number, temporal field object, and weekfields
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(filterApptByWeek()); //set appt table view to the filtered list
    }
    /** This is the onViewByMonth method.
     * This method corresponds to a radio button and when pressed, calls a helper method
     * called toggleWidgets. ToggleWidgets is a method containing appropriate control flow statements
     * to check for various conditions. Please see toggleWidgets. Additionally, this method sets the appointment
     * table view to only list appointments by month. See filterByMonth method.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.*/
    public void onViewByMonth(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(filterApptByMonth()); //set appt table view to the filtered list
    }
    /** This is the onViewAll method.
     * This method corresponds to a radio button and when pressed, calls a helper method
     * called toggleWidgets. ToggleWidgets is a method containing appropriate control flow statements
     * to check for various conditions. Please see toggleWidgets. Additionally, this method sets the appointment
     * table view to view all appointments.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
    public void onViewAll(ActionEvent actionEvent) {
        toggleWidgets(); //control flow statement within toggle widget method
        apptTableView.setItems(DBAppointments.getAllAppointments()); //set appt table view to all appointments*/
    }
    /** This is the onSelectViewByDate method.
     * This method corresponds to a date picker and is only visible to the user when the user selects to view appointments,
     * as mentioned in toggleWidgets method. This method uses control flow statements to check for which radio button
     * is selected to view by and sets the appointment table view to the appropriate filter.
     * @param actionEvent Method takes in an action event that gets triggered when the user clicks on the corresponding button.
     * */
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
    /** This is the filterApptByMonth method.
     * This is a helper method that gets called to reduce redundancy across multiple methods. This method
     * gets the date and year from the display calendar picker when the user makes a selection on the appropriate calendar widget.
     * It then iterates through the appointment lists and makes a comparison within a control flow statement to see if the appointment start date
     * matches the selected month and year and adds it to a filtered appointment list.
     * LOIGCAL ERROR: When adding to the filtered list, appointments with the same month but different year were being
     * added to the filtered list. To correct this, I made another comparator to the selected year to verify the month selected year matches with
     * the appointment year and month.
     * @return filteredAppointmentList This method returns a list containing all filtered appointments by the selected month.
     * */
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
    /** This is the filterApptByWeek method.
     * This is a helper method that gets called to reduce redundancy across multiple methods. This method
     * gets the date and year from the display calendar picker when the user makes a selection on the appropriate calendar widget. This method also calculates
     * an integer called weekNumber by using a TemporalField object to get the weekOfMonth, and using it to get the value on the selected date.
     * It then iterates through the appointment lists and makes a comparison within a control flow statement to see if the appointment start date
     * matches the selected weekNumber, month, and year, and adds it to a filtered appointment list.
     * @return filteredAppointmentList This method returns a list containing all filtered appointments by the selected week of month.
     * */
    public ObservableList<Appointment> filterApptByWeek(){
        //this method is used twice within onSelectViewByDate, and onViewByWeek. Placed into method to reduce redundancy
        TemporalField tf = WeekFields.of(Locale.getDefault()).weekOfMonth(); //getting week of month and placing into temporal field object
        int weekNumber = displayByCalendarPicker.getValue().get(tf); //getting week number by using temporal field object as a parameter within displayCalendarPicker.getValue().get()

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
    /** This is the toggleWidgets method.
     * This method helps with the readability of the code without having other programmers guess on the reasoning behind disabling/enabling,
     * and setting opacity to make labels to be visible/invisible. This method primarily serves as a control flow statement to check
     * which radio buttons are selected, and changing the visibility/functionality of the appropriate labels/widgets.
     * */
    public void toggleWidgets(){
        //to reduce redundancy, made a helper toggle method to turn widgets on and off depending on
        //radio button selection within control flow statements.
        ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Nat", Locale.getDefault());

        if(viewCustomers.isSelected()){
            if(Locale.getDefault().getLanguage().equals("fr")) {
                addButton.setText(rb.getString("AddCustomer"));
                modifyButton.setText(rb.getString("ModifyCustomer"));
                deleteButton.setText(rb.getString("DeleteCustomer"));
            }
            else {
                addButton.setText("Add Customer");
                modifyButton.setText("Modify Customer");
                deleteButton.setText("Delete Customer");
            }
            customerTableView.setOpacity(1); //making customer table visible
            customerTableView.setDisable(false); //making customer table usable
            apptTableView.setDisable(true); //making appointment table unusable
            apptTableView.setOpacity(0); //making appointment table invisible
            viewByApptBox.setOpacity(0); //making container containing radio buttons invisible
            Helper.viewAllCustomersToggle = true;
        }
        else if(viewAppointments.isSelected()){
            if(Locale.getDefault().getLanguage().equals("fr")) {
                addButton.setText(rb.getString("AddAppointment"));
                modifyButton.setText(rb.getString("ModifyAppointment"));
                deleteButton.setText(rb.getString("DeleteAppointment"));
            }
            else {
                addButton.setText("Add Appointment");
                modifyButton.setText("Modify Appointment");
                deleteButton.setText("Delete Appointment");
            }
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
    /** This is the getSelectedAppointment Method.
     * This method is used to get the user selection appointment and is primarily used within the modify appointment
     * scene. This method gets called to check to see if the selectedAppointment start and end times will be overlapping with
     * the modified appointment start and end times. Since the user would like like to modify the appointment time, overlapping the previous
     * start and end times should not be present. Please see checkForConflict() method within AppointmentPage.java for more details.*/
    public static Appointment getSelectedAppointment(){
        return selectedAppointment;
    }
    /////////////////////////////////////////////////////////////////
}
