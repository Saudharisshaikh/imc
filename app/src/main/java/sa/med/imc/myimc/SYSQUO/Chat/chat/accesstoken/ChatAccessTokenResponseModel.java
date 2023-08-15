package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatAccessTokenResponseModel {

    @Expose
    @SerializedName("accessToken")
    private String accessToken;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
}
