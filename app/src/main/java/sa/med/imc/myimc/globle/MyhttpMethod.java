package sa.med.imc.myimc.globle;

import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Network.Constants.IS_BEFORE_LOGIN;
import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.util.OnCancelResponse;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.MedicineDetail.model.MedicineResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DoctorFullDeatilsModel;
import sa.med.imc.myimc.Physicians.model.DoctorModel;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.AllDoctoListener;
import sa.med.imc.myimc.Physicians.presenter.FullDoctorDetailListner;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CadiologyReportOldModel;
import sa.med.imc.myimc.Records.model.CardiologyModel;
import sa.med.imc.myimc.Records.model.DischargeModel;
import sa.med.imc.myimc.Records.model.DischargeOldModel;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LaboratoryHistoryModel;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.OperativeModel;
import sa.med.imc.myimc.Records.model.OperativeReportOldModel;
import sa.med.imc.myimc.Records.model.RadiologyOldModel;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickReportOldModel;
import sa.med.imc.myimc.SignUp.model.SelfRegistrationOtpResponse;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.activity.PaymentWebViewActivity;
import sa.med.imc.myimc.globle.interfaces.AppointmentListner;
import sa.med.imc.myimc.globle.interfaces.CardiologyListner;
import sa.med.imc.myimc.globle.interfaces.CardiologyReportOldListner;
import sa.med.imc.myimc.globle.interfaces.ContactUsListner;
import sa.med.imc.myimc.globle.interfaces.DischageOdListner;
import sa.med.imc.myimc.globle.interfaces.DischargeReportListner;
import sa.med.imc.myimc.globle.interfaces.LabreportListner;
import sa.med.imc.myimc.globle.interfaces.LabreportOldDataListner;
import sa.med.imc.myimc.globle.interfaces.MedicineListner;
import sa.med.imc.myimc.globle.interfaces.NewVitalListner;
import sa.med.imc.myimc.globle.interfaces.OperativeOldListner;
import sa.med.imc.myimc.globle.interfaces.OperativeReportListner;
import sa.med.imc.myimc.globle.interfaces.PatientArrivalListner;
import sa.med.imc.myimc.globle.interfaces.PatientIdListner;
import sa.med.imc.myimc.globle.interfaces.PdfListner;
import sa.med.imc.myimc.globle.interfaces.PhysicianListner;
import sa.med.imc.myimc.globle.interfaces.RadiologyListner;
import sa.med.imc.myimc.globle.interfaces.RadiologyOldListner;
import sa.med.imc.myimc.globle.interfaces.SelfRegistrationListner;
import sa.med.imc.myimc.globle.interfaces.SelfRegistrationOtp;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.interfaces.SickLeaveListner;
import sa.med.imc.myimc.globle.interfaces.SickLeveOldReportListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;

public class MyhttpMethod {

    private static final long REQUEST_DE = 5000;
    Context context;
    CustomDailogBuilder progressDialog;
    ValidateResponse user;
    int hosp1;
    String lang=Constants.ENGLISH;
    String phone,cancelText;
    public MyhttpMethod(Context context) {
        this.context = context;
        progressDialog = new CustomDailogBuilder(context);
        user = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
        hosp1 = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        phone = preferences.getString("ph", "");
        cancelText = context.getString(R.string.cancel_appointment_error, phone );

    }


    public void generatePaymentUrl(Activity activity, RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration) {
        progressDialog.show();

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getGenerate_payment_url();

        final String mRequestBody = new Gson().toJson(requestBodyForPaymentUrlGeneration);
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Intent intent = new Intent(context, PaymentWebViewActivity.class);
                        intent.putExtra("requestBodyForPaymentUrlGeneration", mRequestBody);
                        intent.putExtra("ref", jsonObject.getJSONObject("data").getString("ref"));
                        intent.putExtra("url", jsonObject.getJSONObject("data").getString("url"));
                        context.startActivity(intent);
                    } else {
                        String message = jsonObject.getJSONObject("data").getJSONObject("error").getString("message");
                        Log.e("abcd", message);
                        if (message.contains("Duplicate")) {
                            messageDailog(activity, "This transaction is duplicate for this booking. Please contact administrator");
                        } else {
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            messageDailog(activity, activity.getResources().getString(R.string.please_try_again));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.getMessage());
                    messageDailog(activity, activity.getResources().getString(R.string.please_try_again));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) context, error);

                progressDialog.dismiss();
                Log.e("abcd", error.toString());
