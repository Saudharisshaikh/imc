package sa.med.imc.myimc.medprescription.invoice;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Base.BaseAdaptor;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.TextViewUtils;
import sa.med.imc.myimc.databinding.ItemPrescriptionInvoiceBinding;
import sa.med.imc.myimc.healthcare.HealthCareAdaptor;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;
import sa.med.imc.myimc.medprescription.invoice.model.PrescriptiontInvoiceItem;

public class PrescriptionInvoiceAdaptor extends BaseAdaptor<PrescriptiontInvoiceItem, PrescriptionInvoiceAdaptor.ActionType, PrescriptionInvoiceAdaptor.ViewHolder> {
    RecyclerViewListener<PrescriptiontInvoiceItem, ActionType> listener;
    public PrescriptionInvoiceAdaptor(Context context, RecyclerViewListener<PrescriptiontInvoiceItem, ActionType> listener) {
        super(context, listener);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPrescriptionInvoiceBinding.inflate(LayoutInflater.from(getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPrescriptionInvoiceBinding binding = holder.binding;
        PrescriptiontInvoiceItem item = getItem(position);
        CharSequence orderId = getColouredOrderText(item);
        binding.setOrderId(orderId);
        if (item.getOrderDate() != null) {
            CharSequence date = getColouredDateText(item);
            binding.setDate(date);
        }
    }

    private CharSequence getColouredOrderText(PrescriptiontInvoiceItem item) {
        android.util.Pair<String,Integer> label = new Pair<>(getContext().getResources().getString(R.string.txt_orderId) + " : ",
                getContext().getResources().getColor(R.color.primary_color_dark) );
        android.util.Pair<String,Integer> text = new Pair<>(String.valueOf(item.getId()),
                getContext().getResources().getColor(R.color.black));
        List<Pair<String,Integer>> list= new ArrayList<>();
        list.add(label);
        list.add(text);
    return TextViewUtils.colorText(list);
    }

    private CharSequence getColouredDateText(PrescriptiontInvoiceItem item) {
        android.util.Pair<String,Integer> label = new Pair<>(getContext().getResources().getString(R.string.txt_placed_on) + " : ",
                getContext().getResources().getColor(R.color.primary_color_dark) );
        android.util.Pair<String,Integer> text = new Pair<>(Common.convertTtime(item.getOrderDate()),
                getContext().getResources().getColor(R.color.black));
        List<Pair<String,Integer>> list= new ArrayList<>();
        list.add(label);
        list.add(text);
        return TextViewUtils.colorText(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemPrescriptionInvoiceBinding binding;

        public ViewHolder(ItemPrescriptionInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                listener.onClick(ActionType.DOWNLOAD,getItem(getAdapterPosition()));
            });
        }
    }

    public enum ActionType {
        DOWNLOAD
    }
}
