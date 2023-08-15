package sa.med.imc.myimc.Settings;

import static sa.med.imc.myimc.Utils.Common.messageDailogForCompleteActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sa.med.imc.myimc.Adapter.RecyclerViewAdapter;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Profile.presenter.ProfileImpl;
import sa.med.imc.myimc.Profile.presenter.ProfilePresenter;
import sa.med.imc.myimc.Profile.view.ProfileViews;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.ReportListActivity;
import sa.med.imc.myimc.Settings.model.ReportTypeResponse;
import sa.med.imc.myimc.Settings.model.Select_TypeResponse;
import sa.med.imc.myimc.Settings.model.Validations;
import sa.med.imc.myimc.SignUp.activity.SelfRegistrationActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.FileUtil;
import sa.med.imc.myimc.Utils.FileUtils;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.ContactUsListner;

public class ContactUsActivity extends BaseActivity implements RecyclerViewAdapter.ItemClickListener, ProfileViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.lay_btn_send)
    LinearLayout layBtnSend;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.lay_main_callback)
    LinearLayout layMainCallback;
    @BindView(R.id.tv_report_type)
    TextView tvReportType;

    @BindView(R.id.tv_select_visit)
    TextView tvSelectVisit;
    String episode_type, episodeno, episode_id, care_provider_description, app_date, modified_date;


    @BindView(R.id.tv_booking_id)
    TextView tvBookingId;


    //  tv_checkbox
    @BindView(R.id.tv_checkbox)
    TextView tv_checkbox;

    @BindView(R.id.tv_booking_date)
    TextView tvBookingDate;
    @BindView(R.id.et_title_report)
    EditText etTitleReport;
    @BindView(R.id.et_msg_report)
    EditText etMsgReport;
    @BindView(R.id.lay_main_request_report)
    LinearLayout layMainRequestReport;

    BookingResponse response;
    ReportTypeResponse reportTypeResponse;
    Select_TypeResponse Select_TypeResponse;
    ReportTypeResponse reportTypeTrasnsResponse;

    @BindView(R.id.et_email_report)
    EditText etEmailReport;
    @BindView(R.id.rd_physical_copy)
    RadioButton rdPhysicalCopy;
    @BindView(R.id.llperson)
    LinearLayout llperson;

    @BindView(R.id.rd_scanned_copy)
    RadioButton rdScannedCopy;
    @BindView(R.id.rd_general)
    RadioButton rdGeneral;
    @BindView(R.id.rd_recent_visit_report)
    RadioButton rdRecentVisitReport;
    @BindView(R.id.rd_other)
    RadioButton rdOther;
    @BindView(R.id.tv_espise_title)
    TextView tvEspiseTitle;
    @BindView(R.id.et_phone_report)
    TextView etPhoneReport;
    //etFullName
    @BindView(R.id.etFullName)
    EditText etFullName;
    //etNationalId
    @BindView(R.id.etNationalId)
    EditText etNationalId;
    @BindView(R.id.etDob)
    TextView etDob;
    @BindView(R.id.checkbox)
    CheckBox checkbox;


    @BindView(R.id.checkbox_submitted)
    CheckBox checkbox_submitted;


    String report_id = "";
    String report_id1 = "";
    String report_id2 = "";
    String copyType = "";

    String terms = "";
    String info = "";
    String msg = "";
    String full_name = "";
    String nationality_id = "";
    String dob = "";
    String emailID = "";
    String oTP = "";
    int is_email_valified = 0;

    @BindView(R.id.rd_request_trans_copy)
    RadioButton rdRequestTransCopy;
    @BindView(R.id.tv_upload_report)
    TextView tvUploadReport;
    @BindView(R.id.tv_uploaded_id_file_name)
    TextView tv_uploaded_id_file_name;
    @BindView(R.id.layFile)
    LinearLayout layFile;
    //txtAddMail
    @BindView(R.id.txtAddMail)
    TextView txtAddMail;
    //tick
    @BindView(R.id.tick)
    ImageView tick;
    private static final int FILE_SELECT_CODE = 0;
    String path_id = "", translate = "no";
    private LinearLayoutManager layoutManager;
    List<Select_TypeResponse.EpisodeList> userList = null;
    PopupWindow pwindow = null;
    String selected_value;
    Dialog dialog;
    ProfilePresenter profilePresenter;
    boolean isFromLogin = false;
    ProfileResponse profileResponse;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            sa.med.imc.myimc.Settings.model.Select_TypeResponse.EpisodeList thisItem = userList.get(position);
            // Toast.makeText(ContactUsActivity.this, "You Clicked: " + thisItem.getApptDate() + "-" + thisItem.getConsultant(), Toast.LENGTH_SHORT).show();

            app_date = thisItem.getEpisode_date();
            Log.e("DATE_IS", "DATA_IS" + app_date);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
            SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

//            Date d = null;
//            try {
//                d = df.parse(app_date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            String formatted = toDf.format(d);

//            StringTokenizer st = new StringTokenizer(app_date, "T");
//            String community = st.nextToken();
//
//            Log.e("SPLIT_DATE", "DATE_IS" + community);
//            String date_is = community;
//
//            DateTimeFormatter inputFormat = null;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            }
//            LocalDate dateTime = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                dateTime = LocalDate.parse(date_is, inputFormat);
//            }
//
//
//            DateTimeFormatter outputFormat = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.e("DATE_IS", "date_is" + dateTime.format(outputFormat));
//                modified_date = dateTime.format(outputFormat);
//
//            }


            tvSelectVisit.setText(app_date + "-" + thisItem.getCare_provider_description());
            episode_type = thisItem.getEpisodeType();
            episodeno = thisItem.getEpisode_number();
            episode_id = thisItem.getEpisode_id();
            care_provider_description = thisItem.getCare_provider_description();

            // selected_value=thisItem.getApptDate()+"-"+thisItem.getConsultant();
            pwindow.dismiss();
            Log.e("CLICKED_ITEM", "CLICKED_ITEM" + thisItem);
            isFromLogin = getIntent().getBooleanExtra("isFromLogin", false);

        }
    };

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity, boolean b) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra("isFromLogin", b);

        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String type) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        intent.putExtra("type", "report");
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ContactUsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        profilePresenter = new ProfileImpl(this, ContactUsActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_report);
//        layBtnSend.setEnabled(false);
        ButterKnife.bind(this);
        layBtnSend.setEnabled(true);
        tv_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imc.med.sa/en/terms-and-conditions"));
                startActivity(browserIntent);
            }
        });
       /* etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
        txtAddMail.setVisibility(View.GONE);
        tick.setVisibility(View.VISIBLE);*/
        try {
            is_email_valified = Integer.parseInt(SharedPreferencesUtils.getInstance(this).getValue(Constants.IS_EMAIL_VERIFIED,"0"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
//        if (is_email_valified==1) {
//            etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
//            txtAddMail.setVisibility(View.GONE);
//           // tick.setVisibility(View.VISIBLE);
//        } else if (is_email_valified==0) {
//            tick.setVisibility(View.GONE);
//            txtAddMail.setVisibility(View.VISIBLE);
//            etEmailReport.setText("");
//        }
//else {
//
//        }
        // etEmailReport.setFocusable(false);
        if (getIntent().hasExtra("type")) {
            toolbarTitle.setText(getString(R.string.request_for_report));
            layMainRequestReport.setVisibility(View.VISIBLE);
            layMainCallback.setVisibility(View.GONE);


            if (is_email_valified == 1) {
                etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
                txtAddMail.setVisibility(View.GONE);
                tick.setVisibility(View.VISIBLE);
            } else if (is_email_valified == 0) {
                tick.setVisibility(View.GONE);
                txtAddMail.setVisibility(View.VISIBLE);
                etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));

                if (!etEmailReport.getText().toString().trim().isEmpty()){
                    txtAddMail.setText("Verify");
                }
            }
            rdPhysicalCopy.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    llperson.setVisibility(View.VISIBLE);
                } else {
                    llperson.setVisibility(View.GONE);
                }
            });
            setCheckListeners();
            loadProfile();
        } else {
            toolbarTitle.setText(getString(R.string.request_a_callback));
            layMainCallback.setVisibility(View.VISIBLE);
            layMainRequestReport.setVisibility(View.GONE);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String updateMail = preferences.getString("UPDATE_MAIL", "");
        if (!updateMail.trim().isEmpty()) {
            etEmail.setText(updateMail);
        } else {
            etEmail.setText("@gmail.com");
        }
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, "").length() > 0) {
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER) != null) {

                etEmail.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
                etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
//                is_email_valified = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getIsEmailVerified();

                if (is_email_valified == 1) {
                    etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
                    txtAddMail.setVisibility(View.GONE);
                     tick.setVisibility(View.VISIBLE);
                } else if (is_email_valified == 0) {
                    tick.setVisibility(View.GONE);
                    txtAddMail.setVisibility(View.VISIBLE);
                    etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
                    if (!etEmailReport.getText().toString().trim().isEmpty()){
                        txtAddMail.setText("Verify");
                    }
                }

                //  Toast.makeText(ContactUsActivity.this, "CONTACT_US_EMAIL" + is_email_valified, Toast.LENGTH_LONG).show();
                etPhone.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMobileNumber());
                etPhoneReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMobileNumber());

                /*if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLang().equalsIgnoreCase("ar"))
                    etName.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getFirst_name_ar() + " " +
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMiddle_name_ar() + " " +
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLast_name_ar());
                else*/
                etName.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getFirstName() + " " +
                        SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLastName());

            }
        }
