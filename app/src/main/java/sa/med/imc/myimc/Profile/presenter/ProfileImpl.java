package sa.med.imc.myimc.Profile.presenter;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;
import static sa.med.imc.myimc.globle.UrlWithURLDecoder.update_info;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

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

import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Profile.model.LastBookingResponse;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Profile.view.ProfileViews;
import sa.med.imc.myimc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.globle.SessionExpired;

/**
 * Profile API implementation class.
 * Get Profile data
 * Update Profile data
 */

public class ProfileImpl implements ProfilePresenter {

    private Activity activity;
    private ProfileViews views;

    public ProfileImpl(ProfileViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public void callGetUserProfileApi(String user_id, String token, int hosp) {
        views.showLoading();



        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ProfileResponse> xxx = webService.getUserInfo(user_id, hosp);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();

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
                ProfileResponse response1 = new Gson().fromJson(response, new TypeToken<ProfileResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {

                        try {
                            SharedPreferencesUtils.getInstance(activity).setValue(Constants.IS_EMAIL_VERIFIED, response1.getPatientProfile().getIsEmailVerified());
                            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_PATIENT_EMAIL, response1.getPatientProfile().getEmail());

                        } catch (Exception e) {
                            SharedPreferencesUtils.getInstance(activity).setValue(Constants.IS_EMAIL_VERIFIED, "0");
                        }
                        views.onGetProfile(response1);
                    } else
                        views.onFail(response1.getMessage());
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
                views.onFail("No Profile Found");
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
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


//        xxx.enqueue(new Callback<ProfileResponse>() {
//            @Override
//            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    ProfileResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetProfile(response1);
//                        } else
//                            views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProfileResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = activity.getString(R.string.time_out_messgae);
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    message = "Unknown";
//                    views.onFail(message);
//                }
//            }
//        });
    }

    @Override
    public void callGetMedication(String token, String mrNumber, String episodeId, String itemCount, String page, int hosp) {
        if (!page.equalsIgnoreCase("0"))
            views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("episodeId", episodeId);
            object.put("itemCount", itemCount);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<MedicationRespone> xxx = webService.fetchMedication(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                views.hideLoading();
                MedicationRespone response1  = new Gson().fromJson(response, new TypeToken<MedicationRespone>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetMedication(response1);
                    } else
                        views.onFail(response1.getMessage());
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



//        xxx.enqueue(new Callback<MedicationRespone>() {
//            @Override
//            public void onResponse(Call<MedicationRespone> call, Response<MedicationRespone> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    MedicationRespone response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetMedication(response1);
//                        } else
//                            views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MedicationRespone> call, Throwable t) {
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
    public void callUpdateProfileApi(String mrNumber, String token, String email, String address, String addressAr, int hosp) {
        views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("email", email);
            object.put("address", address);
            object.put("addressAr", addressAr);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.updateUserInfo(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = update_info;
//        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                views.hideLoading();
                SimpleResponse response1 = new Gson().fromJson(response, new TypeToken<SimpleResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onUpdateProfile(response1);
                    } else
                        views.onFail(response1.getMessage());
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
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.e("abcd", responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                }
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                views.onFail(activity.getResources().getString(R.string.plesae_try_again));
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


//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onUpdateProfile(response1);
//                        } else
//                            views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = activity.getString(R.string.time_out_messgae);
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    message = "Unknown";
//                    views.onFail(message);
//                }
//            }
//        });

    }

}
