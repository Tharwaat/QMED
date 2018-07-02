package com.example.root.qmed;

import java.io.Serializable;

/**
 * Created by Omar on 5/16/2018.
 */

public class Request implements Serializable{
    String customerName;
    String pharmacy;
    String customerAddress;
    String customerPhone;
    String state = "stall";
    String medicine;
    String customerID;
    String userAction;

    public Request()
    {

    }

    public Request(String customerName, String pharmacy, String customerAddress, String customerPhone, String state, String medicine, String customerID, String userAction) {
        this.customerName = customerName;
        this.pharmacy = pharmacy;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.state = state;
        this.medicine = medicine;
        this.customerID = customerID;
        this.userAction = userAction;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }
}
