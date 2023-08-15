package sa.med.imc.myimc.SignUp.activity;

import static sa.med.imc.myimc.Base.BaseActivity.hideKeyboard;
import static sa.med.imc.myimc.Utils.Common.messageDailogForCompleteActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Login.Validate.VerificationActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.model.SelfRegistrationOtpResponse;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.SelfRegistrationListner;

public class SelfRegistrationOtpVerificationActivity extends AppCompatActivity {

    Pinview pinview;
    LinearLayout lay_btn_login;
    String details, mobileNumber;
    SelfRegistrationOtpResponse selfRegistrationOtpResponse;

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_registration_otp_verification);
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        details = getIntent().getStringExtra("details");
        selfRegistrationOtpResponse = new Gson().fromJson(getIntent().getStringExtra("otp"), new TypeToken<SelfRegistrationOtpResponse>() {
        }.getType());
        initView();
        AlertDialog alertDialog = new AlertDialog.Builder(SelfRegistrationOtpVerificationActivity.this).create();
        alertDialog.setTitle("Otp!");
        alertDialog.setMessage(selfRegistrationOtpResponse.otpCode);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (BuildConfig.DEBUG) {
            alertDialog.show();
        }
    }

    private void initView() {
        pinview = findViewById(R.id.pinview);
        lay_btn_login = findViewById(R.id.lay_btn_login);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> finish());

        pinview.setTextColor(Color.WHITE);
        pinview.setCursorColor(Color.WHITE);
        pinview.setTextSize(15);

        lay_btn_login.setOnClickListener(view -> validationPin());
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                hideKeyboard(SelfRegistrationOtpVerificationActivity.this);
                validationPin();
            }
        });
    }

    // validate oin and call API
    void validationPin() {
        if (pinview.getValue().length() == 4) {
            String otp = pinview.getValue();//opt1.getText().toString().trim() + opt2.getText().toString().trim() + opt3.getText().toString().trim() + opt4.getText().toString().trim();

            new MyhttpMethod(SelfRegistrationOtpVerificationActivity.this).selfRegistrationOtpVarification(mobileNumber, otp, new SelfRegistrationListner() {
                @Override
                public void onSucces(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                            register();
                        } else {
                            Toast.makeText(SelfRegistrationOtpVerificationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SelfRegistrationOtpVerificationActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(this, "Please enter Otp", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {

        new MyhttpMethod(SelfRegistrationOtpVerificationActivity.this).selfRegistration(details, new SelfRegistrationListner() {
            @Override
            public void onSucces(String response) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
////                            Toast.makeText(SelfRegistrationActivity.this, jsonObject.getString("status"), Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                messageDailogForCompleteActivity(SelfRegistrationOtpVerificationActivity.this, getString(R.string.self_registration_success));

            }
        });
    }

}