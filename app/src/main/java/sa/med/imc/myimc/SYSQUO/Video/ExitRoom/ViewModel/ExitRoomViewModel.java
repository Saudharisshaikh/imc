package sa.med.imc.myimc.SYSQUO.Video.ExitRoom.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Repository.ExitRoomRepository;


public class ExitRoomViewModel extends AndroidViewModel {

    private LiveData<ExitRoomResponseModel> exitRoomResponseModelLiveData;
    private ExitRoomRepository exitRoomRepository;



    public ExitRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        exitRoomRepository = new ExitRoomRepository();
        exitRoomResponseModelLiveData = exitRoomRepository.AddBalResponseLiveData();
    }

    public void ExitRoom(String token, String lang, ExitRoomRequestModel exitRoomRequestModel) {

        exitRoomRepository.exitRoomAPI(token, lang, exitRoomRequestModel);
    }

    public LiveData<ExitRoomResponseModel> getVolumesResponseLiveData() {
        return exitRoomResponseModelLiveData;
    }


}
