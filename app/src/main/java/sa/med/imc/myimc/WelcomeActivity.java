package sa.med.imc.myimc;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;
import static sa.med.imc.myimc.Utils.Common.simpleDailog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentContainerView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Departments.view.DepartmentsActivity;
import sa.med.imc.myimc.HealthTips.view.HealthTipsActivity;
import sa.med.imc.myimc.Login.LoginActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.AllDoctorBeforeLoginActivity;
import sa.med.imc.myimc.Physicians.FindAllDoctorActivity;
import sa.med.imc.myimc.Physicians.FindDoctorActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansActivity;
import sa.med.imc.myimc.Records.view.SingleFragmentActivity;
import sa.med.imc.myimc.RequestAppointment.view.RequestAppointmentActivity;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.SignUp.activity.SelfRegistrationActivity;
import sa.med.imc.myimc.SignUp.view.SignUpStep1Activity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;
import sa.med.imc.myimc.Wellness.Wellness_WebView;
import sa.med.imc.myimc.splash.SplashActivity;


public class WelcomeActivity extends BaseActivity {

    //    @BindView(R.id.tv_sign_in)
//    TextView tvSignIn;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    //    @BindView(R.id.lay_way_finder)
//    TextView laywayfinder;
    @BindView(R.id.lay_call_us)
    LinearLayout cardCallUs;
    @BindView(R.id.tv_english)
    TextView tvEnglish;
    @BindView(R.id.tv_arabic)
    TextView tvArabic;

  @BindView(R.id.smallContainer)
  FragmentContainerView smallContainer;

    String currentVersion = "", onlineVersion = "", force = "NO";
    @BindView(R.id.tv_login_guest)
    TextView tvLoginGuest;
    @BindView(R.id.lay_symtom_tracker)
    RelativeLayout laySymtomTracker;
    @BindView(R.id.tv_english_new)
    TextView tvEnglishNew;
    Animation animSlide, animSlideAr;
    @BindView(R.id.tv_arabic_new)
    TextView tvArabicNew;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.layLangueButton)
    RelativeLayout layLangueButton;
    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.layNewLanguageButton)
    RelativeLayout layNewLanguageButton;

    @BindView(R.id.lay_hhc)
    RelativeLayout lay_hhc;

    @BindView(R.id.lay_virtual_family)
    RelativeLayout lay_virtual_family;

    @BindView(R.id.chatBot)
    ImageView chatBot;

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

   // @BindView(R.id.iv_text_logo)
   // ImageView iv_text_logo;

    @BindView(R.id.iv_doc_pic)
    ImageView iv_doc_pic;


    @BindView(R.id.iv_back)
    ImageView iv_back;


    @BindView(R.id.tv_label_welcome)
    TextView tv_label_welcome;

    @BindView(R.id.lay_departments)
    LinearLayout lay_departments;

    @BindView(R.id.lay_main_fc)
    LinearLayout lay_main_fc;

    @BindView(R.id.lay_way_finder)
    RelativeLayout erlaywayfinder;


    int windowwidth;
    int windowheight;
    private ViewGroup mRrootLayout;
    RelativeLayout.LayoutParams parms;
    LinearLayout.LayoutParams par;
    String lang = Constants.ENGLISH,code="";

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.messageDailog(WelcomeActivity.this, String.valueOf(R.string.txt_form_submitted));
        }
    };

    @Override
    protected Context getActivityContext() {
        return this;
    }

    void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(WelcomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                logger("token:   ", newToken);
//                SharedPreferencesUtils.getInstance(WelcomeActivity.this).setValue(Constants.KEY_DEVICE_ID, newToken);
            }
        })
                .addOnFailureListener(WelcomeActivity.this, Throwable::printStackTrace);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(WelcomeActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        if (getIntent().getBooleanExtra("isPopUp",false)){
            simpleDailog(this,getIntent().getStringExtra("title"),getIntent().getStringExtra("message"));
        }
        if (!MainActivity.active)
            SharedPreferencesUtils.getInstance(this).clearSomeValues(this);

        lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        try {
            LocalBroadcastManager.getInstance(WelcomeActivity.this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.Filter.REPORT_REQUEST));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButterKnife.bind(this);
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0)==4){
            lay_hhc.setVisibility(View.GONE);
        }

        getPhoneNumber("", SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

        if (!BuildConfig.DEBUG) {
//            checkUpdatePlayStore();
        }




        if (BuildConfig.DEBUG) {
            // open doctor list if symptom checker result clinic clicked
            // if app opened with link - deep linking process functionality
            // ATTENTION: This was auto-generated to handle app links.
            tvTest.setVisibility(View.VISIBLE);
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();


            if (appLinkData != null) {
                String url = appLinkData.toString();
                String clinic_id = url.split("\\?")[1].split("=")[1];

                logger("data", appLinkData.getPath() + "   clinic_id==" + clinic_id);
                logger("action", appLinkAction + "");
                if (clinic_id != null) {
                    if (clinic_id.length() > 0) {
                        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_CLINIC_ID, clinic_id);
                        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, "").length() > 0) {

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }

            chatBot.setVisibility(View.VISIBLE);
        } else {
            tvTest.setVisibility(View.GONE);
        }

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Your code.
                onBackPressed();
            }
        });

        ////Aftab Code
        SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
        String hospitalLargeImage = sp.getString("hospitalLargeImage",null);
        String hospitalLogo = sp.getString("hospitalLogo",null)+"?12345";
        String locations = sp.getString("hospitals_locations",null);
        String hospitalId = sp.getString("hospitalId",null);

        String specialMessage = sp.getString("specialMessage",null);

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            specialMessage = sp.getString("specialMessage_ar",null);
        }

        if(!specialMessage.equals("")) {
            showSpecialMessage(specialMessage);
        }
        ImageView iv_doc_pic = findViewById(R.id.iv_doc_pic);
        Picasso.get().load(hospitalLargeImage)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).fit().into(iv_doc_pic);


        ImageView iv_logo = findViewById(R.id.iv_logo);
        Picasso.get().load(hospitalLogo)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).resize(900, 300)
                .centerInside().into(iv_logo);

        chatBot.setVisibility(View.VISIBLE);

