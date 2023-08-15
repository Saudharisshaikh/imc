package sa.med.imc.myimc.Home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.UpcomingBookingAdapter;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Clinics.view.ClinicsFragment;
import sa.med.imc.myimc.Departments.view.DepartmentsActivity;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.model.FormIdNameModel;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.presenter.BookingImpl;
import sa.med.imc.myimc.Managebookings.presenter.BookingPresenter;
import sa.med.imc.myimc.Managebookings.presenter.CheckInImpl;
import sa.med.imc.myimc.Managebookings.presenter.CheckInPresenter;
import sa.med.imc.myimc.Managebookings.ui.ManageBookingMainActivity;
import sa.med.imc.myimc.Managebookings.util.OnCancelResponse;
import sa.med.imc.myimc.Managebookings.view.AppoinmentOnclick;
import sa.med.imc.myimc.Managebookings.view.BookingViews;
import sa.med.imc.myimc.Managebookings.view.BottomBarcodeDialog;
import sa.med.imc.myimc.Managebookings.view.BottomPaymentDialog;
import sa.med.imc.myimc.Managebookings.view.CheckInViews;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.MedicineDetail.activity.MedicineActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.FindDoctorActivity;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansFragment;
import sa.med.imc.myimc.Questionaires.view.QuestionareActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.ReportListActivity;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.EmergencyCallActivity;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.ViewModel.CreateRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;
import sa.med.imc.myimc.WelcomeActivity;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.AppointmentListner;
import sa.med.imc.myimc.globle.interfaces.PatientArrivalListner;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;
import sa.med.imc.myimc.healthcare.HealthCareFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.ARRIVED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.ATTENDED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.CANCELLED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.NO_SHOW;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.UPCOMING;
import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;
import static sa.med.imc.myimc.Network.ImcApplication.updateLanguage;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.SLIP;
import static sa.med.imc.myimc.Utils.Common.getFromDate;
import static sa.med.imc.myimc.Utils.Common.simpleDailog;
import static sa.med.imc.myimc.Utils.Common.toAMPM;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Users next appointment detail
 * Show all recommended options.
 * Search doctors
 * Check all notifications
 */
public class HomeFragment extends Fragment implements BookingViews, CheckInViews, BottomPaymentDialog.BottomDailogListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_SLIP_FILE = 6;
    private ResponseBody responseBody;

    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.relative_info)
    RelativeLayout relativeInfo;
    @BindView(R.id.card_income)
    CardView cardIncome;
    @BindView(R.id.iv_med)
    ImageView ivMed;
    @BindView(R.id.lay_medication)
    RelativeLayout layMedication;
    @BindView(R.id.lay_lab_reports)
    LinearLayout layLabReports;
    @BindView(R.id.lay_radiology)
    LinearLayout layRadiology;
    @BindView(R.id.lay_find_doctor)
    LinearLayout layFindDoctor;
    @BindView(R.id.lay_clinics)
    LinearLayout layClinics;
    //    @BindView(R.id.lay_virtual_family)
