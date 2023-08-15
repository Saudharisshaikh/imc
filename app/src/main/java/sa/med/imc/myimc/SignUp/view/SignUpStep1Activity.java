package sa.med.imc.myimc.SignUp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Adapter.SimpleSearchRecyclerViewAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.VerificationActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.IDNameModel;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.model.CityResponse;
import sa.med.imc.myimc.SignUp.model.CompaniesResponse;
import sa.med.imc.myimc.SignUp.model.DistrictResponse;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.model.UserDetailModel;
import sa.med.imc.myimc.SignUp.presenter.SignUpImpl;
import sa.med.imc.myimc.SignUp.presenter.SignUpPresenter;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.TextDrawable;
import sa.med.imc.myimc.WelcomeActivity;
import sa.med.imc.myimc.databinding.ActivitySignUpStepIOldBinding;
import test.jinesh.captchaimageviewlib.CaptchaImageView;

import static sa.med.imc.myimc.Utils.LocaleHelper.onAttach;

public class SignUpStep1Activity extends BaseActivity implements SignUpViews {

    @BindView(R.id.lay_btn_next)
    LinearLayout layBtnNext;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_first_name_ar)
    EditText etFirstNameAr;
    @BindView(R.id.et_father_name)
    EditText etFatherName;
    @BindView(R.id.et_father_name_ar)
    EditText etFatherNameAr;
    @BindView(R.id.et_family_name)
    EditText etFamilyName;
    @BindView(R.id.et_family_name_ar)
    EditText etFamilyNameAr;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_national_id_names)
    EditText etNationalIdNames;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_lang)
    TextView tvLang;
    @BindView(R.id.tv_dob)
    TextView tvDob;
    @BindView(R.id.tv_id_exp)
    TextView tv_id_exp;
    @BindView(R.id.et_reason_visit)
    EditText etReasonVisit;

    String code = "(+966)";
    NationalityResponse nationalityResponse;
    CityResponse cityResponse;
    DistrictResponse districtResponse;

    SignUpPresenter presenter;
    HashMap<String, String> testHashMap = new HashMap<String, String>();
    @BindView(R.id.et_home_phone_number)
    EditText etHomePhoneNumber;
    @BindView(R.id.et_input_captcha)
    EditText etInputCaptcha;
    @BindView(R.id.captcha_image)
    CaptchaImageView captchaImage;
    @BindView(R.id.iv_regenerate)
    ImageView ivRegenerate;
    @BindView(R.id.tv_select_id)

    TextView tvSelectID;
    String type_id = "", country_id = "", city_id = "", distt_id = "", nationality_id = "", marital_status = "";
    String country_nation = "";

    String regex = "^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$";
    String regexHome = "^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$";
    @BindView(R.id.et_grandfather_name)
    EditText etGrandfatherName;
    @BindView(R.id.et_grandfather_name_ar)
    EditText etGrandfatherNameAr;
    @BindView(R.id.tv_district)
    TextView tvDistrict;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_nationality)
    TextView tvNationality;
    @BindView(R.id.et_first_name_emergency)
    EditText etFirstNameEmergency;
    @BindView(R.id.et_family_name_emergency)
    EditText etFamilyNameEmergency;
    @BindView(R.id.et_emergency_phone_number)
    EditText etEmergencyPhoneNumber;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.lay_mrn)
    LinearLayout layMrn;
    @BindView(R.id.tv_marital_status)
    TextView tvMaritalStatus;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    private ActivitySignUpStepIOldBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(SignUpStep1Activity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        SignUpStep1Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up_step_i_old);
        ButterKnife.bind(this);

        presenter = new SignUpImpl(this, this);
        captchaImage.setCaptchaLength(4);
        captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS);

        setTextWatcher();

        tvDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id)) || tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id))) {
//                    if (!tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select)))
//                        if (etNationalIdNames.getText().toString().trim().length() == 10)
//                            presenter.callGetInformationApi(SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    etNationalIdNames.getText().toString(),
//                                    Common.changeFormatDob(tvDob.getText().toString()));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        receiveArguments();

        if (SharedPreferencesUtils.getInstance(SignUpStep1Activity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            binding.llfirstName.setVisibility(View.GONE);
            binding.llfamilyName.setVisibility(View.GONE);
            binding.llfatherName.setVisibility(View.GONE);
        } else {
            binding.llfirstNameAr.setVisibility(View.GONE);
            binding.llfamilyNameAr.setVisibility(View.GONE);
            binding.llfatherNameAr.setVisibility(View.GONE);
        }
    }
    private void receiveArguments() {
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            iv_logo.setVisibility(View.INVISIBLE);
        }
    }


    // set text change listener to remove error messages
    void setTextWatcher() {
        etFirstName.addTextChangedListener(textWatcher);
        etFatherName.addTextChangedListener(textWatcher);
        etFamilyName.addTextChangedListener(textWatcher);
        etGrandfatherName.addTextChangedListener(textWatcher);

        etFirstNameAr.addTextChangedListener(textWatcher);
        etFatherNameAr.addTextChangedListener(textWatcher);
        etFamilyNameAr.addTextChangedListener(textWatcher);
        etGrandfatherNameAr.addTextChangedListener(textWatcher);

        etPhoneNumber.addTextChangedListener(textWatcher);

        etNationalIdNames.addTextChangedListener(nationalTextWatcher);
        tvGender.addTextChangedListener(textWatcher);

        tvLang.addTextChangedListener(textWatcher);
        tvDob.addTextChangedListener(textWatcher);
        etReasonVisit.addTextChangedListener(textWatcher);
        etHomePhoneNumber.addTextChangedListener(textWatcher);

        etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        etPhoneNumber.setCompoundDrawablePadding(code.length() * 20);

        etHomePhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        etHomePhoneNumber.setCompoundDrawablePadding(code.length() * 20);

        etEmergencyPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        etEmergencyPhoneNumber.setCompoundDrawablePadding(code.length() * 20);

    }

    boolean isValidWithCaptcha() {
        boolean valid = true;
        if (isValid()) {
            if (etInputCaptcha.getText().toString().length() == 0) {
                etInputCaptcha.setError(getString(R.string.required));
                valid = false;
            } else if (!captchaImage.getCaptchaCode().equalsIgnoreCase(etInputCaptcha.getText().toString())) {
                makeToast(getString(R.string.wrong_captcha));
                captchaImage.regenerate();
                etInputCaptcha.setText("");
                valid = false;
            }
        } else
            valid = false;
        if (!valid)
            layBtnNext.setEnabled(false);

        return valid;
    }

    // Add validations to all field values.
    boolean isValid() {
        boolean valid = true;
        if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.passport_id)))
            type_id = "P";
        else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id)))
            type_id = "I";
        else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id)))
            type_id = "B";
        else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.birth_notification)))
            type_id = "BR";
        else
            type_id = "";

        if (tvMaritalStatus.getText().toString().equalsIgnoreCase(getString(R.string.single)))
            marital_status = "S";
        else if (tvMaritalStatus.getText().toString().equalsIgnoreCase(getString(R.string.married)))
            marital_status = "M";
        else if (tvMaritalStatus.getText().toString().equalsIgnoreCase(getString(R.string.other_status)))
            marital_status = "U";
        else
            marital_status = "";


        if (etFirstName.getText().toString().trim().length() == 0 && etFirstNameAr.getText().toString().trim().length() == 0) {
            etFirstName.setError(getString(R.string.required));
            etFirstNameAr.setError(getString(R.string.required));
            valid = false;
        }

        if (etFatherName.getText().toString().trim().length() == 0 && etFatherNameAr.getText().toString().trim().length() == 0) {
            etFatherName.setError(getString(R.string.required));
            etFatherNameAr.setError(getString(R.string.required));
            valid = false;
        }

