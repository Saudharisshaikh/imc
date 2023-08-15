package sa.med.imc.myimc.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.view.AppoinmentOnclick;
import sa.med.imc.myimc.Managebookings.view.BottomBarcodeDialog;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.Questionaires.view.QuestionareActivity;
import sa.med.imc.myimc.R;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.ViewModel.CreateRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.PatientArrivalListner;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static sa.med.imc.myimc.Managebookings.util.BookingStatus.ARRIVED;
import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;
import static sa.med.imc.myimc.Utils.Common.messageDailog;
import static sa.med.imc.myimc.Utils.Common.showAlert;
import static sa.med.imc.myimc.Utils.Common.toAMPM;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Upcoming Booking list.
 */

public class UpcomingBookingAdapter extends RecyclerView.Adapter<UpcomingBookingAdapter.Viewholder> implements LocationListener {
    List<BookingAppoinment> list;
    Activity context;
    FragmentActivity activity;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;
    String lang;
    AppoinmentOnclick appoinmentOnclick;
    PatientArrivalListner patientArrivalListner;
    private double latitude;
    private double longitude;


    public UpcomingBookingAdapter(Activity context, FragmentActivity fragmentActivity, List<BookingAppoinment> list, AppoinmentOnclick appoinmentOnclick, PatientArrivalListner patientArrivalListner1) {
        this.context = context;
        this.activity = fragmentActivity;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        this.appoinmentOnclick = appoinmentOnclick;
        this.patientArrivalListner = patientArrivalListner1;
        Log.e("abcd", String.valueOf(list.size()));
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_upcoming_booking_new, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        BookingAppoinment booking = list.get(position);

        try {
            if (booking.isDoctorActive()) {
                holder.status_view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.online_status));
            } else {
                holder.status_view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.offline_status));
            }
        } catch (Resources.NotFoundException e) {
            holder.status_view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.offline_status));
        }


        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListPopUp(holder.iv_more, context, onItemClickListener, position, booking);

            }
        });
        Log.e("abcd",booking.getServiceType());
        holder.service_title.setText(booking.getServiceDescription());
        if (booking.getServiceType().equalsIgnoreCase("Offline") | booking.getServiceType().equalsIgnoreCase("0")) {
            holder.status_view.setVisibility(View.INVISIBLE);
            holder.tv_title.setText(context.getString(R.string.next_appointment) + " - "+context.getString(R.string.in_person));
            holder.upcomingVideoCallButton.setVisibility(View.GONE);
            holder.upcoming_lay_checkin.setVisibility(View.VISIBLE);

            if (booking.isPaid()) {
                holder.upcoming_lay_checkin.setClickable(true);
                holder.upcoming_lay_checkin.setEnabled(true);
                holder.upcomingCheckinText.setClickable(true);
                holder.upcomingCheckinText.setEnabled(true);

                holder.upcoming_lay_checkin.setVisibility(View.VISIBLE);
                holder.upcomingCheckinText.setVisibility(View.VISIBLE);
                holder.upcomingCheckinText.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                holder.upcoming_lay_checkin.setClickable(false);
                holder.upcoming_lay_checkin.setEnabled(false);
                holder.upcomingCheckinText.setClickable(false);
                holder.upcomingCheckinText.setEnabled(false);

                holder.upcoming_lay_checkin.setVisibility(View.VISIBLE);


                holder.upcomingCheckinText.setVisibility(View.VISIBLE);
                holder.upcomingCheckinText.setTextColor(context.getResources().getColor(R.color.text_grey_color));
            }

            try {
                if (booking.getShowCheckIn().trim().equalsIgnoreCase("FALSE")){
                    holder.upcoming_lay_checkin.setClickable(false);
                    holder.upcoming_lay_checkin.setEnabled(false);
                    holder.upcomingCheckinText.setClickable(false);
                    holder.upcomingCheckinText.setEnabled(false);

                    holder.upcoming_lay_checkin.setVisibility(View.INVISIBLE);
                    holder.upcomingCheckinText.setVisibility(View.INVISIBLE);
                    holder.upcomingCheckinText.setTextColor(context.getResources().getColor(R.color.text_grey_color));
                }
            } catch (Resources.NotFoundException | NullPointerException e) {
                holder.upcoming_lay_checkin.setClickable(false);
                holder.upcoming_lay_checkin.setEnabled(false);
                holder.upcomingCheckinText.setClickable(false);
                holder.upcomingCheckinText.setEnabled(false);

                holder.upcoming_lay_checkin.setVisibility(View.INVISIBLE);
                holder.upcomingCheckinText.setVisibility(View.INVISIBLE);
                holder.upcomingCheckinText.setTextColor(context.getResources().getColor(R.color.text_grey_color));
            }


        } else {
            holder.status_view.setVisibility(View.VISIBLE);
            holder.tv_title.setText(context.getString(R.string.next_appointment) + " - "+context.getString(R.string.tele_med));
            holder.upcoming_lay_checkin.setVisibility(View.INVISIBLE);
            holder.upcomingVideoCallText.setVisibility(View.VISIBLE);
            if (booking.isPaid()) {
                holder.upcomingVideoCallText.setClickable(true);
                holder.upcomingVideoCallText.setEnabled(true);
                holder.upcomingVideoCallText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.upcomingVideoCallButton.setEnabled(true);
                holder.new_text.setVisibility(View.VISIBLE);
            } else {
                holder.upcomingVideoCallText.setTextColor(context.getResources().getColor(R.color.text_grey_color));
                holder.upcomingVideoCallText.setClickable(false);
                holder.upcomingVideoCallText.setEnabled(false);
                holder.upcomingVideoCallButton.setEnabled(false);
                holder.new_text.setVisibility(View.INVISIBLE);
            }
        }

        //2022-07-01 10:00:00
        String fullDate = booking.getApptDateString();
        String year = fullDate.substring(0, 4);
        String month = fullDate.substring(5, 7);
        String dayOfMonth = fullDate.substring(8, 10);

        String time = fullDate.substring(11, 16);

