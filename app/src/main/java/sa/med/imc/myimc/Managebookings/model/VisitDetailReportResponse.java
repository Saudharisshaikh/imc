package sa.med.imc.myimc.Managebookings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitDetailReportResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("labReportsCount")
    @Expose
    private Integer labReportsCount;
    @SerializedName("radioLogyReportsCount")
    @Expose
    private Integer radioLogyReportsCount;
    @SerializedName("sickReportsCount")
    @Expose
    private Integer sickReportsCount;
    @SerializedName("prescriptionCount")
    @Expose
    private Integer prescriptionCount;

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

    public Integer getLabReportsCount() {
        return labReportsCount;
    }

    public void setLabReportsCount(Integer labReportsCount) {
        this.labReportsCount = labReportsCount;
    }

    public Integer getRadioLogyReportsCount() {
        return radioLogyReportsCount;
    }

    public void setRadioLogyReportsCount(Integer radioLogyReportsCount) {
        this.radioLogyReportsCount = radioLogyReportsCount;
    }

    public Integer getSickReportsCount() {
        return sickReportsCount;
    }

    public void setSickReportsCount(Integer sickReportsCount) {
        this.sickReportsCount = sickReportsCount;
    }

    public Integer getPrescriptionCount() {
        return prescriptionCount;
    }

    public void setPrescriptionCount(Integer prescriptionCount) {
        this.prescriptionCount = prescriptionCount;
    }
}
