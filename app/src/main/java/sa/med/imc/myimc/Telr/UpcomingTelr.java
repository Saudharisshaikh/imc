package sa.med.imc.myimc.Telr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.presenter.CheckInImpl;
import sa.med.imc.myimc.Managebookings.presenter.CheckInPresenter;
import sa.med.imc.myimc.Managebookings.view.CheckInViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.Utils.Common;

import static sa.med.imc.myimc.Home.HomeFragment.isGPSEnabled;

public class UpcomingTelr extends BaseActivity implements CheckInViews {


    TextView toolbarTitle;
//    @BindView(R.id.main_toolbar)
//    RelativeLayout mainToolbar;
    @BindView(R.id.main_container_wrapper)
    FrameLayout mainContainerWrapper;
    //    @BindView(R.id.tabs)
//    TabLayout tabs;
    List<Booking> upcomingList = new ArrayList<>();
    int selected_pos = -1;
    private String arrival = "0";

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
    WebView WebView;

    ImageView ivBackBottom;
    TextView tvClinicName,tvPhysicianSpeciality,tvAppointmentDate,tvAppointmentTime,tvAppointmentPrice;
    TextView tvPhysicianName,tvAppointmentType,tvPatientPay,tvPatientDiscount,tvPatientVat,tv_cancel,tvPayCash;
    LinearLayout layPrice,layB;
    View viewLineTop;
    RelativeLayout layItem;
    TextView btn_continue,edt5;






    TabLayout tabLayout;
    ImageView imageView, imageViewRecord, appoint_center_button, mayoimage, profileImage;
    TextView textView;
    RelativeLayout maintoolbar;

    String record_string = Constants.TYPE.RECORD_MAIN;
    String clinic_physician = "";
    String medicine = "";


    boolean onBackRecords = false, isGPSEnable = false, isNetworkEnable = false;

    FragmentManager fragmentManager;
    private int Id = 0;
    private String report_title = "";
    private String report_number = "";
    private String report_Data = "";
    ReportsPresenter presenter;
    CheckInPresenter checkInPresenter;
    Booking booking;




    @Override
    protected Context getActivityContext() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_telr);

//        tabLayout=findViewById(R.id.tabs);
        imageView = findViewById(R.id.homeimage);
        imageViewRecord = findViewById(R.id.record1);
        appoint_center_button = findViewById(R.id.appoint_center_button);
        mayoimage = findViewById(R.id.mayoimage);
        profileImage = findViewById(R.id.profileImage);
        WebView = findViewById(R.id.webView);

        ivBackBottom = findViewById(R.id.iv_back_bottom);
        tvClinicName = findViewById(R.id.tv_clinic_name);
        tvPhysicianSpeciality = findViewById(R.id.tv_physician_speciality);
        tvAppointmentDate = findViewById(R.id.tv_appointment_date);
        tvAppointmentTime = findViewById(R.id.tv_appointment_time);
        tvAppointmentPrice = findViewById(R.id.tv_appointment_price);
        tvPhysicianName = findViewById(R.id.tv_physician_name);
        tvAppointmentType = findViewById(R.id.tv_appointment_type);
        tvPatientPay = findViewById(R.id.tv_patient_pay);
        tvPatientDiscount = findViewById(R.id.tv_patient_discount);
        tvPatientVat = findViewById(R.id.tv_patient_vat);
        tv_cancel = findViewById(R.id.tv_cancel);
        tvPayCash = findViewById(R.id.tv_pay_cash);
        layPrice = findViewById(R.id.lay_price);
        layB = findViewById(R.id.lay_b);
        viewLineTop = findViewById(R.id.view_line_top);
        layItem = findViewById(R.id.lay_item);
        btn_continue = findViewById(R.id.btn_continue);
        edt5 = findViewById(R.id.edt5);
        checkInPresenter = new CheckInImpl( this, getParent());



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

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGPSEnabled(getApplicationContext()) && SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.KEY_LAT, "").length() > 0) {
                    checkInPresenter.callCheckLocation(SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.KEY_LAT, ""),
                            SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.KEY_LONG, ""),
                            Common.getConvertToCheckIn(booking.getApptDateString()),
                            SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.SELECTED_HOSPITAL, 0));
                }
            }
        });






    }





