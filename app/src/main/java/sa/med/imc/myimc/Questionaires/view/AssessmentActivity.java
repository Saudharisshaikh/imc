package sa.med.imc.myimc.Questionaires.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.ItemAssessmentAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentImpl;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

/*
Assessment questions and answers.
Start API and get questions one by one.
APIs implemented.
 */
public class AssessmentActivity extends BaseActivity implements AssessmentViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.rv_assessment)
    RecyclerView rvAssessment;
    @BindView(R.id.no_result)
    TextView noResult;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    @BindView(R.id.lay_btn_exit)
    LinearLayout layBtnExit;
    @BindView(R.id.lay_btn_previous)
    LinearLayout lay_btn_previous;
    @BindView(R.id.lay_bt)
    LinearLayout lay_bt;
    ItemAssessmentAdapter adapter;
    List<AssessmentModel.Map> list = new ArrayList<>();
    AssessmentPresenter presenter;
    BookingAppoinment.PromisForm promisForm;
    BookingAppoinment booking;
    @Override
    protected Context getActivityContext() {
        return this;
    }
    String id;

    //Start activity from Questionare Activity
    public static void startActivity(Activity activity, BookingAppoinment.PromisForm promisForm1,BookingAppoinment booking1) {
        Intent intent = new Intent(activity, AssessmentActivity.class);
        intent.putExtra("promisForm", new Gson().toJson(promisForm1));
        intent.putExtra("booking", new Gson().toJson(booking1));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        ButterKnife.bind(this);
        presenter = new AssessmentImpl(this, this);

        lay_btn_previous.setEnabled(false);

        booking = new Gson().fromJson(getIntent().getStringExtra("booking"), new TypeToken<BookingAppoinment>() {}.getType());
        promisForm = new Gson().fromJson(getIntent().getStringExtra("promisForm"), new TypeToken<BookingAppoinment.PromisForm>() {}.getType());

//        presenter.callGetFirstQuestion(token, getIntent().getStringExtra("id"));
        presenter.callGetFirstQuestion(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                promisForm,booking);

    }

    // Initialize list view and add adapter to display data,
    void setUpRecyclerView() {
        adapter = new ItemAssessmentAdapter(this, list);
        rvAssessment.setHasFixedSize(true);
        rvAssessment.setLayoutManager(new LinearLayoutManager(this));
        rvAssessment.setAdapter(adapter);
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_done, R.id.lay_btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                break;

            case R.id.lay_btn_done:
                if (adapter.getSelected() > -1) {
//                    presenter.callGetNextAssessment(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), promisForm.getAssessmentId(),
//                            list.get(adapter.getSelected()).getItemResponseOID(),
//                            list.get(adapter.getSelected()).getValue());

                     presenter.callGetNextAssessment(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            id,
                            list.get(adapter.getSelected()).getItemResponseOID(),
                            list.get(adapter.getSelected()).getValue(),
                             SharedPreferencesUtils.getInstance(AssessmentActivity.this).getValue(Constants.SELECTED_HOSPITAL,0));


                }
                break;

            case R.id.lay_btn_exit:
                exitDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        AssessmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // exit alert dialog.
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this);
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
                    Typeface typeface = ResourcesCompat.getFont(AssessmentActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(AssessmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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
    public void onGetAssessmentFirst(AssessmentResponse response12) {
        id=response12.getAssessmentId();
        if (response12.getAssessmentId() != null)
            id = response12.getAssessmentId();
        AssessmentModel response1 = response12.getAssessment();
        if (response1 != null)
            if (response1.getItems() != null) {
                lay_btn_previous.setVisibility(View.GONE);
                layBtnDone.setVisibility(View.VISIBLE);

                int size = response1.getItems().get(0).getElements().size();
                String tag = "";
                for (int i = 0; i < size - 1; i++) {
                    if (i == 0)
                        tag = response1.getItems().get(0).getElements().get(i).getDescription();
                    else
                        tag = tag + " " + response1.getItems().get(0).getElements().get(i).getDescription();
                }

                label.setText(tag);

                list = new ArrayList<>();
                /*if(response1.getItems() == null || response1.getItems().get(0) == null || response1.getItems().get(0).getElements() == null || response1.getItems().get(0).getElements()) {
                    // handling the null exception
                    Toast.makeText(getActivityContext(), getResources().getString(R.string.no_data_content), Toast.LENGTH_LONG).show();
                } else {*/
                if(response1.getItems().get(0).getElements().size() > 1) {
                    list.addAll(response1.getItems().get(0).getElements().get(size - 1).getMaps());
                    setUpRecyclerView();
                } else {
                    Toast.makeText(getActivityContext(), getResources().getString(R.string.no_data_content), Toast.LENGTH_LONG).show();
                }
            } else {
                Common.makeToast(AssessmentActivity.this, getString(R.string.try_again));
            }
    }

    @Override
    public void onGetAssessmentNext(AssessmentResponse response12) {
        id=response12.getAssessmentId();
        if (response12.getAssessmentId() != null)
            id = response12.getAssessmentId();
        AssessmentModel response1 = response12.getAssessment();
        if (response1 != null)
            if (response1.getItems() != null) {
                if (response1.getDateFinished() != null && response1.getDateFinished().length() > 0) {
                    ResultAssessmentActivity.startActivity(this, response12.getAssessmentId(), booking.getId(), id,
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), promisForm.getFormName());
                } else {
                    lay_btn_previous.setEnabled(true);
                    int size = response1.getItems().get(0).getElements().size();
                    String tag = "";
                    for (int i = 0; i < size - 1; i++) {
                        if (i == 0)
                            tag = response1.getItems().get(0).getElements().get(i).getDescription();
                        else
                            tag = tag + " " + response1.getItems().get(0).getElements().get(i).getDescription();
                    }

                    label.setText(tag);
                    list = new ArrayList<>();
                    list.addAll(response1.getItems().get(0).getElements().get(size - 1).getMaps());
                    setUpRecyclerView();
                }

            } else {
                Common.makeToast(AssessmentActivity.this, getString(R.string.try_again));
            }
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
}
