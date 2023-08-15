package sa.med.imc.myimc.Physicians.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorFullDeatilsModel {
    public String status;
    public String message;
    public PhysicianData physicianData;
    public String no_show_status;

    public DoctorFullDeatilsModel() {
    }

    public DoctorFullDeatilsModel(String status, String message,PhysicianData physicianData, String no_show_status) {
        this.status = status;
        this.message = message;
        this.physicianData = physicianData;
        this.no_show_status = no_show_status;
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
    public class PhysicianData{
        public String docCode;
        public String givenName;
        public String givenNameAr;
        public String familyName;
        public String familyNameAr;
        public String fathersName;
        public String fathersNameAr;
        public String isActive;
        public String deptCode;
        public String clinicCode;
        public String clinicNameEn;
        public String clincNameAr;
        public String deptNameEn;
        public String deptNameAr;
        public String docRating;
        public String docImg;
        public PhysicianResponse.Service service;
        public String specialitiesTxt;
        public String specialitiesTxtAr;
        public String educationTxt;
        public String educationTxtAr;
        public String affilationsTxt;
        public String affilationsTxtAr;
        public String researchTxt;
        public String researchTxtAr;
        public String languagesTxt;
        public String languagesTxtAr;
        public String status;
        public String dispOnPortal;
        public String gender;

        public String specInstructEn;

        public String specInstructAr;

        public int teleHealthEligibleFlag;
        public String profilePic;

        public PhysicianData() {
        }

        public PhysicianData(String docCode, String givenName, String givenNameAr, String familyName, String familyNameAr, String fathersName, String fathersNameAr, String isActive, String deptCode, String clinicCode, String clinicNameEn, String clincNameAr, String deptNameEn, String deptNameAr, String docRating, String docImg, PhysicianResponse.Service service, String specialitiesTxt, String specialitiesTxtAr, String educationTxt, String educationTxtAr, String affilationsTxt, String affilationsTxtAr, String researchTxt, String researchTxtAr, String languagesTxt, String languagesTxtAr, String status, String dispOnPortal,
                             String gender, int teleHealthEligibleFlag, String profilePic, String specInstructEn, String specInstructAr) {
            this.docCode = docCode;
            this.givenName = givenName;
            this.givenNameAr = givenNameAr;
            this.familyName = familyName;
            this.familyNameAr = familyNameAr;
            this.fathersName = fathersName;
            this.fathersNameAr = fathersNameAr;
            this.isActive = isActive;
            this.deptCode = deptCode;
            this.clinicCode = clinicCode;
            this.clinicNameEn = clinicNameEn;
            this.clincNameAr = clincNameAr;
            this.deptNameEn = deptNameEn;
            this.deptNameAr = deptNameAr;
            this.docRating = docRating;
            this.docImg = docImg;
            this.service = service;
            this.specialitiesTxt = specialitiesTxt;
            this.specialitiesTxtAr = specialitiesTxtAr;
            this.educationTxt = educationTxt;
            this.educationTxtAr = educationTxtAr;
            this.affilationsTxt = affilationsTxt;
            this.affilationsTxtAr = affilationsTxtAr;
            this.researchTxt = researchTxt;
            this.researchTxtAr = researchTxtAr;
            this.languagesTxt = languagesTxt;
            this.languagesTxtAr = languagesTxtAr;
            this.status = status;
            this.dispOnPortal = dispOnPortal;
            this.gender = gender;
            this.teleHealthEligibleFlag = teleHealthEligibleFlag;
            this.profilePic = profilePic;

            this.specInstructEn = specInstructEn;
            this.specInstructAr = specInstructAr;

            Log.e("specInstructAr","specInstructAr" );

        }

        public String getspecInstructEn() {
            return specInstructEn;
        }

        public void setSpecInstructEn(String specInstructEn) {
            this.specInstructEn = specInstructEn;
        }

        public String getSpecInstructAr() {
            String s=specInstructEn;

            try {
                if (!specInstructAr.isEmpty()){
                    s=specInstructAr;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return s;
        }

        public void setSpecInstructAr(String specInstructAr) {
            this.specInstructAr = specInstructAr;
        }

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
            String s=givenNameAr;

            try {
                if (givenNameAr.trim().isEmpty()){
                    s=givenName;
                }
            } catch (Exception e) {
                s=givenName;
            }


            return s;
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
            String s=familyNameAr;

            try {
                if (familyNameAr.trim().isEmpty()){
                    s=familyName;
                }
            } catch (Exception e) {
                s=familyName;
            }


            return s;
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
            String s=clincNameAr;

            try {
                if (clincNameAr.trim().isEmpty()){
                    s=clinicNameEn;
                }
            } catch (Exception e) {
                s=clinicNameEn;
            }
            return s;
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

        public PhysicianResponse.Service getService() {
            return service;
        }

        public void setService(PhysicianResponse.Service service) {
            this.service = service;
        }

        public String getSpecialitiesTxt() {
            return specialitiesTxt;
        }

        public void setSpecialitiesTxt(String specialitiesTxt) {
            this.specialitiesTxt = specialitiesTxt;
        }

        public String getSpecialitiesTxtAr() {
            String s=specialitiesTxtAr;

            try {
                if (specialitiesTxtAr.trim().isEmpty()){
                    s=specialitiesTxt;
                }
            } catch (Exception e) {
                s=specialitiesTxt;
            }
            return s;
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
            String s=educationTxtAr;

            try {
                if (educationTxtAr.trim().isEmpty()){
                    s=educationTxt;
                }
            } catch (Exception e) {
                s=educationTxt;
            }
            return s;
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
            String s=languagesTxtAr;

            try {
                if (languagesTxtAr.trim().isEmpty()){
                    s=languagesTxt;
                }
            } catch (Exception e) {
                s=languagesTxt;
            }
            return s;
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

        public String getDispOnPortal() {
            return dispOnPortal;
        }

        public void setDispOnPortal(String dispOnPortal) {
            this.dispOnPortal = dispOnPortal;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getTeleHealthEligibleFlag() {
            return teleHealthEligibleFlag;
        }

        public void setTeleHealthEligibleFlag(int teleHealthEligibleFlag) {
            this.teleHealthEligibleFlag = teleHealthEligibleFlag;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }
    }



}