//        String formatedTime = toAMPM(time);
//        String onlyTime = formatedTime.substring(0, 5);
//        String amPm = formatedTime.substring(6);

        holder.tvMonthDay.setText(dayOfMonth);
        DateFormat sdf = new SimpleDateFormat("MM");
        try {
            Date date = sdf.parse(month);
            if (lang.equalsIgnoreCase(Constants.ARABIC)){
                holder.tvMonth.setText(new SimpleDateFormat("MMM",new Locale("ar")).format(date) + "-" + year);
            }else {
                holder.tvMonth.setText(new SimpleDateFormat("MMM",new Locale("en")).format(date) + "-" + year);
            }
        } catch (Exception e) {
            holder.tvMonth.setText(month + "-" + year);
        }

        holder.tvTime.setText(time);
//        holder.tvTimeAmPm.setText(amPm);

        holder.tvDoctorName.setText(context.getText(R.string.dr)+" "+booking.getCareProviderDescription()+" "+booking.getFamilyName());
        holder.tvClinic.setText(booking.getSpecialityDescription());


        try {
            if (booking.isPaid()) {
                holder.tv_payment_value.setText(context.getString(R.string.paid));
            } else {
                holder.tv_payment_value.setText(context.getString(R.string.pending));
            }
        } catch (Exception e) {
            holder.tv_payment_value.setText(context.getString(R.string.pending));
        }



        holder.layPayment.setOnClickListener(view -> {
            if (booking.isPaid() == false) {
                appoinmentOnclick.onPay(booking);
            }
        });
        holder.upcomingCheckinButton.setOnClickListener(view -> {
            showCheckInDailo(booking);
        });

        holder.upcomingCheckinText.setOnClickListener(view -> {
            showCheckInDailo(booking);
        });

        holder.lay_doc_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicianResponse.ProviderList providerList = new PhysicianResponse.ProviderList();
                providerList.setProviderCode(booking.getCareProviderCode());
                providerList.setProviderDescription(booking.getCareProviderDescription());


                Intent intent = new Intent(context, PhysicianFullDetailActivity.class);
                intent.putExtra("code", booking.getCareProviderCode());
                intent.putExtra("data", new Gson().toJson(providerList));
                intent.putExtra("specialityCode", booking.getSpecialityCode());
                intent.putExtra("specialityDescription", booking.getSpecialityDescription());
                context.startActivity(intent);
            }
        });


        try {
            if (booking.getSlotBookingId().trim().contains("|")) {
                holder.layPayment.setVisibility(View.VISIBLE);
            } else {
                holder.layPayment.setVisibility(View.INVISIBLE);
                holder.tvClinic.setText(booking.getClinicDescEn());

            }
        } catch (Exception e) {
            holder.layPayment.setVisibility(View.INVISIBLE);
            try {
                if (!booking.getClinicDescEn().isEmpty()){
                    holder.tvClinic.setText(booking.getClinicDescEn());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }


        try {
            if (booking.getPaymentButton().trim().equalsIgnoreCase("FALSE")) {
                holder.layPayment.setVisibility(View.INVISIBLE);
            } else {
                holder.layPayment.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            holder.layPayment.setVisibility(View.INVISIBLE);

        }


        holder.upcomingVideoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (booking.isDoctorActive()) {
                        makeCall(booking, position);
                    } else {
                        messageDailog((Activity) context, context.getString(R.string.doctor_offline));
                    }
                } catch (Exception e) {
                    messageDailog((Activity) context, context.getString(R.string.doctor_offline));
                }

            }
        });
        holder.upcomingVideoCallText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (booking.isDoctorActive()) {
                        makeCall(booking, position);
                    } else {
                        messageDailog((Activity) context, context.getString(R.string.doctor_offline));
                    }
                } catch (Exception e) {
                    messageDailog((Activity) context, context.getString(R.string.doctor_offline));
                }

            }
        });

        List<BookingAppoinment.PromisForm> newPromisFormList = new ArrayList<>();
        try {
            if (booking.getPromisForm() != null) {
                holder.layAssessment.setVisibility(View.VISIBLE);

                int notCompletedPromisForm = 0;
                List<BookingAppoinment.PromisForm> promisFormList = booking.getPromisForm();
                for (int i = 0; i < promisFormList.size(); i++) {
                    BookingAppoinment.PromisForm promisForm = promisFormList.get(i);
                    try {
                        if (promisForm.getAssessmentStatus().trim().equalsIgnoreCase("notCompleted")) {
                            notCompletedPromisForm++;
                            newPromisFormList.add(promisForm);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (notCompletedPromisForm > 0) {
                    holder.layAssessment.setVisibility(View.VISIBLE);
                    holder.tv_assessment_value.setText(notCompletedPromisForm + "");
                } else {
                    holder.layAssessment.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.layAssessment.setVisibility(View.INVISIBLE);

            }
        } catch (Exception e) {
            holder.layAssessment.setVisibility(View.INVISIBLE);
        }

        holder.layAssessment.setOnClickListener(view -> {
            LocalDate todayDate = LocalDate.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formatter = formatter.withLocale(Locale.ENGLISH);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            LocalDate appDate = LocalDate.parse(fullDate, formatter);


            long diffInDays = Duration.between(todayDate.atStartOfDay(), appDate.atStartOfDay()).toDays();
            if (diffInDays > 7) {
                showAlert((Activity) context, context.getResources()
                        .getString(R.string.complete_appointment_assessment_error));
                return;
            }

            BookingAppoinment newBooking = booking;
            newBooking.setPromisForm(newPromisFormList);
            QuestionareActivity.startActivity(context, newBooking);

        });
        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(context, booking.getCareProviderCode()))
                .error(R.drawable.mdoc_placeholder)
                .placeholder(R.drawable.male_img).fit().into(holder.iv_profile);

        if (booking.getBookingStatus() == ARRIVED) {
            holder.upcoming_lay_checkin.setClickable(false);
            holder.upcoming_lay_checkin.setEnabled(false);
            holder.upcomingCheckinText.setClickable(false);
            holder.upcomingCheckinText.setEnabled(false);

            holder.upcoming_lay_checkin.setVisibility(View.INVISIBLE);
            holder.upcomingCheckinText.setVisibility(View.INVISIBLE);
            holder.upcomingCheckinText.setTextColor(context.getResources().getColor(R.color.text_grey_color));
        }

        if (lang.equalsIgnoreCase(Constants.ARABIC)){
            try {
                if (!booking.givenNameAr.isEmpty()){
                    holder.tvDoctorName.setText(context.getText(R.string.dr)+" "+booking.getGivenNameAr()+" "+booking.getFamilyNameAr());
                    holder.tvClinic.setText(booking.getClinicDescAr());
                }else {

                    holder.tvDoctorName.setText(context.getText(R.string.dr)+" "+booking.getArabicProviderDescription());
                    holder.tvClinic.setText(booking.getArabicSpecialityDescription());

                }
                holder.service_title.setText(booking.getArabicServiceDescription());

            } catch (Exception e) {

                holder.tvDoctorName.setText(context.getText(R.string.dr)+" "+booking.getArabicProviderDescription());
                holder.tvClinic.setText(booking.getArabicSpecialityDescription());
            }


        }

//        if (booking.getServiceType().equalsIgnoreCase("Offline") | booking.getServiceType().equalsIgnoreCase("0")) {
//            holder.layPayment.setVisibility(View.INVISIBLE);
//        }
    }

    private void makeCall(BookingAppoinment booking, int position) {
        Log.e("abcd", new Gson().toJson(booking));
        Log.e("abcd", "call click");
//                if (booking.getFamilyName() != null)
//                    onItemClickListener.videoCall(position, context.getString(R.string.dr) + " " + booking.getGivenName() + " " + booking.getFamilyName());
//                else
        String lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        String DrName = null;
        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            DrName = context.getResources().getString(R.string.dr) + " " + booking.getArabicProviderDescription() + " - " + booking.getSpecialityDescription();
        } else {
            DrName = context.getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " - " + booking.getSpecialityDescription();
        }
//        appoinmentOnclick.videoCall(position, DrName);
        //                SharedPreferencesUtils.getInstance(ManageBookingsActivity.this).setValue(Constants.KEY_VIDEO_PHYSICIAN, upcomingList.get(position).getDocCode());
        SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_VIDEO_PHYSICIAN, booking.getCareProviderCode());
//        SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_VIDEO_PHYSICIAN, "DR1403");
        SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
        clickToStartVideoCall(booking.getSlotBookingId(), booking.getApptDateString());


    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(context.getResources().getString(R.string.yes), new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation(LocationManager locationManager,BookingAppoinment booking) {
        if (ActivityCompat.checkSelfPermission(
                context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 112);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,0,this
                    );
           // Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.d("--locationGPS", "getLocation: "+locationGPS);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                String latitude = String.valueOf(lat);
                String longitude = String.valueOf(longi);



                Dialog sucDialog = new Dialog(context); // Context, this, etc.
                sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                sucDialog.setContentView(R.layout.dialog_confirm_arrival);

                TextView yes = sucDialog.findViewById(R.id.tv_yes);
                TextView tv_no = sucDialog.findViewById(R.id.tv_no);
                ImageView iv_back_bottom = sucDialog.findViewById(R.id.iv_back_bottom);

                sucDialog.setCancelable(false);

                yes.setOnClickListener(v -> {
                    sucDialog.dismiss();

                    new MyhttpMethod(context).patientChechIn(booking.getSlotBookingId(), booking.getApptDateString(), latitude,longitude,patientArrivalListner);

                });

                tv_no.setOnClickListener(v -> sucDialog.dismiss());
                iv_back_bottom.setOnClickListener(v -> sucDialog.cancel());

                sucDialog.show();

            } else {
                Toast.makeText(context, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showCheckInDailo(BookingAppoinment booking) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation(locationManager,booking);
        }

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            SharedPreferencesUtils.getInstance(context.getApplication()).setValue(Constants.KEY_LAT, String.valueOf(latitude));
            SharedPreferencesUtils.getInstance(context.getApplication()).setValue(Constants.KEY_LONG, String.valueOf(longitude));

        }

    }


    class Viewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_month_day)
        TextView tvMonthDay;
        @BindView(R.id.tv_month)
        TextView tvMonth;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_time_am_pm)
        TextView tvTimeAmPm;
        @BindView(R.id.tv_doctor_name)
        TextView tvDoctorName;
        @BindView(R.id.tv_clinic_name)
        TextView tvClinic;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.service_title)
        TextView service_title;
        @BindView(R.id.card_income)
        CardView cardIncome;
        @BindView(R.id.status_view)
        View status_view;
        //        @BindView(R.id.tv_consultation)
