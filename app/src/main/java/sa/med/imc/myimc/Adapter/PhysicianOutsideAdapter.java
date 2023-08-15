package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.PhysicianModel;
import sa.med.imc.myimc.R;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Physician from CMS list.
 */

public class PhysicianOutsideAdapter extends RecyclerView.Adapter<PhysicianOutsideAdapter.Viewholder> {
    List<PhysicianModel> list;

    Context context;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;
    String lang = Constants.ENGLISH;

    public PhysicianOutsideAdapter(Context context, List<PhysicianModel> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_physician_out, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        PhysicianModel physician = list.get(position);
//        holder.tvReviews.setText(context.getString(R.string.reviews, "1"));
        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (physician.getGivenNameAr() != null)
                if (physician.getFamilyNameAr() != null)
                    holder.tv_physician_name.setText(physician.getTitleAr() + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr());
                else
                    holder.tv_physician_name.setText(physician.getTitleAr() + " " + physician.getGivenNameAr());
            else
                holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");


            if (physician.getDesignation() != null)
                holder.tv_physician_speciality.setText(physician.getDesignation());
            else {
                holder.tv_physician_speciality.setText("");
            }

            if (physician.getSpecialitiesTxtAr() != null) {
                holder.tv_clinic_service.setText(physician.getSpecialitiesTxtAr());
            } else {
                holder.tv_clinic_service.setText("");

            }

        } else {
            if (physician.getGivenName() != null)
                if (physician.getFamilyName() != null)
                    holder.tv_physician_name.setText(physician.getTitle() + " " + physician.getGivenName() + " " + physician.getFamilyName());
                else
                    holder.tv_physician_name.setText(physician.getTitle() + " " + physician.getGivenName());
            else
                holder.tv_physician_name.setText(context.getString(R.string.dr) + " ");

            if (physician.getDesignation() != null)
                holder.tv_physician_speciality.setText(physician.getDesignation());
            else {
                holder.tv_physician_speciality.setText("");
            }

            if (physician.getSpecialitiesTxt() != null) {
                holder.tv_clinic_service.setText(physician.getSpecialitiesTxt());
            } else {
                holder.tv_clinic_service.setText("");

            }

        }

//        if (physician.getDocRating() != null) {
//            holder.rating.setRating(Float.parseFloat(physician.getDocRating()));
//        }

        if (physician.getImgUrl() != null)
            if (physician.getImgUrl().length() != 0) {
//                String url = WebUrl.IMAGE_URL + physician.getDocImg() + ".xhtml?In=default";
                Picasso.get().load(physician.getImgUrl())
                        .error(R.drawable.mdoc_placeholder)
                        .placeholder(R.drawable.male_img).into(holder.iv_physician_pic);
//                networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).
            }
        holder.tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProfile(position);
            }
        });

        holder.tv_book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBookAppointment(position);
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

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onViewProfile(int position);

        void onBookAppointment(int position);
    }
}
