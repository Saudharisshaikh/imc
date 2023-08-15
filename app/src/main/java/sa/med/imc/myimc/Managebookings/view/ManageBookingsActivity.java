package sa.med.imc.myimc.Managebookings.view;

import static sa.med.imc.myimc.Managebookings.util.BookingStatus.*;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked_code;
import static sa.med.imc.myimc.Utils.Common.getFromDate;
import static sa.med.imc.myimc.Utils.Common.toAMPM;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.CancelledBookingAdapter;
import sa.med.imc.myimc.Adapter.CompletedBookingAdapter;
import sa.med.imc.myimc.Adapter.NoShowBookingAdapter;
import sa.med.imc.myimc.Adapter.UpcomingBookingAdapter;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Interfaces.CheckLocation;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.presenter.BookingImpl;
import sa.med.imc.myimc.Managebookings.presenter.BookingPresenter;
import sa.med.imc.myimc.Managebookings.util.BookingStatus;
import sa.med.imc.myimc.Managebookings.util.OnCancelResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Notifications.view.NotificationsActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.ViewModel.CreateRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Telr.TelrFragment;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.NonSwipeViewPager;

import java.io.Serializable;
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
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.AppointmentListner;
import sa.med.imc.myimc.globle.interfaces.PatientArrivalListner;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;

public class ManageBookingsActivity extends AppCompatActivity {

    List<BookingAppoinment> upcomingList = new ArrayList<>();
    List<BookingAppoinment> completedList = new ArrayList<>();
    List<BookingAppoinment> cancelledList = new ArrayList<>();
    List<BookingAppoinment> noshowList = new ArrayList<>();
    List<BookingAppoinment> arrivedList = new ArrayList<>();
    ImageView iv_back, iv_filter;

    public static void startActivityForResult(Activity activity) {
        Log.v(activity.getClass().getSimpleName(), "startActivityForResult: ");
        Intent intent = new Intent(activity, ManageBookingsActivity.class);
        activity.startActivityForResult(intent, TelrActivity.PAYMENT_CODE);
    }

    TabLayout tab_layout;
    String fromDate;
    String toDate;
    RecyclerView appoinment_recycler_view;
    TextView total_count;

    boolean isUpdated = false;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ManageBookingsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ManageBookingsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        appoinment_recycler_view = findViewById(R.id.appoinment_recycler_view);
        iv_back = findViewById(R.id.iv_back);
        iv_filter = findViewById(R.id.iv_filter);
        appoinment_recycler_view.setHasFixedSize(true);
        appoinment_recycler_view.setLayoutManager(new LinearLayoutManager(ManageBookingsActivity.this));
        tab_layout = findViewById(R.id.tab_layout);
        total_count = findViewById(R.id.total_count);

