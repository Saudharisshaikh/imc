package sa.med.imc.myimc.Clinics.presenter;

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
import com.google.gson.reflect.TypeToken;

import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.view.ClinicViews;
import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;

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

/**
 * Clinic API implementation class.
 * Get Clinic data
 */

public class ClinicImpl implements ClinicPresenter {

    private Activity activity;
    private ClinicViews views;

    public ClinicImpl(ClinicViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGetAllClinics(String token, String physician_id, String isActive, String search_txt, String item_count, String page, int hosp) {
        if (page.equalsIgnoreCase("0")) {
            if (search_txt.length() == 0)
                views.showLoading();
        } else
            views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", "");
            object.put("isActive", "Y");
            object.put("search_txt", search_txt);
            object.put("item_count", item_count);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ClinicResponse> xxx = webService.getAllWebClinics(SharedPreferencesUtils.getInstance(activity).getValue(Constants.HOSPITAL_CODE, "IMC"));



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
//        final String mRequestBody =object.toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                views.hideLoading();
                ClinicResponse response1 = new Gson().fromJson(response, new TypeToken<ClinicResponse>() {}.getType());
                if (response1 != null) {

                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        views.onGetClinicsList(response1);
                    } else
                        views.onFail(response1.getStatus());
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideLoading();
                new SessionExpired((Activity) activity,error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                views.onFail(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                    return null;
//                }
//            }

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



        Log.e("abcd",token);
//        xxx.enqueue(new Callback<ClinicResponse>() {
//            @Override
//            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    ClinicResponse response1 = response.body();
//                    if (response1 != null) {
//
//                        if (response1.getStatus().equalsIgnoreCase("Success")) {
//                            views.onGetClinicsList(response1);
//                        } else
//                            views.onFail(response1.getStatus());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ClinicResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    message = "Unknown";
//                    views.onFail(message);
//
//                }
//            }
//        });

    }

    @Override
    public void callGetSearchClinics(String token, String physician_id, String isActive, String search_txt, String item_count, String page, int hosp) {
        if (search_txt.length() == 0 && !page.equalsIgnoreCase("0"))
            views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", "");
            object.put("isActive", "Y");
            object.put("search_txt", search_txt.toLowerCase());
            object.put("item_count", item_count);
            object.put("page", page);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ClinicResponse> xxx = webService.getAllClinics(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                ClinicResponse response1 = new Gson().fromJson(response, new TypeToken<ClinicResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetSearchClinicsList(response1);
                    } else
                        views.onFail(response1.getStatus());
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideLoading();
                new SessionExpired((Activity) activity,error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                views.onFail(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

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


//
//        xxx.enqueue(new Callback<ClinicResponse>() {
//            @Override
//            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    ClinicResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetSearchClinicsList(response1);
//                        } else
//                            views.onFail(response1.getStatus());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ClinicResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    message = "Unknown";
//                    views.onFail(message);
//
//                }
//            }
//        });

    }

}
