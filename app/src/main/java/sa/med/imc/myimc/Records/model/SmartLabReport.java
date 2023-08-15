package sa.med.imc.myimc.Records.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class SmartLabReport {

    @SerializedName("mainTest")
    @Expose
    private String mainTest;
    @SerializedName("longName")
    @Expose
    private String longName;

    @PrimaryKey
    @NonNull
    @SerializedName("specimenNum")
    @Expose
    private String specimenNum = "";
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
    private String reportType = "";
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusWeb")
    @Expose
    private Integer statusWeb;
    @SerializedName("episodeNo")
    @Expose
    private String episodeNo;
    @Ignore
    @SerializedName("smartReport")
    @Expose
    private Integer smartReport;

    private String MRN;

    private String localPath = "";

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getMainTest() {
        return mainTest;
    }

    public void setMainTest(String mainTest) {
        this.mainTest = mainTest;
    }

    public String getLongName() {
        return longName;
    }

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

    public Integer getSmartReport() {
        return smartReport;
    }

    public void setSmartReport(Integer smartReport) {
        this.smartReport = smartReport;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }
}
