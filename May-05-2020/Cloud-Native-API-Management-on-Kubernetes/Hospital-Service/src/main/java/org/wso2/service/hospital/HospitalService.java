/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.service.hospital;

import org.wso2.service.hospital.daos.Appointment;
import org.wso2.service.hospital.daos.AppointmentRequest;
import org.wso2.service.hospital.daos.ChannelingFeeDao;
import org.wso2.service.hospital.daos.Doctor;
import org.wso2.service.hospital.daos.HospitalDAO;
import org.wso2.service.hospital.daos.Patient;
import org.wso2.service.hospital.daos.PatientRecord;
import org.wso2.service.hospital.daos.Status;
import org.wso2.service.hospital.utils.HospitalUtil;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class HospitalService {

    private Map<Integer, Appointment> appointments = new HashMap<>();
    private HospitalDAO hospitalDAO = new HospitalDAO();
    List<String> catergories = hospitalDAO.getCatergories();
    List<Doctor> doctorsList = hospitalDAO.getDoctorsList();


    public HospitalService() {
        catergories.add("surgery");
        catergories.add("cardiology");
        catergories.add("gynaecology");
        catergories.add("ent");
        catergories.add("paediatric");
    }

    public Response reserveAppointment(AppointmentRequest appointmentRequest, @PathParam("category") String category) {

        // Check whether the requested category available
        if (hospitalDAO.getCatergories().contains(category)) {
            Appointment appointment = HospitalUtil.makeNewAppointment(appointmentRequest, this.hospitalDAO);

            if (appointment == null) {
                Status status = new Status("Doctor "+ appointmentRequest.getDoctor() + " is not available in " +
                        appointmentRequest.getHospital());
                return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
            }

            this.appointments.put(appointment.getAppointmentNumber(), appointment);
            hospitalDAO.getPatientMap().put(appointmentRequest.getPatient().getSsn(), appointmentRequest.getPatient());
            if (!hospitalDAO.getPatientRecordMap().containsKey(appointmentRequest.getPatient().getSsn())) {
                PatientRecord patientRecord = new PatientRecord(appointmentRequest.getPatient());
                hospitalDAO.getPatientRecordMap().put(appointmentRequest.getPatient().getSsn(), patientRecord);
            }

            return Response.status(Response.Status.OK) .entity(appointment).type(MediaType.APPLICATION_JSON).build();
        } else {
            // Cannot find a doctor for this category
            Status status = new Status("Invalid Category");
            return Response.ok(status, MediaType.APPLICATION_JSON).build();
        }
    }

    public Response checkChannellingFee(@PathParam("appointment_id") int id) {
        //Check for the appointment number validity
        ChannelingFeeDao channelingFee = new ChannelingFeeDao();
        if (appointments.containsKey(id)) {
            Patient patient = appointments.get(id).getPatient();
            Doctor doctor = appointments.get(id).getDoctor();

            channelingFee.setActualFee(Double.toString(doctor.getFee()));
            channelingFee.setDoctorName(doctor.getName().toLowerCase());
            channelingFee.setPatientName(patient.getName().toLowerCase());

            return Response.status(Response.Status.OK) .entity(channelingFee).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status = new Status("Error.Could not Find the Requested appointment ID");
            return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response updatePatientRecord(HashMap<String,Object> patientDetails) {
        String SSN = (String)patientDetails.get("SSN");
        List symptoms = (List)patientDetails.get("symptoms");
        List treatments = (List)patientDetails.get("treatments");

        if (hospitalDAO.getPatientMap().get(SSN) != null) {
            Patient patient = hospitalDAO.getPatientMap().get(SSN);
            PatientRecord patientRecord = hospitalDAO.getPatientRecordMap().get(SSN);
            if (patient != null) {
                patientRecord.updateSymptoms(symptoms);
                patientRecord.updateTreatments(treatments);
                Status status =new Status("Record Update Success");
                return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
            } else {
                Status status =new Status("Could not find valid Patient Record");
                return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
            }
        } else {
            Status status =new Status("Could not find valid Patient Entry");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response getPatientRecord(@PathParam("SSN") String SSN) {
        PatientRecord patientRecord = hospitalDAO.getPatientRecordMap().get(SSN);

        if (patientRecord == null) {
            Status status =new Status("Could not find valid Patient Entry");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.OK).entity(patientRecord).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response isEligibleForDiscount(@PathParam("appointment_id") int id) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            Status status =new Status("Invalid appointment ID");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        } else {
            boolean eligible = HospitalUtil.checDiscountEligibility(appointment.getPatient().getDob());
            Status status = new Status(String.valueOf(eligible));
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public Response addNewDoctor(Doctor doctor) {
        String category = doctor.getCategory();
        if (!catergories.contains(category)) {
            catergories.add(category);
        }
        if (this.hospitalDAO.findDoctorByName(doctor.getName()) == null) {
            this.doctorsList.add(doctor);
            Status status =new Status("New Doctor Added Successfully");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status =new Status("Doctor Already Exist in the system");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
