package sa.med.imc.myimc.RequestAppointment.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.VerificationActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RequestAppointment.presenter.GuestAppointmentImpl;
import sa.med.imc.myimc.RequestAppointment.presenter.GuestAppointmentPresenter;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.TextDrawable;
import test.jinesh.captchaimageviewlib.CaptchaImageView;


public class RequestAppointmentActivity extends BaseActivity implements GuestAppointmentViews {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_myseld)
    TextView tvMyseld;
    @BindView(R.id.tv_someone_else)
    TextView tvSomeoneElse;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.et_relationship)
    EditText etRelationship;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_father_name)
    EditText etFatherName;
    @BindView(R.id.et_grandfather_name)
    EditText etGrandfatherName;
    @BindView(R.id.et_grandfather_name_ar)
    EditText etGrandfatherNameAr;
    @BindView(R.id.et_family_name)
    EditText etFamilyName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_national_id_names)
    EditText et_national_id_names;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_dob)
    TextView tvDob;
    @BindView(R.id.tv_lang)
    TextView tvLang;
    @BindView(R.id.tv_select_id)
    TextView tvSelectID;
    @BindView(R.id.et_reason_visit)
    EditText etReasonVisit;
    @BindView(R.id.lay_mrn)
    LinearLayout layMrn;
    @BindView(R.id.lay)
    RelativeLayout lay;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;
    @BindView(R.id.et_first_name_ar)
    EditText etFirstNameAr;
    @BindView(R.id.et_father_name_ar)
    EditText etFatherNameAr;
    @BindView(R.id.et_family_name_ar)
    EditText etFamilyNameAr;

    String regex = "^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$";
    String type_id = "";
    GuestAppointmentPresenter presenter;
    String code = "(+966)";

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

            et_national_id_names.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            etReasonVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
            etPhoneNumber.setCompoundDrawablePadding(code.length() * 20);
//            tvGender.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            tvLang.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            tvDob.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//            tvGender.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_droparrow, 0);
//            tvLang.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_droparrow, 0);
//            tvDob.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calender_date, 0);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @BindView(R.id.tv_id_exp)
    TextView tvIdExp;
    @BindView(R.id.et_input_captcha)
    EditText etInputCaptcha;
    @BindView(R.id.captcha_image)
    CaptchaImageView captchaImage;
    @BindView(R.id.iv_regenerate)
    ImageView ivRegenerate;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, RequestAppointmentActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(RequestAppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment);
        ButterKnife.bind(this);
        captchaImage.setCaptchaLength(4);
        captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS);

        setTextWatcher();
        presenter = new GuestAppointmentImpl(this, this);
//        etReasonVisit.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                if (isValid()) {
//                    saveDataToLocal();
//                }
//                return true;
//            }
//            return false;
//        });
    }

    @Override
    public void onBackPressed() {
        finish();
        RequestAppointmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_regenerate, R.id.tv_id_exp, R.id.tv_select_id, R.id.iv_back, R.id.tv_dob, R.id.tv_lang, R.id.tv_gender, R.id.lay_btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_regenerate:
                captchaImage.regenerate();
                etInputCaptcha.setText("");
                break;

            case R.id.tv_id_exp:
                Common.getDateFromPickerCU(tvIdExp, this);
                break;

            case R.id.tv_select_id:
                showListPopUp(tvSelectID, "id", tvSelectID.getText().toString());
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_dob:
                Common.setBDDate(tvDob, RequestAppointmentActivity.this);
                break;

            case R.id.tv_lang:
                showListPopUp(tvLang, "l", tvLang.getText().toString());
                break;

            case R.id.tv_gender:
                showListPopUp(tvGender, "g", tvGender.getText().toString());
                break;

            case R.id.lay_btn_done:
                layBtnDone.setEnabled(false);
                etFirstName.clearFocus();
                //If values are valid or not
                if (isValidWithCaptcha()) {
                    saveDataToLocal();

                }else
                    layBtnDone.setEnabled(true);

                break;
        }
    }

    // save values in local for use in book appointment as guest
    void saveDataToLocal() {
        //create test hashmap
        HashMap<String, String> testHashMap = new HashMap<String, String>();
        testHashMap.put(Constants.GUEST.FIRST_NAME, etFirstName.getText().toString());
        testHashMap.put(Constants.GUEST.FAMILY_NAME, etFamilyName.getText().toString());
        testHashMap.put(Constants.GUEST.FATHER_NAME, etFatherName.getText().toString());

        testHashMap.put(Constants.GUEST.FIRST_NAME_AR, etFirstNameAr.getText().toString());
        testHashMap.put(Constants.GUEST.FAMILY_NAME_AR, etFamilyNameAr.getText().toString());
        testHashMap.put(Constants.GUEST.FATHER_NAME_AR, etFatherNameAr.getText().toString());

        if (!tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.GENDER, tvGender.getText().toString());

        if (!tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.LANG, tvLang.getText().toString());
        else
            testHashMap.put(Constants.GUEST.LANG, getString(R.string.english_lang));


        if (!tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select)))
            testHashMap.put(Constants.GUEST.DOB, tvDob.getText().toString());

        testHashMap.put(Constants.GUEST.IQAMA_ID, et_national_id_names.getText().toString());
        testHashMap.put(Constants.GUEST.REASON_VISIT, etReasonVisit.getText().toString());
        testHashMap.put(Constants.GUEST.PHONE, etPhoneNumber.getText().toString());

        //convert to string using gson

        for (String s : testHashMap.keySet()) {
            SharedPreferencesUtils.getInstance(this).setValue(s, testHashMap.get(s));
        }
        presenter.callGuestLoginApi(etPhoneNumber.getText().toString(),
                et_national_id_names.getText().toString(), type_id,
                SharedPreferencesUtils.getInstance(RequestAppointmentActivity.this).getValue(Constants.SELECTED_HOSPITAL,0));

