package controller;

import DAO.DBCountries;
import DAO.DBProvinces;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.Province;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerPage implements Initializable {


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
        Helper.goToMainMenu(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onProvinceSelection(ActionEvent actionEvent) {
            //divisionBox.setItems(DBProvinces.getAllProvinces());
    }

    public void onCountrySelection(ActionEvent actionEvent) {
        //Country selectedCountry = countryBox.getSelectionModel().getSelectedItem();
        if (countryBox.getValue().getId() == 1) {
            divisionBox.setItems(DBProvinces.selectProvince(1));
        }
        else if(countryBox.getValue().getId() == 2 ){
            divisionBox.setItems(DBProvinces.selectProvince(2));
        }
        else if(countryBox.getValue().getId() == 3 ){
            divisionBox.setItems(DBProvinces.selectProvince(3));
        }
    }
}
