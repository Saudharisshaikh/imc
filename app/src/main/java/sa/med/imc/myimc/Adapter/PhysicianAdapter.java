package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DoctorFullDeatilsModel;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.FullDoctorDetailListner;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.DailogAlert;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

/**
 * Physician list.
 */

public class PhysicianAdapter extends RecyclerView.Adapter<PhysicianAdapter.Viewholder> {
    List<PhysicianResponse.ProviderList> list;
    List<PhysicianResponse.ProviderList> All_list;
    List<AllDoctorListModel.DoctorListBasedOnSpeciality> doctorListBasedOnSpecialities2;


    Context context;
    String yes = "0";
    String specialityDescription = null;
    OnItemClickListener onItemClickListener;
    String lang = Constants.ENGLISH;
    DailogAlert dailogAlert;

    public PhysicianAdapter(Context context, List<PhysicianResponse.ProviderList> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
    }

    public PhysicianAdapter(Context context, List<PhysicianResponse.ProviderList> list, String yes) {
        this.context = context;
        this.list = list;
        this.yes = "1";
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
    }

    public PhysicianAdapter(Context context, List<PhysicianResponse.ProviderList> list, String yes, String specialityDescription, DailogAlert dailogAlert1) {
        this.context = context;
        this.list = list;
        this.specialityDescription = specialityDescription;
        this.dailogAlert = dailogAlert1;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
    }

    public PhysicianAdapter(Context context, List<PhysicianResponse.ProviderList> list,
                            String yes, String specialityDescription,
                            DailogAlert dailogAlert1, List<AllDoctorListModel.DoctorListBasedOnSpeciality> doctorListBasedOnSpecialities) {
        this.context = context;
        this.list = list;
        this.specialityDescription = specialityDescription;
        this.dailogAlert = dailogAlert1;
        this.doctorListBasedOnSpecialities2 = doctorListBasedOnSpecialities;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setAllData() {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);

    }


