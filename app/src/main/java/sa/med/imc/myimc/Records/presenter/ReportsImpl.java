package sa.med.imc.myimc.Records.presenter;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.model.SmartLabReport;
import sa.med.imc.myimc.Records.model.SmartLabReportResponse;
import sa.med.imc.myimc.Records.view.ReportsViews;
import sa.med.imc.myimc.Utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PaymentWebViewActivity;
import sa.med.imc.myimc.globle.model.ServicePrice;

/**
 * Reports API implementation class.
 * Get Lab reports data
 * Get Sick reports data
 * Get Radiology reports data
 * Get Smart reports data
 * Get Operative reports data
 * Get Cardiology reports data
 */

public class ReportsImpl implements ReportsPresenter {

    ImcDatabase db;
    private Activity activity;
    private ReportsViews views;

    public ReportsImpl(ReportsViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
        db = Room.databaseBuilder(activity, ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

    }

    @Override
    public void callGetAllLabReports(String page,String bookingStatus) {
        views.showLoading();
        ValidateResponse user=SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_USER);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        String patientInsuranceId = SharedPreferencesUtils.getInstance(activity).getValue(Constants.patientInsuranceId, "");
        String token = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_ACCESS_TOKEN, "");
        String mrn = user.getMrn();

        String hospitalCode=SharedPreferencesUtils.getInstance(activity).getValue(Constants.HOSPITAL_CODE,"IMC");
        String Url = new UrlWithURLDecoder().getLaboratory_results(patientInsuranceId,page,hospitalCode);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                views.hideLoading();
                LabReportResponse labReportResponse = new Gson().fromJson(response, new TypeToken<LabReportResponse>() {}.getType());
                views.onGetLabReports(labReportResponse);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideLoading();
                Log.e("abcd", error.toString());
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void callGetAllMedicalReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {
        if (!episodeId.equalsIgnoreCase("0")) {
            if (page.equalsIgnoreCase("0")) {
                if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                    views.showLoading();
            }
        } else {
            episodeId = "";
        }


        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("order", "");// Value for order key should be asc/desc.  if order key empty then by default will be desc

            object.put("searchTxt", searc.toLowerCase());
            object.put("episodeNo", episodeId);
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.fetchMedReports(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String response11 = null;
                    try {
                        response11 = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject object1 = new JSONObject(response11);
                        if (object1.has("code")) {
                            if (object1.getString("code").equalsIgnoreCase("403")) {
                                views.onFail("expired");
                            }

                            if (object1.getString("code").equalsIgnoreCase("401")) {
                                views.onFail(object1.getString("message"));
                            }

                        } else {
                            MedicalResponse response1 = new Gson().fromJson(response11, MedicalResponse.class);
                            if (response1 != null) {
                                if (response1.getStatus().equalsIgnoreCase("true")) {

                                    if (response1.getMedicalReports() != null)
                                        if (searc.length() == 0) {

                                            for (int i = 0; i < response1.getMedicalReports().size(); i++) {
                                                MedicalReport data1 = response1.getMedicalReports().get(i);
//                                                data1.setMRN(mrn_number);
//                                                db.medicalReportDataDao().saveMedicalReportData(data1);
                                            }
                                        }
                                    views.onGetMedicalReports(response1);

                                } else
                                    views.onFail(response1.getMessage());

                            } else {
                                views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
                views.hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {

//                    List<MedicalReport> medicalReports = db.medicalReportDataDao().loadMedicalReportData(mrn_number);

//                    if (medicalReports != null && medicalReports.size() > 0) {
//                        message = activity.getString(R.string.network_content);
//                        views.onFail(message);
//
//                        MedicalResponse response = new MedicalResponse();
//                        response.setMedicalReports(medicalReports);
//                        response.setStatus("true");
//                        response.setMessage("ddfs");
//                        response.setTotal_items(String.valueOf(medicalReports.size()));
//                        views.onGetMedicalReports(response);
//                    } else {
//                        views.onNoInternet();
//                    }
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetAllSickLeaveReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {

        if (!episodeId.equalsIgnoreCase("0")) {
            if (page.equalsIgnoreCase("0")) {
                if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                    views.showLoading();
            }
        } else {
            episodeId = "";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("order", "");// Value for order key should be asc/desc.  if order key empty then by default will be desc

            object.put("searchTxt", searc.toLowerCase());
            object.put("episodeNo", episodeId);
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SickLeaveResponse> xxx = webService.fetchSickLeaveReports(body);

        xxx.enqueue(new Callback<SickLeaveResponse>() {
            @Override
            public void onResponse(Call<SickLeaveResponse> call, Response<SickLeaveResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SickLeaveResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {

                            if (response1.getSickLeaves() != null)
                                for (int i = 0; i < response1.getSickLeaves().size(); i++) {
//                                    SickLeave data1 = response1.getSickLeaves().get(i);
//                                    data1.setMRN(mrn_number);
//                                    db.sickLeaveDataDao().saveSickLeaveData(data1);
                                }

                            views.onGetSickLeaveReports(response1);
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
            public void onFailure(Call<SickLeaveResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {

//                    List<SickLeave> sickLeave = db.sickLeaveDataDao().loadSickLeaveData(mrn_number);
//
//                    if (sickLeave != null && sickLeave.size() > 0) {
//                        message = activity.getString(R.string.network_content);
//                        views.onFail(message);
//
//                        SickLeaveResponse response = new SickLeaveResponse();
//                        response.setSickLeaves(sickLeave);
//                        response.setStatus("true");
//                        response.setMessage("ddfs");
//                        response.setTotalItems(String.valueOf(sickLeave.size()));
//                        views.onGetSickLeaveReports(response);
//                    } else {
//                        views.onNoInternet();
//                    }

                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }

    @Override
    public void callGenerateMedicalPdf(String token, String mr_number, String order_no, String order_no_line, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("order_no", order_no);
            object.put("order_no_line", order_no_line);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<ResponseBody> xxx = webService.generateMedReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    Headers headers = response.headers();

                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateMedicalPdf(response1, headers);

                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
    public void callGenerateLabPdf(String token, String mr_number, String report_type, String specimen_num, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            if (report_type == null)
                report_type = "";
            object.put("report_type", report_type);
            object.put("specimen_num", specimen_num);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generateLabReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateLabPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }


    @Override
    public void callGenerateSickLeavePdf(String token, String mr_number, String
            leave_id, String with_diagnosis, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("leave_id", leave_id);
            object.put("with_diagnosis", with_diagnosis);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generateSickLeaveReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateLabPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
    public void callGeneratePaymentPdf(String token, String mr_number, int hosp, int sessionId,String refId) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("hosp", hosp);
            object.put("sessionId", sessionId);
            object.put("refId", refId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generatePaymentReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGeneratePaymentPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
    public void callGetOperativeReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {
        if (page.equalsIgnoreCase("0")) {
            if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                views.showLoading();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("createdBy", searc);
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.fetchOperativeReports(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String response11 = null;
                        try {
                            response11 = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject object1 = new JSONObject(response11);
                            if (object1.has("code")) {
                                if (object1.getString("code").equalsIgnoreCase("403")) {
                                    views.onFail("expired");
                                }

                                if (object1.getString("code").equalsIgnoreCase("401")) {
                                    views.onFail(object1.getString("message"));
                                }
                                if (object1.getString("code").length() == 0) {
                                    views.onFail(object1.getString("message"));
                                }

                            } else {
                                OperativeResponse response1 = new Gson().fromJson(response11, OperativeResponse.class);
                                if (response1.getStatus().equalsIgnoreCase("true")) {
                                    views.onGetOperativeReports(response1);
                                } else
                                    views.onFail(response1.getMessage());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }

                views.hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
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
    public void callGetDischargeReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {
        if (page.equalsIgnoreCase("0")) {
            if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                views.showLoading();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("createdBy", searc);
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.fetchDischargeReports(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String response11 = null;
                        try {
                            response11 = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject object1 = new JSONObject(response11);
                            if (object1.has("code")) {
                                if (object1.getString("code").equalsIgnoreCase("403")) {
                                    views.onFail("expired");
                                }

                                if (object1.getString("code").equalsIgnoreCase("401")) {
                                    views.onFail(object1.getString("message"));
                                }

                                if (object1.getString("code").length() == 0) {
                                    views.onFail(object1.getString("message"));
                                }
                            } else {
                                OperativeResponse response1 = new Gson().fromJson(response11, OperativeResponse.class);
                                if (response1.getStatus().equalsIgnoreCase("true")) {
                                    views.onGetOperativeReports(response1);
                                } else
                                    views.onFail(response1.getMessage());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }

                views.hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
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

    //here
    @Override
    public void callGenerateOperativePdf(String token, String mr_number, String reportId, String reportType, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("reportId", reportId);
            object.put("reportType", reportType);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generateOperativeReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateLabPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }

    //{mrNumber: "004150", orderNumber: "B110431", language: "en"}


    @Override
    public void callGenerateSmartLabPdf(String token, String mr_number, String orderNumber, String language) {
        Log.d("Reports Impl", "callGenerateSmartLabPdf: Token is " + token);
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            // ["mrNumber": 004150, "orderNumber": B557892, "language": en]
            object.put("mrNumber", mr_number);
            object.put("orderNumber", orderNumber);
            object.put("language", "en");

        } catch (JSONException e) {
            Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside JSON exception");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        Call<ResponseBody> xxx = webService.generateSmartLabReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue ");
                Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue, Response.code: " + response.code());
                Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue, Response.headers: " + response.headers());
                Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue, Response.body: " + response.body().toString());
//                Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue, Gson body: "+new Gson().toJson(response.body()));
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {

                    ResponseBody response1 = response.body();
                    if (response1 != null) {
//                        views.onGenerateMedicalPdf(response1, headers);
//                        views.onGenerateLabPdf(response1, headers);
/*                            JSONObject jsonObject = new JSONObject(response1.toString());
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String fileData = dataObject.getString("file");
                            Log.d("Reports Impl", "onResponse: File data is: "+fileData);*/
//                            ResponseBody responseBody = new ;
                        views.onGenerateSmartReport(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    Log.d("ReportsImpl", "callGenerateSmartLabPdf: Inside xxx.enqueue, Erro rResponse is: " + response.body());
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";

                    views.onFail(message);

                }
            }
        });
    }


    @Override
    public void callGenerateDischargePdf(String token, String mr_number, String reportId, String reportType, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("reportId", reportId);
            object.put("reportType", reportType);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generateDischargeReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateLabPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }

    @Override
    public void callGetCardioReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {

        if (page.equalsIgnoreCase("0")) {
            if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                views.showLoading();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("searchTxt", searc);
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("episodeNo", "");
            object.put("order", "");
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.fetchCardioReports(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String response11 = null;
                        try {
                            response11 = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject object1 = new JSONObject(response11);
                            if (object1.has("code")) {
                                if (object1.getString("code").equalsIgnoreCase("403")) {
                                    views.onFail("expired");
                                }

                                if (object1.getString("code").equalsIgnoreCase("401")) {
                                    views.onFail(object1.getString("message"));
                                }

                                if (object1.getString("code").length() == 0) {
                                    views.onFail(object1.getString("message"));
                                }
                            } else {
                                CardioReportResponse response1 = new Gson().fromJson(response11, CardioReportResponse.class);
                                if (response1.getStatus().equalsIgnoreCase("true")) {
                                    views.onGetCardioReports(response1);
                                } else
                                    views.onFail(response1.getMessage());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }

                views.hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
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
    public void callGenerateCardioPdf(String token, String mr_number, String attendanceId, String reportType,int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("attendanceId", attendanceId);
            object.put("report_type", reportType);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        Call<ResponseBody> xxx = webService.generateCardioReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGenerateLabPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(activity);
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
    }


    @Override
    public void callGetAllLabReportsMedicus(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp) {
        if (!episodeId.equalsIgnoreCase("0")) {
            if (page.equalsIgnoreCase("0")) {
                if (searc.length() == 0 && (from.length() > 0 || to.length() > 0 || from.length() == 0 || to.length() == 0))
                    views.showLoading();
            }
        } else {
            episodeId = "";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrn_number);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("episodeNo", episodeId);
            object.put("order", "");// Value for order key should be asc/desc.  if order key empty then by default will be desc
            object.put("searchTxt", searc.toLowerCase());
            object.put("itemCount", item_set);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.fetchLabReportsMedicus();
        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String response11;
                    if (responseBody != null) {
                        try {
                            response11 = responseBody.string();
                            JSONObject object1 = new JSONObject(response11);
                            if (object1.has("code")) {
                                if (object1.getString("code").equalsIgnoreCase("403")) {
                                    views.onFail("expired");
                                }

                                if (object1.getString("code").equalsIgnoreCase("401")) {
                                    views.onFail(object1.getString("message"));
                                }

                            } else {
                                if (object1.getString("status").equalsIgnoreCase("true")) {
                                    if (searc.length() == 0) {
                                        if (object1.getJSONArray("data") != null && object1.getJSONArray("data").length() != 0) {
                                            List<LabReportsParentMedicus> lstOfParentLabReports = new ArrayList<>();

                                            for (int i = 0; i < object1.getJSONArray("data").length(); i++) {
                                                JSONObject json_array = object1.getJSONArray("data").getJSONObject(i);

                                                Iterator<?> keys = json_array.keys();
                                                //  List<LabReportsMedicus> lstOfChildLabReports = new ArrayList<>();
                                                String familyName = null;
                                                String givenName = null;
                                                String orderNum = null;
                                                String aptTime = null;

                                                while (keys.hasNext()) {
                                                    String key = (String) keys.next();
                                                    Log.e("key====", " " + key);
                                                    JSONArray jsonArray = object1.getJSONArray("data").getJSONObject(i).getJSONArray(key);
                                                    Log.e(key + "====", " " + jsonArray.toString());
                                                    //   LabReportsMedicus[] resultant = new Gson().fromJson(jsonArray.toString(), LabReportsMedicus[].class);
                                                    Type userListType = new TypeToken<ArrayList<LabReportsMedicus>>() {
                                                    }.getType();
                                                    List<LabReportsMedicus> userArray = new Gson().fromJson(jsonArray.toString(), userListType);

//                                                    familyName = userArray.get(0).getFamilyName();
                                                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                                                        givenName = userArray.get(0).getDocNameAr();
                                                    } else {
                                                        givenName = userArray.get(0).getDocName();
                                                    }

                                                    orderNum = userArray.get(0).getOrderNum();
                                                    aptTime = userArray.get(0).getAptTime();
                                                    //  lstOfChildLabReports.add(userArray);
                                                    LabReportsParentMedicus labReportsParentMedicus = new LabReportsParentMedicus();
//                                                    labReportsParentMedicus.setFamilyName(familyName);

                                                    labReportsParentMedicus.setGivenName(givenName);
                                                    labReportsParentMedicus.setOrderNum(orderNum);
                                                    labReportsParentMedicus.setAptTime(aptTime);
                                                    labReportsParentMedicus.setLstOfChildMedicus(userArray);
                                                    lstOfParentLabReports.add(labReportsParentMedicus);
                                                }

                                            }

                                            views.onGetLabReportsMedicus(lstOfParentLabReports, object1.getInt("total_items"));
                                        } else {
                                            views.onGetLabReportsMedicus(null, object1.getInt("total_items"));
                                        }
                                    }
                                } else {
                                    //fail....
                                }

                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }


                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }

                views.hideLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = t.toString();
                views.onFail(message);
            }
        });


    }


    @Override
    public void callGetPin(String token, String mr_number) {

        views.showLoading();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain; charset=utf-8"), mr_number);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PinResponse> xxx = webService.getPinForMedicus(mr_number);

        Log.e("abcd",xxx.request().url().toString());

        xxx.enqueue(new Callback<PinResponse>() {
            @Override
            public void onResponse(Call<PinResponse> call, Response<PinResponse> response) {
                Log.e("abcd",response.body().getData().getPin());
                Log.e("abcd",new Gson().toJson(response.body()));
                views.hideLoading();
                if (response.isSuccessful()) {
                    PinResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus() == 200) {
                            views.onGetPin(response1);
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
            public void onFailure(Call<PinResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = t.getMessage();
                    views.onFail(message);
                }
            }
        });
    }


}

