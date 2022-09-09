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
        countryBox.setItems(DBCountries.getAllCountries());
       // divisionBox.setItems((DBProvinces.getAllProvinces()));
        System.out.println("Login page initialized!");
    }


    public void onSave(ActionEvent actionEvent) throws IOException {
        String name = nameText.getText();
        String address = addressText.getText();
        String postalCode = postalCodeText.getText();
        String phoneNumber = phoneNumberText.getText();
        //Country country = countryBox.getValue();
        Province province = divisionBox.getValue();
        int provinceId = province.getProvinceId();
        DBCustomers.insertCustomer(name, address, postalCode, phoneNumber, provinceId); //inserting customer into database

        Helper.goToMainMenu(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onProvinceSelection(ActionEvent actionEvent) {
            //divisionBox.setItems(DBProvinces.getAllProvinces());
    }

    public void onCountrySelection(ActionEvent actionEvent) {
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
    }
}
