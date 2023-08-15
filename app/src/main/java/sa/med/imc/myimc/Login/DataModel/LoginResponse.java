package sa.med.imc.myimc.Login.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("attempt")
    @Expose
    private String attempt;
    @SerializedName(value = "mobilenum", alternate = "mobileNum")
    @Expose
    private String mobilenum;
    @SerializedName("userId")
    @Expose
    private String userid;
    @SerializedName("otpCode")
    @Expose
    private String otpCode;
    @SerializedName("consent")
    @Expose
    private String consent;
    @SerializedName("ismultiple")
    @Expose
    private String ismultiple;
    @SerializedName("usersList")
    @Expose
    private List<UsersList> usersList = null;

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getIsmultiple() {
        return ismultiple;
    }

    public void setIsmultiple(String ismultiple) {
        this.ismultiple = ismultiple;
    }

    public List<UsersList> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UsersList> usersList) {
        this.usersList = usersList;
    }

    public class UsersList {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("fNameEn")
        @Expose
        private String fNameEn;
        @SerializedName("mNameEn")
        @Expose
        private String mNameEn;
        @SerializedName("lNameEn")
        @Expose
        private String lNameEn;
        @SerializedName("familyNameEn")
        @Expose
        private String familyNameEn;
        @SerializedName("fNameAr")
        @Expose
        private String fNameAr;
        @SerializedName("mNameAr")
        @Expose
        private String mNameAr;
        @SerializedName("lNameAr")
        @Expose
        private String lNameAr;
        @SerializedName("familyNameAr")
        @Expose
        private String familyNameAr;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("natCode")
        @Expose
        private String natCode;
        @SerializedName("nationality")
        @Expose
        private String nationality;
        @SerializedName("nationalityAr")
        @Expose
        private String nationalityAr;
        @SerializedName("maritalStatus")
        @Expose
        private String maritalStatus;
        @SerializedName("telHome")
        @Expose
        private String telHome;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("doc1Number")
        @Expose
        private String doc1Number;
        @SerializedName("doc2Number")
        @Expose
        private String doc2Number;
        @SerializedName("doc1Type")
        @Expose
        private String doc1Type;
        @SerializedName("doc2Type")
        @Expose
        private String doc2Type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFNameEn() {
            return fNameEn;
        }

        public void setFNameEn(String fNameEn) {
            this.fNameEn = fNameEn;
        }

        public String getMNameEn() {
            return mNameEn;
        }

        public void setMNameEn(String mNameEn) {
            this.mNameEn = mNameEn;
        }

        public String getLNameEn() {
            return lNameEn;
        }

        public void setLNameEn(String lNameEn) {
            this.lNameEn = lNameEn;
        }

        public String getFamilyNameEn() {
            return familyNameEn;
        }

        public void setFamilyNameEn(String familyNameEn) {
            this.familyNameEn = familyNameEn;
        }

        public String getFNameAr() {
            return fNameAr;
        }

        public void setFNameAr(String fNameAr) {
            this.fNameAr = fNameAr;
        }

        public String getMNameAr() {
            return mNameAr;
        }

        public void setMNameAr(String mNameAr) {
            this.mNameAr = mNameAr;
        }

        public String getLNameAr() {
            return lNameAr;
        }

        public void setLNameAr(String lNameAr) {
            this.lNameAr = lNameAr;
        }

        public String getFamilyNameAr() {
            return familyNameAr;
        }

        public void setFamilyNameAr(String familyNameAr) {
            this.familyNameAr = familyNameAr;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getNatCode() {
            return natCode;
        }

        public void setNatCode(String natCode) {
            this.natCode = natCode;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getNationalityAr() {
            return nationalityAr;
        }

        public void setNationalityAr(String nationalityAr) {
            this.nationalityAr = nationalityAr;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getTelHome() {
            return telHome;
        }

        public void setTelHome(String telHome) {
            this.telHome = telHome;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getDoc1Number() {
            return doc1Number;
        }

        public void setDoc1Number(String doc1Number) {
            this.doc1Number = doc1Number;
        }

        public String getDoc2Number() {
            return doc2Number;
        }

        public void setDoc2Number(String doc2Number) {
            this.doc2Number = doc2Number;
        }

        public String getDoc1Type() {
            return doc1Type;
        }

        public void setDoc1Type(String doc1Type) {
            this.doc1Type = doc1Type;
        }

        public String getDoc2Type() {
            return doc2Type;
        }

        public void setDoc2Type(String doc2Type) {
            this.doc2Type = doc2Type;
        }

    }

}
