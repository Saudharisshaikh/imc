package sa.med.imc.myimc.Adapter;

import androidx.room.Room;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.util.List;

/**
 * Medicines list.
 */

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.Viewholder> {
    List<MedicationRespone.MedicationModel> list;
    Context context;
    int pos_select = -1;
    String lang = "";
    OnItemClickListener onItemClickListener;
    ImcDatabase db;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MedicinesAdapter(Context context, List<MedicationRespone.MedicationModel> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "");
        db = Room.databaseBuilder(context, ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_medicine, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {


        if (list.get(position).getDeliveryDate() != null)
            holder.delivery_date.setText(Common.getConvertDate(String.valueOf(list.get(position).getDeliveryDate())));

        if (list.get(position).getDeliveryNum() != null)
            holder.delivery_number.setText(String.valueOf(list.get(position).getDeliveryNum()));

        if (list.get(position).getDescp() != null)
            holder.tv_medicine_name.setText(list.get(position).getDescp());

//        if (list.get(position).getPatientName() != null)
//            holder.tv_medicine_desc.setText(list.get(position).getPatientName());

        if (list.get(position).getNoOfDays() != null)
            holder.tv_number_days.setText(list.get(position).getNoOfDays());

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {

            if (list.get(position).getQty() != null)
                holder.tv_quantity.setText(list.get(position).getQty() + " (" + list.get(position).getUomDescription() + ")");


            if (list.get(position).getStrenght() != null && list.get(position).getStrenghtUom() != null && list.get(position).getFreqDescriptionAr() != null)
                holder.tv_description.setText(list.get(position).getStrenght() + " " + list.get(position).getStrenghtUom().toLowerCase() + ", " + list.get(position).getFreqDescriptionAr());
            else if (list.get(position).getFreqDescriptionAr() != null)
                holder.tv_description.setText(list.get(position).getFreqDescriptionAr());
        } else {
            if (list.get(position).getQty() != null)
                holder.tv_quantity.setText(list.get(position).getQty() + " (" + list.get(position).getUomDescription() + ")");

            if (list.get(position).getStrenght() != null && list.get(position).getStrenghtUom() != null && list.get(position).getFreqDescription() != null)
                holder.tv_description.setText(list.get(position).getStrenght() + " " + list.get(position).getStrenghtUom().toLowerCase() + ", " + list.get(position).getFreqDescription());
            else if (list.get(position).getFreqDescription() != null)
                holder.tv_description.setText(list.get(position).getFreqDescription());
        }

        if (db.medicationReminderDataDao().isReminder(String.valueOf(list.get(position).getDispendingId())) != null)
            holder.iv_reminder.setVisibility(View.VISIBLE);
        else
            holder.iv_reminder.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_medicine_name;

        TextView tv_medicine_desc;

        TextView delivery_date;

        TextView delivery_number;
        TextView tv_quantity;
        TextView tv_number_days, tv_description;
        ImageView iv_reminder;


        Viewholder(View itemView) {
            super(itemView);
            tv_medicine_name = itemView.findViewById(R.id.tv_medicine_name);
            tv_medicine_desc = itemView.findViewById(R.id.tv_medicine_desc);
            delivery_date = itemView.findViewById(R.id.tv_delivery_date);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            delivery_number = itemView.findViewById(R.id.tv_delivery_number);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_number_days = itemView.findViewById(R.id.tv_number_days);
            iv_reminder = itemView.findViewById(R.id.iv_reminder);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
