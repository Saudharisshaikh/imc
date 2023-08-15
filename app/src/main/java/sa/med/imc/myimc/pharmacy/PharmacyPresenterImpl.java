package sa.med.imc.myimc.pharmacy;

import android.app.Activity;
import android.app.Service;

import com.google.gson.JsonObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.pharmacy.model.PharmacyItem;
import sa.med.imc.myimc.pharmacy.model.PharmacyResponse;
import sa.med.imc.myimc.pharmacy.model.PharmacyTelrResponse;
import sa.med.imc.myimc.pharmacy.model.ValidatePharmacyPaymentRequest;

public class PharmacyPresenterImpl implements PharmacyPresenter{
    private final Activity activity;
    private final PharmacyView views;

    public PharmacyPresenterImpl(Activity activity,PharmacyView view){
        this.activity=activity;
        this.views=view;
    }

    @Override
    public void callPrescriptionDetails(String rxNo, String episodeNo) {
        views.showLoading();
        WebService webService = ServiceGenerator.createPharmacyService(WebService.class, "Bearer " + SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_ACCESS_TOKEN,""));
        String patId =  SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_MRN,"");
        int hospId = SharedPreferencesUtils.getInstance(activity).getValue(Constants.SELECTED_HOSPITAL,-1);

        Call<List<PharmacyItem>> xxx = webService.fetchPrescriptionMeds(patId,String.valueOf(hospId),rxNo,episodeNo);

        xxx.enqueue(new Callback<List<PharmacyItem>>() {
            @Override
            public void onResponse(Call<List<PharmacyItem>> call, Response<List<PharmacyItem>> response) {
                views.hideLoading();
                if (response.isSuccessful() && response.code()==200){
                    views.onDetailResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PharmacyItem>> call, Throwable t) {
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
    public void validate(PharmacyItem item) {
        views.showLoading();
        ValidatePharmacyPaymentRequest request = new ValidatePharmacyPaymentRequest();
        request.setEpisodeNo(String.valueOf(item.getEpisodeNo()));
        request.setOrderNo(String.valueOf(item.getOrderNo()));
        request.setRxNo(item.getRxNo());
        request.setMrNumber(SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_MRN,""));
        request.setPatName(SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_MRN,""));
        request.setAmount("100");
        Call<PharmacyTelrResponse> call = ServiceGenerator.createPharmacyService(WebService.class,SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_ACCESS_TOKEN,"")).validatePharmD(request);
        call.enqueue(new Callback<PharmacyTelrResponse>() {
            @Override
            public void onResponse(Call<PharmacyTelrResponse> call, Response<PharmacyTelrResponse> response) {
                views.hideLoading();
                if (response.isSuccessful() && response.code()==200){
                    views.onPaymentValidation(response.body());
                }
            }

            @Override
            public void onFailure(Call<PharmacyTelrResponse> call, Throwable t) {
                views.hideLoading();
            }
        });
    }
}
