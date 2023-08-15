package sa.med.imc.myimc.SYSQUO.Selection.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomResponseModel;
import sa.med.imc.myimc.SYSQUO.Selection.Repository.CreateRoomRepository;


public class CreateRoomViewModel extends AndroidViewModel {

    private LiveData<CreateRoomResponseModel> createRoomResponseModelLiveData;
    private CreateRoomRepository createRoomRepository;



    public CreateRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        createRoomRepository = new CreateRoomRepository();
        createRoomResponseModelLiveData = createRoomRepository.CreateRoomResponseLiveData();
    }

    public void CreateRoom(String token, String lang,String HospitalID, CreateRoomRequestModel createRoomRequestModel) {

        createRoomRepository.createRoomAPI(token, lang,HospitalID, createRoomRequestModel);
    }

    public LiveData<CreateRoomResponseModel> getVolumesResponseLiveData() {
        return createRoomResponseModelLiveData;
    }


}
