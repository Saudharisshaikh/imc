package sa.med.imc.myimc.SYSQUO.Video.JoningRequest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ConferenceJoiningReqViewModel extends AndroidViewModel {

    private LiveData<ConferenceJoiningResponseMode> addBalanceResponseLiveData;
    private ConferenceJoiningReqRepository addBalanceRepository;



    public ConferenceJoiningReqViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        addBalanceRepository = new ConferenceJoiningReqRepository();
        addBalanceResponseLiveData = addBalanceRepository.ConfRequResponseLiveData();
    }

    public void ConfRequ(String token,String HospitalID, String Lang, ConferenceJoiningRequestMode conferenceJoiningRequestMode) {

        addBalanceRepository.serverTokenAPI(token,HospitalID ,Lang, conferenceJoiningRequestMode);
    }

    public LiveData<ConferenceJoiningResponseMode> getVolumesResponseLiveData() {
        return addBalanceResponseLiveData;
    }


}
