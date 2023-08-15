package sa.med.imc.myimc.AddGuardian.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GuardianResponse {
    

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("guardianId")
        @Expose
        private String guardianId;
        @SerializedName("isActive")
        @Expose
        private Integer isActive;
        @SerializedName("noOfDays")
        @Expose
        private Integer noOfDays;
        @SerializedName("consent")
        @Expose
        private Integer consent;
        @SerializedName("userDetails")
        @Expose
        private UserDetails userDetails;

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

        public String getGuardianId() {
            return guardianId;
        }

        public void setGuardianId(String guardianId) {
            this.guardianId = guardianId;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public Integer getNoOfDays() {
            return noOfDays;
        }

        public void setNoOfDays(Integer noOfDays) {
            this.noOfDays = noOfDays;
        }

        public Integer getConsent() {
            return consent;
        }

        public void setConsent(Integer consent) {
            this.consent = consent;
        }

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

    
    public class UserDetails {

        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobilenum")
        @Expose
        private String mobilenum;
        @SerializedName("national_id")
        @Expose
        private String nationalId;
        @SerializedName("nationality")
        @Expose
        private String nationality;
        @SerializedName("nationalityAr")
        @Expose
        private String nationalityAr;
        @SerializedName("national_id_type")
        @Expose
        private String nationalIdType;
        @SerializedName("lang")
        @Expose
        private String lang;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("middle_name")
        @Expose
        private String middleName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("family_name")
        @Expose
        private String familyName;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("mrnumber")
        @Expose
        private String mrnumber;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("notification_status")
        @Expose
        private String notificationStatus;
        @SerializedName("marital_status")
        @Expose
        private String maritalStatus;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("mrnumberAr")
        @Expose
        private String mrnumberAr;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("arabic_first_name")
        @Expose
        private String firstNameAr;
        @SerializedName("middle_name_ar")
        @Expose
        private String middleNameAr;
        @SerializedName("arabic_last_name")
        @Expose
        private String lastNameAr;
        @SerializedName("family_name_ar")
        @Expose
        private String familyNameAr;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobilenum() {
            return mobilenum;
        }

        public void setMobilenum(String mobilenum) {
            this.mobilenum = mobilenum;
        }

        public String getNationalId() {
            return nationalId;
        }

        public void setNationalId(String nationalId) {
            this.nationalId = nationalId;
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

        public String getNationalIdType() {
            return nationalIdType;
        }

        public void setNationalIdType(String nationalIdType) {
            this.nationalIdType = nationalIdType;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getMrnumber() {
            return mrnumber;
        }

        public void setMrnumber(String mrnumber) {
            this.mrnumber = mrnumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNotificationStatus() {
            return notificationStatus;
        }

        public void setNotificationStatus(String notificationStatus) {
            this.notificationStatus = notificationStatus;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
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

        public String getMrnumberAr() {
            return mrnumberAr;
        }

        public void setMrnumberAr(String mrnumberAr) {
            this.mrnumberAr = mrnumberAr;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getFirstNameAr() {
            return firstNameAr;
        }

        public void setFirstNameAr(String firstNameAr) {
            this.firstNameAr = firstNameAr;
        }

        public String getMiddleNameAr() {
            return middleNameAr;
        }

        public void setMiddleNameAr(String middleNameAr) {
            this.middleNameAr = middleNameAr;
        }

        public String getLastNameAr() {
            return lastNameAr;
        }

        public void setLastNameAr(String lastNameAr) {
            this.lastNameAr = lastNameAr;
        }

        public String getFamilyNameAr() {
            return familyNameAr;
        }

        public void setFamilyNameAr(String familyNameAr) {
            this.familyNameAr = familyNameAr;
        }

    }
}
