package sa.med.imc.myimc.HealthSummary.presenter;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Query;
import sa.med.imc.myimc.HealthSummary.model.AllergyResponse;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.HealthSummary.view.HealthViews;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Heath Allergies API implementation class.
 * Get All Allergies
 * Get All Readings
 */

public class HealthImpl implements HealthPresenter {

    private Activity activity;
    private HealthViews views;

    public HealthImpl(HealthViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGetAllAllergies(String token, String patientId, String mrn) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);

        Call<AllergyResponse> xxx = webService.fetchAllergies(patientId, mrn);

        Log.e("abcd",xxx.request().url().toString());
        xxx.enqueue(new Callback<AllergyResponse>() {
            @Override
            public void onResponse(Call<AllergyResponse> call, Response<AllergyResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    AllergyResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("Success")) {
                            views.onSuccess(response1);
                        } else

                            views.onFail(response1.getStatus());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<AllergyResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
//                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetAllReadings(String token, String mrn,String page) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ReadingResponse> xxx = webService.fetchAllReadings(mrn,page);


        Log.e("abcd",xxx.request().url().toString());
        xxx.enqueue(new Callback<ReadingResponse>() {
            @Override
            public void onResponse(Call<ReadingResponse> call, Response<ReadingResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    ReadingResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.isStatus()) {
                            views.onSuccessReadings(response1);
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
            public void onFailure(Call<ReadingResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
//                    views.onFail(message);
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
