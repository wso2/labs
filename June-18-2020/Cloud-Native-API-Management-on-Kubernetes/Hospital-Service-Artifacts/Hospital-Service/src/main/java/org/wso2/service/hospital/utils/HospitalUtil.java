package org.wso2.service.hospital.utils;

import org.wso2.service.hospital.daos.*;

import java.util.Calendar;

/**
 * Created by nadeeshaan on 7/20/16.
 */
public class HospitalUtil {

    public static int appointmentNumber = 1;

    public static Appointment makeNewAppointment(AppointmentRequest appointmentRequest, HospitalDAO hospitalDAO) {

        Appointment newAppointment = new Appointment();
        Doctor doctor = hospitalDAO.findDoctorByName(appointmentRequest.getDoctor());
        if (doctor == null || !doctor.getHospital().equalsIgnoreCase(appointmentRequest.getHospital())) {
            return null;
        }
        newAppointment.setAppointmentNumber(appointmentNumber++);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(appointmentRequest.getPatient());
        newAppointment.setFee(doctor.getFee());
        newAppointment.setConfirmed(false);

        return newAppointment;
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

    public static boolean checDiscountEligibility(String dob) {
        int yob = Integer.parseInt(dob.split("-")[0]);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - yob;

        if (age < 12 || age > 55) {
            return true;
        } else {
            return false;
        }
    }
}