//        if (etGrandfatherName.getText().toString().trim().length() == 0 && etGrandfatherNameAr.getText().toString().trim().length() == 0) {
//            etGrandfatherName.setError(getString(R.string.required));
//            etGrandfatherNameAr.setError(getString(R.string.required));
//            valid = false;
//        }

        if (etFamilyName.getText().toString().trim().length() == 0 && etFamilyNameAr.getText().toString().trim().length() == 0) {
            etFamilyName.setError(getString(R.string.required));
            etFamilyNameAr.setError(getString(R.string.required));
            valid = false;
        }

//        if (etFirstNameEmergency.getText().toString().trim().length() == 0) {
//            etFirstNameEmergency.setError(getString(R.string.required));
//            valid = false;
//        }

//        if (etFamilyNameEmergency.getText().toString().trim().length() == 0) {
//            etFamilyNameEmergency.setError(getString(R.string.required));
//            valid = false;
//        }

        Pattern pattern = Pattern.compile(regex);
        if (etPhoneNumber.getText().toString().trim().length() == 0) {
            etPhoneNumber.setError(getString(R.string.required));
            valid = false;
        } else if (etPhoneNumber.getText().toString().trim().length() < 9) {
            etPhoneNumber.setError(getString(R.string.invalid));
            valid = false;
        } else if (!etPhoneNumber.getText().toString().matches(pattern.pattern())) {
            etPhoneNumber.setError(getString(R.string.invalid));
            valid = false;
        }


