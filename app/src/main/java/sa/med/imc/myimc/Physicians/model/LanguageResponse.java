package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LanguageResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("languages")
    @Expose
    private List<Language> languages = null;

    @SerializedName("data")
    @Expose
    private List<Language> department = null;


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

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getDepartment() {
        return department;
    }

    public void setDepartment(List<Language> department) {
        this.department = department;
    }

    public class Language {

        @SerializedName("descEn")
        @Expose
        private String descEn;
        @SerializedName("descAr")
        @Expose
        private String descAr;
        @SerializedName("showOnPortal")
        @Expose
        private String showOnPortal;
        @SerializedName("showOnWeb")
        @Expose
        private String showOnWeb;
        @SerializedName("id")
        @Expose
        private String id;

        public String getDescEn() {
            return descEn;
        }

        public void setDescEn(String descEn) {
            this.descEn = descEn;
        }

        public String getDescAr() {
            return descAr;
        }

        public void setDescAr(String descAr) {
            this.descAr = descAr;
        }

        public String getShowOnPortal() {
            return showOnPortal;
        }

        public void setShowOnPortal(String showOnPortal) {
            this.showOnPortal = showOnPortal;
        }

        public String getShowOnWeb() {
            return showOnWeb;
        }

        public void setShowOnWeb(String showOnWeb) {
            this.showOnWeb = showOnWeb;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

}
