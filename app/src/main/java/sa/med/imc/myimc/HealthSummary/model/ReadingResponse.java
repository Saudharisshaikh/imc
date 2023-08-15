package sa.med.imc.myimc.HealthSummary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadingResponse {

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("status")
    @Expose
    boolean status;

    @SerializedName("total_items")
    @Expose
    String total_items;

    @SerializedName("total_pages")
    @Expose
    String total_pages;

    @SerializedName("data")
    @Expose
    List<ReadingModel> readingModels;


    public ReadingResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<ReadingModel> getReadingModels() {
        return readingModels;
    }

    public void setReadingModels(List<ReadingModel> readingModels) {
        this.readingModels = readingModels;
    }

    public class ReadingModel {

        @SerializedName("readingId")
        @Expose
        String readingId;

        @SerializedName("patId")
        @Expose
        String patId;

        @SerializedName("episodeNo")
        @Expose
        String episodeNo;

        @SerializedName("height")
        @Expose
        double height;

        @SerializedName("weight")
        @Expose
        double weight;

        @SerializedName("bodyTempture")
        @Expose
        String bodyTempture;

        @SerializedName("bloodPressure")
        @Expose
        String bloodPressure;

        @SerializedName("respiratoryRate")
        @Expose
        String respiratoryRate;

        @SerializedName("pluseRate")
        @Expose
        String pluseRate;

        @SerializedName("oxSaturation")
        @Expose
        String oxSaturation;

        @SerializedName("notes")
        @Expose
        String notes;

        @SerializedName("readingDate")
        @Expose
        String readingDate;

        @SerializedName("ibw")
        @Expose
        String ibw;

        @SerializedName("bsa")
        @Expose
        String bsa;

        @SerializedName("bee")
        @Expose
        String bee;

        @SerializedName("bmi")
        @Expose
        String bmi;

        @SerializedName("bloodGlucose")
        @Expose
        String bloodGlucose;

        @SerializedName("epsiodeLocationEn")
        @Expose
        String epsiodeLocationEn;

        @SerializedName("epsiodeLocationAr")
        @Expose
        String epsiodeLocationAr;

        @SerializedName("hosp")
        @Expose
        String hosp;

        public ReadingModel() {
        }

        public ReadingModel(String readingId, String patId, String episodeNo, double height, double weight, String bodyTempture, String bloodPressure, String respiratoryRate, String pluseRate, String oxSaturation, String notes, String readingDate, String ibw, String bsa, String bee, String bmi, String bloodGlucose, String epsiodeLocationEn, String epsiodeLocationAr, String hosp) {
            this.readingId = readingId;
            this.patId = patId;
            this.episodeNo = episodeNo;
            this.height = height;
            this.weight = weight;
            this.bodyTempture = bodyTempture;
            this.bloodPressure = bloodPressure;
            this.respiratoryRate = respiratoryRate;
            this.pluseRate = pluseRate;
            this.oxSaturation = oxSaturation;
            this.notes = notes;
            this.readingDate = readingDate;
            this.ibw = ibw;
            this.bsa = bsa;
            this.bee = bee;
            this.bmi = bmi;
            this.bloodGlucose = bloodGlucose;
            this.epsiodeLocationEn = epsiodeLocationEn;
            this.epsiodeLocationAr = epsiodeLocationAr;
            this.hosp = hosp;
        }

        public String getReadingId() {
            return readingId;
        }

        public void setReadingId(String readingId) {
            this.readingId = readingId;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getEpisodeNo() {
            return episodeNo;
        }

        public void setEpisodeNo(String episodeNo) {
            this.episodeNo = episodeNo;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getBodyTempture() {
            return bodyTempture;
        }

        public void setBodyTempture(String bodyTempture) {
            this.bodyTempture = bodyTempture;
        }

        public String getBloodPressure() {
            return bloodPressure;
        }

        public void setBloodPressure(String bloodPressure) {
            this.bloodPressure = bloodPressure;
        }

        public String getRespiratoryRate() {
            return respiratoryRate;
        }

        public void setRespiratoryRate(String respiratoryRate) {
            this.respiratoryRate = respiratoryRate;
        }

        public String getPluseRate() {
            return pluseRate;
        }

        public void setPluseRate(String pluseRate) {
            this.pluseRate = pluseRate;
        }

        public String getOxSaturation() {
            return oxSaturation;
        }

        public void setOxSaturation(String oxSaturation) {
            this.oxSaturation = oxSaturation;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getReadingDate() {
            return readingDate;
        }

        public void setReadingDate(String readingDate) {
            this.readingDate = readingDate;
        }

        public String getIbw() {
            return ibw;
        }

        public void setIbw(String ibw) {
            this.ibw = ibw;
        }

        public String getBsa() {
            return bsa;
        }

        public void setBsa(String bsa) {
            this.bsa = bsa;
        }

        public String getBee() {
            return bee;
        }

        public void setBee(String bee) {
            this.bee = bee;
        }

        public String getBmi() {
            return bmi;
        }

        public void setBmi(String bmi) {
            this.bmi = bmi;
        }

        public String getBloodGlucose() {
            return bloodGlucose;
        }

        public void setBloodGlucose(String bloodGlucose) {
            this.bloodGlucose = bloodGlucose;
        }

        public String getEpsiodeLocationEn() {
            return epsiodeLocationEn;
        }

        public void setEpsiodeLocationEn(String epsiodeLocationEn) {
            this.epsiodeLocationEn = epsiodeLocationEn;
        }

        public String getEpsiodeLocationAr() {
            return epsiodeLocationAr;
        }

        public void setEpsiodeLocationAr(String epsiodeLocationAr) {
            this.epsiodeLocationAr = epsiodeLocationAr;
        }

        public String getHosp() {
            return hosp;
        }

        public void setHosp(String hosp) {
            this.hosp = hosp;
        }
    }
}
