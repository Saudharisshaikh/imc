package sa.med.imc.myimc.AddGuardian.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.AddGuardian.model.GuardianResponse;
import sa.med.imc.myimc.AddGuardian.presenter.GuardianImpl;
import sa.med.imc.myimc.AddGuardian.presenter.GuardianPresenter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Utils.Common;

public class ConsentForm2Activity extends BaseActivity implements GuardianViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.check_accept)
    CheckBox checkAccept;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;

    GuardianPresenter presenter;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity, int num_days, String gur_mrn, int active) {
        Intent intent = new Intent(activity, ConsentForm2Activity.class);
        intent.putExtra("num_days", num_days);
        intent.putExtra("gur_mrn", gur_mrn);
        intent.putExtra("active", active);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(ConsentForm2Activity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ConsentForm2Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form2);
        ButterKnife.bind(this);
        layBtnDone.setEnabled(false);

        presenter = new GuardianImpl(this, this);

        tvContent.setBackgroundColor(Color.TRANSPARENT);
        String outhtml = getString(R.string.consent_for_add_guardian);

        if (SharedPreferencesUtils.getInstance(ConsentForm2Activity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        ConsentForm2Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_done:
                if (checkAccept.isChecked()) {
                    presenter.callAddGuardian(
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, ""),
                            getIntent().getStringExtra("gur_mrn"),
                            getIntent().getIntExtra("active", 0),
                            getIntent().getIntExtra("num_days", 0), 1				,
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0)
                    );

                } else {
                    makeToast(getString(R.string.please_accept_condent));
                }
                break;
        }
    }

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
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>" + text + "</body></html>";

        tvContent.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }

    @Override
    public void onGetGuardian(GuardianResponse response) {

    }

    @Override
    public void onAddGuardian(SimpleResponse response) {
        onFail(response.getMessage());
        AddGuardianActivity.isAdded=true;
        finish();
    }

    @Override
    public void onRemove(SimpleResponse response) {

    }

    @Override
    public void onLinkUnlink(SimpleResponse response,int active) {

    }

    @Override
    public void onUpdateGuardian(GuardianResponse response) {

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
}
