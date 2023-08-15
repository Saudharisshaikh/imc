package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class DrTimeSlot implements Serializable {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("slotList")
    private List<SlotList> slotList;
    @Expose
    @SerializedName("dateTo")
    private String dateTo;
    @Expose
    @SerializedName("dateFrom")
    private String dateFrom;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SlotList> getSlotList() {
        return slotList;
    }

    public void setSlotList(List<SlotList> slotList) {
        this.slotList = slotList;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public static class SlotList {
        @Expose
        @SerializedName("slotBookingId")
        private String slotBookingId;
        @Expose
        @SerializedName("slotToTime")
        private String slotToTime;
        @Expose
        @SerializedName("slotFromTime")
        private String slotFromTime;
        @Expose
        @SerializedName("slotDate")
        private String slotDate;

        public String getSlotBookingId() {
            return slotBookingId;
        }

        public void setSlotBookingId(String slotBookingId) {
            this.slotBookingId = slotBookingId;
        }

        public String getSlotToTime() {
            return slotToTime;
        }

        public void setSlotToTime(String slotToTime) {
            this.slotToTime = slotToTime;
        }

        public String getSlotFromTime() {
            return slotFromTime;
        }

        public void setSlotFromTime(String slotFromTime) {
            this.slotFromTime = slotFromTime;
        }

        public String getSlotDate() {
            return slotDate;
        }

        public void setSlotDate(String slotDate) {
            this.slotDate = slotDate;
        }
    }
}
