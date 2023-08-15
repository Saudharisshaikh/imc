package sa.med.imc.myimc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ai.medicus.insights.MedicusInsights;
import ai.medicus.medicuscore.LoginWithPinDelegate;
import ai.medicus.medicuscore.LoginWithPinErrors;
import ai.medicus.medicuscore.OnlinePatient;
import ai.medicus.medicuscore.Patient;
import ai.medicus.medicuscore.SyncWithServerDelegate;
import ai.medicus.smartreport.SmartReport;
import ai.medicus.smartreport.SmartReportCallback;
import ai.medicus.utils.MedicusUtilities;
import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Base.MCPatient;

import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.Records.view.ReportsViews;

public class ViewReportActivity extends AppCompatActivity implements ReportsViews, NavigationView.OnNavigationItemSelectedListener, FragmentListener, LocationListener {

    private final String TAG_PANEL = "tag_panel";
    private final String TAG_BIOMARKER = "tag_biomarker";
    private final String TAG_REPORT_DETAILS = "tag_report_details";
    //    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.main_toolbar)
//    RelativeLayout mainToolbar;
    @BindView(R.id.main_container_wrapper)
    FrameLayout mainContainerWrapper;
    //    @BindView(R.id.tabs)
//    TabLayout tabs;
    int i = 0;
    public static boolean active = false;

    Fragment homeFragment, recordsFragment, profileFragment, chatFragment,
            physiciansFragment, clinicsFragment, medicineFragment, lMSRecordsFragment, healthSummaryFragment, healthAndFitness,
            allergiesFragment, vitalSignsFragment, wellness, bodymeasurement, heatandvitals, sleepcycle, activity;
    @BindView(R.id.iv_logo)
//    ImageView ivLogo;
//    @BindView(R.id.iv_notifications)
    ImageView ivNotifications;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_english)
    TextView tvEnglish;
    @BindView(R.id.tv_arabic)
    TextView tvArabic;
    @BindView(R.id.appoint_center_button)
    ImageView appointCenterButton;
    @BindView(R.id.count_frame)
    FrameLayout countFrame;
    TextView tv_email;

    List<LabReport> reports = new ArrayList<>();
    List<MedicalReport> medicalReports = new ArrayList<>();
    List<SickLeave> sick_reports = new ArrayList<>();
    List<LabReportsParentMedicus> reportsMedicus = new ArrayList<>();


    TabLayout tabLayout;
    ImageView imageView, imageViewRecord, appoint_center_button, mayoimage, profileImage;
    TextView textView;
    RelativeLayout maintoolbar;

    String q = Constants.TYPE.RECORD_MAIN;
    String clinic_physician = "";
    String medicine = "";
    String accssionNum = "";
    String Position;


    boolean onBackRecords = false, isGPSEnable = false, isNetworkEnable = false;

    FragmentManager fragmentManager;
    private int Id = 0;
    private String report_title = "";
    private String report_number = "";
    private String report_Data = "";
    ReportsPresenter presenter;
    int page = 0, total_items = 0, totalReportsItems = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        getData();
//        tabLayout=findViewById(R.id.tabs);
        imageView = findViewById(R.id.homeimage);
        imageViewRecord = findViewById(R.id.record1);
        appoint_center_button = findViewById(R.id.appoint_center_button);
        mayoimage = findViewById(R.id.mayoimage);
        profileImage = findViewById(R.id.profileImage);

//        textView=findViewById(R.id.toolbar_title);
//        maintoolbar=findViewById(R.id.main_toolbar);


        fragmentManager = getSupportFragmentManager();

        presenter = new ReportsImpl(this, this);


        Log.e("abcd",accssionNum);

        if (MCPatient.getInstance().getPatientRecord() != null) {
            Log.e("abcd","MCPatient not null");

            initReportDetailsFragment();
            initSR();


        } else {
            Log.e("abcd","get pin");

            presenter.callGetPin(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""));
        }

