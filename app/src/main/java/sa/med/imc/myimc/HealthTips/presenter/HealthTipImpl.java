package sa.med.imc.myimc.HealthTips.presenter;

import android.app.Activity;

import sa.med.imc.myimc.HealthTips.model.HealthTipsResponse;
import sa.med.imc.myimc.HealthTips.view.HealthTipViews;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Heath Tips API implementation class.
 */

public class HealthTipImpl implements HealthTipPresenter {

    Activity activity;
    HealthTipViews views;

    public HealthTipImpl(HealthTipViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGetAllHealthTips(int page, String name, String category) {
        if (page == 1) {
            if (name.length() == 0) {
                views.showLoading();
            }
        } else {
            views.showLoading();
        }

        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");

        Call<HealthTipsResponse> xxx = webService.getAllHealthTips(String.valueOf(page), name, category);
        xxx.enqueue(new Callback<HealthTipsResponse>() {
            @Override
            public void onResponse(Call<HealthTipsResponse> call, Response<HealthTipsResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    HealthTipsResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus()) {
                            views.onSuccess(response1);
                        } else

                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<HealthTipsResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }


}
