package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import sa.med.imc.myimc.Physicians.model.NextTimeResponse;

public class TimeSlotsNextResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<NextTimeResponse.Datum> timeSlots = null;

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


    public List<NextTimeResponse.Datum> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<NextTimeResponse.Datum> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
