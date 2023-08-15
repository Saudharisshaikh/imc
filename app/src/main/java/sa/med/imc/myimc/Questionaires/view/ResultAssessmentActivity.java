package sa.med.imc.myimc.Questionaires.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Questionaires.model.ResultResponse;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentImpl;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

public class ResultAssessmentActivity extends BaseActivity implements AssessmentViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lay_btn_view_report)
    LinearLayout layBtnViewReport;
    String token = "", id = "";
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.main_iew)
    RelativeLayout mainIew;
    AssessmentPresenter presenter;
    String score = "0";
    ResultResponse.Score resultModel;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity, String form_id, String booking_id, String id, String token, String form_name) {
        Intent intent = new Intent(activity, ResultAssessmentActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("form_id", form_id);
        intent.putExtra("booking_id", booking_id);
        intent.putExtra("token", token);
        intent.putExtra("form_name", form_name);

        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_assessment);
        ButterKnife.bind(this);
        presenter = new AssessmentImpl(this, this);

        id = getIntent().getStringExtra("id");
        token = getIntent().getStringExtra("token");
        getResults(id);
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_view_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_view_report:
                if (resultModel != null) {
                    PromissResultsActivity.startActivity(this, score, getIntent().getStringExtra("form_name"), resultModel.getTheta(), resultModel.getStdError());
                    finish();
                    ResultAssessmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    makeToast(getString(R.string.plesae_try_again));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        ResultAssessmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void getResults(String id) {
        Common.showDialog(this);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("assessmentId",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WebService webService = ServiceGenerator.createService(WebService.class, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (jsonObject).toString());
        Call<ResultResponse> xxx = webService.getAssessmentResult(body);

        Log.e("abcd",jsonObject.toString());
        xxx.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                Common.hideDialog();
                Log.e("abcd",new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    ResultResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getScore() != null) {
                            if (response1.getScore().getTScore() != null) {
//                          layBtnViewReport.setVisibility(View.VISIBLE);
                                score = response1.getScore().getTScore();
                                resultModel = response1.getScore();

//                            presenter.callSubmitAssessmentToken(SharedPreferencesUtils.getInstance(ResultAssessmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(ResultAssessmentActivity.this).getValue(Constants.KEY_MRN, ""),
//                                    getIntent().getStringExtra("form_id"), getIntent().getStringExtra("booking_id"), id, "", response1.getTScore(), response1.getTheta(), response1.getStdError());

                            } else {
                                Common.makeToast(ResultAssessmentActivity.this, getString(R.string.try_again));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
            }
        });

    }

    @Override
    public void onGetAssessmentFirst(AssessmentResponse response) {

    }

    @Override
    public void onGetAssessmentNext(AssessmentResponse response) {

    }

    @Override
    public void onGetListAssessments(CompletedResponse response) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {

    }

    @Override
    public void onNoInternet() {

    }
}
