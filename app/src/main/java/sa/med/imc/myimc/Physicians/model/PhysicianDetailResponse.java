package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhysicianDetailResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("physicianData")
    @Expose
    private PhysicianData physicianData;
    @SerializedName("no_show_status")
    @Expose
    private String no_show_status;

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

    public PhysicianData getPhysicianData() {
        return physicianData;
    }

    public void setPhysicianData(PhysicianData physicianData) {
        this.physicianData = physicianData;
    }

    public String getNo_show_status() {
        return no_show_status;
    }

    public void setNo_show_status(String no_show_status) {
        this.no_show_status = no_show_status;
    }

    public class PhysicianData implements Serializable {

        @SerializedName("docCode")
        @Expose
        private String docCode;
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
        @SerializedName("docRating")
        @Expose
        private String docRating;
        @SerializedName("docImg")
        @Expose
        private String docImg;
        @SerializedName("service")
        @Expose
        private Service service;
        @SerializedName("specialitiesTxt")
        @Expose
        private String specialitiesTxt;
        @SerializedName("specialitiesTxtAr")
        @Expose
        private String specialitiesTxtAr;
        @SerializedName("educationTxt")
        @Expose
        private String educationTxt;
        @SerializedName("educationTxtAr")
        @Expose
        private String educationTxtAr;
        @SerializedName("affilationsTxt")
        @Expose
        private String affilationsTxt;
        @SerializedName("affilationsTxtAr")
        @Expose
        private String affilationsTxtAr;
        @SerializedName("researchTxt")
        @Expose
        private String researchTxt;
        @SerializedName("researchTxtAr")
        @Expose
        private String researchTxtAr;
        @SerializedName("languagesTxt")
        @Expose
        private String languagesTxt;
        @SerializedName("languagesTxtAr")
        @Expose
        private String languagesTxtAr;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("teleHealthEligibleFlag")
        @Expose
        private int teleHealthEligibleFlag;

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getGivenNameAr() {
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

        public String getDocRating() {
            return docRating;
        }

        public void setDocRating(String docRating) {
            this.docRating = docRating;
        }

        public String getDocImg() {
            return docImg;
        }

        public void setDocImg(String docImg) {
            this.docImg = docImg;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
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

        public String getEducationTxt() {
            return educationTxt;
        }

        public void setEducationTxt(String educationTxt) {
            this.educationTxt = educationTxt;
        }

        public String getEducationTxtAr() {
            return educationTxtAr;
        }

        public void setEducationTxtAr(String educationTxtAr) {
            this.educationTxtAr = educationTxtAr;
        }

        public String getAffilationsTxt() {
            return affilationsTxt;
        }

        public void setAffilationsTxt(String affilationsTxt) {
            this.affilationsTxt = affilationsTxt;
        }

        public String getAffilationsTxtAr() {
            return affilationsTxtAr;
        }

        public void setAffilationsTxtAr(String affilationsTxtAr) {
            this.affilationsTxtAr = affilationsTxtAr;
        }

        public String getResearchTxt() {
            return researchTxt;
        }

        public void setResearchTxt(String researchTxt) {
            this.researchTxt = researchTxt;
        }

        public String getResearchTxtAr() {
            return researchTxtAr;
        }

        public void setResearchTxtAr(String researchTxtAr) {
            this.researchTxtAr = researchTxtAr;
        }

        public String getLanguagesTxt() {
            return languagesTxt;
        }

        public void setLanguagesTxt(String languagesTxt) {
            this.languagesTxt = languagesTxt;
        }

        public String getLanguagesTxtAr() {
            return languagesTxtAr;
        }

        public void setLanguagesTxtAr(String languagesTxtAr) {
            this.languagesTxtAr = languagesTxtAr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getTeleHealthEligibleFlag() {
            return teleHealthEligibleFlag;
        }

        public void setTeleHealthEligibleFlag(int teleHealthEligibleFlag) {
            this.teleHealthEligibleFlag = teleHealthEligibleFlag;
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