//                messageDailog(activity,"");
                messageDailog(activity, activity.getResources().getString(R.string.please_try_again));

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
                headers.put(Constants.PLATFORM, "Android");
                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getServicePrice(String slotBookingId, boolean is_final_price, ServicePriceListner servicePriceListner) {
        progressDialog.show();
        String patientInsuranceId = SharedPreferencesUtils.getInstance(context).getValue(Constants.patientInsuranceId, "");
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = null;
        try {
            Url = new UrlWithURLDecoder().getService_price(slotBookingId, patientInsuranceId, is_final_price);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(context, "Someting went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("abcd", response);
                        ServicePrice servicePrice = new Gson().fromJson(response, new TypeToken<ServicePrice>() {
                        }.getType());
                        if (servicePrice.getStatus().equals("Success")) {
                            servicePriceListner.onSuccess(servicePrice);
                        } else {
                            Toast.makeText(context, "Someting went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SessionExpired((Activity) context, error);

                        Log.e("abcd", error.toString());
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    public void verifyPayment(Activity activity, String refId,
                              RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration, int hosp) {
        String mrn = user.getMrn();
        String patientId = user.getPatientId();

        progressDialog.show();

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("refId", refId);
            requestObj.put("mrn", mrn);
            requestObj.put("patientId", patientId);
            requestObj.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            showAlertDailog(activity, e.toString());
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getVerify_payment();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String transactionId = jsonObject.getJSONObject("data").getJSONObject("order").getJSONObject("transaction").getString("ref");
                        settleBill(activity, transactionId, requestBodyForPaymentUrlGeneration);
                    } else {
                        String message = context.getResources().getString(R.string.payment_error,phone);
                        Log.e("abcd", message);
                        progressDialog.dismiss();
                        showAlertDailog(activity, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.getMessage());
                    progressDialog.dismiss();
                    showAlertDailog(activity, context.getResources().getString(R.string.payment_error,phone));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) context, error);

                progressDialog.dismiss();
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.e("abcd", responseBody);

                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                progressDialog.dismiss();
                showAlertDailog(activity, context.getResources().getString(R.string.payment_error,phone));

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
                headers.put(Constants.PLATFORM, "Android");
                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestQueue.add(stringRequest);
            }
        }, REQUEST_DE);

    }

    public void settleBill(Activity activity, String transactionId,
                           RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration) {


        progressDialog.show();
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("transactionId", transactionId);
            requestObj.put("amountPaid", requestBodyForPaymentUrlGeneration.getAmount());
            requestObj.put("paymentDate", requestBodyForPaymentUrlGeneration.getAppointmentDate());
            requestObj.put("slotBookingId", requestBodyForPaymentUrlGeneration.getSlotBookingId());
            requestObj.put("hosp", hosp1);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            showAlertDailog(activity, e.toString());
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getSettle_bill();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("Success")) {
                        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.success_dailog, viewGroup, false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);


                        AppCompatButton buttonOk = dialogView.findViewById(R.id.buttonOk);
                        builder.setView(dialogView);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        buttonOk.setOnClickListener(v -> {
                            Intent in = new Intent(context, MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(in);
                            activity.finish();
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    } else {
                        progressDialog.dismiss();
                        showAlertDailog(activity, context.getResources().getString(R.string.payment_error,phone));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    progressDialog.dismiss();
                    showAlertDailog(activity, context.getResources().getString(R.string.payment_error,phone));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
                progressDialog.dismiss();
                showAlertDailog(activity, context.getResources().getString(R.string.payment_error,phone));

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestQueue.add(stringRequest);
            }
        }, REQUEST_DE);


    }

    public void getAppoinmentList(Activity activity,String Url, String hospitalCode, String fromDate, String toDate, AppointmentListner appointmentListner) {
        String mrn = user.getMrn();
        String patientId = user.getPatientId();

        progressDialog.show();
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("hospitalCode", hospitalCode);
            requestObj.put("patientId", patientId);
            requestObj.put("mrNumber", mrn);
            requestObj.put("fromDate", fromDate);
            requestObj.put("toDate", toDate);
            requestObj.put("bookingStatus", "");
            requestObj.put("physicianCode", "");
            requestObj.put("clinicCode", "");
            requestObj.put("page", "0");
            requestObj.put("itemCount", "10000");

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            showAlertDailog(activity, e.toString());
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

//        String Url = new UrlWithURLDecoder().getBookings_all();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("abcd", response);
                progressDialog.dismiss();
                JSONArray jsonArray = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getString("status").equals("true")) {
                        String message = jsonObject.getString("message");
//                        messageDailog(activity,message);
                        return;
                    }
                    jsonArray = jsonObject.getJSONArray("bookings");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
