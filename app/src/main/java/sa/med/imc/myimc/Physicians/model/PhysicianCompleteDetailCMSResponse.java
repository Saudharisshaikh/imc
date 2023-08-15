package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PhysicianCompleteDetailCMSResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("docCode")
        @Expose
        private String docCode;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("titleAr")
        @Expose
        private String titleAr;
        @SerializedName("givenName")
        @Expose
        private String givenName;
        @SerializedName("givenNameAr")
        @Expose
        private String givenNameAr;
        @SerializedName("fathersName")
        @Expose
        private String fathersName;
        @SerializedName("fathersNameAr")
        @Expose
        private String fathersNameAr;
        @SerializedName("familyName")
        @Expose
        private String familyName;
        @SerializedName("familyNameAr")
        @Expose
        private String familyNameAr;
        @SerializedName("titleDesc")
        @Expose
        private String titleDesc;
        @SerializedName("titleDescAr")
        @Expose
        private String titleDescAr;
        @SerializedName("department_id")
        @Expose
        private Integer departmentId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("gender")
        @Expose
        private String gender;
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
        @SerializedName("publications")
        @Expose
        private String publications;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("designation_ar")
        @Expose
        private String designationAr;
        @SerializedName("docRating")
        @Expose
        private Integer docRating;
        @SerializedName("expYears")
        @Expose
        private Integer expYears;
        @SerializedName("imgUrl")
        @Expose
        private String imgUrl;
        @SerializedName("imgUrlAr")
        @Expose
        private String imgUrlAr;
        @SerializedName("isactive")
        @Expose
        private Integer isactive;
        @SerializedName("dispOnPortal")
        @Expose
        private Integer dispOnPortal;
        @SerializedName("dispOnWeb")
        @Expose
        private Integer dispOnWeb;
        @SerializedName("certificate")
        @Expose
        private String certificate;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("membership_ar")
        @Expose
        private String membershipAr;
        @SerializedName("membership_en")
        @Expose
        private String membershipEn;
        @SerializedName("licence_ar")
        @Expose
        private String licenceAr;
        @SerializedName("licence_en")
        @Expose
        private String licenceEn;
        @SerializedName("is_surgon")
        @Expose
        private String isSurgon;
        @SerializedName("is_appointment")
        @Expose
        private String isAppointment;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
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

        public String getTitleDesc() {
            return titleDesc;
        }

        public void setTitleDesc(String titleDesc) {
            this.titleDesc = titleDesc;
        }

        public String getTitleDescAr() {
            return titleDescAr;
        }

        public void setTitleDescAr(String titleDescAr) {
            this.titleDescAr = titleDescAr;
        }

        public Integer getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
            this.departmentId = departmentId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSpecialitiesTxt() {
            return specialitiesTxt;
        }

        public void setSpecialitiesTxt(String specialitiesTxt) {
            this.specialitiesTxt = specialitiesTxt;
        }

        public String getspecInstructEn() {
            return specInstructEn;
        }

        public String getspecInstructAr() {
            return specInstructAr;
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

        public String getPublications() {
            return publications;
        }

        public void setPublications(String publications) {
            this.publications = publications;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDesignationAr() {
            return designationAr;
        }

        public void setDesignationAr(String designationAr) {
            this.designationAr = designationAr;
        }

        public Integer getDocRating() {
            return docRating;
        }

        public void setDocRating(Integer docRating) {
            this.docRating = docRating;
        }

        public Integer getExpYears() {
            return expYears;
        }

        public void setExpYears(Integer expYears) {
            this.expYears = expYears;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgUrlAr() {
            return imgUrlAr;
        }

        public void setImgUrlAr(String imgUrlAr) {
            this.imgUrlAr = imgUrlAr;
        }

        public Integer getIsactive() {
            return isactive;
        }

        public void setIsactive(Integer isactive) {
            this.isactive = isactive;
        }

        public Integer getDispOnPortal() {
            return dispOnPortal;
        }

        public void setDispOnPortal(Integer dispOnPortal) {
            this.dispOnPortal = dispOnPortal;
        }

        public Integer getDispOnWeb() {
            return dispOnWeb;
        }

        public void setDispOnWeb(Integer dispOnWeb) {
            this.dispOnWeb = dispOnWeb;
        }

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getMembershipAr() {
            return membershipAr;
        }

        public void setMembershipAr(String membershipAr) {
            this.membershipAr = membershipAr;
        }

        public String getLicenceAr() {
            return licenceAr;
        }

        public void setLicenceAr(String licenceAr) {
            this.licenceAr = licenceAr;
        }

        public String getLicenceEn() {
            return licenceEn;
        }

        public void setLicenceEn(String licenceEn) {
            this.licenceEn = licenceEn;
        }

        public String getIsSurgon() {
            return isSurgon;
        }

        public void setIsSurgon(String isSurgon) {
            this.isSurgon = isSurgon;
        }

        public String getIsAppointment() {
            return isAppointment;
        }

        public void setIsAppointment(String isAppointment) {
            this.isAppointment = isAppointment;
        }

        public String getMembershipEn() {
            return membershipEn;
        }

        public void setMembershipEn(String membershipEn) {
            this.membershipEn = membershipEn;
        }
    }

}