package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {

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
    }
}
