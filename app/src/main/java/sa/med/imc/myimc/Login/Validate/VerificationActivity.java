///*code for debug*/
package sa.med.imc.myimc.Login.Validate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Base.MCPatient;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.view.SignUpStep2Activity;
import sa.med.imc.myimc.UrlOpen.ConsentActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.jinesh.captchaimageviewlib.CaptchaImageView;

/*
 * Verification screen to authenticate old users.
 * User will receive a OTP on mobile number and have to input only after that user can login to account.
 * If OTP not received then user can use Resend option to get OTP again.
 */

public class VerificationActivity extends BaseActivity implements ValidateViews, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.opt1)
    EditText opt1;
    @BindView(R.id.opt2)
    EditText opt2;
    @BindView(R.id.opt3)
    EditText opt3;
    @BindView(R.id.opt4)
    EditText opt4;
    @BindView(R.id.tv_resend_otp)
    TextView tvResendOtp;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.lay_btn_login)
    LinearLayout layBtnLogin;

    ValidatePresenter presenter;
    @BindView(R.id.label_des)
    TextView labelDes;
    @BindView(R.id.label_phone)
    TextView label_phone;
    String otp = "";
    @BindView(R.id.text_capt)
    TextView textCapt;
    @BindView(R.id.et_input_captcha)
    EditText etInputCaptcha;
    @BindView(R.id.captcha_image)
    CaptchaImageView captchaImage;
    @BindView(R.id.iv_regenerate)
    ImageView ivRegenerate;
    @BindView(R.id.capt_lay)
    LinearLayout captLay;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    int count_wrong_otp = 0;

    int max_attempts = 0;
    @BindView(R.id.pinview)
    Pinview pinview;
    boolean isLoading = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                if (pinview != null)
                    pinview.setValue(message);
                //Do whatever you want with the code here
            }
        }
    };

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity, String data) {
        Intent intent = new Intent(activity, VerificationActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    private void receiveArguments() {
        /*
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            iv_logo.setVisibility(View.INVISIBLE);
        }*/

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        String hospitalLogo_white = sp.getString("hospitalLogo_white",null);

        Log.e("hospitalLogo_white",hospitalLogo_white);
        Picasso.get().load(hospitalLogo_white)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).resize(1000, 500)
                .centerInside().into(iv_logo);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        VerificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);

        GoogleApiClient mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        pinview.setTextColor(Color.WHITE);
        pinview.setCursorColor(Color.WHITE);
        pinview.setTextSize(15);
        startSMS();
        count_wrong_otp = 0;
        startTimer();

       String mob = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PHONE, "");
//
//
//
//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//           // mob.reverse();
//            int index=mob.lastIndexOf("*");
//            String mobile = mob.substring(index+1);
//            String stars = mob.substring(0,mob.lastIndexOf("*"));
//            mob = stars+mobile;
//        }
        label_phone.setText(mob);
        labelDes.setText(getString(R.string.otp_sent));

        presenter = new ValidateImpl(this, this);
        captchaImage.setCaptchaLength(4);
        captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS);
//        hideKeyboard();
        // setUpWatcher();
        if (BuildConfig.DEBUG) {
            if (getIntent().hasExtra("otp")) {
                showOtp(getIntent().getStringExtra("otp"));
            }
        }

        if (getIntent().hasExtra("otp_c")) {
            if (getIntent().getStringExtra("otp_c").length() > 0)
                max_attempts = Integer.parseInt(getIntent().getStringExtra("otp_c"));
//            if (max_attempts >= 4) {
//                captLay.setVisibility(View.VISIBLE);
//                textCapt.setVisibility(View.VISIBLE);
//            }
        }