//        setUpTabs();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("HomeFragment", 1);
                startActivity(intent);

            }
        });

        imageViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("RecordFragment", 2);
                startActivity(intent);
            }
        });

        appoint_center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("AppointmentFragment", 3);
                startActivity(intent);
            }
        });

        mayoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("MayoFragment", 4);
                startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("ProfileFragment", 5);
                startActivity(intent);
            }
        });


    }

    public void getData(){
        Intent intent = getIntent();
        if (intent != null){
            accssionNum = intent.getStringExtra("accsessionNum");
            Log.d(accssionNum,"..............start..........");
        }

    }


    public void initSR() {
        MedicusUtilities.initUtileco(this, MCPatient.getInstance().get());
        SmartReport.Companion.configure(MCPatient.getInstance().get(), new InsightListener(), new SmartReportCallback() {
                    @Override
                    public void onEditReportClicked(Activity activity, int reportId) {

                    }

                    @Override
                    public void onOpenProfilePopupClicked(Activity activity) {

                    }

                    @Override
                    public void onOpenPopup(Activity activity, Context context, String symbol) {

                    }

                    @Override
                    public void onShouldUserOpenCreateAccountActivity(Activity activity) {

                    }

                    @Override
                    public void getPanelDetailsFragment(int reportId, int panelId) {
                        initReportDetailsFragment(SmartReport.Companion.getPanelDetailsFragment(reportId, panelId), TAG_PANEL);

                    }

                    @Override
                    public void getBiomarkerDetailsFragment(int reportId, String biomarkerJson) {
                        initReportDetailsFragment(SmartReport.Companion.getBiomarkerDetailsFragment(reportId, biomarkerJson, null), TAG_BIOMARKER);

                    }
                }
                , false, false);
        MedicusInsights.configure(MCPatient.getInstance().get(), new InsightListener(),
                "en", String.valueOf(this.getPackageManager()), false);


    }

    public void initReportDetailsFragment() {
//        if (!isFinishing()) {
        Log.e("abcd", "initReportDetailsFragment");

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, SmartReport.Companion.getReportDetailsFragment(
                        null, //REPLACE THIS WITH CORRECT REPORT ID OR NULL
                        accssionNum, //REPLACE THIS WITH CORRECT REPORT NUMBER OR NULL
                        null,
                        1.606401212E9)) // REPLACE THIS WITH CORRECT REPORT DATE
                .setReorderingAllowed(true)
                .addToBackStack(TAG_REPORT_DETAILS)
                .commit();
//        }
    }


    private void initReportDetailsFragment(Fragment fragment, String tag) {

        if (fragment != null) {
//        if (!isFinishing()) {
            fragmentManager.beginTransaction()
                    .add(R.id.frame_container, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(tag)
                    .commit();
//        }
        }
    }




    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() > 1)
            fragmentManager.popBackStackImmediate();
        else
            finish();
    }

    @Override
    public void onGetLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetMedicalReports(MedicalResponse response) {

    }

    @Override
    public void onGetSickLeaveReports(SickLeaveResponse response) {

    }

    @Override
    public void onGetOperativeReports(OperativeResponse response) {

    }

    @Override
    public void onGetCardioReports(CardioReportResponse reportResponse) {

    }

    @Override
    public void onGetSearchLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetSearchMedicalReports(MedicalResponse response) {

    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> lstOfLabReportParentMedicus, int totalItems) {


        accssionNum = lstOfLabReportParentMedicus.get(0).getLstOfChildMedicus().get(0).getAccessionNum();
        total_items = lstOfLabReportParentMedicus.size();
        totalReportsItems = totalItems;
    }

    @Override
    public void onGetPin(PinResponse response) {
        Log.e("abcd", "onGetPin method called....");
        handleLogin(response.getData().getPin());
    }

    @Override
    public void onGenerateSmartReport(ResponseBody response, Headers headers) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {
        try {
            if (msg != null)
                if (msg.equalsIgnoreCase("timeout")) {
                    if (reports.size() == 0 && medicalReports.size() == 0 && sick_reports.size() == 0) {

                    } else {
//                    Common.makeToast(this, getString(R.string.time_out_messgae));
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }

                } else {
//                Common.makeToast(this, msg);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
        } catch (Exception e) {
            Log.e("abcd",e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onNoInternet() {

    }

    private void handleLogin(String pin) {
        String mrnNo = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "");
        showLoading();
        OnlinePatient onlinePatient = OnlinePatient.create(false);
        onlinePatient.loginWithPinAsync(mrnNo,//"Razia" 089599
                pin, // "23A593K",//2A46FKD
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH),
                new LoginWithPinDelegate() {
                    @Override
                    public void onLoginWithPinSuccess(Patient patient) {
                        Log.e("abcd", "onLoginWithPinSuccess method called ...");
//                          requireActivity().runOnUiThread(() -> hideLoading());


                        int id = patient.getPatientRecord().getId();
                        SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS, id);
                        MCPatient.getInstance().setCurrent(patient);
//

                        MCPatient.getInstance().get().syncWithServer(new SyncWithServerDelegate() {
                            @Override
                            public void onSyncWithServerSuccess() {
                                Log.e("abcd", "onSyncWithServerSuccess method called ...");
                                //    openSmartReports();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MCPatient.getInstance().get().refreshData();
                                        initReportDetailsFragment();
                                        initSR();
                                        hideLoading();
                                    }
                                });
                            }

                            @Override
                            public void onSyncWithServerFailure(String errorMessage) {
                                Log.e("abcd", "onSyncWithServerFailure method called ...");
                                Log.e("abcd", "onSyncWithServerFailure errormesg : " + errorMessage);
                            }
                        });
                    }

                    @Override
                    public void onLoginWithPinFailure(LoginWithPinErrors errors) {
                        try {
                            Log.e("abcd", "onLoginWithPinFailure method called ..."+errors.getErrorMessage());

                            ViewReportActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MedicusUtilities.getPatient();
                                    hideLoading();
                                    onFail(errors.getErrorMessage());
                                }
                            });
