package sa.med.imc.myimc.Managebookings.model;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookingAppoinment implements Serializable {

    String id;
    String patId;
    String patName;
    String apptDate;
    String apptDateString;
    boolean selfCheckIn;
    String paymentStatus;
    String isArrived;
    String hidePayCheckin;
    int bookingStatus;
    String specialityCode;

    String specialityDescription;
    String clinicDescEn;
    String clinicDescAr;

    String serviceCode;
    String serviceDescription;
    String arabicProviderDescription;
    String arabicServiceDescription;
    String arabicSpecialityDescription;

    @SerializedName(value = "serviceType",alternate = "teleBooking")
    @Expose
    String serviceType;

    @SerializedName(value = "careProviderCode",alternate = "docCode")
    String careProviderCode;

    @SerializedName(value = "careProviderDescription",alternate = "givenName")
    @Expose
    String careProviderDescription="";
    String familyName="";
    String familyNameAr="";
    public String givenNameAr="";
    String slotBookingId;
    boolean isPaid;
    boolean isDoctorActive;
    String telemedicineStatus;
    String paymentButton;
    String showCheckIn;

    List<PromisForm> promisForm;
    Service service;

    public BookingAppoinment() {
    }

    public BookingAppoinment(String id, String patId, String patName, String apptDate, String apptDateString, boolean selfCheckIn, String paymentStatus, String isArrived, String hidePayCheckin, int bookingStatus, String specialityCode, String specialityDescription, String serviceCode, String serviceDescription, String serviceType, String careProviderCode, String careProviderDescription, String slotBookingId, boolean isPaid, boolean isDoctorActive, String telemedicineStatus, List<PromisForm> promisForm, Service service) {
        this.id = id;
        this.patId = patId;
        this.patName = patName;
        this.apptDate = apptDate;
        this.apptDateString = apptDateString;
        this.selfCheckIn = selfCheckIn;
        this.paymentStatus = paymentStatus;
        this.isArrived = isArrived;
        this.hidePayCheckin = hidePayCheckin;
        this.bookingStatus = bookingStatus;
        this.specialityCode = specialityCode;
        this.specialityDescription = specialityDescription;
        this.serviceCode = serviceCode;
        this.serviceDescription = serviceDescription;
        this.serviceType = serviceType;
        this.careProviderCode = careProviderCode;
        this.careProviderDescription = careProviderDescription;
        this.slotBookingId = slotBookingId;
        this.isPaid = isPaid;
        this.isDoctorActive = isDoctorActive;
        this.telemedicineStatus = telemedicineStatus;
        this.promisForm = promisForm;
        this.service = service;
    }

    public String getArabicProviderDescription() {
        String s=arabicProviderDescription;

        try {
            if (arabicProviderDescription.trim().isEmpty()){
                s=careProviderDescription;
            }
        } catch (Exception e) {
            s=careProviderDescription;
        }


        return s;
    }

    public void setArabicProviderDescription(String arabicProviderDescription) {
        this.arabicProviderDescription = arabicProviderDescription;
    }

    public String getArabicServiceDescription() {
        String s=arabicServiceDescription;

        try {
            if (arabicServiceDescription.trim().isEmpty()){
                s=serviceDescription;
            }
        } catch (Exception e) {
            s=serviceDescription;
        }


        return s;
    }

    public void setArabicServiceDescription(String arabicServiceDescription) {
        this.arabicServiceDescription = arabicServiceDescription;
    }

    public String getArabicSpecialityDescription() {
        String s=arabicSpecialityDescription;

        try {
            if (arabicSpecialityDescription.trim().isEmpty()){
                s=specialityDescription;
            }
        } catch (Exception e) {
            s=specialityDescription;
        }


        return s;
    }

    public void setArabicSpecialityDescription(String arabicSpecialityDescription) {
        this.arabicSpecialityDescription = arabicSpecialityDescription;
    }

    public String getClinicDescAr() {
        String s=clinicDescAr;
        String s2=clinicDescAr;

        try {
            if (getSlotBookingId().trim().contains("|")) {
                s2=getSpecialityDescription();
            } else {
                s2=getClinicDescEn();
            }
        } catch (Exception e) {
            try {
                if (!getClinicDescEn().isEmpty()){
                    s2=getClinicDescEn();
                }
            } catch (Exception exception) {
                s2=getSpecialityDescription();
            }

        }

        try {
            if (clinicDescAr.trim().isEmpty()){
                s=s2;
            }
        } catch (Exception e) {
            s=s2;
        }
        return s;
    }

    public void setClinicDescAr(String clinicDescAr) {
        this.clinicDescAr = clinicDescAr;
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

    public String getGivenNameAr() {
        String s=givenNameAr;

        try {
            if (givenNameAr.trim().isEmpty()){
                s=careProviderDescription;
            }
        } catch (Exception e) {
            s=careProviderDescription;
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

    public String getShowCheckIn() {
        return showCheckIn;
    }

    public void setShowCheckIn(String showCheckIn) {
        this.showCheckIn = showCheckIn;
    }

    public String getPaymentButton() {
        return paymentButton;
    }

    public void setPaymentButton(String paymentButton) {
        this.paymentButton = paymentButton;
    }

    public boolean isDoctorActive() {
        return isDoctorActive;
    }

    public void setDoctorActive(boolean doctorActive) {
        isDoctorActive = doctorActive;
    }

    public String getTelemedicineStatus() {
        return telemedicineStatus;
    }

    public void setTelemedicineStatus(String telemedicineStatus) {
        this.telemedicineStatus = telemedicineStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }



    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getApptDateString() {
        return apptDateString;
    }

    public void setApptDateString(String apptDateString) {
        this.apptDateString = apptDateString;
    }



    public boolean isSelfCheckIn() {
        return selfCheckIn;
    }

    public void setSelfCheckIn(boolean selfCheckIn) {
        this.selfCheckIn = selfCheckIn;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getIsArrived() {
        return isArrived;
    }

    public void setIsArrived(String isArrived) {
        this.isArrived = isArrived;
    }

    public String getHidePayCheckin() {
        return hidePayCheckin;
    }

    public void setHidePayCheckin(String hidePayCheckin) {
        this.hidePayCheckin = hidePayCheckin;
    }

    public int getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getClinicDescEn() {
        return clinicDescEn;
    }

    public void setClinicDescEn(String clinicDescEn) {
        this.clinicDescEn = clinicDescEn;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCareProviderCode() {
        return careProviderCode;
    }

    public String getDocCode() {
        return careProviderCode;
    }

    public void setCareProviderCode(String careProviderCode) {
        this.careProviderCode = careProviderCode;
    }

    public String getCareProviderDescription() {


        return careProviderDescription;
    }

    public void setCareProviderDescription(String careProviderDescription) {
        this.careProviderDescription = careProviderDescription;
    }

    public String getSlotBookingId() {
        return slotBookingId;
    }

    public void setSlotBookingId(String slotBookingId) {
        this.slotBookingId = slotBookingId;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public List<PromisForm> getPromisForm() {
        return promisForm;
    }

    public void setPromisForm(List<PromisForm> promisForm) {
        this.promisForm = promisForm;
    }


    public class Service {
        String id;
        String desc;
        String descAr;
        String deptCode;


        public Service() {
        }

        public Service(String id, String desc, String descAr, String deptCode) {
            this.id = id;
            this.desc = desc;
            this.descAr = descAr;
            this.deptCode = deptCode;
        }

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

    public class PromisForm {

        String formId;
        String formName;
        String assessmentId;
        String hosp;
        String promisQuestionnaireCode;
        String assessmentStatus;

        public PromisForm() {
        }

        public PromisForm(String formId, String formName, String assessmentId, String hosp, String promisQuestionnaireCode, String assessmentStatus) {
            this.formId = formId;
            this.formName = formName;
            this.assessmentId = assessmentId;
            this.hosp = hosp;
            this.promisQuestionnaireCode = promisQuestionnaireCode;
            this.assessmentStatus = assessmentStatus;
        }

        public String getAssessmentStatus() {
            return assessmentStatus;
        }

        public void setAssessmentStatus(String assessmentStatus) {
            this.assessmentStatus = assessmentStatus;
        }

        public String getPromisQuestionnaireCode() {
            return promisQuestionnaireCode;
        }

        public void setPromisQuestionnaireCode(String promisQuestionnaireCode) {
            this.promisQuestionnaireCode = promisQuestionnaireCode;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public String getFormName() {
            return formName;
        }

        public void setFormName(String formName) {
            this.formName = formName;
        }

        public String getAssessmentId() {
            return assessmentId;
        }

        public void setAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
        }

        public String getHosp() {
            return hosp;
        }

        public void setHosp(String hosp) {
            this.hosp = hosp;
        }
    }
}
