package sa.med.imc.myimc.Settings;

import static sa.med.imc.myimc.Utils.Common.getFromDate;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.switchNotification)
    Switch switchNotification;
    @BindView(R.id.layNotification)
    RelativeLayout layNotification;
    @BindView(R.id.layEducationalContent)
    RelativeLayout layEducationalContent;
    @BindView(R.id.latContactus)
    RelativeLayout latContactus;
    @BindView(R.id.layTerms)
    RelativeLayout layTerms;
    @BindView(R.id.latPrivacy)
    RelativeLayout latPrivacy;
    @BindView(R.id.latAboutApp)
    RelativeLayout latAboutApp;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    @BindView(R.id.rd_english)
    RadioButton rdEnglish;
    @BindView(R.id.rd_arabic)
    RadioButton rdArabic;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        SettingActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setUpRadioSelection();

        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
            layNotification.setVisibility(View.GONE);
        } else {
            layNotification.setVisibility(View.VISIBLE);

            if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_NOTIFICATION, Constants.NOTIFICATION_ON).equalsIgnoreCase(Constants.NOTIFICATION_ON)) {
                switchNotification.setChecked(true);
            } else {
                switchNotification.setChecked(false);
            }

            switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (buttonView.isPressed()) {
                    if (isNetAvail()) {
                        if (switchNotification.isChecked()) {
                            onOffNotification(SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_MRN, ""), Constants.NOTIFICATION_ON,
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        } else {
                            onOffNotification(SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_MRN, ""), Constants.NOTIFICATION_OFF,
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        }
                    } else {

                        Common.noInternet(SettingActivity.this);
                        if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_NOTIFICATION, Constants.NOTIFICATION_ON).equalsIgnoreCase(Constants.NOTIFICATION_ON)) {
                            switchNotification.setChecked(true);
                        } else {
                            switchNotification.setChecked(false);
                        }
                    }
                }
            });
        }
    }
    // change language ask dialog for confirmation.
    private void askLanguageChange(String lang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setCancelable(false);

        try {
            if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                builder.setTitle("Language!");
                builder.setMessage("Do you want to change language?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        callChangeLanguage(SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_MRN, ""), lang,
                                SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                        ai.medicus.utils.SharedPreferencesUtils.setLocalLanguage(Constants.ENGLISH);

//                        if (MedicusUtilities.getPatient() != null)
//                            MedicusUtilities.getPatient().updateLanguage("en");

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            } else {
                builder.setTitle("اللغة!");
                builder.setMessage("هل ترغب في تغيير اللغة؟");

                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue("toDate", getFromDate(2,0));
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue("fromDate", getFromDate(0,2));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        callChangeLanguage(SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_MRN, ""), lang,
                                SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                        ai.medicus.utils.SharedPreferencesUtils.setLocalLanguage(Constants.ARABIC);

//                        if (MedicusUtilities.getPatient() != null)
//                            MedicusUtilities.getPatient().updateLanguage("ar");
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(SettingActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to LTR
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }


    public static void startSetting(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        SettingActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.latAboutApp_info, R.id.layEducationalContent, R.id.latContactus, R.id.layTerms, R.id.latPrivacy, R.id.latAboutApp, R.id.lay_btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.layEducationalContent:
                makeToast("Coming soon...");
                break;

            case R.id.latContactus:
                ContactOptionsActivity.startActivity(this);
                break;

            case R.id.layTerms:
                Intent intentTwe = new Intent(this, UrlOpenActivity.class);
                intentTwe.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_TERMS);
                startActivity(intentTwe);
                break;

            case R.id.latPrivacy:
                Intent intent = new Intent(this, UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_PRIVACY);
                startActivity(intent);
                break;

            case R.id.latAboutApp:
                Intent intent1 = new Intent(this, UrlOpenActivity.class);
                intent1.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_ABOUT_US);
                startActivity(intent1);
                break;
            case R.id.latAboutApp_info:
                AboutAppActivity.startActivity(SettingActivity.this);
                break;

            case R.id.lay_btn_done:
                break;
        }
    }

    // Set up radio selection
    void setUpRadioSelection() {
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            rdArabic.setChecked(true);
        } else {
            rdEnglish.setChecked(true);
        }

        rdArabic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isNetAvail()) {
                    if (isChecked) {
                        if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                            askLanguageChange(Constants.ARABIC);

                    }
                } else {
                    if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        rdArabic.setChecked(true);
                    } else {
                        rdEnglish.setChecked(true);
                    }
                    Common.noInternet(SettingActivity.this);
                }
            }
        });

        rdEnglish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isNetAvail()) {
                    if (isChecked) {
                        if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                            askLanguageChange(Constants.ENGLISH);

                    }
                } else {
                    if (SharedPreferencesUtils.getInstance(SettingActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        rdArabic.setChecked(true);
                    } else {
                        rdEnglish.setChecked(true);
                    }
                    Common.noInternet(SettingActivity.this);
                }
            }
        });
    }

    // Change language API request
    public void callChangeLanguage(String token, String user_id, String language, int hosp) {
        Common.showDialog(this);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.changeLanguage(user_id, language, hosp);
//
//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//
//
//                        } else {
//                            makeToast(response1.getMessage());
//                        }
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                Common.hideDialog();
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    makeToast(getString(R.string.time_out_messgae));
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    Common.noInternet(MainActivity.this);
//                } else
//                    makeToast(t.getMessage());
//            }
//        });

        changeLanguage(language);

        Intent i1 = new Intent(SettingActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();
    }


    // Call on off notification API
    public void onOffNotification(String token, String mrn, String status, int hosp) {
        Common.showDialog(this);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.onOffNotification(mrn, status, hosp);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            Common.makeToast(SettingActivity.this, response1.getMessage());
                            SharedPreferencesUtils.getInstance(SettingActivity.this).setValue(Constants.KEY_NOTIFICATION, status);
                        } else {
                            makeToast(response1.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    makeToast(getString(R.string.time_out_messgae));
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(SettingActivity.this);
                } else
                    makeToast(t.getMessage());

            }
        });
    }

    //Change language method
    void changeLanguage(String lan) {
        if (lan.equalsIgnoreCase(Constants.ENGLISH)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ARABIC);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
        }

        Intent i1 = new Intent(SettingActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();

//        recreate();
    }

    //Change language method
    void changeLanguageGuest(String lan) {
        if (lan.equalsIgnoreCase(Constants.ENGLISH)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
        }

        Intent i1 = new Intent(SettingActivity.this, GuestHomeActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();

//        recreate();
    }
}
