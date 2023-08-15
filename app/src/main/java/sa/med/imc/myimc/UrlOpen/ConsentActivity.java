package sa.med.imc.myimc.UrlOpen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Login.Validate.ValidateImpl;
import sa.med.imc.myimc.Login.Validate.ValidatePresenter;
import sa.med.imc.myimc.Login.Validate.ValidateViews;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConsentActivity extends BaseActivity implements ValidateViews {


    ValidatePresenter presenter;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.check_accept)
    CheckBox checkAccept;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;


    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity, String otp) {
        Intent intent = new Intent(activity, ConsentActivity.class);
        intent.putExtra("otp", otp);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(ConsentActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ConsentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);
        ButterKnife.bind(this);
        tvContent.setBackgroundColor(Color.TRANSPARENT);
        layBtnDone.setEnabled(false);

        presenter = new ValidateImpl(this, this);
        String outhtml = getString(R.string.consent_data_t);

        if (SharedPreferencesUtils.getInstance(ConsentActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getData("rtl", outhtml);
        } else {
            getData("ltr", outhtml);
        }

        checkAccept.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    layBtnDone.setEnabled(true);

                } else {
                    layBtnDone.setEnabled(false);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        ConsentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onValidateOtp(ValidateResponse response) {
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_ACCESS_TOKEN, response.getAccessToken());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER, response.getUserDetails());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, response.getUserDetails().getLang());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, "user");
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_MRN, response.getUserDetails().getMrnumber());
//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, response.getNotif_count());
//
//        LocaleHelper.setLocale(this, response.getUserDetails().getLang());
//        Intent i1 = new Intent(this, MainActivity.class);
//        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i1);
    }

    @Override
    public void onResend(LoginResponse response) {
        onFail(getString(R.string.otp_resent));
    }

    @Override
    public void onConsentSuccess(SimpleResponse response) {
        LocaleHelper.setLocale(this, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH));

        Intent i1 = new Intent(this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
    }

    @Override
    public void onSuccessGuestValidate(SimpleResponse response) {

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
        Common.noInternet(this);
    }

    @OnClick(R.id.lay_btn_done)
    public void onViewClicked() {

        if (checkAccept.isChecked()) {
            presenter.callUpdateConsentApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""), "1");

        } else {
            onFail(getString(R.string.please_accept_condent));
        }
    }

    // load data with custom font
    void getData(String direction, String text) {
        String text_load = "<html  dir=" + direction + "><head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_res/font/sans_plain.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    text-align: justify;\n" +
                "    padding-right: 10px;\n" +
                "    padding-left: 10px;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>" + text + "</body></html>";

        tvContent.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }
}
