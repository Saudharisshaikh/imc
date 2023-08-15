package sa.med.imc.myimc.Managebookings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingResponseNew {

    @Expose
    @SerializedName("bookings")
    private List<Bookings> bookings;
    @Expose
    @SerializedName("dashboardBuffer")
    private int dashboardBuffer;
    @Expose
    @SerializedName("totalBookings")
    private String totalBookings;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private String status;

    public List<Bookings> getBookings() {
        return bookings;
    }

    public void setBookings(List<Bookings> bookings) {
        this.bookings = bookings;
    }

    public int getDashboardBuffer() {
        return dashboardBuffer;
    }

    public void setDashboardBuffer(int dashboardBuffer) {
        this.dashboardBuffer = dashboardBuffer;
    }

    public String getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(String totalBookings) {
        this.totalBookings = totalBookings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Bookings {
        @Expose
        @SerializedName("isPaid")
        private boolean isPaid;
        @Expose
        @SerializedName("slotBookingId")
        private String slotBookingId;
        @Expose
        @SerializedName("serviceType")
        private String serviceType;
        @Expose
        @SerializedName("serviceDescription")
        private String serviceDescription;
        @Expose
        @SerializedName("serviceCode")
        private String serviceCode;
        @Expose
        @SerializedName("specialityDescription")
        private String specialityDescription;
        @Expose
        @SerializedName("specialityCode")
        private String specialityCode;
        @Expose
        @SerializedName("promisForm")
        private String promisForm;
        @Expose
        @SerializedName("service")
        private String service;
        @Expose
        @SerializedName("bookingStatus")
        private int bookingStatus;
        @Expose
        @SerializedName("hidePayCheckin")
        private boolean hidePayCheckin;
        @Expose
        @SerializedName("isArrived")
        private String isArrived;
        @Expose
        @SerializedName("paymentStatus")
        private String paymentStatus;
        @Expose
        @SerializedName("selfCheckIn")
        private String selfCheckIn;
        @Expose
        @SerializedName("teleBooking")
        private String teleBooking;
        @Expose
        @SerializedName("apptDateString")
        private String apptDateString;
        @Expose
        @SerializedName("apptDate")
        private String apptDate;
        @Expose
        @SerializedName("docCode")
        private String docCode;
        @Expose
        @SerializedName("patName")
        private String patName;
        @Expose
        @SerializedName("patId")
        private String patId;
        @Expose
        @SerializedName("id")
        private String id;

        public boolean getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(boolean isPaid) {
            this.isPaid = isPaid;
        }

        public String getSlotBookingId() {
            return slotBookingId;
        }

        public void setSlotBookingId(String slotBookingId) {
            this.slotBookingId = slotBookingId;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
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

        public String getSpecialityDescription() {
            return specialityDescription;
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

        public String getPromisForm() {
            return promisForm;
        }

        public void setPromisForm(String promisForm) {
            this.promisForm = promisForm;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public int getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(int bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public boolean getHidePayCheckin() {
            return hidePayCheckin;
        }

        public void setHidePayCheckin(boolean hidePayCheckin) {
            this.hidePayCheckin = hidePayCheckin;
        }

        public String getIsArrived() {
            return isArrived;
        }

        public void setIsArrived(String isArrived) {
            this.isArrived = isArrived;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getSelfCheckIn() {
            return selfCheckIn;
        }

        public void setSelfCheckIn(String selfCheckIn) {
            this.selfCheckIn = selfCheckIn;
        }

        public String getTeleBooking() {
            return teleBooking;
        }

        public void setTeleBooking(String teleBooking) {
            this.teleBooking = teleBooking;
        }

        public String getApptDateString() {
            return apptDateString;
        }

        public void setApptDateString(String apptDateString) {
            this.apptDateString = apptDateString;
        }

        public String getApptDate() {
            return apptDate;
        }

        public void setApptDate(String apptDate) {
            this.apptDate = apptDate;
        }

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getPatName() {
            return patName;
        }

        public void setPatName(String patName) {
            this.patName = patName;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