//        if (etEmergencyPhoneNumber.getText().toString().trim().length() == 0) {
//            etEmergencyPhoneNumber.setError(getString(R.string.required));
//            valid = false;
//        } else if (etEmergencyPhoneNumber.getText().toString().trim().length() < 9) {
//            etEmergencyPhoneNumber.setError(getString(R.string.invalid));
//            valid = false;
//        }


//        if (etHomePhoneNumber.getText().toString().trim().length() == 0) {
//            etHomePhoneNumber.setError(getString(R.string.required));
//            valid = false;
//        } else if (etHomePhoneNumber.getText().toString().trim().length() < 9) {
//            etHomePhoneNumber.setError(getString(R.string.invalid));
//            valid = false;
//
//        }

//        if (tvMaritalStatus.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            tvMaritalStatus.setError(getString(R.string.required));
//            valid = false;
//        }

        if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_any_id));
        } else {
            if (etNationalIdNames.getText().toString().trim().length() == 0) {
                etNationalIdNames.setError(getString(R.string.required));
                valid = false;

            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id))
                    && etNationalIdNames.getText().toString().trim().length() != 10) {
                etNationalIdNames.setError(getString(R.string.invalid));
                valid = false;
            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id))
                    && etNationalIdNames.getText().toString().trim().length() != 10) {
                etNationalIdNames.setError(getString(R.string.invalid));
                valid = false;
            }
        }
        if (!tvSelectID.getText().toString().trim().equalsIgnoreCase(getString(R.string.birth_notification)))
        {
            if (tv_id_exp.getText().toString().trim().length() == 0 || tv_id_exp.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
                makeToast(getString(R.string.select_id_expiry_date));
                valid = false;
            }
        }
//        else if (tvDob.getText().toString().trim().length() == 0 || tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_dob));
//            valid = false;
//        }

        else if (tvGender.getText().toString().trim().length() == 0 || tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_gender));
            valid = false;
        }