//        pin_view.requestPinEntryFocus();

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                hideKeyboard(VerificationActivity.this);
                validationPin();
            }
        });
        hideKeyboard(VerificationActivity.this);
        pinview.requestPinEntryFocus();

        receiveArguments();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        finish();
        VerificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }


    @OnClick({R.id.tv_resend_otp, R.id.iv_back, R.id.lay_btn_login, R.id.iv_regenerate})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_regenerate:
                captchaImage.regenerate();
                break;

            case R.id.tv_resend_otp:
                if (pinview.getValue().length() > 0) {
                    pinview.clearValue();
                }
                if (getIntent().getStringExtra("type").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                    presenter.callGuestResendOtpApi(getIntent().getStringExtra("mob"));
                } else if (getIntent().hasExtra("new")) {


                    presenter.callRegisterResendOtpApi(getIntent().getStringExtra("mob"), getIntent().getStringExtra("iqama"),
                            getIntent().getStringExtra("type_id"),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                } else
                    presenter.callResendOtpApi(getIntent().getStringExtra("type"), getIntent().getStringExtra("mrn"), "USER",
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                break;

            case R.id.lay_btn_login:
                validationPin();
                break;
        }
    }

    // validate oin and call API
    void validationPin() {
        if (pinview.getValue().length() == 4) {
            if (getIntent().hasExtra("otp")) {
                otp = pinview.getValue();//opt1.getText().toString().trim() + opt2.getText().toString().trim() + opt3.getText().toString().trim() + opt4.getText().toString().trim();

                if (count_wrong_otp >= 3) {
                    if (captchaImage.getCaptchaCode().equalsIgnoreCase(etInputCaptcha.getText().toString())) {
                        callAPI();
                    } else if (etInputCaptcha.getText().toString().length() == 0) {
                        etInputCaptcha.setError(getString(R.string.required));
                    } else {
                        makeToast(getString(R.string.wrong_captcha));
                    }
                } else
                    callAPI();
            }
        } else {
            onFail(getString(R.string.otp_invalid));
        }
    }

    // call API to verify user
    void callAPI() {
        if (!isLoading) {
            if (getIntent().getStringExtra("type").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                presenter.callGuestValidateApi(getIntent().getStringExtra("mob"), otp);
            } else if (getIntent().hasExtra("new")) {
//                registar();

//
//                Intent qi = new Intent(this, SignUpStep2Activity.class);
//                HashMap<String, String> testHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("user");
//
//                for (String s : testHashMap.keySet()) {
//                    qi.putExtra(s, testHashMap.get(s));
//                }
//                startActivity(qi);
//                finish();
                presenter.callRegisterValidateApi(getIntent().getStringExtra("mob"), otp, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
            } else {
                presenter.callValidateApi(otp,
                        SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""),
                        "USER",
                        SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_DEVICE_ID, ""),
                        "1", SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0)
                );
            }
        }
    }



    @Override
    public void onValidateOtp(ValidateResponse response) {
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_ACCESS_TOKEN, response.getAccessToken());

        /*SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER, response.getUserDetails());
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_MAIN, response.getUserDetails());

        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, response.getUserDetails().getLang());
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.USER_TYPE);
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_MRN, response.getMrnumber());
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION, response.getUserDetails().getNotification_staus());
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, response.getNotif_count());*/

        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER, response);
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_MAIN, response.getUserDetails());

        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE,
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LOGIN_LANGUAGE,Constants.ENGLISH));
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_PATIENT_ID, response.getPatientId());
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.USER_TYPE);
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_MRN, response.getMrn());
        try {
            SharedPreferencesUtils.getInstance(this).setValue(Constants.patientInsuranceId, response.getInsuranceList().get(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION, response.getUserDetails().getNotification_staus());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, response.getNotif_count());

//        LocaleHelper.setLocale(this, response.getUserDetails().getLang());
        MCPatient.getInstance().destroy();
        MCPatient.updateCurrentPatient();
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS, 0);


        LocaleHelper.setLocale(this,  SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE,Constants.ENGLISH));

        if (getIntent().getStringExtra("consent").equalsIgnoreCase("0")) {
            ConsentActivity.startActivity(VerificationActivity.this, otp);
        } else {
            Intent i1 = new Intent(this, MainActivity.class);
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);
            finish();
        }
    }

    @Override
    public void onResend(LoginResponse response) {
        startTimer();
        max_attempts = Integer.parseInt(response.getAttempt());
        if (BuildConfig.DEBUG) {
            showOtp(response.getOtpCode());
        }
    }

    @Override
    public void showLoading() {
        isLoading = true;
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
        isLoading = false;
    }

    @Override
    public void onFail(String msg) {
        etInputCaptcha.setText("");
        if (msg != null) {
            if (msg.contains("Invalid OTP") || msg.contains("خطأ في رمز التحقق أو رقم المستخدم")) {
                count_wrong_otp = count_wrong_otp + 1;

                if (pinview.getValue().length() > 0) {
                    pinview.clearValue();
                }
            }
            if (count_wrong_otp >= 3) {
                captLay.setVisibility(View.VISIBLE);
                textCapt.setVisibility(View.VISIBLE);
            }
            makeToast(msg);

        }
    }

    @Override
    public void onNoInternet() {
        Common.noInternet(this);
    }


    // Show dialog if fields are empty
    void showOtp(String otp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("OTP")
                .setMessage(otp)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pinview.requestPinEntryFocus();

                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(VerificationActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        alertDialog.show();
    }

    @Override
    public void onConsentSuccess(SimpleResponse response) {

    }

    @Override
    public void onSuccessGuestValidate(SimpleResponse response) {

        if (getIntent().hasExtra("new")) {
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_ACCESS_TOKEN, response.getAccessToken());

            Intent qi = new Intent(this, SignUpStep2Activity.class);
            HashMap<String, String> testHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("user");

            for (String s : testHashMap.keySet()) {
                qi.putExtra(s, testHashMap.get(s));
            }
            startActivity(qi);
            finish();

        } else {
            String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.LANG, getString(R.string.english_lang));

            if (lang.equalsIgnoreCase(getString(R.string.arabic_lang)))
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            else
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.GUEST_TYPE);
            SharedPreferencesUtils.getInstance(this).setValue(Constants.GUEST.ACCESS_TOKEN, response.getAccessToken());

            LocaleHelper.setLocale(this, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH));

            Intent i = new Intent(getApplicationContext(), GuestHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }


    void startSMS() {
        IncomingSms mSmsBroadcastReceiver = new IncomingSms();
        //set google api client for hint request
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(aVoid -> {
            // makeToast("suscees SMS");
        });

        task.addOnFailureListener(e -> {
            // makeToast("error SMS");

        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void startTimer() {
        tvResendOtp.setEnabled(false);
        tvResendOtp.setTextColor(Color.parseColor("#C0C1C3"));
        tvCount.setVisibility(View.VISIBLE);
        tvCount.setText("60");
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) millisUntilFinished / 1000;
                if (sec < 10)
                    tvCount.setText("0" + sec);
                else
                    tvCount.setText(sec + "");

            }

            @Override
            public void onFinish() {
                tvResendOtp.setTextColor(Color.WHITE);
                tvCount.setVisibility(View.GONE);
                tvCount.setText("00");

//                tvCount.setText(getString(R.string.otp_text, "00:00"));
                tvResendOtp.setEnabled(true);
            }
        }.start();

    }
}
/*for release*/
//
//package sa.med.imc.myimc.Login.Validate;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.appcompat.app.AlertDialog;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.goodiebag.pinview.Pinview;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.phone.SmsRetriever;
//import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.Task;
//
//import java.util.HashMap;
//
//import sa.med.imc.myimc.Base.BaseActivity;
//import sa.med.imc.myimc.BuildConfig;
//import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
//import sa.med.imc.myimc.Login.DataModel.LoginResponse;
//import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
//import sa.med.imc.myimc.MainActivity;
//import sa.med.imc.myimc.Network.Constants;
//import sa.med.imc.myimc.Network.SharedPreferencesUtils;
//import sa.med.imc.myimc.Network.SimpleResponse;
//import sa.med.imc.myimc.R;
//import sa.med.imc.myimc.SignUp.view.SignUpStep2Activity;
//import sa.med.imc.myimc.UrlOpen.ConsentActivity;
//import sa.med.imc.myimc.Utils.Common;
//import sa.med.imc.myimc.Utils.LocaleHelper;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import test.jinesh.captchaimageviewlib.CaptchaImageView;
//
///*
// * Verification screen to authenticate old users.
// * User will receive a OTP on mobile number and have to input only after that user can login to account.
// * If OTP not received then user can use Resend option to get OTP again.
// */
//
//public class VerificationActivity extends BaseActivity implements ValidateViews, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    @BindView(R.id.opt1)
//    EditText opt1;
//    @BindView(R.id.opt2)
//    EditText opt2;
//    @BindView(R.id.opt3)
//    EditText opt3;
//    @BindView(R.id.opt4)
//    EditText opt4;
//    @BindView(R.id.tv_resend_otp)
//    TextView tvResendOtp;
//    @BindView(R.id.tv_count)
//    TextView tvCount;
//    @BindView(R.id.lay_btn_login)
//    LinearLayout layBtnLogin;
//
//    ValidatePresenter presenter;
//    @BindView(R.id.label_des)
//    TextView labelDes;
//    String otp = "";
//    @BindView(R.id.text_capt)
//    TextView textCapt;
//    @BindView(R.id.et_input_captcha)
//    EditText etInputCaptcha;
//    @BindView(R.id.captcha_image)
//    CaptchaImageView captchaImage;
//    @BindView(R.id.iv_regenerate)
//    ImageView ivRegenerate;
//    @BindView(R.id.capt_lay)
//    LinearLayout captLay;
//    int count_wrong_otp = 0;
//
//    int max_attempts = 0;
//    @BindView(R.id.pinview)
//    Pinview pinview;
//    boolean isLoading = false;
//    LoginResponse login_response;
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("otp")) {
//                final String message = intent.getStringExtra("message");
//                if (pinview != null)
//                    pinview.setValue(message);
//                //Do whatever you want with the code here
//            }
//        }
//    };
//
//    @Override
//    protected Context getActivityContext() {
//        return this;
//    }
//
//
//    public static void startActivity(Activity activity, String data) {
//        Intent intent = new Intent(activity, VerificationActivity.class);
//        intent.putExtra("data", data);
//        activity.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        if (SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        } else {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
//
//        VerificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_verification);
//        ButterKnife.bind(this);
//
//        GoogleApiClient mCredentialsApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.CREDENTIALS_API)
//                .build();
//
//        pinview.setTextColor(Color.WHITE);
//        pinview.setCursorColor(Color.WHITE);
//        pinview.setTextSize(15);
//        startSMS();
//        count_wrong_otp = 0;
//        startTimer();
//
//        StringBuffer mob = new StringBuffer(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PHONE, ""));
//
//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//            mob.reverse();
//        }
//
//        labelDes.setText(getString(R.string.otp_sent, mob));
//
//        presenter = new ValidateImpl(this, this);
//        captchaImage.setCaptchaLength(4);
//        captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS);
////        hideKeyboard();
//        // setUpWatcher();
//       // showOtp(login_response.getMessage());
//
////        if (BuildConfig.DEBUG)
////        {
////            if (getIntent().hasExtra("otp")) {
////                showOtp(getIntent().getStringExtra("otp"));
////            }
////        }
////        else {
////            //Toast.makeText()
////           showOtp(login_response.getMessage());
////        }
////String loginrespos=login_response.getMessage();
////        Log.e("login_response","response"+loginrespos+"");
//
//        if (getIntent().hasExtra("otp_c")) {
//            if (getIntent().getStringExtra("otp_c").length() > 0)
//                max_attempts = Integer.parseInt(getIntent().getStringExtra("otp_c"));
////            if (max_attempts >= 4) {
////                captLay.setVisibility(View.VISIBLE);
////                textCapt.setVisibility(View.VISIBLE);
////            }
//        }
////        pin_view.requestPinEntryFocus();
//
//        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
//            @Override
//            public void onDataEntered(Pinview pinview, boolean fromUser) {
//
//                hideKeyboard(VerificationActivity.this);
//                validationPin();
//            }
//        });
//        hideKeyboard(VerificationActivity.this);
//        pinview.requestPinEntryFocus();
//    }
//
//    @Override
//    public void onResume() {
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//        VerificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//    }
//
//
//    @OnClick({R.id.tv_resend_otp, R.id.iv_back, R.id.lay_btn_login, R.id.iv_regenerate})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//
//            case R.id.iv_back:
//                onBackPressed();
//                break;
//
//            case R.id.iv_regenerate:
//                captchaImage.regenerate();
//                break;
//
//            case R.id.tv_resend_otp:
//                if (pinview.getValue().length() > 0) {
//                    pinview.clearValue();
//                }
//                if (getIntent().getStringExtra("type").equalsIgnoreCase(Constants.GUEST_TYPE)) {
//                    presenter.callGuestResendOtpApi(getIntent().getStringExtra("mob"));
//                } else if (getIntent().hasExtra("new")) {
//                    presenter.callRegisterResendOtpApi(getIntent().getStringExtra("mob"), getIntent().getStringExtra("iqama"),
//                            getIntent().getStringExtra("type_id"));
//
//                } else
//                    presenter.callResendOtpApi(getIntent().getStringExtra("type"), getIntent().getStringExtra("mrn"), "USER");
//                break;
//
//            case R.id.lay_btn_login:
//                validationPin();
//                break;
//        }
//    }
//
//    // validate oin and call API
//    void validationPin() {
//        if (pinview.getValue().length() == 4) {
//            if (getIntent().hasExtra("otp")) {
//                otp = pinview.getValue();//opt1.getText().toString().trim() + opt2.getText().toString().trim() + opt3.getText().toString().trim() + opt4.getText().toString().trim();
//
//                if (count_wrong_otp >= 3) {
//                    if (captchaImage.getCaptchaCode().equalsIgnoreCase(etInputCaptcha.getText().toString())) {
//                        callAPI();
//                    } else if (etInputCaptcha.getText().toString().length() == 0) {
//                        etInputCaptcha.setError(getString(R.string.required));
//                    } else {
//                        makeToast(getString(R.string.wrong_captcha));
//                    }
//                } else
//                    callAPI();
//            }
//        } else {
//            onFail(getString(R.string.otp_invalid));
//        }
//    }
//
//    // call API to verify user
//    void callAPI() {
//        if (!isLoading) {
//            if (getIntent().getStringExtra("type").equalsIgnoreCase(Constants.GUEST_TYPE)) {
//                presenter.callGuestValidateApi(getIntent().getStringExtra("mob"), otp);
//            } else if (getIntent().hasExtra("new")) {
//                presenter.callRegisterValidateApi(getIntent().getStringExtra("mob"), otp);
//
//            } else {
//                presenter.callValidateApi(otp,
//                        SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""),
//                        "USER",
//                        SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_DEVICE_ID, ""), "1");
//            }
//        }
//    }
//
//    @Override
//    public void onValidateOtp(ValidateResponse response) {
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_ACCESS_TOKEN, response.getAccessToken());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER, response.getUserDetails());
//       // SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER, response.getUserDetails().getIsEmailVerified());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_MAIN, response.getUserDetails());
//
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, response.getUserDetails().getLang());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.USER_TYPE);
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_MRN, response.getUserDetails().getMrnumber());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION, response.getUserDetails().getNotification_staus());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, response.getNotif_count());
//
//        LocaleHelper.setLocale(this, response.getUserDetails().getLang());
//
//        if (getIntent().getStringExtra("consent").equalsIgnoreCase("0")) {
//            ConsentActivity.startActivity(VerificationActivity.this, otp);
//        } else {
//            Intent i1 = new Intent(this, MainActivity.class);
//            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i1);
//            finish();
//        }
//    }
//
//    @Override
//    public void onResend(LoginResponse response) {
//        startTimer();
//        max_attempts = Integer.parseInt(response.getAttempt());
//        if (BuildConfig.DEBUG)
//        {
//            showOtp(response.getOtpCode());
//        }
//        else {
//            showOtp(response.getMessage());
//        }
//    }
//
//    @Override
//    public void showLoading() {
//        isLoading = true;
//        Common.showDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        Common.hideDialog();
//        isLoading = false;
//    }
//
//    @Override
//    public void onFail(String msg) {
//        etInputCaptcha.setText("");
//        if (msg != null) {
//            if (msg.contains("Invalid OTP") || msg.contains("خطأ في رمز التحقق أو رقم المستخدم")) {
//                count_wrong_otp = count_wrong_otp + 1;
//
//                if (pinview.getValue().length() > 0) {
//                    pinview.clearValue();
//                }
//            }
//            if (count_wrong_otp >= 3) {
//                captLay.setVisibility(View.VISIBLE);
//                textCapt.setVisibility(View.VISIBLE);
//            }
//            makeToast(msg);
//
//        }
//    }
//
//    @Override
//    public void onNoInternet() {
//        Common.noInternet(this);
//    }
//
//
//    // Show dialog if fields are empty
//    void showOtp(String otp) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("OTP")
//                .setMessage(otp)
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        pinview.requestPinEntryFocus();
//
//                    }
//                });
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dlg) {
//                try {
//                    Typeface typeface = ResourcesCompat.getFont(VerificationActivity.this, R.font.font_app);
//                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    if (SharedPreferencesUtils.getInstance(VerificationActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
//                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
//                    } else {
//                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        alertDialog.show();
//    }
//
//    @Override
//    public void onConsentSuccess(SimpleResponse response) {
//
//    }
//
//    @Override
//    public void onSuccessGuestValidate(SimpleResponse response) {
//
//        if (getIntent().hasExtra("new")) {
//            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_ACCESS_TOKEN, response.getAccessToken());
//
//            Intent qi = new Intent(this, SignUpStep2Activity.class);
//            HashMap<String, String> testHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("user");
//
//            for (String s : testHashMap.keySet()) {
//                qi.putExtra(s, testHashMap.get(s));
//            }
//            startActivity(qi);
//            finish();
//
//        } else {
//            String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.LANG, getString(R.string.english_lang));
//
//            if (lang.equalsIgnoreCase(getString(R.string.arabic_lang)))
//                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
//            else
//                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
//
//            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.GUEST_TYPE);
//            SharedPreferencesUtils.getInstance(this).setValue(Constants.GUEST.ACCESS_TOKEN, response.getAccessToken());
//
//            LocaleHelper.setLocale(this, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH));
//
//            Intent i = new Intent(getApplicationContext(), GuestHomeActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//            finish();
//        }
//    }
//
//
//    void startSMS() {
//        IncomingSms mSmsBroadcastReceiver = new IncomingSms();
//        //set google api client for hint request
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
//        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
//
//        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
//        Task<Void> task = client.startSmsRetriever();
//
//        task.addOnSuccessListener(aVoid -> {
//            // makeToast("suscees SMS");
//        });
//
//        task.addOnFailureListener(e -> {
//            // makeToast("error SMS");
//
//        });
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    void startTimer() {
//        tvResendOtp.setEnabled(false);
//        tvResendOtp.setTextColor(Color.parseColor("#C0C1C3"));
//        tvCount.setVisibility(View.VISIBLE);
//        tvCount.setText("60");
//        new CountDownTimer(60000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                int sec = (int) millisUntilFinished / 1000;
//                if (sec < 10)
//                    tvCount.setText("0" + sec);
//                else
//                    tvCount.setText(sec + "");
//
//            }
//
//            @Override
//            public void onFinish() {
//                tvResendOtp.setTextColor(Color.WHITE);
//                tvCount.setVisibility(View.GONE);
//                tvCount.setText("00");
////                tvCount.setText(getString(R.string.otp_text, "00:00"));
//                tvResendOtp.setEnabled(true);
//            }
//        }.start();
//
//    }
//}
//
//
