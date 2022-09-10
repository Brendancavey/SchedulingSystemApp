package controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBProvinces;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.Customer;
import model.Province;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerPage implements Initializable {

    public TextField idText;
    public TextField nameText;
    public TextField addressText;
    public TextField postalCodeText;
    public TextField phoneNumberText;
    public ComboBox<Country> countryBox;
    public ComboBox<Province> divisionBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setting countryBox to show all countries
        countryBox.setItems(DBCountries.getAllCountries());

        System.out.println("Customer page initialized!");
    }
    public void onSave(ActionEvent actionEvent) throws IOException {
        String name = nameText.getText();
        String address = addressText.getText();
        String postalCode = postalCodeText.getText();
        String phoneNumber = phoneNumberText.getText();
        //since country is not an object imported from the SQL Server and we're not saving customer objects into a data structure in the program,
        //country information is obtained from using select query from province id at the main menu under onModify. - starting currently from line 113.
        //Country country = countryBox.getValue();
        Province province = divisionBox.getValue();
        int provinceId = province.getProvinceId();
        if (Helper.userClickedAddCustomer) { //if user clicked on add Customer from the main menu page, then insert customer into data base
            DBCustomers.insertCustomer(name, address, postalCode, phoneNumber, provinceId); //inserting customer into database
        }
        else{ //else the user must have clicked on modify customer, therefore userClickedAddCustomer is false
            int customerId = Integer.valueOf(idText.getText());
            DBCustomers.updateCustomer(customerId, name, address, postalCode, phoneNumber, provinceId); //updating customer
        }
        //whatever the result of userClickedAddCustomer, set value back to false (default)
        Helper.userClickedAddCustomer = false;
        Helper.goToMainMenu(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onProvinceSelection(ActionEvent actionEvent) {
        //divisionBox.setItems(DBProvinces.getAllProvinces());
    }
    /** LOGICAL ERROR: When selecting a country, all provinces were still showing in the province drop down menu. To
     * correct this, I made a control flow statement to check for which provinces belonged to the selected country,
     * and set the drop down menu to only display those provinces.*/
    public void onCountrySelection(ActionEvent actionEvent) {
        //on country selection, make sure to selectFirst. Since the control flow statement
        //comes after selectFirst, then selectingFirst is empty. Using clear() was not working
        divisionBox.getSelectionModel().selectFirst();
        //showing only provinces within their region
        if (countryBox.getValue().getId() == 1) {
            divisionBox.setItems(DBProvinces.selectProvinceByCountryId(1));
        }
        else if(countryBox.getValue().getId() == 2 ){
            divisionBox.setItems(DBProvinces.selectProvinceByCountryId(2));
        }
        else if(countryBox.getValue().getId() == 3 ){
            divisionBox.setItems(DBProvinces.selectProvinceByCountryId(3));
        }
    }
    ///////////////////////////////////////////////////////////////
    ////////////////////HELPER METHODS FOR CUSTOMER PAGE///////////
    public void sendCustomerInformation(Customer customer){
        idText.setText(String.valueOf(customer.getId()));
        nameText.setText(customer.getName());
        addressText.setText(customer.getAddress());
        postalCodeText.setText(customer.getPostalCode());
        phoneNumberText.setText(customer.getPhoneNumber());
        countryBox.getSelectionModel().select(customer.getCountry());
        divisionBox.getSelectionModel().select(customer.getProvince());

        /////////////////ON MODIFY CUSTOMER/////////////////////////////
        //Since country box is preSelected, need to fire onCountrySelection to display all provinces when attempting to
        //modify province to mimic as if a country was selected.
        countryBox.fireEvent(new ActionEvent());
        ////////////////////////////////////////////////////////////////
    }
}
