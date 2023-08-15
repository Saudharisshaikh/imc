package sa.med.imc.myimc.Physicians.model;

import java.util.List;

public class DoctorModel {
    String doctor_id;
    String doctor_code;
    String doctor_description;
    String doctor_date_from;
    String doctor_date_to;
    String doctor_speciality_code;
    String doctor_speciality_description;
    String status;
    List<LicenseList> license_list;
    List<LocationList> location_list;

    public DoctorModel() {
    }

    public DoctorModel(String doctor_id, String doctor_code, String doctor_description, String doctor_date_from, String doctor_date_to, String doctor_speciality_code, String doctor_speciality_description, String status, List<LicenseList> license_list, List<LocationList> location_list) {
        this.doctor_id = doctor_id;
        this.doctor_code = doctor_code;
        this.doctor_description = doctor_description;
        this.doctor_date_from = doctor_date_from;
        this.doctor_date_to = doctor_date_to;
        this.doctor_speciality_code = doctor_speciality_code;
        this.doctor_speciality_description = doctor_speciality_description;
        this.status = status;
        this.license_list = license_list;
        this.location_list = location_list;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_code() {
        return doctor_code;
    }

    public void setDoctor_code(String doctor_code) {
        this.doctor_code = doctor_code;
    }

    public String getDoctor_description() {
        return doctor_description;
    }

    public void setDoctor_description(String doctor_description) {
        this.doctor_description = doctor_description;
    }

    public String getDoctor_date_from() {
        return doctor_date_from;
    }

    public void setDoctor_date_from(String doctor_date_from) {
        this.doctor_date_from = doctor_date_from;
    }

    public String getDoctor_date_to() {
        return doctor_date_to;
    }

    public void setDoctor_date_to(String doctor_date_to) {
        this.doctor_date_to = doctor_date_to;
    }

    public String getDoctor_speciality_code() {
        return doctor_speciality_code;
    }

    public void setDoctor_speciality_code(String doctor_speciality_code) {
        this.doctor_speciality_code = doctor_speciality_code;
    }

    public String getDoctor_speciality_description() {
        return doctor_speciality_description;
    }

    public void setDoctor_speciality_description(String doctor_speciality_description) {
        this.doctor_speciality_description = doctor_speciality_description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LicenseList> getLicense_list() {
        return license_list;
    }

    public void setLicense_list(List<LicenseList> license_list) {
        this.license_list = license_list;
    }

    public List<LocationList> getLocation_list() {
        return location_list;
    }

    public void setLocation_list(List<LocationList> location_list) {
        this.location_list = location_list;
    }
}
