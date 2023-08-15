package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotsResponse {


    public class TimeSlot {

        @SerializedName("startTime")
        @Expose
        private String startTime;
        @SerializedName("sessionId")
        @Expose
        private Integer sessionId;
        @SerializedName("day")
        @Expose
        private Object day;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("timeslotState")
        @Expose
        private Integer timeslotState;
        @SerializedName("sessionType")
        @Expose
        private Integer sessionType;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer getSessionId() {
            return sessionId;
        }

        public void setSessionId(Integer sessionId) {
            this.sessionId = sessionId;
        }

        public Object getDay() {
            return day;
        }

        public void setDay(Object day) {
            this.day = day;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public Integer getTimeslotState() {
            return timeslotState;
        }

        public void setTimeslotState(Integer timeslotState) {
            this.timeslotState = timeslotState;
        }

        public Integer getSessionType() {
            return sessionType;
        }

        public void setSessionType(Integer sessionType) {
            this.sessionType = sessionType;
        }
    }


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TimeSlot> timeSlots = null;

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


    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
