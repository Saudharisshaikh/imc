package sa.med.imc.myimc.SYSQUO.Selection.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateRoomResponseModel {


    @Expose
    @SerializedName("validBufferRange")
    private boolean validBufferRange;
    @Expose
    @SerializedName("bookingCompletedMessage")
    private String bookingCompletedMessage;
    @Expose
    @SerializedName("bufferRangeMessage")
    private String bufferRangeMessage;
    @Expose
    @SerializedName("bookingCompletedStatus")
    private boolean bookingCompletedStatus;
    @Expose
    @SerializedName("bufferRangeAfter")
    private int bufferRangeAfter;
    @Expose
    @SerializedName("bufferRangeBefore")
    private int bufferRangeBefore;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public boolean getValidBufferRange() {
        return validBufferRange;
    }

    public void setValidBufferRange(boolean validBufferRange) {
        this.validBufferRange = validBufferRange;
    }

    public String getBufferRangeMessage() {
        return bufferRangeMessage;
    }

    public void setBufferRangeMessage(String bufferRangeMessage) {
        this.bufferRangeMessage = bufferRangeMessage;
    }

    public String getBookingCompletedMessage() {
        return bookingCompletedMessage;
    }

    public void setBookingCompletedMessage(String bookingCompletedMessage) {
        this.bookingCompletedMessage = bookingCompletedMessage;
    }

    public boolean getBookingCompletedStatus() {
        return bookingCompletedStatus;
    }

    public void setBookingCompletedStatus(boolean bookingCompletedStatus) {
        this.bookingCompletedStatus = bookingCompletedStatus;
    }

    public int getBufferRangeAfter() {
        return bufferRangeAfter;
    }

    public void setBufferRangeAfter(int bufferRangeAfter) {
        this.bufferRangeAfter = bufferRangeAfter;
    }

    public int getBufferRangeBefore() {
        return bufferRangeBefore;
    }

    public void setBufferRangeBefore(int bufferRangeBefore) {
        this.bufferRangeBefore = bufferRangeBefore;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data {
        @Expose
        @SerializedName("room")
        private Room room;
        @Expose
        @SerializedName("accessToken")
        private String accessToken;

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    public static class Room {
        @Expose
        @SerializedName("mediaRegion")
        private String mediaRegion;

        @Expose
        @SerializedName("recordParticipantsOnConnect")
        private boolean recordParticipantsOnConnect;
        @Expose
        @SerializedName("maxConcurrentPublishedTracks")
        private int maxConcurrentPublishedTracks;
        @Expose
        @SerializedName("maxParticipants")
        private int maxParticipants;
        @Expose
        @SerializedName("endTime")
        private String endTime;
        @Expose
        @SerializedName("statusCallbackMethod")
        private String statusCallbackMethod;
        @Expose
        @SerializedName("statusCallback")
        private String statusCallback;
        @Expose
        @SerializedName("uniqueName")
        private String uniqueName;
        @Expose
        @SerializedName("enableTurn")
        private boolean enableTurn;
        @Expose
        @SerializedName("accountSid")
        private String accountSid;
        @Expose
        @SerializedName("dateUpdated")
        private String dateUpdated;
        @Expose
        @SerializedName("dateCreated")
        private String dateCreated;
        @Expose
        @SerializedName("links")
        private Links links;
        @Expose
        @SerializedName("url")
        private String url;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("duration")
        private String duration;
        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("sid")
        private String sid;

        public String getMediaRegion() {
            return mediaRegion;
        }

        public void setMediaRegion(String mediaRegion) {
            this.mediaRegion = mediaRegion;
        }


        public boolean getRecordParticipantsOnConnect() {
            return recordParticipantsOnConnect;
        }

        public void setRecordParticipantsOnConnect(boolean recordParticipantsOnConnect) {
            this.recordParticipantsOnConnect = recordParticipantsOnConnect;
        }

        public int getMaxConcurrentPublishedTracks() {
            return maxConcurrentPublishedTracks;
        }

        public void setMaxConcurrentPublishedTracks(int maxConcurrentPublishedTracks) {
            this.maxConcurrentPublishedTracks = maxConcurrentPublishedTracks;
        }

        public int getMaxParticipants() {
            return maxParticipants;
        }

        public void setMaxParticipants(int maxParticipants) {
            this.maxParticipants = maxParticipants;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatusCallbackMethod() {
            return statusCallbackMethod;
        }

        public void setStatusCallbackMethod(String statusCallbackMethod) {
            this.statusCallbackMethod = statusCallbackMethod;
        }

        public String getStatusCallback() {
            return statusCallback;
        }

        public void setStatusCallback(String statusCallback) {
            this.statusCallback = statusCallback;
        }

        public String getUniqueName() {
            return uniqueName;
        }

        public void setUniqueName(String uniqueName) {
            this.uniqueName = uniqueName;
        }

        public boolean getEnableTurn() {
            return enableTurn;
        }

        public void setEnableTurn(boolean enableTurn) {
            this.enableTurn = enableTurn;
        }

        public String getAccountSid() {
            return accountSid;
        }

        public void setAccountSid(String accountSid) {
            this.accountSid = accountSid;
        }

        public String getDateUpdated() {
            return dateUpdated;
        }

        public void setDateUpdated(String dateUpdated) {
            this.dateUpdated = dateUpdated;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }

    public static class Links {
        @Expose
        @SerializedName("recording_rules")
        private String recording_rules;
        @Expose
        @SerializedName("participants")
        private String participants;
        @Expose
        @SerializedName("recordings")
        private String recordings;

        public String getRecording_rules() {
            return recording_rules;
        }

        public void setRecording_rules(String recording_rules) {
            this.recording_rules = recording_rules;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public String getRecordings() {
            return recordings;
        }

        public void setRecordings(String recordings) {
            this.recordings = recordings;
        }
    }
}
