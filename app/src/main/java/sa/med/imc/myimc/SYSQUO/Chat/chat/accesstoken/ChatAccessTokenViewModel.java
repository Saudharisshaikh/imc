package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ChatAccessTokenViewModel extends AndroidViewModel {

    private LiveData<ChatAccessTokenResponseModel> chatAccessTokenResponseModelLiveData;
    private ChatAccessTokenRepository chatAccessTokenRepository;



    public ChatAccessTokenViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        chatAccessTokenRepository = new ChatAccessTokenRepository();
        chatAccessTokenResponseModelLiveData = chatAccessTokenRepository.ChatAccessTokenResponseLiveData();
    }

    public void ChatAccessToken(String token, String lang, ChatAccessTokenRequetModel chatAccessTokenRequetModel) {

        chatAccessTokenRepository.chatAccessTokenAPI(token, lang, chatAccessTokenRequetModel);
    }

    public LiveData<ChatAccessTokenResponseModel> getVolumesResponseLiveData() {
        return chatAccessTokenResponseModelLiveData;
    }


}
