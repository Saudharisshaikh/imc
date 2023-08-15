package sa.med.imc.myimc.Telr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.Login.LoginActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.view.BookingViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.Records.view.ReportsViews;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.EmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.ViewModel.EmergencyCallViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.splash.SplashActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;

import static sa.med.imc.myimc.Utils.Common.ReportTypes.SLIP;

public class TelrActivity extends BaseActivity implements ReportsViews, BookingViews {
    public static final int PAYMENT_CODE = 900;
    private final String TAG_PANEL = "tag_panel";
    private final String TAG_BIOMARKER = "tag_biomarker";
    private final String TAG_REPORT_DETAILS = "tag_report_details";
    public final static String SUCCESS = "openHomeSuccess";
    public final static String FAIL = "openHomeFail";
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
    ReportsPresenter presenter;

    private ResponseBody responseBody;

    private static final int CREATE_SLIP_FILE = 6;


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
    WebView webView;


    boolean loadingFinished = true;
    boolean redirect = false;

    ImcDatabase db;

    int sick_leave_id = 0;


    TabLayout tabLayout;
    ImageView imageView, imageViewRecord, appoint_center_button, mayoimage, profileImage;
    TextView textView;
    RelativeLayout maintoolbar;

    String record_string = Constants.TYPE.RECORD_MAIN;
    String clinic_physician = "";
    String medicine = "";

    NextTimeResponse.Datum session;


    boolean onBackRecords = false, isGPSEnable = false, isNetworkEnable = false;

    FragmentManager fragmentManager;
    private int Id = 0;
    private String report_title = "";
    private String report_number = "";
    private String report_Data = "";

    List<SickLeave> sick_reports = new ArrayList<>();


    @Override
    protected Context getActivityContext() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telr);

        presenter = new ReportsImpl(this, this);

//        tabLayout=findViewById(R.id.tabs);
        imageView = findViewById(R.id.homeimage);
        imageViewRecord = findViewById(R.id.record1);
        appoint_center_button = findViewById(R.id.appoint_center_button);
        mayoimage = findViewById(R.id.mayoimage);
        profileImage = findViewById(R.id.profileImage);
        webView = findViewById(R.id.webView);

//        PIYUSH 01-04-2022
        if(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)){
            webView.setVisibility(View.GONE);
            paymentGateWaiting("Message!", "Payment for emergency calls will be integrated here");
        }


        if (getIntent().hasExtra("ref")) {
//            toolbarTitle.setText(getString(R.string.payment));
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
//            ivBack.setVisibility(View.GONE);

        } else {
            if (getIntent().getStringExtra("link").contains("mayo")) {
//                toolbarTitle.setText(getString(R.string.mayo_clinic));

            } else if (getIntent().getStringExtra("link").contains("matterport")) {
//                toolbarTitle.setText(getString(R.string.virtual_tour_imc));
            } else if (getIntent().getStringExtra("link").contains("symptom-checker")) {
//                toolbarTitle.setText(getString(R.string.symtom_tracker));
            } else if (getIntent().getStringExtra("link").contains("TeleHealth")) {
//                toolbarTitle.setText(getString(R.string.telemedcinice_consent_form));
            } else if (getIntent().getStringExtra("link").contains("wellness")) {
//                toolbarTitle.setText(getString(R.string.wellness));
            }
        }

        loadWeb(getIntent().getStringExtra("link"));


//        Intent intent = getIntent();


