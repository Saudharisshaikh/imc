package sa.med.imc.myimc.SYSQUO.Selection.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.SYSQUO.Network.APIClass;
import sa.med.imc.myimc.SYSQUO.Network.RetrofitInstance;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomResponseModel;

public class CreateRoomRepository {

    private MutableLiveData<CreateRoomResponseModel> mutableLiveData;

    public CreateRoomRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void createRoomAPI(String token, String lang,String HospitalID, CreateRoomRequestModel createRoomRequestModel){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<CreateRoomResponseModel> call = apiDataService.createRoom("Bearer "+token, lang,
                HospitalID,createRoomRequestModel);

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",new Gson().toJson(createRoomRequestModel));

        call.enqueue(new Callback<CreateRoomResponseModel>() {
            @Override
            public void onResponse(Call<CreateRoomResponseModel> call, Response<CreateRoomResponseModel> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());

                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CreateRoomResponseModel> call, Throwable t) {
                mutableLiveData.postValue(null);
                Log.e("abcd",t.toString());

            }
        });
    }




    public LiveData<CreateRoomResponseModel> CreateRoomResponseLiveData() {
        return mutableLiveData;
    }


}
