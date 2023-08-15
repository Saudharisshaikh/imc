package sa.med.imc.myimc.SYSQUO.Video.VideoToken;

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

public class ServerTokenRepository {

    private MutableLiveData<ServerTokenResponseModel> mutableLiveData;

    public ServerTokenRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void serverTokenAPI(@NonNull String lang, @NonNull String identity, @NonNull String roomName){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<ServerTokenResponseModel> call = apiDataService.serverToken(lang, identity, roomName);

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",identity+" "+roomName);

        call.enqueue(new Callback<ServerTokenResponseModel>() {
            @Override
            public void onResponse(Call<ServerTokenResponseModel> call, Response<ServerTokenResponseModel> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ServerTokenResponseModel> call, Throwable t) {
                Log.e("abcd",t.toString());

                mutableLiveData.postValue(null);
            }
        });
    }




    public LiveData<ServerTokenResponseModel> AddBalResponseLiveData() {
        return mutableLiveData;
    }


}
