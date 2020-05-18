package org.wso2.service.hospital;

import org.wso2.service.hospital.daos.AppointmentRequest;
import org.wso2.service.hospital.daos.Doctor;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Created by nadeeshaan on 8/1/16.
 */

@Path("/pinevalley/categories")
public class PineValleyHospitalService extends HospitalService {

    public PineValleyHospitalService() {
        super();
        doctorsList.add((new Doctor("seth mears", "pine valley community hospital", "surgery", "3.00 p.m - 5.00 p.m", 8000)));
        doctorsList.add((new Doctor("emeline fulton", "pine valley community hospital", "cardiology", "8.00 a.m - 10.00 a.m", 4000)));
    }

    @POST
    @Path("/{category}/reserve")
    public Response reserveAppointment(AppointmentRequest appointmentRequest, @PathParam("category") String category) {
        return super.reserveAppointment(appointmentRequest, category);
    }

    @GET
    @Path("/appointments/{appointment_id}/fee")
    public Response checkChannellingFee(@PathParam("appointment_id") int id) {
        return super.checkChannellingFee(id);
    }

    @POST
    @Path("/patient/updaterecord")
    public Response updatePatientRecord(HashMap<String,Object> patientDetails) {
        return super.updatePatientRecord(patientDetails);
    }

    @GET
    @Path("/patient/{SSN}/getrecord")
    public Response getPatientRecord(@PathParam("SSN") String SSN) {
        return super.getPatientRecord(SSN);
    }

    @GET
    @Path("/patient/appointment/{appointment_id}/discount")
    public Response isEligibleForDiscount(@PathParam("appointment_id") int id) {
        return super.isEligibleForDiscount(id);
    }

    @POST
    @Path("/admin/newdoctor")
    public Response addNewDoctor(Doctor doctor) {
        return super.addNewDoctor(doctor);
    }
}
