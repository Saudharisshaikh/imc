package sa.med.imc.myimc.Departments.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import sa.med.imc.myimc.Departments.model.DepartmentDoctorResponse;
import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.Departments.view.DepartmentViews;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Get Department API implementation class.
 */

public class DepartmentImpl implements DepartmentsPresenter {

    private Activity activity;
    private DepartmentViews views;
    private WebService webService;

    public DepartmentImpl(DepartmentViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
         webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");

    }

    @Override
    public void callAllDepartments(int page, String name) {
        if (page == 1) {
            if (name.length() == 0) {
                views.showLoading();
            }
        } else {
            views.showLoading();
        }

        Call<DepartmentResponse> xxx = webService.getAllDepartments(String.valueOf(page), name);

        Log.e("abcd",xxx.request().url().toString());
        Log.e("abcd",new Gson().toJson(xxx.request()));
        Log.e("abcd",name);

        xxx.enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                views.hideLoading();
                if (response.isSuccessful()) {
                    DepartmentResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus()) {
                            views.onSuccess(response1);
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
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));

                }
            }
        });
    }

    @Override
    public void callDepartmentDoctors(String id) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
        Call<DepartmentDoctorResponse> xxx = webService.getDepartmentDoctors(id);

        Log.e("abcd",xxx.request().url().toString());

        xxx.enqueue(new Callback<DepartmentDoctorResponse>() {
            @Override
            public void onResponse(Call<DepartmentDoctorResponse> call, Response<DepartmentDoctorResponse> response) {
                Log.e("abcd",new Gson().toJson(response.body()));

                views.hideLoading();
                if (response.isSuccessful()) {
                    DepartmentDoctorResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus()) {
                            views.onDoctorSuccess(response1);
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
            public void onFailure(Call<DepartmentDoctorResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }
        });
    }
}
