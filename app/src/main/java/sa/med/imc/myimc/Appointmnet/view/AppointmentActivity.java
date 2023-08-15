package sa.med.imc.myimc.Appointmnet.view;

import static sa.med.imc.myimc.Network.Constants.ARABIC;
import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Network.Constants.KEY_LANGUAGE;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked_code;
import static sa.med.imc.myimc.Utils.Common.showAlert;
import static sa.med.imc.myimc.Utils.Common.showConsentPopUpAgree;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import sa.med.imc.myimc.Adapter.TimeSlotsAdapter;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.model.ServiceResponse;
import sa.med.imc.myimc.Appointmnet.model.SessionDatesResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsNextResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.Appointmnet.presenter.AppointmentImpl;
import sa.med.imc.myimc.Appointmnet.presenter.AppointmentPresenter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.BottomConfirmDialogNextAvailable;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Telr.TelrFragment;
import sa.med.imc.myimc.Telr.UpcomingTelr;
import sa.med.imc.myimc.Telr.UpcomingTelrFragment;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;

/*
 * Dates set on calendar
 * Show time slots on date select
 * Disable all unavailable dates
 * Book appointment for Logged in users
 * Book Appointment for Guest users
 */
/*
 {"physicianId":"DR196","clinicId":"C-CAR","serviceId":"CARDIO","date":"12-Nov-20"}
 {"physicianId":"DR076","clinicId":"C-URO","serviceId":"228","date":"2020-11-17"}
 {"physicianId":"DR227","clinicId":"C-CAR","serviceId":"CARDIO","date":"2020-11-16"}
 */


public class AppointmentActivity extends BaseActivity implements AppointmentViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_notifications)
    ImageView ivNotifications;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rv_slots)
    RecyclerView rvSlots;
    @BindView(R.id.lay_btn_book)
    LinearLayout layBtnBook;

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.changeModeMonth)
    TextView changeModeMonth;

   @BindView(R.id.changeModeWeek)
    TextView changeModeWeek;


    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.tv_book_appointment_app)
    TextView tvBookAppointment;
    @BindView(R.id.tv_book_appointment)
    TextView tvBookAppointmentDoctor;
    Integer day_dis = null;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.no_slots)
    TextView noSlots;
    @BindView(R.id.iv_back_bottom)
    ImageView ivBackBottom;
    @BindView(R.id.tv_clinic_name)
    TextView tvClinicName;
    @BindView(R.id.tv_physician_name_b)
    TextView tvPhysicianNameB;
    @BindView(R.id.tv_physician_name)
    TextView tvPhysicianName;
    @BindView(R.id.tv_physician_speciality)
    TextView tvPhysicianSpeciality;
    @BindView(R.id.tv_appointment_date)
    TextView tvAppointmentDate;
    @BindView(R.id.tv_appointment_time)
    TextView tvAppointmentTime;
    @BindView(R.id.mai)
    RelativeLayout mai;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
    @BindView(R.id.lay_b)
    LinearLayout layB;
    @BindView(R.id.view_line_top)
    View viewLineTop;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.lay_item)
    RelativeLayout layItem;
    @BindView(R.id.fl_bottom_sheet_container)
    FrameLayout flBottomSheetContainer;
    @BindView(R.id.rl_confirm)
    RelativeLayout rlConfirm;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    String mPhoneNo = "", mUserType = "";
    @BindView(R.id.label1)
    TextView label1;
    @BindView(R.id.rd_in_persoh)
    RadioButton rdInPersoh;
    @BindView(R.id.rd_telemed)
    RadioButton rdTelemed;
    @BindView(R.id.radio)
    RadioGroup radio;
    @BindView(R.id.tv_appointment_type)
    TextView tvAppointmentType;
    @BindView(R.id.tv_patient_pay)
    TextView tvPatientPay;
    @BindView(R.id.tv_patient_discount)
    TextView tvPatientDiscount;
    @BindView(R.id.tv_patient_vat)
    TextView tvPatientVat;
    @BindView(R.id.tv_appointment_price)
    TextView tvAppointmentPrice;
    @BindView(R.id.lay_price)
    LinearLayout layPrice;
    @BindView(R.id.layLegend)
    LinearLayout layLegend;
    TextView tv_yes;
    CheckBox check2, check1;
    @BindView(R.id.iv_physician_pic)
    CircleImageView ivPhysicianPic;
    @BindView(R.id.tv_clinic_service)
    TextView tvClinicService;
    @BindView(R.id.tv_next_available_time)
    TextView tvNextAvailableTime;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_physician_address)
    TextView tvPhysicianAddress;
    @BindView(R.id.tv_physician_distance)
    TextView tvPhysicianDistance;
    @BindView(R.id.tv_view_profile)
    TextView tvViewProfile;
    @BindView(R.id.card_doctor)
    CardView cardDoctor;
    @BindView(R.id.iv_next)
    ImageView iv_next;
    @BindView(R.id.iv_prev)
    ImageView iv_prev;
    @BindView(R.id.layDoctor)
    RelativeLayout layDoctor;
    @BindView(R.id.tv_new_appointment_doctor_name)
    TextView tvNewAppointmentDoctorName;
    @BindView(R.id.spinner)
    Spinner spinner;
    ArrayList<String> listServices;
    ArrayList<String> listServicesAr;
    ArrayList<String> listServicesCode;
    ArrayList<String> listServicesType;
    String consultationtype;
    private BottomSheetBehavior behaviorBtmsheet;
    private PhysicianResponse.ProviderList physician;
    private PhysicianDetailResponse.PhysicianData physician_full;
    private BottomConfirmDialogNextAvailable mBottomConfirmDialogNextAvailable;
    private String mClinicId;
    private String mPhysicianId, mServiceId;
    private AppointmentPresenter presenter;
    private List<DrTimeSlot.SlotList> DrlstOfTimeSlot = new ArrayList<>();
    private List<TimeSlotsResponse.TimeSlot> lstOfTimeSlot = new ArrayList<>();
    private List<TimeSlotsResponse.TimeSlot> lstOfTimeSlotInperson = new ArrayList<>();
    private List<TimeSlotsResponse.TimeSlot> lstOfTimeSlotMedicine = new ArrayList<>();


    private PhysicianResponse physicianResponse;
    private List<PhysicianResponse.Physician> lstOfPhysician = new ArrayList<>();
    private List<Integer> listOfDays = new ArrayList<>();
    private HashMap<String, Boolean> calendarDates = new HashMap<String, Boolean>();
    private TimeSlotsAdapter mAdapter;
    private Unbinder unBinder;
    private BookingAppoinment mBookingInfo;
    private String selectedDate = "", mBookingId = "", docCode = "", fromDate = null, bookingSlotID = null, bookingslotTime = null;
    int selected_day = 0, bookingTime = 0, physician_pos = 0;
    Date formateddate;
    boolean checkformat;
    String specialityCode, specialityDescription, arSpecialityDescription, SelectedServiceCode;
    String lan = SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(KEY_LANGUAGE, ENGLISH);

    String apType;
    boolean isTele=false;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    //open from physician list book button
    public static void start(Activity activity, boolean isTele1, PhysicianResponse.ProviderList physician, String specialityCode, String specialityDescription1, String arSpecialityDescription1) {
        Intent intent = new Intent(activity, AppointmentActivity.class);

        Log.e("frac", specialityCode + " " + specialityDescription1);
        intent.putExtra("physician", physician);
        intent.putExtra("isTele", isTele1);
        intent.putExtra("specialityCode", specialityCode);
        intent.putExtra("specialityDescription", specialityDescription1);
        intent.putExtra("arSpecialityDescription", arSpecialityDescription1);
        activity.startActivityForResult(intent, TelrActivity.PAYMENT_CODE);
    }

    // Open from physician detail book button
    public static void start(Activity activity, PhysicianDetailResponse.PhysicianData physician) {
        Intent intent = new Intent(activity, AppointmentActivity.class);
        intent.putExtra("physician", physician);
        intent.putExtra("full", "yes");
        activity.startActivityForResult(intent, TelrActivity.PAYMENT_CODE);
    }

    // Open from Upcoming booking for reschedule
    public static void start(Activity activity, BookingAppoinment booking) {
        Intent intent = new Intent(activity, AppointmentActivity.class);
        intent.putExtra("booking", booking);
        activity.startActivityForResult(intent, TelrActivity.PAYMENT_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        AppointmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment);

        unBinder = ButterKnife.bind(this);
        isTele=getIntent().getBooleanExtra("isTele",false);

        initView();
        initBottomSheet();
        initTimeSlotRecyclerView();
//        rdInPersoh.setClickable(false);
//        rdTelemed.setClickable(false);
        // If from upcoming to reschedule
        if (getIntent().hasExtra("booking")) {
            mBookingInfo = (BookingAppoinment) getIntent().getSerializableExtra("booking");
            mBookingId = String.valueOf(mBookingInfo.getId());
            mClinicId = mBookingInfo.getSpecialityCode();
            mPhysicianId = mBookingInfo.getDocCode();
            tvBookAppointment.setText(getString(R.string.reschedule_appointment));

            tvNewAppointmentDoctorName.setVisibility(View.VISIBLE);
            String name = "";
            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                name = mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getArabicSpecialityDescription();
            else
                name = mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getSpecialityDescription();
            name = getString(R.string.dr) + " " + name;

            tvNewAppointmentDoctorName.setText(getString(R.string.new_appointment_doctor, name));

//            if (mBookingInfo.getService() != null)
//                if (mBookingInfo.getService().getService_id() != null)
//                    mServiceId = mBookingInfo.getService().getService_id();
//
//            if (mBookingInfo.getTeleBooking() == 1) {
//                rdTelemed.setChecked(true);
//                rdInPersoh.setEnabled(false);
//            } else {
//                rdInPersoh.setChecked(true);
//                rdTelemed.setEnabled(false);
//            }
        }
        // If from Physician profile to book
        else if (getIntent().hasExtra("full")) {
            physician_full = (PhysicianDetailResponse.PhysicianData) getIntent().getSerializableExtra("physician");

            mClinicId = physician_full.getClinicCode();
            mPhysicianId = physician_full.getDocCode();
            mServiceId = physician_full.getService().getId();

            tvNewAppointmentDoctorName.setVisibility(View.VISIBLE);
            String name = "";
            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                name = physician_full.getGivenNameAr() + " " + physician_full.getFamilyNameAr();
            else
                name = physician_full.getGivenName() + " " + physician_full.getFamilyName();
            name = getString(R.string.dr) + " " + name;

            tvNewAppointmentDoctorName.setText(getString(R.string.new_appointment_doctor, name));

//            if (physician_full.getTeleHealthEligibleFlag() == 1)
//                rdTelemed.setEnabled(true);
//            else
//                rdTelemed.setEnabled(false);
        }
        // If from Physician list to book
        else {
            physician = (PhysicianResponse.ProviderList) getIntent().getSerializableExtra("physician");
            mClinicId = getIntent().getStringExtra("specialityCode");
            specialityCode = getIntent().getStringExtra("specialityCode");
            specialityDescription = getIntent().getStringExtra("specialityDescription");
            arSpecialityDescription = getIntent().getStringExtra("arSpecialityDescription");
            mPhysicianId = physician.getProviderCode();
//            mServiceId = physician.getService().getId();

            tvNewAppointmentDoctorName.setVisibility(View.VISIBLE);


            String fullName = physician.getProviderDescription();

            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(KEY_LANGUAGE, ENGLISH).equalsIgnoreCase(ARABIC)) {
                fullName = physician.getArabicProviderDescription();
            }

            tvNewAppointmentDoctorName.setText(getString(R.string.new_appointment_doctor, fullName));

        }
        Log.e("frac", specialityCode + " " + specialityDescription);

        presenter = new AppointmentImpl(this, this);

        // Call fetch dates API
