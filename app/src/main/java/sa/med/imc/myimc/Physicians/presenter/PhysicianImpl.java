package sa.med.imc.myimc.Physicians.presenter;

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
import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianViews;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.SessionExpired;

/**
 * Physician API implementation class.
 * Get Physician data
 * Get next available Time slot of a doctor
 */

public class PhysicianImpl implements PhysicianPresenter {

    private Activity activity;
    private PhysicianViews views;

    public PhysicianImpl(PhysicianViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGetAllDoctors(String token, String rat, String search_txt, String lang, String item_count, String page, int hosp) {
        if (page.equalsIgnoreCase("0")) {
            if (search_txt.length() == 0 && (lang.length() > 0 || rat.length() > 0 || lang.length() == 0 || rat.length() == 0))
                views.showLoading();
        } else
            views.showLoading();
        String clinic_code;
        if (rat.length() != 0) {
            clinic_code = rat;
        } else {
            clinic_code = "0";
        }
        WebService webService = ServiceGenerator.createService(WebService.class);
        JSONObject object = new JSONObject();
        try {
            object.put("mrnumber", "");
            object.put("clinic_code", clinic_code);
            object.put("search_txt", search_txt);
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


        Call<PhysicianResponse> xxx = webService.getAllDoctorsWeb(clinic_code);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
//        final String mRequestBody =object.toString();

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
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PhysicianResponse> call, Throwable t) {
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
    public void callGetSearchDoctors(String token, String mrn, String clinic_id, String search_txt, String lang,
                                     String rating, String item_count, String page, String type, int hosp) {
        if (page.equalsIgnoreCase("0")) {
            if (search_txt.length() == 0 && (clinic_id.length() > 0 || rating.length() > 0 || clinic_id.length() == 0 || rating.length() == 0))
                views.showLoading();
        } else
            views.showLoading();

        if (clinic_id.length() == 0) {
            clinic_id = "0";
        }
        if (rating.length() != 0) {
            clinic_id = rating;
        }

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {


            object.put("mrnumber", mrn);
            object.put("clinic_code", clinic_id);
            object.put("search_txt", search_txt.toLowerCase());
            object.put("rating", "");
            object.put("serviceid", "");
            object.put("search_word", "-");
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
                PhysicianResponse response1 = new Gson().fromJson(response, new TypeToken<PhysicianResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        views.onGetPhysicianList(response1);
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


//        xxx.enqueue(new Callback<PhysicianResponse>() {
//
//            @Override
//            public void onResponse(Call<PhysicianResponse> call, Response<PhysicianResponse> response) {
//                views.hideLoading();
//                Log.e("abcd", String.valueOf(response.code()));
//                Log.e("abcd", "response");
//                if (response.isSuccessful()) {
//                    PhysicianResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("Success")) {
//                            views.onGetPhysicianList(response1);
//                        } else
//                            views.onFail(response1.getMessage());
//                    } else {
//                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
//                    Log.e("abcd", String.valueOf(response.code()));
//                    Log.e("abcd", "error");
//
//                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PhysicianResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                Log.e("abcd","error");
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
//
//
//        });
    }

    @Override
    public void callGetDoctorsInfo(String token, String doctor_id, String mrn, String clinic_id, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            if (token.length() != 0)
                object.put("mrnumber", mrn);
            else
                object.put("mrnumber", "");

            object.put("clinicid", clinic_id);
            object.put("phyid", doctor_id);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PhysicianDetailResponse> xxx = null;

//        if (token.length() != 0)
//            xxx = webService.getDoctorInfoWeb(body);
////            xxx = webService.getDoctorInfo(body);
//        else
//            xxx = webService.getDoctorInfoWeb(body);



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                PhysicianDetailResponse response1 = new Gson().fromJson(response, new TypeToken<PhysicianDetailResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetPhysicianProfile(response1);
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


//        xxx.enqueue(new Callback<PhysicianDetailResponse>() {
//            @Override
//            public void onResponse(Call<PhysicianDetailResponse> call, Response<PhysicianDetailResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    PhysicianDetailResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetPhysicianProfile(response1);
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
//            public void onFailure(Call<PhysicianDetailResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    // message = "Unknown";
//                    //  views.onFail(message);
//
//                }
//            }
//        });
    }

    @Override
    public void callGetCMSDoctorProfileData(String doctor_id) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
        Call<PhysicianCompleteDetailCMSResponse> xxx = webService.getDoctorFullDetailCMS(doctor_id);



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                PhysicianCompleteDetailCMSResponse response1 = new Gson().fromJson(response, new TypeToken<PhysicianCompleteDetailCMSResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus()) {
                        views.onGetCMSPhysician(response1);
                    }
//                        else
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

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


//        xxx.enqueue(new Callback<PhysicianCompleteDetailCMSResponse>() {
//            @Override
//            public void onResponse(Call<PhysicianCompleteDetailCMSResponse> call, Response<PhysicianCompleteDetailCMSResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    PhysicianCompleteDetailCMSResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus()) {
//                            views.onGetCMSPhysician(response1);
//                        }
////                        else
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
//            public void onFailure(Call<PhysicianCompleteDetailCMSResponse> call, Throwable t) {
//                views.hideLoading();
//                String message = "";
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    message = "timeout";
//                    views.onFail(message);
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    views.onNoInternet();
//                } else {
//                    // message = "Unknown";
//                    //  views.onFail(message);
//                }
//            }
//        });
    }

    @Override
    public void callGetNextAvailableDateTime(String token, String physicianId, String clinicId, String serviceId, String deptCode, int pos, int hosp) {
//        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", physicianId);
            object.put("clinicId", clinicId);
            object.put("serviceId", serviceId);
            object.put("deptCode", deptCode);
            object.put("sessionType", "0");
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<NextTimeResponse> xxx = webService.getNextAvailableTimeSlot(body);



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();
        final String mRequestBody =object.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                NextTimeResponse response1 = new Gson().fromJson(response, new TypeToken<NextTimeResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetNextAvailableTIme(response1, pos);
                    }
//                        else
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


//        xxx.enqueue(new Callback<NextTimeResponse>() {
//            @Override
//            public void onResponse(Call<NextTimeResponse> call, Response<NextTimeResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    NextTimeResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            views.onGetNextAvailableTIme(response1, pos);
//                        }
////                        else
////                            views.onFail(response1.getMessage());
//                    } else {
////                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                    }
//                } else {
////                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NextTimeResponse> call, Throwable t) {
//                views.hideLoading();
////                String message = "";
////
////                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
////                    message = "timeout";
////                    views.onFail(message);
////                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
////                    views.onNoInternet();
////                } else {
////                    // message = "Unknown";
////                    //  views.onFail(message);
////
////                }
//            }
//        });
    }

    @Override
    public void callGetDoctorServiceList(String token, String providerCode, String specialityCode) {
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<DrServiceResponse> xxx = webService.getDoctorsService(providerCode, specialityCode);


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        String Url = xxx.request().url().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                DrServiceResponse response1 = new Gson().fromJson(response, new TypeToken<DrServiceResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
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
//        xxx.enqueue(new Callback<DrServiceResponse>() {
//            @Override
//            public void onResponse(Call<DrServiceResponse> call, Response<DrServiceResponse> response) {
//                views.hideLoading();
//                if (response.isSuccessful()) {
//                    DrServiceResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
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



}
