package sa.med.imc.myimc.globle.model;

public class RequestBodyForPaymentUrlGeneration {
    String serviceCode;
    String slotBookingId;
    int status = 0;
    String patientId;
    String payerForname;
    String payerSurname;
    String docCode;
    String otpLogin="";
    String docName;
    String payerEmail;
    String appointmentDate;
    String payerAddress;
    String amount;
    String serviceName;
    String payerTitle;
    String mrn;
    String hashLink="";
    String teleHealthFlag="0";
    boolean isExpiredFlag = true;

    public RequestBodyForPaymentUrlGeneration() {
    }

    public RequestBodyForPaymentUrlGeneration(String serviceCode, String slotBookingId, int status, String patientId, String payerForname, String payerSurname, String docCode, String otpLogin, String docName, String payerEmail, String appointmentDate, String payerAddress, String amount, String serviceName, String payerTitle, String mrn, String hashLink, String teleHealthFlag, boolean isExpiredFlag) {
        this.serviceCode = serviceCode;
        this.slotBookingId = slotBookingId;
        this.status = status;
        this.patientId = patientId;
        this.payerForname = payerForname;
        this.payerSurname = payerSurname;
        this.docCode = docCode;
        this.otpLogin = otpLogin;
        this.docName = docName;
        this.payerEmail = payerEmail;
        this.appointmentDate = appointmentDate;
        this.payerAddress = payerAddress;
        this.amount = amount;
        this.serviceName = serviceName;
        this.payerTitle = payerTitle;
        this.mrn = mrn;
        this.hashLink = hashLink;
        this.teleHealthFlag = teleHealthFlag;
        this.isExpiredFlag = isExpiredFlag;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getSlotBookingId() {
        return slotBookingId;
    }

    public void setSlotBookingId(String slotBookingId) {
        this.slotBookingId = slotBookingId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPayerForname() {
        return payerForname;
    }

    public void setPayerForname(String payerForname) {
        this.payerForname = payerForname;
    }

    public String getPayerSurname() {
        return payerSurname;
    }

    public void setPayerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getOtpLogin() {
        return otpLogin;
    }

    public void setOtpLogin(String otpLogin) {
        this.otpLogin = otpLogin;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPayerTitle() {
        return payerTitle;
    }

    public void setPayerTitle(String payerTitle) {
        this.payerTitle = payerTitle;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getHashLink() {
        return hashLink;
    }

    public void setHashLink(String hashLink) {
        this.hashLink = hashLink;
    }

    public String getTeleHealthFlag() {
        return teleHealthFlag;
    }

    public void setTeleHealthFlag(String teleHealthFlag) {
        this.teleHealthFlag = teleHealthFlag;
    }

    public boolean isExpiredFlag() {
        return isExpiredFlag;
    }

    public void setExpiredFlag(boolean expiredFlag) {
        isExpiredFlag = expiredFlag;
    }
}