//        PIYUSH TRACKER
        /*if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE))
            presenter.callGetAllDates(SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.ACCESS_TOKEN, ""), mClinicId, mPhysicianId, mServiceId, mUserType, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
        else
            presenter.callGetAllDates(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), mClinicId, mPhysicianId, mServiceId, mUserType, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));*/

        presenter.fetchServiceList(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), physician.getProviderCode(), specialityCode);


        fromDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH) + 90));//not sure this is needed
        long endOfMonth = calendar.getTimeInMillis();
        //may need to reinitialize calendar, not sure

        changeModeMonth.setOnClickListener(view -> {
            changeModeWeek.setBackground(getDrawable(R.drawable.round_unselect));
            changeModeMonth.setBackground(getDrawable(R.drawable.round));

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (-10*scale + 0.5f);

            calendarView.setPadding(0,0,0,dpAsPixels);
            calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .setMinimumDate(CalendarDay.today())
                    .commit();
        });

        changeModeWeek.setOnClickListener(view -> {
            changeModeWeek.setBackground(getDrawable(R.drawable.round));
            changeModeMonth.setBackground(getDrawable(R.drawable.round_unselect));

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (18*scale + 0.5f);

            calendarView.setPadding(0,0,0,dpAsPixels);

            calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .setMinimumDate(CalendarDay.today())
                    .commit();
        });


        changeModeWeek.performClick();
        final LocalDate localDate = LocalDate.now();

        calendarView.setLeftArrow(R.drawable.calendar_back_arrow);
        calendarView.setRightArrow(R.drawable.calendar_next_arrow);
        calendarView.setSelectedDate(localDate);



//        calendarView.setMaxDate(endOfMonth);
//        calendarView.setMinDate(System.currentTimeMillis());


//        calendarView.setMinDate(System.currentTimeMillis());
//        calendarView.setMaxDate(System.currentTimeMillis()+(1000*60*60*24*200));


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                getSelectedDateSlote(date);
            }
        });
//        getSelectedDateSlote(calendarView.getSelectedDate());


//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView1, int year, int month, int dayOfMonth) {
//                int newMonth = month;
//                if (month == 12) {
//                    newMonth = 1;
//                } else {
//                    newMonth = newMonth + 1;
//                }
//
//                selectedDate = Common.getDate2(year + "-" + newMonth + "-" + dayOfMonth);
//
//                Log.e("abcd", selectedDate);
//                selected_day = Common.getDateDay(selectedDate);
//
//
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                Date newDate = null;
//                try {
//                    newDate = format.parse(selectedDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // 2022-07-05
//                fromDate = format.format(newDate);
//                presenter.fetchDrTimeSlot(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        fromDate, fromDate, physician.getProviderCode(), SelectedServiceCode, specialityCode);
//
//            }
//        });

        // set date selection from calendar
//        calendarViewMaterial.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
//                selectedDate = Common.getDate(calendarDay.getYear() + "-" + (calendarDay.getMonth()) + "-" + calendarDay.getDay());
//                selected_day = Common.getDateDay(selectedDate);
//                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
//                Date newDate = null;
//                try {
//                    newDate = format.parse(selectedDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                format = new SimpleDateFormat("yyyy-MM-dd"); // 2022-07-05
//                fromDate = format.format(newDate);
//                presenter.fetchDrTimeSlot(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""), fromDate, fromDate, physician.getProviderCode(), SelectedServiceCode, specialityCode);
//                //       Log.e("selectedDate :",selectedDate);
//                // If selected date day is not in active session
////                PIYUSH TRACK CARE
//                /*if (!listOfDays.contains(selected_day)) {
//                    lstOfTimeSlot.clear();
//                    lstOfTimeSlotInperson.clear();
//                    lstOfTimeSlotMedicine.clear();
//                    mAdapter.notifyDataSetChanged();
//                    noSlots.setText("No Clinic");
//
//                    label.setVisibility(View.GONE);
//                    noSlots.setVisibility(View.VISIBLE);
//                    rvSlots.setVisibility(View.GONE);
//                    layBtnBook.setVisibility(View.GONE);
//
//                    label1.setVisibility(View.GONE);
//                    radio.setVisibility(View.GONE);
//                }
//                // If selected date day is in active session then get all time slots
//                else {
//                    if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE))
//                        presenter.callGetAllTimeSlots(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.ACCESS_TOKEN, ""),
//                                mClinicId, mPhysicianId, mServiceId, selectedDate, mUserType, SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                    else
//                        presenter.callGetAllTimeSlots(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                mClinicId, mPhysicianId, mServiceId, selectedDate, mUserType, SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                }*/
//            }
//        });
//


        /*radio.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rd_in_persoh:
                    Log.e("TELE", "PERSON_CLICKED");
                    Log.e("LIST_IS", "LIST_IS" + lstOfTimeSlotInperson.size());
                    if (lstOfTimeSlotInperson.size() == 0) {
                        noSlots.setVisibility(View.VISIBLE);
                        rvSlots.setVisibility(View.GONE);
                        layBtnBook.setVisibility(View.GONE);
                        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                            presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                                    Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.GUEST_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        } else {
                            presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                                    Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        }

                    }
                     else {
                        // if (rdInPersoh.isChecked()) {
                        mAdapter.getFilter(1).filter("ff");
                        rvSlots.setVisibility(View.VISIBLE);
                        layBtnBook.setVisibility(View.VISIBLE);
                        label.setVisibility(View.VISIBLE);
                        layLegend.setVisibility(View.VISIBLE);
                        noSlots.setVisibility(View.GONE);
                        //  }
                    }

                    break;
                case R.id.rd_telemed:
                    Log.e("TELE", "TELE_CLICKED");
                    Log.e("LIST_IS", "LIST_IS" + lstOfTimeSlotMedicine.size());
                    // if (lstOfTimeSlotMedicine.isEmpty())
                    if (lstOfTimeSlotMedicine.size() == 0) {
                        //here
                        layBtnBook.setVisibility(View.GONE);
                        label.setVisibility(View.GONE);
                        rvSlots.setVisibility(View.GONE);
                        layLegend.setVisibility(View.GONE);
                        noSlots.setVisibility(View.VISIBLE);
                        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                            presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                                    Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.GUEST_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        }
                        else {
                            presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                    SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                                    Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                        }


                    } else {
                        if (rdTelemed.isChecked()) {
//                            mAdapter.getFilter(1).filter("ff");
//                            piyush
                            mAdapter.getFilter(2).filter("ff");
                            rvSlots.setVisibility(View.VISIBLE);
                            layBtnBook.setVisibility(View.VISIBLE);
                            noSlots.setVisibility(View.GONE);
//                            Piyush
                            label.setVisibility(View.VISIBLE);
                            layLegend.setVisibility(View.VISIBLE);
                        }
//                        else {
//                            layBtnBook.setVisibility(View.GONE);
//                            label.setVisibility(View.GONE);
//                        }
                    }

                    break;

            }
        });*/

