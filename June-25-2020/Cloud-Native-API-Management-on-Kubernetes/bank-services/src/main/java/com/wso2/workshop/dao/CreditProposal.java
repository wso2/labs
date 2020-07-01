package com.wso2.workshop.dao;

public class CreditProposal {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private int yearsLivedInUK;
    private double requestedLoanAmount;

    public CreditProposal(String id, String firstName, String lastName, String address, int yearsLivedInUK, double requestedLoanAmount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.yearsLivedInUK = yearsLivedInUK;
        this.requestedLoanAmount = requestedLoanAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getYearsLivedInUK() {
        return yearsLivedInUK;
    }

    public void setYearsLivedInUK(int yearsLivedInUK) {
        this.yearsLivedInUK = yearsLivedInUK;
    }

    public double getRequestedLoanAmount() {
        return requestedLoanAmount;
    }

    public void setRequestedLoanAmount(double requestedLoanAmount) {
        this.requestedLoanAmount = requestedLoanAmount;
    }

    @Override
    public String toString() {
        return "CreditProposal{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", yearsLivedInUK=" + yearsLivedInUK +
                ", requestedLoanAmount=" + requestedLoanAmount +
                '}';
    }
}