//    LinearLayout layVirtualFamily;
    @BindView(R.id.lay_way_finder)
    RelativeLayout layWayFinder;
    @BindView(R.id.iv_que)
    ImageView ivQue;
    @BindView(R.id.lay_health_question)
    RelativeLayout layHealthQuestion;
    @BindView(R.id.lay_mangeBookings)
    RelativeLayout layMangeBookings;
    @BindView(R.id.tv_month_day)
    TextView tvMonthDay;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_clinic_name)
    TextView tvClinic;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lay_doc_profile)
    RelativeLayout layDocProfile;
    @BindView(R.id.lay_health_byte)
    LinearLayout layHealthByte;
    @BindView(R.id.lay_departments)
    LinearLayout layDepartments;
    @BindView(R.id.lay_dependent)
    LinearLayout layDependent;
    @BindView(R.id.layAssessment)
    LinearLayout layAssessment;
    @BindView(R.id.tv_assessment_label)
    TextView tvAssessmentLabel;
    @BindView(R.id.upcoming_checkin_text)
    TextView upcomingCheckinText;
    @BindView(R.id.upcoming_videoCall_text)
    TextView upcomingVideoCallText;
    @BindView(R.id.upcoming_checkin_button)
    LinearLayout upcomingCheckinButton;
    @BindView(R.id.upcoming_videoCall_button)
    LinearLayout upcomingVideoCallButton;

    @BindView(R.id.lay_hhc)
    RelativeLayout lay_hhc;

    FragmentListener fragmentAdd;
    int page = 0, buffer_time = 0;
    BookingAppoinment booking;
    BookingResponseNew.Bookings bookingResponseNew;
    BookingPresenter presenter;
    @BindView(R.id.iv_book)
    ImageView ivBook;
    @BindView(R.id.lay_symtom_tracker)
    RelativeLayout laySymtomTracker;
    @BindView(R.id.chatBot)
    ImageView chatBot;
    @BindView(R.id.tv_assessment_value)
    TextView tvAssessmentValue;
    @BindView(R.id.tv_payment_label)
    TextView tvPaymentLabel;
    @BindView(R.id.tv_payment_value)
    TextView tvPaymentValue;
    /*    @BindView(R.id.tv_arrived_label)
        TextView tvArrivedLabel;
        @BindView(R.id.tv_arrival_value)
        TextView tvArrivalValue;
        @BindView(R.id.layArrive)
        LinearLayout layArrive;*/
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ic_error_red)
    ImageView ic_error_red;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.layBottomValues)
    LinearLayout layBottomValues;
    @BindView(R.id.layPayment)
    LinearLayout layPayment;

    @BindView(R.id.lay_virtual_family)
    RelativeLayout lay_virtual_family;


    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.iv_h_s)
    ImageView iv_h_s;
    @BindView(R.id.iv_h_rq)
    ImageView iv_h_rq;
    @BindView(R.id.lay_request_report)
    RelativeLayout lay_request_report;
    @BindView(R.id.Home_Wellness_eng)
    ImageView Home_Wellness_eng;
    @BindView(R.id.fitness_and_wellness)
    RelativeLayout fitness_and_wellness;
    @BindView(R.id.emergency_video_call)
    RelativeLayout emergency_video_call;


    BottomPaymentDialog bottomPaymentDialog;
    CheckInPresenter checkInPresenter;
    String latitude = "", longitude = "", arrival = "0";
    Spotlight spotlight;
    RelativeLayout.LayoutParams parms;
    LinearLayout.LayoutParams par;
    int count = 0;
    BottomBarcodeDialog mBottomBarcodeDialog;
    Fragment physiciansFragment, clinicsFragment;
    String clinic_physician = "";
    String fromDate = "", code="";


    public HomeFragment() {
        // Required empty public constructor
    }

    // Broadcast receiver to receive API call to refresh dashboard if time equal or more that buffer time passed
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            loadAppointList();
//            if (presenter != null) {
//                if (booking != null) {
//
//                    Calendar curr = Calendar.getInstance();
//                    String hour = String.valueOf(curr.get(Calendar.HOUR_OF_DAY));
//                    String minute = String.valueOf((curr.get(Calendar.MINUTE)));
//
//                    if (curr.get(Calendar.MINUTE) < 10) {
//                        minute = "0" + minute;
//                    }
//
//                    if (curr.get(Calendar.HOUR_OF_DAY) < 10) {
//                        hour = "0" + hour;
//                    }
//                    String current_time = hour + ":" + minute;
//                    String time2 = Common.getConvertDate15Buffer(booking.getApptDateString(), buffer_time);
//
//                    if (current_time.compareTo(time2) > 0 || current_time.equalsIgnoreCase(time2)) {
//                        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                "1", String.valueOf(page), Constants.BookingStatus.UPCOMING);
//                    }
//
//                    if (intent.hasExtra("cancel")) {
//                        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                "1", String.valueOf(page), Constants.BookingStatus.UPCOMING);
//
//                    }
//
//                } else {
//                    presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                            "1", String.valueOf(page), Constants.BookingStatus.UPCOMING);
//                }
//            }
        }
    };

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        code = SharedPreferencesUtils.getInstance(getContext()).getValue(HOSPITAL_CODE, "IMC");

    }

    private void loadAppointList() {
        RecyclerView appoinment_recycler_view = getView().findViewById(R.id.appoinment_recycler_view);
        appoinment_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        String toDate = "";


        try {
            toDate = SharedPreferencesUtils.getInstance(getContext()).getValue("toDate", getFromDate(2, 0));
            fromDate = SharedPreferencesUtils.getInstance(getContext()).getValue("fromDate", getFromDate(0, 0));

            if (fromDate.trim().isEmpty()) {
                toDate = getFromDate(2, 0);
                fromDate = getFromDate(0, 0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SharedPreferencesUtils.getInstance(getContext()).clearAppointmentDate();

        new MyhttpMethod(getContext()).getAppoinmentList(getActivity(),new UrlWithURLDecoder().getDashboard(),
                SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.HOSPITAL_CODE, "IMC"),
                fromDate, toDate, new AppointmentListner() {
                    @Override
                    public void onSuccess(List<BookingAppoinment> bookingAppoinmentList) {
                        List<BookingAppoinment> upcomingList = new ArrayList<>();
                        for (int i = 0; i < bookingAppoinmentList.size(); i++) {
                            BookingAppoinment bookingAppoinment = bookingAppoinmentList.get(i);
                            if (bookingAppoinment.getBookingStatus() == UPCOMING) {
                                String fullDate = bookingAppoinment.getApptDateString().trim();
                                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                                try {
                                    Date appointmentDate = formatter1.parse(fullDate);

                                    if (new Date().before(appointmentDate)) {
                                        Log.e("abcd", "before");
                                        try {
                                            if (!bookingAppoinment.getTelemedicineStatus().trim().equalsIgnoreCase("Completed")){
                                                upcomingList.add(bookingAppoinment);
                                            }
                                        } catch (Exception e) {
                                            upcomingList.add(bookingAppoinment);
                                        }

                                    } else {
                                        Log.e("abcd", "after");
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Log.e("abcd", e.toString());
                                }
                            }
                        }
                        Log.e("abcd", new Gson().toJson(upcomingList));
                        UpcomingBookingAdapter upcomingBookingAdapter = new UpcomingBookingAdapter(getActivity(), getActivity(), upcomingList, new AppoinmentOnclick() {
                            @Override
                            public void onPay(BookingAppoinment booking) {
                                payAppoinment(booking);
                            }

                            @Override
                            public void onCancel(BookingAppoinment booking) {
                                MyhttpMethod myhttpMethod = new MyhttpMethod(getContext());
                                myhttpMethod.cancelAppointment(getActivity(), booking, new OnCancelResponse() {
                                    @Override
                                    public void onSuccess() {
                                        upcomingList.clear();
//                        cancelledList.clear();
//                        completedList.clear();
//                        noshowList.clear();
//                        arrivedList.clear();
                                        loadAppointList();
                                    }
                                });
                            }

                            @Override
                            public void videoCall(int position, String DrName) {

                            }
                        }, new PatientArrivalListner() {
                            @Override
                            public void onSuccess(String response) {
                                Log.e("abcd", response);
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                                upcomingList.clear();
                                loadAppointList();


                            }

                            @Override
                            public void onFaild(String massage) {
                                dailog(massage);

//                        Toast.makeText(getContext(), massage, Toast.LENGTH_LONG).show();

                            }
                        });

                        appoinment_recycler_view.setAdapter(upcomingBookingAdapter);
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1, container, false);
        ButterKnife.bind(this, view);
//        if (BuildConfig.DEBUG) {
//            chatBot.setVisibility(View.VISIBLE);
//        }

        if (SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            Home_Wellness_eng.setImageDrawable(getResources().getDrawable(R.drawable.welness_arabic_img));

        }

        presenter = new BookingImpl(this, getActivity());
        latitude = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, "");
        longitude = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LONG, "");
//        Log.e("latitude", "========" + latitude);
//        Log.e("longitude", "========" + longitude);

        arrival = "0";
        checkInPresenter = new CheckInImpl(this, getActivity());

        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Constants.Filter.REFRESH_HOME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUserVisibleHint(true);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes")) {
            layDependent.setVisibility(View.VISIBLE);
        } else {
            layDependent.setVisibility(View.GONE);
        }


//        receiveArguments();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAppointList();

        String hospital_code = SharedPreferencesUtils.getInstance(getContext()).getValue(HOSPITAL_CODE, "IMC");

        if (hospital_code.equalsIgnoreCase("FC") || hospital_code.equalsIgnoreCase("KH")){

            lay_hhc.setVisibility(View.GONE);
            lay_virtual_family.setVisibility(View.GONE);

        }


    }

    private void receiveArguments() {
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            laySymtomTracker.setVisibility(View.GONE);
            lay_virtual_family.setVisibility(View.GONE);
            layWayFinder.setVisibility(View.GONE);
            imageView1.setImageResource(R.drawable.ic_cardiology_icon);
            iv_h_s.setImageResource(R.drawable.ic_records);
            textView1.setText(R.string.health_summary);
            textView2.setText(R.string.view_health_summary);
            textView3.setText(R.string.reports);
            textView4.setText(R.string.view_radiology_reports);
            textView5.setText(R.string.location_map);
            textView6.setText(R.string.view_way_finder);

            iv_h_rq.setImageResource(R.drawable.ic_location_bottom_round);
        }
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            Home_Wellness_eng.setImageDrawable(getResources().getDrawable(R.drawable.welness_arabic_img));

        }
        fitness_and_wellness.setVisibility(View.VISIBLE);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Call Api only when displayed to user
//            if (presenter != null) {
//                presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                        "1", String.valueOf(page), "2");
//
//            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @OnClick({R.id.lay_hhc, R.id.layAssessment, /*R.id.layPayment,*/R.id.fitness_and_wellness, R.id.iv_more, R.id.chatBot, R.id.lay_request_report, R.id.lay_symtom_tracker, R.id.lay_health_byte, R.id.lay_departments, R.id.lay_doc_profile, R.id.lay_mangeBookings, R.id.lay_medication, R.id.lay_lab_reports, R.id.lay_radiology, R.id.lay_find_doctor, R.id.lay_clinics, R.id.lay_virtual_family, R.id.lay_way_finder, R.id.lay_retail, R.id.lay_health_question, R.id.upcoming_videoCall_button, R.id.lay_health_summary, R.id.upcoming_checkin_text, R.id.emergency_video_call})
    public void onViewClicked(View view) {
        updateLanguage(getActivity());

        switch (view.getId()) {
            case R.id.layAssessment:
                if (booking.getPromisForm() != null) {
                    if (booking.getPromisForm().size() > 0)
                        QuestionareActivity.startActivity(getActivity(), booking);
                    else
                        layAssessment.setClickable(false);
                }
                break;

            /*case R.id.layPayment:
                if (booking.getPaymentStatus() == 0) {
                    String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

                    presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                            String.valueOf(booking.getSessionId()), selected_date);
                }
                break;*/

            case R.id.iv_more:
                showListPopUp(ivMore, requireContext(), booking);
                break;

            case R.id.lay_hhc:
                fragmentAdd.hideToolbar();
                SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_MRN_FOR_HHC,SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_MRN,""));
                fragmentAdd.openFragment(HealthCareFragment.newInstance());
                break;

            case R.id.chatBot:
                Common.showChatBot(getActivity());
                break;
            case R.id.lay_request_report:
            case R.id.lay_lab_reports:
                startActivity(new Intent(getContext(), ReportListActivity.class));
//                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
//
//                    if (fragmentAdd != null)
//                        fragmentAdd.openLMSRecordFragment("LMSRecordFragment", Constants.TYPE.RECORD_LAB, "");
////                ContactUsActivity.startActivity(getActivity(), "request");
//                } else {
//                    showLocation();
//                }
                break;

            case R.id.lay_symtom_tracker:
                WebViewActivity.startActivity(getActivity(), BuildConfig.SYMPTOM_CHECKER_LINK);
                break;

            case R.id.lay_health_byte:

                if (fragmentAdd != null) {
                    fragmentAdd.openWellness("WelnessFragment");
                }

//                String url_en = "https://www.imc.med.sa/en/wellness";
//                String url_ar = "https://www.imc.med.sa/ar/wellness";
//                Intent intent = new Intent(getContext(), Wellness_WebView.class);
//                intent.putExtra("WellnessEnglish", url_en);
//                intent.putExtra("WellnessArabic", url_ar);
//                startActivity(intent);
//                 WellnessFragment wellnessFragment = WellnessFragment.newInstance();
//                 startFragment(getResources().getString(R.string.chatbot), wellnessFragment);
//                ((MainActivity) getContext()).mainToolbar.setVisibility(View.GONE);
                break;
            case R.id.fitness_and_wellness:
//                if (!BuildConfig.DEBUG) {
//                    Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (fragmentAdd != null) {
                    fragmentAdd.openFitness("FitnessFragment");
                }

