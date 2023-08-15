package sa.med.imc.myimc.UrlOpen;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlContentResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title_en")
        @Expose
        private String titleEn;
        @SerializedName("title_ar")
        @Expose
        private String titleAr;
        @SerializedName("heading1_en")
        @Expose
        private String heading1En;
        @SerializedName("heading1_ar")
        @Expose
        private String heading1Ar;
        @SerializedName("description1_en")
        @Expose
        private String description1En;
        @SerializedName("description1_ar")
        @Expose
        private String description1Ar;
        @SerializedName("heading2_en")
        @Expose
        private String heading2En;
        @SerializedName("heading2_ar")
        @Expose
        private String heading2Ar;
        @SerializedName("description2_en")
        @Expose
        private String description2En;
        @SerializedName("description2_ar")
        @Expose
        private String description2Ar;
        @SerializedName("heading3_en")
        @Expose
        private String heading3En;
        @SerializedName("heading3_ar")
        @Expose
        private String heading3Ar;
        @SerializedName("description3_en")
        @Expose
        private String description3En;
        @SerializedName("description3_ar")
        @Expose
        private String description3Ar;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("description_en")
        @Expose
        private String descriptionEn;
        @SerializedName("description_ar")
        @Expose
        private String descriptionAr;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitleEn() {
            return titleEn;
        }

        public void setTitleEn(String titleEn) {
            this.titleEn = titleEn;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
        }

        public String getHeading1En() {
            return heading1En;
        }

        public void setHeading1En(String heading1En) {
            this.heading1En = heading1En;
        }

        public String getHeading1Ar() {
            return heading1Ar;
        }

        public void setHeading1Ar(String heading1Ar) {
            this.heading1Ar = heading1Ar;
        }

        public String getDescription1En() {
            return description1En;
        }

        public void setDescription1En(String description1En) {
            this.description1En = description1En;
        }

        public String getDescription1Ar() {
            return description1Ar;
        }

        public void setDescription1Ar(String description1Ar) {
            this.description1Ar = description1Ar;
        }

        public String getHeading2En() {
            return heading2En;
        }

        public void setHeading2En(String heading2En) {
            this.heading2En = heading2En;
        }

        public String getHeading2Ar() {
            return heading2Ar;
        }

        public void setHeading2Ar(String heading2Ar) {
            this.heading2Ar = heading2Ar;
        }

        public String getDescription2En() {
            return description2En;
        }

        public void setDescription2En(String description2En) {
            this.description2En = description2En;
        }

        public String getDescription2Ar() {
            return description2Ar;
        }

        public void setDescription2Ar(String description2Ar) {
            this.description2Ar = description2Ar;
        }

        public String getHeading3En() {
            return heading3En;
        }

        public void setHeading3En(String heading3En) {
            this.heading3En = heading3En;
        }

        public String getHeading3Ar() {
            return heading3Ar;
        }

        public void setHeading3Ar(String heading3Ar) {
            this.heading3Ar = heading3Ar;
        }

        public String getDescription3En() {
            return description3En;
        }

        public void setDescription3En(String description3En) {
            this.description3En = description3En;
        }

        public String getDescription3Ar() {
            return description3Ar;
        }

        public void setDescription3Ar(String description3Ar) {
            this.description3Ar = description3Ar;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getDescriptionEn() {
            return descriptionEn;
        }

        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
        }

        public String getDescriptionAr() {
            return descriptionAr;
        }

        public void setDescriptionAr(String descriptionAr) {
            this.descriptionAr = descriptionAr;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