//                            getParent().runOnUiThread(() -> {
//                                hideLoading();
//                                onFail(errors.getErrorMessage());
//                                //....//
//                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
    }

//    private void startFragment(String title, Fragment fragment) {
//        String backStateName = fragment.getClass().getName();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment oldFragment = fragmentManager.findFragmentByTag(backStateName);
//        if (oldFragment != null) {
//            fragmentManager.beginTransaction().remove(oldFragment).commit();
//        }
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (fragment.isAdded()) {
//            Fragment currentFragment = fragmentManager.findFragmentByTag(backStateName);
//            if (currentFragment != null) {
//                fragmentTransaction.detach(currentFragment);
//                fragmentTransaction.attach(currentFragment);
//                fragmentTransaction.addToBackStack(backStateName).commit();
//            } else {
//                fragmentTransaction.replace(R.id.frame_container, fragment);
//                fragmentTransaction.addToBackStack(backStateName).commit();
//            }
//        } else {
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//            fragmentTransaction.add(R.id.frame_container, fragment);
//            fragmentTransaction.addToBackStack(backStateName).commit();
//
//        }
//    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void openProfileMedicineFragment(String value) {

    }

    @Override
    public void openPhysicianFragment(String value, String clinic_id) {

    }

    @Override
    public void openClinicFragment(String value) {

    }

    @Override
    public void openPaymentInfoFragment(Serializable value, String key) {

    }

    @Override
    public void openTelrFragment(Serializable book, Serializable res, String value) {

    }

    @Override
    public void openMedicineFragment(String type, String value) {

    }

    @Override
    public void backPressed(String type) {

    }

    @Override
    public void openLMSRecordFragment(String value, String type, String episodeId) {

    }

    @Override
    public void startTask(int time) {

    }

    @Override
    public void openHealthSummary(String value) {

    }

    @Override
    public void openFitness(String value) {

    }

    @Override
    public void openWellness(String value) {

    }

    @Override
    public void openBodyMeasurement(String value) {

    }

    @Override
    public void openActivity(String value) {

    }

    @Override
    public void openSleepCycle(String value) {

    }

    @Override
    public void openHeatAndVitals(String value) {

    }

    @Override
    public void openAllergies(String value) {

    }

    @Override
    public void openVitalSigns(String value) {

    }


    @Override
    public void checkLocation() {

    }
}