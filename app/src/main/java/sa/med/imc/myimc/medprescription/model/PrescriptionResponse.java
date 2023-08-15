package sa.med.imc.myimc.medprescription.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("prescription")
    @Expose
    private List<Prescription> prescription = null;
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

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
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


    public class Prescription {

        @SerializedName("precriptionNo")
        @Expose
        private String precriptionNo;
        @SerializedName("docName")
        @Expose
        private String docName;
        @SerializedName("docNameAr")
        @Expose
        private String docNameAr;
        @SerializedName("orderDate")
        @Expose
        private String  orderDate;
        @SerializedName("episodeNumber")
        @Expose
        private Integer episodeNumber;

        @SerializedName("orderNumber")
        @Expose
        private Integer orderNumber;

        @SerializedName("invoiceCount")
        @Expose
        private Integer invoiceCount = 0;

        @SerializedName("episodeType")
        private String episodeType;

        public String getPrecriptionNo() {
            return precriptionNo;
        }

        public void setPrecriptionNo(String precriptionNo) {
            this.precriptionNo = precriptionNo;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getDocNameAr() {
            return docNameAr;
        }

        public void setDocNameAr(String docNameAr) {
            this.docNameAr = docNameAr;
        }

        public Integer getEpisodeNumber() {
            return episodeNumber;
        }

        public Integer getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(Integer orderNumber) {
            this.orderNumber = orderNumber;
        }

        public void setEpisodeNumber(Integer episodeNumber) {
            this.episodeNumber = episodeNumber;
        }

        public String getEpisodeType() {
            return episodeType;
        }

        public void setEpisodeType(String episodeType) {
            this.episodeType = episodeType;
        }

        public Integer getInvoiceCount() {
            return invoiceCount;
        }

        public void setInvoiceCount(Integer invoiceCount) {
            this.invoiceCount = invoiceCount;
        }
    }
}
