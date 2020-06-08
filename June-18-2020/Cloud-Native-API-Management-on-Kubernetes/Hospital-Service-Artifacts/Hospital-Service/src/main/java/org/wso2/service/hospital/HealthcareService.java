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

import com.google.gson.Gson;
import org.wso2.service.hospital.daos.*;
import org.wso2.service.hospital.utils.HealthCareUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0.0-SNAPSHOT
 */
@Path("/healthcare")
public class HealthcareService {

    public HealthcareService() {
        fillCategories();
        HealthcareDao.doctorsList.add((new Doctor("thomas collins", "grand oak community hospital", "surgery", "9.00 a.m - 11.00 a.m", 7000)));
        HealthcareDao.doctorsList.add((new Doctor("henry parker", "grand oak community hospital", "ent", "9.00 a.m - 11.00 a.m", 4500)));
        HealthcareDao.doctorsList.add((new Doctor("abner jones", "grand oak community hospital", "gynaecology", "8.00 a.m - 10.00 a.m", 11000)));
        HealthcareDao.doctorsList.add((new Doctor("abner jones", "grand oak community hospital", "ent", "8.00 a.m - 10.00 a.m", 6750)));
        HealthcareDao.doctorsList.add((new Doctor("anne clement", "clemency medical center", "surgery", "8.00 a.m - 10.00 a.m", 12000)));
        HealthcareDao.doctorsList.add((new Doctor("thomas kirk", "clemency medical center", "gynaecology", "9.00 a.m - 11.00 a.m", 8000)));
        HealthcareDao.doctorsList.add((new Doctor("cailen cooper", "clemency medical center", "paediatric", "9.00 a.m - 11.00 a.m", 5500)));
        HealthcareDao.doctorsList.add((new Doctor("seth mears", "pine valley community hospital", "surgery", "3.00 p.m - 5.00 p.m", 8000)));
        HealthcareDao.doctorsList.add((new Doctor("emeline fulton", "pine valley community hospital", "cardiology", "8.00 a.m - 10.00 a.m", 4000)));
        HealthcareDao.doctorsList.add((new Doctor("jared morris", "willow gardens general hospital", "cardiology", "9.00 a.m - 11.00 a.m", 10000)));
        HealthcareDao.doctorsList.add((new Doctor("henry foster", "willow gardens general hospital", "paediatric", "8.00 a.m - 10.00 a.m", 10000)));
    }

    @GET
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorsByCategory(@PathParam("category") String category) {
        List<Doctor> stock = HealthcareDao.findDoctorByCategory(category);
        Gson gson = new Gson();
        String jsonResponse;

        if (stock != null && stock.size() > 0) {
            return Response.status(Response.Status.OK) .entity(stock).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status = new Status("Could not find any entry for the requested Category");
            return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/payments")
    public Response settlePayment(PaymentSettlement paymentSettlement) {
        //Check for the appointment number validity
        Gson gson = new Gson();
        if (paymentSettlement.getAppointmentNumber() >= 0) {
            Payment payment = HealthCareUtil.createNewPaymentEntry(paymentSettlement);
            payment.setStatus("Settled");
            HealthcareDao.payments.put(payment.getPaymentID(), payment);
            return Response.status(Response.Status.OK) .entity(payment).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status = new Status("Error.Could not Find the Requested appointment ID");
            return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/payments")
    public Response getAllPayments() {
        HashMap payments = HealthcareDao.payments;
        return Response.status(Response.Status.OK) .entity(payments).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/payments/payment/{payment_id}")
    public Response getPaymentDetails(@PathParam("payment_id") String payment_id) {
        Gson gson = new Gson();
        Payment payment = HealthcareDao.payments.get(payment_id);
        String jsonResponse;

        if (payment != null) {
            return Response.status(Response.Status.OK) .entity(payment).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status = new Status("Invalid payment id provided");
            return Response.status(Response.Status.OK) .entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/admin/newdoctor")
    public Response addNewDoctor(Doctor doctor) {
        String category = doctor.getCategory();
        if (!HealthcareDao.catergories.contains(category)) {
            HealthcareDao.catergories.add(category);
        }
        if (this.findDoctorByName(doctor.getName()) == null) {
            HealthcareDao.doctorsList.add(doctor);
            Status status =new Status("New Doctor Added Successfully");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        } else {
            Status status =new Status("Doctor Already Exist in the system");
            return Response.status(Response.Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
        }
    }

    public void fillCategories() {
        HealthcareDao.catergories.add("surgery");
        HealthcareDao.catergories.add("cardiology");
        HealthcareDao.catergories.add("gynaecology");
        HealthcareDao.catergories.add("ent");
        HealthcareDao.catergories.add("paediatric");
    }

    private Doctor findDoctorByName(String name) {
        for (Doctor doctor: HealthcareDao.doctorsList) {
            if (doctor.getName().equals(name)) {
                return doctor;
            }
        }
        return null;
    }
}