//        calendarViewMaterial.setTitleMonths(R.array.months);
        // calendarViewMaterial.setWeekDayLabels();

        fcHospitalCheck();

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                presenter.fetchServiceList(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""), physician.getProviderCode(), specialityCode);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedServiceCode = listServicesCode.get(position);
//                consultationtype = listServicesType.get(position);
                apType = listServicesType.get(position);
                if (apType.equals("Offline")) {
//                    rdInPersoh.setChecked(true);
//                    rdTelemed.setChecked(false);
                    consultationtype = getString(R.string.in_person);
                } else if (apType.equals("Online")) {
//                    rdInPersoh.setChecked(false);
//                    rdTelemed.setChecked(true);
                    consultationtype = getString(R.string.tele_med);
                }

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateFrom = df.format(c);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date monthFirstDay = calendar.getTime();
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date monthLastDay = calendar.getTime();

//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateTo = df.format(monthLastDay);

                if (fromDate != null) {
                    presenter.fetchDrTimeSlot(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""), fromDate, fromDate, physician.getProviderCode(), SelectedServiceCode, specialityCode);
                } else {

                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    presenter.fetchDrTimeSlot(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""), timeStamp, timeStamp, physician.getProviderCode(), SelectedServiceCode, specialityCode);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getSelectedDateSlote(CalendarDay date) {
        int month=date.getMonth();

        String strMonth="";
        if (month<10){
            strMonth="0"+month;
        }else{
            strMonth= String.valueOf(month);
        }

        selectedDate = Common.getDate2(date.getYear() + "-" + strMonth + "-" + date.getDay());

        Log.e("abcd", selectedDate);
        selected_day = Common.getDateDay(selectedDate);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = format.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // 2022-07-05
        fromDate = format.format(newDate);
        presenter.fetchDrTimeSlot(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""),
                fromDate, fromDate, physician.getProviderCode(), SelectedServiceCode, specialityCode);


    }
    private void fcHospitalCheck() {
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 1) == 1) {
            rdTelemed.setVisibility(View.VISIBLE);
        } else {
            rdTelemed.setVisibility(View.VISIBLE);
//            Piyush Invisible
//            rdTelemed.setVisibility(View.INVISIBLE);
            ImageView iv_logo = findViewById(R.id.iv_logo);

            /*
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                iv_logo.setImageResource(R.drawable.icon_fc_logo_arabic_new_white);
            } else {
                iv_logo.setImageResource(R.drawable.icon_logo_fc_new_white);
            }*/

            SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);
            String hospitalLogo = sp.getString("hospitalLogo",null);
            Picasso.get().load(hospitalLogo)
                    .error(R.drawable.error_icon)
                    .placeholder(R.drawable.placeholder_icon).fit().into(iv_logo);
        }
    }

    private void initView() {
        mUserType = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "");
        if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE)) {
            rdTelemed.setVisibility(View.INVISIBLE);
        } else {
            rdTelemed.setVisibility(View.VISIBLE);
        }

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            mPhoneNo = preferences.getString("ph", "") + "+";
        } else
            mPhoneNo = "+" + preferences.getString("ph", "");

        if (isTele){
            rdTelemed.setChecked(true);
            rdInPersoh.setChecked(false);
        }else {
            rdTelemed.setChecked(false);
            rdInPersoh.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layBtnBook != null)
            layBtnBook.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (layBtnBook != null)
            layBtnBook.setEnabled(true);
    }

    @OnClick({R.id.iv_back, R.id.tv_next_available_time, R.id.lay_btn_book, R.id.iv_next, R.id.iv_prev, R.id.tv_view_profile, R.id.tv_book_appointment})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_next_available_time:
                PhysicianResponse.Physician physician = lstOfPhysician.get(physician_pos);

                if (physician.getService() != null) {
                    if (mBottomConfirmDialogNextAvailable == null)
                        mBottomConfirmDialogNextAvailable = BottomConfirmDialogNextAvailable.newInstance();
                    NextTimeResponse.Datum session = new NextTimeResponse().new Datum();
                    session.setSessionId(Integer.valueOf(physician.getSessionId()));
                    session.setSessionDateShort(physician.getDate());
                    session.setSessionTimeShort(physician.getTime());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", physician);
                    bundle.putSerializable("session", session);

                    mBottomConfirmDialogNextAvailable.setArguments(bundle);

                    if (!mBottomConfirmDialogNextAvailable.isAdded())
                        mBottomConfirmDialogNextAvailable.show(getSupportFragmentManager(), "DAILOG");
                }

                break;

            case R.id.iv_prev:
                physician_pos = physician_pos - 1;

                if (physician_pos < lstOfPhysician.size())
                    setData(physician_pos);

                break;

            case R.id.iv_next:
                physician_pos = physician_pos + 1;

                if (physician_pos < lstOfPhysician.size())
                    setData(physician_pos);

                break;

            case R.id.tv_view_profile:
                PhysicianDetailActivity.startActivity(this, lstOfPhysician.get(physician_pos).getDocCode(), lstOfPhysician.get(physician_pos).getClinicCode(), lstOfPhysician.get(physician_pos));
                break;

            case R.id.tv_book_appointment:
                Intent i1 = new Intent(AppointmentActivity.this, AppointmentActivity.class);
                lstOfPhysician.get(physician_pos).setDate(selectedDate);
                i1.putExtra("physician", lstOfPhysician.get(physician_pos));
                //  i1.putExtra("alternate_phy", selectedDate); // selectedDate 30-Nov-20
                startActivity(i1);
                finish();

                //   Log.e("selectedDate : ",selectedDate);