//        presenter.callRegisterLoginApi(etPhoneNumber.getText().toString(),et_national_id_names.getText().toString(),type_id);
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

        et_national_id_names.addTextChangedListener(textWatcher);
        tvGender.addTextChangedListener(textWatcher);

        tvLang.addTextChangedListener(textWatcher);
        tvDob.addTextChangedListener(textWatcher);
        etReasonVisit.addTextChangedListener(textWatcher);
        etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code), null, null, null);
        etPhoneNumber.setCompoundDrawablePadding(code.length() * 20);

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
        return valid;
    }


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
//
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


        if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_any_id));
        } else {
            if (et_national_id_names.getText().toString().trim().length() == 0) {
                et_national_id_names.setError(getString(R.string.required));
                valid = false;

            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id))
                    && et_national_id_names.getText().toString().trim().length() != 10) {
                et_national_id_names.setError(getString(R.string.invalid));
                valid = false;
            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id))
                    && et_national_id_names.getText().toString().trim().length() != 10) {
                et_national_id_names.setError(getString(R.string.invalid));
                valid = false;
            }
        }
        if (tvIdExp.getText().toString().trim().length() == 0 || tvIdExp.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_id_expiry_date));
            valid = false;
        } else if (tvDob.getText().toString().trim().length() == 0 || tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_dob));
            valid = false;
        } else if (tvGender.getText().toString().trim().length() == 0 || tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_gender));
            valid = false;
        } else if (tvLang.getText().toString().trim().length() == 0 || tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.language));
            valid = false;
        }

        if (!valid) {
            etFirstName.requestFocus();
            layBtnDone.setEnabled(true);

        }
        return valid;
    }


    // Add validations to all field values.
    boolean isValidOld() {
        boolean valid = true;

        if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.passport_id)))
            type_id = "passport";
        else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id)))
            type_id = "iqama";
        else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id)))
            type_id = "national";
        else
            type_id = "";

        if (etFirstName.getText().toString().trim().length() == 0) {
            etFirstName.setError(getString(R.string.required));
            valid = false;
        }

        if (etFatherName.getText().toString().trim().length() == 0) {
            etFatherName.setError(getString(R.string.required));
            valid = false;
        }

        if (etFamilyName.getText().toString().trim().length() == 0) {
            etFamilyName.setError(getString(R.string.required));
            valid = false;
        }

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


        if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_any_id));
        } else {
            if (et_national_id_names.getText().toString().trim().length() == 0) {
                et_national_id_names.setError(getString(R.string.required));
                valid = false;

            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.iqama_id))
                    && et_national_id_names.getText().toString().trim().length() != 10) {
                et_national_id_names.setError(getString(R.string.invalid));
                valid = false;
            } else if (tvSelectID.getText().toString().equalsIgnoreCase(getString(R.string.national_id))
                    && et_national_id_names.getText().toString().trim().length() != 10) {
                et_national_id_names.setError(getString(R.string.invalid));
                valid = false;
            }
        }

//        if (tvGender.getText().toString().trim().length() == 0 || tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            String messgae = getString(R.string.select_gender);
//
//            if (tvLang.getText().toString().trim().length() == 0 || tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//                messgae = getString(R.string.select_lang_gender);
//            }
//
//            if (tvDob.getText().toString().trim().length() == 0 || tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//                messgae = getString(R.string.select_lang_gender_dob);
//            }
//            makeToast(messgae);
//            valid = false;
//
//        } else if (tvLang.getText().toString().trim().length() == 0 || tvLang.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            String messgae = getString(R.string.select_lang);
//
//            if (tvDob.getText().toString().trim().length() == 0 || tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//                messgae = getString(R.string.select__lang_dob);
//            }
//            makeToast(messgae);
//            valid = false;
//
//        } else
        if (tvDob.getText().toString().trim().length() == 0 || tvDob.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_dob));
            valid = false;
        }

        if (!valid)
            etFirstName.requestFocus();
//
//        if (etReasonVisit.getText().toString().trim().length() == 0) {
//            etReasonVisit.setError(getString(R.string.required));
//            valid = false;
//        }
        return valid;
    }

    // Pop up list to select gender or language
    public void showListPopUp(TextView view, String type, String lastValue) {
        List<String> list = new ArrayList<>();
        if (type.equalsIgnoreCase("g")) {
            list.add(getString(R.string.male));
            list.add(getString(R.string.female));
            list.add(getString(R.string.unspecified));
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
                et_national_id_names.setVisibility(View.VISIBLE);
            }
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()

    }

    @Override
    public void onSuccessLogin(LoginResponse response) {
        layBtnDone.setEnabled(true);

        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_PHONE, response.getMobilenum());

        Intent qi = new Intent(this, VerificationActivity.class);
        qi.putExtra("otp", response.getOtpCode());
        qi.putExtra("otp_c", response.getAttempt());
        qi.putExtra("type", Constants.GUEST_TYPE);
        qi.putExtra("mob", response.getMobilenum());
        startActivity(qi);
    }

    @Override
    public void showLoading() {
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
        layBtnDone.setEnabled(true);

    }

    @Override
    public void onFail(String msg) {
        layBtnDone.setEnabled(true);
        makeToast(msg);
    }

    @Override
    public void onNoInternet() {
        layBtnDone.setEnabled(true);
        Common.noInternet(this);
    }
}
