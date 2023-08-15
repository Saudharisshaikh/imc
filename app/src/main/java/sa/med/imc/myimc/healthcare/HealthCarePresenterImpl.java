package sa.med.imc.myimc.healthcare;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Base.GenericResponse;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;
import sa.med.imc.myimc.healthcare.model.HealthCareListRequest;
import sa.med.imc.myimc.healthcare.model.HealthCareResponse;
import sa.med.imc.myimc.healthcare.model.HealthCareSubmission;
import timber.log.Timber;

public class HealthCarePresenterImpl implements HealthCarePresenter {
    private final HealthCareView view;

    public HealthCarePresenterImpl(HealthCareView view) {
        this.view = view;
    }

    @Override
    public void onHealthCareRequest() {
        view.showLoading();
        HealthCareListRequest request = new HealthCareListRequest();
        int hospId = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.SELECTED_HOSPITAL,-1);
        request.setHosp(String.valueOf(hospId));
        request.setHHCFormId(1);
        Call<GenericResponse<List<HealthCareItem>>> call = ServiceGenerator.createService(WebService.class,SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN,""))
                .fetchHealthCareList(request);
        call.enqueue(new Callback<GenericResponse<List<HealthCareItem>>>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse<List<HealthCareItem>>> call, @NonNull Response<GenericResponse<List<HealthCareItem>>> response) {
                view.hideLoading();
                String value = new Gson().toJson(response.body());
                if (response.isSuccessful() && response.body()!=null && response.body().getData()!=null){
                    view.onHealthCareListResponse(response.body().getData());
                }
                else if (response.body()!=null){
                    view.onFail(response.body().getMessage());
                }
                else{
                    view.onFail("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse<List<HealthCareItem>>> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onFail(t.getMessage());
            }
        });
    }

    @Override
    public void onHealthCareSubmit(String name,String email,String number,List<HealthCareItem> list) {
        view.showLoading();
        HealthCareSubmission submission=new HealthCareSubmission();
        submission.setSubmittedFor(name);
        submission.setContact(number);
        String patId = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_MRN_FOR_HHC,"");
        int hospId = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.SELECTED_HOSPITAL,-1);
//        String token = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN,"");
        submission.setHHCFormId(1);
        submission.setEmail(email);
        submission.setHosp(String.valueOf(hospId));
        submission.setPatId(patId.isEmpty() ? "guest" : patId);
        List<Integer> selections = new ArrayList<>();
        sortSelection(list,selections);
        submission.setQuestionIds(selections);
        Call<GenericResponse<String>> call = ServiceGenerator.createService(WebService.class)
                .healthCareSubmission(submission);

        Log.e("abcd",new Gson().toJson(submission));
        Log.e("abcd",call.request().url().toString());

        call.enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse<String>> call,@NonNull Response<GenericResponse<String>> response) {
                view.hideLoading();
                Log.e("abcd",new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.code()==200){
                    view.onHealthCareSubmission();
                }
                else {
                    view.onFail("Failed");
                }

            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse<String>> call,@NonNull Throwable t) {
                view.hideLoading();
                view.onFail(t.getMessage());
            }
        });

    }

    private void sortSelection(List<HealthCareItem> list, List<Integer> selections) {
        for (HealthCareItem item : list){
            if (item.isChecked()) {
                selections.add(item.getId());
                sortSelection(item.getLinkedHHCFormQuestions(),selections);
            }
        }
    }
}
