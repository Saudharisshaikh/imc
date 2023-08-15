package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.ViewReportActivity;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

/**
 * Lab Reports list adapter.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.Viewholder> {
    List<LabReport> list;
    private List<LabReport> All_list;
    OnItemClickListener onItemClickListener;

    Context context;
    int pos_select = -1;

    public RecordsAdapter(Context context, List<LabReport> list) {
        this.context = context;
        this.list = list;
    }


    public void setAllData(List<LabReport> list) {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_labreport, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        LabReport report = list.get(position);
        holder.tv_report_examination.setText(Html.fromHtml(report.getOrder_description()));
//        holder.tv_report_examination.setText(report.getOrder_description());
        holder.tv_date.setText(report.getCollected_date());
        holder.tv_available.setText(report.getOrdered_doctor());

        holder.tv_order_no.setText(context.getString(R.string.order_no)+" "+report.getOrder_id());
        holder.tv_physician_name_.setText(report.getOrdered_doctor());
        holder.tv_visit_date_.setText(report.getCollected_date());

        if (report.getDepartment_code().contains("HAEM") | report.getDepartment_code().contains("CHEM")) {
            Log.e("smart", "true");
            holder.btn_smart_report.setVisibility(View.VISIBLE);
        } else {
            holder.btn_smart_report.setVisibility(View.GONE);
        }

        try {
            if (!report.isSmart_report()) {
                holder.btn_smart_report.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btn_smart_report.setOnClickListener(view -> {
            Intent i = new Intent(context, ViewReportActivity.class);
            i.putExtra("accsessionNum", report.getOrder_accession_number());
            context.startActivity(i);
        });

//        if (report.getStatus() == 0) {
//            holder.tv_available.setText(context.getResources().getString(R.string.result_not_made));
//            holder.tv_view_report.setVisibility(View.GONE);
//            holder.tv_available.setVisibility(View.VISIBLE);
//
//        } else if (report.getStatus() == 1) {
//            holder.tv_available.setText("");
//            holder.tv_view_report.setVisibility(View.VISIBLE);
//            holder.tv_available.setVisibility(View.GONE);
//        }
//
//        holder.itemView.setOnClickListener(view -> {
//            context.startActivity(new Intent(context, PdfWebViewActivity.class));
//        });
        holder.ibtn_switch.setOnClickListener(view -> {
            if (holder.ll_content.getVisibility() == View.GONE) {
                holder.ll_content.setVisibility(View.VISIBLE);
                holder.ibtn_switch.setImageResource(R.drawable.ic_arrow_up);
            } else {
                holder.ll_content.setVisibility(View.GONE);
                holder.ibtn_switch.setImageResource(R.drawable.ic_arrow_down);
            }
        });

        holder.tv_view_report.setOnClickListener(v -> {
            ValidateResponse user = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();
            int hosp = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

            String pdfUrl = new UrlWithURLDecoder().getLaboratory_pdf_url(report.getSpecimen_number(), mrn, hosp, "0");
            Intent intent = new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url", pdfUrl);
            intent.putExtra("name", report.getOrder_description());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_report_examination)
        TextView tv_report_examination;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_available)
        TextView tv_available;
        @BindView(R.id.tv_view_report)
        TextView tv_view_report;
        @BindView(R.id.btn_smart_report)
        Button btn_smart_report;

        @BindView(R.id.tv_physician_name_)
        TextView tv_physician_name_;

        @BindView(R.id.tv_order_no)
        TextView tv_order_no;

        @BindView(R.id.tv_visit_date_)
        TextView tv_visit_date_;

        @BindView(R.id.ibtn_switch)
        ImageButton ibtn_switch;

        @BindView(R.id.ll_content)
        LinearLayout ll_content;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
