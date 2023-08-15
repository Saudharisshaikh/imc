package sa.med.imc.myimc.SignUp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.Login.Validate.VerificationActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNumberActivity extends BaseActivity {

    @BindView(R.id.et_country_code)
    EditText etCountryCode;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.lay_btn_submit)
    LinearLayout layBtnSubmit;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.lay_btn_submit)
    public void onViewClicked() {
//        Intent qi = new Intent(this, SignUpStep1Activity.class);
//        startActivity(qi);
//        VerificationActivity.startActivity(this,"");

        Intent i = new Intent(getApplicationContext(), GuestHomeActivity.class);
        String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, lang);
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.GUEST_TYPE);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
