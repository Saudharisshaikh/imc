package sa.med.imc.myimc.Records.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class LabReport {
    public String order_id;
    public String order_description;
    public String department_code;
    public String ordered_doctor;
    public String order_status;
    public String collected_date;
    public String collected_time;
    public String labepisode_number;
    public String specimen_number;
    public String authorizing_user;
    public String result_date;
    public String result_time;
    public String order_accession_number;
    public boolean smart_report;


    public LabReport() {
    }


    public LabReport(String order_id, String order_description, String department_code, String ordered_doctor, String order_status, String collected_date, String collected_time, String labepisode_number, String specimen_number, String authorizing_user, String result_date, String result_time, String order_accession_number) {
        this.order_id = order_id;
        this.order_description = order_description;
        this.department_code = department_code;
        this.ordered_doctor = ordered_doctor;
        this.order_status = order_status;
        this.collected_date = collected_date;
        this.collected_time = collected_time;
        this.labepisode_number = labepisode_number;
        this.specimen_number = specimen_number;
        this.authorizing_user = authorizing_user;
        this.result_date = result_date;
        this.result_time = result_time;
        this.order_accession_number = order_accession_number;
    }

    public String getOrder_accession_number() {
        return order_accession_number;
    }

    public boolean isSmart_report() {
        return smart_report;
    }

    public void setSmart_report(boolean smart_report) {
        this.smart_report = smart_report;
    }

    public void setOrder_accession_number(String order_accession_number) {
        this.order_accession_number = order_accession_number;
    }

    public String getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(String department_code) {
        this.department_code = department_code;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public String getOrdered_doctor() {
        return ordered_doctor;
    }

    public void setOrdered_doctor(String ordered_doctor) {
        this.ordered_doctor = ordered_doctor;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCollected_date() {
        return collected_date;
    }

    public void setCollected_date(String collected_date) {
        this.collected_date = collected_date;
    }

    public String getCollected_time() {
        return collected_time;
    }

    public void setCollected_time(String collected_time) {
        this.collected_time = collected_time;
    }

    public String getLabepisode_number() {
        return labepisode_number;
    }

    public void setLabepisode_number(String labepisode_number) {
        this.labepisode_number = labepisode_number;
    }

    public String getSpecimen_number() {
        return specimen_number;
    }

    public void setSpecimen_number(String specimen_number) {
        this.specimen_number = specimen_number;
    }

    public String getAuthorizing_user() {
        return authorizing_user;
    }

    public void setAuthorizing_user(String authorizing_user) {
        this.authorizing_user = authorizing_user;
    }

    public String getResult_date() {
        return result_date;
    }

    public void setResult_date(String result_date) {
        this.result_date = result_date;
    }

    public String getResult_time() {
        return result_time;
    }

    public void setResult_time(String result_time) {
        this.result_time = result_time;
    }
}
