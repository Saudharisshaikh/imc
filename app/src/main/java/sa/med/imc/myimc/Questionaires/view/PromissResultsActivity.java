package sa.med.imc.myimc.Questionaires.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.view.PhysiciansActivity;
import sa.med.imc.myimc.Questionaires.model.ResultSingleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.view.SignUpStep1Activity;
import sa.med.imc.myimc.Utils.Common;
/*
Final assessment result screen
 * Patient's score
 * Suggestion for patient
 */

public class PromissResultsActivity extends BaseActivity {
    TextView label, tv_score;
    @BindView(R.id.tv_std_error)
    TextView tv_std_error;
    @BindView(R.id.tv_theta)
    TextView tvTheta;
    @BindView(R.id.tv_doc_name)
    TextView tvDocName;
    @BindView(R.id.tv_appointment_date)
    TextView tvAppointmentDate;
    @BindView(R.id.tv_submission_date)
    TextView tvSubmissionDate;


    // open from completed forms list and submission assessment
    public static void startActivity(Activity activity, String asss_id) {
        Intent intent = new Intent(activity, PromissResultsActivity.class);
        intent.putExtra("asss_id", asss_id);
        activity.startActivity(intent);
        activity.finish();
    }

    // open from completed forms list and submission assessment
    public static void startActivity(Activity activity, String score, String form_name, String theta, String std) {
        Intent intent = new Intent(activity, PromissResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("form_name", form_name);
        intent.putExtra("theta", theta);
        intent.putExtra("std", std);
        activity.startActivity(intent);
    }

    public static void startActivityFromCompletedAssessment(Activity activity, String form, String result) {
        Intent intent = new Intent(activity, PromissResultsActivity.class);
        intent.putExtra("form", form);
        intent.putExtra("result", result);
        activity.startActivity(intent);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(PromissResultsActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        PromissResultsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promiss_results);
        ButterKnife.bind(this);
        label = findViewById(R.id.label);
        tv_score = findViewById(R.id.tv_score);

        if (getIntent().hasExtra("asss_id")) {
            viewResult(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), getIntent().getStringExtra("asss_id"));

        } else if(getIntent().hasExtra("form")) {
            showFormAndResult(getIntent().getStringExtra("form"), getIntent().getStringExtra("result"));
        }
        else {
            label.setText(getString(R.string.your_score, getIntent().getStringExtra("form_name")));
            tv_std_error.setText(getString(R.string.std_error, getIntent().getStringExtra("std")));
            tvTheta.setText(getString(R.string.theta_value, getIntent().getStringExtra("theta")));
            tv_score.setText(getIntent().getStringExtra("score"));

            label.setVisibility(View.VISIBLE);
            tv_std_error.setVisibility(View.VISIBLE);
            tvTheta.setVisibility(View.VISIBLE);
            tv_score.setVisibility(View.VISIBLE);
        }
    }

    private void showFormAndResult(String form, String result) {
        // text
        label.setText(getString(R.string.your_score, form));
        label.setVisibility(View.VISIBLE);

        // count
        tv_score.setText(result);
        tv_score.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
//        String baseAcrivity=SharedPreferencesUtils.getInstance(PromissResultsActivity.this).getValue(Constants.BASE_ACTIVITY_FOR_ACCESSMENT,"main");
//        if (baseAcrivity.equalsIgnoreCase("main")){
//
//        }else {
//
//        }
        Intent intent = new Intent(PromissResultsActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
//        PromissResultsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void viewResult(String token, String mrNumber, String assessmentId) {
        Common.showDialog(this);

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("assessmentId", assessmentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, BuildConfig.BASE_URL, token);
        Call<ResultSingleResponse> xxx = webService.viewResult(body);

        xxx.enqueue(new Callback<ResultSingleResponse>() {
            @Override
            public void onResponse(Call<ResultSingleResponse> call, Response<ResultSingleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    ResultSingleResponse response1 = response.body();
                    if (response1 != null) {
                        label.setText(getString(R.string.your_score, response1.getFormName()));
                        tv_score.setText(response1.getResult());
                        tvAppointmentDate.setText(getString(R.string.appointment_date_) + " " + Common.completedAssessmentDateTime(response1.getBookingdate()));
                        tvSubmissionDate.setText(getString(R.string.submission_date) + " " + Common.completedAssessmentDateTime(response1.getCompletedDate()));

                        if (SharedPreferencesUtils.getInstance(PromissResultsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                            tvDocName.setText(getString(R.string.dr) + " " + response1.getPhysicianNameAr());
                        } else {
                            tvDocName.setText(getString(R.string.dr) + " " + response1.getPhysicianName());
                        }

                        tvAppointmentDate.setVisibility(View.VISIBLE);
                        tvSubmissionDate.setVisibility(View.VISIBLE);
                        tvDocName.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResultSingleResponse> call, Throwable t) {
                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
            }
        });

    }

}
