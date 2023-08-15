package sa.med.imc.myimc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Interfaces.OnAdapterItemClickListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.view.LMSRecordsFragment;
import sa.med.imc.myimc.Utils.Common;

public class LabReportHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    List<LabReportsParentMedicus> list;
    private List<LabReportsParentMedicus> All_list;
    OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;

    public LabReportHeaderAdapter(Context context, List<LabReportsParentMedicus> reports) {
        list = reports;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lab_report_header, parent, false);
        return new ItemViewHolder(context, view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        LabReportsParentMedicus item = list.get(pos);
        viewHolder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setAllData(List<LabReportsParentMedicus> list) {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);
    }

    public void setCurrentlyAttachedFragment(LMSRecordsFragment fragment) {
        onAdapterItemClickListener = (OnAdapterItemClickListener) fragment;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo, tvPhysicianName, tvVisitDate;
        private Button btnSmartReport;
        private LinearLayout ll_content;
        private RecyclerView recyclerView_;
        private ImageButton ibtnSwitch;
        private MaterialCardView cardview_;
        private Context context;

        // region Constructors
        public ItemViewHolder(Context context, View view) {
            super(view);

            this.context = context;

            tvOrderNo = view.findViewById(R.id.tv_order_no);
            btnSmartReport = view.findViewById(R.id.btn_smart_report);
            tvPhysicianName = view.findViewById(R.id.tv_physician_name_);
            tvVisitDate = view.findViewById(R.id.tv_visit_date_);
            recyclerView_ = view.findViewById(R.id.recyclerView_);
            ibtnSwitch = view.findViewById(R.id.ibtn_switch);
            ll_content = view.findViewById(R.id.ll_content);
            cardview_ = view.findViewById(R.id.cardview);


        }

        // endregion
        public void bind(final LabReportsParentMedicus model, final OnItemClickListener listener) {

            ibtnSwitch.setOnClickListener(v -> {
                if (ll_content.getVisibility() == View.GONE) {
                    ll_content.setVisibility(View.VISIBLE);
                    ibtnSwitch.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    ll_content.setVisibility(View.GONE);
                    ibtnSwitch.setImageResource(R.drawable.ic_arrow_down);
                }

            });

            if (model.getLstOfChildMedicus().get(0).getHosp_ID().equals("4")) {
                cardview_.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ed_blue));
            }

            if (model.getOrderNum() != null) {
                tvOrderNo.setText(ll_content.getResources().getString(R.string.order_no) + model.getOrderNum()); // R.string.order_no + model.getOrderNum()
            }
            if (model.getGivenName() != null) {
                tvPhysicianName.setText(model.getGivenName());
            }
            if (model.getAptTime() != null) {
                tvVisitDate.setText(Common.getConvertServerDateTimeA(model.getAptTime()));
//                tvVisitDate.setText(Common.getConvertDateTime(model.getAptTime()));
            }

            for (LabReportsMedicus person : model.getLstOfChildMedicus()) {
                if (person.getSmartReport() == 1) {
                    btnSmartReport.setVisibility(View.VISIBLE);
                    break;
                }
            }
            if (SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
                btnSmartReport.setVisibility(View.GONE);
            }

//            LabReportContentAdapter labReportAdapter = new LabReportContentAdapter(context, model.getLstOfChildMedicus());
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
//            recyclerView_.setLayoutManager(linearLayoutManager);
//            recyclerView_.setAdapter(labReportAdapter);
//            labReportAdapter.setOnItemClickListener(new LabReportContentAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int innerPosition, LabReportsMedicus model) {
//                    if (listener != null) {
//                        listener.onItemClick(getAdapterPosition(), innerPosition, model);
//                    }
//
//                }
//
//                @Override
//                public void onItemMoreClick(int position, LabReportsMedicus model) {
//
//                }
//            });
////
//            btnSmartReport.setOnClickListener(v -> {
//                if (listener != null)
//                    listener.onItemMoreClick(model, getAdapterPosition());
//            });
//
////            btnSmartReport.setOnClickListener(v -> onAdapterItemClickListener.onItemClick(getAdapterPosition(), btnSmartReport, list.get(getAdapterPosition()).getLstOfChildMedicus().get(getAdapterPosition()).getAccessionNum(), 0));


        }
    }

    // inner interfaces
    public interface OnItemClickListener {
        void onItemClick(int outerPosition, int innerPosition, LabReportsMedicus model);

        void onItemMoreClick(LabReportsParentMedicus model, int item);
    }

    // set listeners
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