//                Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.coming_soon), Toast.LENGTH_LONG).show();

//                    FitnessAndWellnesFragment fitnessAndWellnesFragment=FitnessAndWellnesFragment.newInstance();
//                startFragment(getResources().getString(R.string.chatbot),fitnessAndWellnesFragment);
            case R.id.lay_health_question:
//                HealthTipsActivity.startActivity(getActivity());
                break;

            case R.id.lay_departments:
                Intent dd = new Intent(getActivity(), DepartmentsActivity.class);
                startActivity(dd);
//                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
//
//                } else {
//                    if (fragmentAdd != null)
//                        fragmentAdd.openHealthSummary("HealthSummaryFragment");
//                }
                break;

            case R.id.lay_doc_profile:
//                if (booking.getDoc_isActive() == null || booking.getDoc_isActive().equalsIgnoreCase("0"))
//                    Common.doctorNotAvailableDialog(requireActivity());
//                else {
//                    PhysicianResponse.Physician physician = new PhysicianResponse().new Physician();
//                    physician.setClinicCode(booking.getSpecialityCode());
//                    physician.setDocCode(String.valueOf(booking.getDocCode()));
//                    physician.setGivenName(booking.getCareProviderDescription());
//                    physician.setGivenNameAr(booking.getCareProviderDescription());
//
//                    physician.setFamilyName(booking.getSpecialityDescription());
//                    physician.setFamilyNameAr(booking.getSpecialityDescription());
//
//                    physician.setFathersName(booking.getFathersName());
//                    physician.setFathersNameAr(booking.getFatherNameAr());
//
//                    physician.setClinicNameEn(booking.getSpecialityDescription());
//                    physician.setClincNameAr(booking.getSpecialityDescription());
//                    physician.setHrId(booking.getPatId());
//                    physician.setTeleHealthEligibleFlag(booking.getTeleHealthLink());
//
//                    PhysicianResponse.Service service = new PhysicianResponse().new Service();
//                    if (booking.getService() != null) {
//                        if (booking.getService().getDeptCode() != null)
//                            service.setDeptCode(booking.getService().getDeptCode());
//
//                        if (booking.getService().getService_id() != null)
//                            service.setId(booking.getService().getService_id());
//                        service.setDesc(booking.getService().getDesc());
//                        service.setDescAr(booking.getService().getDescAr());
//                    }
//
//                    physician.setService(service);
//                    PhysicianDetailActivity.startActivity(getActivity(), String.valueOf(booking.getDocCode()), booking.getClinicCode(), physician);
//                }
                break;

            case R.id.lay_mangeBookings:
                Intent intent = new Intent(getContext(), ManageBookingMainActivity.class);
                someActivityResultLauncher.launch(intent);


//                ManageBookingsActivity.startActivityForResult(getActivity());
                break;

            case R.id.lay_medication:
//                if (fragmentAdd != null)
//                    fragmentAdd.openMedicineFragment("MedicinesFragment", "");
                startActivity(new Intent(getContext(), MedicineActivity.class));
                break;

            case R.id.lay_radiology:
                if (fragmentAdd != null)
                    fragmentAdd.openLMSRecordFragment("LMSRecordFragment", Constants.TYPE.RECORD_RADIO, "");

                break;

            case R.id.lay_find_doctor:
//                PhysiciansActivity.startActivity(getActivity());
//                if (fragmentAdd != null)
//                    fragmentAdd.openPhysicianFragment("PhysiciansFragment", "");
//                break;
                Intent i = new Intent(getContext(), FindDoctorActivity.class);
                startActivity(i);
//                if (fragmentAdd != null)
//
//                    fragmentAdd.openClinicFragment("ClinicFragmentC");
                break;

            case R.id.lay_clinics:
                if (fragmentAdd != null)
                    fragmentAdd.openClinicFragment("ClinicFragmentC");
                break;

            case R.id.lay_virtual_family:
//                VirtualTourActivity.startActivity(getActivity());
                WebViewActivity.startActivity(getActivity(), WebUrl.VIRTUAL_TOUR_LINK);
                break;

            case R.id.lay_way_finder:
//                WayFinderMapActivity.startActivity(getActivity());
                showLocation();
                break;

            case R.id.lay_retail:
                Common.makeToast(getActivity(), "Coming soon...");
//                AllCategoriesActivity.startActivity(getActivity());
                break;


            case R.id.lay_health_summary:
                if (fragmentAdd != null)
                    fragmentAdd.openHealthSummary("HealthSummaryFragment");
//                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
//                    if (fragmentAdd != null)
//                        fragmentAdd.openHealthSummary("HealthSummaryFragment");
//                } else {
//                    if (fragmentAdd != null)
//                        fragmentAdd.openLMSRecordFragment("LMSRecordFragment", Constants.TYPE.RECORD_LAB, "");
//                }
                break;
            case R.id.upcoming_checkin_text:

//                if (fragmentAdd != null){
//                    fragmentAdd.openTelrFragment("");
//                }
//                break;

                if (fragmentAdd != null)
                    if (booking.getPaymentStatus() == "0") {
                        String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());
                        presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                                String.valueOf(booking.getSlotBookingId()), selected_date,
                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                    }

                break;
            case R.id.upcoming_videoCall_button:
                String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                String DrName = null;
                if (booking.getCareProviderDescription() != null) {
//                    if (booking.getFamilyName() != null) {
//                        DrName =  "DR. " + booking.getGivenName() + " " + booking.getFamilyName();
//                    }
//                    else {
//                        DrName= "DR. " + booking.getGivenName();
//                    }
                    if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " - " + booking.getSpecialityDescription();
                    } else {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " - " + booking.getSpecialityDescription();
                    }
                }

                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN, booking.getDocCode());
                clickToStartVideoCall(booking.getId(), booking.getApptDateString());
                break;
