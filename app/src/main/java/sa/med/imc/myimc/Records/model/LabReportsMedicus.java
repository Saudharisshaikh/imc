package sa.med.imc.myimc.Records.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabReportsMedicus {

    @SerializedName("mainTest")
    @Expose
    private String mainTest;
    @SerializedName("longName")
    @Expose
    private String longName;
    @SerializedName("hosp")
    @Expose
    private String hosp_ID;
    @SerializedName("specimenNum")
    @Expose
    private String specimenNum;
    @SerializedName("accessionNum")
    @Expose
    private String accessionNum;
    @SerializedName("dateTaken")
    @Expose
    private String dateTaken;
    @SerializedName("patId")
    @Expose
    private String patId;
    @SerializedName("trackingNum")
    @Expose
    private String trackingNum;
    @SerializedName("orderNum")
    @Expose
    private String orderNum;
    @SerializedName("orderNumLine")
    @Expose
    private String orderNumLine;
    @SerializedName("reportType")
    @Expose
    private String reportType;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusWeb")
    @Expose
    private Integer statusWeb;
    @SerializedName("episodeNo")
    @Expose
    private String episodeNo;
    @SerializedName("docName")
    @Expose
    private String docName;
    @SerializedName("docNameAr")
    @Expose
    private String docNameAr;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    @SerializedName("aptTimeAr")
    @Expose
    private String aptTimeAr;
    @SerializedName("smartReport")
    @Expose
    private Integer smartReport;

    public String getMainTest() {
        return mainTest;
    }

    public void setMainTest(String mainTest) {
        this.mainTest = mainTest;
    }

    public String getLongName() {
        return longName;
    }
    public String getHosp_ID() { return hosp_ID; }
    public void setHosp_ID(String hosp_ID) { this.hosp_ID = hosp_ID; }
    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getSpecimenNum() {
        return specimenNum;
    }

    public void setSpecimenNum(String specimenNum) {
        this.specimenNum = specimenNum;
    }

    public String getAccessionNum() {
        return accessionNum;
    }

    public void setAccessionNum(String accessionNum) {
        this.accessionNum = accessionNum;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNumLine() {
        return orderNumLine;
    }

    public void setOrderNumLine(String orderNumLine) {
        this.orderNumLine = orderNumLine;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusWeb() {
        return statusWeb;
    }

    public void setStatusWeb(Integer statusWeb) {
        this.statusWeb = statusWeb;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocNameAr() {
        return docNameAr;
    }

    public void setDocNameAr(String docNameAr) {
        this.docNameAr = docNameAr;
    }

    public String getAptTime() {
        return aptTime;
    }

    public void setAptTime(String aptTime) {
        this.aptTime = aptTime;
    }

    public String getAptTimeAr() {
        return aptTimeAr;
    }

    public void setAptTimeAr(String aptTimeAr) {
        this.aptTimeAr = aptTimeAr;
    }

    public Integer getSmartReport() {
        return smartReport;
    }

    public void setSmartReport(Integer smartReport) {
        this.smartReport = smartReport;
    }

}