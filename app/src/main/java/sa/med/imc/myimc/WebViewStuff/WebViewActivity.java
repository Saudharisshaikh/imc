package sa.med.imc.myimc.WebViewStuff;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.Login.LoginActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.splash.SplashActivity;
import sa.med.imc.myimc.Utils.Common;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.pd_load)
    ProgressBar pdLoad;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.lay_request_appointment)
    RelativeLayout layRequestAppointment;
    @BindView(R.id.lay_call_us)
    RelativeLayout layCallUs;
    @BindView(R.id.layyy)
    LinearLayout layyy;
    @BindView(R.id.tv_request)
    TextView tvRequest;

    @Override
    protected Context getActivityContext() {
        return null;
    }

    public static void startActivity(Activity activity, String link) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("link", link);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String link, String ref) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String link, String ref, String from, String id, String arrival) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        intent.putExtra("id", id);
        intent.putExtra("arrival", arrival);
        intent.putExtra("from", from);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("ref")) {
            toolbarTitle.setText(getString(R.string.payment));
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
//            ivBack.setVisibility(View.GONE);

        } else {
            if (getIntent().getStringExtra("link").contains("mayo")) {
                toolbarTitle.setText(getString(R.string.mayo_clinic));

            }
            else if (getIntent().getStringExtra("link").contains("matterport")) {
                toolbarTitle.setText(getString(R.string.virtual_tour_imc));
            } else if (getIntent().getStringExtra("link").contains("symptom-checker")) {
                toolbarTitle.setText(getString(R.string.symtom_tracker));
            } else if (getIntent().getStringExtra("link").contains("TeleHealth")) {
                toolbarTitle.setText(getString(R.string.telemedcinice_consent_form));
            }else if (getIntent().getStringExtra("link").contains("wellness")){
                toolbarTitle.setText(getString(R.string.wellness));
            }
        }

        loadWeb(getIntent().getStringExtra("link"));


        Typeface typeface = ResourcesCompat.getFont(this,R.font.font_app);
        toolbarTitle.setTypeface(typeface);
    }

    // load url in web view
    void loadWeb(String url) {
        webView.invalidate();
//        webView.clearCache(true);
//        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
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
                    if (url.contains("imc.app.link") && SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_MRN, "").length() > 0) {
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
                    if (url.contains("clinicsMapping") && SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_MRN, "").length() > 0) {
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

                if (url.contains("tel:"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+" + url.replace("tel:", ""))));
                else
                    view.loadUrl(url);

                if (getIntent().hasExtra("from")) {
                    if (url.contains("selfcheckin/payement-authorized")) {//|| url.contains("selfcheckin/payement-declined")
                        webView.setVisibility(View.GONE);
                        verifyPaymentCheckIn(SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getIntent().getStringExtra("ref"), "back", getIntent().getStringExtra("id"), getIntent().getStringExtra("arrival"),
                                SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
                    }
                } else {
                    if (url.contains("doctor/declined")) {
                        webView.setVisibility(View.GONE);
                        verifyPayment(SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getIntent().getStringExtra("ref"), url);
                    } else if (url.contains("doctor/authorized")) {
                        webView.setVisibility(View.GONE);
                        verifyPayment(SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
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

//                if (url.contains("/ar/")) {
//                    layyy.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                    tvRequest.setText("طلب موعد");
//
//                } else if (url.contains("/en/")) {
//                    tvRequest.setText("Request an Appointment");
//                    layyy.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//                }

//                if (url.contains("doctors") || url.contains("doctorsprofile") || url.contains("departments") || url.contains("department")) {
//                layyy.setVisibility(View.VISIBLE);
//                } else {
//                    layyy.setVisibility(View.GONE);
//                }
                if (url.contains("http://192.168.1.49:8080") || url.contains("https://patientportal.imc.med.sa")) {
                    webView.setVisibility(View.GONE);
                    pdLoad.setVisibility(View.GONE);
                } else {
                    webView.setVisibility(View.VISIBLE);
                    pdLoad.setVisibility(View.GONE);
                }
            }

        });
    }


    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("from")) {
            finish();
        } else {
            if (getIntent().hasExtra("ref")) {
                verifyPayment(SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        getIntent().getStringExtra("ref"), "back");
            } else {
                finish();
            }
        }

    }

    @OnClick({R.id.iv_back, R.id.lay_request_appointment, R.id.lay_call_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_request_appointment:
                ContactOptionsActivity.startActivity(this);
                break;

            case R.id.lay_call_us:
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + WebUrl.IMC_SUPPORT_NUMBER)));
                break;
        }
    }

    public void showAlert(Activity activity, String title, String msg, int logo) {
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
                    LocalBroadcastManager.getInstance(WebViewActivity.this).sendBroadcast(refresh);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            finish();
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
//                        if (url.contains("doctor/declined")) {
//                            showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
//                        } else if (url.contains("doctor/authorized")) {
//                            showAlert(WebViewActivity.this, getString(R.string.success), getString(R.string.payment_successful), R.drawable.ic_successful_icon_green);
//                        }

                        if (response1.getMethod() == null) {
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
                                            showAlert(WebViewActivity.this, getString(R.string.failed), response1.getOrder().getTransaction().getMessage(), R.drawable.ic_unsuccessful_icon);
                                        else
                                            showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                    else
                                        showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                else
                                    showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
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
                    Common.noInternet(WebViewActivity.this);
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

    // Alert dialog
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

                            if (SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                                Intent i = new Intent(WebViewActivity.this, GuestHomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent i1 = new Intent(WebViewActivity.this, MainActivity.class);
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
                    Typeface typeface = ResourcesCompat.getFont(WebViewActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(WebViewActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    void verifyPaymentCheckIn(String token, String refId, String url, String bookingId, String updateArrival,int hosp) {
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
                                showAlert(WebViewActivity.this, getString(R.string.success), response1.getMessage(), R.drawable.ic_successful_icon_green);
                            } else {
                                showAlert(WebViewActivity.this, getString(R.string.failed), response1.getMessage(), R.drawable.ic_unsuccessful_icon);
//                                makeToast(response1.getMessage());
                            }
                        } else {
                            if (url.equalsIgnoreCase("back")) {
                                finish();
                            } else {
                                if (response1.getOrder() != null)
                                    if (response1.getOrder().getTransaction() != null)
                                        if (response1.getOrder().getTransaction().getMessage() != null)
                                            showAlert(WebViewActivity.this, getString(R.string.failed), response1.getOrder().getTransaction().getMessage(), R.drawable.ic_unsuccessful_icon);
                                        else
                                            showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                    else
                                        showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                else
                                    showAlert(WebViewActivity.this, getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
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
                    Common.noInternet(WebViewActivity.this);
                } else {
                    message = "Unknown";
                    makeToast(message);

                }
            }
        });
    }

}