//                if (booking.getPaymentStatus() == 0) {
//                    String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());
//
//                    presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                            String.valueOf(booking.getSessionId()), selected_date,
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                }
//                break;


            case R.id.emergency_video_call:
                boolean navigation3 = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_NAV_CLASS, false);
                if (!navigation3) {
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_EMERGENCY_CALL, true);
                    Intent in = new Intent(getActivity(), EmergencyCallActivity.class);
                    startActivity(in);
                    getActivity().finish();
                }
                break;
        }
    }

    private void startFragment(String title, Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.main_container_wrapper, fragment);
            fragmentTransaction.addToBackStack(backStateName).commit();
        }
    }

    @Override
    public void onGetBookings(BookingResponse response) {
//        if (response.getBookings() != null) {
//            buffer_time = response.getDashBoardBuffer();
//            if (fragmentAdd != null)
//                fragmentAdd.startTask(response.getDashBoardBuffer());
//
//            if (response.getBookings().size() > 0) {
//                if (response.getBookings().get(0).getBookingStatus() == 2) { //future booking
//
//                 /*   if (BuildConfig.DEBUG) {
//                        layBottomValues.setVisibility(View.VISIBLE);
//                        view_line.setVisibility(View.VISIBLE);
//                    }*/
//
//                    if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
//                        layBottomValues.setVisibility(View.VISIBLE);
//                        view_line.setVisibility(View.VISIBLE);
//                    }
//
//                    booking = response.getBookings().get(0);
//                    cardIncome.setVisibility(View.VISIBLE);
//                    layMangeBookings.setVisibility(View.VISIBLE);//GONE
//                    Booking booking = response.getBookings().get(0);
////                    setData(booking);
//
//                } else {
//                    cardIncome.setVisibility(View.GONE);
//                    layMangeBookings.setVisibility(View.VISIBLE);
//                }
//            } else {
//                booking = null;
//                cardIncome.setVisibility(View.GONE);
//                layMangeBookings.setVisibility(View.VISIBLE);
//            }
//        } else {
//            booking = null;
//            cardIncome.setVisibility(View.GONE);
//            layMangeBookings.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void onGetBookingsNew(BookingResponseNew response) {
        if (response.getBookings() != null) {
            buffer_time = response.getDashboardBuffer();
            if (fragmentAdd != null)
                fragmentAdd.startTask(response.getDashboardBuffer());

            if (response.getBookings().size() > 0) {
                for (int p = 0; p < response.getBookings().size(); p++) {
                    if (response.getBookings().get(p).getBookingStatus() == 2) { //future booking

                 /*   if (BuildConfig.DEBUG) {
                        layBottomValues.setVisibility(View.VISIBLE);
                        view_line.setVisibility(View.VISIBLE);
                    }*/

                        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
                            layBottomValues.setVisibility(View.VISIBLE);
                            view_line.setVisibility(View.VISIBLE);
                        }

                        bookingResponseNew = response.getBookings().get(p);
                        cardIncome.setVisibility(View.VISIBLE);
                        layMangeBookings.setVisibility(View.VISIBLE);//GONE
                        BookingResponseNew.Bookings booking = response.getBookings().get(p);
                        setDataNew(response);

                    } else {
                        cardIncome.setVisibility(View.GONE);
                        layMangeBookings.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                booking = null;
                cardIncome.setVisibility(View.GONE);
                layMangeBookings.setVisibility(View.VISIBLE);
            }
        } else {
            booking = null;
            cardIncome.setVisibility(View.GONE);
            layMangeBookings.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onReschedule(SimpleResponse response) {

    }

    @Override
    public void onCancel(SimpleResponse response) {
//        Common.makeToast(getActivity(), response.getMessage());
        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
//        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
//                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                "1", String.valueOf(page), Constants.BookingStatus.UPCOMING);

    }

    @Override
    public void onDownloadConfirmation(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(SLIP);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case SLIP:
                String slipFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_booking_confirmation_" + System.currentTimeMillis() + ".pdf";
                createFile(slipFile, CREATE_SLIP_FILE);
                break;
        }
    }

    private void createFile(String fileName, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, requestCode);
    }

    private void alterDocument(Uri uri) {
        try {
            int count = -1;
            ParcelFileDescriptor pfd = getActivity().getContentResolver().
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
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_SLIP_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                alterDocument(uri);
                openFile(uri);
            }
        }
    }

    @Override
    public void onGetPrice(PriceResponse response) {
//        if (bottomPaymentDialog == null)
//            bottomPaymentDialog = BottomPaymentDialog.newInstance();

        ((MainActivity) getContext()).openTelrFragment(booking, response, "telrfragment");


//        bottomPaymentDialog.setArguments(bundle);
//        bottomPaymentDialog.setArguments(bundle);


//        if (!bottomPaymentDialog.isAdded())
//            bottomPaymentDialog.show(getChildFragmentManager(), "DAILOG");
    }

    @Override
    public void showLoading() {
        Common.showDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        Common.hideDialog();

        try {
            if (msg == null) {
//            cardIncome.setVisibility(View.GONE);
//            layMangeBookings.setVisibility(View.VISIBLE);
//            Common.makeToast(getActivity(), getActivity().getString(R.string.time_out_messgae));
            } else {
//                if (msg.equalsIgnoreCase(getString(R.string.plesae_try_again)) || msg.trim().equalsIgnoreCase("timeout"))
//                    Log.e("rrrrr", "sds");
//                else Common.showAlert(getActivity(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoInternet() {
//        Common.noInternet(getActivity());
    }

    // Set data of next appointment in Views
    /*void setData(Booking booking) {
        Activity activity = getActivity();
        if (activity != null) {
            if (booking.getTeleBooking() == 1) {
                tv_title.setText(getString(R.string.next_appointment) + " - " + getString(R.string.tele_med));
            } else {
                tv_title.setText(getString(R.string.next_appointment) + " - " + getString(R.string.in_person));
            }

            tvDoctorName.setText(booking.getGivenName());
            String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                if (booking.getGivenNameAr() != null)
                    if (booking.getFamilyNameAr() != null)
                        tvDoctorName.setText(Html.fromHtml("د. " + booking.getGivenNameAr() + " " + booking.getFamilyNameAr()));
                    else
                        tvDoctorName.setText(Html.fromHtml("د. " + booking.getGivenNameAr()));

                if (booking.getClinicDescAr() != null)
                    tvClinic.setText(Html.fromHtml(booking.getClinicDescAr()));

            } else {
                if (booking.getGivenName() != null)
                    if (booking.getFamilyName() != null)
                        tvDoctorName.setText(Html.fromHtml("DR. " + booking.getGivenName() + " " + booking.getFamilyName()));
                    else
                        tvDoctorName.setText(Html.fromHtml("DR. " + booking.getGivenName()));

                if (booking.getClinicDescEn() != null)
                    tvClinic.setText(Html.fromHtml(booking.getClinicDescEn()));
            }

            if (booking.getApptDate() != null) {
                if (booking.getApptDate().length() > 0) {
                    tvMonth.setText(Common.getConvertToMonthHome(booking.getApptDateString()));
                    tvMonthDay.setText(Common.getConvertToDayHome(booking.getApptDateString()));
                    tvTime.setText(Common.getConvertToTimeHome(booking.getApptDateString()));
                    tvTimeAmPm.setText(Common.getConvertToAMHome(booking.getApptDateString()));
                }
            }
            if (booking.getDocImg() != null) {
                if (booking.getDocImg().length() != 0) {
                    String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + booking.getDocImg();// + ".xhtml?In=default";
                    Picasso.get().load(url).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img).fit().into(ivProfile);
                }
            }

//            if (BuildConfig.DEBUG)
//            {
//&& booking.isSelfCheckIn() == false
            if (booking.getPaymentStatus() == 1) {
                tvPaymentValue.setText(this.getString(R.string.paid));
                ic_error_red.setVisibility(View.GONE);
                tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                *//*if (booking.isSelfCheckIn() == false) {
                    tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (booking.getIsArrived() == 1) {
                    tvArrivalValue.setText(this.getString(R.string.yes));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*//*

            } else if (booking.getPaymentStatus() == 0) {
                if (booking.isSelfCheckIn() == false)//value.selfCheckIn == 0
                {
                    ic_error_red.setVisibility(View.GONE);
                    tvPaymentValue.setText(this.getString(R.string.pending));
                    tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    layPayment.setClickable(false);
                    *//*tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);*//*

                } else {
                    ic_error_red.setVisibility(View.VISIBLE);//here
                    tvPaymentValue.setText(this.getString(R.string.pending));
                    tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                    *//*tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);*//*
                }
            } else if (booking.getPaymentStatus() == 1) {
                ic_error_red.setVisibility(View.GONE);
                tvPaymentValue.setText(this.getString(R.string.paid));
                tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                layPayment.setClickable(false);
                layPayment.setClickable(false);
                *//*tvArrivalValue.setText(this.getString(R.string.yes));
                tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);*//*
            }

            showToolTip();
            // author rohit
            if (booking.getIsArrived() == 0) {
                upcomingCheckinText.setText(R.string.check_in);
                upcomingCheckinText.setTextColor(Color.WHITE);
//                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                upcomingCheckinButton.setBackgroundResource(R.drawable.bt_home_white);
                if (!booking.isSelfCheckIn()) {
                    // hide red image; change text color to gray
                    upcomingCheckinText.setTextColor(Color.GRAY);
                    upcomingCheckinText.setCompoundDrawables(null, null, null, null);
                    upcomingCheckinButton.setBackground(null);
                    upcomingCheckinText.setOnClickListener(v -> {

                    });
                }
            } else {
                upcomingCheckinText.setText(R.string.checked_in);
                upcomingCheckinText.setTextColor(Color.GREEN);
                upcomingCheckinButton.setBackground(null);
                upcomingCheckinText.setCompoundDrawables(null, null, null, null);
//                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                upcomingCheckinText.setOnClickListener(v -> {
                });
            }

            if (booking.getPromisForm() != null) {
                if (booking.getPromisForm().size() > 0) {
                    Log.d("Home Fragment", "setData: Inside if block");
                    tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                    tvAssessmentValue.setText(booking.getPromisForm().size() + "");
                    // if assessment is greater than 0, then display the start assessment menu item in the pop up menu
//                ic_error_red.setVisibility(View.VISIBLE);
                } else {
                    Log.d("Home Fragment", "setData: Inside else block");
                    tvAssessmentValue.setText("0");
                    tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            } else {
                Log.d("Home Fragment", "setData: Inside else block");
                tvAssessmentValue.setText("0");
                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

//                ic_error_red.setVisibility(View.GONE);
            }

            *//*
     * author: rohit
     *
     * Purpose of the below code:  display the error image(red color exclamation) over menu icon;
     * *//*
            ArrayList<FormIdNameModel> formIdNameModelList = new ArrayList<>();
//            formIdNameModelList = booking.getPromisForm();
            if (booking.getPromisForm() != null)
                formIdNameModelList.addAll(booking.getPromisForm());

            try {
                if (formIdNameModelList.size() > 0) {
                    if (booking.getPaymentStatus() == 1 && booking.getIsArrived() == 1) {
                        ic_error_red.setVisibility(View.GONE);
                    } else {
                        if (!booking.isSelfCheckIn()) {
                            ic_error_red.setVisibility(View.GONE);
                        } else {
                            ic_error_red.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (booking.getPaymentStatus() == 1 && booking.getIsArrived() == 1) {
                        ic_error_red.setVisibility(View.GONE);
                    } else {
                        if (!booking.isSelfCheckIn())
                            ic_error_red.setVisibility(View.GONE);
                        else
                            ic_error_red.setVisibility(View.VISIBLE);
                    }
                }
            } catch (NullPointerException nullPointerException) {
                Log.e("Home Fragment", "setData: Null pointer exception found " + nullPointerException.getStackTrace().toString());
                Toast.makeText(getContext(), getResources().getString(R.string.no_data_available), Toast.LENGTH_LONG).show();
            }
            if (booking.getTeleBooking() != 0) {
                ic_error_red.setVisibility(View.VISIBLE);
            }
            // end of the menu icon display code

        }
    }*/

    // Set data of next appointment in Views
    void setDataNew(BookingResponseNew booking) {
        Activity activity = getActivity();
        if (activity != null) {
            if (booking.getBookings().size() == 1) {
                tv_title.setText(getString(R.string.next_appointment) + " - " + getString(R.string.tele_med));
            } else {
                tv_title.setText(getString(R.string.next_appointment) + " - " + getString(R.string.in_person));
            }

            tvDoctorName.setText(booking.getBookings().get(0).getDocCode());
            String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

            tvDoctorName.setText(Html.fromHtml("DR. " + booking.getBookings().get(0).getDocCode()));

            if (booking.getBookings().get(0).getServiceDescription() != null)
                tvClinic.setText(Html.fromHtml(booking.getBookings().get(0).getServiceDescription()));

            if (booking.getBookings().get(0).getApptDate() != null) {
                if (booking.getBookings().get(0).getApptDate().length() > 0) {
//                    tvMonth.setText(Common.getConvertToMonthHome(booking.getBookings().get(0).getApptDate()));
//                    tvMonthDay.setText(Common.getConvertToDayHome(booking.getBookings().get(0).getApptDate()));
//                    tvTime.setText(Common.getConvertToTimeHome(booking.getBookings().get(0).getApptDate()));
//                    tvTimeAmPm.setText(Common.getConvertToAMHome(booking.getBookings().get(0).getApptDate()));
                }
            }
            /*if (booking.getDocImg() != null) {
                if (booking.getDocImg().length() != 0) {
                    String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + booking.getDocImg();// + ".xhtml?In=default";
                    Picasso.get().load(url).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img).fit().into(ivProfile);
                }
            }*/

            if (booking.getBookings().get(0).getPaymentStatus() != null) {
                tvPaymentValue.setText(this.getString(R.string.paid));
                ic_error_red.setVisibility(View.GONE);
                tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                /*if (booking.isSelfCheckIn() == false) {
                    tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (booking.getIsArrived() == 1) {
                    tvArrivalValue.setText(this.getString(R.string.yes));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }*/

            } else if (booking.getBookings().get(0).getPaymentStatus() == null) {
                if (booking.getBookings().get(0).getSelfCheckIn() == null)//value.selfCheckIn == 0
                {
                    ic_error_red.setVisibility(View.GONE);
                    tvPaymentValue.setText(this.getString(R.string.pending));
                    tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    layPayment.setClickable(false);
                    /*tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);*/

                } else {
                    ic_error_red.setVisibility(View.VISIBLE);//here
                    tvPaymentValue.setText(this.getString(R.string.pending));
                    tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                    /*tvArrivalValue.setText(this.getString(R.string.no));
                    tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);*/
                }
            } else if (booking.getBookings().get(0).getPaymentStatus() != null) {
                ic_error_red.setVisibility(View.GONE);
                tvPaymentValue.setText(this.getString(R.string.paid));
                tvPaymentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                layPayment.setClickable(false);
                layPayment.setClickable(false);
                /*tvArrivalValue.setText(this.getString(R.string.yes));
                tvArrivedLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);*/
            }

            showToolTip();
            // author rohit
            if (booking.getBookings().get(0).getIsArrived() == null) {
                upcomingCheckinText.setText(R.string.check_in);
                upcomingCheckinText.setTextColor(Color.WHITE);
//                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                upcomingCheckinButton.setBackgroundResource(R.drawable.bt_home_white);
                if (booking.getBookings().get(0).getSelfCheckIn() == null) {
                    // hide red image; change text color to gray
                    upcomingCheckinText.setTextColor(Color.GRAY);
                    upcomingCheckinText.setCompoundDrawables(null, null, null, null);
                    upcomingCheckinButton.setBackground(null);
                    upcomingCheckinText.setOnClickListener(v -> {

                    });
                }
            } else {
                upcomingCheckinText.setText(R.string.checked_in);
                upcomingCheckinText.setTextColor(Color.GREEN);
                upcomingCheckinButton.setBackground(null);
                upcomingCheckinText.setCompoundDrawables(null, null, null, null);
//                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                upcomingCheckinText.setOnClickListener(v -> {
                });
            }

            if (booking.getBookings().get(0).getPromisForm() != null) {
                if (booking.getBookings().size() > 0) {
                    Log.d("Home Fragment", "setData: Inside if block");
                    tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error_red, 0);
                    tvAssessmentValue.setText(booking.getBookings().size() + "");
                    // if assessment is greater than 0, then display the start assessment menu item in the pop up menu
//                ic_error_red.setVisibility(View.VISIBLE);
                } else {
                    Log.d("Home Fragment", "setData: Inside else block");
                    tvAssessmentValue.setText("0");
                    tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            } else {
                Log.d("Home Fragment", "setData: Inside else block");
                tvAssessmentValue.setText("0");
                tvAssessmentLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

//                ic_error_red.setVisibility(View.GONE);
            }

            /*
             * author: rohit
             *
             * Purpose of the below code:  display the error image(red color exclamation) over menu icon;
             * */
            ArrayList<FormIdNameModel> formIdNameModelList = new ArrayList<>();
//            formIdNameModelList = booking.getPromisForm();
            if (booking.getBookings().get(0).getPromisForm() != null)
//                formIdNameModelList.addAll(booking.getBookings());

                try {
                    if (formIdNameModelList.size() > 0) {
                        if (booking.getBookings().get(0).getPaymentStatus() != "null" && booking.getBookings().get(0).getIsArrived() != null) {
                            ic_error_red.setVisibility(View.GONE);
                        } else {
                            if (booking.getBookings().get(0).getSelfCheckIn() != null) {
                                ic_error_red.setVisibility(View.GONE);
                            } else {
                                ic_error_red.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (booking.getBookings().get(0).getPaymentStatus() != null && booking.getBookings().get(0).getIsArrived() != null) {
                            ic_error_red.setVisibility(View.GONE);
                        } else {
                            if (booking.getBookings().get(0).getSelfCheckIn() != null)
                                ic_error_red.setVisibility(View.GONE);
                            else
                                ic_error_red.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (NullPointerException nullPointerException) {
                    Log.e("Home Fragment", "setData: Null pointer exception found " + nullPointerException.getStackTrace().toString());
                    Toast.makeText(getContext(), getResources().getString(R.string.no_data_available), Toast.LENGTH_LONG).show();
                }
            if (booking.getBookings().get(0).getTeleBooking() != null) {
                ic_error_red.setVisibility(View.VISIBLE);
            }
            // end of the menu icon display code

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Cancel booking alert and call API on Yes click
    void cancelBookingConfirmation(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_cancel_booking));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.callCancelBooking(id, SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
            }
        });
        alert.show();
    }

    // show IMC on map
    void showLocation() {
        String uri;

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


    // Pop up list to select gender or language
    public void showListPopUp(ImageView view, Context context, BookingAppoinment booking) {

        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_custom_list_upcoming, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
//        TextView tv_confirm_arrival = popupView.findViewById(R.id.tv_confirm_arrival);
        TextView tv_goto_consult = popupView.findViewById(R.id.tv_goto_consult);
        TextView tv_check_in = popupView.findViewById(R.id.tv_check_in);
        TextView tv_start_assessment = popupView.findViewById(R.id.tv_start_assessment);
        TextView tv_reschedule = popupView.findViewById(R.id.tv_reschedule);
        TextView tv_cancel = popupView.findViewById(R.id.tv_cancel);
        TextView tv_download = popupView.findViewById(R.id.tv_download);
        TextView tv_bar_code = popupView.findViewById(R.id.tv_bar_code);

        tv_goto_consult.setVisibility(View.GONE);
        tv_check_in.setVisibility(View.GONE);

        if (booking.getPromisForm() != null) {
            if (booking.getPromisForm().size() > 0) {
                tv_start_assessment.setVisibility(View.VISIBLE);
            } else {
                tv_start_assessment.setVisibility(View.GONE);
            }
        }

        if (booking.getServiceType() == "1") {
            tv_goto_consult.setVisibility(View.VISIBLE);
        } else {
            tv_goto_consult.setVisibility(View.GONE);
        }

        /*if (booking.isSelfCheckIn()) {
            tv_check_in.setVisibility(View.VISIBLE);
        } else {
            tv_check_in.setVisibility(View.GONE);
        }*/

        /* below code is for checkin show-hide option */
        if (booking.getPaymentStatus() == "1" && booking.getIsArrived() == "1") {
            tv_check_in.setVisibility(View.GONE);
        }
        else {
            if (booking.isSelfCheckIn()) {
                tv_check_in.setVisibility(View.VISIBLE);
            } else {
                tv_check_in.setVisibility(View.GONE);
            }
        }
        if (booking.getServiceType() == "1") {
            tv_check_in.setVisibility(View.GONE);
        }

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            tv_check_in.setVisibility(View.GONE);
            tv_start_assessment.setVisibility(View.GONE);
        }

        tv_goto_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

//                if (booking.getTeleHealthLink() != null) {
//                    gotoConsultConfirmation(booking);
//
//                }
            }
        });

        tv_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

                presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        String.valueOf(booking.getSlotBookingId()), selected_date,
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        });

        tv_start_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                QuestionareActivity.startActivity(getActivity(), booking);
            }
        });

        tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                presenter.callPrintSlipApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), Integer.valueOf(booking.getId()), SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        });

        tv_reschedule.setOnClickListener(view1 -> {
            popupWindow.dismiss();

            if (booking.getService() != null)
                rescheduleBookingConfirmation(booking);
            else
                setNoServicesDialog(getActivity());
        });

        tv_cancel.setOnClickListener(view12 -> {
            popupWindow.dismiss();

            cancelBookingConfirmation(String.valueOf(booking.getId()));
        });

        tv_bar_code.setOnClickListener(view13 -> {
            popupWindow.dismiss();

            if (mBottomBarcodeDialog == null)
                mBottomBarcodeDialog = BottomBarcodeDialog.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(booking.getId()));

            mBottomBarcodeDialog.setArguments(bundle);

            if (!mBottomBarcodeDialog.isAdded())
                mBottomBarcodeDialog.show(getChildFragmentManager(), "DAILOG");

        });


        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()
    }

    // reschedule booking confirmation alert
    void rescheduleBookingConfirmation(BookingAppoinment booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_reschedule_booking));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppointmentActivity.start(getActivity(), booking);

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    // goto Consult booking confirmation alert
    void gotoConsultConfirmation(BookingAppoinment booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_go_consult));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                PIYUSH 24-03-22
                /*Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(booking.getTeleHealthLink()));
                startActivity(i);*/
                String DrName = null;
                String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                if (booking.getCareProviderDescription() != null) {
//                    if (booking.getFamilyName() != null) {
//                        DrName =  "DR. " + booking.getGivenName() + " " + booking.getFamilyName();
//                    }
//                    else {
//                        DrName= "DR. " + booking.getGivenName();
//                    }
                    if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " - " + booking.getSpecialityDescription();
                    } else {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " - " + booking.getSpecialityDescription();
                    }
                }

                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN, booking.getDocCode());
                clickToStartVideoCall(booking.getId(), booking.getApptDateString());
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }

    public void clickToStartVideoCall(String bookingId, String bookingDateTime) {
        try {
            String lang;
            if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            } else {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            }
            Common.showDialog(getActivity());
            CreateRoomViewModel viewModel = ViewModelProviders.of(getActivity()).get(CreateRoomViewModel.class);
            viewModel.init();
            CreateRoomRequestModel createRoomRequestModel = new CreateRoomRequestModel();
            createRoomRequestModel.setPageNumber(0);
            createRoomRequestModel.setPageSize(0);
            String mrnNumber = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, null);
            String physician = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_VIDEO_PHYSICIAN, null);
            createRoomRequestModel.setRoomName(mrnNumber + "_" + physician);
            createRoomRequestModel.setUserEmail("xyz@gmail.com");
            createRoomRequestModel.setBookingId(bookingId);
            createRoomRequestModel.setDoctorId(physician);
            createRoomRequestModel.setUserId(mrnNumber);
            createRoomRequestModel.setBookingSlotTime(bookingDateTime);
            createRoomRequestModel.setDeviceType("Android");
            String deviceToken = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_DEVICE_ID, "");
            createRoomRequestModel.setDeviceToken(deviceToken);
            String token = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, null);
            viewModel.CreateRoom(token, lang,SharedPreferencesUtils.getInstance(getContext()).getValue(HOSPITAL_CODE, "IMC"), createRoomRequestModel);
            viewModel.getVolumesResponseLiveData().observe(getActivity(), response -> {

                if (response != null) {
                    if (response.getStatus()) {
                        Common.hideDialog();
                        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Dexter.withContext(getActivity()).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        SharedPreferencesUtils.getInstance(getActivity()).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ACCESS_TOKEN, response.getData().getAccessToken());
                                        SharedPreferencesUtils.getInstance(getActivity()).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_BACK_CLASS, "2");
//                                        if(!response.getBookingCompletedStatus()) {
                                        if (response.getValidBufferRange()) {
//                                                Common.hideDialog();
                                            SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_EMERGENCY_CALL, false);
                                            Intent in = new Intent(getActivity(), VideoActivity.class);
                                            startActivity(in);
                                            getActivity().finish();
                                        } else {
//                                                Common.hideDialog();
                                            SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_EMERGENCY_CALL, false);
//                                                gotoCheckTiming("Message!", "Meetings can be joined up to "+response.getBufferRangeBefore()+" minutes before the start time.", 1);
                                            gotoCheckTiming(getResources().getString(R.string.informationMessage), response.getBufferRangeMessage(), 1);
                                        }
