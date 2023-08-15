package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionDatesResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bookingTime")
    @Expose
    private Integer bookingTime;

    @SerializedName("data")
    @Expose
    private List<SessionDate> sessionDates = null;

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

    public List<SessionDate> getSessionDates() {
        return sessionDates;
    }

    public void setSessionDates(List<SessionDate> sessionDates) {
        this.sessionDates = sessionDates;
    }
    public Integer getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Integer bookingTime) {
        this.bookingTime = bookingTime;
    }
    public class SessionDate {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;
        @SerializedName("clinicCode")
        @Expose
        private String clinicCode;
        @SerializedName("day")
        @Expose
        private String day;
        @SerializedName("docCode")
        @Expose
        private String docCode;
        @SerializedName("deptCode")
        @Expose
        private String deptCode;
        @SerializedName("serviceCode")
        @Expose
        private String serviceCode;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("sessionType")
        @Expose
        private Integer sessionType;

        public Integer getSessionType() {
            return sessionType;
        }

        public void setSessionType(Integer sessionType) {
            this.sessionType = sessionType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getClinicCode() {
            return clinicCode;
        }

        public void setClinicCode(String clinicCode) {
            this.clinicCode = clinicCode;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


    }


}
