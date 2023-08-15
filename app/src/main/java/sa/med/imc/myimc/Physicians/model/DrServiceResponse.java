package sa.med.imc.myimc.Physicians.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class DrServiceResponse implements Serializable {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("serviceList")
    private List<ServiceList> serviceList;
    @Expose
    @SerializedName("specialityCode")
    private String specialityCode;
    @Expose
    @SerializedName("providerCode")
    private String providerCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ServiceList> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceList> serviceList) {
        this.serviceList = serviceList;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public static class ServiceList {
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("serviceDescription")
        private String serviceDescription;
        @Expose
        @SerializedName("arabicServiceDescription")
        private String arabicServiceDescription;
        @Expose
        @SerializedName("serviceCode")
        private String serviceCode;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getArabicServiceDescription() {
            String s=serviceDescription;

            try {
                if (!arabicServiceDescription.trim().isEmpty()){
                    s=arabicServiceDescription;
                }else {
                    s=serviceDescription;
                }
            } catch (Exception e) {
                s=serviceDescription ;          }


            return s;
        }

        public void setArabicServiceDescription(String arabicServiceDescription) {
            this.arabicServiceDescription = arabicServiceDescription;
        }
    }
}