//        TextView tv_consultation;
//        @BindView(R.id.tv_view_assessment)
//        TextView tv_view_assessment;
//        @BindView(R.id.lay_btn_manage_appointment)
//        LinearLayout lay_btn_manage_appointment;
//        @BindView(R.id.lay_btn_cancel_appoint)
//        LinearLayout lay_btn_cancel_appoint;
/*        @BindView(R.id.layArrive)
        LinearLayout layArrive;*/

        @BindView(R.id.lay_doc_profile)
        RelativeLayout lay_doc_profile;
        @BindView(R.id.iv_profile)
        ImageView iv_profile;
        //        @BindView(R.id.iv_print)
//        ImageView iv_print;
//        @BindView(R.id.tv_btn_checkin)
//        TextView tv_btn_checkin;
//        @BindView(R.id.lay_btn_checkin)
//        LinearLayout lay_btn_checkin;
        @BindView(R.id.iv_more)
        ImageView iv_more;
        @BindView(R.id.tv_payment_value)
        TextView tv_payment_value;
        /*        @BindView(R.id.tv_arrival_value)
                TextView tv_arrival_value;
                @BindView(R.id.tv_arrived_label)
                TextView tv_arrived_label;*/
/*        @BindView(R.id.tv_payment_label)
        TextView tv_payment_label;*/
        @BindView(R.id.tv_assessment_value)
        TextView tv_assessment_value;
        @BindView(R.id.tv_assessment_label)
        TextView tv_assessment_label;
        @BindView(R.id.ic_error_red)
        ImageView ic_error_red;
        @BindView(R.id.view_line)
        View view_line;
        @BindView(R.id.layBottomValues)
        LinearLayout layBottomValues;
        @BindView(R.id.layAssessment)
        LinearLayout layAssessment;
        @BindView(R.id.upcoming_checkin_text)
        TextView upcomingCheckinText;
        @BindView(R.id.upcoming_videoCall_text)
        TextView upcomingVideoCallText;

        @BindView(R.id.upcoming_checkin_button)
        LinearLayout upcomingCheckinButton;
        @BindView(R.id.upcoming_videoCall_button)
        LinearLayout upcomingVideoCallButton;
        @BindView(R.id.layPayment)
        LinearLayout layPayment;
        @BindView(R.id.upcoming_lay_checkin)
        LinearLayout upcoming_lay_checkin;
        @BindView(R.id.new_text)
        TextView new_text;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardIncome.setVisibility(View.VISIBLE);
        }
    }

    public interface OnItemClickListener {
        void onRescheduleBooking(int position);

        void onCancelBooking(int position);

        void onProfileClick(int position);

        //        PIYUSH-24-03-22
        void onGotoConsult(int position, String DrName);

        void videoCall(int position, String DrName);

        void onStartAssessment(int position);

        void onPrint(int position);

        void onCheck(int position);

        void onConfirmArrival(int position);

        void onScanBarCode(int position);

        void onCheckinButtonListener(int position);
    }

    // Pop up list to select gender or language
    private void showListPopUp(ImageView view, Context context, OnItemClickListener onItemClickListener, int position, BookingAppoinment booking) {
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

//        tv_check_in.setVisibility(View.GONE);
//        tv_goto_consult.setVisibility(View.GONE);
//
//        if (booking.getTeleBooking() == "1") {
//            tv_goto_consult.setVisibility(View.VISIBLE);
//        } else {
//            tv_goto_consult.setVisibility(View.GONE);
//        }
//
//
//        if (booking.getPaymentStatus() == "1" && booking.getIsArrived() == "1") {
//            tv_check_in.setVisibility(View.GONE);
//        } else {
//            if (booking.isSelfCheckIn()) {
//                tv_check_in.setVisibility(View.VISIBLE);
//            } else {
//                tv_check_in.setVisibility(View.GONE);
//            }
//        }
//
//        if (booking.getTeleBooking() == "1") {
//            tv_check_in.setVisibility(View.GONE);
//        }
//
//        if (SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
//            tv_check_in.setVisibility(View.GONE);
//        }
//
//        /* if assessment value is greater than 0, then display the start assessment option in the menu items list*/
//        if (booking.getPromisForm() != null) {
//            if (booking.getPromisForm().size() > 0) {
//                tv_start_assessment.setVisibility(View.VISIBLE);
//            } else {
//                tv_start_assessment.setVisibility(View.GONE);
//            }
//        }


        tv_goto_consult.setOnClickListener(view1 -> {
            popupWindow.dismiss();
//            PIYUSH-24-03-22
            if (booking.getSpecialityDescription() != null) {
                String lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                String DrName = null;
                if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                    DrName = context.getResources().getString(R.string.dr) + " " + booking.getArabicProviderDescription() + " " + booking.getSpecialityDescription();
                } else {
                    DrName = context.getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription() + " " + booking.getSpecialityDescription();
                }
                onItemClickListener.onGotoConsult(position, DrName);
            } else {
                String lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                String DrName = null;
                if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                    DrName = context.getResources().getString(R.string.dr) + " " + booking.getArabicProviderDescription();
                } else {
                    DrName = context.getResources().getString(R.string.dr) + " " + booking.getCareProviderDescription();
                }
                onItemClickListener.onGotoConsult(position, DrName);
//            onItemClickListener.onGotoConsult(position, tvDoctorName);
            }
        });

        tv_check_in.setOnClickListener(view12 -> {
            popupWindow.dismiss();

            onItemClickListener.onCheck(position);
        });

        tv_start_assessment.setOnClickListener(view13 -> {
            popupWindow.dismiss();

            onItemClickListener.onStartAssessment(position);
        });

        tv_download.setOnClickListener(view14 -> {
            popupWindow.dismiss();

            onItemClickListener.onPrint(position);
        });

        tv_reschedule.setOnClickListener(view16 -> {
            popupWindow.dismiss();

            onItemClickListener.onRescheduleBooking(position);
        });

        tv_cancel.setOnClickListener(view15 -> {
            popupWindow.dismiss();
            appoinmentOnclick.onCancel(booking);
//            onItemClickListener.onCancelBooking(position);
        });

        tv_bar_code.setOnClickListener(view17 -> {
            popupWindow.dismiss();

            BottomBarcodeDialog mBottomBarcodeDialog =BottomBarcodeDialog.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(list.get(position).getId()));

            mBottomBarcodeDialog.setArguments(bundle);

            if (!mBottomBarcodeDialog.isAdded())
                mBottomBarcodeDialog.show(activity.getSupportFragmentManager(), "DAILOG");

//            onItemClickListener.onScanBarCode(position);
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5//(int) view.getX()
    }


    private void clickToStartVideoCall(String bookingId, String bookingDateTime) {
        try {
            String lang;
            if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
            } else {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            }
            Common.showDialog(context);
            CreateRoomViewModel viewModel = ViewModelProviders.of(activity).get(CreateRoomViewModel.class);
            viewModel.init();

            CreateRoomRequestModel createRoomRequestModel = new CreateRoomRequestModel();
            createRoomRequestModel.setPageNumber(0);
            createRoomRequestModel.setPageSize(0);
            String mrnNumber = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN, null);
            String physician = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_VIDEO_PHYSICIAN, null);
            createRoomRequestModel.setRoomName(mrnNumber + "_" + physician);
            createRoomRequestModel.setUserEmail("xyz@gmail.com");