//                final LocalDate ses =LocalDate.parse(Common.getConvert(selectedDate));
//                Log.e("ses: ",ses.toString()); //2020-10-27
//                selectedDate = ses.toString();
//
//                Log.e("selectedDate : ",selectedDate); //31-Oct-20
//                Intent i1 = new Intent(AppointmentActivity.this, AppointmentActivity.class);
//                list.get(physician_pos).setDate(selectedDate);
//                i1.putExtra("physician", list.get(physician_pos));
//                i1.putExtra("alternate_phy",selectedDate);
//                startActivity(i1);
//                finish();
                //  AppointmentActivity.start(AppointmentActivity.this, list.get(physician_pos));
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_book:
                if (mAdapter.getSessionId() == null) {
                    makeToast(getString(R.string.please_select_time_slot));
                } else {
                    /*if (getIntent().hasExtra("booking")) { // getIntent().hasExtra("booking") mBookingId.length() > 0         @changed byPm
                        onRequestButtonClick();
                    } else {
                        if (rdTelemed.isChecked()) {
                            showConsentPopUp();
                        } else {
                            onRequestButtonClick();
                        }
                    }*/
                    if (consultationtype.equalsIgnoreCase(getString(R.string.tele_med))){
                        showConsentPopUpAgree(AppointmentActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showConfirmDailog();
                            }
                        });
                    }else {
                        showConfirmDailog();
                    }
                }
                break;
        }
    }

    private void showConfirmDailog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(AppointmentActivity.this).inflate(R.layout.confirm_appointment, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
        String clinicName = specialityDescription;
        text_clinic.setText(clinicName);

        TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
        String fullName = physician.getProviderDescription();

        text_phisician.setText(fullName);

        TextView text_date = dialogView.findViewById(R.id.text_date);
        text_date.setText(fromDate);

        TextView text_time = dialogView.findViewById(R.id.text_time);
        text_time.setText(bookingslotTime);

        TextView text_consultent_type = dialogView.findViewById(R.id.text_consultent_type);
        text_consultent_type.setText(consultationtype);

        ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
        clear_dailog.setOnClickListener(view -> alertDialog.dismiss());

        AppCompatButton btn_continue = dialogView.findViewById(R.id.btn_continue);
        AppCompatButton btn_no = dialogView.findViewById(R.id.btn_no);

        if (SharedPreferencesUtils.getInstance().getValue(Constants.KEY_LANGUAGE, ENGLISH).equalsIgnoreCase(ARABIC)) {
            text_phisician.setText(physician.getArabicProviderDescription());
            try {
                if (!arSpecialityDescription.isEmpty())
                    text_clinic.setText(arSpecialityDescription);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        btn_no.setOnClickListener(view -> alertDialog.dismiss());
        btn_continue.setOnClickListener(view -> {
            presenter.callBookingApt(SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance().getValue(Constants.KEY_PATIENT_ID, ""),
                    mAdapter.getSessionId()
                    , physician.getProviderCode(),
                    SelectedServiceCode, specialityCode,
                    SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.HOSPITAL_CODE, "IMC"),
                    mAdapter.getSelectedDat(),
                    mAdapter.getSelectedTime2(),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0),
                    physician.getProviderDescription(),physician.getArabicProviderDescription(),apType
            );
            alertDialog.dismiss();
        });

        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    void onRequestButtonClick() {
        layBtnBook.setEnabled(false);

        Calendar curr_date = Calendar.getInstance();
        String current_date = Common.getDate(curr_date.get(Calendar.YEAR) + "-" + (curr_date.get(Calendar.MONTH) + 1) + "-" + curr_date.get(Calendar.DAY_OF_MONTH));


        if (selectedDate.equalsIgnoreCase(current_date)) {
            Calendar curr = Calendar.getInstance();
            curr.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
            String current_time = Common.getCurrentTime(curr.get(Calendar.HOUR_OF_DAY) + ":" + (curr.get(Calendar.MINUTE)) + ":" + curr.get(Calendar.SECOND), bookingTime);
            String slot = Common.getCurrentTime24(mAdapter.getSelectedTime());

            if (current_time.compareTo(slot) > 0 || current_time.equalsIgnoreCase(slot)) {
                bookingStatusDialog(getString(R.string.alert), getString(R.string.time_passed_away), 0);
            } else {
                if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() > 0         @chnaged byPm
                    // Set Data when intent from upcoming booking.
//                    if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
//                        setDataSheet(mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getSpecialityDescription(), mBookingInfo.getClinicDescAr(), mBookingInfo.getService().getDescAr(), selectedDate, mAdapter.getSelectedTime());
//                    else
//                        setDataSheet(mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getSpecialityDescription(), mBookingInfo.getClinicDescEn(), mBookingInfo.getService().getDesc(), selectedDate, mAdapter.getSelectedTime());
//
//                    if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                        behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    }

                } else {
                    if (rdTelemed.isChecked()) {
                        presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, "")
                                , "", String.valueOf(mAdapter.getSessionId()), selectedDate, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                    } else {

                        // check language and show data accordingly
                        if (getIntent().hasExtra("full")) {
                            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                                setDataSheet(physician_full.getGivenNameAr() + " " + physician_full.getFamilyNameAr(), physician_full.getClincNameAr(), physician_full.getService().getDescAr(), selectedDate, mAdapter.getSelectedTime());
                            else
                                setDataSheet(physician_full.getGivenName() + " " + physician_full.getFamilyName(), physician_full.getClinicNameEn(), physician_full.getService().getDesc(), selectedDate, mAdapter.getSelectedTime());
                        } else {
                            setDataSheet(physician.getProviderDescription(), specialityDescription, specialityDescription, selectedDate, mAdapter.getSelectedTime());
                        }

                        if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                }

            }
        } else {
            if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() > 0         @chnaged byPm
                // Set Data when intent from upcoming booking.
//                if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
//                    setDataSheet(mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getSpecialityDescription(), mBookingInfo.getClinicDescAr(), mBookingInfo.getService().getDescAr(), selectedDate, mAdapter.getSelectedTime());
//                else
//                    setDataSheet(mBookingInfo.getCareProviderDescription() + " " + mBookingInfo.getSpecialityDescription(), mBookingInfo.getClinicDescEn(), mBookingInfo.getService().getDesc(), selectedDate, mAdapter.getSelectedTime());


                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            } else {
                if (rdTelemed.isChecked()) {
                    presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, "")
                            , "", String.valueOf(mAdapter.getSessionId()), selectedDate, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else {

                    // check language and show data accordingly
                    if (getIntent().hasExtra("full")) {
                        if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                            setDataSheet(physician_full.getGivenNameAr() + " " + physician_full.getFamilyNameAr(), physician_full.getClincNameAr(), physician_full.getService().getDescAr(), selectedDate, mAdapter.getSelectedTime());
                        else
                            setDataSheet(physician_full.getGivenName() + " " + physician_full.getFamilyName(), physician_full.getClinicNameEn(), physician_full.getService().getDesc(), selectedDate, mAdapter.getSelectedTime());


                    } else {
                        setDataSheet(physician.getProviderDescription(), specialityDescription, specialityDescription, selectedDate, mAdapter.getSelectedTime());
                    }

                    if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            }
        }
    }

    // Set data in confirm booking sheet
    void setDataSheet(String phy_name, String clinic, String service, String date, String time) {
        if (phy_name != null)
            tvPhysicianNameB.setText(phy_name);
        if (clinic != null)
            tvClinicName.setText(clinic);
        if (service != null)
            tvPhysicianSpeciality.setText(service);
        if (date != null)
            tvAppointmentDate.setText(Common.parseDate(date));
        if (time != null)
            tvAppointmentTime.setText(Common.parseShortTime(time));

        if (rdInPersoh.isChecked()) {
            layPrice.setVisibility(View.GONE);
            tvAppointmentType.setText(getString(R.string.in_person));
        } else if (rdTelemed.isChecked()) {
            layPrice.setVisibility(View.VISIBLE);
            tvAppointmentType.setText(getString(R.string.tele_med));
        }
    }

    // Initialize Time Slot list view and add adapter to display data,
    void initTimeSlotRecyclerView() {
        mAdapter = new TimeSlotsAdapter(this, DrlstOfTimeSlot, selectedDate,false);
        rvSlots.setHasFixedSize(true);
        rvSlots.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rvSlots.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TimeSlotsAdapter.OnItemClickListener() {
            @Override
            public void onSlotClick(String slotID, String slotTime) {
//                Common.makeToast(AppointmentActivity.this, "Piyush");
                bookingSlotID = slotID;
                bookingslotTime = slotTime;
            }

            @Override
            public void onUnSelect(String SlotID, String slotTime) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
//
//            if (mAdapter.getSessionId() != null) {
//                alertBack(getString(R.string.alert), getString(R.string.discard_booking));
//            } else {
//                finish();
//            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onGetDates(SessionDatesResponse response) {
        /*listOfDays = new ArrayList<>();

        if (response.getSessionDates() != null)
            if (response.getSessionDates().size() > 0) {
                //  bookingTime = response.getBookingTime();
                String maxDate = null;
                for (SessionDatesResponse.SessionDate l : response.getSessionDates()) {
                    day_dis = Integer.valueOf(l.getDay());
                    listOfDays.add(day_dis);
                    //if maxDate is smaller than the new date
                    if (maxDate == null || maxDate.compareTo(l.getEndDate()) < 0) {
                        maxDate = l.getEndDate();
                        day_dis = Integer.valueOf(l.getDay());
                    }
                }

                // set up calendar
                if (maxDate != null)
                    setUpCalendar(maxDate);

                mainContent.setVisibility(View.VISIBLE);

                // check for reschedule
                if (getIntent().hasExtra("booking")) {   // getIntent().hasExtra("booking") mBookingId.length() > 0         @changed byPm
                    String date = mBookingInfo.getApptDate();
                    Log.e("apptDate: ", date); // 2020-10-27T10:00:00.000+0000
                    final LocalDate ses = LocalDate.of(Common.getConvertToYearInt(date), Common.getConvertToMonthHomeInt(date), Common.getConvertToDayInt(date));
                    Log.e("apptDate: ", ses.toString()); // 2020-10-27

                    calendarViewMaterial.setCurrentDate(ses);
                    calendarViewMaterial.setSelectedDate(ses);

                    selectedDate = Common.getDate(Common.getConvertToYearInt(date) + "-" + Common.getConvertToMonthHomeInt(date) + "-" + Common.getConvertToDayInt(date));
                    selected_day = Common.getDateDay(selectedDate);

                    Log.e("SELECTED_DATE", "SELECTED_DATE" + selectedDate);
                }
                else {

                    Calendar curr = Calendar.getInstance();
                    selectedDate = Common.getDate(curr.get(Calendar.YEAR) + "-" + (curr.get(Calendar.MONTH) + 1) + "-" + curr.get(Calendar.DAY_OF_MONTH));
                    // if came from alternate doctor card added bottom of calandar
                    if (physician != null)
                        if (physician.getDate() != null) {
                            String date = physician.getDate(); // 27-Nov-20
                            final LocalDate ses = LocalDate.parse(Common.getConvert(date));
                            calendarViewMaterial.setCurrentDate(ses);
                            calendarViewMaterial.setSelectedDate(ses);
                            selectedDate = Common.getConvert(date);
                            selected_day = Common.getDateDayI(selectedDate);
                        } else {
                            selected_day = Common.getDateDay(selectedDate);
                        }
                    Log.e("SELECTED_DATE11", "SELECTED_DATE11" + selectedDate);

                    String input = selectedDate;

                    Log.e("SELECTED_DATE11", "SELECTED_DATE11" + selectedDate);
                    boolean checkformat;
                    // if (input.matches("([0-9]{4})/([0-9]{2})/([0-9]{2})")) // for yyyy/MM/dd format
                    if (input.matches("([0-9]{2})-([0-9]{3})-([0-9]{2})")) // for yyyy/MM/dd format
                        checkformat = true;
                    else
                        checkformat = false;
                    Log.e("SELECTED_DATE11", "SELECTED_DATE11" + checkformat);

                    if (checkformat == false) {
                        SimpleDateFormat input_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                        try {

                            formateddate = input_date.parse(selectedDate);  // parse input
                            Log.e("SELECTED_DATE11", "SELECTED_DATE11" + output.format(formateddate));
                            selectedDate = output.format(formateddate);
                            Log.e("SELECTED_DATE11", "SELECTED_DATE11" + selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    //System.out.println(dateTime);
                    Log.e("SELECTED_DATE1122", "SELECTED_DATE1122" + selectedDate);
                }

//                selected_day = Common.getDateDay(selectedDate);    //@PM
                // check current date day is in available days or not
                if (listOfDays.contains(selected_day)) {
                    if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE))
                        presenter.callGetAllTimeSlots(SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.ACCESS_TOKEN, ""),
                                mClinicId, mPhysicianId, mServiceId, selectedDate, mUserType, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                    else
                        presenter.callGetAllTimeSlots(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                mClinicId, mPhysicianId, mServiceId, selectedDate, mUserType, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                }
                else {
                    presenter.callGetAllTimeSlots(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            mClinicId, mPhysicianId, mServiceId, selectedDate, mUserType, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                }

            }
        else {
                onFail(getString(R.string.no_dates));
                finish();
            }*/

    }

    // set dates on calendar and disable unavailable dates
    void setUpCalendar(String maxd) {
        calendarDates = new HashMap<>();
//        calendarViewMaterial.addDecorator(new PrimeDayDisableDecorator());
        final LocalDate calendar = LocalDate.now();
//        calendarViewMaterial.setSelectedDate(calendar);
        // Add dates on calendars
        Calendar min_date_c = Calendar.getInstance();
        min_date_c.set(Calendar.DAY_OF_MONTH, 1);

        Calendar max_date_c = Calendar.getInstance();

        max_date_c.set(Common.getConvertToYearInt(maxd), Common.getConvertToMonthHomeInt(maxd) - 1, Common.getConvertToDayInt(maxd));
        int daysInMonth = max_date_c.getActualMaximum(Calendar.DAY_OF_MONTH);

//        int daysInMonth = max_date_c.getActualMaximum(Calendar.DAY_OF_MONTH);
        logger("last month  ", "  " + daysInMonth + "     last>" + Common.getConvertToDayInt(maxd));

        final LocalDate min = LocalDate.of(calendar.getYear(), calendar.getMonth(), 1);
        final LocalDate max = LocalDate.of(Common.getConvertToYearInt(maxd), Common.getConvertToMonthHomeInt(maxd), daysInMonth);//Common.getConvertToDayInt(maxd));

        Calendar max_date_c_end = Calendar.getInstance();
        max_date_c_end.set(Common.getConvertToYearInt(maxd), Common.getConvertToMonthHomeInt(maxd) - 1, daysInMonth);

        Calendar calendar1 = Calendar.getInstance();
        String curr = calendar1.get(Calendar.DAY_OF_MONTH) + "/" + (calendar1.get(Calendar.MONTH) + 1) + "/" + calendar1.get(Calendar.YEAR);

        // Add current date in hash map and disable current date
        calendarDates.put(curr, false);

        // Add dates in hash map to disable unavailable dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c_end); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            String date = loopdate.get(Calendar.DAY_OF_MONTH) + "/" + (loopdate.get(Calendar.MONTH) + 1) + "/" + loopdate.get(Calendar.YEAR);

            if (date.equalsIgnoreCase(curr)) {
                if (listOfDays.contains(dayOfWeek)) {
                    calendarDates.put(date, false);
                } else {
                    calendarDates.put(date, true);
                }
            } else {
                if (loopdate.before(calendar1)) {
                    calendarDates.put(date, true);
                } else {
                    if (listOfDays.contains(dayOfWeek)) {
                        if (!calendarDates.containsKey(date)) {
                            if (loopdate.after(max_date_c)) {
                                calendarDates.put(date, true);
                            } else
                                calendarDates.put(date, false);
                        }
                    } else {
                        calendarDates.put(date, true);
                    }
                }
            }
        }

//        calendarViewMaterial.state().edit()
//                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
//                .setMinimumDate(min)
//                .setShowWeekDays(true)
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .setMaximumDate(max)
//                .commit();

    }

    @Override
    public void onGetTimeSlots(TimeSlotsResponse response) {
        lstOfTimeSlot.clear();
        lstOfTimeSlotInperson.clear();
        lstOfTimeSlotMedicine.clear();
        noSlots.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        noSlots.setText(response.getMessage()); // getString(R.string.no_time_slot));
        noSlots.setMovementMethod(LinkMovementMethod.getInstance());

//        Integer sessionResponse = response.getTimeSlots().get(0).getSessionId();
//
//        Intent intent = new Intent();
//        intent.putExtra("SessionID", sessionResponse);
//        startActivity(intent);


        if (response.getTimeSlots() != null) {
            lstOfTimeSlot.addAll(response.getTimeSlots());
            //----------------------------->
            for (TimeSlotsResponse.TimeSlot item : lstOfTimeSlot) {
                if (item.getSessionType() == 0 || item.getSessionType() == 1) {
                    lstOfTimeSlotInperson.add(item);
                }
            }
            //----------------------------->
            for (TimeSlotsResponse.TimeSlot item : lstOfTimeSlot) {
//                if (item.getSessionType() == 1 || item.getSessionType() == 0) {
//                    PIYUSH CHANGE
                if (item.getSessionType() == 2 || item.getSessionType() == 0) {
                    lstOfTimeSlotMedicine.add(item);
                }
            }

            if (lstOfTimeSlot.size() == 0) {
                label.setVisibility(View.GONE);
//                label1.setVisibility(View.GONE);
                radio.setVisibility(View.VISIBLE);
                noSlots.setVisibility(View.VISIBLE); // VISIBLE
                rvSlots.setVisibility(View.GONE);
                layBtnBook.setVisibility(View.GONE);//here
                Log.e("VISISBLE1", "VISISBLE1");
                Toast.makeText(AppointmentActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                // call API
//                if (BuildConfig.DEBUG)
//                    layDoctor.setVisibility(View.VISIBLE);

            } else {
                label.setVisibility(View.VISIBLE);
                label1.setVisibility(View.VISIBLE);
                radio.setVisibility(View.VISIBLE);
                noSlots.setVisibility(View.GONE);
                rvSlots.setVisibility(View.VISIBLE);
//                layLegend.setVisibility(View.VISIBLE);  // @PM changed on 23oct
                layBtnBook.setVisibility(View.VISIBLE);

//                if (BuildConfig.DEBUG)
//                    layDoctor.setVisibility(View.INVISIBLE);
            }

            if (lstOfTimeSlotInperson.isEmpty()) {     // @PM added on 23oct
                layLegend.setVisibility(View.GONE);
            } else {
                layLegend.setVisibility(View.VISIBLE);
            }

        } else {
            label.setVisibility(View.GONE);
//            label1.setVisibility(View.GONE);
//            radio.setVisibility(View.GONE);
            noSlots.setVisibility(View.VISIBLE); //VISIBLE
            rvSlots.setVisibility(View.GONE);
            layLegend.setVisibility(View.GONE);
            layBtnBook.setVisibility(View.GONE);
            Log.e("VISISBLE2", "VISISBLE2");

          /*  //here
            if (lstOfTimeSlotMedicine.isEmpty()) {     // @PM added on 23oct
                //  layBtnBook.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
                layBtnBook.setVisibility(View.GONE);
            }
//till here*/


//            if (BuildConfig.DEBUG)
//                layDoctor.setVisibility(View.VISIBLE);

            // call API
        }
        if (BuildConfig.DEBUG) {
            if (lstOfTimeSlot.size() == 0) {
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE))
                    presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                            Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.GUEST_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                else
                    presenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), mClinicId, "", "", "",
                            Constants.RECORD_SET_HUNDRED, String.valueOf(0), Constants.USER_TYPE, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        }


        /*Collections.sort(lstOfTimeSlot, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
        if (!getIntent().hasExtra("booking")) { // if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() == 0    @chnaged byPm
            if (rdTelemed.isChecked())
                mAdapter.getFilter(2).filter("ff");
            else
                mAdapter.getFilter(1).filter("ff");
        } else if (getIntent().hasExtra("booking")) {
            if (mBookingInfo.getTeleBooking() == 1) {
                mAdapter.getFilter(2).filter("ff");
            } else {
                mAdapter.getFilter(1).filter("ff");
            }
        }*/

        mAdapter.setSelectedDate(selectedDate, bookingTime);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBookAppointmentSuccess(BookResponse response) {
        String statusMsg = "", title = getString(R.string.failed);
        if (response.getPaymentUrl() != null) {
            if (response.getPaymentRef() != null) {
                if (rdTelemed.isChecked()) {
                    TelrActivity.startActivityForResult(AppointmentActivity.this, response.getPaymentUrl(), response.getPaymentRef());
                } else {
                    TelrActivity.startActivity(AppointmentActivity.this, response.getPaymentUrl(), response.getPaymentRef());
                }

            } else {
                statusMsg = getString(R.string.booking_failed);//"bookings.failed";
                bookingStatusDialog(title, statusMsg, response.getData().getStatus());
            }
        } else {
            switch (response.getData().getStatus()) {
                case 99:
                    title = getString(R.string.success);
                    statusMsg = getString(R.string.success_booking);//"Booked successfully";
                    break;

                case -4:
                    statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                    break;

                case 0:
                    statusMsg = getString(R.string.booking_exist, mPhoneNo);//"Bookings exists";
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
                    statusMsg = getString(R.string.booking_failed); //"bookings.failed";
                    break;
            }
            bookingStatusDialog(title, statusMsg, response.getData().getStatus());

        }

    }

    @Override
    public void onRescheduleAppointment(BookResponse response) {

        String statusMsg = "", title = getString(R.string.failed);
        switch (response.getData().getStatus()) {
            case 99:
                title = getString(R.string.success);
                statusMsg = getString(R.string.success_booking_reschedue);//"Booked successfully";
                break;

            case -4:
            case -7:
                statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                break;

            case 0:
                statusMsg = getString(R.string.booking_exist, mPhoneNo);//"Bookings exists";
                break;

            case -5:
                statusMsg = getString(R.string.no_show_message);//"Bookings no_show";
                break;

            case -2:
                statusMsg = getString(R.string.booking_time_in_other_clinic);//"booking.exists_time_allowed";
                break;

            // Max 3 bookings per day
            case -6:
                statusMsg = getString(R.string.booking_max);//"booking.max_total_booking_reached";
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
            case -51:
                statusMsg = response.getMessage();//"booking failed for guest user ";
                break;

            case 111:
                statusMsg = getString(R.string.pending_pre_authorization);
                break;

            default:
                statusMsg = getString(R.string.booking_failed);//"bookings.failed";
                break;
        }
        // Show alert
        bookingStatusDialog(title, statusMsg, response.getData().getStatus());
    }

    @Override
    public void onGetPrice(PriceResponse response) {


        if (response.getItemPrice() != null) {
            // if (response.getItemPrice() > 0) {
            tvPatientPay.setText(response.getPatientPay() + "");
            tvPatientDiscount.setText(response.getPaientDiscount() + "");
            tvPatientVat.setText(response.getPatientVAT() + "");
            tvAppointmentPrice.setText(response.getItemPrice() + "");
            //}
        } else {
            tvPatientPay.setText("0.0" + "");
            tvPatientDiscount.setText("0.0" + "");
            tvPatientVat.setText("0.0" + "");
            tvAppointmentPrice.setText("0.0" + "");
        }
        // check language and show data accordingly
        if (getIntent().hasExtra("full")) {
            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                setDataSheet(physician_full.getGivenNameAr() + " " + physician_full.getFamilyNameAr(), physician_full.getClincNameAr(), physician_full.getService().getDescAr(), selectedDate, mAdapter.getSelectedTime());
            else
                setDataSheet(physician_full.getGivenName() + " " + physician_full.getFamilyName(), physician_full.getClinicNameEn(), physician_full.getService().getDesc(), selectedDate, mAdapter.getSelectedTime());

        } else {
            setDataSheet(physician.getProviderDescription(), specialityCode, specialityDescription, selectedDate, mAdapter.getSelectedTime());
        }

        if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    @Override
    public void onGetPhysicianList(PhysicianResponse response) {
        lstOfPhysician = new ArrayList<>();
        physicianResponse = response;
        if (response.getPhysicians() != null && response.getPhysicians().size() > 0) {

            for (int i = 0; i < response.getPhysicians().size(); i++) {
                PhysicianResponse.Physician physician = response.getPhysicians().get(i);
                if (physician.getService() != null && !physician.getDocCode().equalsIgnoreCase(mPhysicianId)) {
                    presenter.fetchAlternatePhysicians(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            physician.getDocCode(), physician.getClinicCode(), physician.getService().getId(), physician.getDeptCode(),
                            "0", selectedDate, selectedDate, i, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                }
            }
            //   presenter.fetchAlternatePhysiciansNew(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), response.getPhysicians(), selectedDate);

        }


    }

    @Override
    public void onGetTimeSlotsAlternate(TimeSlotsNextResponse response, int pos) {
        if (response.getTimeSlots().size() != 0) {
            PhysicianResponse.Physician physician = physicianResponse.getPhysicians().get(pos);
            physician.setDate(response.getTimeSlots().get(0).getSessionDateShort());
            physician.setTime(response.getTimeSlots().get(0).getSessionTimeShort());
            physician.setSessionId(String.valueOf(response.getTimeSlots().get(0).getSessionId()));

            lstOfPhysician.add(physician);
        }

        physician_pos = 0;
        setData(physician_pos);
    }

    @Override
    public void onGetServiceList(DrServiceResponse serviceResponse) {
        listServices = new ArrayList<>();
        listServicesAr = new ArrayList<>();
        listServicesCode = new ArrayList<>();
        listServicesType = new ArrayList<>();


        boolean isTele = false;

        for (int i = 0; i < serviceResponse.getServiceList().size(); i++) {
            DrServiceResponse.ServiceList serviceList = serviceResponse.getServiceList().get(i);

            if (rdInPersoh.isChecked()) {
                if (serviceList.getType().equalsIgnoreCase("Offline")) {
                    listServices.add(serviceResponse.getServiceList().get(i).getServiceDescription());
                    listServicesAr.add(serviceResponse.getServiceList().get(i).getArabicServiceDescription());

                    listServicesCode.add(serviceResponse.getServiceList().get(i).getServiceCode());
                    listServicesType.add(serviceResponse.getServiceList().get(i).getType());
                }
            }

            if (rdTelemed.isChecked()) {
                if (serviceList.getType().equalsIgnoreCase("Online")) {
                    listServices.add(serviceResponse.getServiceList().get(i).getServiceDescription());
                    listServicesAr.add(serviceResponse.getServiceList().get(i).getArabicServiceDescription());

                    listServicesCode.add(serviceResponse.getServiceList().get(i).getServiceCode());
                    listServicesType.add(serviceResponse.getServiceList().get(i).getType());
                }

            }
            if (serviceList.getType().equalsIgnoreCase("Online")) {
                isTele = true;
            }
        }

        if (rdTelemed.isChecked()) {
            if (listServices.size() == 0) {
                showAlert(AppointmentActivity.this, getString(R.string.no_telemedicine_services));
            }
        }
        ArrayAdapter aa;
        if (lan.equalsIgnoreCase(ENGLISH)){
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listServices);
        }else {
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listServicesAr);
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


        if (isTele) {
            rdTelemed.setVisibility(View.VISIBLE);
        } else {
            rdTelemed.setVisibility(View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onGetTimeSlot(DrTimeSlot response) {
        try {
//            lstOfTimeSlot.clear();
            DrlstOfTimeSlot.clear();
//            lstOfTimeSlotInperson.clear();
//            lstOfTimeSlotMedicine.clear();
            noSlots.setAutoLinkMask(Linkify.PHONE_NUMBERS);
//            noSlots.setText(response.getStatus()); // getString(R.string.no_time_slot));
            noSlots.setMovementMethod(LinkMovementMethod.getInstance());

//        Integer sessionResponse = response.getTimeSlots().get(0).getSessionId();
//
//        Intent intent = new Intent();
//        intent.putExtra("SessionID", sessionResponse);
//        startActivity(intent);


            if (response.getSlotList().size() > 0) {
                LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(45);
                String nowTime = expirationTime.format(DateTimeFormatter.ofPattern("HH:mm",Locale.ENGLISH));
                String mNowTime=nowTime.replace(":",".");
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                for (int i=0;i<response.getSlotList().size();i++){
                    DrTimeSlot.SlotList slotList=response.getSlotList().get(i);

                    String slotDateString = slotList.getSlotDate();
                    String slotTime = slotList.getSlotFromTime();
                    String mSlotTime=slotTime.replace(":",".");

                    Double dNowTime= Double.valueOf(mNowTime);
                    Double dSlotTime= Double.valueOf(mSlotTime);

                    Log.e("abcd","dNowTime "+dNowTime);
                    Log.e("abcd","dSlotTime "+dSlotTime);

                    Date slotDate = formatter1.parse(slotDateString);
                    if (new Date().before(slotDate)) {
                        Log.e("abcd","after ");

                        DrlstOfTimeSlot.add(slotList);
                    }else {
                        Log.e("abcd","equal ");

                        if(dNowTime<dSlotTime){
                            DrlstOfTimeSlot.add(slotList);
                        }
                    }


                }

//                DrlstOfTimeSlot.addAll(response.getSlotList());
                //----------------------------->

                //----------------------------->

                if (DrlstOfTimeSlot.size() == 0) {
                    label.setVisibility(View.GONE);
//                    label1.setVisibility(View.GONE);
                    radio.setVisibility(View.VISIBLE);
                    noSlots.setVisibility(View.VISIBLE); // VISIBLE
                    noSlots.setText(getString(R.string.no_time_slot));

                    rvSlots.setVisibility(View.GONE);
                    layBtnBook.setVisibility(View.GONE);//here
                    Log.e("VISISBLE1", "VISISBLE1");
                    Toast.makeText(AppointmentActivity.this, response.getStatus(), Toast.LENGTH_LONG).show();

                } else {
                    label.setVisibility(View.VISIBLE);
                    label1.setVisibility(View.VISIBLE);
                    radio.setVisibility(View.VISIBLE);
                    noSlots.setVisibility(View.GONE);
                    rvSlots.setVisibility(View.VISIBLE);
//                layLegend.setVisibility(View.VISIBLE);  // @PM changed on 23oct
                    layBtnBook.setVisibility(View.VISIBLE);

                }

            } else {
                label.setVisibility(View.GONE);
//                label1.setVisibility(View.GONE);
//                radio.setVisibility(View.GONE);
                noSlots.setVisibility(View.VISIBLE); //VISIBLE
                rvSlots.setVisibility(View.GONE);
                layLegend.setVisibility(View.GONE);
                layBtnBook.setVisibility(View.GONE);
                noSlots.setText(getString(R.string.no_time_slot));
                Log.e("VISISBLE2", "VISISBLE2");
            }


            Collections.sort(DrlstOfTimeSlot, (o1, o2) -> o1.getSlotFromTime().compareTo(o2.getSlotFromTime()));
            /*if (!getIntent().hasExtra("booking")) { // if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() == 0    @chnaged byPm
                if (rdTelemed.isChecked())
                    mAdapter.getFilter(2).filter("ff");
                else
                    mAdapter.getFilter(1).filter("ff");
            } else if (getIntent().hasExtra("booking")) {
                if (mBookingInfo.getTeleBooking() == 1) {
                    mAdapter.getFilter(2).filter("ff");
                } else {
                    mAdapter.getFilter(1).filter("ff");
                }
            }*/

            mAdapter.setSelectedDate(selectedDate, bookingTime);
            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewBook(BookResponse response) {
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(AppointmentActivity.this).inflate(R.layout.simple_alert, viewGroup, false);
//        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);
//
//
//        TextView contentTextView=dialogView.findViewById(R.id.content);
//        TextView buttonOk=dialogView.findViewById(R.id.buttonOk);
//        contentTextView.setText(getString(R.string.appointment_successfull));
//        builder.setView(dialogView);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        buttonOk.setOnClickListener(v -> alertDialog.dismiss());
//
//        alertDialog.show();


//        Intent in  = new Intent(this, MainActivity.class);
//        startActivity(in);
//        finish();

//        Toast.makeText(this, getString(R.string.appointment_successfull), Toast.LENGTH_LONG).show();

        showAppointmentPaymentDailog();


    }

    private void showAppointmentPaymentDailog() {
        MyhttpMethod myhttpMethod = new MyhttpMethod(AppointmentActivity.this);

        myhttpMethod.getServicePrice(mAdapter.getSessionId(), true, new ServicePriceListner() {
            @Override
            public void onSuccess(ServicePrice servicePrice) {
                double patientShare = Double.parseDouble(servicePrice.getPatientShare());
                if (patientShare < 1) {
                    Intent in = new Intent(AppointmentActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra(booking_successfully_booked, booking_successfully_booked_code);
                    startActivity(in);
                    finish();
                    return;
                }

                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(AppointmentActivity.this).inflate(R.layout.confirm_booking, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
                String clinicName = specialityDescription;
                text_clinic.setText(clinicName);

                TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
                String fullName = physician.getProviderDescription();

                text_phisician.setText(fullName);

                TextView text_date = dialogView.findViewById(R.id.text_date);
                text_date.setText(fromDate);

                TextView text_time = dialogView.findViewById(R.id.text_time);
                text_time.setText(bookingslotTime);

                TextView text_consultent_type = dialogView.findViewById(R.id.text_consultent_type);
                text_consultent_type.setText(consultationtype);

                TextView text_patient_pay = dialogView.findViewById(R.id.patient_pay);
                text_patient_pay.setText(servicePrice.getPatientShare());

//                TextView text_discount = dialogView.findViewById(R.id.text_discount);
//                text_discount.setText(servicePrice.getPayorShare());

//                TextView text_vat = dialogView.findViewById(R.id.text_vat);
//                text_vat.setText(servicePrice.getPayorShare());


                TextView text_total_amount = dialogView.findViewById(R.id.text_total_amount);
                text_total_amount.setText(servicePrice.getOrderPrice());

                TextView service_price = dialogView.findViewById(R.id.service_price);
                service_price.setText(servicePrice.getOrderPrice());

                TextView patient_pay = dialogView.findViewById(R.id.patient_pay);
                patient_pay.setText(servicePrice.getPatientShare());


                ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
                clear_dailog.setOnClickListener(view -> alertDialog.dismiss());

                if (SharedPreferencesUtils.getInstance().getValue(Constants.KEY_LANGUAGE, ENGLISH).equalsIgnoreCase(ARABIC)) {
                    text_phisician.setText(physician.getArabicProviderDescription());

                    try {
                        if (!arSpecialityDescription.isEmpty())
                            text_clinic.setText(arSpecialityDescription);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                AppCompatButton paynow = dialogView.findViewById(R.id.paynow);
                AppCompatButton pay_later = dialogView.findViewById(R.id.pay_later);

                pay_later.setOnClickListener(view -> {

                    Intent in = new Intent(AppointmentActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra(booking_successfully_booked, booking_successfully_booked_code);
                    startActivity(in);
                    finish();
                });
                if (servicePrice.getIs_final_price()) {
                    paynow.setVisibility(View.VISIBLE);
                }

                if(servicePrice.equals("0")){
                    paynow.setVisibility(View.GONE);
                    pay_later.setText("Close");
                }


                Log.e("test","test");

                paynow.setOnClickListener(view -> {
                    ValidateResponse user = SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_USER);

                    String serviceCode = listServicesCode.get(spinner.getSelectedItemPosition());
                    String slotBookingId = mAdapter.getSessionId();
                    String patientId = user.getPatientId();
                    String payerForname = user.getFirstName();
                    String payerSurname = user.getLastName();
                    String docCode = physician.getProviderCode();
                    String docName = physician.getProviderDescription();
                    String payerEmail = user.getEmail(AppointmentActivity.this);

                    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm a").format(Calendar.getInstance().getTime());
                    String appointmentDate = timeStamp;

                    String payerAddress = user.getAddress();
                    String amount = servicePrice.getPatientShare();
                    String serviceName = listServices.get(spinner.getSelectedItemPosition());
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
                    myhttpMethod.generatePaymentUrl(AppointmentActivity.this, requestBodyForPaymentUrlGeneration);
                });

                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });

    }

    @Override
    public void onGetTimeSlotsAlternateNew(TimeSlotsNextResponse[] timeSlotsNextResponses) {
//        if (timeSlotsNextResponses.length != 0) {
//            PhysicianResponse.Physician physician = physicianResponse.getPhysicians().get(pos);
//            physician.setDate(response.getTimeSlots().get(0).getSessionDateShort());
//            physician.setTime(response.getTimeSlots().get(0).getSessionTimeShort());
//            physician.setSessionId(String.valueOf(response.getTimeSlots().get(0).getSessionId()));
//
//            lstOfPhysician.add(physician);
//        }
//
//        physician_pos = 0;
//        setData(physician_pos);


    }

    void setData(int physician_pos) {
        if (physician_pos == 0) {

        } else {
            tvBookAppointmentDoctor.setText(getString(R.string.book_now));
            if (lstOfPhysician.size() == 0) {
                layDoctor.setVisibility(View.INVISIBLE);
            } else {
                // layDoctor.setVisibility(View.VISIBLE);//here
                layBtnBook.setVisibility(View.GONE); // new
                rvSlots.setVisibility(View.GONE); // new
                label.setVisibility(View.GONE); // new

                if (physician_pos == lstOfPhysician.size() - 1) {
                    iv_next.setEnabled(false);
                    iv_next.setVisibility(View.GONE); //

                    if (physician_pos > 0) {
                        iv_prev.setEnabled(true);
                        iv_prev.setVisibility(View.VISIBLE); //
                    } else {
                        iv_prev.setEnabled(false);
                        iv_prev.setVisibility(View.GONE);       //
                    }
                } else {
                    iv_next.setVisibility(View.VISIBLE);  //
                    iv_next.setEnabled(true);

                    if (physician_pos > 0) {
                        iv_prev.setEnabled(true);
                        iv_prev.setVisibility(View.VISIBLE);   //
                    } else {
                        iv_prev.setEnabled(false);
                        iv_prev.setVisibility(View.GONE);      //
                    }
                }

                PhysicianResponse.Physician physician = lstOfPhysician.get(physician_pos);
                //     layDoctor.setVisibility(View.VISIBLE);

                tvNextAvailableTime.setVisibility(View.VISIBLE);
                String mystring = getString(R.string.next_available_time) + "\n" + Common.parseShortDate(physician.getDate()) + ", " + Common.parseShortTime(physician.getTime());
                tvNextAvailableTime.setText(mystring);

                String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

                if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                    if (physician.getGivenNameAr() != null)
                        if (physician.getFamilyNameAr() != null)
                            tvPhysicianName.setText(getString(R.string.dr) + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr());
                        else
                            tvPhysicianName.setText(getString(R.string.dr) + " " + physician.getGivenNameAr());
                    else
                        tvPhysicianName.setText(getString(R.string.dr) + " ");

                    if (physician.getSpecialitiesTxtAr() != null) {
                        Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxtAr());
                        String trimmed = spannableString.toString().trim();
                        tvPhysicianSpeciality.setText(trimmed);
                    } else
                        tvPhysicianSpeciality.setText("");

                    if (physician.getClincNameAr() != null)
                        tvClinicService.setText(physician.getClincNameAr());
                    else
                        tvClinicService.setText("");
                } else {
                    if (physician.getGivenName() != null)
                        if (physician.getFamilyName() != null)
                            tvPhysicianName.setText(getString(R.string.dr) + " " + physician.getGivenName() + " " + physician.getFamilyName());
                        else
                            tvPhysicianName.setText(getString(R.string.dr) + " " + physician.getGivenName());
                    else
                        tvPhysicianName.setText(getString(R.string.dr) + " ");

                    if (physician.getSpecialitiesTxt() != null) {
                        Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxt());
                        String trimmed = spannableString.toString().trim();
                        tvPhysicianSpeciality.setText(trimmed);
                    } else
                        tvPhysicianSpeciality.setText("");

                    if (physician.getClinicNameEn() != null)
                        tvClinicService.setText(physician.getClinicNameEn());
                    else
                        tvClinicService.setText("");
                }

                if (physician.getDocImg() != null)
                    if (physician.getDocImg().length() != 0) {
                        String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + physician.getDocImg();
                        Picasso.get().load(url)
                                .error(R.drawable.mdoc_placeholder)
                                .placeholder(R.drawable.male_img).fit().into(ivPhysicianPic);
                    }
            }
        }


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
        if (layBtnBook != null)
            layBtnBook.setEnabled(true);
        Toast.makeText(AppointmentActivity.this, msg, Toast.LENGTH_LONG).show();

//        makeToast(msg);
    }

    @Override
    public void onNoInternet() {
        Common.noInternet(this);
    }


    //  Disable dates decorator
    private class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(final CalendarDay day) {
//            final Calendar calendar = Calendar.getInstance();
//            day.getDay();
//            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//            calendar.set(Calendar.MONTH, day.getMonth());
            // calendar.set(day.getYear(), day.getMonth(), day.getDay());
//            String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
            String date = day.getDay() + "/" + day.getMonth() + "/" + day.getYear();

            if (calendarDates.get(date) != null)
                return calendarDates.get(date);
            else
                return false;
        }

        @Override
        public void decorate(final DayViewFacade view) {
            view.setDaysDisabled(true);
        }

    }

    // initiate BottomSheet
    private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.rl_confirm);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);

        findViewById(R.id.iv_back_bottom).setOnClickListener(vv -> {
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

                if (layBtnBook != null)
                    layBtnBook.setEnabled(true);
            }
        });

        findViewById(R.id.tv_yes).setOnClickListener(vv -> {

            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            if (mUserType.equalsIgnoreCase(Constants.GUEST_TYPE)) {
                String lang;
                String gender = "";

                if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.male))) {
                    gender = "M";
                } else if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.female))) {
                    gender = "F";
                } else if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.GENDER, "").equalsIgnoreCase(getString(R.string.unspecified))) {
                    gender = "U";
                }


                if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.LANG, "").equalsIgnoreCase(getString(R.string.arabic_lang))) {
                    lang = "ar";
                } else {
                    lang = "en";
                }

                // call Book appointment API and get back to Home
                presenter.callBookAppointmentGuest(
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.IQAMA_ID, ""),

                        String.valueOf(mAdapter.getSessionId()),
                        mAdapter.getSelectedTime(), selectedDate,

                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FIRST_NAME, ""),
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FIRST_NAME_AR, ""),

                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FATHER_NAME, ""),
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FATHER_NAME_AR, ""),

                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FAMILY_NAME, ""),
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.FAMILY_NAME_AR, ""),

                        gender,
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.PHONE, ""), "",//Phone two

                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.DOB, ""),
                        lang,
                        SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.GUEST.REASON_VISIT, ""));

                SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, mAdapter.getSessionId());

            } else {
                if (getIntent().hasExtra("booking")) { //getIntent().hasExtra("booking") mBookingId.length() > 0         @chnaged byPm
                    // call API for reschedule
                    presenter.callRescheduleAppointment(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_MRN, ""),
                            String.valueOf(mAdapter.getSessionId()), mAdapter.getSelectedTime(), selectedDate, mBookingId, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, mAdapter.getSessionId());
                } else {
                    // call Book appointment API and get back to Home
                    String teleHealthFlag = "0";
                    if (rdTelemed.isChecked())
                        teleHealthFlag = "1";

                    presenter.callBookAppointment(SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_MRN, ""),
                            String.valueOf(mAdapter.getSessionId()), mAdapter.getSelectedTime(), selectedDate, teleHealthFlag, SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

                    SharedPreferencesUtils.getInstance(this).setValue(Constants.SESSION_ID, mAdapter.getSessionId());
                }
            }
        });

        findViewById(R.id.tv_no).setOnClickListener(vv -> {
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

                if (layBtnBook != null)
                    layBtnBook.setEnabled(true);
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
                        if (layBtnBook != null)
                            layBtnBook.setEnabled(true);
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

                            if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                                Intent i = new Intent(AppointmentActivity.this, GuestHomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent i1 = new Intent(AppointmentActivity.this, MainActivity.class);
                                i1.putExtra("restart", "yes");
                                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);
                            }

                        } else {
                            if (layBtnBook != null)
                                layBtnBook.setEnabled(true);
                        }


                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(AppointmentActivity.this, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    // Alert dialog
    public void alertBack(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        finish();
                        AppointmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                });

        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(dlg -> {

            try {
                Typeface typeface = ResourcesCompat.getFont(AppointmentActivity.this, R.font.font_app);
                ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(AppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    @Override
    protected void onDestroy() {
        if (unBinder != null) unBinder.unbind();
        super.onDestroy();
    }


    public boolean isValueExist(int type, List<TimeSlotsResponse.TimeSlot> customers) {
        Iterator<TimeSlotsResponse.TimeSlot> iterator = customers.iterator();
        while (iterator.hasNext()) {
            TimeSlotsResponse.TimeSlot customer = iterator.next();
            if (customer.getSessionType() == type) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Common.onActivityResult(this, requestCode, resultCode, data);
    }

}
