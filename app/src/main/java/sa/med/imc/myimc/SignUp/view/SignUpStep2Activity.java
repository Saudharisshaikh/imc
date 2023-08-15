package sa.med.imc.myimc.SignUp.view;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Adapter.SimpleSearchRecyclerViewAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.IDNameModel;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.model.CityResponse;
import sa.med.imc.myimc.SignUp.model.CompaniesResponse;
import sa.med.imc.myimc.SignUp.model.DistrictResponse;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.model.UserDetailModel;
import sa.med.imc.myimc.SignUp.presenter.SignUpImpl;
import sa.med.imc.myimc.SignUp.presenter.SignUpPresenter;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.FileUtil;
import sa.med.imc.myimc.splash.SplashActivity;
import test.jinesh.captchaimageviewlib.CaptchaImageView;

public class SignUpStep2Activity extends BaseActivity implements SignUpViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.tv_upload_id)
    TextView tvUploadId;
    @BindView(R.id.tv_uploaded_id_file_name)
    TextView tvUploadedIdFileName;
    @BindView(R.id.rd_yes)
    RadioButton rdYes;
    @BindView(R.id.rd_no)
    RadioButton rdNo;
    @BindView(R.id.tv_insurance_company)
    TextView tvInsuranceCompany;
    @BindView(R.id.et_policy_number)
    EditText etPolicyNumber;
    @BindView(R.id.tv_insurance_expiry_date)
    TextView tvInsuranceExpiryDate;
    @BindView(R.id.et_class_plan)
    EditText etClassPlan;
    @BindView(R.id.tv_upload_insurance_id)
    TextView tvUploadInsuranceId;
    @BindView(R.id.tv_uploaded_insurance_file_name)
    TextView tvUploadedInsuranceFileName;
    @BindView(R.id.lay_mrn)
    LinearLayout layMrn;
    @BindView(R.id.lay)
    RelativeLayout lay;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.lay_btn_back)
    LinearLayout layBtnBack;
    @BindView(R.id.lay_btn_register)
    LinearLayout layBtnRegister;
    @BindView(R.id.text_capt)
    TextView textCapt;
    @BindView(R.id.et_input_captcha)
    EditText etInputCaptcha;
    @BindView(R.id.captcha_image)
    CaptchaImageView captchaImage;
    @BindView(R.id.iv_regenerate)
    ImageView ivRegenerate;
    @BindView(R.id.capt_lay)
    LinearLayout captLay;

    Bitmap path_id=null;
    Bitmap path_insurance_card=null;
    String path = "";

    String type = "", hasInsurance = "no", company_id = "";

    private static final int FILE_SELECT_CODE = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @BindView(R.id.lay_insurance_detail)
    LinearLayout layInsuranceDetail;
    CompaniesResponse companiesResponse;

    SignUpPresenter presenter;
    @BindView(R.id.et_membership_id)
    EditText etMembershipId;
    @BindView(R.id.et_submitter_name)
    EditText etSubmitterName;
    @BindView(R.id.tv_relation)
    TextView tvRelation;
    @BindView(R.id.checkbox_concent)
    CheckBox checkboxConcent;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignUpStep2Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_sign_up_step_ii);
        ButterKnife.bind(this);
        presenter = new SignUpImpl(this, this);
        checkbox.setText(Html.fromHtml(getString(R.string.agree_to_policy)));
        checkbox.setMovementMethod(LinkMovementMethod.getInstance());

        captchaImage.setCaptchaLength(4);
        captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS);

        rdYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (buttonView.isChecked())
                    layInsuranceDetail.setVisibility(View.VISIBLE);
            }
        });
        rdNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (buttonView.isChecked())
                    layInsuranceDetail.setVisibility(View.GONE);
            }
        });
    }


    // check permission to save reports required storage permission
    void checkPermissionFirst(int typew) {
        if (typew == 1) {
            type = "id";
        } else {
            type = "insu";
        }

        selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 33: {
                if (grantResults.length > 0) {
                    selectImage();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        SignUpStep2Activity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    // Show alert to agree to terms.
    void acceptAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.agree_to_terms))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Show alert to agree to terms.
    void acceptConsentAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.agree_to_consent))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @OnClick({R.id.tv_conesnt, R.id.tv_relation, R.id.iv_regenerate, R.id.iv_back, R.id.tv_upload_id, R.id.tv_insurance_expiry_date, R.id.tv_insurance_company, R.id.tv_upload_insurance_id, R.id.lay_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_conesnt:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(WebUrl.CONSENT_SIGNUP_LIVE));
                startActivity(i);
                break;

            case R.id.tv_relation:
                showListPopUp(tvRelation, "r", tvRelation.getText().toString());
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_insurance_expiry_date:
                Common.getDateFromPickerCU(tvInsuranceExpiryDate, this);
                break;

            case R.id.tv_insurance_company:
                if (companiesResponse == null) {
                    presenter.callGetCompaniesApi(SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else
                    showListPopUp(tvInsuranceCompany, tvInsuranceCompany.getText().toString(), companiesResponse);

                break;

            case R.id.tv_upload_id:
                type = "id";
                checkPermissionFirst(1);
                break;

            case R.id.tv_upload_insurance_id:
                type = "insu";
                checkPermissionFirst(2);
                break;

            case R.id.lay_btn_register:
                layBtnRegister.setEnabled(false);
                Intent intent = getIntent();

                String lang_s = "E";
                String gender_s = "";

                if (intent.getStringExtra(Constants.GUEST.GENDER).equalsIgnoreCase(getString(R.string.male))) {
                    gender_s = "M";
                } else if (intent.getStringExtra(Constants.GUEST.GENDER).equalsIgnoreCase(getString(R.string.female))) {
                    gender_s = "F";
                } else if (intent.getStringExtra(Constants.GUEST.GENDER).equalsIgnoreCase(getString(R.string.unspecified))) {
                    gender_s = "U";
                }


                if (intent.getStringExtra(Constants.GUEST.LANG).equalsIgnoreCase(getString(R.string.arabic_lang))) {
                    lang_s = "A";
                } else {
                    lang_s = "E";
                }
                if (isValidWithCaptcha())
                    if (checkbox.isChecked()) {
                        if (checkboxConcent.isChecked()) {
                            if (hasInsurance.equalsIgnoreCase("no"))
                                presenter.callSinUpAPI(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                        intent.getStringExtra(Constants.GUEST.IQAMA_ID),
                                        intent.getStringExtra(Constants.GUEST.DOC_TYPE),
                                        intent.getStringExtra(Constants.GUEST.DOC_EXP_DATE),
                                        intent.getStringExtra(Constants.GUEST.FIRST_NAME),
                                        intent.getStringExtra(Constants.GUEST.FIRST_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.FAMILY_NAME),
                                        intent.getStringExtra(Constants.GUEST.FAMILY_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.FATHER_NAME),
                                        intent.getStringExtra(Constants.GUEST.FATHER_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.GRAND_FATHER_NAME),
                                        intent.getStringExtra(Constants.GUEST.GRAND_FATHER_NAME_AR),
                                        gender_s,
                                        intent.getStringExtra(Constants.GUEST.PHONE),
                                        intent.getStringExtra(Constants.GUEST.HOME_PHONE),
                                        intent.getStringExtra(Constants.GUEST.DISTRICT),
                                        intent.getStringExtra(Constants.GUEST.CITY),
                                        intent.getStringExtra(Constants.GUEST.COUNTRY),
                                        intent.getStringExtra(Constants.GUEST.NATIONALITY),
                                        intent.getStringExtra(Constants.GUEST.DOB),
                                        lang_s,
                                        intent.getStringExtra(Constants.GUEST.ER_FIRST_NAME),
                                        intent.getStringExtra(Constants.GUEST.ER_FAMILY_NAME),
                                        intent.getStringExtra(Constants.GUEST.ER_PHONE),
                                        intent.getStringExtra(Constants.GUEST.REASON_VISIT),
                                        hasInsurance,
                                        "",//insuranceCompany
                                        "",//CompanyCode
                                        "",//idCard
                                        "",//policyNumber
                                        "",//insuranceMemberId
                                        "",//insuranceExpDate
                                        "",//insuranceClass
                                        "",//insuranceAttachment
                                        etSubmitterName.getText().toString(),
                                        tvRelation.getText().toString(),
                                        path_id,
                                        path_insurance_card,
                                        intent.getStringExtra(Constants.GUEST.MARTIAL_STATUS),
                                        SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                            else

                                presenter.callSinUpAPI(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                        intent.getStringExtra(Constants.GUEST.IQAMA_ID),
                                        intent.getStringExtra(Constants.GUEST.DOC_TYPE),
                                        intent.getStringExtra(Constants.GUEST.DOC_EXP_DATE),
                                        intent.getStringExtra(Constants.GUEST.FIRST_NAME),
                                        intent.getStringExtra(Constants.GUEST.FIRST_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.FAMILY_NAME),
                                        intent.getStringExtra(Constants.GUEST.FAMILY_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.FATHER_NAME),
                                        intent.getStringExtra(Constants.GUEST.FATHER_NAME_AR),
                                        intent.getStringExtra(Constants.GUEST.GRAND_FATHER_NAME),
                                        intent.getStringExtra(Constants.GUEST.GRAND_FATHER_NAME_AR),
                                        gender_s,
                                        intent.getStringExtra(Constants.GUEST.PHONE),
                                        intent.getStringExtra(Constants.GUEST.HOME_PHONE),
                                        intent.getStringExtra(Constants.GUEST.DISTRICT),
                                        intent.getStringExtra(Constants.GUEST.CITY),
                                        intent.getStringExtra(Constants.GUEST.COUNTRY),
                                        intent.getStringExtra(Constants.GUEST.NATIONALITY),
                                        intent.getStringExtra(Constants.GUEST.DOB),
                                        lang_s,
                                        intent.getStringExtra(Constants.GUEST.ER_FIRST_NAME),
                                        intent.getStringExtra(Constants.GUEST.ER_FAMILY_NAME),
                                        intent.getStringExtra(Constants.GUEST.ER_PHONE),
                                        intent.getStringExtra(Constants.GUEST.REASON_VISIT),
                                        hasInsurance,
                                        tvInsuranceCompany.getText().toString(),//insuranceCompany
                                        company_id,//CompanyCode
                                        "",//idCard
                                        etPolicyNumber.getText().toString(),//policyNumber
                                        etMembershipId.getText().toString(),//insuranceMemberId
                                        tvInsuranceExpiryDate.getText().toString(),//insuranceExpDate
                                        etClassPlan.getText().toString(),//insuranceClass
                                        "",//insuranceAttachment
                                        etSubmitterName.getText().toString(),
                                        tvRelation.getText().toString(),
                                        path_id,
                                        path_insurance_card,
                                        intent.getStringExtra(Constants.GUEST.MARTIAL_STATUS),
                                        SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        } else {
                            layBtnRegister.setEnabled(true);
                            //makeToast(getString(R.string.consent_form));
                            acceptConsentAlertDialog();
                        }
                    } else {
                        layBtnRegister.setEnabled(true);
                        acceptAlertDialog();
                    }
                else
                    layBtnRegister.setEnabled(true);

                break;


            case R.id.iv_regenerate:
                captchaImage.regenerate();
                etInputCaptcha.setText("");
                break;
        }
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
            layBtnRegister.setEnabled(true);

        return valid;
    }

    boolean isValid() {


        boolean valid = true;
        if (path_id==null) {
            makeToast(getString(R.string.attach_id_card));
            valid = false;
        }


        if (tvRelation.getText().toString().equalsIgnoreCase(getString(R.string.select)) || tvRelation.getText().toString().length() == 0) {
            makeToast(getString(R.string.relation_to_patiet));
            valid = false;
        }

        if (etSubmitterName.getText().toString().trim().length() == 0) {
            etSubmitterName.setError(getString(R.string.required));
            valid = false;
        } else if (etSubmitterName.getText().toString().trim().length() < 10) {
            etSubmitterName.setError(getString(R.string.submitter_invalid));
            valid = false;
        }

        if (rdYes.isChecked()) {
            hasInsurance = "yes";
//            if (tvInsuranceCompany.getText().toString().equalsIgnoreCase(getString(R.string.select)) || tvInsuranceCompany.getText().toString().length() == 0) {
//                makeToast(getString(R.string.select_insurance_company));
//                valid = false;
//            }
//            else if (tvInsuranceExpiryDate.getText().toString().equalsIgnoreCase(getString(R.string.select)) || tvInsuranceExpiryDate.getText().toString().length() == 0) {
//                makeToast(getString(R.string.select_expiry_date));
//                valid = false;
//            }
//            else if (path_insurance_card.length() == 0) {
//                makeToast(getString(R.string.attach_insurance_card));
//                valid = false;
//            }

//            if (etPolicyNumber.getText().toString().length() == 0) {
//                etPolicyNumber.setError(getString(R.string.required));
//                valid = false;
//            }

//            if (etClassPlan.getText().toString().length() == 0) {
//                etClassPlan.setError(getString(R.string.required));
//                valid = false;
//            }

        }if (rdNo.isChecked()) {
            company_id = "";
            hasInsurance = "no";
            path_insurance_card = null;
            tvUploadedInsuranceFileName.setVisibility(View.GONE);
            tvUploadedInsuranceFileName.setText("");
            valid = true;
        }
        if (!valid)
            layBtnRegister.setEnabled(true);

        return valid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE && data != null) {
//            ArrayList<NormalFile> file = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
//            String path = file.get(0).getPath();
//            logger("pic", path);
//        }

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File tempFile = new File(uri.toString());
            File file = createTmpFileFromUri(this, uri, tempFile.getName());

            if (file != null) {
                if (type.equalsIgnoreCase("id")) {

                    try {
                        path_id = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), uri);
                    } catch (IOException e) {
                        Log.e("abcd",e.toString());
                        e.printStackTrace();
                    }

                    tvUploadedIdFileName.setText(file.getName());
                    tvUploadedIdFileName.setVisibility(View.VISIBLE);
                }

                if (type.equalsIgnoreCase("insu")) {

                    try {
                        path_insurance_card = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), uri);
                    } catch (IOException e) {
                        Log.e("abcd",e.toString());
                        e.printStackTrace();
                    }
                    tvUploadedInsuranceFileName.setText(file.getName());
                    tvUploadedInsuranceFileName.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Problem while reading the image please try again later.", Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

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
//                if (fileSizeInKB <= 1500) {
//                    if (type.equalsIgnoreCase("id")) {
//                        tvUploadedIdFileName.setText(file.getName());
//                        path_id = path;
//                        tvUploadedIdFileName.setVisibility(View.VISIBLE);
//                    }
//
//                    if (type.equalsIgnoreCase("insu")) {
//                        tvUploadedInsuranceFileName.setText(file.getName());
//                        path_insurance_card = path;
//                        tvUploadedInsuranceFileName.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    makeToast(getString(R.string.max_file_size));
//                }
//                logger("pic", "File Path: " + path);
//            }
//            // Get the file instance
//            // File file = new File(path);
//            // Initiate the upload
//        }
//
//        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
//            if (path != null) {
//                File file = new File(path);
//                long fileSizeInBytes = file.length();
//                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//                long fileSizeInKB = fileSizeInBytes / 1024;
//                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                long fileSizeInMB = fileSizeInKB / 1024;
//
//                if (fileSizeInKB <= 1500) {
//                    if (type.equalsIgnoreCase("id")) {
//                        tvUploadedIdFileName.setText(file.getName());
//                        path_id = path;
//                        tvUploadedIdFileName.setVisibility(View.VISIBLE);
//                    }
//
//                    if (type.equalsIgnoreCase("insu")) {
//                        tvUploadedInsuranceFileName.setText(file.getName());
//                        path_insurance_card = path;
//                        tvUploadedInsuranceFileName.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    makeToast(getString(R.string.max_file_size));
//                }
//                logger("pic", "File Path: " + path);
//            }
//        }
    }

    private File createTmpFileFromUri(Context context,Uri uri,String fileName) {
        File file = null;
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
            file = new File(context.getCacheDir() + "/" + fileName);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream,file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    // open storage to pick image
//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
//        } catch (ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//        }
//    }

    // open camera
//    private void openCameraIntent() {
//        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
//            //Create a file to store the image
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this, "sa.med.imc.myimc.fileprovider", photoFile);
//                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
//            }
//        }
//    }

//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        path = image.getAbsolutePath();
//        return image;
//    }

    // Pop up list to select relation
    public void showListPopUp(TextView view, String type, String lastValue) {
        List<String> list = new ArrayList<>();
        if (type.equalsIgnoreCase("r")) {
            list.add(getString(R.string.self));
            list.add(getString(R.string.legal_gurdian));
            list.add(getString(R.string.relative));
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
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()

    }

    @Override
    public void onGetInfo(UserDetailModel response) {

    }


    @Override
    public void onSignUp(SimpleResponse response) {
        showAlert(this, response.getMessage());

    }

    public void showAlert(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.success));
        builder.setIcon(R.drawable.imc_logo);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent i1 = new Intent(activity, SplashActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i1);
                activity.finish();
//                SharedPreferencesUtils.getInstance(SignUpStep2Activity.this).clearAllSignUp(SignUpStep2Activity.this);
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

    @Override
    public void onSuccessOTP(LoginResponse response) {

    }

    @Override
    public void onGetComapanies(CompaniesResponse response) {
        if (response.getStatus().equalsIgnoreCase("Success")) {
            this.companiesResponse = response;
            showListPopUp(tvInsuranceCompany, tvInsuranceCompany.getText().toString(), this.companiesResponse);
        }

    }

    @Override
    public void onGetNationality(NationalityResponse response) {

    }

    @Override
    public void onGetCity(CityResponse response) {

    }

    @Override
    public void onGetDistrict(DistrictResponse response) {

    }


    // Pop up list to select booking id or date or report type
    public void showListPopUp(TextView view, String lastValue, CompaniesResponse companiesResponse) {
        List<IDNameModel> list = new ArrayList<>();
//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//            for (int i = 0; i < companiesResponse.getCompanies().size(); i++) {
//                if (companiesResponse.getCompanies().get(i).getCompanyNameAr() != null)
//                    list.add(new IDNameModel(String.valueOf(companiesResponse.getCompanies().get(i).getCode()),
//                            companiesResponse.getCompanies().get(i).getCompanyNameAr()));
//                else
//                    list.add(new IDNameModel(String.valueOf(companiesResponse.getCompanies().get(i).getCode()),
//                            companiesResponse.getCompanies().get(i).getCompanyName()));
//            }
//        } else {
        for (int i = 0; i < companiesResponse.getInsurance_list().size(); i++) {
            list.add(new IDNameModel(String.valueOf(companiesResponse.getInsurance_list().get(i).getInsurance_code()),
                    companiesResponse.getInsurance_list().get(i).getInsurance_description()));
        }
//        }

        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_insurance_custom_list, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                Common.getScreenWidt(SignUpStep2Activity.this),//view.getWidth(),
                ViewGroup.LayoutParams.MATCH_PARENT
//                Common.getScreenFullHeight(SignUpStep2Activity.this)
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        popupWindow.update();

        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);
        EditText edSearch = popupView.findViewById(R.id.ed_search);
        edSearch.setEnabled(true);
        edSearch.setFocusable(true);
        edSearch.requestFocus();

        SimpleSearchRecyclerViewAdapter adapter = new SimpleSearchRecyclerViewAdapter(this, list, lastValue);
//        edSearch.setOnTypingModified(new CustomTypingEditText.OnTypingModified() {
//            @Override
//            public void onIsTypingModified(EditText view, boolean isTyping) {
//
//                if (!isTyping) {
//                    if (edSearch.getText().toString().trim().length() > 0) {
//                        adapter.getFilter().filter(edSearch.getText().toString());
//                    }
//                }
//            }
//        });

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Common.hideSoftKeyboard(SignUpStep2Activity.this);
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
//                    if (charSequence.toString().length() > 0) {
//                        edSearch.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_search, 0, android.R.drawable.ic_menu_close_clear_cancel, 0);
//                    } else {
//                        edSearch.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_search, 0, 0, 0);
//                    }

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
            view.setText(adapter.getName(position).getName());
            company_id = String.valueOf(adapter.getName(position).getId());
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

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
        layBtnRegister.setEnabled(true);

        makeToast(msg);
        captchaImage.regenerate();
        etInputCaptcha.setText("");
    }

    @Override
    public void onNoInternet() {
        layBtnRegister.setEnabled(true);
        Common.noInternet(this);
    }


    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_a_photo))) {
                    ImagePicker.with(SignUpStep2Activity.this)
                            .cameraOnly()
                            .compress(1024)
                            .start();


//                    openCameraIntent();
                } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                    ImagePicker.with(SignUpStep2Activity.this)
                            .galleryOnly()
                            .compress(1024)
                            .start();

//                    showFileChooser();
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


}
