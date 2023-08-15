package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

/**
 * No show Booking list adapter.
 */

public class NoShowBookingAdapter extends RecyclerView.Adapter<NoShowBookingAdapter.Viewholder> {
    List<BookingAppoinment> list;
    Context context;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;
    String lang;

    public NoShowBookingAdapter(Context context, List<BookingAppoinment> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_no_show_booking, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        BookingAppoinment booking = list.get(position);
        //2022-07-01 10:00:00
        holder.service_title.setText(booking.getServiceDescription());


        String fullDate=booking.getApptDateString();
        String year=fullDate.substring(0,4);
        String month=fullDate.substring(5,7);
        String dayOfMonth=fullDate.substring(8,10);

        String time=fullDate.substring(11,16);

//        String formatedTime=toAMPM(time);
//        String onlyTime=formatedTime.substring(0,5);
//        String amPm=formatedTime.substring(6);

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

        holder.lay_doc_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhysicianResponse.ProviderList providerList=new PhysicianResponse.ProviderList();
                providerList.setProviderCode(booking.getCareProviderCode());
                providerList.setProviderDescription(booking.getCareProviderDescription());


                Intent intent = new Intent(context, PhysicianFullDetailActivity.class);
                intent.putExtra("code", booking.getCareProviderCode());
                intent.putExtra("data",new Gson().toJson(providerList));
                intent.putExtra("specialityCode", booking.getSpecialityCode());
                intent.putExtra("specialityDescription", booking.getSpecialityDescription());
                context.startActivity(intent);
            }
        });
        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(context, booking.getCareProviderCode()))
                .error(R.drawable.mdoc_placeholder)
                .placeholder(R.drawable.male_img).fit().into(holder.iv_profile);


        try {
            if (booking.getSlotBookingId().trim().contains("|")) {

            } else {
                holder.tvClinic.setText(booking.getClinicDescEn());

            }
        } catch (Exception e) {
            try {
                if (!booking.getClinicDescEn().isEmpty()){
                    holder.tvClinic.setText(booking.getClinicDescEn());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

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

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    String toAMPM(String s){
        String formattedTime=s;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime time = LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
            String temp=time.format(DateTimeFormatter.ofPattern("hh:mm a"));
            formattedTime =temp;
        }
        return formattedTime;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.lay_btn_visit_detail)
        LinearLayout lay_btn_visit_detail;

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
        @BindView(R.id.lay_doc_profile)
        RelativeLayout lay_doc_profile;
        @BindView(R.id.iv_profile)
        ImageView iv_profile;
        @BindView(R.id.service_title)
        TextView service_title;
        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onVisitDetail(int position);

        void onProfileClick(int position);
    }
}