//        if (getArguments() != null) {
//            if (getArguments().containsKey("data")) {
//                physician = (PhysicianResponse.Physician) getArguments().getSerializable("data");
//                session = (NextTimeResponse.Datum) getArguments().getSerializable("session");
//            }
//        }

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

    public static void startActivity(Activity activity, String link) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("link", link);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String link, String ref) {
        Intent intent = new Intent(activity, TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        activity.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, String link, String ref) {
        Intent intent = new Intent(activity, TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        activity.startActivityForResult(intent,PAYMENT_CODE);
    }

    public static void startTitleActivityForResult(Activity activity, String link,String title) {
        Intent intent = new Intent(activity, TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent,PAYMENT_CODE);
    }

    public static void startTitleActivityForResult(Fragment activity, String link,String title) {
        Intent intent = new Intent(activity.requireActivity(), TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent,PAYMENT_CODE);
    }

    public static void startActivity(Activity activity, String link, String ref, String from, String id, String arrival) {
        Intent intent = new Intent(activity, TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        intent.putExtra("id", id);
        intent.putExtra("arrival", arrival);
        intent.putExtra("from", from);
        activity.startActivity(intent);
    }

    public static void startActivityForResult(Fragment fragment, String link, String ref, String from, String id, String arrival) {
        Intent intent = new Intent(fragment.getContext(), TelrActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        intent.putExtra("id", id);
        intent.putExtra("arrival", arrival);
        intent.putExtra("from", from);
        fragment.getActivity().startActivityForResult(intent,PAYMENT_CODE);
    }

    void loadWeb(String url) {
        webView.invalidate();
//        webView.clearCache(true);
//        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(50);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setLayerType(View.LAYER_TYPE_NONE, null);
        webView.getSettings().setDomStorageEnabled(true);

        if (getIntent().getStringExtra("link").contains("matterport")) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        webView.loadUrl(url);

        this.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                logger("link ", url);

                if (getIntent().getStringExtra("link").contains("symptom-checker")) {
                    if (url.contains("imc.app.link") && SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_MRN, "").length() > 0) {
                        MainActivity.isFromSymptom = true;
                        finish();
                    } else if (url.contains("imc.app.link")) {
                        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                    // open doctor list if symptom checker result clinic clicked
                    // if app opened with link - deep linking process functionality
                    if (url.contains("clinicsMapping") && SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_MRN, "").length() > 0) {
                        String clinic_id = url.split("\\?")[1].split("=")[1];
                        SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_CLINIC_ID, clinic_id);

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else if (url.contains("clinicsMapping")) {
                        String clinic_id = url.split("\\?")[1].split("=")[1];
                        SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_CLINIC_ID, clinic_id);

                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }

                }
                else if (url.contains("payement-cancelled") || url.contains("doctor/cancelled")){
                    finish();
                }

                if (url.contains("tel:"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+" + url.replace("tel:", ""))));
                else
                    view.loadUrl(url);

                if (getIntent().hasExtra("from")) {
                    if (url.contains("selfcheckin/payement-authorized") || url.contains("selfcheckin/payement-declined")) {//|| url.contains("selfcheckin/payement-declined")
                        webView.setVisibility(View.GONE);
                        verifyPaymentCheckIn(SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getIntent().getStringExtra("ref"), "back", getIntent().getStringExtra("id"), getIntent().getStringExtra("arrival"),
                                SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

                    }

                } else {
                    if (url.contains("doctor/declined")) {
                        webView.setVisibility(View.GONE);
                        verifyPayment(SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getIntent().getStringExtra("ref"), url);
                    } else if (url.contains("doctor/authorized")) {
                        webView.setVisibility(View.GONE);
                        verifyPayment(SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getIntent().getStringExtra("ref"), url);
                    }
                }

                return super.shouldOverrideUrlLoading(view, url);
//                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                logger("link page ", url);

//                WindowManager manager = (WindowManager) TelrActivity.this.getSystemService(Context.WINDOW_SERVICE);
//
//                DisplayMetrics metrics = new DisplayMetrics();
//                manager.getDefaultDisplay().getMetrics(metrics);
//
//                metrics.widthPixels /= metrics.density;

                if (url.equalsIgnoreCase("https://secure.telr.com/tools/demoacs.html")) {
                    webView.loadUrl("javascript:document.body.style.zoom = " + String.valueOf(
                            3) + ";");
                }


//                if (url.contains("/ar/")) {
//                    layyy.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                    tvRequest.setText("طلب موعد");
//
//                } else if (url.contains("/en/")) {
//                    tvRequest.setText("Request an Appointment");
//                    layyy.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                }

//                if (url.contains("doctors") || url.contains("doctorsprofile") || url.contains("departments") || url.contains("department")) {
//                    webView.setVisibility(View.VISIBLE);
//                } else {
//                    webView.setVisibility(View.GONE);
//                }
                if (url.contains("http://192.168.1.49:8080") || url.contains("https://patientportal.imc.med.sa")) {
                    if (!redirect) {
                        webView.setVisibility(View.GONE);
                    }
//                    pdLoad.setVisibility(View.GONE);
                } else {
                    if (!url.contains("auth/login")) {
                        webView.setVisibility(View.VISIBLE);
                    }
                    else{
                        webView.setVisibility(View.GONE);
                    }
                    redirect = false;
//                    pdLoad.setVisibility(View.GONE);
                }
            }

        });
    }


    void verifyPaymentCheckIn(String token, String refId, String url, String bookingId, String updateArrival, int hosp) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {
            object.put("refId", refId);
            object.put("bookingId", bookingId);
            object.put("updateArrival", updateArrival);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.verifyPaymentCheckIn(body);

        xxx.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    BookResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getMethod() == null) {
                            if (response1.getStatus().equalsIgnoreCase("true")) {
                                showAlert(TelrActivity.this, getString(R.string.success), response1.getMessage(), R.drawable.ic_successful_icon_green);
                            } else {
                                showAlert(TelrActivity.this, getString(R.string.failed), response1.getMessage(), R.drawable.ic_unsuccessful_icon);
//                                makeToast(response1.getMessage());
                            }
                        } else {
                            if (url.equalsIgnoreCase("back")) {
                                finish();
                            } else {
                                if (response1.getOrder() != null)
                                    if (response1.getOrder().getTransaction() != null)
                                        if (response1.getOrder().getTransaction().getMessage() != null)
                                            showAlert(TelrActivity.this, getString(R.string.failed), response1.getOrder().getTransaction().getMessage(), R.drawable.ic_unsuccessful_icon);
                                        else
                                            showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                    else
                                        showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                else
                                    showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                            }
                        }
                    } else {
                        makeToast(getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    makeToast(getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Common.hideDialog();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    makeToast(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(TelrActivity.this);
                } else {
                    message = "Unknown";
                    makeToast(message);

                }
            }
        });
    }

    public void showAlert(Activity activity, String title, String msg, int logo) {
        webView.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setIcon(logo);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            if (title.equalsIgnoreCase(getString(R.string.success))) {
                try {
                    Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                    refresh.putExtra("cancel", "cancel");
                    LocalBroadcastManager.getInstance(TelrActivity.this).sendBroadcast(refresh);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            finish();
        });
        if (logo != R.drawable.ic_unsuccessful_icon) {
            builder.setNeutralButton(getResources().getString(R.string.string_download_invoice), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    presenter.callGeneratePaymentPdf(SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.KEY_MRN, ""),
                            SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.SELECTED_HOSPITAL, 0),
                            SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.SESSION_ID, 0),
                            getIntent().getStringExtra("ref"));