//
//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0) == 4) {
//            //     tvRegister.setVisibility(View.GONE);
//        } else {
//            tvRegister.setVisibility(View.VISIBLE);
//        }


        animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_language);
        animSlideAr = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_language);

        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0) == 4) {
           // iv_logo.setVisibility(View.GONE);
            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
              //  iv_text_logo.setImageResource(R.drawable.logo_2);
            } else {
              //  iv_text_logo.setImageResource(R.drawable.logo_1);
            }
            smallContainer.setVisibility(View.GONE);
        } else {
          //  iv_logo.setVisibility(View.VISIBLE);
          //  iv_text_logo.setImageResource(R.drawable.logo_text);
            smallContainer.setVisibility(View.VISIBLE);

        }

//        FacebookSdk.setAutoLogAppEventsEnabled(false);
//        AppEventsLogger logger = AppEventsLogger.newLogger(this);
//        logger.logEvent(AppEventsConstants.EVENT_NAME_AD_CLICK);

//        FacebookSdk.setIsDebugEnabled(true);
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ARABIC);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);

            ImageView Wellness_img = findViewById(R.id.Wellness_img);
            Wellness_img.setImageDrawable(getResources().getDrawable(R.drawable.welness_arabic_img));


            tvEnglishNew.startAnimation(animSlide);
            animSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tvArabicNew.setVisibility(View.GONE);
                    tvEnglishNew.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvArabicNew.setVisibility(View.GONE);
                    tvEnglishNew.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);

            tvArabicNew.startAnimation(animSlideAr);
            animSlideAr.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tvArabicNew.setVisibility(View.VISIBLE);
                    tvEnglishNew.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvArabicNew.setVisibility(View.VISIBLE);
                    tvEnglishNew.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });


        }
        String time = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_CACHE_TIME, "");
        logger("time   ", "" + time);
        if (!time.equalsIgnoreCase(Common.getCurrentDatePayOnly()))
            deleteCache(this);

        getToken();

        if (BuildConfig.DEBUG) {
            // tvLoginGuest.setVisibility(View.VISIBLE);
            tvTest.setVisibility(View.VISIBLE);
        } else {
            tvTest.setVisibility(View.GONE);
        }
        hideViews();
        receiveArguments();

        String hospital_code = SharedPreferencesUtils.getInstance(getActivityContext()).getValue(HOSPITAL_CODE, "IMC");

        if (hospital_code.equalsIgnoreCase("FC") || hospital_code.equalsIgnoreCase("KH")){

            lay_hhc.setVisibility(View.GONE);
            lay_virtual_family.setVisibility(View.GONE);

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

    private void receiveArguments() {
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
           // iv_logo.setVisibility(View.GONE);
            tv_label_welcome.setText(getString(R.string.welcome_text_FC));

            laySymtomTracker.setVisibility(View.GONE);
            lay_virtual_family.setVisibility(View.GONE);
            chatBot.setVisibility(View.GONE);
            lay_departments.setVisibility(View.INVISIBLE);
            lay_main_fc.setVisibility(View.VISIBLE);
            //iv_doc_pic.setImageResource(R.drawable.option2);
            erlaywayfinder.setVisibility(View.GONE);
        }
    }

    //check if new version available on play store
    void checkUpdatePlayStore() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            112);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }





    @OnClick({R.id.lay_hhc,R.id.chatBot, R.id.lay_symtom_tracker, R.id.tv_login_guest, R.id.lay_virtual_family, R.id.lay_way_finder, R.id.lay_find_doctor, R.id.lay_health_byte, R.id.lay_departments, R.id.lay_main_fc, R.id.tv_sign_in, R.id.tv_register, R.id.lay_call_us, R.id.tv_english, R.id.tv_arabic, R.id.tv_english_new, R.id.tv_arabic_new})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.chatBot:
                if (SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_CHATBOT_LINK, "").length() == 0)
                    getPhoneNumber("yes", SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                else
                    Common.showChatBot(this);
                break;

            case R.id.tv_login_guest:
                Intent gues = new Intent(getApplicationContext(), RequestAppointmentActivity.class);
                startActivity(gues);
                break;

            case R.id.lay_hhc:
                SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_MRN_FOR_HHC,"314134");
                SingleFragmentActivity.startActivity(this,Constants.TYPE.HHC);
                break;


            case R.id.lay_symtom_tracker:
                WebViewActivity.startActivity(this, BuildConfig.SYMPTOM_CHECKER_LINK);
                break;

            case R.id.lay_way_finder:
