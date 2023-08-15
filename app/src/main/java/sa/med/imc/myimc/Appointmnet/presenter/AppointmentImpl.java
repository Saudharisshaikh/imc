package sa.med.imc.myimc.Appointmnet.presenter;

import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked_code;
import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;

import android.app.Activity;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.model.SessionDatesResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsNextResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Appointmnet.view.AppointmentViews;
import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.AppointmentServiceGenerator;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.SessionExpired;

/**
 * Book Appointment API implementation class.
 * Get Session dates and time slots data
 * For Guest and Logged In users
 */

public class AppointmentImpl implements AppointmentPresenter {

    private Activity activity;
    private AppointmentViews views;
    final CompositeDisposable disposable = new CompositeDisposable();

    public AppointmentImpl(AppointmentViews views, Activity activity) {
        this.views = views;
        this.activity = activity;

    }

    @Override
    public void callGetAllDates(String token, String clinic_id, String doctor_id, String service_id, String type, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", doctor_id);
            object.put("clinicId", clinic_id);
            object.put("serviceId", service_id);
            object.put("date", "");
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SessionDatesResponse> xxx;

        if (type.equalsIgnoreCase(Constants.GUEST_TYPE))
            xxx = webService.getGuestSessionDates(body);
        else
            xxx = webService.getSessionDates(body);

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                SessionDatesResponse response1 = new Gson().fromJson(response, new TypeToken<SessionDatesResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetDates(response1);
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

//        xxx.enqueue(new Callback<SessionDatesResponse>() {
//            @Override
//            public void onResponse(Call<SessionDatesResponse> call, Response<SessionDatesResponse> response) {
//                views.hideLoading();
//
//                if (response.isSuccessful()) {
//                    SessionDatesResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetDates(response1);
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
//            public void onFailure(Call<SessionDatesResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
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
    public void callGetAllTimeSlots(String token, String clinic_id, String doctor_id, String service_id, String date, String type, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", doctor_id);
            object.put("clinicId", clinic_id);
            object.put("serviceId", service_id);
            object.put("date", date);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<TimeSlotsResponse> xxx;

        if (type.equalsIgnoreCase(Constants.GUEST_TYPE))
            xxx = webService.getGuestTimeSlots(body);
        else
            xxx = webService.getTimeSlots(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                TimeSlotsResponse response1 = new Gson().fromJson(response, new TypeToken<TimeSlotsResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetTimeSlots(response1);
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

//        xxx.enqueue(new Callback<TimeSlotsResponse>() {
//            @Override
//            public void onResponse(Call<TimeSlotsResponse> call, Response<TimeSlotsResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    TimeSlotsResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetTimeSlots(response1);
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
//            public void onFailure(Call<TimeSlotsResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
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
    public void callBookAppointment(String token, String mrNumber, String sessionId, @NotNull String timeSlot, String date, String telehealth, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("sessionId", sessionId);
            object.put("timeSlot", timeSlot.toLowerCase());
            object.put("date", date);
            object.put("bookingId", "");
            object.put("teleHealthFlag", telehealth);
            object.put("consent", "1");
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.bookAppointment(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true") || response1.getStatus().equalsIgnoreCase("200")) {
                        views.onBookAppointmentSuccess(response1);
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
//
//        xxx.enqueue(new Callback<BookResponse>() {
//            @Override
//            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    BookResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true") || response1.getStatus().equalsIgnoreCase("200")) {
//                            views.onBookAppointmentSuccess(response1);
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
//            public void onFailure(Call<BookResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    // message = "Unknown";  // modified @Pm
//                    message = activity.getResources().getString(R.string.plesae_try_again);
//                    views.onFail(message);
//                }
//            }
//        });

    }


    @Override
    public void callBookAppointmentGuest(String token, String iqumaId, String sessionId, String timeSlot, String date,
                                         String patName, String patNameAr, String familyName, String familyNameAr,
                                         String fatherName, String fatherNameAr, String gender, String mobile,
                                         String phone, String userDob, String lang, String comments) {
        views.showLoading();
        JSONObject object = new JSONObject();

        if (mobile.length() > 0)
            mobile = WebUrl.COUNTRY_CODE + mobile;

        if (phone.length() > 0)
            phone = WebUrl.COUNTRY_CODE + phone;

        try {
            object.put("sessionId", sessionId);
            object.put("bookingAptDate", date);
            object.put("timesolt", timeSlot.toLowerCase());

            object.put("iqumaId", iqumaId);

            object.put("patName", patName);
            object.put("patNameAr", patNameAr);

            object.put("familyName", familyName);
            object.put("familyNameAr", familyNameAr);

            object.put("fatherName", fatherName);
            object.put("fatherNameAr", fatherNameAr);

            object.put("gender", gender);
            object.put("mobile", mobile);
            object.put("phone", phone);
            object.put("userDob", userDob);
            object.put("lang", lang);
            object.put("comments", comments);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.bookAppointmentGuest(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onBookAppointmentSuccess(response1);
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

//        xxx.enqueue(new Callback<BookResponse>() {
//            @Override
//            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    BookResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onBookAppointmentSuccess(response1);
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
//            public void onFailure(Call<BookResponse> call, Throwable t) {
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
    public void callRescheduleAppointment(String token, String mrNumber, String sessionId, String timeSlot, String date, String bookingId, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("sessionId", sessionId);
            object.put("timeSlot", timeSlot);
            object.put("date", date);
            object.put("bookingId", bookingId);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.rescheduleBooking(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onRescheduleAppointment(response1);
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

//
//        xxx.enqueue(new Callback<BookResponse>() {
//            @Override
//            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    BookResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onRescheduleAppointment(response1);
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
//            public void onFailure(Call<BookResponse> call, Throwable t) {
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
    public void callGetAppointmentPrice(String token, String mrNumber, String sessionId, String date, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PriceResponse> xxx = webService.getBookingPrice(sessionId, date, hosp);



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                PriceResponse response1 = new Gson().fromJson(response, new TypeToken<PriceResponse>() {}.getType());
                if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
                    views.onGetPrice(response1);
//                        } else
//                            views.onFail(response1.getMessage());
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

//        xxx.enqueue(new Callback<PriceResponse>() {
//            @Override
//            public void onResponse(Call<PriceResponse> call, Response<PriceResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    Log.e("result before", response.toString());
//                    PriceResponse response1 = response.body();
//                    Log.e("result after", response1.toString());
//                    if (response1 != null) {
////                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                        views.onGetPrice(response1);
////                        } else
////                            views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PriceResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    //message = "Unknown";
//                    message = t.getMessage();
//                    views.onFail(message);
//
//                }
//            }
//        });

    }

    @Override
    public void callGetSearchDoctors(String token, String mrn, String clinic_id, String search_txt, String lang, String rating, String item_count, String page, String type, int hosp) {
        views.showLoading();

        if (clinic_id.length() == 0) {
            clinic_id = "0";
        }
        if (rating.length() != 0) {
            clinic_id = rating;
        }

        WebService webService = AppointmentServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {


            object.put("mrnumber", mrn);
            object.put("clinic_code", clinic_id);
            object.put("search_txt", search_txt.toLowerCase());
            object.put("rating", "");
            object.put("serviceid", "");
            object.put("deptCode", "");
            object.put("lang", lang);
            object.put("item_count", item_count);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<PhysicianResponse> xxx;
        if (type.length() > 0)
            xxx = webService.getAllDoctorsWeb(clinic_id);
        else
            xxx = webService.getAllDoctors(clinic_id);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                PhysicianResponse response1 = new Gson().fromJson(response, new TypeToken<PhysicianResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetPhysicianList(response1);
                    } else
                        views.onFail(response1.getMessage());
                } else {
                    views.hideLoading();
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
//        xxx.enqueue(new Callback<PhysicianResponse>() {
//            @Override
//            public void onResponse(Call<PhysicianResponse> call, Response<PhysicianResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    PhysicianResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetPhysicianList(response1);
//                        } else
//                            views.onFail(response1.getMessage());
//                    } else {
//                        views.hideLoading();
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.hideLoading();
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PhysicianResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
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
        //Subscribe
//SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            physician.getDocCode(), physician.getClinicCode(), physician.getService().getId(), physician.getDeptCode(), "0", selectedDate, selectedDate, i
//       xxx.map(new Function<PhysicianResponse, List<PhysicianResponse.Physician>>() {
//           @Override
//           public List<PhysicianResponse.Physician> apply(PhysicianResponse user) throws Exception {
//               return user.getPhysicians();
//           }
//       }).flatMap( physician-> {
//           JSONObject objectt = new JSONObject();
//           RequestBody bodyy = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), objectt.toString());
//           return webService.fetchAlternatePhysicianss(bodyy);
//        })
//         .doOnSubscribe(disposable -> {
//           views.showLoading();
//       })
//       .subscribeOn(Schedulers.io())
//       .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new SingleObserver<TimeSlotsNextResponse>() {
//           @Override
//           public void onSubscribe(Disposable d) {
//
//           }
//
//           @Override
//           public void onSuccess(TimeSlotsNextResponse response) {
//               views.hideLoading();
//
//                    if (response != null) {
//                        if (response.getStatus().equalsIgnoreCase("true")) {
////                            views.onGetTimeSlots(response);
//                          //  Log.e("TAG",response.getTimeSlots())
//                        } else
//                            views.onFail(response.getMessage());
//                    } else {
//                        views.hideLoading();
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//
//
//           }
//
//           @Override
//           public void onError(Throwable t) {
//               views.hideLoading();
//              String message = "";
//
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    message = "Unknown";
//                    views.onFail(message);
//
//                }
//           }
//       });

        //     final List<String> race = new ArrayList<>(Arrays.asList("Alan", "Bob", "Cobb", "Dan", "Evan", "Finch"));


//        Observable.fromArray(race)
//                .flatMap(str->{
//
//                    return Observable.just("a");
//                })
//                .subscribe(System.out::println);


//       .doOnSubscribe(disposable -> {
//           views.showLoading();
//       })
//       .subscribeOn(Schedulers.io())
//       .observeOn(AndroidSchedulers.mainThread())
//       .subscribe(new SingleObserver<TimeSlotsNextResponse>() {
//           @Override
//           public void onSubscribe(Disposable d) {
//
//           }
//
//           @Override
//           public void onSuccess(TimeSlotsNextResponse timeSlotsNextResponse) {
//               views.hideLoading();
//           }
//
//           @Override
//           public void onError(Throwable e) {
//               views.hideLoading();
//           }
//       });


    }

    @Override
    public void fetchAlternatePhysicians(String token, String physicianId, String clinicId, String serviceId, String deptCode, String sessionType, String startDate, String endDate, int pos, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {

            object.put("physicianId", physicianId);
            object.put("clinicId", clinicId);
            object.put("serviceId", serviceId);
            object.put("deptCode", deptCode);
            object.put("sessionType", sessionType);
            object.put("startDate", startDate);
            object.put("endDate", endDate);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<TimeSlotsNextResponse> xxx = webService.fetchAlternatePhysicians(body);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();
        Log.e("abcd",Url);
        Log.e("abcd",mRequestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                TimeSlotsNextResponse response1 = new Gson().fromJson(response, new TypeToken<TimeSlotsNextResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetTimeSlotsAlternate(response1, pos);
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
//
//        xxx.enqueue(new Callback<TimeSlotsNextResponse>() {
//            @Override
//            public void onResponse(Call<TimeSlotsNextResponse> call, Response<TimeSlotsNextResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    TimeSlotsNextResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetTimeSlotsAlternate(response1, pos);
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
//            public void onFailure(Call<TimeSlotsNextResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) { // ClassNotFoundException
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
    public void fetchServiceList(String token, String providercode, String specialityCode) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<DrServiceResponse> xxx = webService.getDoctorsService(providercode, specialityCode);


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
                DrServiceResponse response1 = new Gson().fromJson(response, new TypeToken<DrServiceResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        views.onGetServiceList(response1);
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
//
//        xxx.enqueue(new Callback<DrServiceResponse>() {
//            @Override
//            public void onResponse(Call<DrServiceResponse> call, Response<DrServiceResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    DrServiceResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("Success")) {
//                            views.onGetServiceList(response1);
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
//            public void onFailure(Call<DrServiceResponse> call, Throwable t) {
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
    public void fetchDrTimeSlot(String token, String datefrom, String dateto, String providercode, String servicecode, String specialityCode) {
        views.showLoading();

        Log.e("abcd",datefrom+" "+dateto);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<DrTimeSlot> xxx = webService.getDoctorsTimeSlot(datefrom, dateto, providercode, servicecode, specialityCode);

        Log.e("abcd",xxx.request().url().toString());


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                DrTimeSlot response1 = new Gson().fromJson(response, new TypeToken<DrTimeSlot>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        views.onGetTimeSlot(response1);
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
//        xxx.enqueue(new Callback<DrTimeSlot>() {
//            @Override
//            public void onResponse(Call<DrTimeSlot> call, Response<DrTimeSlot> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    DrTimeSlot response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("Success")) {
//                            views.onGetTimeSlot(response1);
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
//            public void onFailure(Call<DrTimeSlot> call, Throwable t) {
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
    public void callBookingApt(String token, String PatientID, String SlotId,
                               String providerCode,
                               String serviceCode,
                               String specialityCode,
                               String hospitalCode,
                               String slotDate,
                               String slotFromTime,
                               int hosp,String providerDescription,
                               String arabicProviderDescription,String type) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("patientId", PatientID);
            object.put("slotBookingId", SlotId);
            object.put("providerDescription", providerDescription);
            object.put("arabicProviderDescription", arabicProviderDescription);
            object.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("abcd",object.toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.callBookingAPT(body, providerCode, serviceCode, specialityCode, hospitalCode
        ,slotDate);

        String url=xxx.request().url().toString()+"&slotFromTime="+slotFromTime;

        Log.e("abcd",url);

        final String mRequestBody =object.toString();
        Log.e("abcd",mRequestBody);

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("abcd",response);
                views.hideLoading();
                String lang= SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").trim().equalsIgnoreCase("forbidden")){
                        if (lang.equalsIgnoreCase(Constants.ENGLISH)){
                            views.onFail(jsonObject.getString("englishMessage"));
                        }else {

                            views.onFail(jsonObject.getString("arabicMessage"));
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {

                            try {
                                if (response1.getPaymentStatus().equalsIgnoreCase("FALSE")){
                                    Intent in = new Intent(activity, MainActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                    activity.startActivity(in);
                                    activity.finish();
                                }else {
                                    views.onNewBook(response1);
                                }
                            } catch (Exception exception) {
                                Intent in = new Intent(activity, MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                activity.startActivity(in);
                                activity.finish();
                            }


                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                    return;
                }
                BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        try {
                            if (response1.getPaymentStatus().equalsIgnoreCase("FALSE")){
                                Intent in = new Intent(activity, MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                activity.startActivity(in);
                                activity.finish();
                            }else {
                                views.onNewBook(response1);
                            }
                        } catch (Exception e) {

                            Intent in = new Intent(activity, MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                            activity.startActivity(in);
                            activity.finish();
                        }

                    } else
                        views.onFail(response1.getMessage());
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) activity,error);

                views.hideLoading();
                String message = error.toString();
                views.onFail(message);
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

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(activity).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


//        xxx.enqueue(new Callback<BookResponse>() {
//            @Override
//            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    BookResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("Success")) {
//                            views.onNewBook(response1);
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
//            public void onFailure(Call<BookResponse> call, Throwable t) {
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
    public void fetchAlternatePhysiciansNew(String token, List<PhysicianResponse.Physician> physicians,
                                            String selectedDate) {
        ArrayList<Observable<TimeSlotsNextResponse>> abc = new ArrayList<Observable<TimeSlotsNextResponse>>();
//        ObservableList<TimeSlotsNextResponse>> xXx;
//          Observable.zip(xxx, new Function<>())
        //"0", selectedDate, selectedDate, i)
        for (int i = 0; i < physicians.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("physicianId", physicians.get(i).getDocCode());
                object.put("clinicId", physicians.get(i).getClinicCode());
                object.put("serviceId", physicians.get(i).getService().getId());
                object.put("deptCode", physicians.get(i).getDeptCode());
                object.put("sessionType", "0");
                object.put("startDate", selectedDate);
                object.put("endDate", selectedDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            WebService webService = AppointmentServiceGenerator.createService(WebService.class, "Bearer " + token);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
            abc.add(webService.fetchAlternatePhysicianss(body));
        }

        Observable.zip(Observable.fromIterable(abc), new Function<Object[], TimeSlotsNextResponse[]>() {
                    @Override
                    public TimeSlotsNextResponse[] apply(@NotNull Object[] objects) throws Exception {
                        TimeSlotsNextResponse[] currencies = new TimeSlotsNextResponse[objects.length];
                        for (int i = 0; i < objects.length; i++) {
                            currencies[i] = (TimeSlotsNextResponse) objects[i];
                        }
                        return currencies;
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    views.showLoading();
                })
                .subscribe(new Observer<TimeSlotsNextResponse[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TimeSlotsNextResponse[] timeSlotsNextResponses) {
                        views.hideLoading();
                        // views.onGetTimeSlotsAlternateNew(timeSlotsNextResponses);
                        for (int i = 0; i < timeSlotsNextResponses.length; i++) {
                            Log.e("TAG", timeSlotsNextResponses[i].getMessage().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        views.hideLoading();
                        String message = "";

                        if (t instanceof SocketTimeoutException) { // ClassNotFoundException
                            message = "timeout";
                            views.onFail(message);
                        } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                            views.onNoInternet();
                        } else {
                            message = "Unknown";
                            views.onFail(message);

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
