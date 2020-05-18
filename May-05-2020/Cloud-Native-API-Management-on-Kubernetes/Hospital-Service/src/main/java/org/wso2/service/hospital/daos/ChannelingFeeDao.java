package org.wso2.service.hospital.daos;

/**
 * Created by nadeeshaan on 7/21/16.
 */
public class ChannelingFeeDao {
    String patientName;
    String doctorName;
    String actualFee;
//    String discountedFee;
//    String discount;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

//    public String getDiscountedFee() {
//        return discountedFee;
//    }

//    public void setDiscountedFee(String discountedFee) {
//        this.discountedFee = discountedFee;
//    }

//    public String getDiscount() {
//        return discount;
//    }

//    public void setDiscount(String discount) {
//        this.discount = discount;
//    }
}
