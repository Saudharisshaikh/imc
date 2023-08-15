package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DoctorAdapterModel implements Serializable {
    @Expose
    @SerializedName("physician_name")
    private String physician_name;

    @Expose
    @SerializedName("physician_speciality")
    private String physician_speciality;

    @Expose
    @SerializedName("clinic_service")
    private String clinic_service;

    public String getPhysician_name() {
        return physician_name;
    }

    public void setPhysician_name(String physician_name) {
        this.physician_name = physician_name;
    }

    public String getPhysician_speciality() {
        return physician_speciality;
    }

    public void setPhysician_speciality(String physician_speciality) {
        this.physician_speciality = physician_speciality;
    }

    public String getClinic_service() {
        return clinic_service;
    }

    public void setClinic_service(String clinic_service) {
        this.clinic_service = clinic_service;
    }
}
