package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.medprescription.model.PrescriptionResponse;

/**
 * Prescription Adapter list.
 */

public class PerscriptionAdapter extends RecyclerView.Adapter<PerscriptionAdapter.Viewholder> {
    List<PrescriptionResponse.Prescription> list;
    Context context;
    String lang = "";
    OnItemClickListner itemClickListner;

    public PerscriptionAdapter(Context context, List<PrescriptionResponse.Prescription> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "");

    }

    public void setOnClickListener(OnItemClickListner onClickListener) {
        this.itemClickListner = onClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_perscription, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        PrescriptionResponse.Prescription prescription = list.get(position);

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (prescription.getDocNameAr() != null)
                holder.tv_doctor_name.setText(prescription.getDocNameAr());
            else
                holder.tv_doctor_name.setText(prescription.getDocName());
        } else
            holder.tv_doctor_name.setText(prescription.getDocName());
        holder.tv_order.setText(context.getString(R.string.order_value, String.valueOf(prescription.getPrecriptionNo())));
        holder.tv_date.setText(Common.completedAssessmentDateTime(prescription.getOrderDate()));
        holder.tv_view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListner.clickPrescription(holder.getAdapterPosition());
            }
        });
        holder.tv_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListner.clickPharmacy(list.get(holder.getLayoutPosition()));
            }
        });
        if (SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            holder.tv_Buy.setVisibility(View.GONE);
        }


        holder.tv_invoice.setVisibility(prescription.getInvoiceCount()!=null && prescription.getInvoiceCount() > 0 ? View.VISIBLE:View.GONE);

        holder.tv_Buy.setOnClickListener(view -> itemClickListner.clickPharmacy(list.get(holder.getLayoutPosition())));

        holder.tv_invoice.setOnClickListener(view -> itemClickListner.onDownload(list.get(holder.getLayoutPosition())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_doctor_name)
        TextView tv_doctor_name;
        @BindView(R.id.tv_order)
        TextView tv_order;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_view_report)
        TextView tv_view_report;
        @BindView(R.id.tv_pharmacy)
        TextView tv_Buy;
        @BindView(R.id.tv_invoice)
        TextView tv_invoice;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListner {
        void clickPrescription(int position);
        void clickPharmacy(PrescriptionResponse.Prescription prescription);
        void onDownload(PrescriptionResponse.Prescription prescription);
    }
}
