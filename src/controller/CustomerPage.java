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
        divisionBox.setItems((DBProvinces.getAllProvinces()));
        System.out.println("Login page initialized!");
    }


    public void onSave(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onProvinceSelection(ActionEvent actionEvent) {
        /*Country selectedCountry = (Country)countryBox.getSelectionModel().getSelectedItem();
        if(selectedCountry.getId() == 1){
            DivisionBox.setItems(DBProvinces.getAllProvinces());
        }
        else{
            DivisionBox.setItems(null);
        }*/
    }
}
