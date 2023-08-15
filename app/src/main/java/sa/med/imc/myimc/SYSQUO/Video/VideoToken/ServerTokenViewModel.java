package sa.med.imc.myimc.SYSQUO.Video.VideoToken;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ServerTokenViewModel extends AndroidViewModel {

    private LiveData<ServerTokenResponseModel> addBalanceResponseLiveData;
    private ServerTokenRepository addBalanceRepository;



    public ServerTokenViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        addBalanceRepository = new ServerTokenRepository();
        addBalanceResponseLiveData = addBalanceRepository.AddBalResponseLiveData();
    }

    public void ServerToken(String lang, String identity, String roomName) {

        addBalanceRepository.serverTokenAPI(lang, identity, roomName);
    }

    public LiveData<ServerTokenResponseModel> getVolumesResponseLiveData() {
        return addBalanceResponseLiveData;
    }


}
