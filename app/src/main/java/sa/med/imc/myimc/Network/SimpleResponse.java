package sa.med.imc.myimc.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OID")
    @Expose
    private String OID;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("releasedBuild")
    @Expose
    private String releasedBuild;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("registrationOID")
    @Expose
    private String registrationOID;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("updateApp")
    @Expose
    private String updateApp;

    @SerializedName("chatBotLink")
    @Expose
    private String chatBotLink;

    public String getChatBotLink() {
        return chatBotLink;
    }

    public void setChatBotLink(String chatBotLink) {
        this.chatBotLink = chatBotLink;
    }

    public String getUpdateApp() {
        return updateApp;
    }

    public void setUpdateApp(String updateApp) {
        this.updateApp = updateApp;
    }

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


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReleasedBuild() {
        return releasedBuild;
    }

    public void setReleasedBuild(String releasedBuild) {
        this.releasedBuild = releasedBuild;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getRegistrationOID() {
        return registrationOID;
    }

    public void setRegistrationOID(String registrationOID) {
        this.registrationOID = registrationOID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
