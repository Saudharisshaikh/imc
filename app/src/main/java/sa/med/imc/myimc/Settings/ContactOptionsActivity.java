package sa.med.imc.myimc.Settings;

import static sa.med.imc.myimc.Network.Constants.SELECTED_HOSPITAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

public class ContactOptionsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lay_item_request_call)
    RelativeLayout layItemRequestCall;
    @BindView(R.id.lay_item_call_us_direct)
    RelativeLayout layItemCallUsDirect;
    String phone = "+966920027778";
    @BindView(R.id.tv_des1)
    TextView tvDes1;
    @BindView(R.id.lay_item_request_for_report)
    RelativeLayout layItemRequestForReport;
    @BindView(R.id.card_request_report)
    CardView cardRequestReport;
    boolean isFromLogin=false;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ContactOptionsActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivityFromLogin(Activity activity) {
        Intent intent = new Intent(activity, ContactOptionsActivity.class);
        intent.putExtra("isFromLogin",true);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        ContactOptionsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(ContactOptionsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ContactOptionsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_options);
        ButterKnife.bind(this);
        isFromLogin=getIntent().getBooleanExtra("isFromLogin",false);


//        int hos=SharedPreferencesUtils.getInstance(this).getValue(SELECTED_HOSPITAL,1);
//        if (hos!=1){
//            phone=""
//        }


        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        phone = preferences.getString("ph", "");



        if (SharedPreferencesUtils.getInstance(ContactOptionsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            tvDes1.setText(getString(R.string.call_us_content, phone ));
        } else {
            tvDes1.setText(getString(R.string.call_us_content, phone));
        }
        cardRequestReport.setVisibility(View.GONE);

// need visible here when need report
//        if (BuildConfig.DEBUG)
//            if (SharedPreferencesUtils.getInstance(ContactOptionsActivity.this).getValue(Constants.KEY_USER_ID, "").length() > 0) {
//                if (SharedPreferencesUtils.getInstance(ContactOptionsActivity.this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.USER_TYPE))
//                    cardRequestReport.setVisibility(View.GONE);
//                else
//                    cardRequestReport.setVisibility(View.GONE);
//
//            } else {
//                cardRequestReport.setVisibility(View.GONE);
//            }

    }

    @OnClick({R.id.iv_back, R.id.lay_item_request_call, R.id.lay_item_call_us_direct, R.id.lay_item_request_for_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_item_request_call:
                ContactUsActivity.startActivity(this,isFromLogin);
                break;

            case R.id.lay_item_call_us_direct:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + phone)));
                break;

            case R.id.lay_item_request_for_report:
                ContactUsActivity.startActivity(this, "request");
                break;
        }
    }
}
