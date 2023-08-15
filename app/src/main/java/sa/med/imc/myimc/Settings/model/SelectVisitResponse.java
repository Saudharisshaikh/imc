package sa.med.imc.myimc.Settings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectVisitResponse {
    public String getEpisodeKey() {
        return episodeKey;
    }

    public void setEpisodeKey(String episodeKey) {
        this.episodeKey = episodeKey;
    }

    public String getEpisodeType() {
        return episodeType;
    }

    public void setEpisodeType(String episodeType) {
        this.episodeType = episodeType;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getPatid() {
        return patid;
    }

    public void setPatid(String patid) {
        this.patid = patid;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public String getConsultantAr() {
        return consultantAr;
    }

    public void setConsultantAr(String consultantAr) {
        this.consultantAr = consultantAr;
    }

    @SerializedName("episodeKey")
    @Expose
    private String episodeKey;
    @SerializedName("episodeType")
    @Expose
    private String episodeType;
    @SerializedName("episodeNo")
    @Expose
    private String episodeNo;

    @SerializedName("patid")
    @Expose
    private String patid;
    @SerializedName("apptDate")
    @Expose
    private String apptDate;
    @SerializedName("consultant")
    @Expose
    private String consultant;

    @SerializedName("consultantAr")
    @Expose
    private String consultantAr;
}
