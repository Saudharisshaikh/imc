package sa.med.imc.myimc.SignUp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NationalityResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("nationalities")
    @Expose
    private List<Nationality> nationalities = null;

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

    public List<Nationality> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<Nationality> nationalities) {
        this.nationalities = nationalities;
    }


    public class Nationality {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("countryNameAr")
        @Expose
        private String countryNameAr;
        @SerializedName("countryNameEn")
        @Expose
        private String countryNameEn;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCountryNameAr() {
            return countryNameAr;
        }

        public void setCountryNameAr(String countryNameAr) {
            this.countryNameAr = countryNameAr;
        }

        public String getCountryNameEn() {
            return countryNameEn;
        }

        public void setCountryNameEn(String countryNameEn) {
            this.countryNameEn = countryNameEn;
        }

    }
}
