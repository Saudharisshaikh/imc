package sa.med.imc.myimc.healthcare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sa.med.imc.myimc.Base.BaseAdaptor;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Utils.ColorUtils;
import sa.med.imc.myimc.databinding.ItemHealthcareBinding;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;

public class HealthCareAdaptor extends BaseAdaptor<HealthCareItem,HealthCareAdaptor.Action, HealthCareAdaptor.Holder> {
    private int selectionsCount = 0;
    public static final String RECYCLER_VIEW_VISIBILITY = "view";
    RecyclerViewListener<HealthCareItem, HealthCareAdaptor.Action> listener;

    public HealthCareAdaptor(Context context) {
        super(context);
    }

    public HealthCareAdaptor(Context context, RecyclerViewListener<HealthCareItem, HealthCareAdaptor.Action> listener) {
        super(context, listener);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemHealthcareBinding.inflate(LayoutInflater.from(getContext()),parent,false));
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        super.onViewRecycled(holder);
        holder.binding.setCheckBoxSelection(false);
    }

    @Override
    public long getItemId(int position) {
        if (position==RecyclerView.NO_POSITION){
            return position;
        }
        return getItem(position).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (holder.getAdapterPosition()==RecyclerView.NO_POSITION){
            return;
        }
        HealthCareItem model = getItem(position);
        ItemHealthcareBinding binding = holder.binding;
        //binding.setListVisibility(model.isChecked());
        binding.setCheckBoxSelection(model.isChecked());
        binding.recyclerView.post(()->{
            if (holder.getAdapterPosition()==RecyclerView.NO_POSITION){
                return;
            }
            HealthCareItem innerModel = getItem(holder.getAdapterPosition());
            holder.adaptor.setAndClearData(innerModel.getLinkedHHCFormQuestions());
            holder.adaptor.notifyDataSetChanged();
        });
        if (model.getColor()==0){
            model.setColor(ColorUtils.getColorWithAlpha());
        }
       // binding.recyclerView.setBackgroundColor(model.getColor());
        if (SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_LANGUAGE,"").equals("ar")){
            binding.setText(model.getQuestionAr());
            binding.setShowToolTip(model.getTooltipAr()!=null);
        }
        else{
            binding.setText(model.getQuestionEn());
            binding.setShowToolTip(model.getTooltipEn()!=null);
        }
        binding.cbSelection.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (holder.getAdapterPosition()==RecyclerView.NO_POSITION){
                return;
            }
            HealthCareItem innerModel = getItem(holder.getAdapterPosition());
            if (innerModel.getParentId()==null){
                if (isChecked){
                    selectionsCount++;
                }
                else{
                    selectionsCount--;
                }
            }
            innerModel.setChecked(isChecked);
            binding.setListVisibility(isChecked);
        });
        binding.ivToolTip.setOnClickListener(v->{
            listener.onClick(Action.TOOL_TIP,getItem(holder.getAdapterPosition()));
        });
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size()>0){
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(RECYCLER_VIEW_VISIBILITY)){
                HealthCareItem model = getItem(position);
                ItemHealthcareBinding binding = holder.binding;
                binding.setListVisibility(model.isChecked());
            }
        }
        else{
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ItemHealthcareBinding binding;
        public HealthCareAdaptor adaptor;


        public Holder(@NonNull ItemHealthcareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setListVisibility(false);
            this.adaptor = new HealthCareAdaptor(binding.getRoot().getContext());
            this.adaptor.setHasStableIds(true);
            this.binding.recyclerView.setAdapter(this.adaptor);
            this.binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),RecyclerView.VERTICAL));
        }
    }

    public int getSelectionsCount() {
        return selectionsCount;
    }

    public enum Action{
        TOOL_TIP
    }
}
