package org.wso2.service.hospital.daos;

import java.util.UUID;

/**
 * Created by nadeeshaan on 7/21/16.
 */
public class Payment {
    private String patient;
    private double actualFee;
    private int discount;
    private double discounted;
    private String paymentID;

    private String status;

    public Payment() {
        this.paymentID = "30c2f6df-a838-4d41-bb9b-2e1a3e02547d";
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public double getActualFee() {
        return actualFee;
    }

    public void setActualFee(double actualFee) {
        this.actualFee = actualFee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getDiscounted() {
        return discounted;
    }

    public void setDiscounted(double discounted) {
        this.discounted = discounted;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
