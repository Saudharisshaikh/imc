package sa.med.imc.myimc.webView;

import android.app.Activity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.webView.model.ValidatePaymentRefRequest;

public class WebViewPresenterImpl implements WebViewPresenter{

    private Activity activity;
    private WebViewViews viewViews;

    public WebViewPresenterImpl(Activity activity, WebViewViews viewViews) {
        this.activity = activity;
        this.viewViews = viewViews;
    }

    @Override
    public void validatePaymentRef(String ref) {
        viewViews.showLoading();
        ValidatePaymentRefRequest request = new ValidatePaymentRefRequest();
        request.setRefId(ref);
        Call<JsonObject> call = ServiceGenerator.createPharmacyService(WebService.class, SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_ACCESS_TOKEN,""))
                .validatePaymentRef(request);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                viewViews.onValidatePaymentRef();
                viewViews.hideLoading();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                viewViews.hideLoading();
            }
        });


    }
}
