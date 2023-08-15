package sa.med.imc.myimc.Records.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SickLeave {
    public String document_id;
    public String clinic_code;
    public String clinic_description;
    public String authorized_user;
    public String authorized_date;
    public String authorized_time;
    public String document_status;

    public SickLeave() {
    }

    public SickLeave(String document_id, String clinic_code, String clinic_description, String authorized_user, String authorized_date, String authorized_time, String document_status) {
        this.document_id = document_id;
        this.clinic_code = clinic_code;
        this.clinic_description = clinic_description;
        this.authorized_user = authorized_user;
        this.authorized_date = authorized_date;
        this.authorized_time = authorized_time;
        this.document_status = document_status;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getClinic_code() {
        return clinic_code;
    }

    public void setClinic_code(String clinic_code) {
        this.clinic_code = clinic_code;
    }

    public String getClinic_description() {
        return clinic_description;
    }

    public void setClinic_description(String clinic_description) {
        this.clinic_description = clinic_description;
    }

    public String getAuthorized_user() {
        return authorized_user;
    }

    public void setAuthorized_user(String authorized_user) {
        this.authorized_user = authorized_user;
    }

    public String getAuthorized_date() {
        return authorized_date;
    }

    public void setAuthorized_date(String authorized_date) {
        this.authorized_date = authorized_date;
    }

    public String getAuthorized_time() {
        return authorized_time;
    }

    public void setAuthorized_time(String authorized_time) {
        this.authorized_time = authorized_time;
    }

    public String getDocument_status() {
        return document_status;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }
}
