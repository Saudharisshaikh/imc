package sa.med.imc.myimc.SignUp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("extPatientInfo")
    @Expose
    private ExtPatientInfo extPatientInfo;

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

    public ExtPatientInfo getExtPatientInfo() {
        return extPatientInfo;
    }

    public void setExtPatientInfo(ExtPatientInfo extPatientInfo) {
        this.extPatientInfo = extPatientInfo;
    }


    public class ExtPatientInfo {

        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("nationality")
        @Expose
        private String nationality;
        @SerializedName("maritalStatus")
        @Expose
        private String maritalStatus;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("firstNameAr")
        @Expose
        private String firstNameAr;
        @SerializedName("idType")
        @Expose
        private String idType;
        @SerializedName("secondNameEn")
        @Expose
        private String secondNameEn;
        @SerializedName("dateOfBirth")
        @Expose
        private String dateOfBirth;
        @SerializedName("thirdNameEn")
        @Expose
        private String thirdNameEn;
        @SerializedName("lastNameEn")
        @Expose
        private String lastNameEn;
        @SerializedName("occupation")
        @Expose
        private Object occupation;
        @SerializedName("patientStatus")
        @Expose
        private String patientStatus;
        @SerializedName("healthId")
        @Expose
        private String healthId;
        @SerializedName("secondNameAr")
        @Expose
        private String secondNameAr;
        @SerializedName("thirdNameAr")
        @Expose
        private String thirdNameAr;
        @SerializedName("lastNameAr")
        @Expose
        private String lastNameAr;
        @SerializedName("firstNameEn")
        @Expose
        private String firstNameEn;
        @SerializedName("placeOfBirth")
        @Expose
        private String placeOfBirth;
        @SerializedName("idNumber")
        @Expose
        private String idNumber;
        @SerializedName("maritalStatusCode")
        @Expose
        private String maritalStatusCode;
        @SerializedName("nationalityCode")
        @Expose
        private String nationalityCode;
        @SerializedName("clientIdentifierId")
        @Expose
        private Object clientIdentifierId;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getFirstNameAr() {
            return firstNameAr;
        }

        public void setFirstNameAr(String firstNameAr) {
            this.firstNameAr = firstNameAr;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getSecondNameEn() {
            return secondNameEn;
        }

        public void setSecondNameEn(String secondNameEn) {
            this.secondNameEn = secondNameEn;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getThirdNameEn() {
            return thirdNameEn;
        }

        public void setThirdNameEn(String thirdNameEn) {
            this.thirdNameEn = thirdNameEn;
        }

        public String getLastNameEn() {
            return lastNameEn;
        }

        public void setLastNameEn(String lastNameEn) {
            this.lastNameEn = lastNameEn;
        }

        public Object getOccupation() {
            return occupation;
        }

        public void setOccupation(Object occupation) {
            this.occupation = occupation;
        }

        public String getPatientStatus() {
            return patientStatus;
        }

        public void setPatientStatus(String patientStatus) {
            this.patientStatus = patientStatus;
        }

        public String getHealthId() {
            return healthId;
        }

        public void setHealthId(String healthId) {
            this.healthId = healthId;
        }

        public String getSecondNameAr() {
            return secondNameAr;
        }

        public void setSecondNameAr(String secondNameAr) {
            this.secondNameAr = secondNameAr;
        }

        public String getThirdNameAr() {
            return thirdNameAr;
        }

        public void setThirdNameAr(String thirdNameAr) {
            this.thirdNameAr = thirdNameAr;
        }

        public String getLastNameAr() {
            return lastNameAr;
        }

        public void setLastNameAr(String lastNameAr) {
            this.lastNameAr = lastNameAr;
        }

        public String getFirstNameEn() {
            return firstNameEn;
        }

        public void setFirstNameEn(String firstNameEn) {
            this.firstNameEn = firstNameEn;
        }

        public String getPlaceOfBirth() {
            return placeOfBirth;
        }

        public void setPlaceOfBirth(String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getMaritalStatusCode() {
            return maritalStatusCode;
        }

        public void setMaritalStatusCode(String maritalStatusCode) {
            this.maritalStatusCode = maritalStatusCode;
        }

        public String getNationalityCode() {
            return nationalityCode;
        }

        public void setNationalityCode(String nationalityCode) {
            this.nationalityCode = nationalityCode;
        }

        public Object getClientIdentifierId() {
            return clientIdentifierId;
        }

        public void setClientIdentifierId(Object clientIdentifierId) {
            this.clientIdentifierId = clientIdentifierId;
        }

    }
}
