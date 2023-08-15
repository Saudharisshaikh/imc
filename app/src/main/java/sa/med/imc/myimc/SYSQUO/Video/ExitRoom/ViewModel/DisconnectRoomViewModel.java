package sa.med.imc.myimc.SYSQUO.Video.ExitRoom.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Repository.DisconnectRoomRepository;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Repository.ExitRoomRepository;


public class DisconnectRoomViewModel extends AndroidViewModel {

    private LiveData<DisconnectRoomResponseModel> disconnectRoomResponseModelLiveData;
    private DisconnectRoomRepository disconnectRoomRepository;



    public DisconnectRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        disconnectRoomRepository = new DisconnectRoomRepository();
        disconnectRoomResponseModelLiveData = disconnectRoomRepository.DisconnectRoomResponseLiveData();
    }

    public void DisconnectRoom(String token, String lang, DisconnectRoomRequestModel disconnectRoomRequestModel) {

        disconnectRoomRepository.disconnectRoomAPI(token, lang, disconnectRoomRequestModel);
    }

    public LiveData<DisconnectRoomResponseModel> getVolumesResponseLiveData() {
        return disconnectRoomResponseModelLiveData;
    }


}
