package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PhysicianResponse implements Serializable {


    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("providerList")
    private List<ProviderList> providerList;
    @Expose
    @SerializedName("specialityCode")
    private String specialityCode;
    @Expose
    @SerializedName("specialityDescription")
    private String specialityDescription;

    @Expose
    @SerializedName("arabicSpecialityDescription")
    private String arabicSpecialityDescription;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProviderList> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<ProviderList> providerList) {
        this.providerList = providerList;
    }

    public String getArabicSpecialityDescription() {
        return arabicSpecialityDescription;
    }

    public void setArabicSpecialityDescription(String arabicSpecialityDescription) {
        this.arabicSpecialityDescription = arabicSpecialityDescription;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getSpecialityDescription() {
        return specialityDescription;
    }

    public void setSpecialityDescription(String specialityDescription) {
        this.specialityDescription = specialityDescription;
    }

    public static class ProviderList implements Serializable {
        @Expose
        @SerializedName("providerDescription")
        private String providerDescription;
        @Expose
        @SerializedName(value = "arabicProviderDescription", alternate = "arabic_provider_description")
        private String arabicProviderDescription;
        @Expose
        @SerializedName("providerCode")
        private String providerCode;

        @Expose
        @SerializedName("isPatientPortalDoctor")
        private boolean isPatientPortalDoctor;

        public String getArabicProviderDescription() {
            String s = arabicProviderDescription;

            try {
                if (arabicProviderDescription.trim().isEmpty()) {
                    s = providerDescription;
                }
            } catch (Exception e) {
                s = providerDescription;
            }


            return s;
        }

        public void setArabicProviderDescription(String arabicProviderDescription) {
            this.arabicProviderDescription = arabicProviderDescription;
        }

        public String getProviderDescription() {
            return providerDescription;
        }

        public boolean isPatientPortalDoctor() {
            return isPatientPortalDoctor;
        }

        public void setPatientPortalDoctor(boolean patientPortalDoctor) {
            isPatientPortalDoctor = patientPortalDoctor;
        }

        public void setProviderDescription(String providerDescription) {
            this.providerDescription = providerDescription;
        }

        public String getProviderCode() {
            return providerCode;
        }

        public void setProviderCode(String providerCode) {
            this.providerCode = providerCode;
        }
    }


    //    @SerializedName("status")
//    @Expose
//    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("no_show_status")
    @Expose
    private String no_show_status;

    @SerializedName("total_items")
    @Expose
    private String total_items;

    @SerializedName("physicians")
    @Expose
    private List<Physician> physicians = null;

    /*public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Physician> getPhysicians() {
        return physicians;
    }

    public void setPhysicians(List<Physician> physicians) {
        this.physicians = physicians;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }


    public String getNo_show_status() {
        return no_show_status;
    }

    public void setNo_show_status(String no_show_status) {
        this.no_show_status = no_show_status;
    }

    public class Physician implements Serializable {

        @SerializedName("docCode")
        @Expose
        private String docCode;
        @SerializedName("hosp")
        @Expose
        private String hosp_ID;
        @SerializedName("hrId")
        @Expose
        private String hrId;
        @SerializedName("givenName")
        @Expose
        private String givenName;
        @SerializedName("givenNameAr")
        @Expose
        private String givenNameAr;
        @SerializedName("familyName")
        @Expose
        private String familyName;
        @SerializedName("familyNameAr")
        @Expose
        private String familyNameAr;
        @SerializedName("fathersName")
        @Expose
        private String fathersName;
        @SerializedName("fathersNameAr")
        @Expose
        private String fathersNameAr;
        @SerializedName("isActive")
        @Expose
        private String isActive;
        @SerializedName("deptCode")
        @Expose
        private String deptCode;
        @SerializedName("clinicCode")
        @Expose
        private String clinicCode;
        @SerializedName("clinicNameEn")
        @Expose
        private String clinicNameEn;
        @SerializedName("clincNameAr")
        @Expose
        private String clincNameAr;
        @SerializedName("deptNameEn")
        @Expose
        private String deptNameEn;
        @SerializedName("deptNameAr")
        @Expose
        private String deptNameAr;
        @SerializedName("docImg")
        @Expose
        private String docImg;
        @SerializedName("docRating")
        @Expose
        private String docRating;
        @SerializedName("specialitiesTxt")
        @Expose
        private String specialitiesTxt;
        @SerializedName("specialitiesTxtAr")
        @Expose
        private String specialitiesTxtAr;



        @SerializedName("specInstructEn")
        @Expose
        private String specInstructEn;
        @SerializedName("specInstructAr")
        @Expose
        private String specInstructAr;




        @SerializedName("teleHealthEligibleFlag")
        @Expose
        private String teleHealthEligibleFlag;
        @SerializedName("service")
        @Expose
        private Service service;

        private String date;
        private String time;
        private String sessionId;

        private NextTimeResponse.Datum session;

        public NextTimeResponse.Datum getSession() {
            return session;
        }

        public void setSession(NextTimeResponse.Datum session) {
            this.session = session;
        }

        public String getHosp_ID() {
            return hosp_ID;
        }

        public void setHosp_ID(String hosp_ID) {
            this.hosp_ID = hosp_ID;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getTeleHealthEligibleFlag() {
            if (teleHealthEligibleFlag == null)
                teleHealthEligibleFlag = "0";
            return teleHealthEligibleFlag;
        }

        public void setTeleHealthEligibleFlag(String teleHealthEligibleFlag) {
            this.teleHealthEligibleFlag = teleHealthEligibleFlag;
        }

        public String getSpecialitiesTxt() {
            return specialitiesTxt;
        }

        public void setSpecialitiesTxt(String specialitiesTxt) {
            this.specialitiesTxt = specialitiesTxt;
        }

        public String getSpecialitiesTxtAr() {
            return specialitiesTxtAr;
        }

        public void setSpecialitiesTxtAr(String specialitiesTxtAr) {
            this.specialitiesTxtAr = specialitiesTxtAr;
        }

        public String getSpecInstructEn() {
            return specInstructEn;
        }

        public void setSpecInstructEn(String specialitiesTxt) {
            this.specInstructEn = specInstructEn;
        }

        public String getSpecInstructAr() {
            return specInstructAr;
        }

        public void setSpecInstructTxtAr(String specInstructAr) {
            this.specInstructAr = specInstructAr;
        }





        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getHrId() {
            return hrId;
        }

        public void setHrId(String hrId) {
            this.hrId = hrId;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getGivenNameAr() {
            if (givenNameAr == null)
                givenNameAr = "";
            return givenNameAr;
        }

        public void setGivenNameAr(String givenNameAr) {
            this.givenNameAr = givenNameAr;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getFamilyNameAr() {
            if (familyNameAr == null)
                familyNameAr = "";
            return familyNameAr;
        }

        public void setFamilyNameAr(String familyNameAr) {
            this.familyNameAr = familyNameAr;
        }

        public String getFathersName() {
            return fathersName;
        }

        public void setFathersName(String fathersName) {
            this.fathersName = fathersName;
        }

        public String getFathersNameAr() {
            return fathersNameAr;
        }

        public void setFathersNameAr(String fathersNameAr) {
            this.fathersNameAr = fathersNameAr;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getClinicCode() {
            return clinicCode;
        }

        public void setClinicCode(String clinicCode) {
            this.clinicCode = clinicCode;
        }

        public String getClinicNameEn() {
            return clinicNameEn;
        }

        public void setClinicNameEn(String clinicNameEn) {
            this.clinicNameEn = clinicNameEn;
        }

        public String getClincNameAr() {
            return clincNameAr;
        }

        public void setClincNameAr(String clincNameAr) {
            this.clincNameAr = clincNameAr;
        }

        public String getDeptNameEn() {
            return deptNameEn;
        }

        public void setDeptNameEn(String deptNameEn) {
            this.deptNameEn = deptNameEn;
        }

        public String getDeptNameAr() {
            return deptNameAr;
        }

        public void setDeptNameAr(String deptNameAr) {
            this.deptNameAr = deptNameAr;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public String getDocImg() {
            return docImg;
        }

        public void setDocImg(String docImg) {
            this.docImg = docImg;
        }

        public String getDocRating() {
            return docRating;
        }

        public void setDocRating(String docRating) {
            this.docRating = docRating;
        }
    }

    public class Service implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("descAr")
        @Expose
        private String descAr;
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

        public String getDescAr() {
            return descAr;
        }

        public void setDescAr(String descAr) {
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
