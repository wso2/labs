package org.wso2.service.hospital.daos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nadeeshaan on 7/21/16.
 */
public class PatientRecord {
    private Patient patient;
    private HashMap<String,List<String>> symptoms = new HashMap<>();
    private HashMap<String,List<String>> treatments = new HashMap<>();

    public PatientRecord(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public HashMap<String, List<String>> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(HashMap<String, List<String>> symptoms) {
        this.symptoms = symptoms;
    }

    public HashMap<String, List<String>> getTreatments() {
        return treatments;
    }

    public void setTreatments(HashMap<String, List<String>> treatments) {
        this.treatments = treatments;
    }

    public void updateTreatments(List<String> treatments) {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.treatments.put(date,treatments);
    }
    public void updateSymptoms(List<String> symptoms) {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.symptoms.put(date,symptoms);
    }
}
