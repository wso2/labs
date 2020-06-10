package org.wso2.service.hospital.daos;

/**
 * Created by nadeeshaan on 7/28/16.
 */
public class Status {
    String status = "";

    public Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
