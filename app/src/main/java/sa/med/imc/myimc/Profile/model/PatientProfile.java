package sa.med.imc.myimc.Profile.model;

import java.io.Serializable;

public class PatientProfile {
    public String patId;
    public String familyName;
    public String familyNameAr;
    public String dob;
    public String age;
    public String gender;
    public String nationality;
    public String maritalStatus;
    public String mobile;
    public String addressEn;
    public String addressAr;
    public String email;
    public String lnameAr;
    public String fnameAr;
    public String mnameAr;
    private String isEmailVerified;
    public String mname;
    public String lname;
    public String fname;

    public PatientProfile(String patId, String familyName, String familyNameAr, String dob, String age, String gender, String nationality, String maritalStatus, String mobile, String addressEn, String addressAr, String email, String lnameAr, String fnameAr, String mnameAr, String isEmailVerified, String mname, String lname, String fname) {
        this.patId = patId;
        this.familyName = familyName;
        this.familyNameAr = familyNameAr;
        this.dob = dob;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.maritalStatus = maritalStatus;
        this.mobile = mobile;
        this.addressEn = addressEn;
        this.addressAr = addressAr;
        this.email = email;
        this.lnameAr = lnameAr;
        this.fnameAr = fnameAr;
        this.mnameAr = mnameAr;
        this.isEmailVerified = isEmailVerified;
        this.mname = mname;
        this.lname = lname;
        this.fname = fname;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getAddressAr() {
        return addressAr;
    }

    public void setAddressAr(String addressAr) {
        this.addressAr = addressAr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLnameAr() {
        return lnameAr;
    }

    public void setLnameAr(String lnameAr) {
        this.lnameAr = lnameAr;
    }

    public String getFnameAr() {
        return fnameAr;
    }

    public void setFnameAr(String fnameAr) {
        this.fnameAr = fnameAr;
    }

    public String getMnameAr() {
        return mnameAr;
    }

    public void setMnameAr(String mnameAr) {
        this.mnameAr = mnameAr;
    }

    public String getIsEmailVerified() {
        String i="0";

        try {
            if (isEmailVerified!=null){
                i=isEmailVerified;
            }
        } catch (Exception e) {
            i="0";
        }


        return i;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