//    if (getArguments() != null) {
//
////            mainToolbar.setVisibility(View.VISIBLE);
//        booking = (Booking) getArguments().getSerializable("booking");
//        PriceResponse response = (PriceResponse) getArguments().getSerializable("price");
//
////            if (booking.isHidePayCheckin())
////                tvPayCash.setEnabled(false);
////            else
////            tvPayCash.setEnabled(true);
//
//
//        if (response.getItemPrice() != null) {
//            // if (response.getItemPrice() > 0) {
//            if (Double.compare(response.getItemPrice(), Double.valueOf(0.0)) > 0) {
//                tvPatientPay.setText(response.getPatientPay() + "");
//                tvPatientDiscount.setText(response.getPaientDiscount() + "");
//                tvPatientVat.setText(response.getPatientVAT() + "");
//                tvAppointmentPrice.setText(response.getItemPrice() + "");
//            }
//
//            else
//            {
//                tvPatientPay.setText("0.0" + "");
//                tvPatientDiscount.setText("0.0" + "");
//                tvPatientVat.setText("0.0" + "");
//                tvAppointmentPrice.setText("0.0" + "");
//            }
//        }
//        else
//        {
//            tvPatientPay.setText("0.0" + "");
//            tvPatientDiscount.setText("0.0" + "");
//            tvPatientVat.setText("0.0" + "");
//            tvAppointmentPrice.setText("0.0" + "");
//        }
//        if (SharedPreferencesUtils.getInstance(getParent()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
//
//            setDataSheet(booking.getGivenNameAr() + " " + booking.getFamilyNameAr(), booking.getClinicDescAr(), booking.getService().getDescAr(),
//                    Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));
//        else
//            setDataSheet(booking.getGivenName() + " " + booking.getFamilyName(), booking.getClinicDescEn(), booking.getService().getDesc(),
//                    Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));
//
//    }

    void setDataSheet(String phy_name, String clinic, String service, String date, String time) {
        if (phy_name != null)
            tvPhysicianName.setText(phy_name);
        if (clinic != null)
            tvClinicName.setText(clinic);
        if (service != null)
            tvPhysicianSpeciality.setText(service);
        if (date != null)
            tvAppointmentDate.setText(date);
        if (time != null)
            tvAppointmentTime.setText(Common.parseShortTime(time));

        layPrice.setVisibility(View.VISIBLE);
        tvAppointmentType.setText(getString(R.string.in_person));
    }



    @Override
    public void onGetLocation(SimpleResponse response) {
        if (booking.getPaymentStatus() == 1) {//1
            checkInPresenter.callArrival(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getId()),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
        } else {
            String time = Common.getConvertToCheckInTime(booking.getApptDateString());
            String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

            checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getSessionId()), time, selected_date, String.valueOf(booking.getId()), "0", "1", "0"
                    , SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }

    @Override
    public void onGetPaymentSuccess(PaymentResponse response) {
        if (response.getPaymentUrl() != null) {
            TelrActivity.startActivity(this, response.getPaymentUrl(), response.getPaymentRef(), "upcoming", String.valueOf(upcomingList.get(selected_pos).getId()), arrival);

//            if(fragmentAdd != null){
//                    fragmentAdd.openPaymentInfoFragment(response,"");
//                }
        } else {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("");
            builder.setMessage(response.getMessage());
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i1 = new Intent(getActivityContext(), MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    finish();
                    dialog.dismiss();

//                    AppointmentActivity.start(getActivity(), booking);
                }
            });
    }




}

    @Override
    public void onArrived(SimpleResponse response) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {
        if (this != null) {
            Common.makeToast(this, this.getString(R.string.time_out_messgae));
        }else {
            if (this != null)
                Common.makeToast(this, msg);
        }
    }

    @Override
    public void onNoInternet() {

    }
}