package sa.med.imc.myimc.Records.model;

public class DischargeModel {
    public String documentId;
    public String authorizingUser;
    public String authorizedDate;
    public String authorizedTime;
    public String reportType;
    public String hosp;
    public String oldData;

    public DischargeModel() {
    }

    public DischargeModel(String documentId, String authorizingUser, String authorizedDate, String authorizedTime, String reportType, String hosp, String oldData) {
        this.documentId = documentId;
        this.authorizingUser = authorizingUser;
        this.authorizedDate = authorizedDate;
        this.authorizedTime = authorizedTime;
        this.reportType = reportType;
        this.hosp = hosp;
        this.oldData = oldData;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAuthorizingUser() {
        return authorizingUser;
    }

    public void setAuthorizingUser(String authorizingUser) {
        this.authorizingUser = authorizingUser;
    }

    public String getAuthorizedDate() {
        return authorizedDate;
    }

    public void setAuthorizedDate(String authorizedDate) {
        this.authorizedDate = authorizedDate;
    }

    public String getAuthorizedTime() {
        return authorizedTime;
    }

    public void setAuthorizedTime(String authorizedTime) {
        this.authorizedTime = authorizedTime;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }
}
