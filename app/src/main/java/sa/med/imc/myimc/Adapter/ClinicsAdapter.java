package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

/**
 * Clinics list.
 */

public class
ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.Viewholder> {
    List<ClinicResponse.SpecialityList> list;
    List<ClinicResponse.SpecialityList> All_list;

    Context context;
    int pos_select = -1;
    String lang = Constants.ENGLISH;

    public ClinicsAdapter(Context context, List<ClinicResponse.SpecialityList> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

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
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_clinic, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder,  int position) {
        position=holder.getAdapterPosition();

        ClinicResponse.SpecialityList model = list.get(position);

        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            holder.tv_clinic_name.setText(model.getSpecialityDescription());

        }else {
            holder.tv_clinic_name.setText(model.getArabicSpecialityDescription());

        }


        /*if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (model.getDescAr() != null)
                holder.tv_clinic_name.setText(model.getDescAr());

        } else {
            if (model.getDescEn() != null)
                holder.tv_clinic_name.setText(model.getDescEn());
        }*/

        if (model.getSpecialityCode() != null) {
            String url =new UrlWithURLDecoder().getClinic_profile(context,model.getSpecialityCode());
//            Log.e("clinic  ", "==== " + url);
            Picasso.get().load(url).error(R.drawable.clinic_img).fit().into(holder.iv_clinic_pic);

//            networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).
        }

//        if (model.getId() != null) {
//            switch (model.getId()) {
//                case "C-OBG":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.ob_gyne_clinic);
//                    break;
//
//                case "C-BBH":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.bad_breath);
//                    break;
//
//                case "C-CAR":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.cardiology_clinic);
//                    break;
//
//                case "C-END":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.endocrine);
//                    break;
//
//                case "C-RHE":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.rheumatology);
//                    break;
//
//                case "C-SUR":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.surgical);
//                    break;
//
//                case "C-WC":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.wound_care);
//                    break;
//
//                case "C-ENT":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.ent_clinic);
//                    break;
//
//                case "C-FAM":
//                    holder.iv_clinic_pic.setImageResource(R.drawable.family_cli);
//                    break;
//
//                default:
//                    holder.iv_clinic_pic.setImageResource(R.drawable.clinic_img);
//                    break;
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clinic_name)
        TextView tv_clinic_name;
        @BindView(R.id.tv_clinic_address)
        TextView tv_clinic_address;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;
        @BindView(R.id.iv_clinic_pic)
        ImageView iv_clinic_pic;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
