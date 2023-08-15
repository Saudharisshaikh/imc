package sa.med.imc.myimc.SYSQUO.EmergencyCall.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyCallRequestModel {

    @Expose
    @SerializedName("userEmail")
    private String userEmail;
    @Expose
    @SerializedName("userId")
    private String userId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
