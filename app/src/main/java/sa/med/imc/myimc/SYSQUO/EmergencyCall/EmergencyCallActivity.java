package sa.med.imc.myimc.SYSQUO.EmergencyCall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Calendar;

import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.model.SessionDatesResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsNextResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.Appointmnet.presenter.AppointmentImpl;
import sa.med.imc.myimc.Appointmnet.presenter.AppointmentPresenter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentViews;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Utils.Common;


public class EmergencyCallActivity extends BaseActivity implements AppointmentViews {
    CheckBox check2, check1;
    TextView tv_yes;
    RelativeLayout main_toolbarEmer;
    WebView contentWeb;
    private BottomSheetBehavior behaviorBtmsheet;
    String mUserType = "";

    private AppointmentPresenter presenter;
    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysquo_activity_emergency_call);

        mUserType = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "");
        presenter = new AppointmentImpl(this, this);
        tv_yes = findViewById(R.id.tv_yes_c);
        TextView tv_no = findViewById(R.id.tv_no_c);
        contentWeb = findViewById(R.id.tv_content);

        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);

        check1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (b && check2.isChecked()) {
                    tv_yes.setEnabled(true);

                } else {
                    tv_yes.setEnabled(false);
                }
            }
        });

        check2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (b) {
                    showConsentPopUpAgree();
                } else {
                    tv_yes.setEnabled(false);
                }
                check2.setChecked(false);
            } else if (!b) {
                tv_yes.setEnabled(false);
            }
        });

        contentWeb.getSettings().setJavaScriptEnabled(true);
        contentWeb.getSettings().setLoadWithOverviewMode(true);
        contentWeb.getSettings().setUseWideViewPort(true);
        contentWeb.getSettings().setAllowContentAccess(true);
        contentWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        contentWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        contentWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        contentWeb.getSettings().setSupportZoom(true);

        String outhtml = getString(R.string.telemedcinice_consent_content);

        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getData("rtl", outhtml, contentWeb);
        } else {
            getData("ltr", outhtml, contentWeb);
        }

        contentWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                logger("link ", url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                logger("link page ", url);
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestButtonClick();
            }
        });
        initBottomSheet();
