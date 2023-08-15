package sa.med.imc.myimc.Records.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardioReportResponse {

    public class Datum {

        @SerializedName("reportVerify")
        @Expose
        private String reportVerify;
        @SerializedName("reportDate")
        @Expose
        private String reportDate;
        @SerializedName("reportVerified")
        @Expose
        private String reportVerified;
        @SerializedName("reportName")
        @Expose
        private String reportName;
        @SerializedName("reportNumber")
        @Expose
        private Integer reportNumber;
        @SerializedName("reportStatus")
        @Expose
        private String reportStatus;
        @SerializedName("reportAttendenceid")
        @Expose
        private String reportAttendenceid;
        @SerializedName("reportExamDate")
        @Expose
        private String reportExamDate;

        public String getReportVerify() {
            return reportVerify;
        }

        public void setReportVerify(String reportVerify) {
            this.reportVerify = reportVerify;
        }

        public String getReportDate() {
            return reportDate;
        }

        public void setReportDate(String reportDate) {
            this.reportDate = reportDate;
        }

        public String getReportVerified() {
            return reportVerified;
        }

        public void setReportVerified(String reportVerified) {
            this.reportVerified = reportVerified;
        }

        public Integer getReportNumber() {
            return reportNumber;
        }

        public void setReportNumber(Integer reportNumber) {
            this.reportNumber = reportNumber;
        }

        public String getReportStatus() {
            return reportStatus;
        }

        public void setReportStatus(String reportStatus) {
            this.reportStatus = reportStatus;
        }

        public String getReportAttendenceid() {
            return reportAttendenceid;
        }

        public void setReportAttendenceid(String reportAttendenceid) {
            this.reportAttendenceid = reportAttendenceid;
        }

        public String getReportExamDate() {
            return reportExamDate;
        }

        public void setReportExamDate(String reportExamDate) {
            this.reportExamDate = reportExamDate;
        }

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }
    }


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("total_items")
    @Expose
    private String totalItems;
    @SerializedName("total_pages")
    @Expose
    private String totalPages;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }


}
