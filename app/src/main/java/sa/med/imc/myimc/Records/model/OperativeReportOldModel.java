package sa.med.imc.myimc.Records.model;

import java.util.ArrayList;

public class OperativeReportOldModel {
    public String status;
    public String message;
    public ArrayList<Data> data;
    public String total_items;
    public String total_pages;

    public OperativeReportOldModel() {
    }

    public OperativeReportOldModel(String status, String message, ArrayList<Data> data, String total_items, String total_pages) {
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
        public int id;
        public String patId;
        public String status;
        public String createdBy;
        public String createdDate;
        public String verifyBy;
        public String verifyDate;
        public String reportType;

        public Data() {
        }

        public Data(int id, String patId, String status, String createdBy, String createdDate, String verifyBy, String verifyDate, String reportType) {
            this.id = id;
            this.patId = patId;
            this.status = status;
            this.createdBy = createdBy;
            this.createdDate = createdDate;
            this.verifyBy = verifyBy;
            this.verifyDate = verifyDate;
            this.reportType = reportType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getVerifyBy() {
            return verifyBy;
        }

        public void setVerifyBy(String verifyBy) {
            this.verifyBy = verifyBy;
        }

        public String getVerifyDate() {
            return verifyDate;
        }

        public void setVerifyDate(String verifyDate) {
            this.verifyDate = verifyDate;
        }

        public String getReportType() {
            return reportType;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }
    }
}
