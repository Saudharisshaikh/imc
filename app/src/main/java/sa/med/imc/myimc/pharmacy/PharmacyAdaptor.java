package sa.med.imc.myimc.pharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sa.med.imc.myimc.Base.BaseAdaptor;
import sa.med.imc.myimc.databinding.ItemPharmacyBinding;
import sa.med.imc.myimc.pharmacy.model.PharmacyItem;
import sa.med.imc.myimc.pharmacy.model.PharmacyResponse;

public class PharmacyAdaptor extends BaseAdaptor<PharmacyItem, PharmacyAdaptor.PharmacyEvent, PharmacyAdaptor.Holder> {

    public PharmacyAdaptor(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PharmacyAdaptor.Holder(ItemPharmacyBinding.inflate(LayoutInflater.from(getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ItemPharmacyBinding binding = holder.binding;
        PharmacyItem item = getDataList().get(position);
        binding.tvMedicineDesc.setText(item.getProductDesc());
        binding.tvNumberDays.setText(item.getNoOfDays());
        binding.tvDescription.setText(item.getInStr());

    }

    public static class Holder extends RecyclerView.ViewHolder{
        public ItemPharmacyBinding binding;

        public Holder(@NonNull ItemPharmacyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public enum PharmacyEvent {
        ITEM_CLICK
    }


}
