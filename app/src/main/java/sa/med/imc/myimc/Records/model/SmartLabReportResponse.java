package sa.med.imc.myimc.Records.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmartLabReportResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_items")
    @Expose
    private String total_items;
    @SerializedName("total_pages")
    @Expose
    private String total_pages;
    @SerializedName("data")
    @Expose
    private List<SmartLabReport> labReports = null;

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

    public List<SmartLabReport> getLabReports() {
        return labReports;
    }

    public void setLabReports(List<SmartLabReport> labReports) {
        this.labReports = labReports;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }



}
