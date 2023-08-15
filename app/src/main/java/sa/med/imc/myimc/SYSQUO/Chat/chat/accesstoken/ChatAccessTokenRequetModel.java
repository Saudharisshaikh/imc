package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatAccessTokenRequetModel {

    @Expose
    @SerializedName("identity")
    private String identity;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
