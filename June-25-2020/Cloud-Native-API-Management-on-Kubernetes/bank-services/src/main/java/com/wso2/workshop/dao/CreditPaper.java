package com.wso2.workshop.dao;

import java.util.Base64;

public class CreditPaper {
    private String reference;
    private double requestedAmount;
    private CreditType creditType;
    private Customer customer;

    public CreditPaper(CreditApprovalRequest approvalRequest, Customer customer) {
        this.reference = Base64.getEncoder().encodeToString(customer.getFirstName().getBytes()).replace("=", "").toUpperCase();;
        this.requestedAmount = approvalRequest.getRequestedAmount();
        this.creditType = approvalRequest.getCreditType();
        this.customer = customer;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
