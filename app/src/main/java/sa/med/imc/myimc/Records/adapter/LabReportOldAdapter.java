package sa.med.imc.myimc.Records.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import sa.med.imc.myimc.Adapter.LabReportContentAdapter;
import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.LaboratoryHistoryModel;
import sa.med.imc.myimc.ViewReportActivity;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;
import sa.med.imc.myimc.globle.activity.SmartReportPdfActivity;

public class LabReportOldAdapter extends RecyclerView.Adapter<LabReportOldAdapter.MyViewHolder> {

    Activity context;
    List<LaboratoryHistoryModel> laboratoryHistoryModelList;
    String lang = "";

    public LabReportOldAdapter(Activity context, List<LaboratoryHistoryModel> laboratoryHistoryModelList) {
        this.context = context;
        this.laboratoryHistoryModelList = laboratoryHistoryModelList;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lab_report_header, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LaboratoryHistoryModel laboratoryHistoryModel = laboratoryHistoryModelList.get(position);
        holder.bind(laboratoryHistoryModel);
    }

    @Override
    public int getItemCount() {
        return laboratoryHistoryModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo, tvPhysicianName, tvVisitDate;
        private Button btnSmartReport;
        private LinearLayout ll_content;
        private RecyclerView recyclerView_;
        private ImageButton ibtnSwitch;
        private MaterialCardView cardview_;

        public MyViewHolder(@NonNull View view) {
            super(view);
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
        public void bind(final LaboratoryHistoryModel model) {

            ibtnSwitch.setOnClickListener(v -> {
                if (ll_content.getVisibility() == View.GONE) {
                    ll_content.setVisibility(View.VISIBLE);
                    ibtnSwitch.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    ll_content.setVisibility(View.GONE);
                    ibtnSwitch.setImageResource(R.drawable.ic_arrow_down);
                }

            });

//            if (model.getLstOfChildMedicus().get(0).getHosp_ID().equals("4")) {
//                cardview_.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ed_blue));
//            }

            tvOrderNo.setText(ll_content.getResources().getString(R.string.order_no) + model.getDataList().get(0).getOrderNum()); // R.string.order_no + model.getOrderNum()

            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                tvPhysicianName.setText(model.getDataList().get(0).docNameAr);
                tvVisitDate.setText(model.getDataList().get(0).aptTimeAr);

            } else {
                tvPhysicianName.setText(model.getDataList().get(0).docName);
                tvVisitDate.setText(model.getDataList().get(0).aptTime);

            }


            for (LaboratoryHistoryModel.Data person : model.getDataList()) {
                if (person.getSmartReport() == 1) {
                    btnSmartReport.setVisibility(View.VISIBLE);
                    break;
                }
            }
            if (SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
                btnSmartReport.setVisibility(View.GONE);
            }
            btnSmartReport.setOnClickListener(view -> {

//                new MedicusHelper(context).loadReport();

                Intent i = new Intent(context, ViewReportActivity.class);
                i.putExtra("accsessionNum",model.getDataList().get(0).getAccessionNum());
                context.startActivity(i);
                //
//                ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
//                String mrn = user.getMrn();
//
//                Intent intent=new Intent(context, SmartReportPdfActivity.class);
//                intent.putExtra("orderNumber",model.getName());
//                intent.putExtra("mrNumber",mrn);
//                context.startActivity(intent);
            });

            LabReportContentAdapter labReportAdapter = new LabReportContentAdapter(context, model.getDataList());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            recyclerView_.setLayoutManager(linearLayoutManager);
            recyclerView_.setAdapter(labReportAdapter);
            labReportAdapter.setOnItemClickListener(new LabReportContentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, LaboratoryHistoryModel.Data model) {
                    ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
                    String mrn = user.getMrn();
                    int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

                    String pdfUrl=new UrlWithURLDecoder().getLaboratory_pdf_url(model.getSpecimenNum(),mrn,hosp,"1");
                    Intent intent=new Intent(context, PdfWebViewActivity.class);
                    intent.putExtra("url",pdfUrl);
                    intent.putExtra("name",model.getDocName());
                    context.startActivity(intent);
                }

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
            });
//
//            btnSmartReport.setOnClickListener(v -> {
//                if (listener != null)
//                    listener.onItemMoreClick(model, getAdapterPosition());
//            });
//
//            btnSmartReport.setOnClickListener(v -> onAdapterItemClickListener.onItemClick(getAdapterPosition(), btnSmartReport, list.get(getAdapterPosition()).getLstOfChildMedicus().get(getAdapterPosition()).getAccessionNum(), 0));


        }
    }

}
