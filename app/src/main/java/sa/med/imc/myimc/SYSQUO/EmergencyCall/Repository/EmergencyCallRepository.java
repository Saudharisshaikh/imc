package sa.med.imc.myimc.SYSQUO.EmergencyCall.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallResponseModel;
import sa.med.imc.myimc.SYSQUO.Network.APIClass;
import sa.med.imc.myimc.SYSQUO.Network.RetrofitInstance;

public class EmergencyCallRepository {

    private MutableLiveData<EmergencyCallResponseModel> mutableLiveData;

    public EmergencyCallRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void emergencyCallAPI(@NonNull String token, @NonNull String lang, EmergencyCallRequestModel emergencyCallRequestModel){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<EmergencyCallResponseModel> call = apiDataService.emergencyCall("Bearer "+token, lang, emergencyCallRequestModel);

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",new Gson().toJson(emergencyCallRequestModel));

        call.enqueue(new Callback<EmergencyCallResponseModel>() {
            @Override
            public void onResponse(Call<EmergencyCallResponseModel> call, Response<EmergencyCallResponseModel> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<EmergencyCallResponseModel> call, Throwable t) {
                Log.e("abcd",t.toString());

                mutableLiveData.postValue(null);
            }
        });
    }




    public LiveData<EmergencyCallResponseModel> EmergencyCallResponseLiveData() {
        return mutableLiveData;
    }


}