//                CalculatorsActivity.startActivity(this);
                showLocation();
                break;

            case R.id.lay_find_doctor:
//                PhysiciansActivity.startActivity(this);
                Intent intent=new Intent(WelcomeActivity.this, AllDoctorBeforeLoginActivity.class);
                startActivity(intent);
                break;

            case R.id.lay_health_byte:

                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC))
                    WebViewActivity.startActivity(this, WebUrl.WellnessArabic);
                else
                    WebViewActivity.startActivity(this, WebUrl.WellnessEnglish);

//                String url_en = "https://www.imc.med.sa/en/wellness";
//                String url_ar = "https://www.imc.med.sa/ar/wellness";
//                Intent intent = new Intent(getActivityContext(), Wellness_WebView.class);
//                intent.putExtra("WellnessEnglish", url_en);
//                intent.putExtra("WellnessArabic", url_ar);
//                startActivity(intent);

//                HealthTipsActivity.startActivity(this);
                break;

            case R.id.lay_departments:
                Intent dd = new Intent(getApplicationContext(), DepartmentsActivity.class);
                startActivity(dd);
                break;

            case R.id.lay_main_fc:
                showLocation();
                break;

            case R.id.tv_sign_in:
                if (BuildConfig.DEBUG) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                } else
                    checkVersion();  //need in release build

                break;

            case R.id.tv_register:
                Intent reg = new Intent(getApplicationContext(), SignUpStep1Activity.class);
                startActivity(reg);

                break;

            case R.id.lay_virtual_family:
                WebViewActivity.startActivity(this, WebUrl.VIRTUAL_TOUR_LINK);
                break;

            case R.id.lay_call_us:
                SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_MRN_FOR_HHC,"314134");
                SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.IS_BEFORE_LOGIN,true);
                cardCallUs.setEnabled(false);
                ContactOptionsActivity.startActivityFromLogin(this);
                break;

            case R.id.tv_english:
            case R.id.tv_english_new:
                if (tvEnglishNew.getText().length() == 1) {
                    TranslateAnimation animObj = new TranslateAnimation(tvEnglishNew.getWidth(), 0, 0, 0);
                    animObj.setDuration(1000);
                    tvEnglishNew.startAnimation(animObj);
                    tvEnglishNew.setText(" EN ");
                    hideViews();

                } else {
                    changeLanguage(Constants.ENGLISH);
                }
                break;

            case R.id.tv_arabic:
            case R.id.tv_arabic_new:
                if (tvArabicNew.getText().length() == 1) {
                    TranslateAnimation animObj = new TranslateAnimation(tvArabicNew.getWidth(), 0, 0, 0);
                    animObj.setDuration(1000);
                    tvArabicNew.startAnimation(animObj);
                    tvArabicNew.setText("عربى");
                    hideViews();
                } else {
                    changeLanguage(Constants.ARABIC);
                }
                break;
        }
    }


    //Change language method
    void changeLanguage(String lan) {
        if (lan.equalsIgnoreCase(Constants.ENGLISH)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ARABIC);
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
        }

        Intent i1 = new Intent(WelcomeActivity.this, WelcomeActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();
        WelcomeActivity.this.overridePendingTransition(0, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cardCallUs != null) {
            cardCallUs.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Show IMC on map
    void showLocation() {
        String uri;


        Log.e("code",code);

        switch(code){

            case "FC":
                uri = String.format(Locale.ENGLISH, "geo:%f,%f", 21.5757757, 39.1253487);
                break;

            case "KH":
                uri = String.format(Locale.ENGLISH, "geo:%f,%f", 22.315699095916358, 39.1062226576705);
                break;
            default:
                uri = String.format(Locale.ENGLISH, "geo:%f,%f", 21.513551, 39.174131);

        }

        /*
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0) == 1) {
            uri = String.format(Locale.ENGLISH, "geo:%f,%f", 21.513551, 39.174131);
        } else {
            uri = String.format(Locale.ENGLISH, "geo:%f,%f", 21.5757757, 39.1275374);
        }*/

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    // Get support team toll free number from server
    void getPhoneNumber(String from, int hosp) {
        WebService webService = ServiceGenerator.createService(WebService.class);
        Call<SimpleResponse> xxx = webService.getPhoneNumber(hosp);

        Log.e("xxx",xxx.request().url().toString());
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_CHATBOT_LINK, response1.getChatBotLink());

                            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
                            force = response1.getUpdateApp();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("ph", response1.getPhoneNumber().replace(" ", "").replace("+", ""));
                            editor.apply();

                            Log.e("phph",preferences.getString("ph",""));

                            if (from.length() > 0) {
                                Common.showChatBot(WelcomeActivity.this);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();

                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = getString(R.string.time_out_messgae);
//                    Common.makeToast(WelcomeActivity.this, message);
                    Toast.makeText(WelcomeActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(WelcomeActivity.this);
                } else {
                    message = "Unknown";
                    Common.makeToast(WelcomeActivity.this, message);
                }
            }
        });
    }

    // Check current and play store published app version
    void checkVersion() {
        currentVersion = BuildConfig.VERSION_NAME;
        try {
            new GetVersionCode().execute();
        } catch (Exception e) {
            Common.hideDialog();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

    }


    @SuppressLint("StaticFieldLeak")
    class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Common.showDialog(WelcomeActivity.this);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = getPlayStoreAppVersion("https://play.google.com/store/apps/details?id=" + WelcomeActivity.this.getPackageName() + "&hl=en");
            return newVersion;
        }


        @Override

        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Common.hideDialog();
            WelcomeActivity.this.onlineVersion = onlineVersion;

            if (onlineVersion != null) {

                if (onlineVersion.isEmpty())
                    onlineVersion = currentVersion;

                if (currentVersion.compareTo(onlineVersion) > 0) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    if (currentVersion.equalsIgnoreCase(onlineVersion)) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else
                        //show alert
                        updateAvailable(WelcomeActivity.this, force);
                }

            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
            logger("update", "Current version " + currentVersion + "  playstore version " + onlineVersion);
        }
    }

    // If new update available then show alert to update application
    void updateAvailable(Activity activity, String force) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.update_available))
                .setMessage(activity.getString(R.string.update_meaage, onlineVersion))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.update_now), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

        if (force.equalsIgnoreCase("NO"))
            builder.setNegativeButton(activity.getString(R.string.later), (dialog, which) -> {
                dialog.dismiss();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dlg -> {
            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(WelcomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alertDialog.show();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            boolean s = deleteDir(dir);
            if (s)
                SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_CACHE_TIME, Common.getCurrentDatePayOnly());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private String getAppVersion(String patternString, String inputString) {
        try {
            //Create a pattern
            Pattern pattern = Pattern.compile(patternString);
            if (null == pattern) {
                return null;
            }

            //Match the pattern string in provided string
            Matcher matcher = pattern.matcher(inputString);
            if (null != matcher && matcher.find()) {
                return matcher.group(1);
            }

        } catch (PatternSyntaxException ex) {

            ex.printStackTrace();
        }

        return null;
    }

    private String getPlayStoreAppVersion(String appUrlString) {
        final String currentVersion_PatternSeq = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>";
        final String appVersion_PatternSeq = "htlgb\">([^<]*)</s";
        String playStoreAppVersion = null;

        BufferedReader inReader = null;
        URLConnection uc = null;
        StringBuilder urlData = new StringBuilder();
        try {
            final URL url = new URL(appUrlString);

            uc = url.openConnection();

            if (uc == null) {
                return null;
            }
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            inReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            if (inReader != null) {
                String str = "";
                while ((str = inReader.readLine()) != null) {
                    urlData.append(str);
                }
            }

            // Get the current version pattern sequence
            String versionString = getAppVersion(currentVersion_PatternSeq, urlData.toString());
            if (null == versionString) {
                return null;
            } else {
                // get version from "htlgb">X.X.X</span>
                playStoreAppVersion = getAppVersion(appVersion_PatternSeq, versionString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playStoreAppVersion;
    }

    void hideViews() {

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                if (tvEnglishNew.getText().length() > 1) {
                    TranslateAnimation animObj = new TranslateAnimation(0, (tvEnglishNew.getWidth() / 2) - 60, 0, 0);
                    animObj.setDuration(1000);
                    tvEnglishNew.startAnimation(animObj);
                    tvEnglishNew.setText("E");
                }
            } else {
                if (tvArabicNew.getText().length() > 1) {
                    TranslateAnimation animObj = new TranslateAnimation(0, (tvArabicNew.getWidth() / 2) - 70, 0, 0);
                    animObj.setDuration(1000);
                    tvArabicNew.startAnimation(animObj);
                    tvArabicNew.setText("ع");


                }
            }
//            if (x == 0)
//                x = firstx;
//
//            if (y == 0)
//                y = firsty;
//
//            chatBot.setX(x - dx);
//            chatBot.setY(y - dy);
        }, 3000);


    }

}
