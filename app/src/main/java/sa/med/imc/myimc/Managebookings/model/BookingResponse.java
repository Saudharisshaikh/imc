package sa.med.imc.myimc.Managebookings.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BookingResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("totalBookings")
    @Expose
    private String totalBookings;
    @SerializedName("dashboardBuffer")
    @Expose
    private Integer dashBoardBuffer = 0;
    @SerializedName("code")
    @Expose
    private String code = "";
    @SerializedName("bookings")
    @Expose
    private List<Booking> bookings = null;

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

    public String getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(String totalBookings) {
        this.totalBookings = totalBookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Integer getDashBoardBuffer() {
        return dashBoardBuffer;
    }

    public void setDashBoardBuffer(Integer dashBoardBuffer) {
        this.dashBoardBuffer = dashBoardBuffer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
