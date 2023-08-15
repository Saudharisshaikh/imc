package sa.med.imc.myimc.Records.model;

import java.util.ArrayList;

public class CadiologyReportOldModel {
    public String status;
    public String message;
    public ArrayList<Data> data;
    public String total_items;
    public String total_pages;

    public CadiologyReportOldModel() {
    }

    public CadiologyReportOldModel(String status, String message, ArrayList<Data> data, String total_items, String total_pages) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.total_items = total_items;
        this.total_pages = total_pages;
    }

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
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

    public class Data{
        public String reportVerify;
        public String reportDate;
        public String reportVerified;
        public int reportNumber;
        public String reportStatus;
        public String reportAttendenceid;
        public String reportName;
        public String reportExamDate;

        public Data() {
        }

        public Data(String reportVerify, String reportDate, String reportVerified, int reportNumber, String reportStatus, String reportAttendenceid, String reportName, String reportExamDate) {
            this.reportVerify = reportVerify;
            this.reportDate = reportDate;
            this.reportVerified = reportVerified;
            this.reportNumber = reportNumber;
            this.reportStatus = reportStatus;
            this.reportAttendenceid = reportAttendenceid;
            this.reportName = reportName;
            this.reportExamDate = reportExamDate;
        }

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

        public int getReportNumber() {
            return reportNumber;
        }

        public void setReportNumber(int reportNumber) {
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

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public String getReportExamDate() {
            return reportExamDate;
        }

        public void setReportExamDate(String reportExamDate) {
            this.reportExamDate = reportExamDate;
        }
    }
}
