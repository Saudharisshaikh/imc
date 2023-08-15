package sa.med.imc.myimc.Managebookings.ui;

import static sa.med.imc.myimc.Managebookings.ui.ManageBookingMainActivity.addHistory;
import static sa.med.imc.myimc.Managebookings.ui.ManageBookingMainActivity.removeHistory;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.ARRIVED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.ATTENDED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.CANCELLED;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.NO_SHOW;
import static sa.med.imc.myimc.Managebookings.util.BookingStatus.UPCOMING;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked_code;
import static sa.med.imc.myimc.Utils.Common.getFromDate;
import static sa.med.imc.myimc.Utils.Common.simpleDailog;
import static sa.med.imc.myimc.Utils.Common.toAMPM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sa.med.imc.myimc.Adapter.CancelledBookingAdapter;
import sa.med.imc.myimc.Adapter.CompletedBookingAdapter;
import sa.med.imc.myimc.Adapter.NoShowBookingAdapter;
import sa.med.imc.myimc.Adapter.UpcomingBookingAdapter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.util.OnCancelResponse;
import sa.med.imc.myimc.Managebookings.view.AppoinmentOnclick;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.AppointmentListner;
import sa.med.imc.myimc.globle.interfaces.PatientArrivalListner;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;
import static sa.med.imc.myimc.Managebookings.ui.ManageBookingMainActivity.isUpdated;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class ManageBookingFragment extends Fragment {

    List<BookingAppoinment> upcomingList = new ArrayList<>();
    List<BookingAppoinment> completedList = new ArrayList<>();
    List<BookingAppoinment> cancelledList = new ArrayList<>();
    List<BookingAppoinment> noshowList = new ArrayList<>();
    List<BookingAppoinment> arrivedList = new ArrayList<>();

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


    String url=new UrlWithURLDecoder().getBookings_all();
    boolean isNew=true;
    TabItem upcomingTab;

    public ManageBookingFragment() {
        // Required empty public constructor
    }

    public ManageBookingFragment(String url,boolean isNew) {
        this.url = url;
        this.isNew=isNew;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appoinment_recycler_view = view.findViewById(R.id.appoinment_recycler_view);

        appoinment_recycler_view.setHasFixedSize(true);
        appoinment_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        tab_layout = view.findViewById(R.id.tab_layout);
        total_count = view.findViewById(R.id.total_count);
        upcomingTab = view.findViewById(R.id.upcomingTab);

        if (!isNew){
            tab_layout.removeTabAt(0);
        }

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("abcd", String.valueOf(tab.getPosition()));
                if (isNew){
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
                    if (tab.getPosition()==0){
                        removeHistory();
                    }else {
                        addHistory();
                    }
                }else {
                    switch (tab.getPosition()) {
                        case 0:
                            setCompleted();
                            break;
                        case 1:
                            setCancelled();
                            break;
                        case 2:
                            setNoshow();
                            break;
                        case 3:
                            setArrived();
                            break;

                    }
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

    private void loadAppointList() {
        new MyhttpMethod(getContext()).getAppoinmentList(getActivity(),url,
                SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.HOSPITAL_CODE, "IMC"), fromDate, toDate, new AppointmentListner() {
                    @Override
                    public void onSuccess(List<BookingAppoinment> bookingAppoinmentList) {

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
                        if (isNew){
                            setUpcoming();
                        }else {
                            setCompleted();
                        }
                    }
                });
    }

    private void addUpoming(BookingAppoinment bookingAppoinment) {
        String fullDate = bookingAppoinment.getApptDateString().trim();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);


        LocalDate todayDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formatter = formatter.withLocale(Locale.ENGLISH);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDate appDate = LocalDate.parse(fullDate, formatter);

        SharedPreferences sp = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        String l = sp.getString("inperson_after_buffer_time","300");
        Integer inpersonAfterBufferTime = Integer.parseInt(l);
        long diff = Duration.between(todayDate.atStartOfDay(), appDate.atStartOfDay()).toMinutes();

        try {
            Date appointmentDate = formatter1.parse(fullDate);

            if (new Date().before(appointmentDate)) {
                try {
                    if (bookingAppoinment.getTelemedicineStatus().trim().equalsIgnoreCase("Completed")){
                        completedList.add(bookingAppoinment);
                    }else if(diff<inpersonAfterBufferTime){
                        noshowList.add(bookingAppoinment);
                    }
                    else
                    {
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
        UpcomingBookingAdapter upcomingBookingAdapter = new UpcomingBookingAdapter(getActivity(), getActivity(), upcomingList, new AppoinmentOnclick() {
            @Override
            public void onPay(BookingAppoinment booking) {
                payAppoinment(booking);
            }

            @Override
            public void onCancel(BookingAppoinment booking) {
                MyhttpMethod myhttpMethod = new MyhttpMethod(getContext());
                myhttpMethod.cancelAppointment(getActivity(),booking, new OnCancelResponse() {
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
                SharedPreferencesUtils.getInstance(getContext()).setValue(Constants.KEY_VIDEO_PHYSICIAN, "DR046");
                SharedPreferencesUtils.getInstance(getContext()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
//                clickToStartVideoCall(upcomingList.get(position).getId(), upcomingList.get(position).getApptDateString());
            }
        }, new PatientArrivalListner() {
            @Override
            public void onSuccess(String response) {
                Log.e("abcd", response);
                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

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
            getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
        }
    }


    private void setCompleted() {
        CompletedBookingAdapter completedBookingAdapter = new CompletedBookingAdapter(getContext(), completedList);
        appoinment_recycler_view.setAdapter(completedBookingAdapter);
        total_count.setText(getString(R.string.total_completed) + completedList.size());
        checkAvalable(completedList.size());

    }

    private void setCancelled() {
        CancelledBookingAdapter cancelledBookingAdapter = new CancelledBookingAdapter(getContext(), cancelledList);
        appoinment_recycler_view.setAdapter(cancelledBookingAdapter);
        total_count.setText(getString(R.string.total_cancelled) + cancelledList.size());
        checkAvalable(cancelledList.size());

    }

    private void setNoshow() {
        NoShowBookingAdapter noShowBookingAdapter = new NoShowBookingAdapter(getContext(), noshowList);
        appoinment_recycler_view.setAdapter(noShowBookingAdapter);
        total_count.setText(getString(R.string.total_no_show) + noshowList.size());
        checkAvalable(noshowList.size());

    }

    private void setArrived() {
    }

    private void payAppoinment(BookingAppoinment booking) {
        MyhttpMethod myhttpMethod = new MyhttpMethod(getContext());

        myhttpMethod.getServicePrice(booking.getSlotBookingId(), true, new ServicePriceListner() {
            @Override
            public void onSuccess(ServicePrice servicePrice) {
                double patientShare= Double.parseDouble(servicePrice.getPatientShare());
                if (patientShare<1){
                    Common.messageDailog(getActivity(),getContext().getResources().getString(R.string.pament_zero));
                    return;
                }


                ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.confirm_booking, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                    simpleDailog(requireActivity(),"",getResources().getString(R.string.pay_zero));
                    return;

//                    pay_layout.setVisibility(View.GONE);
//                    not_pay_layout.setVisibility(View.VISIBLE);
                }else {
                    not_pay_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                }

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
                    myhttpMethod.generatePaymentUrl(getActivity(),requestBodyForPaymentUrlGeneration);
                });

                alertDialog.show();

            }
        });
    }


    private void dailog(String msg) {
        Log.e("bcd", "Token Expire");
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


}