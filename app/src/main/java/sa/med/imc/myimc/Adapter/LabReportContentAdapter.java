package sa.med.imc.myimc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LaboratoryHistoryModel;

public class LabReportContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LaboratoryHistoryModel.Data> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context ll_context;

    public LabReportContentAdapter(Context ll_context, List<LaboratoryHistoryModel.Data> lstOfChildMedicus) {
        items = lstOfChildMedicus;
        this.ll_context = ll_context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lab_report_content, parent,
                false);
        return new ItemViewHolder(ll_context, view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        LaboratoryHistoryModel.Data item = items.get(pos);
        viewHolder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo, tvPhysicianName, tvVisitDate;
        private Button btnSmartReport;
        private RecyclerView recyclerView_, ibtnSwitch;
        private MaterialCardView cardview_;
        private TextView tvViewReport, tvSmartReportStatus, tv_report_name;
        private TextView tv_date;
        private Context ll_context;

        // region Constructors
        public ItemViewHolder(Context ll_context, View view) {
            super(view);
            this.ll_context = ll_context;
            tvViewReport = view.findViewById(R.id.tv_view_report);
            tvSmartReportStatus = view.findViewById(R.id.tv_smart_report_status);
            tv_report_name = view.findViewById(R.id.tv_report_name);
            tv_date = view.findViewById(R.id.tv_date);
        }

        // endregion
        public void bind(final LaboratoryHistoryModel.Data model, final OnItemClickListener listener) {

            if (model.getLongName() != null) {
                tv_report_name.setText(model.getLongName());
            }
            if (model.getAptTime() != null) tv_date.setText(model.getAptTime());

            if (model.getSmartReport() == 0) {
                tvSmartReportStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
            }
            if (model.getSmartReport() == 1) {
                tvSmartReportStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
            }

            if (SharedPreferencesUtils.getInstance(ll_context).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
                tvSmartReportStatus.setVisibility(View.INVISIBLE);
            }

            tvViewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition(), model);
                    }

                }
            });
        }
    }

    // inner interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, LaboratoryHistoryModel.Data model);

//        void onItemMoreClick(int position, LabReportsMedicus model);
    }

    // set listeners
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}