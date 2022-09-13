package controller;

import DAO.DBContacts;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsPage implements Initializable {


    public RadioButton viewContactSchedule;
    public RadioButton viewTotalCustomers;
    public VBox viewByBox;
    public RadioButton custViewByType;
    public RadioButton custViewByMonth;
    public RadioButton custViewByCustom;
    public Label totalCustomersCount;
    public Label viewLabel;
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

        System.out.println("Reports Page initialized!");
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }
    }


    public void onSave(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);

    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Helper.goToMainMenu(actionEvent);
    }

    public void onViewContactSchedule(ActionEvent actionEvent) {
        viewByBox.setOpacity(0);
        customerTableView.setDisable(true);
        customerTableView.setOpacity(0);
        contactScheduleTableView.setOpacity(1);
        contactScheduleTableView.setDisable(false);
        viewLabel.setText("Contact");
        for (Contact contacts : DBContacts.getAllContacts()){
            optionsComboBox.getItems().add(contacts);
        }

    }

    public void onViewTotalCustomers(ActionEvent actionEvent) {
        viewByBox.setOpacity(1);
        customerTableView.setDisable(false);
        customerTableView.setOpacity(1);
        contactScheduleTableView.setOpacity(0);
        contactScheduleTableView.setDisable(true);
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().removeAll();
        if(custViewByType.isSelected()) {
            viewLabel.setText("Type");
        }
        else if(custViewByMonth.isSelected()){
            viewLabel.setText("Month");
        }
        else{
            viewLabel.setText("Custom");
        }

    }

    public void onViewByType(ActionEvent actionEvent) {
    }

    public void onViewByMonth(ActionEvent actionEvent) {
    }

    public void onViewByCustom(ActionEvent actionEvent) {
    }
}
