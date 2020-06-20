package com.wso2.workshop.dao;

public class OverdraughtResponse {
    private String creditApplicationReference;
    private String status;
    private double requestedAmount;
    private double outstanding;
    private double approvedAmount;

    public OverdraughtResponse(String creditApplicationReference, String status, double requestedAmount, double outstanding, double approvedAmount) {
        this.creditApplicationReference = creditApplicationReference;
        this.status = status;
        this.requestedAmount = requestedAmount;
        this.outstanding = outstanding;
        this.approvedAmount = approvedAmount;
    }

    public String getCreditApplicationReference() {
        return creditApplicationReference;
    }

    public void setCreditApplicationReference(String creditApplicationReference) {
        this.creditApplicationReference = creditApplicationReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
}
