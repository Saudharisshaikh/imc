package sa.med.imc.myimc.AddGuardian.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.AddGuardian.model.GuardianResponse;
import sa.med.imc.myimc.AddGuardian.presenter.GuardianImpl;
import sa.med.imc.myimc.AddGuardian.presenter.GuardianPresenter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

/*
Add Guardian to provide access to other MRN user to view all detail and to book appointment.
Link/Unlink guardian to remove and grant access.
 */

public class AddGuardianActivity extends BaseActivity implements GuardianViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_mrn)
    EditText etMrn;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    @BindView(R.id.rd_unlimited)
    RadioButton rdUnlimited;
    @BindView(R.id.rd_no_of_days)
    RadioButton rdNoOfDays;
    @BindView(R.id.et_number_days)
    EditText etNumberDays;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.switchActive)
    Switch switchActive;

    GuardianPresenter presenter;
    @BindView(R.id.tv_button)
    TextView tvButton;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.mainLay)
    RelativeLayout mainLay;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;

    public static boolean isAdded = false;

    GuardianResponse response;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startSetting(Activity activity) {
        Intent intent = new Intent(activity, AddGuardianActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        AddGuardianActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guardian);
        ButterKnife.bind(this);
        isAdded = false;
        presenter = new GuardianImpl(this, this);
        callGetApi();
        rdUnlimited.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etNumberDays.setVisibility(View.GONE);
            }
        });

        rdNoOfDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etNumberDays.setVisibility(View.VISIBLE);
            }
        });

        switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (response != null) {
                if (response.getGuardianId() != null) {
                    if (buttonView.isPressed()) {
                        if (isNetAvail()) {
                            if (switchActive.isChecked()) {
                                presenter.callLinkUnlinkGuardian(SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                        SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_MRN, ""), response.getGuardianId(), 1,
                                        SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                            } else {
                                presenter.callLinkUnlinkGuardian(SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                        SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_MRN, ""), response.getGuardianId(), 0,
                                        SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                            }
                        } else {

                            Common.noInternet(AddGuardianActivity.this);
                            if (response.getIsActive() > 0) {
                                switchActive.setChecked(true);
                            } else {
                                switchActive.setChecked(false);
                            }
                        }
                    }
                }
            } else {
                if (buttonView.isPressed()) {
                    if (switchActive.isChecked()) {
                        tvActive.setText(getString(R.string.active));
                    } else {
                        tvActive.setText(getString(R.string.inactive));

                    }
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAdded)
            onBackPressed();
//            if (presenter != null) {
//                mainLay.setVisibility(View.GONE);
//                callGetApi();
//            }

    }

    @OnClick({R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.iv_back, R.id.iv_edit, R.id.lay_btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_try_again:
                if (presenter != null) {
                    callGetApi();
                }
                break;

            case R.id.bt_try_again_time_out:
                if (presenter != null) {
                    callGetApi();
                }
                break;

            case R.id.iv_edit:
                if (tvButton.getText().toString().equalsIgnoreCase(getString(R.string.remove))) {

                    ivEdit.setImageResource(R.drawable.ic_pop_cancel);
                    tvButton.setText(getString(R.string.save));

                    rdUnlimited.setEnabled(true);
                    rdNoOfDays.setEnabled(true);
                    etNumberDays.setEnabled(true);

                } else {
                    ivEdit.setImageResource(R.drawable.ic_edit);
                    tvButton.setText(getString(R.string.remove));

                    rdUnlimited.setEnabled(false);
                    rdNoOfDays.setEnabled(false);
                    etNumberDays.setEnabled(false);
                    if (response != null) {
                        if (response.getGuardianId() != null) {
                            if (response.getNoOfDays() == 0) {
                                rdUnlimited.setChecked(true);
                                etNumberDays.setText("");
                                etNumberDays.setVisibility(View.GONE);
                            } else {
                                rdNoOfDays.setChecked(true);
                                etNumberDays.setVisibility(View.VISIBLE);
                                etNumberDays.setText(response.getNoOfDays() + "");
                            }
                        }
                    }
                }

                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_done:
                if (tvButton.getText().toString().equalsIgnoreCase(getString(R.string.remove))) {
                    if (response != null)
                        removeConfirmation();
                } else if (tvButton.getText().toString().equalsIgnoreCase(getString(R.string.save))) {
                    if (response != null) {
                        if (rdUnlimited.isChecked())
                            callUpdateApi(response.getGuardianId(), 0,1,1);
                        else if (rdNoOfDays.isChecked()) {
                            if (etNumberDays.getText().toString().isEmpty()) {
                                etNumberDays.setError(getString(R.string.required));
                            } else if (Integer.parseInt(etNumberDays.getText().toString().trim()) <= 0) {
                                etNumberDays.setError(getString(R.string.num_day_greater));
                            } else
                                callUpdateApi(response.getGuardianId(), Integer.parseInt(etNumberDays.getText().toString()),1,1);
                        }

                    }
                } else {
                    int active = 1;
                    if (switchActive.isChecked())
                        active = 1;
                    else
                        active = 0;


                    if (etMrn.getText().toString().trim().length() > 0) {
                        if (rdUnlimited.isChecked())
                            ConsentForm2Activity.startActivity(AddGuardianActivity.this, 0, etMrn.getText().toString(), active);
                        else if (rdNoOfDays.isChecked()) {
                            if (etNumberDays.getText().toString().isEmpty()) {
                                etNumberDays.setError(getString(R.string.required));
                            } else if (Integer.parseInt(etNumberDays.getText().toString().trim()) <= 0) {
                                etNumberDays.setError(getString(R.string.num_day_greater));
                            } else
                                ConsentForm2Activity.startActivity(AddGuardianActivity.this, Integer.parseInt(etNumberDays.getText().toString()), etMrn.getText().toString(), active);
                        }
                    } else
                        etMrn.setError(getString(R.string.required));
                }
                break;
        }
    }

    // call update guardian API
    void callUpdateApi(String g_id, int noOfDays,int isActive, int consent) {
        presenter.callUpdateGuardian(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""), g_id, noOfDays,
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0),
                isActive,consent);
    }

    // call get guardian API
    void callGetApi() {
        if (presenter != null) {
            presenter.callGetGuardian(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        AddGuardianActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onGetGuardian(GuardianResponse response) {
        if (response.getGuardianId() != null) {
            this.response = response;
            tvName.setVisibility(View.VISIBLE);
            etName.setVisibility(View.VISIBLE);
            isAdded = false;
            tvToolbarTitle.setText(getString(R.string.guardian_detail));

            etMrn.setText(response.getGuardianId());
            if (SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//                etName.setText(response.getUserDetails().getFirstNameAr() + " " + response.getUserDetails().getMiddleNameAr() + " " + response.getUserDetails().getLastNameAr() + " " + response.getUserDetails().getFamilyNameAr());
                etName.setText(response.getUserDetails().getFirstNameAr() + " " + response.getUserDetails().getLastNameAr());
            } else {
//                etName.setText(response.getUserDetails().getFirstName() + " " + response.getUserDetails().getMiddleName() + " " + response.getUserDetails().getLastName() + " " + response.getUserDetails().getFamilyName());
                etName.setText(response.getUserDetails().getFirstName() + " " + response.getUserDetails().getLastName());
            }
            tvButton.setText(getString(R.string.remove));
            ivEdit.setVisibility(View.VISIBLE);

            if (response.getNoOfDays() == 0) {
                rdUnlimited.setChecked(true);
                etNumberDays.setVisibility(View.GONE);
            } else {
                rdNoOfDays.setChecked(true);
                etNumberDays.setVisibility(View.VISIBLE);
                etNumberDays.setText(response.getNoOfDays() + "");
            }
            if (response.getIsActive() == 1) {
                switchActive.setChecked(true);
                tvActive.setText(getString(R.string.active));

            } else {
                switchActive.setChecked(false);
                tvActive.setText(getString(R.string.inactive));

            }
            etName.setEnabled(false);
            etMrn.setEnabled(false);
            rdUnlimited.setEnabled(false);
            rdNoOfDays.setEnabled(false);
            etNumberDays.setEnabled(false);


        }
        mainLay.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);

    }

    @Override
    public void onAddGuardian(SimpleResponse response) {
        onFail(response.getMessage());
    }

    @Override
    public void onRemove(SimpleResponse response) {
        this.response = null;
        onFail(response.getMessage());
        onBackPressed();
//        tvName.setVisibility(View.GONE);
//        etName.setVisibility(View.GONE);
//        etName.setText("");
//
//        etMrn.setText("");
//        etNumberDays.setText("");
//        rdUnlimited.setChecked(true);
//        etNumberDays.setVisibility(View.GONE);
//
//        etName.setEnabled(true);
//        etMrn.setEnabled(true);
//        rdUnlimited.setEnabled(true);
//        rdNoOfDays.setEnabled(true);
//        etNumberDays.setEnabled(true);
//        isAdded = false;
//        tvToolbarTitle.setText(getString(R.string.add_guardian));
//        tvButton.setText(getString(R.string.done));
    }

    @Override
    public void onLinkUnlink(SimpleResponse response, int active) {

        if (active > 0) {
            tvActive.setText(getString(R.string.active));
        } else {
            tvActive.setText(getString(R.string.inactive));
        }
        onFail(response.getMessage());

    }

    @Override
    public void onUpdateGuardian(GuardianResponse response1) {
        if (response.getGuardianId() != null) {
            response.setNoOfDays(response1.getNoOfDays());
            response.setIsActive(response1.getIsActive());
        }

        etName.setEnabled(false);
        etMrn.setEnabled(false);
        rdUnlimited.setEnabled(false);
        rdNoOfDays.setEnabled(false);
        etNumberDays.setEnabled(false);

        ivEdit.setImageResource(R.drawable.ic_edit);
        tvButton.setText(getString(R.string.remove));
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
        if (msg != null) {
            if (msg.equalsIgnoreCase("timeout")) {
                mainLay.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
            } else
                makeToast(msg);

        }
    }

    @Override
    public void onNoInternet() {
        mainLay.setVisibility(View.GONE);
        mainNoNet.setVisibility(View.VISIBLE);
        mainTimeOut.setVisibility(View.GONE);
    }

    // Remove Guardian dialog.
    private void removeConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGuardianActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.remove_gurdian_content));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.callRemoveGuardian(SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_USER_ID, ""),
                        response.getGuardianId(),
                        SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
                dialog.dismiss();
            }
        });


        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                    Typeface typeface = ResourcesCompat.getFont(AddGuardianActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    // Edit Guardian dialog.
    private void editAlertConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGuardianActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.edit_guardian_content));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ivEdit.setImageResource(R.drawable.ic_pop_cancel);
                tvButton.setText(getString(R.string.save));

                rdUnlimited.setEnabled(true);
                rdNoOfDays.setEnabled(true);
                etNumberDays.setEnabled(true);

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
                    Typeface typeface = ResourcesCompat.getFont(AddGuardianActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (SharedPreferencesUtils.getInstance(AddGuardianActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

}
