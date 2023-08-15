package sa.med.imc.myimc.Physicians.model;

import java.util.List;

public class AllDoctorListModel {
    public String status;
    public List<DoctorListBasedOnSpeciality> doctorListBasedOnSpeciality;

    public AllDoctorListModel() {
    }

    public AllDoctorListModel(String status, List<DoctorListBasedOnSpeciality> doctorListBasedOnSpeciality) {
        this.status = status;
        this.doctorListBasedOnSpeciality = doctorListBasedOnSpeciality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DoctorListBasedOnSpeciality> getDoctorListBasedOnSpeciality() {
        return doctorListBasedOnSpeciality;
    }

    public void setDoctorListBasedOnSpeciality(List<DoctorListBasedOnSpeciality> doctorListBasedOnSpeciality) {
        this.doctorListBasedOnSpeciality = doctorListBasedOnSpeciality;
    }

    public class DoctorList{
        public String provider_code;
        public String provider_description;
        public String arabic_provider_description;
        public boolean is_patientportal_doctor;

        public DoctorList() {
        }

        public DoctorList(String provider_code, String provider_description, String arabic_provider_description, boolean is_patientportal_doctor) {
            this.provider_code = provider_code;
            this.provider_description = provider_description;
            this.arabic_provider_description = arabic_provider_description;
            this.is_patientportal_doctor = is_patientportal_doctor;
        }

        public String getProvider_code() {
            return provider_code;
        }

        public void setProvider_code(String provider_code) {
            this.provider_code = provider_code;
        }

        public String getProvider_description() {
            return provider_description;
        }

        public void setProvider_description(String provider_description) {
            this.provider_description = provider_description;
        }

        public String getArabic_provider_description() {
            return arabic_provider_description;
        }

        public void setArabic_provider_description(String arabic_provider_description) {
            this.arabic_provider_description = arabic_provider_description;
        }

        public boolean getIs_patientportal_doctor() {
            return is_patientportal_doctor;
        }

        public void setIs_patientportal_doctor(boolean is_patientportal_doctor) {
            this.is_patientportal_doctor = is_patientportal_doctor;
        }
    }

    public class DoctorListBasedOnSpeciality{
        public String specialityCode;
        public String specialityDescription;
        public String arabicSpecialityDescription;
        public List<DoctorList> doctorList;

        public DoctorListBasedOnSpeciality() {
        }

        public DoctorListBasedOnSpeciality(String specialityCode, String specialityDescription, List<DoctorList> doctorList) {
            this.specialityCode = specialityCode;
            this.specialityDescription = specialityDescription;
            this.doctorList = doctorList;
        }

        public String getSpecialityCode() {
            return specialityCode;
        }

        public String getArabicSpecialityDescription() {
            return arabicSpecialityDescription;
        }

        public void setArabicSpecialityDescription(String arabicSpecialityDescription) {
            this.arabicSpecialityDescription = arabicSpecialityDescription;
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

        public List<DoctorList> getDoctorList() {
            return doctorList;
        }

        public void setDoctorList(List<DoctorList> doctorList) {
            this.doctorList = doctorList;
        }
    }
}
