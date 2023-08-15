package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageCMSResponse {

    @SerializedName("data")
    @Expose
    private List<LanguageModel> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<LanguageModel> getData() {
        return data;
    }

    public void setData(List<LanguageModel> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class LanguageModel {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("name_ar")
        @Expose
        private String nameAr;
        @SerializedName("full_ar")
        @Expose
        private String fullAr;
        @SerializedName("full_en")
        @Expose
        private String fullEn;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameAr() {
            return nameAr;
        }

        public void setNameAr(String nameAr) {
            this.nameAr = nameAr;
        }

        public String getFullAr() {
            return fullAr;
        }

        public void setFullAr(String fullAr) {
            this.fullAr = fullAr;
        }

        public String getFullEn() {
            return fullEn;
        }

        public void setFullEn(String fullEn) {
            this.fullEn = fullEn;
        }

    }


}
