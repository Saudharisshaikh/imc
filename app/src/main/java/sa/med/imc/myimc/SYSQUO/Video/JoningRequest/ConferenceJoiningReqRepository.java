package sa.med.imc.myimc.SYSQUO.Video.JoningRequest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.SYSQUO.Network.APIClass;
import sa.med.imc.myimc.SYSQUO.Network.RetrofitInstance;
import sa.med.imc.myimc.SYSQUO.Video.VideoToken.ServerTokenResponseModel;

public class ConferenceJoiningReqRepository {

    private MutableLiveData<ConferenceJoiningResponseMode> mutableLiveData;

    public ConferenceJoiningReqRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void serverTokenAPI(String token,String HospitalID, String lang, ConferenceJoiningRequestMode conferenceJoiningRequestMode){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<ConferenceJoiningResponseMode> call = apiDataService.conferenceJoiningReq("Bearer "+token,HospitalID, lang, conferenceJoiningRequestMode);

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",new Gson().toJson(conferenceJoiningRequestMode));

        call.enqueue(new Callback<ConferenceJoiningResponseMode>() {
            @Override
            public void onResponse(Call<ConferenceJoiningResponseMode> call, Response<ConferenceJoiningResponseMode> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ConferenceJoiningResponseMode> call, Throwable t) {
                mutableLiveData.postValue(null);
                Log.e("abcd",t.toString());
            }
        });
    }




    public LiveData<ConferenceJoiningResponseMode> ConfRequResponseLiveData() {
        return mutableLiveData;
    }


}
