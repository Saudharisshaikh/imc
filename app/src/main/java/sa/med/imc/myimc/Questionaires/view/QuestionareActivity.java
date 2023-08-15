package sa.med.imc.myimc.Questionaires.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import sa.med.imc.myimc.BuildConfig;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Adapter.QuestionsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.model.FormIdNameModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Questionaires.model.FormsResponse;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentImpl;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentPresenter;
import sa.med.imc.myimc.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Utils.Common;

public class QuestionareActivity extends BaseActivity implements AssessmentViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.rv_medical_history)
    RecyclerView rvMedicalHistory;
    @BindView(R.id.no_result)
    TextView noResult;
    QuestionsAdapter adapter;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    BookingAppoinment booking;
    String token = "";//"Basic REFFNDgwOUItNTk1My00NjYwLTg3RjEtNUI3RkREQzI5RjVFOkUwRkNGNDBGLTU0MzEtNEM4QS05MkI5LTI3NzkyMkIzOUYzRA==";
    AssessmentPresenter presenter;
    String reg_oid = "", token_promis = "", assessment_ID = "";
    boolean isLoaded = false;


    //Start activity from upcoming appointment
    public static void startActivity(Context context, BookingAppoinment booking) {
        Intent intent = new Intent(context, QuestionareActivity.class);
        intent.putExtra("booking", new Gson().toJson(booking));
        context.startActivity(intent);
    }


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QuestionareActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionare);

        ButterKnife.bind(this);
        presenter = new AssessmentImpl(this, this);
//        presenter.callGetTokens(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""));

//        booking = (BookingAppoinment) getIntent().getSerializableExtra("booking");
        booking = new Gson().fromJson(getIntent().getStringExtra("booking"), new TypeToken<BookingAppoinment>() {}.getType());

        Log.e("abcd",new Gson().toJson(booking.getPromisForm()));
//        getListForms(booking.getPromisForm());
        setUpQuestionsRecyclerView();

        token=SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "");;

    }


    @OnClick({R.id.iv_back, R.id.lay_btn_done, R.id.lay_btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_exit:
                exitDialog();
                break;

            case R.id.lay_btn_done:
                if (adapter.getSelected() > -1) {
                    isLoaded = true;

                    if (booking.getPromisForm().get(adapter.getSelected()).getAssessmentId() != null)
                        assessment_ID = booking.getPromisForm().get(adapter.getSelected()).getAssessmentId();
//                        presenter.callGetAssessmentToken(token, list.get(adapter.getSelected()).getFormId(), String.valueOf(booking.getId()));
//                    else {
//                        isLoaded = true;
                    AssessmentActivity.startActivity(QuestionareActivity.this,booking.getPromisForm().get(adapter.getSelected()),booking);
//                    }
//                    AssessmentActivity.startActivity(QuestionareActivity.this,
//                            list.get(adapter.getSelected()).getFormId(), String.valueOf(booking.getId()),
//                            assessment_ID, token, list.get(adapter.getSelected()).getFormName());
////                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        QuestionareActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (adapter != null) {
//            if (isLoaded)
//                getListForms(booking.getPromisForm());
//        }
    }

    // Initialize Questions list view and add adapter to display data,
    void setUpQuestionsRecyclerView() {
        adapter = new QuestionsAdapter(this,booking.getPromisForm());
        rvMedicalHistory.setHasFixedSize(true);
        rvMedicalHistory.setLayoutManager(new LinearLayoutManager(this));
        rvMedicalHistory.setAdapter(adapter);
        adapter.setOnClickListener(new QuestionsAdapter.OnClickListener() {
            @Override
            public void onChecked(int position) {
                if (booking.getPromisForm().get(position).getAssessmentId() != null)
                    tv_start.setText(getString(R.string.resume));
                else
                    tv_start.setText(getString(R.string.start));
                layBtnDone.setVisibility(View.VISIBLE);
            }
        });
    }


    // exit alert dialog.
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionareActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.exit_alert_assess));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.CONTAINER_FRAGMENT = "BaseFragment";
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
                try {
                    Typeface typeface = ResourcesCompat.getFont(QuestionareActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(QuestionareActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
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
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        makeToast(msg);
    }

    @Override
    public void onNoInternet() {
    }

//    public void getListForms(List<BookingAppoinment.PromisForm> promisFormList) {
//        if (promisFormList.size() >0)
//            if (adapter != null) {
//                list.clear();
//                list=promisFormList;
//                adapter.notifyDataSetChanged();
//
//
//                if (list.size() == 0) {
//                    layBtnDone.setVisibility(View.INVISIBLE);
//                    finish();
//                }
//            } else
//                layBtnDone.setVisibility(View.INVISIBLE);
//        else {
//            layBtnDone.setVisibility(View.INVISIBLE);
//            finish();
//        }
//
////
////        Common.showDialog(this);
////
////        JSONObject object = new JSONObject();
////        isLoaded = false;
////        try {
////            object.put("mrNumber", mrNumber);
////            object.put("sessionId", sessionId);
////            object.put("bookingId", bookingId);
////            object.put("hosp", hosp);
////
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////
////        Log.e("abcd",object.toString());
////        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
////
////        WebService webService = ServiceGenerator.createService(WebService.class, BuildConfig.BASE_URL, token);
////        Call<FormsResponse> xxx = webService.getListAssessment(body);
////
////        xxx.enqueue(new Callback<FormsResponse>() {
////            @Override
////            public void onResponse(Call<FormsResponse> call, Response<FormsResponse> response) {
////                Common.hideDialog();
////                if (response.isSuccessful()) {
////                    FormsResponse response1 = response.body();
////                    if (response1 != null) {
////                        try {
////                            Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
////                            refresh.putExtra("cancel", "cancel");
////                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(refresh);
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////
////                        if (response1.getFormIdNameModels() != null)
////                            if (adapter != null) {
////                                list.clear();
////                                list.addAll(response1.getFormIdNameModels());
////                                adapter.notifyDataSetChanged();
////
////
////                                if (list.size() == 0) {
////                                    layBtnDone.setVisibility(View.INVISIBLE);
////                                    finish();
////                                }
////                            } else
////                                layBtnDone.setVisibility(View.INVISIBLE);
////                        else {
////                            layBtnDone.setVisibility(View.INVISIBLE);
////                            finish();
////                        }
////                    }
////                }
////            }
////
////            @Override
////            public void onFailure(Call<FormsResponse> call, Throwable t) {
////                Common.hideDialog();
//////                Common.makeToast(ContactUsActivity.this, t.getMessage());
////            }
////        });
//
//    }

}
