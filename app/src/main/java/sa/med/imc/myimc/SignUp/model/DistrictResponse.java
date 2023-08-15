package sa.med.imc.myimc.SignUp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("districts")
    @Expose
    private List<District> districts = null;

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

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public class District {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("districtName")
        @Expose
        private String districtName;
        @SerializedName("districtNameAr")
        @Expose
        private String districtNameAr;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getDistrictNameAr() {
            return districtNameAr;
        }

        public void setDistrictNameAr(String districtNameAr) {
            this.districtNameAr = districtNameAr;
        }
    }
}
