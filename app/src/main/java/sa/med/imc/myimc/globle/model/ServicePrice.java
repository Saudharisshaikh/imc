package sa.med.imc.myimc.globle.model;

public class ServicePrice {

    String slotBookingId;
    String orderPrice;
    String payorShare;
    String patientShare;
    String status;
    boolean is_final_price;

    public ServicePrice() {
    }

    public ServicePrice(String slotBookingId, String orderPrice, String payorShare, String patientShare, String status, boolean is_final_price) {
        this.slotBookingId = slotBookingId;
        this.orderPrice = orderPrice;
        this.payorShare = payorShare;
        this.patientShare = patientShare;
        this.status = status;
        this.is_final_price = is_final_price;
    }

    public String getSlotBookingId() {
        return slotBookingId;
    }

    public void setSlotBookingId(String slotBookingId) {
        this.slotBookingId = slotBookingId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPayorShare() {
        return payorShare;
    }

    public void setPayorShare(String payorShare) {
        this.payorShare = payorShare;
    }

    public String getPatientShare() {
        return patientShare;
    }

    public void setPatientShare(String patientShare) {
        this.patientShare = patientShare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIs_final_price() {
        return is_final_price;
    }

    public void setIs_final_price(boolean is_final_price) {
        this.is_final_price = is_final_price;
    }
}