//        is_email_valified = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getIsEmailVerified();
//        if (is_email_valified==1) {
//            etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
//            txtAddMail.setVisibility(View.GONE);
//           // tick.setVisibility(View.VISIBLE);
//        } else if (is_email_valified==0) {
//            tick.setVisibility(View.GONE);
//            txtAddMail.setVisibility(View.VISIBLE);
//            etEmailReport.setText("");
//        }

//
//        if (is_email_valified.equalsIgnoreCase("1")) {
//            etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
//            //   tick.setVisibility(View.VISIBLE);
//            txtAddMail.setVisibility(View.GONE);
//        } else if (is_email_valified.equalsIgnoreCase("0")) {
//            tick.setVisibility(View.GONE);
//            txtAddMail.setVisibility(View.VISIBLE);
//            etEmailReport.setText("");
//        } else {
//
//        }
//        rdPhysicalCopy.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                llperson.setVisibility(View.VISIBLE);
//            } else {
//                llperson.setVisibility(View.GONE);
//            }
//        });

//        setCheckListeners();
        TextView MsgCounter = findViewById(R.id.MsgCounter);
        etMsgReport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = etMsgReport.getText().toString();
                int num = text.length();
                MsgCounter.setText("" + (int) num);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadProfile() {

        profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));


    }

    void setCheckListeners() {
        rdGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    tvEspiseTitle.setVisibility(View.GONE);
                    tvBookingId.setVisibility(View.GONE);
                    layFile.setVisibility(View.GONE);
                    tvReportType.setText(getString(R.string.select));
                    tvSelectVisit.setText(getString(R.string.select));//1
                    tv_uploaded_id_file_name.setVisibility(View.GONE);

                }
            }
        });

        rdRecentVisitReport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    tvEspiseTitle.setVisibility(View.VISIBLE);
                    tvBookingId.setVisibility(View.VISIBLE);
                    layFile.setVisibility(View.GONE);
                    tvReportType.setText(getString(R.string.select));
                    tvSelectVisit.setText(getString(R.string.select));//1
                    tv_uploaded_id_file_name.setVisibility(View.GONE);

                }
            }
        });

        rdOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    tvEspiseTitle.setVisibility(View.GONE);
                    tvBookingId.setVisibility(View.GONE);
                    layFile.setVisibility(View.GONE);
                    tv_uploaded_id_file_name.setVisibility(View.GONE);

                }
            }
        });


    }

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // myCalendar.getDatePicker().setMaxDate(System.currentTimeMillis());
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd-MMM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        response = null;
        reportTypeResponse = null;
        Select_TypeResponse = null;
    }

    @OnClick({R.id.tick, R.id.txtAddMail, R.id.checkbox, R.id.etDob, R.id.checkbox_submitted, R.id.tv_upload_report, R.id.iv_back, R.id.lay_btn_send, R.id.tv_report_type, R.id.tv_select_visit, R.id.tv_booking_id, R.id.tv_booking_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtAddMail:
                showDialog(ContactUsActivity.this, "HI");
                break;
            case R.id.etDob:

//                new DatePickerDialog(ContactUsActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                DatePickerDialog datePickerDialog = new DatePickerDialog(ContactUsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                // Common.getDateFromPickerCU(etDob, this);
                break;
            case R.id.tv_upload_report:
                checkPermissionFirst();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_report_type:
                Log.e("CLICKED", "HIIIII");
                if (reportTypeResponse == null) {
                    callAllReportsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else
                    showListPopUp(tvReportType, "report", tvReportType.getText().toString(), response, reportTypeResponse);
                break;
            case R.id.tv_select_visit:
                Log.e("CLICKED", "Hello");
                callSelectedVisitApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                break;
            case R.id.tv_booking_id:
                if (response == null) {
                    callGetAllBookingsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
                } else
                    showListPopUp(tvBookingId, "id", tvBookingId.getText().toString(), response, reportTypeResponse);
                break;

            case R.id.tv_booking_date:
                showListPopUp(tvBookingDate, "date", tvBookingDate.getText().toString(), response, reportTypeResponse);
                break;

            case R.id.lay_btn_send:

                if (isValid()) {
                    if (rdPhysicalCopy.isChecked()) {
                        //  copyType = "Physical";
                        copyType = "pickup";

                    } else if (rdScannedCopy.isChecked()) {
                        copyType = "email";
                    }

                    if (checkbox.isChecked()) {
                        terms = "true";
                    } else {
                        terms = "false";
                    }

                    if (checkbox_submitted.isChecked()) {
                        info = "true";
                    } else {
                        info = "false";
                    }

//                    if (terms.equalsIgnoreCase("true") && info.equalsIgnoreCase("true")) {
                    layBtnSend.setEnabled(true);
                    String id = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "");
                    String token = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "");
                    String episodeType = episode_type;
                    String episodeNo = episodeno;
                    String copytype = copyType;
                    msg = etMsgReport.getText().toString();
                    String reportId = report_id;


                    if (TextUtils.isEmpty(etFullName.getText().toString())) {
                        full_name = " ";
                    } else {
                        full_name = etFullName.getText().toString();
                    }

                    if (TextUtils.isEmpty(etNationalId.getText().toString())) {
                        nationality_id = " ";
                    } else {
                        nationality_id = etNationalId.getText().toString();
                    }

                    if (TextUtils.isEmpty(etDob.getText().toString())) {
                        dob = " ";
                    } else {
                        dob = etDob.getText().toString();
                    }
                    String info_value = info;
                    String terms_value = terms;

                    if (layMainCallback.getVisibility() == View.VISIBLE) {


                        if (etName.getText().toString().trim().isEmpty()) {
                            etName.setError("please enter name");
                            etName.requestFocus();
                            return;
                        }
                        if (etMsg.getText().toString().trim().isEmpty()) {
                            etMsg.setError("please enter message");
                            etMsg.requestFocus();
                            return;
                        }

                        if (etPhone.getText().toString().trim().isEmpty()) {
                            etPhone.setError("please enter phone number");
                            etPhone.requestFocus();
                            return;
                        }


                        String phoneFirstChar = etPhone.getText().toString().substring(0, 2);
                        String phoneFirstString = String.valueOf(phoneFirstChar);

                        Log.e("abcd", phoneFirstChar);

                        if (etPhone.getText().toString().trim().length() != 10 | !phoneFirstString.equalsIgnoreCase("05")) {
                            etPhone.setError(getResources().getString(R.string.invalid_number));
                            etPhone.requestFocus();
                            return;
                        }
                        if (!etEmail.getText().toString().trim().contains("@") & !etEmail.getText().toString().trim().contains(".")) {
                            etEmail.setError(getString(R.string.enter_valid_email));
                            etEmail.requestFocus();
                            return;
                        }

                        callContactUs(
                                etName.getText().toString(),
                                etEmail.getText().toString(),
                                etPhone.getText().toString(),
                                et_title.getText().toString(),
                                etMsg.getText().toString(),
                                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1)
                        );
                    } else {

                        callRequestReport(
                                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                episode_type,
                                episodeno,
                                copyType,
                                msg,
                                report_id + "-" + translate,
                                full_name,
                                nationality_id,
                                dob,
                                info, terms,
                                path_id);
                    }

                    Log.e("REQUEST_DATA", "REQUEST_DATA" +
                            id + "" + token + " "
                            + episodeType + " " + episodeNo + " " +
                            copytype + " " + msg + " " + reportId + " " +
                            full_name + " " +
                            dob + " " + nationality_id + " " +
                            info_value + " " + terms_value + " " + path_id);


//                    }
//                    else {
//                        layBtnSend.setEnabled(true);
//                        // Toast.makeText(ContactUsActivity.this,"NOTHING IS FINE",Toast.LENGTH_LONG).show();
//                    }

                }

                break;
        }
    }

    // Add validations to all field values.
    boolean isValid() {
        boolean valid = true;


        Validations validation = new Validations(this);

//        if (!validation.isEmailValid(etEmailReport)) {
//            valid = false;
//        }

//        if (!validation.isPhoneValid(etPhoneReport)) {
//            valid = false;
//        }

        if (rdRecentVisitReport.isChecked()) {
            if (tvBookingId.getText().toString().trim().length() == 0 || tvBookingId.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
                makeToast(getString(R.string.select_appon_date_id));
                valid = false;
            }
        }

//        if (etMsgReport.getText().toString().trim().length() == 0) {
//            etMsgReport.setError(getString(R.string.required));
//            valid = false;
//        }


//        if (tvReportType.getText().toString().trim().length() == 0 || tvReportType.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_report_type));
//            valid = false;
//        }

        if (path_id.length() == 0 && translate.equalsIgnoreCase("yes")) {//rdRequestTransCopy.isChecked()) {
            makeToast(getString(R.string.request_translation_file));
            valid = false;
        }

        if (!valid)
            layBtnSend.setEnabled(true);

        return valid;
    }

    @Override
    public void onBackPressed() {
        finish();
        ContactUsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    boolean validAll() {
        Validations validation = new Validations(this);
        boolean msgValid = validation.isMsgValid(etMsg);
        boolean enName = validation.isNameValid(etName);
        boolean emailValid = false;

        if (etEmail.getText().toString().trim().isEmpty()) {
            emailValid = true;
        } else
            emailValid = validation.isEmailValid(etEmail);

        boolean phone = validation.isPhoneValid(etPhone);

        if (msgValid && enName && phone && emailValid) {
            return true;
        } else {
            return false;
        }

    }


    // send a callback request
    public void callContactUs(String name, String email, String phone, String title, String
            message, String mrNumber, int hosp) {
//        Common.showDialog(this);
//        JSONObject object = new JSONObject();


        JSONObject details = new JSONObject();
        try {
            details.put("title", title);
            details.put("name", name);
            details.put("email", email);
            details.put("phone", phone);
            details.put("message", message);
            details.put("MRN Number", mrNumber);
            details.put("HOSP", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        String details = "Name: " + name +
//                " - Email: " + email +
//                " - Mobile Number: " + phone +
//                " – Title: " + title +
//                " – Message: " + message +
//                " – MRN Number: " + mrNumber +
//                " - HOSP: " + hosp;

        new MyhttpMethod(ContactUsActivity.this).callContactUs(details, new ContactUsListner() {
            @Override
            public void onSuccess(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(ContactUsActivity.this, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                et_title.setText("");
                etMsg.setText("");
                etName.setText("");
                etPhone.setText("");
                etEmail.setText("");
                messageDailogForCompleteActivity(ContactUsActivity.this, getString(R.string.request_submitted_success));
            }

            @Override
            public void onFaild(String response) {

            }
        });

//        try {
//
//            object.put("task_type", "Contact Us");
//            object.put("patient_id", patientId);
//            object.put("details", details);
//
////            object.put("title", title);
////            object.put("message", message);
////            object.put("mrNumber", mrNumber);
////            object.put("platform", "Android");//mobile
////            object.put("hosp", hosp);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.e("abcd",object.toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
//        WebService webService = ServiceGenerator.createService(WebService.class);
//        Call<SimpleResponse> xxx = webService.contactUsWeb(body);
//
//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
//                            et_title.setText("");
//                            etMsg.setText("");
//                            etName.setText("");
//                            etPhone.setText("");
//                            etEmail.setText("");
//                            onBackPressed();
//                        } else {
////                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
//                            Toast.makeText(ContactUsActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });

    }

    // get all booking ids and details
    public void callGetAllBookingsApi(String mrNumber, String token) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("fromDate", "");
            object.put("toDate", "");
            object.put("clinicCode", "");
            object.put("physicianCode", "");
            object.put("bookingStatus", Constants.BookingStatus.COMPLETED);
            object.put("itemCount", "100");
            object.put("page", "0");
            object.put("hosp", SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookingResponse> xxx = webService.getAllBookings(body);
        xxx.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    BookingResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            ContactUsActivity.this.response = response1;
                            showListPopUp(tvBookingId, "id", tvBookingId.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
                        }

                    }
                }

                layBtnSend.setEnabled(true);

            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, t.getMessage());
                layBtnSend.setEnabled(true);
            }
        });

    }

    // get all Reports
    public void callAllReportsApi(String token, int hosp) {
        Common.showDialog(this);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ReportTypeResponse> xxx = webService.getReportTypes();

        Log.e("abcd", xxx.request().url().toString());

        xxx.enqueue(new Callback<ReportTypeResponse>() {
            @Override
            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                Log.e("abcd", new Gson().toJson(response.body()));

                Common.hideDialog();
                if (response.isSuccessful()) {
                    ReportTypeResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("Success")) {
                            ContactUsActivity.this.reportTypeResponse = response1;
                            showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
                        }

                    }
                }

                layBtnSend.setEnabled(true);

            }

            @Override
            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, t.getMessage());
                layBtnSend.setEnabled(true);
            }
        });

    }

    // get selected visit type