//        else if (tvDistrict.getText().toString().trim().length() == 0 || tvDistrict.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_distt));
//            valid = false;
//        } else if (tvCity.getText().toString().trim().length() == 0 || tvCity.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_city));
//            valid = false;
//        } else if (tvCountry.getText().toString().trim().length() == 0 || tvCountry.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_country));
//            valid = false;
//        } else if (tvNationality.getText().toString().trim().length() == 0 || tvNationality.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_natioality));
//            valid = false;
//        } else if (tvLang.getText().toString().trim().length() == 0 || tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.language));
//            valid = false;
//        }

        if (!valid) {
            etFirstName.requestFocus();
            captchaImage.regenerate();
            etInputCaptcha.setText("");
            layBtnNext.setEnabled(true);
        }
        return valid;
    }

    // Pop up list to select gender or language
    public void showListPopUp(TextView view, String type, String lastValue) {
        List<String> list = new ArrayList<>();
        if (type.equalsIgnoreCase("g")) {
            list.add(getString(R.string.male));
            list.add(getString(R.string.female));
            //list.add(getString(R.string.unspecified));
        }

        if (type.equalsIgnoreCase("l")) {
            list.add(getString(R.string.arabic_lang));
            list.add(getString(R.string.english_lang));
        }

        if (type.equalsIgnoreCase("id")) {
            list.add(getString(R.string.iqama_id));
            list.add(getString(R.string.national_id));
            list.add(getString(R.string.passport_id));
            list.add(getString(R.string.birth_notification));

        }

        if (type.equalsIgnoreCase("m")) {
            list.add(getString(R.string.single));
            list.add(getString(R.string.married));
            list.add(getString(R.string.other_status));
        }


        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_custom_list, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                view.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);

        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, list, lastValue);
        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_list.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            view.setEnabled(true);
            view.setText(list.get(position));
            popupWindow.dismiss();
            if (type.equalsIgnoreCase("id")) {
                enableIdDobFields(true);
                etNationalIdNames.setVisibility(View.VISIBLE);
                tvDob.setText(getString(R.string.select));
                etNationalIdNames.setText("");
                type_id = "";
                if (list.get(position).equalsIgnoreCase(getString(R.string.iqama_id)) || list.get(position).equalsIgnoreCase(getString(R.string.national_id))) {
                    enableFields(false);
                } else {
                    enableFields(true);
                }
            }
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(onAttach(base, SharedPreferencesUtils.getInstance(base).getValue(Constants.KEY_LANGUAGE, "")));
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            etFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etFatherName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etFamilyName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etGrandfatherName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            etFirstNameAr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etFatherNameAr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etFamilyNameAr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etGrandfatherNameAr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etNationalIdNames.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etReasonVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etHomePhoneNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
            etPhoneNumber.setCompoundDrawablePadding(code.length() * 20);

            etHomePhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
            etHomePhoneNumber.setCompoundDrawablePadding(code.length() * 20);

            etEmergencyPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
            etEmergencyPhoneNumber.setCompoundDrawablePadding(code.length() * 20);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher nationalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            etNationalIdNames.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id)) || tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id))) {
//                if (!tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select)))
//                    if (etNationalIdNames.getText().toString().trim().length() == 10)
//                        presenter.callGetInformationApi(SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                etNationalIdNames.getText().toString(),
//                                Common.changeFormatDob(tvDob.getText().toString()));
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layBtnNext != null)
            layBtnNext.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        showAlert(this);
    }

    public void showAlert(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.abort));
        builder.setIcon(R.drawable.imc_logo);
        builder.setMessage(getString(R.string.abort_mesgae));
        builder.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                int selectedHospital = SharedPreferencesUtils.getInstance(SignUpStep1Activity.this).getValue(Constants.SELECTED_HOSPITAL, 1);
