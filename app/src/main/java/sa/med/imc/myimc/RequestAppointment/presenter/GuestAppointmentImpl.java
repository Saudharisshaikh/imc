package sa.med.imc.myimc.RequestAppointment.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RequestAppointment.view.GuestAppointmentViews;

/**
 * Guest Login API implementation class.
 * Guest login API implemented.
 */

public class GuestAppointmentImpl implements GuestAppointmentPresenter {

    Activity activity;
    GuestAppointmentViews views;

    public GuestAppointmentImpl(GuestAppointmentViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGuestLoginApi(String phoneNumber, String docNumber, String docType,int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("phoneNumber", phoneNumber);
            object.put("docNumber", docNumber);
            object.put("docType", docType);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<LoginResponse> xxx = webService.guestLogin(body);
        xxx.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    LoginResponse response1 = response.body();
                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
                        if (response1.getStatus()) {
                            views.onSuccessLogin(response1);
                        }

                        views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
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

    @Override
    public void callRegisterLoginApi(String phoneNumber, String iqama_id, String type, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
//        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("phoneNumber", phoneNumber);
            object.put("iqumaId", iqama_id);
            object.put("idType", type);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<LoginResponse> xxx = webService.regLogin(body);
        xxx.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    LoginResponse response1 = response.body();
                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
                        if (response1.getStatus()) {
                            views.onSuccessLogin(response1);
                        }

                        views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
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
