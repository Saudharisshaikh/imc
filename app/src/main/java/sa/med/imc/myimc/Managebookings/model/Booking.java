package sa.med.imc.myimc.Managebookings.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Booking implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patId")
    @Expose
    private String patId;
    @SerializedName("clinicDescEn")
    @Expose
    private String clinicDescEn;
    @SerializedName("clinicDescAr")
    @Expose
    private String clinicDescAr;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("apptType")
    @Expose
    private String apptType;
    @SerializedName("sessionId")
    @Expose
    private Integer sessionId;
    @SerializedName("docCode")
    @Expose
    private String docCode;
    @SerializedName("clinicCode")
    @Expose
    private String clinicCode;
    @SerializedName("apptDate")
    @Expose
    private String apptDate;
    @SerializedName("doc_isActive")
    @Expose
    private String doc_isActive;
    @SerializedName("apptDateString")
    @Expose
    private String apptDateString;
    @SerializedName("sysCode")
    @Expose
    private String sysCode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("givenName")
    @Expose
    private String givenName;
    @SerializedName("fathersName")
    @Expose
    private String fathersName;
    @SerializedName("familyName")
    @Expose
    private String familyName;
    @SerializedName("givenNameAr")
    @Expose
    private String givenNameAr;
    @SerializedName("fatherNameAr")
    @Expose
    private String fatherNameAr;
    @SerializedName("familyNameAr")
    @Expose
    private String familyNameAr;
    @SerializedName("hrId")
    @Expose
    private String hrId;
    @SerializedName("teleHealthLink")
    @Expose
    private String teleHealthLink;
    @SerializedName("teleBooking")
    @Expose
    private Integer teleBooking;
    @SerializedName("bookingStatus")
    @Expose
    private Integer bookingStatus;
    @SerializedName("docImg")
    @Expose
    private String docImg;
    @Embedded
    @SerializedName("service")
    @Expose
    private Service service;
    @Ignore
    @SerializedName("promisForm")
    @Expose
    private List<FormIdNameModel> promisForm;
    @Ignore
    @SerializedName("selfCheckIn")
    @Expose
    private boolean selfCheckIn;
    @Ignore
    @SerializedName("paymentStatus")
    @Expose
    private Integer paymentStatus;
    @Ignore
    @SerializedName("isArrived")
    @Expose
    private Integer isArrived;
    @Ignore
    @SerializedName("hidePayCheckin")
    @Expose
    private boolean hidePayCheckin;

    public boolean isHidePayCheckin() {
        return hidePayCheckin;
    }

    public void setHidePayCheckin(boolean hidePayCheckin) {
        this.hidePayCheckin = hidePayCheckin;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getIsArrived() {
        return isArrived;
    }

    public void setIsArrived(Integer isArrived) {
        this.isArrived = isArrived;
    }

    private String MRN;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getClinicDescEn() {
        return clinicDescEn;
    }

    public void setClinicDescEn(String clinicDescEn) {
        this.clinicDescEn = clinicDescEn;
    }

    public String getClinicDescAr() {
        return clinicDescAr;
    }

    public void setClinicDescAr(String clinicDescAr) {
        this.clinicDescAr = clinicDescAr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDoc_isActive() {
        return doc_isActive;
    }

    public void setDoc_isActive(String doc_isActive) {
        this.doc_isActive = doc_isActive;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenNameAr() {
        if (givenNameAr == null)
            givenNameAr = "";
        return givenNameAr;
    }

    public void setGivenNameAr(String givenNameAr) {
        this.givenNameAr = givenNameAr;
    }

    public String getFatherNameAr() {
        if (fatherNameAr == null)
            fatherNameAr = "";
        return fatherNameAr;
    }

    public void setFatherNameAr(String fatherNameAr) {
        this.fatherNameAr = fatherNameAr;
    }

    public String getFamilyNameAr() {
        if (familyNameAr == null)
            familyNameAr = "";
        return familyNameAr;
    }

    public void setFamilyNameAr(String familyNameAr) {
        this.familyNameAr = familyNameAr;
    }

    public Integer getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Integer bookingStatus) {
        this.bookingStatus = bookingStatus;
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

    public String getApptDateString() {
        return apptDateString;
    }

    public void setApptDateString(String apptDateString) {
        this.apptDateString = apptDateString;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getHrId() {
        if (hrId == null)
            hrId = "";
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public String getTeleHealthLink() {
        return teleHealthLink;
    }

    public void setTeleHealthLink(String teleHealthLink) {
        this.teleHealthLink = teleHealthLink;
    }

    public Integer getTeleBooking() {
        return teleBooking;
    }

    public void setTeleBooking(Integer teleBooking) {
        this.teleBooking = teleBooking;
    }

    public List<FormIdNameModel> getPromisForm() {
        return promisForm;
    }

    public boolean isSelfCheckIn() {
        return selfCheckIn;
    }

    public void setSelfCheckIn(boolean selfCheckIn) {
        this.selfCheckIn = selfCheckIn;
    }

    public void setPromisForm(List<FormIdNameModel> promisForm) {
        this.promisForm = promisForm;
    }


}