        iv_back.setOnClickListener(view -> onBackPressed());
        iv_filter.setOnClickListener(view -> openFilterDailo());

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("abcd", String.valueOf(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:
                        setUpcoming();
                        break;
                    case 1:
                        setCompleted();
                        break;
                    case 2:
                        setCancelled();
                        break;
                    case 3:
                        setNoshow();
                        break;
                    case 4:
                        setArrived();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        try {
            toDate = getFromDate(4, 0);
            fromDate = getFromDate(0, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fromDate = fromDate.replace(".", "");
        toDate = toDate.replace(".", "");

        Log.e("abcd", fromDate);
        Log.e("abcd", toDate);

        loadAppointList();
    }

    private void openFilterDailo() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ManageBookingsActivity.this).inflate(R.layout.fillter_dailog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageBookingsActivity.this);


        TextView to_date = dialogView.findViewById(R.id.to_date);
        TextView from_date = dialogView.findViewById(R.id.from_date);
        TextView button_apply = dialogView.findViewById(R.id.button_apply);

        LinearLayout from_date_lay = dialogView.findViewById(R.id.from_date_lay);
        LinearLayout to_date_lay = dialogView.findViewById(R.id.to_date_lay);


        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        to_date.setText(toDate);
        from_date.setText(fromDate);

        to_date_lay.setOnClickListener(view -> {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
            Date date = null;
            try {
                date = dateFormat.parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("abcd", e.toString());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                    DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                    Date date = null;
                    try {
                        int newMonth = months;
                        if (months == 12) {
                            newMonth = 1;
                        } else {
                            newMonth = newMonth + 1;
                        }

                        date = originalFormat.parse(days + "-" + newMonth + "-" + years);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = targetFormat.format(date);
                    to_date.setText(formattedDate);
                    toDate = formattedDate;
                }
            }, year, month, day);
            datePickerDialog.show();
        });
        from_date_lay.setOnClickListener(view -> {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
            Date date = null;
            try {
                date = dateFormat.parse(fromDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("abcd", e.toString());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                    DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                    Date date = null;
                    try {
                        int newMonth = months;
                        if (months == 12) {
                            newMonth = 1;
                        } else {
                            newMonth = newMonth + 1;
                        }

                        date = originalFormat.parse(days + "-" + newMonth + "-" + years);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = targetFormat.format(date);
                    from_date.setText(formattedDate);
                    fromDate = formattedDate;
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        button_apply.setOnClickListener(view -> {
            loadAppointList();
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void loadAppointList() {
        new MyhttpMethod(ManageBookingsActivity.this).getAppoinmentList(ManageBookingsActivity.this,new UrlWithURLDecoder().getBookings_all(),
                SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.HOSPITAL_CODE, "IMC"), fromDate, toDate, new AppointmentListner() {
                    @Override
                    public void onSuccess(List<BookingAppoinment> bookingAppoinmentList) {
//                        Common.messageDailog(ManageBookingsActivity.this,getString(R.string.appointment_message));

                        for (int i = 0; i < bookingAppoinmentList.size(); i++) {
                            BookingAppoinment bookingAppoinment = bookingAppoinmentList.get(i);
                            switch (bookingAppoinment.getBookingStatus()) {

                                case UPCOMING:
                                    //2022-07-01 10:00:00
                                    addUpoming(bookingAppoinment);
                                    break;

                                case CANCELLED:
                                    cancelledList.add(bookingAppoinment);
                                    break;

                                case ATTENDED:
                                    completedList.add(bookingAppoinment);
                                    break;

                                case NO_SHOW:
                                    noshowList.add(bookingAppoinment);
                                    break;
                                case ARRIVED:
                                    completedList.add(bookingAppoinment);
//                                    addUpoming(bookingAppoinment);
                                    break;


                            }
                        }
                        setUpcoming();
                    }
                });
    }

    private void addUpoming(BookingAppoinment bookingAppoinment) {
        String fullDate = bookingAppoinment.getApptDateString().trim();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);

        try {
            Date appointmentDate = formatter1.parse(fullDate);

            if (new Date().before(appointmentDate)) {
                Log.e("abcd", "before");
                try {
                    if (bookingAppoinment.getTelemedicineStatus().trim().equalsIgnoreCase("Completed")){
                        completedList.add(bookingAppoinment);
                    }else {
                        upcomingList.add(bookingAppoinment);
                    }
                } catch (Exception e) {
                    upcomingList.add(bookingAppoinment);
                }

            } else {
                Log.e("abcd", "after");
                noshowList.add(bookingAppoinment);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("abcd", e.toString());
        }

    }

    private void setUpcoming() {
        Log.e("abcd", "Upcoming");
        UpcomingBookingAdapter upcomingBookingAdapter = new UpcomingBookingAdapter(ManageBookingsActivity.this, ManageBookingsActivity.this, upcomingList, new AppoinmentOnclick() {
            @Override
            public void onPay(BookingAppoinment booking) {
                payAppoinment(booking);
            }

            @Override
            public void onCancel(BookingAppoinment booking) {
                MyhttpMethod myhttpMethod = new MyhttpMethod(ManageBookingsActivity.this);
                myhttpMethod.cancelAppointment(ManageBookingsActivity.this,booking, new OnCancelResponse() {
                    @Override
                    public void onSuccess() {
                        upcomingList.clear();
//                        cancelledList.clear();
//                        completedList.clear();
//                        noshowList.clear();
//                        arrivedList.clear();
                        loadAppointList();
                        isUpdated = true;
                    }
                });
            }

            @Override
            public void videoCall(int position, String DrName) {
//                SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).setValue(Constants.KEY_VIDEO_PHYSICIAN, upcomingList.get(position).getDocCode());
                SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).setValue(Constants.KEY_VIDEO_PHYSICIAN, "DR046");
                SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
//                clickToStartVideoCall(upcomingList.get(position).getId(), upcomingList.get(position).getApptDateString());
            }
        }, new PatientArrivalListner() {
            @Override
            public void onSuccess(String response) {
                Log.e("abcd", response);
                Toast.makeText(ManageBookingsActivity.this, response, Toast.LENGTH_LONG).show();

                upcomingList.clear();
                completedList.clear();
                cancelledList.clear();
                noshowList.clear();
                arrivedList.clear();
                loadAppointList();


            }

            @Override
            public void onFaild(String massage) {
                dailog(massage);
//                Toast.makeText(ManageBookingsActivity.this, massage, Toast.LENGTH_LONG).show();
            }
        });
        appoinment_recycler_view.setAdapter(upcomingBookingAdapter);
        total_count.setText(getString(R.string.total_upcoming) + upcomingList.size());

        checkAvalable(upcomingList.size());
    }

    private void checkAvalable(int size) {
        if (size == 0) {
            findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
        }
    }


    private void setCompleted() {
        CompletedBookingAdapter completedBookingAdapter = new CompletedBookingAdapter(ManageBookingsActivity.this, completedList);
        appoinment_recycler_view.setAdapter(completedBookingAdapter);
        total_count.setText(getString(R.string.total_completed) + completedList.size());
        checkAvalable(completedList.size());

    }

    private void setCancelled() {
        CancelledBookingAdapter cancelledBookingAdapter = new CancelledBookingAdapter(ManageBookingsActivity.this, cancelledList);
        appoinment_recycler_view.setAdapter(cancelledBookingAdapter);
        total_count.setText(getString(R.string.total_cancelled) + cancelledList.size());
        checkAvalable(cancelledList.size());

    }

    private void setNoshow() {
        NoShowBookingAdapter noShowBookingAdapter = new NoShowBookingAdapter(ManageBookingsActivity.this, noshowList);
        appoinment_recycler_view.setAdapter(noShowBookingAdapter);
        total_count.setText(getString(R.string.total_no_show) + noshowList.size());
        checkAvalable(noshowList.size());

    }

    private void setArrived() {
    }

    private void payAppoinment(BookingAppoinment booking) {
        MyhttpMethod myhttpMethod = new MyhttpMethod(this);

        myhttpMethod.getServicePrice(booking.getSlotBookingId(), true, new ServicePriceListner() {
            @Override
            public void onSuccess(ServicePrice servicePrice) {
                double patientShare= Double.parseDouble(servicePrice.getPatientShare());
                if (patientShare<1){
                    Common.messageDailog(ManageBookingsActivity.this,getResources().getString(R.string.pament_zero));
                    return;
                }

                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(ManageBookingsActivity.this).inflate(R.layout.confirm_booking, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageBookingsActivity.this);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
                text_clinic.setText(booking.getSpecialityDescription());

                TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
                String fullName = booking.getCareProviderDescription();

                text_phisician.setText(fullName);

                String fullDate = booking.getApptDateString();
                String year = fullDate.substring(0, 4);
                String month = fullDate.substring(5, 7);
                String dayOfMonth = fullDate.substring(8, 10);

                TextView text_date = dialogView.findViewById(R.id.text_date);
                text_date.setText(dayOfMonth + "-" + month + "-" + year);

                //2022-07-01 10:00:00
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


                TextView service_price = dialogView.findViewById(R.id.service_price);
                service_price.setText(servicePrice.getOrderPrice());

                TextView patient_pay = dialogView.findViewById(R.id.patient_pay);
                patient_pay.setText(servicePrice.getPatientShare());

                TextView text_vat = dialogView.findViewById(R.id.text_vat);
                text_vat.setText(servicePrice.getPayorShare());

                TextView text_total_amount = dialogView.findViewById(R.id.text_total_amount);
                text_total_amount.setText(servicePrice.getOrderPrice());

                LinearLayout not_pay_layout = dialogView.findViewById(R.id.not_pay_layout);
                LinearLayout pay_layout = dialogView.findViewById(R.id.pay_layout);

                ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
                clear_dailog.setOnClickListener(view -> alertDialog.dismiss());

                AppCompatButton ok = dialogView.findViewById(R.id.ok);
                AppCompatButton paynow = dialogView.findViewById(R.id.paynow);
                AppCompatButton pay_later = dialogView.findViewById(R.id.pay_later);

                ok.setOnClickListener(view -> {
                    alertDialog.dismiss();
                });

                pay_later.setOnClickListener(view -> {
                    alertDialog.dismiss();
                });
                pay_later.setVisibility(View.GONE);

                if (servicePrice.getIs_final_price()) {
                    not_pay_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                } else {
                    pay_layout.setVisibility(View.GONE);
                    not_pay_layout.setVisibility(View.VISIBLE);
                }

                if (patientShare<1){
                    pay_layout.setVisibility(View.GONE);
                    not_pay_layout.setVisibility(View.VISIBLE);
                }else {
                    not_pay_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                }

                paynow.setOnClickListener(view -> {
                    ValidateResponse user = SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).getValue(Constants.KEY_USER);

                    String serviceCode = booking.getServiceCode();
                    String slotBookingId = booking.getSlotBookingId();
                    String patientId = user.getPatientId();
                    String payerForname = user.getFirstName();
                    String payerSurname = user.getLastName();
                    String docCode = booking.getCareProviderCode();
                    String docName = booking.getCareProviderDescription();
                    String payerEmail = user.getEmail(ManageBookingsActivity.this);


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
                    isUpdated = true;
                    myhttpMethod.generatePaymentUrl(ManageBookingsActivity.this,requestBodyForPaymentUrlGeneration);
                });

                alertDialog.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (isUpdated) {
            setResult(Activity.RESULT_OK, returnIntent);
        }
//        else {
//            setResult(Activity.RESULT_CANCELED, returnIntent);
//
//        }
        finish();

    }


    private void dailog(String msg) {
        Log.e("bcd", "Token Expire");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ManageBookingsActivity.this).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageBookingsActivity.this);


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


}
