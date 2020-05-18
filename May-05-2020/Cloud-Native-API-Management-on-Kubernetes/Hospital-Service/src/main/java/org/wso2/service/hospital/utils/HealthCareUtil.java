package org.wso2.service.hospital.utils;

import org.wso2.service.hospital.daos.HealthcareDao;
import org.wso2.service.hospital.daos.Payment;
import org.wso2.service.hospital.daos.PaymentSettlement;

import java.util.Calendar;

/**
 * Created by nadeeshaan on 7/21/16.
 */
public class HealthCareUtil {
    public static Payment createNewPaymentEntry(PaymentSettlement paymentSettlement) {
        Payment payment = new Payment();
        String dob = paymentSettlement.getPatient().getDob();
        int discount = checkForDiscounts(dob);
        String doctor = paymentSettlement.getDoctor().getName();
        payment.setActualFee(HealthcareDao.findDoctorByName(doctor).getFee());
        payment.setDiscount(discount);
        double discounted = (((HealthcareDao.findDoctorByName(doctor).getFee())/100)*(100-discount));
        payment.setDiscounted(discounted);
        payment.setPatient(paymentSettlement.getPatient().getName());

        return payment;
    }

    //Discount is calculated by checking the age considering the birt year only
    public static int checkForDiscounts(String dob) {
        int yob = Integer.parseInt(dob.split("-")[0]);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - yob;

        if (age < 12) {
            return 15;
        } else if (age > 55){
            return 20;
        } else {
            return 0;
        }
    }
}
