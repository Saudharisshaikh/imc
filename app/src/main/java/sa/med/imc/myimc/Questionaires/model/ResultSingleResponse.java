package sa.med.imc.myimc.Questionaires.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultSingleResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("formName")
    @Expose
    private String formName;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("bookigId")
    @Expose
    private String bookigId;
    @SerializedName("bookingdate")
    @Expose
    private String bookingdate;
    @SerializedName("completedDate")
    @Expose
    private String completedDate;
    @SerializedName("physicianName")
    @Expose
    private String physicianName;
    @SerializedName("physicianNameAr")
    @Expose
    private String physicianNameAr;

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

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBookigId() {
        return bookigId;
    }

    public void setBookigId(String bookigId) {
        this.bookigId = bookigId;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getPhysicianName() {
        return physicianName;
    }

    public void setPhysicianName(String physicianName) {
        this.physicianName = physicianName;
    }

    public String getPhysicianNameAr() {
        return physicianNameAr;
    }

    public void setPhysicianNameAr(String physicianNameAr) {
        this.physicianNameAr = physicianNameAr;
    }


}