//    public void callSelectedVisitApi(String token) {
//        Common.showDialog(this);
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<Select_TypeResponse> xxx = webService.getSelecteVisit();
//        xxx.enqueue(new Callback<Select_TypeResponse>() {
//            @Override
//            public void onResponse(Call<Select_TypeResponse> call, Response<Select_TypeResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                 //   Select_TypeResponse response1 = response.body();
//                      //  ContactUsActivity.this.Select_TypeResponse = response1;
//                      //  showListSelectedVisitPopUp(tvSelectVisit, "",tvSelectVisit.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.Select_TypeResponse);
//                        Log.e("SUCCES_RESPONSE","SUCCESS_RESPONSE" +"" );
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<Select_TypeResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//here
    private void callSelectedVisitApi(String token, int hosp) {
        Log.i("autolog", "getUserList");
        ValidateResponse user = SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_USER);
        String patientId = user.getPatientId();


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Log.i("autolog", " APIService service = retrofit.create(APIService.class);");
        Call<Select_TypeResponse> call = webService.getSelecteVisit(patientId, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""));
        Log.i("autolog", " Call<List<Select_TypeResponse>> call = webService.getSelecteVisit();;");


        Log.e("abcd", call.request().url().toString());

        call.enqueue(new Callback<Select_TypeResponse>() {
            @Override
            public void onResponse(Call<Select_TypeResponse> call, Response<Select_TypeResponse> response) {
                Log.e("abcd", new Gson().toJson(response.body()));

                Log.i("autolog", "onResponse");
                if (response.isSuccessful()) {
                    userList = response.body().getEpisode_list();
//                    Log.i("autolog", "List<User> userList = response.body();");
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    int popupwindowHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                    LayoutInflater layoutInflater = (LayoutInflater) getActivityContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = layoutInflater.inflate(
                            R.layout.dialog_custom_list, null);

                    pwindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pwindow.setHeight(popupwindowHeight);
                    pwindow.setFocusable(true);
                    pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            //TODO dismiss settings
                        }
                    });
                    RecyclerView landmarkRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_list);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivityContext(), userList);
                    landmarkRecyclerView.setHasFixedSize(true);
                    landmarkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
                    landmarkRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(onItemClickListener);
                    //adapter.setOnItemClickListener(onItemClickListener);
                    tvSelectVisit.setText(getString(R.string.select));
                    pwindow.dismiss();
                    pwindow.setWindowLayoutMode(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    pwindow.setHeight(1);
                    pwindow.setWidth(1);
                    // pwindow.showAtLocation(layout, Gravity.NO_GRAVITY,25, mPoint.y);
                    pwindow.showAsDropDown(recyclerView, 2, 2);
                }
            }

            @Override
            public void onFailure(Call<Select_TypeResponse> call, Throwable t) {
                Log.i("autolog", t.getMessage());
            }
        });

    }


    // get reports list can be translate
    public void callTranslaionReportsApi(String token, int hosp) {
        Common.showDialog(this);
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ReportTypeResponse> xxx = webService.getTransaltionReportTypes(hosp);

        xxx.enqueue(new Callback<ReportTypeResponse>() {
            @Override
            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    ReportTypeResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            if (rdRequestTransCopy.isChecked()) {
//                                ContactUsActivity.this.reportTypeTrasnsResponse = response1;
//                                showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeTrasnsResponse);
//                            } else {
                            ContactUsActivity.this.reportTypeResponse = response1;
                            showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
//                            }
                        }

                    }
                }

                layBtnSend.setEnabled(true);

            }

            @Override
            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, t.getMessage());
                layBtnSend.setEnabled(true);
            }
        });

    }

    //TODO: add hosp 
    // send a callback request
    public void callRequestReport(String mrNumber, String token, String episode_type,
                                  String episode_no, String copyType, String message, String reportTypeId,
                                  String fullname, String nationalid,
                                  String dob, String info, String trems, String file) {

        if (mrNumber == null || token == null || episode_type == null || episode_no == null || copyType == null ||
                message == null || reportTypeId == null || fullname == null || nationalid == null || dob == null || info == null ||
                terms == null || file == null) {

            Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.string_please_fill_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        ValidateResponse user = SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_USER);
        String patientId = user.getPatientId();
        int hosp1 = SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0);

        if (TextUtils.isEmpty(etEmailReport.getText().toString().trim()) | !Patterns.EMAIL_ADDRESS.matcher(etEmailReport.getText().toString().trim()).matches()) {
            etEmailReport.setError(getString(R.string.enter_valid_email));
            etEmailReport.requestFocus();
            return;
        }
        if (is_email_valified!=1){
            Toast.makeText(this, getString(R.string.verify_email), Toast.LENGTH_SHORT).show();
            etEmailReport.requestFocus();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestType", "Request Report");
            jsonObject.put("subject", "Request Report");
            jsonObject.put("appointmentDate", app_date);
            jsonObject.put("outPatient", care_provider_description + " " + episode_type);
            jsonObject.put("message", message);
            jsonObject.put("pickupId", nationalid.trim());
            jsonObject.put("pickupDob", dob.trim());
            jsonObject.put("episodeNo", episode_no);
            jsonObject.put("episodeKey", episode_no);
            jsonObject.put("episode", episode_id);
            jsonObject.put("hosp", hosp1);
            jsonObject.put("copyType", copyType);
            jsonObject.put("pickupName", fullname.trim());
            jsonObject.put("email", etEmailReport.getText().toString().trim());
            jsonObject.put("reportType", reportTypeId);
            jsonObject.put("patientId", patientId);
            jsonObject.put("mrNumber", mrNumber);
            jsonObject.put("consentChecked", true);
            jsonObject.put("accept", true);
            jsonObject.put("phoneNumber", etPhoneReport.getText().toString().trim());

//            if (isFromLogin){
//                jsonObject.put("patientId","22");
//                jsonObject.put("mrNumber","314134");
//
//            }else {
//
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

//
//        String details = "mrNumber: " + mrNumber +
//                " - Episode Type: " + episode_type +
//                " - Episode Number: " + episode_no +
//                " – Copy Type: " + copyType +
//                " – Message: " + message +
//                " - Report Type Id: " + reportTypeId +
//                " - Full Name: " + fullname +
//                " - National Id: " + nationalid +
//                " - DOB: " + dob +
//                " - Info: " + info +
//                " - Terms: " + trems;
//
//        Log.e("abcd",details);

        new MyhttpMethod(ContactUsActivity.this).callRequestReport(jsonObject, new ContactUsListner() {
            @Override
            public void onSuccess(String response) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", response);
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }

            @Override
            public void onFaild(String response) {
                Common.messageDailog(ContactUsActivity.this, response);

            }
        });