//                                        }
//                                        else
//                                        {
//                                            gotoCheckTiming(getResources().getString(R.string.alert), response.getBookingCompletedMessage(), 2);
//                                        }
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
                    } else {
                        Common.hideDialog();
                    }
                } else {
                    Common.hideDialog();
                    Toast.makeText(getActivity(), "Some thing went wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Common.hideDialog();
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    // goto Consult booking confirmation alert
    void gotoCheckTiming(String title, String message, int E) {
//        PIYUSH 30-03-2022
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (E == 1) {
                    dialog.dismiss();
                    Intent in = new Intent(getActivity(), VideoActivity.class);
                    startActivity(in);
                    getActivity().finish();
                } else if (E == 2) {
                    dialog.dismiss();
                }
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    // If doctor's service is null
    void setNoServicesDialog(Activity activity) {
        String phone = "";
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert))
                .setMessage(activity.getString(R.string.no_service_content, phone))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_booking_confirmation_" + System.currentTimeMillis() + ".pdf";
//            // download the file
//            InputStream input = new BufferedInputStream(responseBody.byteStream(), 8192);
//            String path = Environment.getExternalStorageDirectory().toString() + Constants.FOLDER_NAME;
//            File file = new File(path);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//            path = file.getPath() + "/" + filename;
//            // Output stream
//            OutputStream output = new FileOutputStream(path);
//
//            byte[] data = new byte[1024];
//
//            while ((count = input.read(data)) != -1) {
//                output.write(data, 0, count);
//            }
//
//            // flushing output
//            output.flush();
//
//            // closing streams
//            output.close();
//            input.close();
//            // Show download success and ask to open file
//            openFile(path);
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    // Show download success and ask to open file
    void openFile(Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        builder.setMessage(getResources().getString(R.string.download_confirmation));
        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Common.openFile(requireActivity(), uri);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.later), (dialog, which) -> dialog.cancel());

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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
    public void onPayOnly(String date) {
        arrival = "0";
        String time = Common.getConvertToCheckInTime(booking.getApptDateString());
        String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

        checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                String.valueOf(booking.getSlotBookingId()), time, selected_date,
                String.valueOf(booking.getId()), "0", "1", "0",
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    @Override
    public void onPayAndCheckIN() {
        arrival = "1";
        confirmArrival();
    }

    @Override
    public void onGetLocation(SimpleResponse response) {
        if (booking.getPaymentStatus() == "1") {//1
            checkInPresenter.callArrival(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getId()),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        } else {
            String time = Common.getConvertToCheckInTime(booking.getApptDateString());
            String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

            checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getSlotBookingId()), time, selected_date, String.valueOf(booking.getId()), "0", "1", "0"
                    , SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }

    @Override
    public void onGetPaymentSuccess(PaymentResponse response) {
        if (response.getPaymentUrl() != null) {
            WebViewActivity.startActivity(getActivity(), response.getPaymentUrl(), response.getPaymentRef(), "upcoming",
                    String.valueOf(booking.getId()), arrival);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(false);
            builder.setTitle("");
            builder.setMessage(response.getMessage());
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i1 = new Intent(getActivity(), MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    getActivity().finish();
                    dialog.dismiss();

//                    AppointmentActivity.start(getActivity(), booking);
                }
            });

            final AlertDialog alert = builder.create();

            alert.show();
        }
    }

    @Override
    public void onArrived(SimpleResponse response) {
    }

    //confirm arrival dialog
    void confirmArrival() {
        try {
            Dialog sucDialog = new Dialog(requireActivity()); // Context, this, etc.
            sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            sucDialog.setContentView(R.layout.dialog_confirm_arrival);

            TextView yes = sucDialog.findViewById(R.id.tv_yes);
            TextView tv_no = sucDialog.findViewById(R.id.tv_no);
            ImageView iv_back_bottom = sucDialog.findViewById(R.id.iv_back_bottom);

            sucDialog.setCancelable(false);
            yes.setOnClickListener(v -> {
                sucDialog.dismiss();
                if (isGPSEnabled(requireActivity()) && SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, "").length() > 0) {
                    checkInPresenter.callCheckLocation(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LONG, ""),
                            Common.getConvertToCheckIn(booking.getApptDateString()),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else {
                    Common.makeToast(getActivity(), getString(R.string.location_not_accessible));
                    if (fragmentAdd != null)
                        fragmentAdd.checkLocation();
                }
            });


            tv_no.setOnClickListener(v -> sucDialog.dismiss());
            iv_back_bottom.setOnClickListener(v -> sucDialog.cancel());
            sucDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Check GPS enabled
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //show spotlight tooltip for one time to patient after booking first appoitment
    void showToolTip() {

        SharedPreferences preferences = requireActivity().getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);

        if (!preferences.getBoolean("tooltip", false)) {
            final Handler handler = new Handler();
            new Thread(() -> handler.postDelayed(() -> {

                if (spotlight == null) {
                    int[] twoLocation = new int[2];
                    ivMore.getLocationInWindow(twoLocation);
                    PointF point = new PointF(twoLocation[0] + ivMore.getWidth() / 2f, twoLocation[1] + ivMore.getHeight() / 2f);
                    // make an target
                    SimpleTarget secondTarget = new SimpleTarget.Builder(requireActivity()).setPoint(point)
                            .setRadius(80f)
                            .setTitle(getString(R.string.new_))
                            .setDescription(getString(R.string.tooltip_message))
                            .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                                @Override
                                public void onStarted(SimpleTarget target) {
                                    Log.e("spot", "spotlight is started");

                                }

                                @Override
                                public void onEnded(SimpleTarget target) {
                                    Log.e("spot", "spotlight is ended");
                                }
                            })
                            .build();


                    spotlight = Spotlight.with(requireActivity())
                            .setOverlayColor(ContextCompat.getColor(requireActivity(), R.color.background_over))
                            .setDuration(1000L)
                            .setAnimation(new DecelerateInterpolator(2f))
                            .setTargets(secondTarget)
                            .setClosedOnTouchedOutside(true)
                            .setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
                                @Override
                                public void onStarted() {
                                }
                            })
                            .setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
                                @Override
                                public void onEnded() {
                                }
                            });
                    spotlight.start(); // start Spotlight
                }

            }, 1000)).start();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("tooltip", true);
            editor.apply();
        }
    }

    private void payAppoinment(BookingAppoinment booking) {
        MyhttpMethod myhttpMethod = new MyhttpMethod(getContext());

        myhttpMethod.getServicePrice(booking.getSlotBookingId(), true, new ServicePriceListner() {
            @Override
            public void onSuccess(ServicePrice servicePrice) {
                ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.confirm_booking, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView service_price = dialogView.findViewById(R.id.service_price);
                service_price.setText(servicePrice.getOrderPrice());

                TextView patient_pay = dialogView.findViewById(R.id.patient_pay);
                patient_pay.setText(servicePrice.getPatientShare());

                TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
                text_clinic.setText(booking.getSpecialityDescription());

                TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
                String fullName = booking.getCareProviderDescription();

                text_phisician.setText(fullName);

                TextView text_date = dialogView.findViewById(R.id.text_date);
                text_date.setText(fromDate);

                //2022-07-01 10:00:00
                String fullDate = booking.getApptDateString();
                String time = fullDate.substring(11, 16);

                String formatedTime = toAMPM(time);
                TextView text_time = dialogView.findViewById(R.id.text_time);
                text_time.setText(formatedTime);

                TextView text_consultent_type = dialogView.findViewById(R.id.text_consultent_type);
                if (booking.getServiceType().equalsIgnoreCase("Offline")) {
                    text_consultent_type.setText("In Person");

                } else {
                    text_consultent_type.setText("Telemedicine");

                }


//                TextView text_patient_pay = dialogView.findViewById(R.id.text_patient_pay);
//                text_patient_pay.setText(servicePrice.getPatientShare());

                TextView text_discount = dialogView.findViewById(R.id.text_discount);
                text_discount.setText(servicePrice.getPayorShare());

                TextView text_vat = dialogView.findViewById(R.id.text_vat);
                text_vat.setText(servicePrice.getPayorShare());

                TextView text_total_amount = dialogView.findViewById(R.id.text_total_amount);
                text_total_amount.setText(servicePrice.getOrderPrice());


                ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
                clear_dailog.setOnClickListener(view -> alertDialog.dismiss());


                LinearLayout not_pay_layout = dialogView.findViewById(R.id.not_pay_layout);
                LinearLayout pay_layout = dialogView.findViewById(R.id.pay_layout);

                AppCompatButton ok = dialogView.findViewById(R.id.ok);
                AppCompatButton paynow = dialogView.findViewById(R.id.paynow);
                AppCompatButton pay_later = dialogView.findViewById(R.id.pay_later);

                ok.setOnClickListener(view -> {
                    alertDialog.dismiss();
                });

                pay_later.setOnClickListener(view -> {
                    alertDialog.dismiss();
                });
                if (servicePrice.getIs_final_price()) {
                    not_pay_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                } else {
                    pay_layout.setVisibility(View.GONE);
                    not_pay_layout.setVisibility(View.VISIBLE);
                }

                double patientShare = Double.parseDouble(servicePrice.getPatientShare());
                if (patientShare < 1) {
                    simpleDailog(requireActivity(),"",getResources().getString(R.string.pay_zero));
                    return;

//                    pay_layout.setVisibility(View.GONE);
//                    not_pay_layout.setVisibility(View.VISIBLE);
                } else {
                    not_pay_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                }

                pay_later.setVisibility(View.GONE);
                paynow.setOnClickListener(view -> {
                    ValidateResponse user = SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_USER);

                    String serviceCode = booking.getServiceCode();
                    String slotBookingId = booking.getSlotBookingId();
                    String patientId = user.getPatientId();
                    String payerForname = user.getFirstName();
                    String payerSurname = user.getLastName();
                    String docCode = booking.getCareProviderCode();
                    String docName = booking.getCareProviderDescription();
                    String payerEmail = user.getEmail(getContext());

                    String year = fullDate.substring(0, 4);
                    String month = fullDate.substring(5, 7);
                    String dayOfMonth = fullDate.substring(8, 10);
                    String date = dayOfMonth + "-" + month + "-" + year + " " + time;

                    String appointmentDate = date;

                    String payerAddress = user.getAddress();
                    String amount = servicePrice.getPatientShare();
                    String serviceName = booking.getServiceDescription();
                    String payerTitle = user.getFirstName();
                    String mrn = user.getMrn();

                    RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration = new RequestBodyForPaymentUrlGeneration();
                    requestBodyForPaymentUrlGeneration.setServiceCode(serviceCode);
                    requestBodyForPaymentUrlGeneration.setSlotBookingId(slotBookingId);
                    requestBodyForPaymentUrlGeneration.setPatientId(patientId);
                    requestBodyForPaymentUrlGeneration.setPayerForname(payerForname);
                    requestBodyForPaymentUrlGeneration.setPayerSurname(payerSurname);
                    requestBodyForPaymentUrlGeneration.setDocCode(docCode);
                    requestBodyForPaymentUrlGeneration.setDocName(docName);
                    requestBodyForPaymentUrlGeneration.setPayerEmail(payerEmail);
                    requestBodyForPaymentUrlGeneration.setAppointmentDate(appointmentDate);
                    requestBodyForPaymentUrlGeneration.setPayerAddress(payerAddress);
                    requestBodyForPaymentUrlGeneration.setAmount(amount);
                    requestBodyForPaymentUrlGeneration.setServiceName(serviceName);
                    requestBodyForPaymentUrlGeneration.setPayerTitle(payerTitle);
                    requestBodyForPaymentUrlGeneration.setMrn(mrn);
                    myhttpMethod.generatePaymentUrl(getActivity(), requestBodyForPaymentUrlGeneration);
                });

                alertDialog.show();

            }
        });
    }


    private void dailog(String msg) {
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(msg);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        loadAppointList();
                    }
                }
            });
}
