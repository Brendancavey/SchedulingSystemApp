package model;

import DAO.DBCustomers;
import DAO.DBUsers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int apptId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastUpdated;

    private int custId;
    private int userId;
    private Contact contact;

    public Appointment(int apptId, String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate, int custId, int userId, Contact contact) {
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.custId = custId;
        this.userId = userId;
        this.contact = contact;
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public String getStartDateReadableFormat(){
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a");
        return formatDateTime.format(startDate);
    }
    public String getEndDateReadableFormat(){
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a");
        return formatDateTime.format(endDate);
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Customer getCustomer(){
        return DBCustomers.selectCustomerById(this.custId);
    }
    public User getUser(){
        return DBUsers.selectUserById(this.userId);
    }
}
