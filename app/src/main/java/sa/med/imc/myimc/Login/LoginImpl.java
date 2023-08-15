package sa.med.imc.myimc.Login;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;
import static sa.med.imc.myimc.Utils.Common.showAlert;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import sa.med.imc.myimc.Base.MCPatient;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.SessionExpired;

/**
 * Login API implementation class.
 * login API implemented.
 */

public class LoginImpl implements LoginPresenter {

    Activity activity;
    LoginViews views;

    public LoginImpl(LoginViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callLoginApi(String id_type, String id_value, String user_type, int hosp) {
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
                String lang= SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE,Constants.ARABIC);
                if (lang.equalsIgnoreCase(Constants.ARABIC)){
                    response1.setMessage(activity.getString(R.string.otp_successfully_sent));
                }
                if (response1 != null) {
                    if (response1.getStatus()) {
                        views.onSuccessLogin(response1);
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



                SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
                String phone = preferences.getString("ph", "");

//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//                views.onFail(error.toString());
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


//        xxx.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    LoginResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus()) {
//                            views.onSuccessLogin(response1);
////                          MCPatient.getInstance().get().refreshData();
////                            MCPatient.updateCurrentPatient();
//                        }
//
//                        views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(finalMessage);
//                    }
//                } else {
//                    views.onFail(finalMessage);
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

//    fun loginWithPin(activity: Activity, onlinePatient: OnlinePatient, name: String,
//                     pin: String) {            onlinePatient.loginWithPinAsync(name, pin,
//            Locale.getDefault().language,
//            object : LoginWithPinDelegate() {
//        override fun onLoginWithPinSuccess(patient: Patient?) {
//            activity.runOnUiThread {
//                MCPatient.setCurrent(patient)
//                MCPatient.getInstance().get().syncWithServer(object : SyncWithServerDelegate() {
//                    override fun onSyncWithServerSuccess() {
//                        activity.runOnUiThread(Runnable {
//                            MCPatient.getInstance().get().refreshData()
//                            MCPatient.updateCurrentPatient()
//                            GeneralFireIntents.fireMainActivityForLabUser(
//                                    activity,
//                                    MCPatient.getInstance().patientRecord.hasPassword)
//                        })
//                    }                                    override fun onSyncWithServerFailure(errorMessage: String) {                                    }
//                })
//            }
//        }                        override fun onLoginWithPinFailure(errors: LoginWithPinErrors?) {
//            activity.runOnUiThread {                            }
//        }                    })
//    }



//    public static void updateCurrentPatient()
//    {
//        int patientId = patient_id;        if (patientId != -1)
//        {            Patient updatedPatient = Patient.get(patientId);            if (updatedPatient != null) {
//        updatedPatient.refreshData();
//        instance.current = updatedPatient;
//        instance.current.syncWithServerAsync();
//        instance.patientRecord = updatedPatient.getPatientRecord();
//        SmartReport.patient = MCPatient.getInstance().get();
//        MedicusInsights.setPatient(MCPatient.getInstance().get());
//        MedicusUtilities.setPatient(MCPatient.getInstance().get());
//    }            instance.patientRecord = null;        }    }
}
