package sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExitRoomRequestModel {

    @Expose
    @SerializedName("roomId")
    private int roomId;
    @Expose
    @SerializedName("pageSize")
    private int pageSize;
    @Expose
    @SerializedName("pageNumber")
    private int pageNumber;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
