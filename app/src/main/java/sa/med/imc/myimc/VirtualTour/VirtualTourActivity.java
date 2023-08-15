package sa.med.imc.myimc.VirtualTour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.VideoCallActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VirtualTourActivity extends BaseActivity {

    @BindView(R.id.et_country_code)
    EditText etCountryCode;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.lay_btn_submit)
    LinearLayout layBtnSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, VirtualTourActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VirtualTourActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_tour);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        VirtualTourActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @OnClick({R.id.iv_back, R.id.lay_btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_submit:
                VideoCallActivity.startActivity(this);
                break;
        }
    }
}
