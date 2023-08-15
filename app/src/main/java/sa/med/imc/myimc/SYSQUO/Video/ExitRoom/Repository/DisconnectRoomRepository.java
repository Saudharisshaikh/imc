package sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.SYSQUO.Network.APIClass;
import sa.med.imc.myimc.SYSQUO.Network.RetrofitInstance;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomResponseModel;

public class DisconnectRoomRepository {

    private MutableLiveData<DisconnectRoomResponseModel> mutableLiveData;

    public DisconnectRoomRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void disconnectRoomAPI(@NonNull String token, @NonNull String lang, DisconnectRoomRequestModel disconnectRoomRequestModel){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<DisconnectRoomResponseModel> call;
//        if(isEmergency) {
            call = apiDataService.disconnectRoom("Bearer "+token, lang, disconnectRoomRequestModel);
//        }
//        else {
//            call = apiDataService.exitRoom(identity);
//        }

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",new Gson().toJson(disconnectRoomRequestModel));

        call.enqueue(new Callback<DisconnectRoomResponseModel>() {
            @Override
            public void onResponse(Call<DisconnectRoomResponseModel> call, Response<DisconnectRoomResponseModel> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DisconnectRoomResponseModel> call, Throwable t) {
                Log.e("abcd",t.toString());

                mutableLiveData.postValue(null);
            }
        });
    }




    public LiveData<DisconnectRoomResponseModel> DisconnectRoomResponseLiveData() {
        return mutableLiveData;
    }


}