//                    messageDailog(activity,e.getMessage());
                }

                List<BookingAppoinment> bookingAppoinmentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BookingAppoinment>>() {
                }.getType());

                appointmentListner.onSuccess(bookingAppoinmentList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                messageDailog(activity,error.getMessage());

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
                headers.put(Constants.PLATFORM, "Android");
                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    void showAlertDailog(Activity activity, String content) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            activity.finish();
        });

        alertDialog.show();
    }


    void messageDailog(Activity activity, String content) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
    void messageDailog(Activity activity, String content, View.OnClickListener onClickListener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            onClickListener.onClick(dialogView);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public void cancelAppointment(Activity activity, BookingAppoinment bookingAppoinment, OnCancelResponse onCancelResponse) {
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");



        if (bookingAppoinment.isPaid()) {



            messageDailog(activity, cancelText);
            return;
        }
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String Url = null;
        if (bookingAppoinment.getSlotBookingId().contains("|")) {
            try {
                Url = new UrlWithURLDecoder().getCancel_appointment(bookingAppoinment.getSlotBookingId());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                messageDailog(activity, cancelText);
                return;
            }
        } else {
            Url = new UrlWithURLDecoder().getCancel_old_appointment(bookingAppoinment.getSlotBookingId());
        }
        StringRequest sr = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("abcd", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                onCancelResponse.onSuccess();
                                Toast.makeText(context, cancelText, Toast.LENGTH_SHORT).show();
                            } else {
                                messageDailog(activity, jsonObject.getString("message"), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                                        LocalBroadcastManager.getInstance(activity).sendBroadcast(refresh);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            messageDailog(activity, cancelText, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                                    LocalBroadcastManager.getInstance(activity).sendBroadcast(refresh);
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        new SessionExpired((Activity) context, error);
//                        Log.e("abcd", error.toString());
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        messageDailog(activity, cancelText, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                                LocalBroadcastManager.getInstance(activity).sendBroadcast(refresh);
                            }
                        });


                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public void loadLabReport(String page, String bookingStatus, LabreportListner labreportListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientInsuranceId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String mrn = user.getMrn();
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getLaboratory_results(patientInsuranceId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                LabReportResponse labReportResponse = new Gson().fromJson(response, new TypeToken<LabReportResponse>() {
                }.getType());

                try {
                    if (labReportResponse.getOrder_list().isEmpty()){
                        labreportListner.onFailed();
                        return;
                    }
                } catch (Exception e) {
                    labreportListner.onFailed();
                    return;
                }


                labreportListner.onSuccess(labReportResponse);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                labreportListner.onFailed();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void loadLabReportOld(String page, LabreportOldDataListner labreportOldDataListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String mrn = user.getMrn();
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getLaboratory_results_old_data(mrn, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();

                String total_items = "";
                List<LaboratoryHistoryModel> laboratoryHistoryModelList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    total_items = jsonObject.getString("total_items");
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.e("abcd data", data.toString());

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObj = data.getJSONObject(i);

                        Log.e("abcd dataObj", dataObj.toString());

                        for (Iterator<String> iter = dataObj.keys(); iter.hasNext(); ) {
                            String key = iter.next();

                            try {
                                Object value = dataObj.get(key);
                                JSONArray dataArray = (JSONArray) value;

                                Log.e("abcd dataArray", dataObj.toString());
                                List<LaboratoryHistoryModel.Data> dataList = new Gson().fromJson(dataArray.toString(), new TypeToken<List<LaboratoryHistoryModel.Data>>() {
                                }.getType());

                                LaboratoryHistoryModel laboratoryHistoryModel = new LaboratoryHistoryModel();
                                laboratoryHistoryModel.setName(key);
                                laboratoryHistoryModel.setDataList(dataList);
                                laboratoryHistoryModelList.add(laboratoryHistoryModel);
                            } catch (final JSONException e) {
                                // Something went wrong!
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    labreportOldDataListner.onFailed();
                }

                Log.e("abcd", laboratoryHistoryModelList.size() + "");
                Log.e("abcd", new Gson().toJson(laboratoryHistoryModelList));
                labreportOldDataListner.onSuccess(laboratoryHistoryModelList, total_items);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                labreportOldDataListner.onFailed();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void radiologyReport(String page, RadiologyListner radiologyListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientInsuranceId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getRadiology_results(patientInsuranceId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray order_list = jsonObject.getJSONArray("order_list");
                        List<MedicalReport> medicalReportList = new Gson().fromJson(order_list.toString(), new TypeToken<List<MedicalReport>>() {
                        }.getType());
                        radiologyListner.onSuccess(medicalReportList);
                    } else {
                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    radiologyListner.onFaild();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                radiologyListner.onFaild();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void radiologyOldReport(String page, RadiologyOldListner radiologyOldListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String mrn = user.getMrn();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getRadiology_results_old(mrn, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                String total_items = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    total_items = jsonObject.getString("total_items");
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        JSONArray data_list = jsonObject.getJSONArray("data");
                        List<RadiologyOldModel> radiologyOldModelList = new Gson().fromJson(data_list.toString(), new TypeToken<List<RadiologyOldModel>>() {
                        }.getType());
                        radiologyOldListner.onSuccess(radiologyOldModelList, total_items);
                    } else {
                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    radiologyOldListner.onFaild();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                radiologyOldListner.onFaild();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void sickLeaveReport(String page, SickLeaveListner sickLeaveListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientInsuranceId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getSick_leave_reports(patientInsuranceId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray sick_list = jsonObject.getJSONArray("document_list");
                        List<SickLeave> sickLeaveList = new Gson().fromJson(sick_list.toString(), new TypeToken<List<SickLeave>>() {
                        }.getType());
                        sickLeaveListner.onSuccess(sickLeaveList);
                    } else {
                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    sickLeaveListner.onFaild();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
                sickLeaveListner.onFaild();
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void sickLeaveReportOld(String page, SickLeveOldReportListner sickLeveOldReportListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientInsuranceId = user.getMrn();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getSick_leave_reports_old(patientInsuranceId, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                SickReportOldModel sickReportOldModel = new Gson().fromJson(response, new TypeToken<SickReportOldModel>() {
                }.getType());
                sickLeveOldReportListner.onSuccess(sickReportOldModel);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
                sickLeveOldReportListner.onFaild();
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void loadPdfInputStream(String url, PdfListner pdfListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        String data = jsonObject.getString("pdf_base64");
                        pdfListner.onSuccess(data);
                    } else {
//                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                        pdfListner.onFail();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    pdfListner.onFail();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                pdfListner.onFail();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void loadSmartPdf(String url, String orderNumber, String mrNumber, PdfListner pdfListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mrNumber", mrNumber);
            jsonObject.put("orderNumber", orderNumber);

            String lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.ENGLISH, Constants.ENGLISH);
            if (lang==Constants.ARABIC) {
                jsonObject.put("language", Constants.ARABIC);
            } else {
                jsonObject.put("language", Constants.ENGLISH);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String mRequestBody = jsonObject.toString();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        Log.e("abcd", url);
        Log.e("abcd", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    pdfListner.onSuccess(jsonObject.getJSONObject("data").getString("file"));

//                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
//                        String data = jsonObject.getJSONObject("data").getString("file");
//                        pdfListner.onSuccess(data);
//                    } else {
////                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
//                        pdfListner.onFail();
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    pdfListner.onFail();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                pdfListner.onFail();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void dischargeReport(String page, DischargeReportListner dischargeReportListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getDischarge_summary_reports(patientId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray sick_list = jsonObject.getJSONArray("document_list");
                        List<DischargeModel> dischargeModelList = new Gson().fromJson(sick_list.toString(), new TypeToken<List<DischargeModel>>() {
                        }.getType());
                        dischargeReportListner.onSuccess(dischargeModelList);
                    } else {
                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    dischargeReportListner.onFailed();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                dischargeReportListner.onFailed();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void dischargeOldReport(String page, DischageOdListner dischageOdListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getMrn();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getDischarge_summary_reports_old(patientId, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                DischargeOldModel dischargeOldModel = new Gson().fromJson(response, new TypeToken<DischargeOldModel>() {
                }.getType());

                dischageOdListner.onSuccess(dischargeOldModel);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                dischageOdListner.onFailed();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void operativeReport(String page, OperativeReportListner operativeReportListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getOperative_reports(patientId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray sick_list = jsonObject.getJSONArray("document_list");
                        List<OperativeModel> operativeModelList = new Gson().fromJson(sick_list.toString(), new TypeToken<List<OperativeModel>>() {
                        }.getType());
                        operativeReportListner.onSuccess(operativeModelList);
                    } else {
                        Toast.makeText(context, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    operativeReportListner.onFailed();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                operativeReportListner.onFailed();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void operativeOldReport(String page, OperativeOldListner operativeOldListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getMrn();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getOperative_reports_old(patientId, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                OperativeReportOldModel operativeReportOldModel = new Gson().fromJson(response, new TypeToken<OperativeReportOldModel>() {
                }.getType());
                operativeOldListner.onSuccess(operativeReportOldModel);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                operativeOldListner.onFaild();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void cardiologyReport(String page, CardiologyListner cardiologyListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");

        String Url = new UrlWithURLDecoder().getCardiology_results(patientId, page, hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("Success")) {
                        JSONArray sick_list = jsonObject.getJSONArray("order_list");
                        List<CardiologyModel> cardiologyModelList = new Gson().fromJson(sick_list.toString(), new TypeToken<List<CardiologyModel>>() {
                        }.getType());
                        cardiologyListner.onSuccess(cardiologyModelList);
                    } else {
                        cardiologyListner.onFailed();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("abcd", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    cardiologyListner.onFailed();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                cardiologyListner.onFailed();


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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void cardiologyReportOld(String page, CardiologyReportOldListner cardiologyReportOldListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getMrn();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1);

        String Url = new UrlWithURLDecoder().getCardiology_results_old(patientId, page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                CadiologyReportOldModel cadiologyReportOldModel = new Gson().fromJson(response, new TypeToken<CadiologyReportOldModel>() {
                }.getType());
                cardiologyReportOldListner.onSuccess(cadiologyReportOldModel);


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                cardiologyReportOldListner.onFailed();

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


//
//    public void loadProfile(ProfileListner profileListner){
//        progressDialog.show();
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String user_id=SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN, "");
//        int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);
//        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
//
//        String Url = new UrlWithURLDecoder().getGet_profile_detail(user_id,hosp);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("abcd", response);
//                progressDialog.dismiss();
//                ProfileModel profileModel = new Gson().fromJson(response, new TypeToken<ProfileModel>() {}.getType());
//                if (profileModel.isStatus()){
//                    profileListner.onSuccess(profileModel);
//                }else {
//                    Toast.makeText(context, profileModel.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Log.e("abcd", error.toString());
////                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                //headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                TIME_OUT_DURATION,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }


    public void getPhysician(String code, PhysicianListner physicianListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getPhysician_details(code);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                DoctorModel doctorModel = new Gson().fromJson(response, new TypeToken<DoctorModel>() {
                }.getType());
                if (doctorModel.getStatus().equalsIgnoreCase("Success")) {
                    physicianListner.onSuccess(doctorModel);
                } else {
                    physicianListner.onFail();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getFullPhysician(String hospitalCode, String doctorCode,boolean isProgress, FullDoctorDetailListner doctorDetailListner) {
        if (isProgress){
            progressDialog.show();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getFull_doctor_details(hospitalCode, doctorCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("abcd", response);
                progressDialog.dismiss();
                DoctorFullDeatilsModel doctorFullDeatilsModel = new Gson().fromJson(response, new TypeToken<DoctorFullDeatilsModel>() {
                }.getType());
                if (doctorFullDeatilsModel.getStatus().trim().equalsIgnoreCase("true")) {
                    doctorDetailListner.onSuccess(doctorFullDeatilsModel);
                } else {
                    doctorDetailListner.onFailed();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                new SessionExpired((Activity) context, error);
                doctorDetailListner.onFailed();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getMedicineList(String page, MedicineListner medicineListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String mrn = user.getMrn();

        String Url = new UrlWithURLDecoder().getMedication_list(patientId, mrn, page);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("abcd", response);

                progressDialog.dismiss();
                MedicineResponse medicineResponse = new Gson().fromJson(response, new TypeToken<MedicineResponse>() {
                }.getType());

                try {
                    if (medicineResponse.getMedication_list().isEmpty()){
                        medicineListner.onFail();
                        return;
                    }
                } catch (Exception e) {
                    medicineListner.onFail();
                    return;
                }


                medicineListner.onSuccess(medicineResponse);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                medicineListner.onFail();
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");
                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void selfRegistration(String details, SelfRegistrationListner selfRegistrationListner) {

        progressDialog.show();

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("task_type", "Self Registration");
            requestObj.put("details", details);
            requestObj.put("hosp", hosp1);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getRegistration();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                selfRegistrationListner.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void selfRegistrationOtp(String mobileNumber, SelfRegistrationOtp selfRegistrationOtp) {

        progressDialog.show();

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("mobileNumber", mobileNumber);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getRegistration_otp();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                SelfRegistrationOtpResponse selfRegistrationOtpResponse= new Gson().fromJson(response, new TypeToken<SelfRegistrationOtpResponse>() {
                }.getType());
                selfRegistrationOtp.onSucces(selfRegistrationOtpResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    public void selfRegistrationOtpVarification(String mobileNumber,String otp, SelfRegistrationListner selfRegistrationListner) {

        progressDialog.show();

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("mobileNumber", mobileNumber);
            requestObj.put("otp", otp);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getRegistration_otp_verification();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);

                selfRegistrationListner.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void patientArrival(String bookingId, PatientArrivalListner patientArrivalListner) {

        progressDialog.show();
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("bookingId", bookingId);
            requestObj.put("hosp", hosp1);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getArrive();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("--patSuccess", response);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("--patSuccess", jsonObject.getString("status"));
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        patientArrivalListner.onSuccess(""+jsonObject.getString("messageDetails"));
                    } else {
                        patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);
                patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));

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
                headers.put("Authorization", "Bearer " + token);  headers.put(Constants.PLATFORM, "Android");
                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void patientChechIn(String bookingId, String apptDateString,String latitude,
                               String longitude, PatientArrivalListner patientArrivalListner) {

        progressDialog.show();
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("date", apptDateString);
            requestObj.put("lat", latitude);
            requestObj.put("lng", longitude);
            requestObj.put("hosp", SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0));

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getcheckCurrentLocation();

        Log.e("patientChechIn", requestObj.toString());
        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(response);
                    Log.d("--onLoc", "onResponse: "+jsonObject.getString("status"));
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        patientArrival(bookingId, patientArrivalListner);
                    } else {
                        patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);
                patientArrivalListner.onFaild(context.getString(R.string.checkin_faild));

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    public void callContactUs(JSONObject details, ContactUsListner contactUsListner) {

        boolean isFromLogin=SharedPreferencesUtils.getInstance(context).getValue(IS_BEFORE_LOGIN,false);

        progressDialog.show();
        String patientId="22";
        if (isFromLogin){
            patientId="22";
        }else {
            try {
                patientId = user.getPatientId();

            } catch (Exception e) {
                patientId="22";
            }

        }

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("task_type", "Contact Us");
            requestObj.put("patient_id", patientId);
            requestObj.put("details", details.toString());
            requestObj.put("hosp", hosp1);

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getContactus();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                contactUsListner.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                Log.e("Authorization","Bearer " + token);
                Log.e("ACCEPT_LANGUAGE",SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                Log.e("HOSPITAL_CODE_FOR_HEADER",SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                Log.e("MRN_KEY",SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                Log.e("headers",headers.toString());

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    public void callRequestReport(JSONObject requestObj, ContactUsListner contactUsListner) {

        progressDialog.show();



//        String patientId = user.getPatientId();

//        JSONObject requestObj = new JSONObject();
//        try {
//            requestObj.put("task_type", "Request Report");
//            requestObj.put("patient_id", patientId);
//            requestObj.put("details", details);
//            requestObj.put("hosp", hosp1);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            progressDialog.dismiss();
//        }

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        String Url = new UrlWithURLDecoder().getReport_request();

        final String mRequestBody = requestObj.toString();
        Log.e("abcd", mRequestBody);
        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        if (lang.equalsIgnoreCase(Constants.ENGLISH)){
                            contactUsListner.onSuccess(jsonObject.getString("message"));
                        }else {
                            contactUsListner.onSuccess(context.getString(R.string.report_successfully_requested_ar));

                        }
                    }else {
                        if (lang.equalsIgnoreCase(Constants.ENGLISH)){
                            contactUsListner.onFaild(jsonObject.getString("message"));
                        }else {
                            contactUsListner.onFaild(context.getString(R.string.report_already_requested_ar));

                        }                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error request",e.getMessage());
                    contactUsListner.onFaild("Something went went wrong. please try again");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("getMessage", error.networkResponse.data.toString());
                new SessionExpired((Activity) context, error);

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
//                try {
//                    headers.put(Constants.MRN_KEY, requestObj.getString("mrNumber"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void getPatientId(String mrn, PatientIdListner patientIdListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        int hosp = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);
        String Url = new UrlWithURLDecoder().getGuardian_getDetails(mrn, hosp);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        patientIdListner.onSuccess(jsonObject.getJSONObject("userDetails").getString("patient_id"));
                    } else {
                        patientIdListner.onFailed(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    patientIdListner.onFailed(e.getMessage());
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                patientIdListner.onFailed(error.getMessage());

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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getNewVitalList(String page, NewVitalListner newVitalListner) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String patientId = user.getPatientId();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String mrn = user.getMrn();

        String Url = new UrlWithURLDecoder().getNew_vital(patientId, page);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);

                progressDialog.dismiss();
                NewVitalModel newVitalModel = new Gson().fromJson(response, new TypeToken<NewVitalModel>() {
                }.getType());
                newVitalListner.onSuccess(newVitalModel);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                newVitalListner.onFailed();
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
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void callLoadDoctorByHospitalCode(String hospitalCode, AllDoctoListener allDoctoListener) {
        progressDialog.show();
        String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String Url = new UrlWithURLDecoder().getGet_all_doctor(hospitalCode);
//        final String mRequestBody =object.toString();
        Log.e("abcd", Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                progressDialog.dismiss();
                AllDoctorListModel response1 = new Gson().fromJson(response, new TypeToken<AllDoctorListModel>() {
                }.getType());
                allDoctoListener.onSuccess(response1);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new SessionExpired((Activity) context, error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                allDoctoListener.onFaild(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                //headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

}