//            createRoomRequestModel.setBookingId(bookingId);
            createRoomRequestModel.setBookingId(bookingId);

            createRoomRequestModel.setDoctorId(physician);
            createRoomRequestModel.setUserId(mrnNumber);
            createRoomRequestModel.setBookingSlotTime(bookingDateTime);
            createRoomRequestModel.setDeviceType("Android");
            String deviceToken = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_DEVICE_ID, "");
            createRoomRequestModel.setDeviceToken(deviceToken);
            String token = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, null);
            viewModel.CreateRoom(token, lang, SharedPreferencesUtils.getInstance(context).getValue(HOSPITAL_CODE, "IMC"), createRoomRequestModel);
            viewModel.getVolumesResponseLiveData().observe(activity, response -> {
                Log.e("abcd", new Gson().toJson(response));

                if (response != null) {
                    if (!response.getValidBufferRange()) {
                        messageDailog((Activity) context, response.getBufferRangeMessage());
                        return;
                    }

                    if (response.getStatus()) {
                        Common.hideDialog();
                        Dexter.withContext(context).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Dexter.withContext(context).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        SharedPreferencesUtils.getInstance(context).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ACCESS_TOKEN, response.getData().getAccessToken());
                                        SharedPreferencesUtils.getInstance(context).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_BACK_CLASS, "1");
//                                        if(!response.getBookingCompletedStatus()) {
                                        if (response.getValidBufferRange()) {
//                                                Common.hideDialog();
                                            SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_EMERGENCY_CALL, false);
                                            Intent in = new Intent(context, VideoActivity.class);
                                            context.startActivity(in);
                                            activity.finish();
                                        } else {
//                                                Common.hideDialog();
                                            SharedPreferencesUtils.getInstance(context).setValue(Constants.KEY_EMERGENCY_CALL, false);
                                            gotoCheckTiming("Message!", response.getBufferRangeMessage(), 1);
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
                    }
                } else {
                    Common.hideDialog();
                    Toast.makeText(context, "Some thing went wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Common.hideDialog();
            e.printStackTrace();
        }

    }

    void gotoCheckTiming(String title, String message, int E) {
//        PIYUSH 30-03-2022
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (E == 1) {
                    dialog.dismiss();
                    Intent in = new Intent(context, VideoActivity.class);
                    context.startActivity(in);
                    activity.finish();
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
                    Typeface typeface = ResourcesCompat.getFont(context, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }
}
