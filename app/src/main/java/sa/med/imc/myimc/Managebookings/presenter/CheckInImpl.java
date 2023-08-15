package sa.med.imc.myimc.Managebookings.presenter;

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
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.view.BookingViews;
import sa.med.imc.myimc.Managebookings.view.CheckInViews;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;

/**
 * CheckIn API implementation class.
 * Pay Only
 * Pay and CheckIn
 */

public class CheckInImpl implements CheckInPresenter {

    private Activity activity;
    CheckInViews checkInViews;


    public CheckInImpl(CheckInViews views, Activity activity) {
        this.checkInViews = views;
        this.activity = activity;
    }


    @Override
    public void callCheckLocation(String token, String lat, String lng, String date, int hosp) {
        checkInViews.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("lat", lat);//"30.704649");//
            object.put("lng", lng);//"76.717873");//
            object.put("date", date);
            object.put("hosp", hosp);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.checkInPayOnly(body);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                checkInViews.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true"))
                            checkInViews.onGetLocation(response1);
                        else
                            checkInViews.onFail(response1.getMessage());

                    } else {
                        checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                checkInViews.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    checkInViews.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    checkInViews.onNoInternet();
                } else {
                    message = t.getMessage();
                    checkInViews.onFail(message);
                }
            }
        });
    }

    @Override
    public void callPayment(String token, String mrNumber, String sessionId, String timeSlot, String date, String bookingId, String teleHealthFlag, String consent,String arrivalUpdate,int hosp) {
        checkInViews.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("sessionId", sessionId);
            object.put("timeSlot", timeSlot);
            object.put("bookingId", bookingId);
            object.put("date", date);
            object.put("teleHealthFlag", teleHealthFlag);
            object.put("consent", consent);
            object.put("arrivalUpdate", arrivalUpdate);
            object.put("hosp", hosp);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PaymentResponse> xxx = webService.checkInPayment(body);

        xxx.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                checkInViews.hideLoading();
                if (response.isSuccessful()) {
                    PaymentResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("200") || response1.getStatus().equalsIgnoreCase("true"))
                            checkInViews.onGetPaymentSuccess(response1);
                        else
                            checkInViews.onFail(response1.getMessage());
                    } else {
                        checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                checkInViews.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    checkInViews.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    checkInViews.onNoInternet();
                } else {
                    message = t.getMessage();
                    checkInViews.onFail(message);
                }
            }
        });
    }

    @Override
    public void callArrival(String token, String mrNumber, String bookingId, int hosp) {
        checkInViews.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("bookingId", bookingId);
            object.put("hosp", hosp);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.confirmArrival(body);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                checkInViews.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true"))
                            checkInViews.onArrived(response1);
                        else
                            checkInViews.onFail(response1.getMessage());
                    } else {
                        checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    checkInViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                checkInViews.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    checkInViews.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    checkInViews.onNoInternet();
                } else {
                    message = t.getMessage();
                    checkInViews.onFail(message);
                }
            }
        });
    }
}
