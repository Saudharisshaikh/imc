package sa.med.imc.myimc.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import sa.med.imc.myimc.Adapter.MrnsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.VerificationActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RequestAppointment.view.RequestAppointmentActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;
import sa.med.imc.myimc.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.provider.Settings.Secure;

/*
 * Login screen for old users have records in IMC.
 * User can login by MRN or Any Id proof specified in list.
 * User will get OTP on registered mobile number.
 * User can switch language english to arabic, arabic to english.
 * user can also get appointment as guest user, but in that case user's info not recorded.
 */

public class LoginActivity extends BaseActivity implements LoginViews {

    @BindView(R.id.et_mrn_number)
    EditText etMrnNumber;
    @BindView(R.id.et_login_id)
    EditText etLoginId;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_login_guest)
    TextView tvLoginGuest;

    LoginPresenter presenter;
    @BindView(R.id.label_signin)
    TextView labelSignin;
    @BindView(R.id.label_des)
    TextView labelDes;
    @BindView(R.id.lay_mrn)
    LinearLayout layMrn;
    @BindView(R.id.lay_buttons)
    LinearLayout layButtons;

    String value = "", type = "";
    @BindView(R.id.rv_mrn)
    RecyclerView rvMrn;
    @BindView(R.id.rl_mrns)
    RelativeLayout rlMrns;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    MrnsAdapter adapter;
    List<LoginResponse.UsersList> list = new ArrayList<>();

    private BottomSheetBehavior behaviorBtmsheet;


    private String androidId;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(LoginActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



        super.onCreate(savedInstanceState);

        androidId = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.IS_BEFORE_LOGIN,false);

        SharedPreferencesUtils.getInstance(this).clearDB(this);

        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_REMEMBER, false)) {
            checkbox.setChecked(true);
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_REMEMBER_TYPE, "").equalsIgnoreCase(Constants.LOGIN_WITH_MRN)) {
                etMrnNumber.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_REMEMBER_MRN, ""));
            } else {
                etLoginId.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_REMEMBER_MRN, ""));
            }
        }

        presenter = new LoginImpl(this, this);
        initBottomSheet();


        etMrnNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    etLoginId.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etLoginId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    etMrnNumber.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etLoginId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                callAPIWithValidation();
                return true;
            }
            return false;
        });
        etMrnNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                callAPIWithValidation();
                return true;
            }
            return false;
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_REMEMBER, true);
                        saveValue();
                    } else {
                        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_REMEMBER, false);
                        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_REMEMBER_MRN, "");
                        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_REMEMBER_TYPE, "");
                    }
                }
            }
        });

        getToken();
        receiveArguments();
    }

    private void receiveArguments() {
        /*
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            iv_logo.setVisibility(View.INVISIBLE);
        }*/
        iv_logo = findViewById(R.id.iv_logo);
        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        String hospitalLogo_white = sp.getString("hospitalLogo_white",null);

        Log.e("hospitalLogo_white",hospitalLogo_white);
        Picasso.get().load(hospitalLogo_white)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).resize(1000, 500)
                .centerInside().into(iv_logo);
    }

    // save value in cookies on remember check
    void saveValue() {
        if (etMrnNumber.getText().toString().trim().length() == 0) {
            if (etLoginId.getText().toString().trim().length() != 0) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_MRN, etLoginId.getText().toString().trim());
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_TYPE, Constants.LOGIN_WITH_ID);
            }
        } else if (etLoginId.getText().toString().trim().length() == 0) {
            if (etMrnNumber.getText().toString().trim().length() != 0) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_MRN, etMrnNumber.getText().toString().trim());
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_TYPE, Constants.LOGIN_WITH_MRN);

            }
        } else {
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_MRN, etMrnNumber.getText().toString().trim());
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_TYPE, Constants.LOGIN_WITH_MRN);
        }
    }

    // create token for firebase notification
    void getToken() {
        String newToken = FirebaseInstanceId.getInstance().getToken();
        logger("Token:  ", newToken + "   ---");


        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_DEVICE_ID, androidId);
        Log.e("androidId",androidId);

//        SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_DEVICE_ID, newToken);

//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//                SharedPreferencesUtils.getInstance(LoginActivity.this).setValue(Constants.KEY_DEVICE_ID, newToken);
//            }
//        });
    }

    @OnClick({R.id.tv_login, R.id.iv_back, R.id.tv_login_guest})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_login:
                callAPIWithValidation();
                break;

            case R.id.tv_login_guest:
                Intent i = new Intent(getApplicationContext(), RequestAppointmentActivity.class);
                startActivity(i);