//        Common.showDialog(this);
//        JSONObject object = new JSONObject();
//        try {
//            object.put("task_type","Request Report");
//            object.put("patient_id",patientId);
//            object.put("details",details);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        MultipartBody.Part file1 = null;
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<JSONObject> xxx;
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
//
//        xxx = webService.requestForReport(body);
//
//
//        xxx.enqueue(new Callback<JSONObject>() {
//            @Override
//            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    JSONObject response1 = response.body();
//                    try {
//                        Toast.makeText(ContactUsActivity.this, response1.getString("status"), Toast.LENGTH_SHORT).show();
//                        alertBack(response1.getString("status"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                layBtnSend.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure(Call<JSONObject> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });

    }

    // Pop up list to select booking id or date or report type
    public void showListPopUp(TextView view, String type, String lastValue, BookingResponse
            response, ReportTypeResponse reportTypeResponse) {
        List<String> list = new ArrayList<>();
        if (type.equalsIgnoreCase("id") && response != null) {

            for (int i = 0; i < response.getBookings().size(); i++) {
                list.add(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()));
            }
        }

        if (type.equalsIgnoreCase("report") && reportTypeResponse != null) {
            for (int i = 0; i < reportTypeResponse.getReport_type_list().size(); i++) {
                list.add(reportTypeResponse.getReport_type_list().get(i).getReport_description());
            }
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
            if (type.equalsIgnoreCase("report")) {
                report_id = String.valueOf(reportTypeResponse.getReport_type_list().get(position).getReport_code());
                translate = String.valueOf(reportTypeResponse.getReport_type_list().get(position).getReport_description());

                if (reportTypeResponse.getReport_type_list().get(position).getReport_description().equalsIgnoreCase("yes")) {
                    layFile.setVisibility(View.VISIBLE);
                } else {
                    layFile.setVisibility(View.GONE);

                }
            }
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()

    }

    //1
    public void showListSelectedVisitPopUp(TextView view, String type, String lastValue, BookingResponse
            response, sa.med.imc.myimc.Settings.model.Select_TypeResponse.EpisodeList selelctvistresponse) {
        List<String> list = new ArrayList<>();
        Log.e("ADAPTER_CONTACT_LIST", selelctvistresponse + "");
        if (type.equalsIgnoreCase("id") && response != null) {
            for (int i = 0; i < response.getBookings().size(); i++) {
                list.add(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()));
            }
        }

        if (selelctvistresponse != null) {
            //  for (int i = 0; i < selelctvistresponse.getSelect_visit().size(); i++) {
            list.add(selelctvistresponse.getEpisode_id());
            // }
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
            // report_id1 = String.valueOf(selelctvistresponse.getSelect_visit().get(position).getEpisodeKey());
            report_id1 = String.valueOf(selelctvistresponse.getEpisode_id());

            Log.e("DATA_IS", report_id1);
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()

    }


    public int getSelectedPosition(TextView view, BookingResponse response) {
        int pos = -1;
        String value = view.getText().toString();
        if (response != null)
            for (int i = 0; i < response.getBookings().size(); i++) {
                if (value.equalsIgnoreCase(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()))) {
                    pos = i;
                }

            }
        return pos;

    }

    // Alert dialog
    public void alertBack(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvReportType.setText(getString(R.string.select));
                        tvBookingId.setText(getString(R.string.select));
                        etMsgReport.setText("");
                        etEmailReport.setText("");
                        onBackPressed();

                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {

                try {
                    Typeface typeface = ResourcesCompat.getFont(ContactUsActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    @NonNull
    private MultipartBody.Part prepareImgFilePart(String filee, String name) {
        File file = new File(filee);
        String mime = FileUtils.getMimeType(file);
        Log.e("mime", "" + mime);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mime), file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File tempFile = new File(uri.toString());
            File file = createTmpFileFromUri(this, uri, tempFile.getName());

            if (file != null) {
                tv_uploaded_id_file_name.setText(file.getName());
                path_id = file.getPath();
                tv_uploaded_id_file_name.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Problem while reading the image please try again later.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createTmpFileFromUri(Context context, Uri uri, String fileName) {
        File file = null;
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            file = new File(context.getCacheDir() + "/" + fileName);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    @SuppressLint("Recycle")
    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = 0;
                if (cursor != null) {
                    column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                }

            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    private void showFileChooser() {
        ImagePicker.with(this)
                .compress(1024)
                .start();
    }

    // check permission to save reports required storage permission
    void checkPermissionFirst() {
        showFileChooser();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 33: {
                if (grantResults.length > 0) {
                    showFileChooser();
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void showDialog(Activity activity, String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_email);

        TextView txtAddMail = (TextView) dialog.findViewById(R.id.txtAddMail_dialog);
        TextView txt_close = (TextView) dialog.findViewById(R.id.txt_close);
        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        EditText et_email_report = (EditText) dialog.findViewById(R.id.et_email_report);
        EditText et_otp = (EditText) dialog.findViewById(R.id.et_otp);

        et_email_report.setText(etEmailReport.getText().toString().trim());

        txtAddMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailID = et_email_report.getText().toString();
                if (isValidEmail(emailID)) {

                    updateProfile(emailID);

                } else {
                    et_email_report.setError("Please enter valid Email Address ");
                    //  Toast.makeText(ContactUsActivity.this,"Please enter valid Email Address ",Toast.LENGTH_LONG).show();
                }
            }
        });
//        text.setText(msg);
//
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (et_otp.getText().toString().trim().isEmpty()) return;
                    oTP = et_otp.getText().toString();

                    callVerifyOTP(SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, ""),
                            SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_PATIENT_ID, ""),
                            SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            emailID, oTP);

                    Log.e("DATA_IS", "DATA_IS" + SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, "") +
                            SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, "") +
                            emailID + oTP);
                } catch (Exception e) {
                    dialog.dismiss();
                }

            }
        });

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void updateProfile(String emailID) {

        profilePresenter.callUpdateProfileApi(SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                emailID, profileResponse.getUserDetails().getAddress(),profileResponse.getUserDetails().getAddress(),
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));


    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // send a callback request
    public void callSendOTP(String mrNumber, String token, String email, int hosp) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {
          /*  object.put("language", "en");
            object.put("platform", "Android");
            object.put("token", token);*/
            object.put("patid", mrNumber);
            object.put("email", email);
            object.put("hosp", hosp);
//            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // RequestBody mr_number = RequestBody.create(MediaType.parse("text/plain"), mrNumber);
        //  RequestBody email_value = RequestBody.create(MediaType.parse("text/plain"), emailID);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        Call<SimpleResponse> xxx = webService.sendOTP("en", "Android", token, "no", body);//"application/json; charset=utf-8",
        //  Call<SimpleResponse> xxx = webService.sendOTP(body);//"application/json; charset=utf-8",

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            // alertBack(response1.getMessage());
                            Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.email_sent), Toast.LENGTH_LONG).show();
                        } else {
                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
                        }
                    }
                }

                //layBtnSend.setEnabled(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, t.getMessage());
                layBtnSend.setEnabled(true);
            }
        });

    }

    public void callVerifyOTP(String mrNumber, String patient_id, String token, String email, String otp) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {

            object.put("patid", mrNumber);
            object.put("patient_id", patient_id);

            object.put("email", email);
            object.put("otp", otp);
//            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // RequestBody mr_number = RequestBody.create(MediaType.parse("text/plain"), mrNumber);
        //  RequestBody email_value = RequestBody.create(MediaType.parse("text/plain"), emailID);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        Call<SimpleResponse> xxx = webService.verifyOTP("en", "Android", token, "no", body);//"application/json; charset=utf-8",
        //  Call<SimpleResponse> xxx = webService.sendOTP(body);//"application/json; charset=utf-8",

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                dialog.dismiss();
                if (response.isSuccessful()) {

                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            // alertBack(response1.getMessage());
                            Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.validated_email), Toast.LENGTH_LONG).show();
                            etEmailReport.setText(email);
                            txtAddMail.setVisibility(View.GONE);

                            profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, ""),
                                    SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        } else {
                            Common.makeToast(ContactUsActivity.this, getResources().getString(R.string.emai_not_validate));
                        }
                    }
                }

                //layBtnSend.setEnabled(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, getResources().getString(R.string.emai_not_validate));
                layBtnSend.setEnabled(true);
            }
        });

    }

    public void updateEmail(String mrNumber, String patient_id, String token, String email, String otp) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {

            object.put("patid", mrNumber);
            object.put("patient_id", patient_id);
            object.put("email", email);
            object.put("otp", otp);
//            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // RequestBody mr_number = RequestBody.create(MediaType.parse("text/plain"), mrNumber);
        //  RequestBody email_value = RequestBody.create(MediaType.parse("text/plain"), emailID);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        // WebService webService = ServiceGenerator.createService(WebService.class);
        Call<SimpleResponse> xxx = webService.verifyOTP("en", "Android", token, "no", body);//"application/json; charset=utf-8",
        //  Call<SimpleResponse> xxx = webService.sendOTP(body);//"application/json; charset=utf-8",

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                dialog.dismiss();
                if (response.isSuccessful()) {

                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            // alertBack(response1.getMessage());
                            Toast.makeText(ContactUsActivity.this, response1.getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
                        }
                    }
                }

                //layBtnSend.setEnabled(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(ContactUsActivity.this, t.getMessage());
                layBtnSend.setEnabled(true);
            }
        });

    }

    @Override
    public void onGetProfile(ProfileResponse response) {
        profileResponse=response;
        etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));

        SharedPreferencesUtils.getInstance(ContactUsActivity.this).setValue(Constants.KEY_USER, response.getUserDetails());
        is_email_valified = Integer.parseInt(SharedPreferencesUtils.getInstance(this).getValue(Constants.IS_EMAIL_VERIFIED,"0"));



        if (is_email_valified == 1) {
            etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));
            txtAddMail.setVisibility(View.GONE);
            tick.setVisibility(View.VISIBLE);
        } else if (is_email_valified == 0) {
            tick.setVisibility(View.GONE);
            txtAddMail.setVisibility(View.VISIBLE);
            etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_PATIENT_EMAIL,""));

            if (!etEmailReport.getText().toString().trim().isEmpty()){
                txtAddMail.setText("Verify");
            }
        }
    }

    @Override
    public void onGetMedication(MedicationRespone response) {

    }

    @Override
    public void onUpdateProfile(SimpleResponse response) {
        callSendOTP(SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                emailID,
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

        Log.e("DATA_IS", "DATA_IS" + SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_MRN, "") +
                SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, "") +
                emailID);
        loadProfile();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {

    }

    @Override
    public void onNoInternet() {

    }
}

