package com.wso2.workshop.dao;

public class CreditApprovalRequest {
    private String customerId;
    private double requestedAmount;
    private CreditType creditType;

    public CreditApprovalRequest(String customerId, double requestedAmount, CreditType creditType) {
        this.customerId = customerId;
        this.requestedAmount = requestedAmount;
        this.creditType = creditType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
}