//                Dexter.withContext(getActivityContext())
//                        .withPermissions(
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE
//                        ).withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//                        if (reportP.areAllPermissionsGranted()) {
//                            callApi();
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
//                            token) {
//                        token.continuePermissionRequest();
//                        /* ... */
//                    }
//                }).check();
                }
            });
        }
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



    private void alterDocument(Uri uri) {
        try {
            int count = -1;
            ParcelFileDescriptor pfd = TelrActivity.this.getContentResolver().
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


    void verifyPayment(String token, String refId, String url) {
        Common.showDialog(this);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain; charset=utf-8"), refId);

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.verifyPayment(body);

        xxx.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    BookResponse response1 = response.body();
                    if (response1 != null) {
                        if (url.contains("doctor/declined")) {
                            showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                        } else if (url.contains("doctor/authorized")) {
                            showAlert(TelrActivity.this, getString(R.string.success), getString(R.string.payment_successful), R.drawable.ic_successful_icon_green);
                        }

                       else if (response1.getMethod() == null) {
                            if (response1.getStatus().equalsIgnoreCase("true")) {
                                onBookAppointmentSuccess(response1);
                            } else {
                                makeToast(response1.getMessage());
                            }
                        } else {
                            if (url.equalsIgnoreCase("back")) {
                                finish();
                            } else {
                                if (response1.getOrder() != null)
                                    if (response1.getOrder().getTransaction() != null)
                                        if (response1.getOrder().getTransaction().getMessage() != null)
                                            showAlert(TelrActivity.this, getString(R.string.failed), response1.getOrder().getTransaction().getMessage(), R.drawable.ic_unsuccessful_icon);
                                        else
                                            showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                    else
                                        showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                else
                                    showAlert(TelrActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                            }
                        }
                    } else {
                        makeToast(getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    makeToast(getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Common.hideDialog();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    makeToast(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(TelrActivity.this);
                } else {
                    message = "Unknown";
                    makeToast(message);
                }
            }
        });
    }


    public void onBookAppointmentSuccess(BookResponse response) {
        String statusMsg = "", title = getString(R.string.failed);

        switch (response.getData().getStatus()) {
            case 99:
                title = getString(R.string.success);
                statusMsg = getString(R.string.success_booking);//"Booked successfully";
                break;

            case -4:
                statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                break;

            case 0:
                String phone = "";
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                    phone = preferences.getString("ph", "") + "+";
                } else
                    phone = "+" + preferences.getString("ph", "");

                statusMsg = getString(R.string.booking_exist, phone);//"Bookings exists";
                break;

            case -5:
                statusMsg = getString(R.string.no_show_message);//"Bookings no_show";
                break;

            case -2:
                statusMsg = getString(R.string.booking_time_in_other_clinic);//"booking.exists_time_allowed";
                break;

            // Max 3 bookings per day configurable from admin side in config table
            case -6:
                statusMsg = getString(R.string.booking_max);//"booking.max_total_booking_reached";
                break;

            case -7:
                statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                break;

            case -8:
                statusMsg = getString(R.string.booking_age_less);// "booking.age_less_than_required";
                break;

            case -9:
                statusMsg = getString(R.string.booking_age_more);//"booking.age_more_than_required";
                break;

            case -10:
                statusMsg = getString(R.string.booking_gender);//"booking.gender_conflict";
                break;

            case -50:
                statusMsg = response.getMessage();//"booking failed for guest user ";
                break;

            case -51:
                statusMsg = response.getMessage();//"booking failed for guest user max limit exceeds";
                break;

            case 111:
                statusMsg = getString(R.string.pending_pre_authorization);
                break;


            default:
                statusMsg = getString(R.string.booking_failed);//"bookings.failed";
                break;
        }
        bookingStatusDialog(title, statusMsg, response.getData().getStatus());

        // Show alert

    }

    public void bookingStatusDialog(String title, String message, int status) {
//        if (status == 99)
//            showSnackBarMsg(getString(R.string.booking_confirmed));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (status == 99) {

                            if (SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                                Intent i = new Intent(TelrActivity.this, GuestHomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent i1 = new Intent(TelrActivity.this, MainActivity.class);
                                i1.putExtra("restart", "yes");
                                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);
                            }
                        }


                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(TelrActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    void downloadFile(ResponseBody responseBody, Headers headers) {
        try {
            int count = 0;
            String depo = headers.get("Content-Disposition");
            String[] depoSplit = depo.split("filename=");
            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();

            filename = SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.KEY_MRN, "") + "_payment_confirmation_" + System.currentTimeMillis() + ".pdf";
            // download the file
            InputStream input = new BufferedInputStream(responseBody.byteStream(), 8192);
            String path = Environment.getExternalStorageDirectory().toString() + Constants.FOLDER_NAME;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            path = file.getPath() + "/" + filename;
            // Output stream
            OutputStream output = new FileOutputStream(path);

            byte[] data = new byte[1024];

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            // Show download success and ask to open file
//            openFile(path);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }

    private void setSuccess(Uri uri){
        Intent intent = new Intent();
        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra(SUCCESS,true);
        setResult(RESULT_OK,intent);
        finish();
    }

    void openFile(Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TelrActivity.this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        builder.setMessage(getResources().getString(R.string.download_invoice));
        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSuccess(uri);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.later), (dialog, which) -> dialog.cancel());

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(TelrActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    void saveToLocal(String path) {
//        db.sickLeaveDataDao().saveSickLeavePath(path, sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
        sick_leave_id = 0;
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
        downloadFile(response, headers);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_SLIP_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                alterDocument(uri);
                openFile(uri);
            }
        }
        else if (requestCode == CREATE_SLIP_FILE) {
            Intent intent = new Intent();
            intent.putExtra(FAIL,true);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {
//        downloadFile(response, headers);
        responseBody = response;
        initDownloadFileFlow(SLIP);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case SLIP:
                String slipFile = SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_MRN, "") + "_Payment_Confirmation_" + System.currentTimeMillis() + ".pdf";
                Common.createFile(this,slipFile, CREATE_SLIP_FILE);
                break;
        }
    }

    @Override
    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> response, int totalItems) {

    }

    @Override
    public void onGetPin(PinResponse response) {

    }

    @Override
    public void onGenerateSmartReport(ResponseBody response, Headers headers) {

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

    }

    @Override
    public void onNoInternet() {

    }

    @Override
    public void onGetBookings(BookingResponse response) {

    }

    @Override
    public void onGetBookingsNew(BookingResponseNew response) {

    }

    @Override
    public void onReschedule(SimpleResponse response) {

    }

    @Override
    public void onCancel(SimpleResponse response) {

    }

    @Override
    public void onDownloadConfirmation(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGetPrice(PriceResponse response) {

    }

//    Emergency Call Dailog
    void paymentGateWaiting(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clickToStartVideoCall();
            }
        });


        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(TelrActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }

    public void clickToStartVideoCall() {
        try {
            String lang;
            if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            }
            else {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            }
            EmergencyCallViewModel viewModel = ViewModelProviders.of(this).get(EmergencyCallViewModel.class);
            viewModel.init();
            EmergencyCallRequestModel emergencyCallRequestModel = new EmergencyCallRequestModel();
            String mrnNumber = SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_MRN, null);
            emergencyCallRequestModel.setUserId(mrnNumber);
            emergencyCallRequestModel.setUserEmail("piyush@gmail.com");

            String token = SharedPreferencesUtils.getInstance(TelrActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, null);
            viewModel.EmergencyCall(token, lang, emergencyCallRequestModel);
            viewModel.getVolumesResponseLiveData().observe(this, response -> {

                if (response != null) {
                    if (response.getStatus()) {
                        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Dexter.withContext(TelrActivity.this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        SharedPreferencesUtils.getInstance(TelrActivity.this).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ACCESS_TOKEN, response.getData().getAccessToken());
                                        SharedPreferencesUtils.getInstance(TelrActivity.this).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_BACK_CLASS, "2");
                                        SharedPreferencesUtils.getInstance(TelrActivity.this).setValue(Constants.KEY_VIDEO_ROOMNAME, response.getRoomName());
                                        SharedPreferencesUtils.getInstance(TelrActivity.this).setValue(Constants.KEY_VIDEO_PHYSICIAN, response.getDoctorId());

                                        Intent in = new Intent(TelrActivity.this, VideoActivity.class);
                                        startActivity(in);
                                        finish();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                    }
                } else {
                    Common.makeToast(this, "Some thing went wrong ");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}