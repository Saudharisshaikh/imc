package sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisconnectRoomRequestModel {

    @Expose
    @SerializedName("userId")
    private String userId;
    @Expose
    @SerializedName("doctorId")
    private String doctorId;
    @Expose
    @SerializedName("roomName")
    private String roomName;

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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
