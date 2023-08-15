package sa.med.imc.myimc;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;
import static sa.med.imc.myimc.Network.Constants.SELECTED_HOSPITAL;
import static sa.med.imc.myimc.Network.ImcApplication.updateLanguage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.vincent.filepicker.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ai.medicus.utils.MedicusUtilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.AddGuardian.view.AddGuardianActivity;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.Calculators.CalculatorsActivity;
import sa.med.imc.myimc.Chat.ChatFragment;
import sa.med.imc.myimc.Clinics.view.ClinicsFragment;
import sa.med.imc.myimc.FitnessAndWellness.FitnessAndWellnessFragment;
import sa.med.imc.myimc.HealthSummary.view.AllergiesFragment;
import sa.med.imc.myimc.HealthSummary.view.HealthSummaryFragment;
import sa.med.imc.myimc.HealthSummary.view.VitalSignsFragment;
import sa.med.imc.myimc.Home.HomeFragment;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Notifications.presenter.NotificationImpl;
import sa.med.imc.myimc.Notifications.presenter.NotificationPresenter;
import sa.med.imc.myimc.Notifications.view.NotificationViews;
import sa.med.imc.myimc.Notifications.view.NotificationsActivity;
import sa.med.imc.myimc.OrderProfile.ProfileOrdersActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansFragment;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Profile.presenter.ProfileImpl;
import sa.med.imc.myimc.Profile.presenter.ProfilePresenter;
import sa.med.imc.myimc.Profile.view.ProfileFragment;
import sa.med.imc.myimc.Profile.view.ProfileViews;
import sa.med.imc.myimc.RateTheAap.RateThisApp;
import sa.med.imc.myimc.Records.view.RecordsFragment;
import sa.med.imc.myimc.Records.view.ReportsListFragment;
import sa.med.imc.myimc.Records.view.activity.ReportListActivity;
import sa.med.imc.myimc.RetailModule.view.RetailFacilitiesActivity;
import sa.med.imc.myimc.SYSQUO.Chat.chat.MainChatActivity_New;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Settings.SettingActivity;
import sa.med.imc.myimc.Telr.PaymentInfoFragment;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Telr.TelrFragment;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;
import sa.med.imc.myimc.Wellness.ActivityFragment;
import sa.med.imc.myimc.Wellness.BodyMeasurementFragment;
import sa.med.imc.myimc.Wellness.HeatAndVitalsFragment;
import sa.med.imc.myimc.Wellness.SleepCycleFragment;
import sa.med.imc.myimc.Wellness.WellnessFragment;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.healthcare.HealthCareFragment;
import sa.med.imc.myimc.medprescription.view.MedicinesPrescriptionsFragment;
import sa.med.imc.myimc.pharmacy.PharmacyFragment;
import sa.med.imc.myimc.splash.SplashActivity;
import sa.med.imc.myimc.webView.WebViewFragment;
import sa.med.imc.myimc.webView.model.WebViewRequest;