//                Intent i = new Intent(getApplicationContext(), GuestHomeActivity.class);
//                String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
//                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_LANGUAGE, lang);
//                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_TYPE, Constants.GUEST_TYPE);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                break;
        }
    }

    // call API with validations
    void callAPIWithValidation() {
        if (etMrnNumber.getText().toString().trim().length() == 0) {
            if (etLoginId.getText().toString().trim().length() == 0) {
                emptyFieldDialog();
            } else {
                value = etLoginId.getText().toString().trim();
                type = Constants.LOGIN_WITH_ID;
                tvLogin.setEnabled(false);
                presenter.callLoginApi(Constants.LOGIN_WITH_ID, etLoginId.getText().toString().trim(),
                        Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0))
                ;

            }
        } else if (etLoginId.getText().toString().trim().length() == 0) {
            if (etMrnNumber.getText().toString().trim().length() == 0) {
                emptyFieldDialog();
            } else {
                value = etMrnNumber.getText().toString().trim();
                type = Constants.LOGIN_WITH_MRN;
                tvLogin.setEnabled(false);
                presenter.callLoginApi(Constants.LOGIN_WITH_MRN, etMrnNumber.getText().toString().trim(),
                        Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        } else {
            value = etMrnNumber.getText().toString().trim();
            type = Constants.LOGIN_WITH_MRN;
            tvLogin.setEnabled(false);
            presenter.callLoginApi(Constants.LOGIN_WITH_MRN, etMrnNumber.getText().toString().trim(),
                    Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
        finish();
        LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    LoginResponse response;

    @Override
    public void onSuccessLogin(LoginResponse response) {
        // If multiple MRN connected to one ID then show list
        this.response = response;
//        showHospitalDialog();
        onLogin();
    }

    private void onLogin() {
//        if (response.getIsmultiple().equalsIgnoreCase("1")) {
//            if (response.getUsersList() != null) {
//                list.clear();
//                list.addAll(response.getUsersList());
//                adapter.notifyDataSetChanged();
//
//                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//            }
//
//        }
        // If one MRN then verifiy user
//        else {
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_USER_ID, response.getUserid());
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_PHONE, response.getMobilenum());

            if (checkbox.isChecked()) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER, true);
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_MRN, value);
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_TYPE, type);
            } else {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER, false);
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_MRN, "");
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_REMEMBER_TYPE, "");
            }
//            Log.e("otp ", "" + response.getOtpCode());
            Intent qi = new Intent(this, VerificationActivity.class);
            qi.putExtra("otp", response.getOtpCode());
            qi.putExtra("otp_c", response.getAttempt());
            qi.putExtra("mrn", value);
            qi.putExtra("consent", response.getConsent());
            qi.putExtra("type", type);
            startActivity(qi);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (tvLogin != null)
//            tvLogin.setEnabled(true);

    }

    @Override
    public void showLoading() {
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
        tvLogin.setEnabled(true);

    }

    @Override
    public void onFail(String msg) {
        makeToast(msg);
        tvLogin.setEnabled(true);
    }

    @Override
    public void onNoInternet() {
        Common.noInternet(this);
        tvLogin.setEnabled(true);
    }

    // Show dialog if fields are empty
    void emptyFieldDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.empty_fields))
                .setMessage(getString(R.string.mrn_empty_error))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvLogin.setEnabled(true);
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(LoginActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(LoginActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    // Bottom sheet to show multiple MRN list
    private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.rl_mrns);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);

        adapter = new MrnsAdapter(this, list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false);
        rvMrn.setLayoutManager(mLayoutManager);
        rvMrn.setAdapter(adapter);

        rvMrn.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                value = list.get(position).getId();
                type = Constants.LOGIN_WITH_MRN;
                presenter.callLoginApi(Constants.LOGIN_WITH_MRN, value, Constants.USER_TYPE,
                        SharedPreferencesUtils.getInstance(LoginActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

            }
        }));

        findViewById(R.id.iv_close).setOnClickListener(vv -> {
            tvLogin.setEnabled(true);
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        behaviorBtmsheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
    }

    private void showHospitalDialog() {

        View layout = LayoutInflater.from(this).inflate(R.layout.dialog_login_hospitals, null);
        Spinner hospitalsSpinner = (Spinner) layout.findViewById(R.id.hospitalsSpinner);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar
                )
        );
        builder.setView(layout);
        builder.setCancelable(false);

        builder.setNegativeButton("Cancel",
                (dialog, whichButton) -> dialog.dismiss()
        );

        AlertDialog alert = builder.create();
        alert.show();

        hospitalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (position != 0) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        alert.dismiss();
                    }, 500);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}
