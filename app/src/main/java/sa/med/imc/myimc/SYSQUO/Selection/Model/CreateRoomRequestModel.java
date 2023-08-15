package sa.med.imc.myimc.SYSQUO.Selection.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateRoomRequestModel {

    @Expose
    @SerializedName("userEmail")
    private String userEmail;
    @Expose
    @SerializedName("roomName")
    private String roomName;
    @Expose
    @SerializedName("bookingId")
    private String bookingId;
    @Expose
    @SerializedName("pageSize")
    private int pageSize;
    @Expose
    @SerializedName("pageNumber")
    private int pageNumber;
    @Expose
    @SerializedName("userId")
    private String userId;
    @Expose
    @SerializedName("doctorId")
    private String doctorId;
    @Expose
    @SerializedName("bookingSlotTime")
    private String bookingSlotTime;
    @Expose
    @SerializedName("deviceType")
    private String deviceType;
    @Expose
    @SerializedName("deviceToken")
    private String deviceToken;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getBookingSlotTime() {
        return bookingSlotTime;
    }

    public void setBookingSlotTime(String bookingSlotTime) {
        this.bookingSlotTime = bookingSlotTime;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
