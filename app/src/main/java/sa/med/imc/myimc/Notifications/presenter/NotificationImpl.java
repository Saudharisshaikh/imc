package sa.med.imc.myimc.Notifications.presenter;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Notifications.view.NotificationViews;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.globle.SessionExpired;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

/**
 * Get Notifications API implementation class.
 * Delete Notification
 */

public class NotificationImpl implements NotificationPresenter {

    private Activity activity;
    private NotificationViews views;

    public NotificationImpl(NotificationViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public void callGetNotificationsCount(String token, String mrNumber, int hosp) {
        views.showLoading();
        String Url = new UrlWithURLDecoder().getNotificationCount(mrNumber, String.valueOf(hosp));
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                views.hideLoading();
                int count=0;

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    count=jsonObject.getInt("status");
                } catch (Exception e) {
                    count=0;
                }


                views.onGetCount(count);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideLoading();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) activity, error);
                views.onGetCount(0);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void callGetNotifications(String token, String mrNumber, String itemCount, String page, int hosp) {
        views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("itemCount", itemCount);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<NotificationResponse> xxx = webService.getAllNotifications(body);

        Log.e("abcd",object.toString());
        Log.e("abcd",xxx.request().url().toString());

        xxx.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                views.hideLoading();
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    NotificationResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
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
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));

                }
            }
        });
    }

    @Override
    public void callDeleteNotification(String token, String notification_id, int hosp) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.deleteNotification(notification_id, hosp);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onDelete(response1);
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
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }

    @Override
    public void callClearAllNotification(String token, String MRN, int hosp) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.clearAll(MRN, hosp);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onClearAll(response1);
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
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }

    @Override
    public void callReadNotification(String token, String MRN, String not_id, int hosp) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.readNotification(MRN, not_id, hosp);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onRead(response1);
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
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }
}
