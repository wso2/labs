package com.wso2.workshop.dao;

public class BureauResponse {
    private String creditProposalId;
    private String date;
    private int creditScore;
    private double requestedAmount;
    private double likelyApprovedAmount;
    private String status;

    public BureauResponse(String creditProposalId, String date, int creditScore, double requestedAmount, double likelyApprovedAmount, String status) {
        this.creditProposalId = creditProposalId;
        this.date = date;
        this.creditScore = creditScore;
        this.requestedAmount = requestedAmount;
        this.likelyApprovedAmount = likelyApprovedAmount;
        this.status = status;
    }

    public String getCreditProposalId() {
        return creditProposalId;
    }

    public void setCreditProposalId(String creditProposalId) {
        this.creditProposalId = creditProposalId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public double getLikelyApprovedAmount() {
        return likelyApprovedAmount;
    }

    public void setLikelyApprovedAmount(double likelyApprovedAmount) {
        this.likelyApprovedAmount = likelyApprovedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
