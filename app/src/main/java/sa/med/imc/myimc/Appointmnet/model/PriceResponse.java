package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("companyPay")
    @Expose
    private Double companyPay;
    @SerializedName("companyDiscount")
    @Expose
    private Double companyDiscount;
    @SerializedName("companyVAT")
    @Expose
    private Double companyVAT;
    @SerializedName("patientPay")
    @Expose
    private Double patientPay;
    @SerializedName("paientDiscount")
    @Expose
    private Double paientDiscount;
    @SerializedName("patientVAT")
    @Expose
    private Double patientVAT;
    @SerializedName("itemPrice")
    @Expose
    private Double itemPrice;
    @SerializedName("vatPercent")
    @Expose
    private Double vatPercent;

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

    public Double getCompanyPay() {
        return companyPay;
    }

    public void setCompanyPay(Double companyPay) {
        this.companyPay = companyPay;
    }

    public Double getCompanyDiscount() {
        return companyDiscount;
    }

    public void setCompanyDiscount(Double companyDiscount) {
        this.companyDiscount = companyDiscount;
    }

    public Double getCompanyVAT() {
        return companyVAT;
    }

    public void setCompanyVAT(Double companyVAT) {
        this.companyVAT = companyVAT;
    }

    public Double getPatientPay() {
        return patientPay;
    }

    public void setPatientPay(Double patientPay) {
        this.patientPay = patientPay;
    }

    public Double getPaientDiscount() {
        return paientDiscount;
    }

    public void setPaientDiscount(Double paientDiscount) {
        this.paientDiscount = paientDiscount;
    }

    public Double getPatientVAT() {
        return patientVAT;
    }

    public void setPatientVAT(Double patientVAT) {
        this.patientVAT = patientVAT;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(Double vatPercent) {
        this.vatPercent = vatPercent;
    }

    @Override
    public String toString() {
        return "PriceResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", companyPay=" + companyPay +
                ", companyDiscount=" + companyDiscount +
                ", companyVAT=" + companyVAT +
                ", patientPay=" + patientPay +
                ", paientDiscount=" + paientDiscount +
                ", patientVAT=" + patientVAT +
                ", itemPrice=" + itemPrice +
                ", vatPercent=" + vatPercent +
                '}';
    }
}
