package sa.med.imc.myimc.Profile.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicationRespone implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<MedicationModel> medicationModels = null;
    @SerializedName("total_items")
    @Expose
    private String totalItems;
    @SerializedName("total_pages")
    @Expose
    private String totalPages;

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

    public List<MedicationModel> getMedicationModels() {
        return medicationModels;
    }

    public void setMedicationModels(List<MedicationModel> medicationModels) {
        this.medicationModels = medicationModels;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }


    public class MedicationModel implements Serializable {

        @SerializedName("dispendingId")
        @Expose
        private long dispendingId;
        @SerializedName("episodeNo")
        @Expose
        private Integer episodeNo;
        @SerializedName("deliveryDate")
        @Expose
        private String deliveryDate;
        @SerializedName("patId")
        @Expose
        private String patId;
        @SerializedName("patientName")
        @Expose
        private String patientName;
        @SerializedName("descp")
        @Expose
        private String descp;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("uom")
        @Expose
        private String uom;
        @SerializedName("uomDescription")
        @Expose
        private String uomDescription;
        @SerializedName("uomDescriptionAr")
        @Expose
        private String uomDescriptionAr;
        @SerializedName("deliveryNum")
        @Expose
        private Integer deliveryNum;
        @SerializedName("deliveryNumLine")
        @Expose
        private Integer deliveryNumLine;
        @SerializedName("orderNo")
        @Expose
        private Integer orderNo;
        @SerializedName("orderNoLine")
        @Expose
        private Integer orderNoLine;
        @SerializedName("expired")
        @Expose
        private Integer expired;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("freq")
        @Expose
        private String freq;
        @SerializedName("freqDescription")
        @Expose
        private String freqDescription;
        @SerializedName("freqDescriptionAr")
        @Expose
        private String freqDescriptionAr;
        @SerializedName("strenght")
        @Expose
        private Integer strenght;
        @SerializedName("strenghtUom")
        @Expose
        private String strenghtUom;
        @SerializedName("urgencyCode")
        @Expose
        private String urgencyCode;
        @SerializedName("urgencyDescription")
        @Expose
        private String urgencyDescription;
        @SerializedName("route")
        @Expose
        private String route;
        @SerializedName("routeDescription")
        @Expose
        private String routeDescription;
        @SerializedName("routeDescriptionAr")
        @Expose
        private String routeDescriptionAr;
        @SerializedName("noOfDays")
        @Expose
        private String noOfDays;
        @SerializedName("inActiveMed")
        @Expose
        private String inActiveMed;
        @SerializedName("hasDelivery")
        @Expose
        private String hasDelivery;
        @SerializedName("returnedDelivery")
        @Expose
        private String returnedDelivery;

        public long getDispendingId() {
            return dispendingId;
        }

        public void setDispendingId(long dispendingId) {
            this.dispendingId = dispendingId;
        }

        public Integer getEpisodeNo() {
            return episodeNo;
        }

        public void setEpisodeNo(Integer episodeNo) {
            this.episodeNo = episodeNo;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getDescp() {
            return descp;
        }

        public void setDescp(String descp) {
            this.descp = descp;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public String getUomDescription() {
            return uomDescription;
        }

        public void setUomDescription(String uomDescription) {
            this.uomDescription = uomDescription;
        }

        public String getUomDescriptionAr() {
            return uomDescriptionAr;
        }

        public void setUomDescriptionAr(String uomDescriptionAr) {
            this.uomDescriptionAr = uomDescriptionAr;
        }

        public Integer getDeliveryNum() {
            return deliveryNum;
        }

        public void setDeliveryNum(Integer deliveryNum) {
            this.deliveryNum = deliveryNum;
        }

        public Integer getDeliveryNumLine() {
            return deliveryNumLine;
        }

        public void setDeliveryNumLine(Integer deliveryNumLine) {
            this.deliveryNumLine = deliveryNumLine;
        }

        public Integer getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Integer orderNo) {
            this.orderNo = orderNo;
        }

        public Integer getOrderNoLine() {
            return orderNoLine;
        }

        public void setOrderNoLine(Integer orderNoLine) {
            this.orderNoLine = orderNoLine;
        }

        public Integer getExpired() {
            return expired;
        }

        public void setExpired(Integer expired) {
            this.expired = expired;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getFreq() {
            return freq;
        }

        public void setFreq(String freq) {
            this.freq = freq;
        }

        public String getFreqDescription() {
            return freqDescription;
        }

        public void setFreqDescription(String freqDescription) {
            this.freqDescription = freqDescription;
        }

        public String getFreqDescriptionAr() {
            return freqDescriptionAr;
        }

        public void setFreqDescriptionAr(String freqDescriptionAr) {
            this.freqDescriptionAr = freqDescriptionAr;
        }

        public Integer getStrenght() {
            return strenght;
        }

        public void setStrenght(Integer strenght) {
            this.strenght = strenght;
        }

        public String getStrenghtUom() {
            return strenghtUom;
        }

        public void setStrenghtUom(String strenghtUom) {
            this.strenghtUom = strenghtUom;
        }

        public String getUrgencyCode() {
            return urgencyCode;
        }

        public void setUrgencyCode(String urgencyCode) {
            this.urgencyCode = urgencyCode;
        }

        public String getUrgencyDescription() {
            return urgencyDescription;
        }

        public void setUrgencyDescription(String urgencyDescription) {
            this.urgencyDescription = urgencyDescription;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getRouteDescription() {
            return routeDescription;
        }

        public void setRouteDescription(String routeDescription) {
            this.routeDescription = routeDescription;
        }

        public String getRouteDescriptionAr() {
            return routeDescriptionAr;
        }

        public void setRouteDescriptionAr(String routeDescriptionAr) {
            this.routeDescriptionAr = routeDescriptionAr;
        }

        public String getNoOfDays() {
            return noOfDays;
        }

        public void setNoOfDays(String noOfDays) {
            this.noOfDays = noOfDays;
        }

        public String getInActiveMed() {
            return inActiveMed;
        }

        public void setInActiveMed(String inActiveMed) {
            this.inActiveMed = inActiveMed;
        }

        public String getHasDelivery() {
            return hasDelivery;
        }

        public void setHasDelivery(String hasDelivery) {
            this.hasDelivery = hasDelivery;
        }

        public String getReturnedDelivery() {
            return returnedDelivery;
        }

        public void setReturnedDelivery(String returnedDelivery) {
            this.returnedDelivery = returnedDelivery;
        }


    }
}