//                SharedPreferencesUtils.getInstance(SignUpStep1Activity.this).clearAllSignUp(SignUpStep1Activity.this);
//                SharedPreferencesUtils.getInstance(SignUpStep1Activity.this).setValue(Constants.SELECTED_HOSPITAL, selectedHospital);

                finish();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        alert.show();
    }

    @OnClick({R.id.tv_marital_status, R.id.tv_id_exp, R.id.tv_district, R.id.tv_city, R.id.tv_country, R.id.tv_nationality, R.id.tv_select_id, R.id.iv_regenerate, R.id.iv_back, R.id.tv_gender, R.id.tv_lang, R.id.tv_dob, R.id.lay_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_id_exp:
                Common.getDateFromPickerCU(tv_id_exp, this);
                break;

            case R.id.tv_district:
                if (districtResponse == null)
                    presenter.callGetDistrictApi();
                else
                    showListPopUp(tvDistrict, tvDistrict.getText().toString(), "distt",
                            nationalityResponse, cityResponse, districtResponse);

                break;

            case R.id.tv_city:
                if (cityResponse == null)
                    presenter.callGetCityApi();
                else
                    showListPopUp(tvCity, tvCity.getText().toString(), "city",
                            nationalityResponse, cityResponse, districtResponse);

                break;


            case R.id.tv_country:
                country_nation = "c";
                if (nationalityResponse == null)
                    presenter.callGetNationalityApi();
                else
                    showListPopUp(tvCountry, tvCountry.getText().toString(), "country",
                            nationalityResponse, cityResponse, districtResponse);

                break;

            case R.id.tv_nationality:
                country_nation = "n";
                if (nationalityResponse == null)
                    presenter.callGetNationalityApi();
                else
                    showListPopUp(tvNationality, tvNationality.getText().toString(), "nation",
                            nationalityResponse, cityResponse, districtResponse);

                break;

            case R.id.tv_select_id:
                showListPopUp(tvSelectID, "id", tvSelectID.getText().toString());
                break;

            case R.id.iv_regenerate:
                captchaImage.regenerate();
                etInputCaptcha.setText("");
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_dob:
                Common.setBDDate(tvDob, SignUpStep1Activity.this);
                break;

            case R.id.tv_lang:
                showListPopUp(tvLang, "l", tvLang.getText().toString());
                break;

            case R.id.tv_gender:
                showListPopUp(tvGender, "g", tvGender.getText().toString());
                break;

            case R.id.tv_marital_status:
                showListPopUp(tvMaritalStatus, "m", tvMaritalStatus.getText().toString());
                break;

            case R.id.lay_btn_next:
                layBtnNext.setEnabled(false);

                etFirstName.clearFocus();
                //If values are valid or not
                if (isValidWithCaptcha()) {
                    saveDataToLocal();
                } else
                    layBtnNext.setEnabled(true);

                break;
        }
    }

    // save values in local for use in book appointment as guest
    void saveDataToLocal() {
        //create test hashmap
        testHashMap.put(Constants.GUEST.FIRST_NAME, etFirstName.getText().toString());
        testHashMap.put(Constants.GUEST.FAMILY_NAME, etFamilyName.getText().toString());
        testHashMap.put(Constants.GUEST.FATHER_NAME, etFatherName.getText().toString());
        testHashMap.put(Constants.GUEST.GRAND_FATHER_NAME, etGrandfatherName.getText().toString());


        if (SharedPreferencesUtils.getInstance(SignUpStep1Activity.this)
                .getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            testHashMap.put(Constants.GUEST.FIRST_NAME, etFirstNameAr.getText().toString());
            testHashMap.put(Constants.GUEST.FAMILY_NAME, etFamilyNameAr.getText().toString());
            testHashMap.put(Constants.GUEST.FATHER_NAME, etFatherNameAr.getText().toString());
            testHashMap.put(Constants.GUEST.GRAND_FATHER_NAME, etGrandfatherNameAr.getText().toString());

        }


        if (!tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.GENDER, tvGender.getText().toString());

        if (!tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.LANG, tvLang.getText().toString());
        else
            testHashMap.put(Constants.GUEST.LANG, getString(R.string.english_lang));


        if (!tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.DOB, tvDob.getText().toString());
        else
            testHashMap.put(Constants.GUEST.DOB, "");

        testHashMap.put(Constants.GUEST.IQAMA_ID, etNationalIdNames.getText().toString());
        testHashMap.put(Constants.GUEST.REASON_VISIT, etReasonVisit.getText().toString());
        testHashMap.put(Constants.GUEST.PHONE, etPhoneNumber.getText().toString());
        testHashMap.put(Constants.GUEST.HOME_PHONE, etHomePhoneNumber.getText().toString());

        testHashMap.put(Constants.GUEST.DISTRICT, distt_id);
        testHashMap.put(Constants.GUEST.CITY, city_id);
        testHashMap.put(Constants.GUEST.COUNTRY, country_id);
        testHashMap.put(Constants.GUEST.NATIONALITY, nationality_id);
        testHashMap.put(Constants.GUEST.DOC_EXP_DATE, tv_id_exp.getText().toString());
        testHashMap.put(Constants.GUEST.DOC_TYPE, type_id);
        testHashMap.put(Constants.GUEST.MARTIAL_STATUS, marital_status);

        testHashMap.put(Constants.GUEST.ER_FIRST_NAME, etFirstNameEmergency.getText().toString());
        testHashMap.put(Constants.GUEST.ER_FAMILY_NAME, etFamilyNameEmergency.getText().toString());
        testHashMap.put(Constants.GUEST.ER_PHONE, etEmergencyPhoneNumber.getText().toString());

        presenter.callGetOtpApi(etPhoneNumber.getText().toString(), etNationalIdNames.getText().toString()
                , type_id,
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    @Override
    public void onGetInfo(UserDetailModel response) {
        if (response.getExtPatientInfo() != null) {
            etFirstName.setText(response.getExtPatientInfo().getFirstNameEn());
            etFirstNameAr.setText(response.getExtPatientInfo().getFirstNameAr());

            etFamilyName.setText(response.getExtPatientInfo().getLastNameEn());
            etFamilyNameAr.setText(response.getExtPatientInfo().getLastNameAr());

            etFatherName.setText(response.getExtPatientInfo().getSecondNameEn());
            etFatherNameAr.setText(response.getExtPatientInfo().getSecondNameAr());

            etGrandfatherName.setText(response.getExtPatientInfo().getThirdNameEn());
            etGrandfatherNameAr.setText(response.getExtPatientInfo().getThirdNameAr());

            String gender_s = "";

            if (response.getExtPatientInfo().getGender().equalsIgnoreCase("M")) {
                gender_s = getString(R.string.male);
            } else if (response.getExtPatientInfo().getGender().equalsIgnoreCase("F")) {
                gender_s = getString(R.string.female);
            }
            if (response.getExtPatientInfo().getGender().equalsIgnoreCase("U")) {
                gender_s = getString(R.string.unspecified);
            }
            tvGender.setText(gender_s);
            enableIdDobFields(false);
        } else {
            enableIdDobFields(true);
        }
    }

    @Override
    public void onSignUp(SimpleResponse response) {

    }

    @Override
    public void onSuccessOTP(LoginResponse response) {
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_PHONE, response.getMobilenum());
        Intent qi = new Intent(this, VerificationActivity.class);
        qi.putExtra("otp", response.getOtpCode());
        qi.putExtra("otp_c", response.getAttempt());
        qi.putExtra("type", "register");
        qi.putExtra("mob", etPhoneNumber.getText().toString().trim());
        qi.putExtra("user", testHashMap);
        qi.putExtra("iqama", etNationalIdNames.getText().toString());
        qi.putExtra("new", "dkfjkjdf");
        qi.putExtra("type_id", type_id);

        startActivity(qi);
    }

    @Override
    public void onGetComapanies(CompaniesResponse response) {

    }

    @Override
    public void onGetNationality(NationalityResponse response) {
        nationalityResponse = response;
        if (country_nation.equalsIgnoreCase("c")) {
            showListPopUp(tvCountry, tvCountry.getText().toString(), "country",
                    nationalityResponse, cityResponse, districtResponse);

        } else
            showListPopUp(tvNationality, tvNationality.getText().toString(), "nation", nationalityResponse,
                    cityResponse, districtResponse);
    }

    @Override
    public void onGetCity(CityResponse response) {
        cityResponse = response;
        showListPopUp(tvCity, tvCity.getText().toString(), "city", nationalityResponse, cityResponse, districtResponse);

    }

    @Override
    public void onGetDistrict(DistrictResponse response) {
        districtResponse = response;
        showListPopUp(tvDistrict, tvDistrict.getText().toString(), "distt", nationalityResponse, cityResponse, districtResponse);


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
        layBtnNext.setEnabled(true);

        makeToast(msg);
        captchaImage.regenerate();
        etInputCaptcha.setText("");
    }

    @Override
    public void onNoInternet() {
        layBtnNext.setEnabled(true);

        Common.noInternet(this);
    }

    // Pop up list to select booking id or date or report type
    public void showListPopUp(TextView view, String lastValue, String type, NationalityResponse
            response, CityResponse cityResponse, DistrictResponse districtResponse) {

        List<IDNameModel> list = new ArrayList<>();
        if (type.equalsIgnoreCase("nation") || type.equalsIgnoreCase("country")) {
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                for (int i = 0; i < response.getNationalities().size(); i++) {
                    if (response.getNationalities().get(i).getCountryNameAr() != null)
                        list.add(new IDNameModel(response.getNationalities().get(i).getCode(), response.getNationalities().get(i).getCountryNameAr()));
                    else
                        list.add(new IDNameModel(response.getNationalities().get(i).getCode(), response.getNationalities().get(i).getCountryNameEn()));
                }
            } else {
                for (int i = 0; i < response.getNationalities().size(); i++) {
                    list.add(new IDNameModel(response.getNationalities().get(i).getCode(), response.getNationalities().get(i).getCountryNameEn()));
                }
            }
        }

        if (type.equalsIgnoreCase("city")) {
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                for (int i = 0; i < cityResponse.getCities().size(); i++) {
                    if (cityResponse.getCities().get(i).getCityName() != null)
                        list.add(new IDNameModel(cityResponse.getCities().get(i).getCode(), cityResponse.getCities().get(i).getCityName()));
                    else
                        list.add(new IDNameModel(cityResponse.getCities().get(i).getCode(), cityResponse.getCities().get(i).getCityName()));
                }
            } else {
                for (int i = 0; i < cityResponse.getCities().size(); i++) {
                    list.add(new IDNameModel(cityResponse.getCities().get(i).getCode(), cityResponse.getCities().get(i).getCityName()));
                }
            }
        }


        if (type.equalsIgnoreCase("distt")) {
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                for (int i = 0; i < districtResponse.getDistricts().size(); i++) {
                    if (districtResponse.getDistricts().get(i).getDistrictNameAr() != null)
                        list.add(new IDNameModel(districtResponse.getDistricts().get(i).getCode(),
                                districtResponse.getDistricts().get(i).getDistrictNameAr()));
                    else
                        list.add(new IDNameModel(districtResponse.getDistricts().get(i).getCode(),
                                districtResponse.getDistricts().get(i).getDistrictName()));
                }
            } else {
                for (int i = 0; i < districtResponse.getDistricts().size(); i++) {
                    list.add(new IDNameModel(districtResponse.getDistricts().get(i).getCode(),
                            districtResponse.getDistricts().get(i).getDistrictName()));
                }
            }
        }

        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_insurance_custom_list, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                Common.getScreenWidt(SignUpStep1Activity.this),//view.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT
