package sa.med.imc.myimc.medprescription.presenter;

import android.app.Activity;

import com.facebook.share.Share;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.medprescription.model.PrescriptionResponse;
import sa.med.imc.myimc.medprescription.view.PrescriptionViews;

/**
 * Get Prescription data
 */

public class PrescriptionImpl implements PrescriptionPresenter {

    private Activity activity;
    private PrescriptionViews views;

    public PrescriptionImpl(PrescriptionViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }


    @Override
    public void callGetPrescriptionApi(String token, String mrNumber, String epsoideNo, String fromDate, String toDate, String itemCount, String page, int hosp) {
        if (!page.equalsIgnoreCase("0"))
            views.showLoading();

        if (fromDate.length() > 0)
            fromDate = Common.getConvertDatePrescription(fromDate);

        if (toDate.length() > 0)
            toDate = Common.getConvertDatePrescription(toDate);

        JSONObject object = new JSONObject();
        try {
            object.put("patId", mrNumber);
            object.put("item_count", itemCount);
            object.put("epsoideNo", epsoideNo);

            object.put("fromDate", fromDate);
            object.put("toDate", toDate);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PrescriptionResponse> xxx = webService.fetchPrescription(body);

        xxx.enqueue(new Callback<PrescriptionResponse>() {
            @Override
            public void onResponse(Call<PrescriptionResponse> call, Response<PrescriptionResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    PrescriptionResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetPrescription(response1);
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
            public void onFailure(Call<PrescriptionResponse> call, Throwable t) {
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
    public void callGeneratePrescriptionPdf(String token, String mr_number, String prespNumber, String epsoideNo, int hosp) {
        Common.showDialog(activity);
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mr_number", mr_number);
            object.put("prespNumber", prespNumber);
            object.put("episodeNumber", epsoideNo);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<ResponseBody> xxx = webService.generatePrescription(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideDialog();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onGeneratePrescriptionPdf(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hideDialog();

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
