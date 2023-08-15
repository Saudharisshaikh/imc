package sa.med.imc.myimc.SYSQUO.Video.JoningRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceJoiningRequestMode {

    @Expose
    @SerializedName("userName")
    private String userName;
    @Expose
    @SerializedName("userId")
    private String userId;
    @Expose
    @SerializedName("roomName")
    private String roomName;
    @Expose
    @SerializedName("patientPlatform")
    private String patientPlatform = "Android";
    @Expose
    @SerializedName("pageSize")
    private int pageSize;
    @Expose
    @SerializedName("pageNumber")
    private int pageNumber;
    @Expose
    @SerializedName("doctorId")
    private String doctorId;
    @Expose
    @SerializedName("patientDeviceId")
    private String patientDeviceId;

    public String getPatientPlatform() {
        return patientPlatform;
    }

    public void setPatientPlatform(String patientPlatform) {
        this.patientPlatform = patientPlatform;
    }

    public String getPatientDeviceId() {
        return patientDeviceId;
    }

    public void setPatientDeviceId(String patientDeviceId) {
        this.patientDeviceId = patientDeviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
