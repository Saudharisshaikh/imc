package sa.med.imc.myimc.SYSQUO.Video.VideoToken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoAccessTokenRequestModel {

    @Expose
    @SerializedName("userEmail")
    private String userEmail;
    @Expose
    @SerializedName("roomName")
    private String roomName;
    @Expose
    @SerializedName("pageSize")
    private int pageSize;
    @Expose
    @SerializedName("pageNumber")
    private int pageNumber;

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
}
