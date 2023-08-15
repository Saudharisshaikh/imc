package sa.med.imc.myimc.SYSQUO.Chat.chat.accesstoken;

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

public class ChatAccessTokenRepository {

    private MutableLiveData<ChatAccessTokenResponseModel> mutableLiveData;

    public ChatAccessTokenRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    public void chatAccessTokenAPI(@NonNull String token, @NonNull String lang, ChatAccessTokenRequetModel chatAccessToken){
        APIClass apiDataService = RetrofitInstance.getInstance().create(APIClass.class);
        Call<ChatAccessTokenResponseModel> call = apiDataService.chatAccessToken(token, lang, chatAccessToken);

        Log.e("abcd",call.request().url().toString());
        Log.e("abcd",new Gson().toJson(chatAccessToken));

        call.enqueue(new Callback<ChatAccessTokenResponseModel>() {
            @Override
            public void onResponse(Call<ChatAccessTokenResponseModel> call, Response<ChatAccessTokenResponseModel> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChatAccessTokenResponseModel> call, Throwable t) {
                Log.e("abcd",t.toString());

                mutableLiveData.postValue(null);
            }
        });
    }




    public LiveData<ChatAccessTokenResponseModel> ChatAccessTokenResponseLiveData() {
        return mutableLiveData;
    }


}
