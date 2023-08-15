package sa.med.imc.myimc.AddGuardian.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.AddGuardian.model.ConsentForm1Validation;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConsentForm1Activity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.et_en_number)
    EditText etEnNumber;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email_id)
    EditText etEmailId;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.lay_btn_next)
    LinearLayout layBtnNext;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ConsentForm1Activity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConsentForm1Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form1);
        ButterKnife.bind(this);
    }


    @Override
    public void onBackPressed() {
        finish();
        ConsentForm1Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_next:
                ConsentForm1Validation validation = new ConsentForm1Validation(this);

                if (isValidAll())
//                    ConsentForm2Activity.startActivity(ConsentForm1Activity.this);

                break;
        }
    }

    boolean isValidAll() {
        ConsentForm1Validation validation = new ConsentForm1Validation(this);
        boolean enValid = validation.isEnValid(etEnNumber);
        boolean emailValid = validation.isEmailValid(etEmailId);
        boolean enName = validation.isPatientNameValid(etName);
        boolean mobileValid = validation.isPhoneValid(etMobileNumber);

        if (enValid && emailValid && enName && mobileValid) {
            return true;
        } else {
            return false;
        }
    }


}