//        showConsentPopUp(viewGroup);
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
// Pop up showConsentPopUp
    public void showConsentPopUp( View view) {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.dialog_dotcor_consent_pop_up, null);
            final PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.update();

            tv_yes = popupView.findViewById(R.id.tv_yes_c);
            TextView tv_no = popupView.findViewById(R.id.tv_no_c);
            WebView contentWeb = popupView.findViewById(R.id.tv_content);

            check1 = popupView.findViewById(R.id.check1);
            check2 = popupView.findViewById(R.id.check2);

            check1.setOnCheckedChangeListener((compoundButton, b) -> {
                if (compoundButton.isPressed()) {
                    if (b && check2.isChecked()) {
                        tv_yes.setEnabled(true);

                    } else {
                        tv_yes.setEnabled(false);
                    }
                }
            });

            check2.setOnCheckedChangeListener((compoundButton, b) -> {
                if (compoundButton.isPressed()) {
                    if (b) {
                        showConsentPopUpAgree();
                    } else {
                        tv_yes.setEnabled(false);
                    }
                    check2.setChecked(false);
                } else if (!b) {
                    tv_yes.setEnabled(false);
                }
            });

            contentWeb.getSettings().setJavaScriptEnabled(true);
            contentWeb.getSettings().setLoadWithOverviewMode(true);
            contentWeb.getSettings().setUseWideViewPort(true);
            contentWeb.getSettings().setAllowContentAccess(true);
            contentWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            contentWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            contentWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            contentWeb.getSettings().setSupportZoom(true);

            String outhtml = getString(R.string.telemedcinice_consent_content);

            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                getData("rtl", outhtml, contentWeb);
            } else {
                getData("ltr", outhtml, contentWeb);
            }

            contentWeb.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                logger("link ", url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
//                logger("link page ", url);
                }
            });

            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    //                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);
                    //                builder.setTitle(getString(R.string.telemedcinice_consent_form))
                    //                        .setMessage(getString(R.string.telemedcinice_consent_error))
                    //                        .setCancelable(false)
                    //                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    //                            @Override
                    //                            public void onClick(DialogInterface dialog, int which) {
                    //                                popupWindow.dismiss();
                    //                            }
                    //                        });
                    //
                    //                AlertDialog alertDialog = builder.create();
                    //
                    //                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    //                    @Override
                    //                    public void onShow(DialogInterface dlg) {
                    //
                    //                        try {
                    //                            Typeface typeface = ResourcesCompat.getFont(AppointmentActivity.this, R.font.font_app);
                    //                            ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);
                    //
                    //                            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    //                                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    //                            } else {
                    //                                alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    //                            }
                    //                        } catch (Exception e) {
                    //                            e.printStackTrace();
                    //                        }
                    //
                    //                    }
                    //                });
                    //                alertDialog.show();

                }
            });


            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    onRequestButtonClick();
                }
            });

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
            popupWindow.showAtLocation(main_toolbarEmer, Gravity.CENTER, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
// Pop up showConsentPopUp for agree
    public void showConsentPopUpAgree() {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_dotcor_consent_pop_up_agree, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();

        TextView tv_i_agree = popupView.findViewById(R.id.tv_i_agree);
        TextView tv_i_disagree = popupView.findViewById(R.id.tv_i_disagree);
        WebView contentWeb = popupView.findViewById(R.id.tv_content);
        ProgressBar progress_bar = popupView.findViewById(R.id.progress_bar);
        LinearLayout layButton = popupView.findViewById(R.id.layButton);

        contentWeb.getSettings().setJavaScriptEnabled(true);
        contentWeb.getSettings().setLoadWithOverviewMode(true);
        contentWeb.getSettings().setUseWideViewPort(true);
        contentWeb.getSettings().setAllowContentAccess(true);
        contentWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        contentWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        contentWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        contentWeb.getSettings().setSupportZoom(true);

        if (SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
            contentWeb.loadUrl(WebUrl.CONSENT_TELEMED_AR);
        else
            contentWeb.loadUrl(WebUrl.CONSENT_TELEMED);

        contentWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                contentWeb.setVisibility(View.VISIBLE);
                layButton.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }


        });
        tv_i_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        tv_i_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (check1 != null) {
                    if (check1.isChecked())
                        tv_yes.setEnabled(true);
                    check2.setChecked(true);
                }

            }
        });

        popupWindow.setOnDismissListener(() -> {

        });

        popupWindow.showAtLocation(contentWeb, Gravity.CENTER, 0, 0);

    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    void getData(String direction, String text, WebView tvContent) {
        String text_load = "<html  dir=" + direction + "><head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_res/font/sans_plain.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: 42px;\n" +
                "    text-align: justify;\n" +
                "    padding-right: 20px;\n" +
                "    padding-left: 20px;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>" + text + "</body></html>";

        tvContent.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    void onRequestButtonClick() {

        Calendar curr_date = Calendar.getInstance();
        String current_date = Common.getDate(curr_date.get(Calendar.YEAR) + "-" + (curr_date.get(Calendar.MONTH) + 1) + "-" + curr_date.get(Calendar.DAY_OF_MONTH));

        if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//            behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            TelrActivity.startActivityForResult(EmergencyCallActivity.this, "", "");
        }
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
// initiate BottomSheet
    private void initBottomSheet() {

        String SessionID = String.valueOf(SharedPreferencesUtils.getInstance(this).getValue(Constants.SESSION_ID, 0));
        View bottomSheet = findViewById(R.id.rl_confirm);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);

        findViewById(R.id.iv_back_bottom).setOnClickListener(vv -> {
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        findViewById(R.id.tv_yes).setOnClickListener(vv -> {

            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE)) {
                String lang;
                String gender = "";

                if (SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.male))) {
                    gender = "M";
                } else if (SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.female))) {
                    gender = "F";
                } else if (SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.unspecified))) {
                    gender = "U";
                }


                if (SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.LANG, "").equalsIgnoreCase(getString(R.string.arabic_lang))) {
                    lang = "ar";
                } else {
                    lang = "en";
                }

                // call Book appointment API and get back to Home
                presenter.callBookAppointmentGuest(
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.IQAMA_ID, ""),

                        SessionID,
                        Common.getCurrentTimeWithLastUpdate(), Common.getCurrentDatePayOnly(),

                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FIRST_NAME, ""),
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FIRST_NAME_AR, ""),

                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FATHER_NAME, ""),
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FATHER_NAME_AR, ""),

                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FAMILY_NAME, ""),
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.FAMILY_NAME_AR, ""),

                        gender,
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.PHONE, ""), "",//Phone two

                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.DOB, ""),
                        lang,
                        SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.GUEST.REASON_VISIT, ""));

                SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, SessionID);

            } else {
                if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() > 0         @chnaged byPm
                    // call API for reschedule
                    presenter.callRescheduleAppointment(SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.KEY_MRN, ""),
                            SessionID, Common.getCurrentTimeWithLastUpdate(), Common.getCurrentDatePayOnly(), "", SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

//                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, mAdapter.getSessionId());
                } else {
                    // call Book appointment API and get back to Home
                    String teleHealthFlag = "0";
                    if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false))
                        teleHealthFlag = "1";

                    presenter.callBookAppointment(SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(EmergencyCallActivity.this).getValue(Constants.KEY_MRN, ""),
                            SessionID, Common.getCurrentTimeWithLastUpdate(), Common.getCurrentDatePayOnly(), teleHealthFlag, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

//                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, mAdapter.getSessionId());
                }
            }
        });

        findViewById(R.id.tv_no).setOnClickListener(vv -> {
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

                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        finish();
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void emergencyArrowBackFun(View view) {
        onBackPressed();
    }

    @Override
    public void onGetDates(SessionDatesResponse response) {

    }

    @Override
    public void onGetTimeSlots(TimeSlotsResponse response) {

    }

    @Override
    public void onBookAppointmentSuccess(BookResponse response) {

    }

    @Override
    public void onRescheduleAppointment(BookResponse response) {

    }

    @Override
    public void onGetPrice(PriceResponse response) {

    }

    @Override
    public void onGetPhysicianList(PhysicianResponse response) {

    }

    @Override
    public void onGetTimeSlotsAlternate(TimeSlotsNextResponse response, int pos) {

    }

    @Override
    public void onGetServiceList(DrServiceResponse serviceResponse) {

    }

    @Override
    public void onGetTimeSlot(DrTimeSlot drTimeSlot) {

    }

    @Override
    public void onGetTimeSlotsAlternateNew(TimeSlotsNextResponse[] timeSlotsNextResponses) {

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

    @Override
    public void onNewBook(BookResponse response){

    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
}