//package sa.med.imc.myimc.Settings;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.common.base.MoreObjects;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.net.URISyntaxException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import sa.med.imc.myimc.Adapter.RecyclerViewAdapter;
//import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
//import sa.med.imc.myimc.Base.BaseActivity;
//import sa.med.imc.myimc.Managebookings.model.BookingResponse;
//import sa.med.imc.myimc.Network.Constants;
//import sa.med.imc.myimc.Network.ServiceGenerator;
//import sa.med.imc.myimc.Network.SharedPreferencesUtils;
//import sa.med.imc.myimc.Network.SimpleResponse;
//import sa.med.imc.myimc.Network.WebService;
//import sa.med.imc.myimc.R;
//import sa.med.imc.myimc.Settings.Model.ReportTypeResponse;
//import sa.med.imc.myimc.Settings.Model.SelectVisitResponse;
//import sa.med.imc.myimc.Settings.Model.Select_TypeResponse;
//import sa.med.imc.myimc.Settings.Model.Validations;
//import sa.med.imc.myimc.Utils.Common;
//import sa.med.imc.myimc.Utils.FileUtil;
//import sa.med.imc.myimc.Utils.FileUtils;
//
//import static java.security.AccessController.getContext;
//
//public class ContactUsActivity extends BaseActivity implements RecyclerViewAdapter.ItemClickListener {
//
//    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.et_title)
//    EditText et_title;
//    @BindView(R.id.et_msg)
//    EditText etMsg;
//    @BindView(R.id.lay_btn_send)
//    LinearLayout layBtnSend;
//    @BindView(R.id.et_name)
//    EditText etName;
//    @BindView(R.id.et_email)
//    EditText etEmail;
//    @BindView(R.id.et_phone)
//    EditText etPhone;
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.lay_main_callback)
//    LinearLayout layMainCallback;
//    @BindView(R.id.tv_report_type)
//    TextView tvReportType;
//
//    @BindView(R.id.tv_select_visit)
//    TextView tvSelectVisit;
//    String episode_type, episodeno, app_date, modified_date;
//
//
//    @BindView(R.id.tv_booking_id)
//    TextView tvBookingId;
//    @BindView(R.id.tv_booking_date)
//    TextView tvBookingDate;
//    @BindView(R.id.et_title_report)
//    EditText etTitleReport;
//    @BindView(R.id.et_msg_report)
//    EditText etMsgReport;
//    @BindView(R.id.lay_main_request_report)
//    LinearLayout layMainRequestReport;
//
//    BookingResponse response;
//    ReportTypeResponse reportTypeResponse;
//    Select_TypeResponse Select_TypeResponse;
//    ReportTypeResponse reportTypeTrasnsResponse;
//
//    @BindView(R.id.et_email_report)
//    EditText etEmailReport;
//    @BindView(R.id.rd_physical_copy)
//    RadioButton rdPhysicalCopy;
//    @BindView(R.id.llperson)
//    LinearLayout llperson;
//
//    @BindView(R.id.rd_scanned_copy)
//    RadioButton rdScannedCopy;
//    @BindView(R.id.rd_general)
//    RadioButton rdGeneral;
//    @BindView(R.id.rd_recent_visit_report)
//    RadioButton rdRecentVisitReport;
//    @BindView(R.id.rd_other)
//    RadioButton rdOther;
//    @BindView(R.id.tv_espise_title)
//    TextView tvEspiseTitle;
//    @BindView(R.id.et_phone_report)
//    EditText etPhoneReport;
//    //etFullName
//    @BindView(R.id.etFullName)
//    EditText etFullName;
//    //etNationalId
//    @BindView(R.id.etNationalId)
//    EditText etNationalId;
//    @BindView(R.id.etDob)
//    TextView etDob;
//    @BindView(R.id.checkbox)
//    CheckBox checkbox;
//
//
//    @BindView(R.id.checkbox_submitted)
//    CheckBox checkbox_submitted;
//
//
//    String report_id = "";
//    String report_id1 = "";
//    String report_id2 = "";
//    String copyType = "";
//
//    String terms = "";
//    String info = "";
//    String msg = "";
//    String full_name = "";
//    String nationality_id = "";
//    String dob = "";
//
//
//    @BindView(R.id.rd_request_trans_copy)
//    RadioButton rdRequestTransCopy;
//    @BindView(R.id.tv_upload_report)
//    TextView tvUploadReport;
//    @BindView(R.id.tv_uploaded_id_file_name)
//    TextView tv_uploaded_id_file_name;
//    @BindView(R.id.layFile)
//    LinearLayout layFile;
//    private static final int FILE_SELECT_CODE = 0;
//    String path_id = "", translate = "no";
//    private LinearLayoutManager layoutManager;
//    List<Select_TypeResponse> userList = null;
//    PopupWindow pwindow = null;
//    String selected_value;
//    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            //TODO: Step 4 of 4: Finally call getTag() on the view.
//            // This viewHolder will have all required values.
//
//            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
//            int position = viewHolder.getAdapterPosition();
//            // viewHolder.getItemId();
//            // viewHolder.getItemViewType();
//            // viewHolder.itemView;
//            Select_TypeResponse thisItem = userList.get(position);
//            Toast.makeText(ContactUsActivity.this, "You Clicked: " + thisItem.getApptDate() + "-" + thisItem.getConsultant(), Toast.LENGTH_SHORT).show();
//
//            app_date = thisItem.getApptDate();
//
//
//            StringTokenizer st = new StringTokenizer(app_date, "T");
//            String community = st.nextToken();
//
//            Log.e("SPLIT_DATE", "DATE_IS" + community);
//            String date_is = community;
//
//            DateTimeFormatter inputFormat = null;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            }
//            LocalDate dateTime = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                dateTime = LocalDate.parse(date_is, inputFormat);
//            }
//
//
//            DateTimeFormatter outputFormat = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                outputFormat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.e("DATE_IS", "date_is" + dateTime.format(outputFormat));
//                modified_date = dateTime.format(outputFormat);
//
//            }
//            tvSelectVisit.setText(modified_date + "-" + thisItem.getConsultant());
//            episode_type = thisItem.getEpisodeType();
//            episodeno = thisItem.getEpisodeNo();
//
//
//            // selected_value=thisItem.getApptDate()+"-"+thisItem.getConsultant();
//            pwindow.dismiss();
//            Log.e("CLICKED_ITEM", "CLICKED_ITEM" + thisItem);
//        }
//    };
//
//    @Override
//    protected Context getActivityContext() {
//        return this;
//    }
//
//    public static void startActivity(Activity activity) {
//        Intent intent = new Intent(activity, ContactUsActivity.class);
//        activity.startActivity(intent);
//    }
//
//    public static void startActivity(Activity activity, String type) {
//        Intent intent = new Intent(activity, ContactUsActivity.class);
//        intent.putExtra("type", "report");
//        activity.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        if (SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        } else {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
//        ContactUsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_request_report);
////        layBtnSend.setEnabled(false);
//        ButterKnife.bind(this);
//        layBtnSend.setEnabled(false);
//        if (getIntent().hasExtra("type")) {
//            toolbarTitle.setText(getString(R.string.request_for_report));
//            layMainRequestReport.setVisibility(View.VISIBLE);
//            layMainCallback.setVisibility(View.GONE);
//
//        } else {
//            toolbarTitle.setText(getString(R.string.request_a_callback));
//            layMainCallback.setVisibility(View.VISIBLE);
//            layMainRequestReport.setVisibility(View.GONE);
//        }
//
//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_ID, "").length() > 0) {
//            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER) != null) {
//
//                etEmail.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
//                etEmailReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getEmail());
//
//                etPhone.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMobilenum());
//                etPhoneReport.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMobilenum());
//
//                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLang().equalsIgnoreCase("ar"))
//                    etName.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getFirst_name_ar() + " " +
//                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMiddle_name_ar() + " " +
//                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLast_name_ar());
//                else
//                    etName.setText(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getFirstName() + " " +
//                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getMiddleName() + " " +
//                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getLastName());
//
//            }
//        }
//
//        rdPhysicalCopy.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                llperson.setVisibility(View.VISIBLE);
//            } else {
//                llperson.setVisibility(View.GONE);
//            }
//        });
//
//        setCheckListeners();
//    }
//
//    void setCheckListeners() {
//        rdGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isPressed()) {
//                if (isChecked) {
//                    tvEspiseTitle.setVisibility(View.GONE);
//                    tvBookingId.setVisibility(View.GONE);
//                    layFile.setVisibility(View.GONE);
//                    tvReportType.setText(getString(R.string.select));
//                    tvSelectVisit.setText(getString(R.string.select));//1
//                    tv_uploaded_id_file_name.setVisibility(View.GONE);
//
//                }
//            }
//        });
//
//        rdRecentVisitReport.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isPressed()) {
//                if (isChecked) {
//                    tvEspiseTitle.setVisibility(View.VISIBLE);
//                    tvBookingId.setVisibility(View.VISIBLE);
//                    layFile.setVisibility(View.GONE);
//                    tvReportType.setText(getString(R.string.select));
//                    tvSelectVisit.setText(getString(R.string.select));//1
//                    tv_uploaded_id_file_name.setVisibility(View.GONE);
//
//                }
//            }
//        });
//
//        rdOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isPressed()) {
//                if (isChecked) {
//                    tvEspiseTitle.setVisibility(View.GONE);
//                    tvBookingId.setVisibility(View.GONE);
//                    layFile.setVisibility(View.GONE);
//                    tv_uploaded_id_file_name.setVisibility(View.GONE);
//
//                }
//            }
//        });
//
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        response = null;
//        reportTypeResponse = null;
//        Select_TypeResponse = null;
//    }
//
//    @OnClick({R.id.checkbox, R.id.etDob, R.id.checkbox_submitted, R.id.tv_upload_report, R.id.iv_back, R.id.lay_btn_send, R.id.tv_report_type, R.id.tv_select_visit, R.id.tv_booking_id, R.id.tv_booking_date})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.etDob:
//                Common.getDateFromPickerCU(etDob, this);
//                break;
//            case R.id.tv_upload_report:
//                checkPermissionFirst();
//                break;
//
//            case R.id.iv_back:
//                onBackPressed();
//                break;
//
//            case R.id.tv_report_type:
//                Log.e("CLICKED", "HIIIII");
//                if (reportTypeResponse == null) {
//                    callAllReportsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
//                } else
//                    showListPopUp(tvReportType, "report", tvReportType.getText().toString(), response, reportTypeResponse);
//                break;
//            case R.id.tv_select_visit:
//                Log.e("CLICKED", "Hello");
//                callSelectedVisitApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
//
//                break;
//            case R.id.tv_booking_id:
//                if (response == null) {
//                    callGetAllBookingsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
//                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
//                } else
//                    showListPopUp(tvBookingId, "id", tvBookingId.getText().toString(), response, reportTypeResponse);
//                break;
//
//            case R.id.tv_booking_date:
//                showListPopUp(tvBookingDate, "date", tvBookingDate.getText().toString(), response, reportTypeResponse);
//                break;
//
//            case R.id.lay_btn_send:
//
//                if (isValid()) {
//                    if (rdPhysicalCopy.isChecked()) {
//                        //  copyType = "Physical";
//                        copyType = "pickup";
//
//                    } else if (rdScannedCopy.isChecked()) {
//                        copyType = "email";
//                    }
//                    if (checkbox.isChecked()) {
//                        terms = "true";
//                    } else {
//                        terms = "false";
//                    }
//                    if (checkbox_submitted.isChecked()) {
//                        info = "true";
//                    } else {
//                        info = "false";
//                    }
//
//                    if (terms.equalsIgnoreCase("true") && info.equalsIgnoreCase("true")) {
//                        layBtnSend.setEnabled(true);
//                       String  id=SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "");
//                       String token=SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "");
//                       String episodeType=episode_type;
//                       String episodeNo=episodeno;
//                       String copytype=copyType;
//                        msg = etMsgReport.getText().toString();
//                        String reportId=report_id;
//
//
//
//                        if (TextUtils.isEmpty(etFullName.getText().toString())){
//                           full_name=" ";
//                        }else {
//                            full_name = etFullName.getText().toString();
//                        }
//
//                        if (TextUtils.isEmpty(etNationalId.getText().toString())){
//                            nationality_id=" ";
//                        }else {
//                            nationality_id = etNationalId.getText().toString();
//                        }
//
//                        if (TextUtils.isEmpty(etDob.getText().toString())){
//                            dob=" ";
//                        }else {
//                            dob = etDob.getText().toString();
//                        }
//                        String info_value=info;
//                        String terms_value=terms;
//
//                        callRequestReport(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
//                                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                episode_type,
//                                episodeno,
//                                copyType,
//                                msg,
//                                report_id,
//                                full_name,
//                                nationality_id,
//                                dob,
//                                info, terms,
//                                path_id);
//
//                        Log.e("REQUEST_DATA", "REQUEST_DATA" +
//                                id+""+token+" "
//                                + episodeType + " " + episodeNo + " " +
//                                copytype + " " + msg + " " + reportId + " " +
//                               full_name + " " +
//                               dob + " " + nationality_id + " " +
//                                info_value + " " + terms_value + " " + path_id);
//
//
//                    } else {
//                        layBtnSend.setEnabled(false);
//                        // Toast.makeText(ContactUsActivity.this,"NOTHING IS FINE",Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//                break;
//        }
//    }
//
//    // Add validations to all field values.
//    boolean isValid() {
//        boolean valid = true;
//
//
//        Validations validation = new Validations(this);
//
//        if (!validation.isEmailValid(etEmailReport)) {
//            valid = false;
//        }
//
//        if (!validation.isPhoneValid(etPhoneReport)) {
//            valid = false;
//        }
//
//        if (rdRecentVisitReport.isChecked()) {
//            if (tvBookingId.getText().toString().trim().length() == 0 || tvBookingId.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//                makeToast(getString(R.string.select_appon_date_id));
//                valid = false;
//            }
//        }
//
//        if (etMsgReport.getText().toString().trim().length() == 0) {
//            etMsgReport.setError(getString(R.string.required));
//            valid = false;
//        }
//
//
//        if (tvReportType.getText().toString().trim().length() == 0 || tvReportType.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
//            makeToast(getString(R.string.select_report_type));
//            valid = false;
//        }
//
//        if (path_id.length() == 0 && translate.equalsIgnoreCase("yes")) {//rdRequestTransCopy.isChecked()) {
//            makeToast(getString(R.string.request_translation_file));
//            valid = false;
//        }
//
//        if (!valid)
//            layBtnSend.setEnabled(true);
//
//        return valid;
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//        ContactUsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
//
//    boolean validAll() {
//        Validations validation = new Validations(this);
//        boolean msgValid = validation.isMsgValid(etMsg);
//        boolean enName = validation.isNameValid(etName);
//        boolean emailValid = false;
//
//        if (etEmail.getText().toString().trim().isEmpty()) {
//            emailValid = true;
//        } else
//            emailValid = validation.isEmailValid(etEmail);
//
//        boolean phone = validation.isPhoneValid(etPhone);
//
//        if (msgValid && enName && phone && emailValid) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//    // send a callback request
//    public void callContactUs(String name, String email, String phone, String title, String
//            message, String mrNumber) {
//        Common.showDialog(this);
//        JSONObject object = new JSONObject();
//        try {
//            object.put("name", name);
//            object.put("email", email);
//            object.put("phone", phone);
//            object.put("title", title);
//            object.put("message", message);
//            object.put("mrNumber", mrNumber);
//            object.put("platform", "Android");//mobile
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
//        WebService webService = ServiceGenerator.createService(WebService.class);
//        Call<SimpleResponse> xxx = webService.contactUsWeb(body);
//
//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
//                            et_title.setText("");
//                            etMsg.setText("");
//                            etName.setText("");
//                            etPhone.setText("");
//                            etEmail.setText("");
//                            onBackPressed();
//                        } else {
//                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
//                        }
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//
//    // get all booking ids and details
//    public void callGetAllBookingsApi(String mrNumber, String token) {
//        Common.showDialog(this);
//        JSONObject object = new JSONObject();
//        try {
//            object.put("mrNumber", mrNumber);
//            object.put("fromDate", "");
//            object.put("toDate", "");
//            object.put("clinicCode", "");
//            object.put("physicianCode", "");
//            object.put("bookingStatus", Constants.BookingStatus.COMPLETED);
//            object.put("itemCount", "100");
//            object.put("page", "0");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<BookingResponse> xxx = webService.getAllBookings(body);
//        xxx.enqueue(new Callback<BookingResponse>() {
//            @Override
//            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    BookingResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            ContactUsActivity.this.response = response1;
//                            showListPopUp(tvBookingId, "id", tvBookingId.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
//                        }
//
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<BookingResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//
//    // get all Reports
//    public void callAllReportsApi(String token) {
//        Common.showDialog(this);
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<ReportTypeResponse> xxx = webService.getReportTypes();
//
//        xxx.enqueue(new Callback<ReportTypeResponse>() {
//            @Override
//            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    ReportTypeResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            ContactUsActivity.this.reportTypeResponse = response1;
//                            showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
//                        }
//
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//
//    // get selected visit type
////    public void callSelectedVisitApi(String token) {
////        Common.showDialog(this);
////        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
////        Call<Select_TypeResponse> xxx = webService.getSelecteVisit();
////        xxx.enqueue(new Callback<Select_TypeResponse>() {
////            @Override
////            public void onResponse(Call<Select_TypeResponse> call, Response<Select_TypeResponse> response) {
////                Common.hideDialog();
////                if (response.isSuccessful()) {
////                 //   Select_TypeResponse response1 = response.body();
////                      //  ContactUsActivity.this.Select_TypeResponse = response1;
////                      //  showListSelectedVisitPopUp(tvSelectVisit, "",tvSelectVisit.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.Select_TypeResponse);
////                        Log.e("SUCCES_RESPONSE","SUCCESS_RESPONSE" +"" );
////                }
////
////                layBtnSend.setEnabled(true);
////
////            }
////
////            @Override
////            public void onFailure(Call<Select_TypeResponse> call, Throwable t) {
////                Common.hideDialog();
////                Common.makeToast(ContactUsActivity.this, t.getMessage());
////                layBtnSend.setEnabled(true);
////            }
////        });
////
////    }
//
//    private void callSelectedVisitApi(String token) {
//        Log.i("autolog", "getUserList");
//        try {
//            String url = "https://patientportal.imc.med.sa/imc_portal/";
//            Log.i("autolog", "https://patientportal.imc.med.sa/imc_portal/");
//            Retrofit retrofit = null;
//            Log.i("autolog", "retrofit");
//
//            if (retrofit == null) {
//                retrofit = new Retrofit.Builder()
//                        .baseUrl(url)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                Log.i("autolog", "build();");
//            }
//            //  WebService service = retrofit.create(WebService.class);
//            //WebService webService = retrofit.create(WebService.class);
//            WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//            Log.i("autolog", " APIService service = retrofit.create(APIService.class);");
//            Call<List<Select_TypeResponse>> call = webService.getSelecteVisit();
//            Log.i("autolog", " Call<List<Select_TypeResponse>> call = webService.getSelecteVisit();;");
//
//            call.enqueue(new Callback<List<Select_TypeResponse>>() {
//                @Override
//                public void onResponse(Call<List<Select_TypeResponse>> call, Response<List<Select_TypeResponse>> response) {
//                    Log.i("autolog", "onResponse");
//                    userList = response.body();
////                    Log.i("autolog", "List<User> userList = response.body();");
//                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
////                    Log.i("autolog", "RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler);");
////                    layoutManager = new LinearLayoutManager(ContactUsActivity.this);
////                    Log.i("autolog", "layoutManager = new LinearLayoutManager(MainActivity.this);");
////                    recyclerView.setLayoutManager(layoutManager);
////                    Log.i("autolog", "recyclerView.setLayoutManager(layoutManager);");
////                    RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(), userList);
////                    Log.i("autolog", "RecyclerViewAdapter recyclerViewAdapter =new RecyclerViewAdapter(getApplicationContext(), userList);");
////                    recyclerView.setAdapter(recyclerViewAdapter);
////                    Log.i("autolog", "recyclerView.setAdapter(recyclerViewAdapter);");
//
//
//                    int popupwindowHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    LayoutInflater layoutInflater = (LayoutInflater) getActivityContext()
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View layout = layoutInflater.inflate(
//                            R.layout.dialog_custom_list, null);
//
//                    pwindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    pwindow.setHeight(popupwindowHeight);
//                    pwindow.setFocusable(true);
//                    pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            //TODO dismiss settings
//                        }
//                    });
//                    RecyclerView landmarkRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_list);
////                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivityContext(), userList);
////                  //  landmarkRecyclerView.setAdapter(new RecyclerViewAdapter(getActivityContext(),userList));
////
////                 //   SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, list, lastValue);
////                    landmarkRecyclerView.setHasFixedSize(true);
////                    landmarkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
////                    landmarkRecyclerView.setAdapter(adapter);
//
//
//                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivityContext(), userList);
//                    landmarkRecyclerView.setHasFixedSize(true);
//                    landmarkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
//                    landmarkRecyclerView.setAdapter(adapter);
//                    adapter.setOnItemClickListener(onItemClickListener);
//                    //adapter.setOnItemClickListener(onItemClickListener);
//                    tvSelectVisit.setText("Select Type");
//                    pwindow.dismiss();
//                    pwindow.setWindowLayoutMode(
//                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                    pwindow.setHeight(1);
//                    pwindow.setWidth(1);
//                    // pwindow.showAtLocation(layout, Gravity.NO_GRAVITY,25, mPoint.y);
//                    pwindow.showAsDropDown(recyclerView, 2, 2);
//                }
//
//                @Override
//                public void onFailure(Call<List<Select_TypeResponse>> call, Throwable t) {
//                    Log.i("autolog", t.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            Log.i("autolog", "Exception");
//        }
//    }
//
//
//    // get reports list can be translate
//    public void callTranslaionReportsApi(String token) {
//        Common.showDialog(this);
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<ReportTypeResponse> xxx = webService.getTransaltionReportTypes();
//
//        xxx.enqueue(new Callback<ReportTypeResponse>() {
//            @Override
//            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    ReportTypeResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
////                            if (rdRequestTransCopy.isChecked()) {
////                                ContactUsActivity.this.reportTypeTrasnsResponse = response1;
////                                showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeTrasnsResponse);
////                            } else {
//                            ContactUsActivity.this.reportTypeResponse = response1;
//                            showListPopUp(tvReportType, "report", tvReportType.getText().toString(), ContactUsActivity.this.response, ContactUsActivity.this.reportTypeResponse);
////                            }
//                        }
//
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//
//            }
//
//            @Override
//            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//
//    // send a callback request
//    public void callRequestReport(String mrNumber, String token, String episode_type,
//                                  String episode_no, String copyType, String message, String reportTypeId,
//                                  String fullname, String nationalid,
//                                  String dob, String info, String trems, String file) {
//        Common.showDialog(this);
//        JSONObject object = new JSONObject();
////        try {
////            object.put("mrNumber", mrNumber);
////            object.put("episode", book_id);
////
////            if (date != null)
////                if (date.length() > 0)
////                    object.put("appointmentDate", Common.getConvertToDateRequest(date));
////                else
////                    object.put("appointmentDate", "");
////            else
////                object.put("appointmentDate", "");
////
////            object.put("reportType", report_type);
////            object.put("email", email);
////            object.put("subject", subject);
////            object.put("copyType", copyType);
////            object.put("message", message);
////            object.put("isGeneral", isGeneral);
////            object.put("phoneNumber", phoneNumber);
////            object.put("reportTypeId", reportTypeId);
//////            file
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//        RequestBody mrNumber1 = RequestBody.create(MediaType.parse("text/plain"), mrNumber);
//        // RequestBody episode = null, appointmentDate = null;
////        if (book_id != null) {
////            episode = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(book_id));
////        } else
////            episode = RequestBody.create(MediaType.parse("text/plain"), "");
////
////        if (date != null) {
////            appointmentDate = RequestBody.create(MediaType.parse("text/plain"), Common.getConvertToDateRequest(date));
////        } else
////            appointmentDate = RequestBody.create(MediaType.parse("text/plain"), "");
//
//        RequestBody episodetype = RequestBody.create(MediaType.parse("text/plain"), episode_type);
//        RequestBody episodeno = RequestBody.create(MediaType.parse("text/plain"), episode_no);
//
//        RequestBody copyType1 = RequestBody.create(MediaType.parse("text/plain"), copyType);
//        RequestBody message1 = RequestBody.create(MediaType.parse("text/plain"), message);
//        ;
//        RequestBody reporttypeid = RequestBody.create(MediaType.parse("text/plain"), reportTypeId);
//        RequestBody full_name = RequestBody.create(MediaType.parse("text/plain"), fullname);
//        RequestBody d_ob = RequestBody.create(MediaType.parse("text/plain"), dob);
//        RequestBody national_id = RequestBody.create(MediaType.parse("text/plain"), nationalid);
//        RequestBody in_fo = RequestBody.create(MediaType.parse("text/plain"), info);
//        RequestBody term = RequestBody.create(MediaType.parse("text/plain"), trems);
//
//        MultipartBody.Part file1 = null;
//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<SimpleResponse> xxx;
//        if (path_id.length() > 0)
//            file1 = prepareImgFilePart(file, "file");
//
//
//        xxx = webService.requestForReport(mrNumber1, episodetype, episodeno,
//                copyType1, message1, reporttypeid, full_name, d_ob, national_id, in_fo, term, file1);
//
//
//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//                            alertBack(response1.getMessage());
//                        } else {
//                            Common.makeToast(ContactUsActivity.this, response1.getMessage());
//                        }
//                    }
//                }
//
//                layBtnSend.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                Common.hideDialog();
//                Common.makeToast(ContactUsActivity.this, t.getMessage());
//                layBtnSend.setEnabled(true);
//            }
//        });
//
//    }
//
//    // Pop up list to select booking id or date or report type
//    public void showListPopUp(TextView view, String type, String lastValue, BookingResponse
//            response, ReportTypeResponse reportTypeResponse) {
//        List<String> list = new ArrayList<>();
//        if (type.equalsIgnoreCase("id") && response != null) {
//
//            for (int i = 0; i < response.getBookings().size(); i++) {
//                list.add(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()));
//            }
//        }
//
//        if (type.equalsIgnoreCase("report") && reportTypeResponse != null) {
//            for (int i = 0; i < reportTypeResponse.getReports().size(); i++) {
//                list.add(reportTypeResponse.getReports().get(i).getReportType());
//            }
//        }
//
//        view.setEnabled(false);
//        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = layoutInflater.inflate(R.layout.dialog_custom_list, null);
//        final PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                view.getWidth(),
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
//
//        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);
//
//        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, list, lastValue);
//        recycler_list.setHasFixedSize(true);
//        recycler_list.setLayoutManager(new LinearLayoutManager(this));
//        recycler_list.setAdapter(adapter);
//        adapter.setOnItemClickListener(position -> {
//            view.setEnabled(true);
//            view.setText(list.get(position));
//            if (type.equalsIgnoreCase("report")) {
//                report_id = String.valueOf(reportTypeResponse.getReports().get(position).getCode());
//                translate = String.valueOf(reportTypeResponse.getReports().get(position).getIsTranslaionReport());
//
//                if (reportTypeResponse.getReports().get(position).getIsTranslaionReport().equalsIgnoreCase("yes")) {
//                    layFile.setVisibility(View.VISIBLE);
//                } else {
//                    layFile.setVisibility(View.GONE);
//
//                }
//            }
//            popupWindow.dismiss();
//        });
//
//        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
//        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()
//
//    }
//
//    //1
//    public void showListSelectedVisitPopUp(TextView view, String type, String lastValue, BookingResponse
//            response, Select_TypeResponse selelctvistresponse) {
//        List<String> list = new ArrayList<>();
//        Log.e("ADAPTER_CONTACT_LIST", selelctvistresponse + "");
//        if (type.equalsIgnoreCase("id") && response != null) {
//            for (int i = 0; i < response.getBookings().size(); i++) {
//                list.add(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()));
//            }
//        }
//
//        if (selelctvistresponse != null) {
//            //  for (int i = 0; i < selelctvistresponse.getSelect_visit().size(); i++) {
//            list.add(selelctvistresponse.getEpisodeKey());
//            // }
//        }
//        view.setEnabled(false);
//        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = layoutInflater.inflate(R.layout.dialog_custom_list, null);
//        final PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                view.getWidth(),
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
//        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);
//        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, list, lastValue);
//        recycler_list.setHasFixedSize(true);
//        recycler_list.setLayoutManager(new LinearLayoutManager(this));
//        recycler_list.setAdapter(adapter);
//        adapter.setOnItemClickListener(position -> {
//            view.setEnabled(true);
//            view.setText(list.get(position));
//            // report_id1 = String.valueOf(selelctvistresponse.getSelect_visit().get(position).getEpisodeKey());
//            report_id1 = String.valueOf(selelctvistresponse.getEpisodeKey());
//
//            Log.e("DATA_IS", report_id1);
//            popupWindow.dismiss();
//        });
//
//        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
//        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()
//
//    }
//
//
//    public int getSelectedPosition(TextView view, BookingResponse response) {
//        int pos = -1;
//        String value = view.getText().toString();
//        if (response != null)
//            for (int i = 0; i < response.getBookings().size(); i++) {
//                if (value.equalsIgnoreCase(response.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response.getBookings().get(i).getApptDateString()))) {
//                    pos = i;
//                }
//
//            }
//        return pos;
//
//    }
//
//    // Alert dialog
//    public void alertBack(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        tvReportType.setText(getString(R.string.select));
//                        tvBookingId.setText(getString(R.string.select));
//                        etMsgReport.setText("");
//                        etEmailReport.setText("");
//                        onBackPressed();
//
//                    }
//                });
//
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dlg) {
//
//                try {
//                    Typeface typeface = ResourcesCompat.getFont(ContactUsActivity.this, R.font.font_app);
//                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
//
//                    if (SharedPreferencesUtils.getInstance(ContactUsActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
//                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
//                    } else {
//                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        alertDialog.show();
//
//    }
//
//    @NonNull
//    private MultipartBody.Part prepareImgFilePart(String filee, String name) {
//        File file = new File(filee);
//        String mime = FileUtils.getMimeType(file);
//        Log.e("mime", "" + mime);
//        RequestBody requestFile = RequestBody.create(MediaType.parse(mime), file);
//        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
//            // Get the Uri of the selected file
//            Uri uri = data.getData();
//            logger("pic", "File Uri: " + uri.toString());
//            // Get the path
//            String path = null;
//            path = FileUtil.getRealPath(this, uri);
//            if (path != null) {
//                File file = new File(path);
//                long fileSizeInBytes = file.length();
//                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//                long fileSizeInKB = fileSizeInBytes / 1024;
//                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                long fileSizeInMB = fileSizeInKB / 1024;
//
//                if (fileSizeInKB <= 2200) {
//                    tv_uploaded_id_file_name.setText(file.getName());
//                    path_id = path;
//                    tv_uploaded_id_file_name.setVisibility(View.VISIBLE);
//
//                } else {
//                    makeToast(getString(R.string.max_file_size));
//                }
//                logger("pic", "File Path: " + path);
//            }
//            // Get the file instance
//            // File file = new File(path);
//            // Initiate the upload
//        }
//    }
//
//    @SuppressLint("Recycle")
//    public String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = {"_data"};
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = 0;
//                if (cursor != null) {
//                    column_index = cursor.getColumnIndexOrThrow("_data");
//                    if (cursor.moveToFirst()) {
//                        return cursor.getString(column_index);
//                    }
//                }
//
//            } catch (Exception e) {
//                // Eat it
//            }
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//
//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//
////        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),
//                    FILE_SELECT_CODE);
//        } catch (ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // check permission to save reports required storage permission
//    void checkPermissionFirst() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                showFileChooser();
//
//            } else {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE}, 33);
//            }
//        } else {
//            showFileChooser();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case 33: {
//                if (grantResults.length > 0) {
//                    showFileChooser();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onItemClick(View view, int position) {
//
//    }
//}
