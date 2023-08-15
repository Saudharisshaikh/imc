package sa.med.imc.myimc.MedicineDetail.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sa.med.imc.myimc.MedicineDetail.MedicineDetailActivity;
import sa.med.imc.myimc.MedicineDetail.model.MedicineModel;
import sa.med.imc.myimc.MedicineDetail.model.MedicineResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    Context context;
    List<MedicineModel> medicineModelList;
    String lang;

    public MedicineAdapter(Context context, List<MedicineModel> medicineModelList) {
        this.context = context;
        this.medicineModelList = medicineModelList;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_medicine, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MedicineModel medicineModel = medicineModelList.get(position);

        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            holder.tv_medicine_desc.setText(medicineModel.getEnglish_instruction());

        }else {
            holder.tv_medicine_desc.setText(medicineModel.getArabic_instruction());

        }

            holder.tv_medicine_name.setText(medicineModel.getMedication_description());
        holder.tv_number_days.setText(medicineModel.getDuration());
        holder.tv_description.setText(medicineModel.getRoute());
        holder.tv_quantity.setText(medicineModel.getQuantity());
        holder.prescription_number.setText("Prescription no: "+medicineModel.getPrescription_number());
        holder.order_status.setText(medicineModel.getOrder_status());

        holder.itemView.setOnClickListener(view -> {
            MedicineDetailActivity.startActivity( context, medicineModel);
        });

    }

    @Override
    public int getItemCount() {
        return medicineModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_medicine_name;

        TextView tv_medicine_desc;

        TextView delivery_date;
        TextView prescription_number;
        TextView order_status;

        TextView delivery_number;
        TextView tv_quantity;
        TextView tv_number_days, tv_description;
        ImageView iv_reminder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_medicine_name = itemView.findViewById(R.id.tv_medicine_name);
            tv_medicine_desc = itemView.findViewById(R.id.tv_medicine_desc);
            delivery_date = itemView.findViewById(R.id.tv_delivery_date);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            delivery_number = itemView.findViewById(R.id.tv_delivery_number);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_number_days = itemView.findViewById(R.id.tv_number_days);
            iv_reminder = itemView.findViewById(R.id.iv_reminder);
            prescription_number = itemView.findViewById(R.id.prescription_number);
            order_status = itemView.findViewById(R.id.order_status);

        }
    }
}
