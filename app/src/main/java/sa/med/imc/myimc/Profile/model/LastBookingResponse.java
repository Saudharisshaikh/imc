package sa.med.imc.myimc.Profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastBookingResponse {

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("patId")
        @Expose
        private String patId;
        @SerializedName("clinicDescEn")
        @Expose
        private String clinicDescEn;
        @SerializedName("clinicDescAr")
        @Expose
        private String clinicDescAr;
        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("apptType")
        @Expose
        private String apptType;
        @SerializedName("sessionId")
        @Expose
        private Integer sessionId;
        @SerializedName("docCode")
        @Expose
        private String docCode;
        @SerializedName("clinicCode")
        @Expose
        private String clinicCode;
        @SerializedName("apptDate")
        @Expose
        private String apptDate;
        @SerializedName("sysCode")
        @Expose
        private String sysCode;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("givenName")
        @Expose
        private String givenName;
        @SerializedName("fathersName")
        @Expose
        private Object fathersName;
        @SerializedName("familyName")
        @Expose
        private String familyName;
        @SerializedName("givenNameAr")
        @Expose
        private String givenNameAr;
        @SerializedName("fatherNameAr")
        @Expose
        private Object fatherNameAr;
        @SerializedName("familyNameAr")
        @Expose
        private String familyNameAr;
        @SerializedName("bookingStatus")
        @Expose
        private Integer bookingStatus;
        @SerializedName("service")
        @Expose
        private Service service;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getClinicDescEn() {
            return clinicDescEn;
        }

        public void setClinicDescEn(String clinicDescEn) {
            this.clinicDescEn = clinicDescEn;
        }

        public String getClinicDescAr() {
            return clinicDescAr;
        }

        public void setClinicDescAr(String clinicDescAr) {
            this.clinicDescAr = clinicDescAr;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getApptType() {
            return apptType;
        }

        public void setApptType(String apptType) {
            this.apptType = apptType;
        }

        public Integer getSessionId() {
            return sessionId;
        }

        public void setSessionId(Integer sessionId) {
            this.sessionId = sessionId;
        }

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getClinicCode() {
            return clinicCode;
        }

        public void setClinicCode(String clinicCode) {
            this.clinicCode = clinicCode;
        }

        public String getApptDate() {
            return apptDate;
        }

        public void setApptDate(String apptDate) {
            this.apptDate = apptDate;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public Object getFathersName() {
            return fathersName;
        }

        public void setFathersName(Object fathersName) {
            this.fathersName = fathersName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getGivenNameAr() {
            return givenNameAr;
        }

        public void setGivenNameAr(String givenNameAr) {
            this.givenNameAr = givenNameAr;
        }

        public Object getFatherNameAr() {
            return fatherNameAr;
        }

        public void setFatherNameAr(Object fatherNameAr) {
            this.fatherNameAr = fatherNameAr;
        }

        public String getFamilyNameAr() {
            return familyNameAr;
        }

        public void setFamilyNameAr(String familyNameAr) {
            this.familyNameAr = familyNameAr;
        }

        public Integer getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(Integer bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("noShowStatus")
    @Expose
    private String noShowStatus;
    @SerializedName("data")
    @Expose
    private Data data;



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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getNoShowStatus() {
        return noShowStatus;
    }

    public void setNoShowStatus(String noShowStatus) {
        this.noShowStatus = noShowStatus;
    }

    public class Service {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("descAr")
        @Expose
        private Object descAr;
        @SerializedName("deptCode")
        @Expose
        private String deptCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Object getDescAr() {
            return descAr;
        }

        public void setDescAr(Object descAr) {
            this.descAr = descAr;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

    }
}
