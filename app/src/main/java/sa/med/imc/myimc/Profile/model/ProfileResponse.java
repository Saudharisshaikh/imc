package sa.med.imc.myimc.Profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;

public class ProfileResponse implements Serializable {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("dependants")
    @Expose
    private List<ValidateResponse> depandant;

    @SerializedName("tcsLoginResponse")
    @Expose
    private ValidateResponse userDetails;

    @SerializedName("patientProfile")
    @Expose
    private PatientProfile patientProfile;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidateResponse getUserDetails() {
        return userDetails;
    }

    /*public void setUserDetails(ValidateResponse userDetails) {
        this = userDetails;
    }*/

    public PatientProfile getPatientProfile() {
        return patientProfile;
    }

    public void setPatientProfile(PatientProfile patientProfile) {
        this.patientProfile = patientProfile;
    }

    public List<ValidateResponse> getDepandant() {
        return depandant;
    }

    public void setDepandant(List<ValidateResponse> depandant) {
        this.depandant = depandant;
    }
}
