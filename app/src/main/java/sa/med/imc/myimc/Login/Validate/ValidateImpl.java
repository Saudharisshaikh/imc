package sa.med.imc.myimc.Login.Validate;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;
import static sa.med.imc.myimc.Utils.Common.showAlert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
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
 * Validate API implementation class.
 */

public class ValidateImpl implements ValidatePresenter {

    Activity activity;
    ValidateViews views;

    public ValidateImpl(ValidateViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public void callValidateApi(String otp, String userid, String user_type, String device_id, String conenst, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        if (device_id.isEmpty())
//            device_id = "abcvcvcvvcvcvc12vc";
            device_id = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_DEVICE_ID, "");
        JSONObject object = new JSONObject();
        try {
            object.put("otp", otp);
            object.put("userId", userid);
//            object.put("user_type", user_type);
//            object.put("device_id", device_id);
            object.put("deviceToken", device_id);
            object.put("consent", conenst);
//            object.put("hosp", hosp);
            object.put("platform", "Android");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<ValidateResponse> xxx = webService.loginValidate(body);
        xxx.enqueue(new Callback<ValidateResponse>() {
            @Override
            public void onResponse(Call<ValidateResponse> call, Response<ValidateResponse> response) {
                Log.e("abcd",new Gson().toJson(response.body()));
                views.hideLoading();
                if (response.isSuccessful()) {
                    ValidateResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("Success")) {
                            views.onValidateOtp(response1);
                        } else {
                            String lang = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                            if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
                                views.onFail(response1.getMessage());
                            }else {
                                views.onFail(activity.getString(R.string.otp_not_matched));
                            }
                        }
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ValidateResponse> call, Throwable t) {
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
    public void callResendOtpApi(String id_type, String id_value, String user_type, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        JSONObject object = new JSONObject();
        String message=activity.getResources().getString(R.string.plesae_mrn_number);

        if (id_type.equalsIgnoreCase( Constants.LOGIN_WITH_ID)){
            try {
                object.put("nationalId", id_value);
                object.put("mrn", "");
//            object.put("user_type", user_type);
//            object.put("hosp", hosp);
                message=activity.getResources().getString(R.string.plesae_id_number);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                object.put("nationalId", "");
                object.put("mrn", id_value);
//            object.put("user_type", user_type);
//            object.put("hosp", hosp);
                message=activity.getResources().getString(R.string.plesae_mrn_number);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<LoginResponse> xxx = webService.login(body);
        String finalMessage = message;


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd", Url);
        Log.e("abcd", mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                LoginResponse response1 = new Gson().fromJson(response, new TypeToken<LoginResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus()) {
                        views.onResend(response1);
//                          MCPatient.getInstance().get().refreshData();
//                            MCPatient.updateCurrentPatient();
                    }

                    views.onFail(response1.getMessage());
                } else {
                    views.onFail(finalMessage);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideLoading();
                new SessionExpired((Activity) activity,error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//                views.onFail(error.toString());
                SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
                String phone = preferences.getString("ph", "");
                if (error.networkResponse.statusCode==500){
                    showAlert(activity,activity.getString(R.string.invalid_user_id));
                }else {
                    showAlert(activity,activity.getString(R.string.service_error,phone));
                }
//
//                if (error instanceof ServerError){
//                    ViewGroup viewGroup = activity.findViewById(android.R.id.content);
//                    View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//
//
//                    TextView contentTextView = dialogView.findViewById(R.id.content);
//                    TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
//                    contentTextView.setText(activity.getString(R.string.service_error));
//                    builder.setView(dialogView);
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    buttonOk.setOnClickListener(v -> {
//                        alertDialog.dismiss();
//                    });
//
//                    alertDialog.setCancelable(false);
//                    alertDialog.show();
//                }else if (error instanceof Server){
//
//                }

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
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);






//
//
//
//        views.showLoading();
//        WebService webService = ServiceGenerator.createService(WebService.class);
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("id_type", id_type);
//            object.put("id_value", id_value);
//            object.put("user_type", user_type);
//            object.put("hosp", hosp);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
//
//        Call<LoginResponse> xxx = webService.resendOtp(body);
//        xxx.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    LoginResponse response1 = response.body();
//                    if (response1 != null) {
////                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                        if (response1.getStatus()) {
//                            views.onResend(response1);
//                        }
//                        views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
    public void callUpdateConsentApi(String mrNumber, String constantValue) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<SimpleResponse> xxx = webService.updateConsent(mrNumber, constantValue);
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onConsentSuccess(response1);
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
    public void callGuestResendOtpApi(String phoneNumber) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

//        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("phoneNumber", phoneNumber);

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
                            views.onResend(response1);
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
    public void callRegisterValidateApi(String phoneNumber, String otp, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
        JSONObject object = new JSONObject();
//        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        try {
            object.put("otp", otp);
            object.put("mobileNumber", phoneNumber);
            object.put("hosp", hosp);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<SimpleResponse> xxx = webService.regValidate(body);
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onSuccessGuestValidate(response1);
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
    public void callRegisterResendOtpApi(String phoneNumber, String iqama_id, String type, int hosp) {

        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
//        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("telMobile", phoneNumber);
            object.put("docNumber", iqama_id);
            object.put("docType", type);
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
                            views.onResend(response1);
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
    public void callGuestValidateApi(String phoneNumber, String otp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
//        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("mobileNumber", phoneNumber);
            object.put("otp", otp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<SimpleResponse> xxx = webService.guestValidate(body);
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onSuccessGuestValidate(response1);
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