    public void clearSearchData() {
        list.clear();
        list.addAll(All_list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_physician, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        PhysicianResponse.ProviderList physician = list.get(position);
//        if (physician.isPatientPortalDoctor()){
//            holder.cardDoctor.setVisibility(View.VISIBLE);
//        }else {
//            holder.cardDoctor.setVisibility(View.GONE);
//        }

        if (yes.equals("1") && physician.getProviderCode() != null && physician.getProviderDescription() == null)
            holder.progress_bar.setVisibility(View.VISIBLE);
        else
            holder.progress_bar.setVisibility(View.GONE);
        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            holder.tv_physician_name.setText(context.getText(R.string.dr) + " " + physician.getProviderDescription());

        } else {
            holder.tv_physician_name.setText(context.getText(R.string.dr) + " " + physician.getArabicProviderDescription());

        }

        holder.tv_physician_speciality.setText(specialityDescription);

        try {
            if (specialityDescription.trim().isEmpty()) {
                if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
                    holder.tv_physician_speciality.setText(doctorListBasedOnSpecialities2.get(position).specialityDescription);

                } else {
                    holder.tv_physician_speciality.setText(doctorListBasedOnSpecialities2.get(position).arabicSpecialityDescription);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        holder.tv_clinic_service.setText(specialityDescription);
//        PIYUSH TRACKER
        /*if (physician.getDate() != null && physician.getTime() != null) {
            holder.tv_next_available_time.setVisibility(View.VISIBLE);
            holder.progress_bar.setVisibility(View.GONE);

            String mystring = context.getString(R.string.next_available_time) + "\n" + Common.parseShortDate(physician.getDate()) + ", " + Common.parseShortTime(physician.getTime());
//            SpannableString content = new SpannableString(mystring);
//            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
            holder.tv_next_available_time.setText(mystring);

//            holder.tv_next_available_time.setText(context.getString(R.string.next_available_time, physician.getDate() + "/" + physician.getTime()));

        } else {
            holder.tv_next_available_time.setVisibility(View.GONE);
        }*/
//        if (BuildConfig.DEBUG) {

//        PIYUSH TRACKER
        /*if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (physician.getGivenNameAr() != null)
                if (physician.getFamilyNameAr() != null)
                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr());
                else
                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenNameAr());
            else
                holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");

            if (physician.getSpecialitiesTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxtAr());
                String trimmed = spannableString.toString().trim();
                holder.tv_physician_speciality.setText(trimmed);
            } else
                holder.tv_physician_speciality.setText("");

            if (physician.getClincNameAr() != null)
                holder.tv_clinic_service.setText(physician.getClincNameAr());
            else
                holder.tv_clinic_service.setText("");
        }
        else {
            if (physician.getGivenName() != null)
                if (physician.getFamilyName() != null)
                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenName() + " " + physician.getFamilyName());
                else
                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenName());
            else
                holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");

            if (physician.getSpecialitiesTxt() != null) {
                Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxt());
                String trimmed = spannableString.toString().trim();
                holder.tv_physician_speciality.setText(trimmed);
            } else
                holder.tv_physician_speciality.setText("");

            if (physician.getClinicNameEn() != null)
                holder.tv_clinic_service.setText(physician.getClinicNameEn());
            else
                holder.tv_clinic_service.setText("");
        }*/

//        }

//        else {
//            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
//                if (physician.getGivenNameAr() != null)
//                    if (physician.getFamilyNameAr() != null)
//                        holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr());
//                    else
//                        holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenNameAr());
//                else
//                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");
//
//                if (physician.getService() != null)
//                    if (physician.getService().getDescAr() != null)
//                        holder.tv_physician_speciality.setText(physician.getService().getDescAr());
//                    else
//                        holder.tv_physician_speciality.setText("");
//                else
//                    holder.tv_physician_speciality.setText("");
//
//
//                if (physician.getClinicNameEn() != null) {
//                    if (physician.getDeptNameEn() != null)
//                        holder.tv_clinic_service.setText(physician.getClinicNameEn());
//                    else
//                        holder.tv_clinic_service.setText(physician.getClinicNameEn());
//
//                } else if (physician.getDeptNameEn() != null) {
//                    holder.tv_clinic_service.setText(physician.getDeptNameEn());
//                } else {
//                    holder.tv_clinic_service.setText("");
//                }
//
//            } else {
//                if (physician.getGivenName() != null)
//                    if (physician.getFamilyName() != null)
//                        holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenName() + " " + physician.getFamilyName());
//                    else
//                        holder.tv_physician_name.setText(context.getString(R.string.dr) + " " + physician.getGivenName());
//                else
//                    holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");
//
//
//                if (physician.getService() != null)
//                    if (physician.getService().getDesc() != null)
//                        holder.tv_physician_speciality.setText(physician.getService().getDesc());
//                    else
//                        holder.tv_physician_speciality.setText("");
//
//                else
//                    holder.tv_physician_speciality.setText("");
//
//
//                if (physician.getClinicNameEn() != null) {
//                    if (physician.getDeptNameEn() != null)
//                        holder.tv_clinic_service.setText(physician.getClinicNameEn() + " | " + physician.getDeptNameEn());
//                    else
//                        holder.tv_clinic_service.setText(physician.getClinicNameEn());
//
//                } else if (physician.getDeptNameEn() != null) {
//                    holder.tv_clinic_service.setText(physician.getDeptNameEn());
//                } else {
//                    holder.tv_clinic_service.setText("");
//                }
//
//            }
//        }

//        PIYUSH TRACKER
        /*if (physician.getDocRating() != null) {
            holder.rating.setRating(Float.parseFloat(physician.getDocRating()));
        }*/


//        PIYUSH TRACKER
        /*if (physician.getDocImg() != null)
            if (physician.getDocImg().length() != 0 && SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL,0)==1) {
                String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + physician.getDocImg();
                Picasso.get().load(url)
                        .error(R.drawable.mdoc_placeholder)
                        .placeholder(R.drawable.male_img).fit().into(holder.iv_physician_pic);
            }else if (physician.getDocImg().length() != 0 && SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL,0)==4){
                String url = BuildConfig.IMAGE_UPLOAD_DOC_URL_FC + physician.getDocImg();
                Picasso.get().load(url)
                        .error(R.drawable.mdoc_placeholder)
                        .placeholder(R.drawable.male_img).fit().into(holder.iv_physician_pic);
            }*/
        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(context, physician.getProviderCode()))
                .error(R.drawable.mdoc_placeholder)
                .placeholder(R.drawable.male_img).fit().into(holder.iv_physician_pic);

        holder.tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProfile(position);
            }
        });
        holder.iv_physician_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProfile(position);
            }
        });

        holder.tv_book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (physician.isPatientPortalDoctor()) {
                    onItemClickListener.onBookAppointment(position);
                } else {
                    dailogAlert.dailog("Physician is unavailable for online booking");
                }
            }
        });

        holder.tv_next_available_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBookNextAvailable(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_physician_name)
        TextView tv_physician_name;
        @BindView(R.id.tv_physician_speciality)
        TextView tv_physician_speciality;
        @BindView(R.id.tv_physician_address)
        TextView tv_physician_address;
        @BindView(R.id.tv_physician_distance)
        TextView tv_physician_distance;

        @BindView(R.id.tv_view_profile)
        TextView tv_view_profile;
        @BindView(R.id.tv_book_appointment)
        TextView tv_book_appointment;

        @BindView(R.id.lay_item)
        RelativeLayout lay_item;
        @BindView(R.id.iv_physician_pic)
        ImageView iv_physician_pic;
        @BindView(R.id.tv_clinic_service)
        TextView tv_clinic_service;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.tv_next_available_time)
        TextView tv_next_available_time;

        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;

        @BindView(R.id.special_instructions)
        TextView special_instructions;

        @BindView(R.id.card_doctor)
        CardView cardDoctor;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onViewProfile(int position);

        void onBookAppointment(int position);

        void onBookNextAvailable(int position);
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }
}
