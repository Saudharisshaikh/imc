package sa.med.imc.myimc.SYSQUO.EmergencyCall.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallResponseModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Repository.EmergencyCallRepository;


public class EmergencyCallViewModel extends AndroidViewModel {

    private LiveData<EmergencyCallResponseModel> emergencyCallResponseModelLiveData;
    private EmergencyCallRepository emergencyCallRepository;



    public EmergencyCallViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        emergencyCallRepository = new EmergencyCallRepository();
        emergencyCallResponseModelLiveData = emergencyCallRepository.EmergencyCallResponseLiveData();
    }

    public void EmergencyCall(String token, String lang, EmergencyCallRequestModel emergencyCallRequestModel) {

        emergencyCallRepository.emergencyCallAPI(token, lang, emergencyCallRequestModel);
    }

    public LiveData<EmergencyCallResponseModel> getVolumesResponseLiveData() {
        return emergencyCallResponseModelLiveData;
    }


}
