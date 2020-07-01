package com.wso2.workshop.dao;

public class OverdraughtRequest {
    private String creditApplicationReference;
    private String customerFisrtName;
    private String customerLastName;
    private String customerId;
    private String phoneNumber;
    private double requestedAmount;

    public OverdraughtRequest(String creditApplicationReference, String customerFisrtName, String customerLastName, String customerId, String phoneNumber, double requestedAmount) {
        this.creditApplicationReference = creditApplicationReference;
        this.customerFisrtName = customerFisrtName;
        this.customerLastName = customerLastName;
        this.customerId = customerId;
        this.phoneNumber = phoneNumber;
        this.requestedAmount = requestedAmount;
    }

    public String getCreditApplicationReference() {
        return creditApplicationReference;
    }

    public void setCreditApplicationReference(String creditApplicationReference) {
        this.creditApplicationReference = creditApplicationReference;
    }

    public String getCustomerFisrtName() {
        return customerFisrtName;
    }

    public void setCustomerFisrtName(String customerFisrtName) {
        this.customerFisrtName = customerFisrtName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