/*
 * Home screen for logged in users
 * All options to get appointments.
 * Search doctors
 * Chat with machine to get help
 * Manage profile data
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentListener, LocationListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.main_container_wrapper)
    FrameLayout mainContainerWrapper;
    @BindView(R.id.tabs)
    TabLayout tabs;
    int i = 0;
    public static boolean active = false;
    private static final int CREATE_SLIP_FILE = 6;
    private ResponseBody responseBody;
    private boolean isExitApp = false;
    String lng = null;

    Fragment homeFragment, recordsFragment, profileFragment, chatFragment,
            physiciansFragment, clinicsFragment, medicineFragment, lMSRecordsFragment, healthSummaryFragment, healthAndFitness,
            allergiesFragment, vitalSignsFragment, wellness, bodymeasurement, heatandvitals, sleepcycle, activity, telrfragment, paymentinfo;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_notifications)
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
    @BindView(R.id.tv_imc)
    TextView tv_imc;
    @BindView(R.id.tv_fc)
    TextView tv_fc;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    @BindView(R.id.appoint_center_button)
    ImageView appointCenterButton;
    @BindView(R.id.notification_count)
    TextView notificationCount;
    @BindView(R.id.count_frame)
    FrameLayout countFrame;
    TextView tv_email;

    ImageView iv_logo;

    private ActionBarDrawerToggle toggle;
    private String getFlag;


    String record_string = Constants.TYPE.RECORD_MAIN;
    String clinic_physician = "";
    String medicine = "";
    public static boolean isFromSymptom = false;

    private Timer mTimer = null;

    boolean onBackRecords = false, isGPSEnable = false, isNetworkEnable = false;

    AlertDialog.Builder builder;
    LocationManager locationManager;
    Double latitude = 0.0, longitude = 0.0;
    Location location;


    int hosp;

    int spinnercount=0;
    ArrayList<HashMap<String, String>> locationsList;
    private ListView lv;

    String token;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }



    @Override
    public void startTask(int time) {
        // Handle to refresh dashboard after every 1 minute
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(refresh);
                }
            }, 0, 60000);

        }
    }

    // stop task
    void stopRepeatingTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                sessionExpired();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // if email updated then refresh home data
    BroadcastReceiver refresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("email")) {
                String email = intent.getStringExtra("email");
                tv_email.setText(email);
                tv_email.setVisibility(View.VISIBLE);

//                ValidateResponse.UserDetails userDetails = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
//                userDetails.setEmail(email);

//                SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_USER, userDetails);
            }
        }
    };


    BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadNotification();
        }
    };


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        token = SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_ACCESS_TOKEN, "");

        setBufferTime();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hosp = SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1);


        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.Filter.REFRESH_MAIN));
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver2, new IntentFilter(Constants.Filter.REFRESH_MAIN_NOTIFICATION));
            LocalBroadcastManager.getInstance(this).registerReceiver(refresh, new IntentFilter(Constants.Filter.UPDATE_HOME));

        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpTabs();
//        if (BuildConfig.DEBUG)
        checkPermissionFirst(0);

        navView.setNavigationItemSelectedListener(this);
        setUpDrawer();
        setUpHeaderValues();

        navView.getMenu().getItem(0).setChecked(true);

        if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes"))
            navView.getMenu().getItem(4).setVisible(false);
        else
            navView.getMenu().getItem(4).setVisible(true);


//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_NOTIFICATION_UNREAD, "").equalsIgnoreCase("0") || SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_NOTIFICATION_UNREAD, "").length() == 0) {
//            countFrame.setVisibility(View.GONE);
//        } else {
//            countFrame.setVisibility(View.VISIBLE);
//        }

        // check for language used by user
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            setViewsChanges(false);
        } else {
            setViewsChanges(true);
        }

        setHosPitalChange();
        Log.e("abcd2", SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, ""));

        changeLanguage(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, ""));
//        if (!BuildConfig.DEBUG) {
//            checkUpdatePlayStore();
//        }

        receiveArguments();
        getFlag = getIntent().getStringExtra("My_Flag");
        if (getFlag != null) {
            if (getFlag.equalsIgnoreCase("Unique")) {

                RateThisApp.showRateDialog(MainActivity.this);

            }
        }


        getIntents();
        loadNotification();
        loadProfile();
        listLocations();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Code to start timer and take action after the timer ends
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        loadNotification();
                        Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(refresh);

                    }
                }, 2000);
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_green));

    }

    private void loadProfile() {
        ProfilePresenter profilePresenter = new ProfileImpl(new ProfileViews() {
            @Override
            public void onGetProfile(ProfileResponse response) {
                try {
                    tv_email.setText(response.getPatientProfile().getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetMedication(MedicationRespone response) {

            }

            @Override
            public void onUpdateProfile(SimpleResponse response) {

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
        }, MainActivity.this);
        profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    private void loadNotification() {
        NotificationPresenter notificationPresenter = new NotificationImpl(new NotificationViews() {
            @Override
            public void onGetCount(int count) {
                if (count > 0) {
                    countFrame.setVisibility(View.VISIBLE);
                    notificationCount.setText(count + "");
                } else {
                    countFrame.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onSuccess(NotificationResponse response) {

            }

            @Override
            public void onDelete(SimpleResponse response) {

            }

            @Override
            public void onClearAll(SimpleResponse response) {

            }

            @Override
            public void onRead(SimpleResponse response) {

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
        }, this);
        notificationPresenter.callGetNotificationsCount(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    public void addFragmentforRecord() {

        Intent i = new Intent(this, ViewReportActivity.class);
        startActivity(i);
    }


    public void openPdf(Uri uri) {

        Intent intent = getIntent();
        int abc = intent.getIntExtra("open", 0);

        if (abc == 0) {
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfOpenintent.setDataAndType(uri, "application/pdf");
            try {
                startActivity(pdfOpenintent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntents() {
        Intent i = getIntent();
        int value = i.getIntExtra("HomeFragment", 0);
        int value1 = i.getIntExtra("RecordFragment", 0);
        int value2 = i.getIntExtra("AppointmentFragment", 0);
        int value3 = i.getIntExtra("MayoFragment", 0);
        int value4 = i.getIntExtra("ProfileFragment", 0);
        if (value != 0) {
            if (value == 1) {
                setUpHomeFragment();
            }
        } else if (value1 != 0) {
            if (value1 == 2) {
                setupRecordFragment();
            }
        } else if (value2 != 0) {
            if (value2 == 3) {
                setupAppointmentFragment();
            }
        } else if (value3 != 0) {
            if (value3 == 4) {
                setupMayoFragment();
            }
        } else if (value4 != 0) {
            if (value4 == 5) {
                setupProfileFragment();
            }
        }

        if (getIntent().getIntExtra(Constants.booking_successfully_booked, 0) == Constants.booking_successfully_booked_code) {
            Common.messageDailog(MainActivity.this, getString(R.string.appointment_successfully_booked));
        }

    }

    private void setupRecordFragment() {
        if (record_string.equalsIgnoreCase(Constants.TYPE.RECORD_MAIN)) {
            if (recordsFragment == null)
                recordsFragment = RecordsFragment.newInstance();
//                        Bundle args = new Bundle();
//                        args.putString("param1", record_string);
//                        if (onBackRecords)
//                            args.putString("param2", "yes");
//                        else
//                            args.putString("param2", "no");
//
//                        recordsFragment.setArguments(args);

            startFragment(getResources().getString(R.string.records), recordsFragment);
            mainToolbar.setVisibility(View.GONE);
            Common.CONTAINER_FRAGMENT = "RecordsFragment";
            record_string = Constants.TYPE.RECORD_MAIN;
        } else {
            record_string = Constants.TYPE.RECORD_MAIN;
        }
    }

    private void setupAppointmentFragment() {
        if (clinic_physician.length() == 0) {

            if (clinicsFragment == null)
                clinicsFragment = ClinicsFragment.newInstance();

            Bundle args = new Bundle();
            if (Common.CONTAINER_FRAGMENT.equalsIgnoreCase("ClinicFragmentC"))
                args.putString("param1", "ClinicFragmentC");
            else {
                args.putString("param1", "ClinicFragment");
                Common.CONTAINER_FRAGMENT = "ClinicFragment";

            }
            args.putString("param2", "");
            clinicsFragment.setArguments(args);

            startFragment("", clinicsFragment);
            mainToolbar.setVisibility(View.GONE);

        } else {
            physiciansFragment = PhysiciansFragment.newInstance();

            Bundle args = new Bundle();
            args.putString("param1", clinic_physician);
            args.putString("param2", "");
            physiciansFragment.setArguments(args);

            startFragment("", physiciansFragment);
            Common.CONTAINER_FRAGMENT = "PhysiciansFragment";
            mainToolbar.setVisibility(View.GONE);
        }


    }

    private void setupMayoFragment() {
        if (chatFragment == null)
            chatFragment = ChatFragment.newInstance();
        startFragment(getResources().getString(R.string.chatbot), chatFragment);
        Common.CONTAINER_FRAGMENT = "ChatFragment";
        mainToolbar.setVisibility(View.GONE);
    }


    private void setupProfileFragment() {
        if (profileFragment == null)
            profileFragment = ProfileFragment.newInstance();

        Bundle args = new Bundle();
        args.putString("param1", medicine);
        profileFragment.setArguments(args);

        startFragment(getResources().getString(R.string.profile), profileFragment);
        Common.CONTAINER_FRAGMENT = "ProfileFragment";
        mainToolbar.setVisibility(View.GONE);
        medicine = "";

    }


    private void receiveArguments() {
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            lng = extras.getString("key");
            // and get whatever type user account id is
        }
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {

            navView.getMenu().getItem(1).setVisible(false);
        }
        iv_logo = findViewById(R.id.iv_logo);
        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        String hospitalLogo = sp.getString("hospitalLogo",null);
        Picasso.get().load(hospitalLogo)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).fit().into(iv_logo);


        String specialMessage = sp.getString("specialMessage","");

        if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            specialMessage = sp.getString("specialMessage_ar",null);
        }

        if(!specialMessage.equals("")) {
            showSpecialMessage(specialMessage);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopRepeatingTask();
        active = false;
        if (isExitApp) {
            isExitApp = false;
//            Process.killProcess(Process.myPid());
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (isFromSymptom) {
            clinic_physician = "";
            selectTab(2);
        }
        // check for notification unread status
    }

    // set up navigation list
    private void setUpDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(menuItem -> false);

        ivMenu.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                Log.e("setUpDrawer","openDrawer");
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                Log.e("setUpDrawer","closeDrawer");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }

    // setup navigation header values
    void setUpHeaderValues() {
        View hview = navView.getHeaderView(0);
        TextView name = hview.findViewById(R.id.tv_name);
        tv_email = hview.findViewById(R.id.tv_email);
        // ImageView iv_profile = hview.findViewById(R.id.iv_profile);

//        ValidateResponse.UserDetails userDetails = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER);
        ValidateResponse userDetails = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER);
        String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
        } else {
            name.setText(userDetails.getArabic_first_name() + " " + userDetails.getArabic_last_name());
        }

        try {
            if (userDetails.getEmail(MainActivity.this) != null) {
                if (userDetails.getEmail(MainActivity.this).length() > 0) {
                    tv_email.setText(userDetails.getEmail(MainActivity.this));
                    tv_email.setVisibility(View.VISIBLE);
                } else {
                    tv_email.setVisibility(View.GONE);
                }
            } else {
                tv_email.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        hview.setOnClickListener(v -> {
            medicine = "";
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            selectTab(4);
        });
    }

    //Setup tab buttons
    void setUpTabs() {
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.home)).setIcon(R.drawable.ic_home_icon));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.records)).setIcon(R.drawable.ic_records_icon));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.appointment)).setIcon(R.drawable.ic_dot_trans));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.mayo_clinic)).setIcon(R.drawable.mayo));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.profile)).setIcon(R.drawable.ic_profile_icon));

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setUpHomeFragment();

                } else if (tab.getPosition() == 1) {
                    startActivity(new Intent(MainActivity.this, ReportListActivity.class));

                } else if (tab.getPosition() == 2) {
                    if (clinic_physician.length() == 0) {

                        if (clinicsFragment == null)
                            clinicsFragment = ClinicsFragment.newInstance();

                        Bundle args = new Bundle();
                        if (Common.CONTAINER_FRAGMENT.equalsIgnoreCase("ClinicFragmentC"))
                            args.putString("param1", "ClinicFragmentC");
                        else {
                            args.putString("param1", "ClinicFragment");
                            Common.CONTAINER_FRAGMENT = "ClinicFragment";

                        }
                        args.putString("param2", "");
                        clinicsFragment.setArguments(args);

                        startFragment("", clinicsFragment);
                        mainToolbar.setVisibility(View.GONE);

                    } else {
                        physiciansFragment = PhysiciansFragment.newInstance();

                        Bundle args = new Bundle();
                        args.putString("param1", clinic_physician);
                        args.putString("param2", "");
                        physiciansFragment.setArguments(args);

                        startFragment("", physiciansFragment);
                        Common.CONTAINER_FRAGMENT = "PhysiciansFragment";
                        mainToolbar.setVisibility(View.GONE);
                    }
//                    }

                } else if (tab.getPosition() == 3) {

                    if (chatFragment == null)
                        chatFragment = ChatFragment.newInstance();
                    startFragment(getResources().getString(R.string.chatbot), chatFragment);
                    Common.CONTAINER_FRAGMENT = "ChatFragment";
                    mainToolbar.setVisibility(View.GONE);


                } else if (tab.getPosition() == 4) {
                    if (profileFragment == null)
                        profileFragment = ProfileFragment.newInstance();

                    Bundle args = new Bundle();
                    args.putString("param1", medicine);
                    profileFragment.setArguments(args);

                    startFragment(getResources().getString(R.string.profile), profileFragment);
                    Common.CONTAINER_FRAGMENT = "ProfileFragment";
                    mainToolbar.setVisibility(View.GONE);
                    medicine = "";
                }

                clinic_physician = "";

                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorWhite);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.grey_color_menu);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 && tab.isSelected()) {
                    Common.CONTAINER_FRAGMENT = "HomeFragment";
                    ivLogo.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.GONE);

                    if (homeFragment == null)
                        homeFragment = new HomeFragment();
                    String backStateName = homeFragment.getClass().getName();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.main_container_wrapper, homeFragment, backStateName);
                    fragmentTransaction.addToBackStack(backStateName).commit();
                    mainToolbar.setVisibility(View.VISIBLE);
                } else if (tab.getPosition() == 1) {
                    startActivity(new Intent(MainActivity.this, ReportListActivity.class));
                }

            }
        });

        onBackRecords = false;

        // if app opened with link - deep linking process functionality
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_CLINIC_ID, "").length() > 0) {
            if (getIntent().hasExtra("restart"))
                setUpHomeFragment();
            else
                openPhysicianFragment("PhysiciansFragmentC", SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_CLINIC_ID, ""));
        } else
            setUpHomeFragment();

    }

    void selectTab(int position) {
        tabs.getTabAt(position).select();
        final int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorWhite);
        if (position != 2)
            tabs.getTabAt(position).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }


    public void startFragment(String title, Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag(backStateName);
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            Fragment currentFragment = fragmentManager.findFragmentByTag(backStateName);
            if (currentFragment != null) {
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.addToBackStack(backStateName).commit();
            } else {
                fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
                fragmentTransaction.addToBackStack(backStateName).commit();
            }
        } else {
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(backStateName).commit();

        }
    }

    // Open dashboard
    private void setUpHomeFragment() {
        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_CLINIC_ID, "");

        selectTab(0);
        Common.CONTAINER_FRAGMENT = "HomeFragment";
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);

        if (homeFragment == null)
            homeFragment = new HomeFragment();
        WellnessFragment wellnessFragment = WellnessFragment.newInstance();
        String backStateName = homeFragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container_wrapper, homeFragment, backStateName);
        fragmentTransaction.addToBackStack(backStateName).commit();
        mainToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_SLIP_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                alterDocument(uri);
                openPdf(uri);
            }
        } else if (requestCode == TelrActivity.PAYMENT_CODE) {
            Common.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void alterDocument(Uri uri) {
        try {
            int count = -1;
            ParcelFileDescriptor pfd = MainActivity.this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            InputStream input = new BufferedInputStream(responseBody.byteStream(), 8192);

            byte[] data = new byte[1024];

            while ((count = input.read(data)) != -1) {
                fileOutputStream.write(data, 0, count);
            }

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openFragment(BaseFragment fragment) {
        Common.CONTAINER_FRAGMENT = fragment.getClass().getSimpleName();
        startFragment("", fragment);
    }

    @OnClick({R.id.iv_notifications, R.id.iv_search, R.id.tv_english, R.id.tv_arabic, R.id.appoint_center_button, R.id.tv_fc, R.id.tv_imc})
    public void onViewClicked(View view) {
        updateLanguage(MainActivity.this);

        switch (view.getId()) {

            case R.id.iv_notifications:

                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                someActivityResultLauncher.launch(intent);

//                activity.startActivity(intent);
//                NotificationsActivity.startActivity(this);
//                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, "0");
//                countFrame.setVisibility(View.GONE);
                break;

            case R.id.iv_search:
                break;

            case R.id.appoint_center_button:
                openClinicFragment("ClinicFragment");
                break;

            case R.id.tv_english:
                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                    askLanguageChange(Constants.ENGLISH);
                break;

            case R.id.tv_arabic:
                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                    askLanguageChange(Constants.ARABIC);
                break;

            case R.id.tv_fc:
                if (hosp == 1) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SELECTED_HOSPITAL, 4);
                    SharedPreferencesUtils.getInstance(this).setValue(HOSPITAL_CODE, "FC");
                    Intent i1 = new Intent(MainActivity.this, MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    finish();
                }

                break;

            case R.id.tv_imc:
                if (hosp != 1) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SELECTED_HOSPITAL, 1);
                    SharedPreferencesUtils.getInstance(this).setValue(HOSPITAL_CODE, "IMC");
                    Intent i1 = new Intent(MainActivity.this, MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    finish();
                }
                break;

        }




    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    loadNotification();
                }
            });

    void setHosPitalChange() {
        int hosp = SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1);
        if (hosp == 1) {
            tv_fc.setBackgroundResource(R.drawable.btn_ar_unselected);
            tv_fc.setTextColor(getResources().getColor(R.color.text_blue_color));

            tv_imc.setBackgroundResource(R.drawable.btn_en_selected);
            tv_imc.setTextColor(getResources().getColor(R.color.colorWhite));

        } else {
            tv_imc.setBackgroundResource(R.drawable.btn_en_unselected);
            tv_imc.setTextColor(getResources().getColor(R.color.text_blue_color));

            tv_fc.setBackgroundResource(R.drawable.btn_ar_selected);
            tv_fc.setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }


    @SuppressLint("ResourceAsColor")
    private void listLocations(){


        locationsList = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        String locations = sp.getString("hospitals_locations",null);
        String selectedCode = sp.getString("hospitalCode",null);
        JSONArray JSONObject;
        try {

            JSONObject  = new JSONArray(locations);

            LinearLayout layout = (LinearLayout) findViewById(R.id.buttons);


/*
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                {
                    //Object item = parent.getItemAtPosition(pos);

                    Log.e("pos",locationsList.get(pos).get("hospitalId"));
                    String hospitalId = locationsList.get(pos).get("hospitalId");
                    String hospitalCode = locationsList.get(pos).get("hospitalCode");
                    String hospitalName = locationsList.get(pos).get("hospitalName");
                    String hospitalName_ar = locationsList.get(pos).get("hospitalName_ar");
                    String hospitalLogo = locationsList.get(pos).get("hospitalLogo");
                    String hospitalLargeImage = locationsList.get(pos).get("hospitalLargeImage");


                    SharedPreferences sp = getActivityContext().getSharedPreferences("location", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("hospitalId", hospitalId);
                    editor.putString("hospitalCode", hospitalCode);
                    editor.putString("hospitalName", hospitalName);
                    editor.putString("hospitalName_ar", hospitalName_ar);
                    editor.putString("hospitalLogo", hospitalLogo);
                    editor.putString("hospitalLargeImage", hospitalLargeImage);
                    editor.commit();

                   // updateSelection(Integer.parseInt(hospitalId),hospitalCode);

                }

                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
*/
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ArrayList<String> items = new ArrayList<String>();
            HashMap<String, String> card;
            int selectPosition =1;
            for (int i = 0; i < JSONObject.length() ; i++) // where x is the size of the list containing your alphabet.
            {
                org.json.JSONObject c = JSONObject.getJSONObject(i);
                String hospitalId = c.getString("hospitalId");
                String hospitalCode = c.getString("hospitalCode");
                String hospitalName = c.getString("hospitalName");
                String hospitalName_ar = c.getString("hospitalName_ar");
                String hospitalLogo = c.getString("hospitalLogo");
                String hospitalLargeImage = c.getString("hospitalLargeImage");
                String specialMessage = c.getString("specialMessage");
                String specialMessage_ar = c.getString("specialMessage_ar");


                card = new HashMap<>();
                card.put("hospitalId", hospitalId);
                card.put("hospitalCode", hospitalCode);
                card.put("hospitalName", hospitalName);
                card.put("hospitalName_ar", hospitalName_ar);
                card.put("hospitalLogo", hospitalLogo);
                card.put("hospitalLargeImage", hospitalLargeImage);

                card.put("specialMessage", specialMessage);
                card.put("specialMessage_ar", specialMessage_ar);

                if(hospitalCode.equals(selectedCode)){
                    selectPosition = i;
                }

                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                    items.add(hospitalName_ar);
                }else{
                    items.add(hospitalName);
                }

                locationsList.add(card);
            }




            // Spinner element
            final Spinner spinner = (Spinner) findViewById(R.id.hospital_spinner);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner


                int spinnerPosition = dataAdapter.getPosition(selectedCode);
                Log.e("spinnerPosition",spinnerPosition+" --"+selectedCode);



            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //check if spinner2 has a selected item and show the value in edittext

                    Log.e("positions"+position,locationsList.toString());
                    String hospitalId = locationsList.get(position).get("hospitalId");
                    String hospitalCode = locationsList.get(position).get("hospitalCode");
                    String hospitalName = locationsList.get(position).get("hospitalName");
                    String hospitalName_ar = locationsList.get(position).get("hospitalName_ar");
                    String hospitalLogo = locationsList.get(position).get("hospitalLogo");
                    String hospitalLargeImage = locationsList.get(position).get("hospitalLargeImage");

                    String specialMessage = locationsList.get(position).get("specialMessage");
                    String specialMessage_ar = locationsList.get(position).get("specialMessage_ar");

                    //editor.putString("specialMessage", locationsList.get(position).get("specialMessage"));
                    //editor.putString("specialMessage_ar", data.get("specialMessage_ar").toString());


                    SharedPreferences sp = getActivityContext().getSharedPreferences("location", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("hospitalId", hospitalId);
                    editor.putString("hospitalCode", hospitalCode);
                    editor.putString("hospitalName", hospitalName);
                    editor.putString("hospitalName_ar", hospitalName_ar);
                    editor.putString("hospitalLogo", hospitalLogo);
                    editor.putString("hospitalLargeImage", hospitalLargeImage);
                    editor.putString("specialMessage", specialMessage);
                    editor.putString("specialMessage_ar", specialMessage_ar);


                    editor.commit();

                    Log.e("specialMessage",specialMessage + "  - " + SELECTED_HOSPITAL);
                    if(spinnercount>0) {
                        updateSelection(Integer.parseInt(hospitalId), hospitalCode);
                    }
                    spinnercount++;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // sometimes you need nothing here
                }
            });

            //spinner.setOnItemSelectedListener(this);

            spinner.setAdapter(dataAdapter);
            spinner.setSelection(selectPosition);


        } catch (final JSONException e) {
            Log.e("abcd", "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }



    public void updateSelection(int selectedHospital,String hospitalCode){

        Log.e("updateSelection",selectedHospital + "updateSelection" + hospitalCode);

        SharedPreferencesUtils.getInstance(this).setValue(Constants.SELECTED_HOSPITAL, selectedHospital);
        SharedPreferencesUtils.getInstance(this).setValue(HOSPITAL_CODE, hospitalCode);

        Intent i1 = new Intent(MainActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();

    }

    //Change language button selection
    void setViewsChanges(boolean eng) {

        if (eng) {
            tvArabic.setBackgroundResource(R.drawable.btn_ar_unselected);
            tvArabic.setTextColor(getResources().getColor(R.color.text_blue_color));

            tvEnglish.setBackgroundResource(R.drawable.btn_en_selected);
            tvEnglish.setTextColor(getResources().getColor(R.color.colorWhite));

            tvEnglish.setEnabled(false);

        } else {
            tvArabic.setBackgroundResource(R.drawable.btn_ar_selected);
            tvArabic.setTextColor(getResources().getColor(R.color.colorWhite));

            tvEnglish.setBackgroundResource(R.drawable.btn_en_unselected);
            tvEnglish.setTextColor(getResources().getColor(R.color.text_blue_color));

            tvArabic.setEnabled(false);

        }
    }

    //Change language method
    void changeLanguage(String lan) {


        if (lan.equalsIgnoreCase(Constants.ARABIC)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ARABIC);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
//            MedicusUtilities.setLocale(MainActivity.this);
            MedicusUtilities.setLanguage(MedicusUtilities.getLocale(),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ARABIC));

//            Patient.getByNumber(String.valueOf(142736)).updateLanguage(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ARABIC));

            setViewsChanges(false);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
//            MedicusUtilities.setLocale(MainActivity.this);
            MedicusUtilities.setLanguage(MedicusUtilities.getLocale(),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH));
//            Patient.getByNumber(String.valueOf(142736)).updateLanguage(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH));
            setViewsChanges(true);
        }
//        recreate();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        updateLanguage(MainActivity.this);

        switch (id) {

            case R.id.nav_dashboard:
                setUpHomeFragment();
                break;

            case R.id.nav_retail:
                RetailFacilitiesActivity.startActivity(this);
                break;

            case R.id.nav_documents:

                break;

            case R.id.nav_medical_cards:
                break;

            case R.id.nav_add_guardian:
                AddGuardianActivity.startSetting(MainActivity.this);
                break;

            case R.id.nav_my_orders:
                ProfileOrdersActivity.startActivity(MainActivity.this);
                break;

            case R.id.nav_settings:
                SettingActivity.startSetting(MainActivity.this);
                break;

            case R.id.nav_health_calculators:
                CalculatorsActivity.startActivity(this);
                break;

            case R.id.nav_about_us:
                Intent intent = new Intent(this, UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_ABOUT_US);
                startActivity(intent);
                break;

            case R.id.nav_help:
                ContactOptionsActivity.startActivity(this);
                break;

            case R.id.nav_heath_care:
                hideToolbar();
                openFragment(HealthCareFragment.newInstance());
                break;

            case R.id.nav_logout:
                askFirst();
                break;
        }

        runOnUiThread(() -> drawerLayout.closeDrawer(GravityCompat.START));
        return true;
    }

    // on device back press drawer closed if opened and fragment pull from back stack.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (Common.CONTAINER_FRAGMENT.isEmpty()) {
            super.onBackPressed();
        } else {
            backPressed(Common.CONTAINER_FRAGMENT);
        }
    }

    @Override
    public void onWebView(WebViewRequest request) {
        Fragment fragment = WebViewFragment.newInstance(request);
        Common.CONTAINER_FRAGMENT = fragment.getClass().getSimpleName();
        mainToolbar.setVisibility(View.GONE);
        startFragment("", fragment);
    }

    @Override
    public void backPressed(String type) {
        tabs.setVisibility(View.VISIBLE);
        appointCenterButton.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (type) {

            case "BaseFragment":
                exitDialog();
                break;


            case "HomeFragment":
                exitDialog();
                break;

            case "PhysiciansFragment":
                setUpHomeFragment();
                break;

            case "AppointmentFragment":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "PhysiciansFragmentC";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openPhysicianFragment("PhysiciansFragmentC", physiciansFragment.getArguments().getString("param2"));
                }
                break;


            case "PhysiciansFragmentC":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // if app opened with link - deep linking process functionality
                    if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_CLINIC_ID, "").length() > 0) {
                        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_CLINIC_ID, "");
                        setUpHomeFragment();

                    } else {
                        fragmentManager.popBackStack();
                        Common.CONTAINER_FRAGMENT = "ClinicFragment";
                    }

                } else {
                    openClinicFragment("ClinicFragment");
                }

                break;

            case "PhysicianDetailFragment":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "PhysiciansFragmentC";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openPhysicianFragment("PhysiciansFragmentC", physiciansFragment.getArguments().getString("param2"));
                }
                break;


            case "AllergiesFragment":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "HealthSummaryFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openHealthSummary("HealthSummaryFragment");
                }
                break;


            case "VitalSignsFragment":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "HealthSummaryFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openHealthSummary("HealthSummaryFragment");
                }
                break;

            case "BodyMeasurementFragment":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "FitnessFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openFitness("FitnessFragment");
                }
                break;

            case "HeatAndVitals":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "FitnessFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openFitness("FitnessFragment");
                }
                break;


            case "SleepCycle":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "FitnessFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openFitness("FitnessFragment ");
                }
                break;


            case "Activity":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "FitnessFragment";

                } else {
                    if (physiciansFragment.getArguments() != null)
                        openFitness("FitnessFragment ");
                }
                break;


            default:
                setUpHomeFragment();
                break;

        }
    }

    @Override
    public void openHome() {
        setUpHomeFragment();
    }

    @Override
    public void openLMSRecordFragment(String value, String type, String episodeId) {

//        record_string = "sfs";
//        selectTab(1);
//        if (lMSRecordsFragment == null)
        lMSRecordsFragment = ReportsListFragment.newInstance();

        Bundle args = new Bundle();
        args.putString("param1", type);
        args.putString("param2", episodeId);
        lMSRecordsFragment.setArguments(args);

        startFragment("", lMSRecordsFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openHealthSummary(String value) {
        if (healthSummaryFragment == null)
            healthSummaryFragment = HealthSummaryFragment.newInstance();

        startFragment("", healthSummaryFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openFitness(String value) {
        if (healthAndFitness == null)
            healthAndFitness = FitnessAndWellnessFragment.newInstance();

        startFragment("", healthAndFitness);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openWellness(String value) {
        if (wellness == null)
            wellness = WellnessFragment.newInstance();

        startFragment("", wellness);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openBodyMeasurement(String value) {
        if (bodymeasurement == null)
            bodymeasurement = BodyMeasurementFragment.newInstance();

        startFragment("", bodymeasurement);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openActivity(String value) {
        if (activity == null)
            activity = ActivityFragment.newInstance();

        startFragment("", activity);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }


    @Override
    public void openSleepCycle(String value) {
        if (sleepcycle == null)
            sleepcycle = SleepCycleFragment.newInstance();

        startFragment("", sleepcycle);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openHeatAndVitals(String value) {
        if (heatandvitals == null)
            heatandvitals = HeatAndVitalsFragment.newInstance();

        startFragment("", heatandvitals);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openAllergies(String value) {
        if (allergiesFragment == null)
            allergiesFragment = AllergiesFragment.newInstance();

        startFragment("", allergiesFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openVitalSigns(String value) {
        if (vitalSignsFragment == null)
            vitalSignsFragment = VitalSignsFragment.newInstance("", "");

        startFragment("", vitalSignsFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void checkLocation() {
        checkPermissionFirst(1);
    }

    // Logout ask dialog for confirmation.
    private void askFirst() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.are_sure_logout));
        builder.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
            dialog.dismiss();

            callLogout(SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_USER_ID, ""),
                    SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_DEVICE_ID, ""),
                    SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
            SharedPreferencesUtils.getInstance(MainActivity.this).clearAll(MainActivity.this);
        });
        builder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.cancel());

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    private String getFromDate(int addMonth, int minusYear) throws ParseException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = 01;

        int newMonth = (month + addMonth);
        if (newMonth > 12) {
            int newYear = newMonth / 12;
            year = (year + newYear);
            month = (newMonth - (newYear * 12));
        } else {
            month = newMonth;
        }


        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = originalFormat.parse(day + "-" + month + "-" + (year - minusYear));
        String formattedDate = targetFormat.format(date);

        return formattedDate;
    }

    // change language ask dialog for confirmation.
    private void askLanguageChange(String lang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);

        try {
            if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                builder.setTitle("Language!");
                builder.setMessage("Do you want to change language?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        callChangeLanguage(SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_MRN, ""), lang,
                                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
                        ai.medicus.utils.SharedPreferencesUtils.setLocalLanguage(Constants.ENGLISH);

                        if (MedicusUtilities.getPatient() != null)
                            MedicusUtilities.getPatient().updateLanguage("en");

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            } else {
                builder.setTitle("اللغة!");
                builder.setMessage("هل ترغب في تغيير اللغة؟");

                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue("toDate", getFromDate(2, 0));
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue("fromDate", getFromDate(0, 2));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        callChangeLanguage(SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_MRN, ""), lang,
                                SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
                        ai.medicus.utils.SharedPreferencesUtils.setLocalLanguage(Constants.ARABIC);

                        if (MedicusUtilities.getPatient() != null)
                            MedicusUtilities.getPatient().updateLanguage("ar");
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to LTR
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }


    @Override
    public void openProfileMedicineFragment(String value) {
//        if (profileFragment == null)
//            profileFragment = ProfileFragment.newInstance(value, "");
//        startFragment(getResources().getString(R.string.profile), profileFragment);
//        Common.CONTAINER_FRAGMENT = "ProfileFragment";
//        mainToolbar.setVisibility(View.GONE);
        medicine = value;
        selectTab(4);

    }

    @Override
    public void openPhysicianFragment(String value, String clinic_id) {
        if (clinic_id.length() == 0) {
            clinic_physician = Constants.TYPE.HOME_PHYSICIAN;
            selectTab(2);
        } else {
            physiciansFragment = PhysiciansFragment.newInstance();

            Bundle args = new Bundle();
            args.putString("param1", value);
            args.putString("param2", clinic_id);
            physiciansFragment.setArguments(args);

            startFragment("", physiciansFragment);
            Common.CONTAINER_FRAGMENT = value;
            mainToolbar.setVisibility(View.GONE);
        }

    }


    @Override
    public void openClinicFragment(String value) {
        clinic_physician = "";
        Common.CONTAINER_FRAGMENT = value;
        selectTab(2);
    }

    @Override
    public void openPaymentInfoFragment(Serializable value, String key) {
        if (paymentinfo == null)

            paymentinfo = PaymentInfoFragment.newInstance();

        Bundle args = new Bundle();
        args.putSerializable("reponse", value);
        paymentinfo.setArguments(args);

        startFragment("", paymentinfo);
        Common.CONTAINER_FRAGMENT = key;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openTelrFragment(Serializable book, Serializable res, String value) {

        if (telrfragment == null)
            telrfragment = TelrFragment.newInstance();

        Bundle args = new Bundle();
        args.putSerializable("booking", book);
        args.putSerializable("price", res);
        telrfragment.setArguments(args);

        startFragment("", telrfragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }


    @Override
    public void openMedicineFragment(String type, String value) {
        MedicinesPrescriptionsFragment medicineFragment = MedicinesPrescriptionsFragment.newInstance();
//            else
//                medicineFragment = MedicinesFragment.newInstance();

        Bundle args = new Bundle();
        args.putString("param1", value);
        medicineFragment.setArguments(args);

        startFragment("", medicineFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openPharmacyFragment(String rxNo, String episodeNo) {
        Fragment fragment = PharmacyFragment.getInstance(rxNo, episodeNo);
        Common.CONTAINER_FRAGMENT = "";
        startFragment("", fragment);
    }

    // Change language API request
    public void callChangeLanguage(String token, String user_id, String language, int hosp) {
        Common.showDialog(this);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.changeLanguage(user_id, language, hosp);
//
//        xxx.enqueue(new Callback<SimpleResponse>() {
//            @Override
//            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    SimpleResponse response1 = response.body();
//                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
//
//
//                        } else {
//                            makeToast(response1.getMessage());
//                        }
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                Common.hideDialog();
//
//                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
//                    makeToast(getString(R.string.time_out_messgae));
//                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    Common.noInternet(MainActivity.this);
//                } else
//                    makeToast(t.getMessage());
//            }
//        });

        changeLanguage(language);
        stopRepeatingTask();

        Intent i1 = new Intent(MainActivity.this, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();
    }

    // Logout API request
    public void callLogout(String token, String user_id, String deviceToken, int hosp) {
        Common.showDialog(this);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.logOut(user_id, deviceToken, hosp);
        Log.d("url", xxx.request().url().toString());
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        makeToast(response1.getMessage());

                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            stopRepeatingTask();
                            SharedPreferencesUtils.getInstance(MainActivity.this).clearAll(MainActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();
                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    makeToast(getString(R.string.time_out_messgae));
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(MainActivity.this);
                } else
                    makeToast(t.getMessage());
            }
        });
    }


    // check permission to save reports required storage permission
    void checkPermissionFirst(int i) {
        this.i = i;
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
//                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport reportP) {

                        if (reportP.areAllPermissionsGranted()) {
                            getLocation(i);
                        } else if (reportP.isAnyPermissionPermanentlyDenied()) {
                            Common.permissionDialog(MainActivity.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
                            token) {
                        token.continuePermissionRequest();/* ... */
                    }
                }).check();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                getLocation(i);
//
//            } else {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                }, 33);
//            }
//        } else
//            getLocation(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 33) {
            if (grantResults.length > 0) {
                getLocation(i);
            }
        }
    }

    // Session Expired dialog.
    private void sessionExpired() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.session_expired));
        builder.setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            SharedPreferencesUtils.getInstance(MainActivity.this).clearAll(MainActivity.this);
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    // exit alert dialog.
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.exit_alert));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    isExitApp = true;
                    if (VideoActivity.videoActivity != null) {
                        VideoActivity.videoActivity.finish();
                    }
                    if (MainChatActivity_New.mainChatActivity_new != null) {
                        MainChatActivity_New.mainChatActivity_new.finish();
                    }
//                    System.exit(4);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.CONTAINER_FRAGMENT = "BaseFragment";
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (SharedPreferencesUtils.getInstance(MainActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    //check if new version available on play store
//    void checkUpdatePlayStore() {
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo,
//                            AppUpdateType.FLEXIBLE,
//                            this,
//                            112);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    void getLocation(int i) {
        if (i == 1)
            if (isGPSEnabled(this)) {
                fn_getlocation();
            } else {
                if (builder == null) {
                    builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
                    builder.setTitle("Enable GPS");
                    builder.setMessage("Enable GPS to provide location")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialog.dismiss();

                                    builder = null;
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    builder = null;
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

        else if (isGPSEnabled(this)) {
            fn_getlocation();
        }
    }

    public static boolean isGPSEnabled(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    private void fn_getlocation() {
        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //Toast.makeText(this,"mainActvity"+longitude+" "+latitude,Toast.LENGTH_LONG).show();
                        Log.d("--latLongs", "fn_getlocation: "+latitude+"  "+longitude);
                        SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LAT, String.valueOf(latitude));
                        SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LONG, String.valueOf(longitude));
                    }
                }

//                mGpsTracker.connect();

            } else
//
                if (isGPSEnable) {
                    location = null;
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LAT, String.valueOf(latitude));
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LONG, String.valueOf(longitude));


                        }
                    }
                }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LAT, String.valueOf(latitude));
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LONG, String.valueOf(longitude));

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void showToolbar() {
        mainToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showNavigation() {
        showCenterIcon();
        tabs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNavigation() {
        hideCenterIcon();
        tabs.setVisibility(View.GONE);
    }

    @Override
    public void showCenterIcon() {
        appointCenterButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCenterIcon() {
        appointCenterButton.setVisibility(View.GONE);
    }

    public void selectHomeTab() {
        tabs.getTabAt(0).select();
    }

    //Change language method
    void changeLanguage2(String lan) {
        if (lan.equalsIgnoreCase(Constants.ENGLISH)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ARABIC);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
        }

        if (lng != null) {
            if (lng.equals("pip")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    private void showSpecialMessage(String specialMessage){

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setMessage(specialMessage)
                .setCancelable(true)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();


    }

    private void setBufferTime() {


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new UrlWithURLDecoder().getBufferTime();
        Log.e("url1",url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response",response);
                        try {


                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

                            org.json.JSONObject object  = new JSONObject(response);

                            SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("inperson_before_buffer_time", object.getString("INPERSON_APT_BEFORE_BUFFER_TIME"));
                            editor.putString("inperson_after_buffer_time", object.getString("INPERSON_APT_AFTER_BUFFER_TIME"));
                            editor.putString("telemed_before_buffer_time", object.getString("TELEMED_APT_BEFORE_BUFFER_TIME"));
                            editor.putString("telemed_after_buffer_time", object.getString("TELEMED_APT_AFTER_BUFFER_TIME"));
                            editor.putString("promis_days_before_booking", object.getString("PROMIS_DAYS_BEFORE_BOOKING"));
                            editor.putString("promis_days_after_booking", object.getString("PROMIS_DAYS_AFTER_BOOKING"));
                            editor.commit();

                        } catch (final JSONException e) {
                            Log.e("getBuffer", ": " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong aftab", Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                Log.e("tokentoken",token);
                Log.e("token",token);
                headers.put("Authorization", "Bearer " + token);

                return headers;
            }
        };;

// Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(stringRequest);

    }

}