//              Common.getScreenFullHeight(SignUpStep1Activity.this)
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        popupWindow.update();

        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);
        EditText edSearch = popupView.findViewById(R.id.ed_search);
        TextView tv_ss = popupView.findViewById(R.id.tv_ss);
        if (type.equalsIgnoreCase("nation"))
            tv_ss.setText(getString(R.string.select_natioality));
        if (type.equalsIgnoreCase("country"))
            tv_ss.setText(getString(R.string.select_country));
        if (type.equalsIgnoreCase("city")) {
            tv_ss.setText(getString(R.string.select_city));
        }
        if (type.equalsIgnoreCase("distt")) {
            tv_ss.setText(getString(R.string.select_distt));
        }

        edSearch.setEnabled(true);
        edSearch.setFocusable(true);
        edSearch.requestFocus();

        SimpleSearchRecyclerViewAdapter adapter = new SimpleSearchRecyclerViewAdapter(this, list, lastValue);
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Common.hideSoftKeyboard(SignUpStep1Activity.this);
                return true;
            }
            return false;
        });
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null)
                    adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_list.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            view.setEnabled(true);
            if (type.equalsIgnoreCase("nation")) {
                tvNationality.setText(adapter.getName(position).getName());
                nationality_id = String.valueOf(adapter.getName(position).getId());
            }
            if (type.equalsIgnoreCase("country")) {
                country_id = String.valueOf(adapter.getName(position).getId());
                tvCountry.setText(adapter.getName(position).getName());

            }

            if (type.equalsIgnoreCase("city")) {
                city_id = String.valueOf(adapter.getName(position).getId());
                view.setText(adapter.getName(position).getName());
            }

            if (type.equalsIgnoreCase("distt")) {
                distt_id = String.valueOf(adapter.getName(position).getId());
                view.setText(adapter.getName(position).getName());

            }
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        popupWindow.showAsDropDown(mainToolbar, 2, 2);//5//(int) view.getX()
    }

    void enableFields(boolean value) {
        value = true;
        etFirstName.setEnabled(value);
        etFirstNameAr.setEnabled(value);

        etFamilyName.setEnabled(value);
        etFamilyNameAr.setEnabled(value);

        etFatherName.setEnabled(value);
        etFatherNameAr.setEnabled(value);

        etGrandfatherName.setEnabled(value);
        etGrandfatherNameAr.setEnabled(value);
        tvGender.setEnabled(value);

        if (value) {
            etFirstName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            etFirstNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));

            etFamilyName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            etFamilyNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));

            etFatherName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            etFatherNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));

            etGrandfatherName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            etGrandfatherNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            tvGender.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
        } else {
            etFirstName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            etFirstNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));

            etFamilyName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            etFamilyNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));

            etFatherName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            etFatherNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));

            etGrandfatherName.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            etGrandfatherNameAr.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            tvGender.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
        }
    }

    void enableIdDobFields(boolean value) {
        value = true;
        tvDob.setEnabled(value);
        etNationalIdNames.setEnabled(value);

        if (value) {
            tvDob.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
            etNationalIdNames.setBackgroundColor(getResources().getColor(R.color.color_ed_fill));
        } else {
            tvDob.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
            etNationalIdNames.setBackgroundColor(getResources().getColor(R.color.color_ed_fill_disable));
        }
    }
}
