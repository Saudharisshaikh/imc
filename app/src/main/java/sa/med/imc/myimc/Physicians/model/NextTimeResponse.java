package sa.med.imc.myimc.Physicians.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextTimeResponse implements Serializable  {


    public class Datum implements Serializable {
        @SerializedName("startTime")
        @Expose
        private String startTime;
        @SerializedName("sessionId")
        @Expose
        private Integer sessionId;
        @SerializedName("day")
        @Expose
        private Object day;

        @Override
        public String toString() {
            return "Datum{" +
                    "startTime='" + startTime + '\'' +
                    ", sessionId=" + sessionId +
                    ", day=" + day +
                    ", duration='" + duration + '\'' +
                    ", timeslotState=" + timeslotState +
                    ", sessionDateShort='" + sessionDateShort + '\'' +
                    ", sessionTimeShort='" + sessionTimeShort + '\'' +
                    ", sessionType=" + sessionType +
                    '}';
        }

        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("timeslotState")
        @Expose
        private Integer timeslotState;
        @SerializedName("sessionDateShort")
        @Expose
        private String sessionDateShort;
        @SerializedName("sessionTimeShort")
        @Expose
        private String sessionTimeShort;
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

        public String getSessionDateShort() {
            return sessionDateShort;
        }

        public void setSessionDateShort(String sessionDateShort) {
            this.sessionDateShort = sessionDateShort;
        }

        public String getSessionTimeShort() {
            return sessionTimeShort;
        }

        public void setSessionTimeShort(String sessionTimeShort) {
            this.sessionTimeShort = sessionTimeShort;
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
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
