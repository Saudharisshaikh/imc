package sa.med.imc.myimc.Clinics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClinicResponse {
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("specialityList")
    private List<SpecialityList> specialityList;
    @Expose
    @SerializedName("hospitalCode")
    private String hospitalCode;
    @Expose
    @SerializedName("hospitalDescription")
    private String hospitalDescription;


    public List<SpecialityList> getSpecialityList() {
        return specialityList;
    }

    public void setSpecialityList(List<SpecialityList> specialityList) {
        this.specialityList = specialityList;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalDescription() {
        return hospitalDescription;
    }

    public void setHospitalDescription(String hospitalDescription) {
        this.hospitalDescription = hospitalDescription;
    }

    public static class SpecialityList {
        @Expose
        @SerializedName("specialityDescription")
        private String specialityDescription;
        @Expose
        @SerializedName("arabicSpecialityDescription")
        private String arabicSpecialityDescription;
        @Expose
        @SerializedName("specialityCode")
        private String specialityCode;

        public String getSpecialityDescription() {
            return specialityDescription;
        }

        public String getArabicSpecialityDescription() {
            String s=arabicSpecialityDescription;
            try {
                if (arabicSpecialityDescription.trim().isEmpty()){
                    s=specialityDescription;
                }
            } catch (Exception e) {
                s=specialityDescription;
            }


            return s;
        }

        public void setArabicSpecialityDescription(String arabicSpecialityDescription) {
            this.arabicSpecialityDescription = arabicSpecialityDescription;
        }

        public void setSpecialityDescription(String specialityDescription) {
            this.specialityDescription = specialityDescription;
        }

        public String getSpecialityCode() {
            return specialityCode;
        }

        public void setSpecialityCode(String specialityCode) {
            this.specialityCode = specialityCode;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClinicResponse() {
    }

    public ClinicResponse(String status, List<SpecialityList> specialityList, String hospitalCode, String hospitalDescription) {
        this.status = status;
        this.specialityList = specialityList;
        this.hospitalCode = hospitalCode;
        this.hospitalDescription = hospitalDescription;
    }
/*@SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_items")
    @Expose
    private String total_items;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("physicians")
    @Expose
    private List<ClinicModel> clinicModels = null;

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

    public List<ClinicModel> getClinicModels() {
        return clinicModels;
    }

    public void setClinicModels(List<ClinicModel> clinicModels) {
        this.clinicModels = clinicModels;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public class ClinicModel {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("descEn")
        @Expose
        private String descEn;
        @SerializedName("descAr")
        @Expose
        private String descAr;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }*/

}
