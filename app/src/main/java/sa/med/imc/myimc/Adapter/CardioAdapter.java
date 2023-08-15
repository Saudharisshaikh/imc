package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Utils.Common;

/**
 * Cardiology Reports list adapter.
 */

public class CardioAdapter extends RecyclerView.Adapter<CardioAdapter.Viewholder> {
    List<CardioReportResponse.Datum> list;
    private List<CardioReportResponse.Datum> All_list;
    OnItemClickListener onItemClickListener;

    Context context;
    int pos_select = -1;

    public CardioAdapter(Context context, List<CardioReportResponse.Datum> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setAllData(List<CardioReportResponse.Datum> list) {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        CardioReportResponse.Datum report = list.get(position);
        if (report.getReportName() != null)
            holder.tv_report_examination.setText(report.getReportName());

//        holder.tv_report_examination.setText(context.getString(R.string.report_number) + " " + report.getReportNumber() + "");

        if (report.getReportDate() != null)
            holder.tv_date.setText(context.getString(R.string.reported_date) + " " + Common.getConvertReportDateTime(report.getReportDate()));

        if (report.getReportExamDate() != null)
            holder.tv_exam_date.setText(context.getString(R.string.exam_date) + " " + Common.getConvertDateTime(report.getReportExamDate()));
//
//        if (report.getStatus().equalsIgnoreCase("N")) {
//            holder.tv_available.setText(context.getResources().getString(R.string.result_not_made));
//            holder.tv_view_report.setVisibility(View.GONE);
//            holder.tv_available.setVisibility(View.VISIBLE);
//
//        } else if (report.getStatus().equalsIgnoreCase("Y")) {
//            holder.tv_available.setText("");
//            holder.tv_view_report.setVisibility(View.VISIBLE);
//            holder.tv_available.setVisibility(View.GONE);
//        }

        if (report.getReportStatus() != null && report.getReportStatus().equalsIgnoreCase("Verified")) {
            holder.tv_available.setText("");
            holder.tv_view_report.setVisibility(View.VISIBLE);
            holder.tv_available.setVisibility(View.GONE);
        } else {
            holder.tv_available.setText(context.getResources().getString(R.string.result_not_made));
            holder.tv_view_report.setVisibility(View.GONE);
            holder.tv_available.setVisibility(View.VISIBLE);
        }
        holder.tv_view_report.setOnClickListener(v -> onItemClickListener.onItemClick(holder.getAdapterPosition()));
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
        @BindView(R.id.tv_exam_date)
        TextView tv_exam_date;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
