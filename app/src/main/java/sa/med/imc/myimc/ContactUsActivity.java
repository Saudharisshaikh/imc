//package sa.med.imc.myimc;
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
//import java.util.ArrayList;
//import java.util.List;
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
//    String episode_type,episodeno;
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
//    //etDob
//    @BindView(R.id.etDob)
//    EditText etDob;
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
//            tvSelectVisit.setText(thisItem.getApptDate() + "-" + thisItem.getConsultant());
//            episode_type=thisItem.getEpisodeType();
//            episodeno=thisItem.getEpisodeNo();
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
//
//        ButterKnife.bind(this);
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
////        rdRequestTransCopy.setOnCheckedChangeListener((buttonView, isChecked) -> {
////            if (buttonView.isPressed()) {
////                if (isChecked) {
////                    tvEspiseTitle.setVisibility(View.GONE);
////                    tvBookingId.setVisibility(View.GONE);
////                    layFile.setVisibility(View.VISIBLE);
////                    tvReportType.setText(getString(R.string.select));
////                }
////            }
////        });
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
//    @OnClick({R.id.checkbox, R.id.checkbox_submitted, R.id.tv_upload_report, R.id.iv_back, R.id.lay_btn_send, R.id.tv_report_type, R.id.tv_select_visit, R.id.tv_booking_id, R.id.tv_booking_date})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//
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
////                if (rdRequestTransCopy.isChecked()) {
////                    if (reportTypeTrasnsResponse == null) {
////                        callTranslaionReportsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
////                    } else
////                        showListPopUp(tvReportType, "report", tvReportType.getText().toString(), response, reportTypeTrasnsResponse);
////
////                } else {
//                if (reportTypeResponse == null) {
//                    callAllReportsApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
//                } else
//                    showListPopUp(tvReportType, "report", tvReportType.getText().toString(), response, reportTypeResponse);
////                }
//                break;
//            case R.id.tv_select_visit:
//                Log.e("CLICKED", "Hello");
//                //  if (Select_TypeResponse == null) {
//                callSelectedVisitApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""));
////                } else
////                showListSelectedVisitPopUp(tvSelectVisit,"", tvSelectVisit.getText().toString(), response, Select_TypeResponse);
////                }
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
//                layBtnSend.setEnabled(false);
////                if (getIntent().hasExtra("type"))
////                {
//                    if (isValid()) {
//                        layBtnSend.setEnabled(false);
//                       // int po = getSelectedPosition(tvBookingId, response);
//                        String copyType = "";
//                        String isGen = "";
//                        Integer id = null;
//                        String date = null;
//                        String terms = null;
//                        String info = null;
//                        if (rdPhysicalCopy.isChecked()) {
//                            //  copyType = "Physical";
//                            copyType = "pickup";
//
//                        } else if (rdScannedCopy.isChecked()) {
//                            copyType = "email";
//                        }
////                        if (rdGeneral.isChecked()) {
////                            isGen = "1";
////                        } else if (rdRecentVisitReport.isChecked()) {
////                            isGen = "0";
//////                        } else if (rdRequestTransCopy.isChecked()) {
//////                            isGen = "2";
////                        } else {
////                            isGen = "0";
////                        }
//
////                        if (po > -1) {
////                            id = response.getBookings().get(po).getId();
////                            date = response.getBookings().get(po).getApptDateString();
////                        }
//// && checkbox_submitted.isChecked()
//
//                        if (checkbox.isChecked()) {
//                            terms = "true";
//                        } else {
//                            terms = "false";
//                        }
//                        if (checkbox_submitted.isChecked()) {
//                            info = "true";
//                        } else {
//                            info = "false";
//                        }
//
//                        if (terms.equalsIgnoreCase("true") && info.equalsIgnoreCase("true")) {
//                            layBtnSend.setEnabled(true);
//
//                            String one = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "");
//                            String two = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "");
//                            String three = tvReportType.getText().toString();
//                            String four = episode_type;
//                            String five = episodeno;
//                            String six = date;
//                            String seven = etTitleReport.getText().toString();
//                            String eight = etMsgReport.getText().toString();
//                            String nine = copyType;
//                            String ten = isGen;
//                            String ele = etPhoneReport.getText().toString();
//                            String twe = etFullName.getText().toString();
//                            String thir = etNationalId.getText().toString();
//                            String fourteen = etDob.getText().toString();
//                            String fif = report_id;
//                            String sixteen = null;
//                            String seventeen = terms;
//                            String eighteen = info;
////                            Log.e("Sending_Data", one);
////                            Log.e("Sending_Data", two);
////                            Log.e("Sending_Data", three);
////                            Log.e("Sending_Data", four);
////                            Log.e("Sending_Data", five);
////                            Log.e("Sending_Data", six);
////                            Log.e("Sending_Data", seven);
////                            Log.e("Sending_Data", eight);
////                            Log.e("Sending_Data", nine);
////                            Log.e("Sending_Data", ten);
////                            Log.e("Sending_Data", ele);
////                            Log.e("Sending_Data", twe);
////                            Log.e("Sending_Data", thir);
////                            Log.e("Sending_Data", fourteen);
////                            Log.e("Sending_Data", fif);
////                            Log.e("Sending_Data", sixteen);
////                            Log.e("Sending_Data", seventeen);
////                            Log.e("Sending_Data", eighteen);
//                                   /* + two + three +four + five + six + seven + eight + nine + ten + ele + twe + thir + fourteen
//                                    + fif+sixteen+seventeen + eighteen );*/
//
//
//                            callRequestReport(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
//                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    episode_type,
//                                    episodeno,
//                                    copyType,
//                                    etMsgReport.getText().toString(),
//                                    report_id,
//                                   etFullName.getText().toString(),
//                                    etNationalId.getText().toString(),
//                           etDob.getText().toString(),
//                                    info,terms,
//                                   path_id);
////  tvReportType.getText().toString(),
//                            /*  etEmailReport.getText().toString(),
//                                    id,
//                                    date,
//                                    etTitleReport.getText().toString(),
//
//                                    isGen,
//                                    etPhoneReport.getText().toString(),
//                                    etFullName.getText().toString(),
//                                    etNationalId.getText().toString(),
//                                    etDob.getText().toString(),
//                                    report_id,*/
//                            // Toast.makeText(ContactUsActivity.this,"ALL FINE",Toast.LENGTH_LONG).show();
//                        }
//                        else
//                            {
//                            layBtnSend.setEnabled(false);
//                            // Toast.makeText(ContactUsActivity.this,"NOTHING IS FINE",Toast.LENGTH_LONG).show();
//                        }
////                            String one=SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "");
////                            String two=SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "");
////                            String three=  tvReportType.getText().toString();
////                            String four=etEmailReport.getText().toString();
////                            int five= id;
////                            String six= date;
////                            String seven= etTitleReport.getText().toString();
////                            String eight= etMsgReport.getText().toString();
////                            String nine=copyType ;
////                            String ten=isGen ;
////                            String ele=etPhoneReport.getText().toString() ;
////                            String twe=etFullName.getText().toString() ;
////                            String thir=etNationalId.getText().toString() ;
////                            String fourteen= etDob.getText().toString();
////                            String fif= report_id;
////                            String sixteen= path_id;
////                            Log.e("Sending_Data",one+ two + three+ four + five+ six + seven + eight+nine+ ten+ele+ twe+thir+fourteen
////                            +fif);
//                        // call api to send request for report...
////                            callRequestReport(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
////                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
////                                    tvReportType.getText().toString(),
////                                    tvSelectVisit.getText().toString(),
////                                    etEmailReport.getText().toString(),
////                                    id,
////                                    date,
////                                    etTitleReport.getText().toString(),
////                                    etMsgReport.getText().toString(),
////                                    copyType,
////                                    isGen,
////                                    etPhoneReport.getText().toString(),
////                                    etFullName.getText().toString(),
////                                    etNationalId.getText().toString(),
////                                    etDob.getText().toString(),
////                                    report_id, path_id);
//                        // return;
//                        //   }
//
//                        // else {
//
//                        //   Toast.makeText(ContactUsActivity.this,"PLEASE ACCEPT PERMISSIONS", Toast.LENGTH_LONG).show();
//                        // }
//                        // call api to send request for report...
//
//                    }
//               // }
////                else
////                    {
////                    if (validAll()) {
////                        layBtnSend.setEnabled(false);
////
////                        // call api to send request for callback...
////                        callContactUs(etName.getText().toString(),
////                                etEmail.getText().toString(),
////                                etPhone.getText().toString(),
////                                et_title.getText().toString(),
////                                etMsg.getText().toString(), SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""));
////                    } else
////                        layBtnSend.setEnabled(true);
////
////                }
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
//                                  String episode_no,String copyType,  String message,String reportTypeId,
//                                  String fullname, String nationalid,
//                                  String dob, String info,String trems ,String file) {
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
//       // RequestBody episode = null, appointmentDate = null;
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
//        RequestBody message1 = RequestBody.create(MediaType.parse("text/plain"), message);;
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
////        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());
//
//        /*if (path_id.length() > 0)
//        {
//            xxx = webService.requestForReport(mrNumber1, episodetype, episodeno,
//                    copyType1,message1, reporttypeid, full_name, d_ob,national_id,in_fo,term, file1);
//
//        }
//               else
//        {
//            xxx = webService.requestForReport(mrNumber1, episodetype, episodeno,
//                    copyType1,message1, reporttypeid, full_name, d_ob,national_id,in_fo,term, file1);
//*//*webService.requestForReport(mrNumber1, episode, appointmentDate,
//                    reportType, email1, subject1, copyType1,
//                    message1, isGeneral1, phoneNumber1, reportTypeId1); *//*
//        }*/
//        xxx = webService.requestForReport(mrNumber1, episodetype, episodeno,
//                copyType1,message1, reporttypeid, full_name, d_ob,national_id,in_fo,term, file1);
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
