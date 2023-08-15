package sa.med.imc.myimc.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Utils.Common;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

/**
 * Medical report list.
 */

public class MedicalReportAdapter extends RecyclerView.Adapter<MedicalReportAdapter.Viewholder> {
    List<MedicalReport> list;
    private List<MedicalReport> All_list;
    OnItemClickListener onItemClickListener;

    Activity context;
    int pos_select = -1;

    public MedicalReportAdapter(Activity context, List<MedicalReport> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    public void setAllData(List<MedicalReport> list) {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);

    }
    public void clearSearchData() {
        list.clear();
        list.addAll(All_list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        MedicalReport report = list.get(position);
        holder.tv_report_examination.setText(report.getOrder_description());
        holder.tv_date.setText(report.getResult_date()+" "+report.getResult_time());
        holder.tv_available.setText(context.getString(R.string.dr) + " " +report.getOrdered_doctor());
        holder.tv_view_report.setVisibility(View.VISIBLE);
//
//        if (report.getResultAvailable() == 0) {
//            holder.tv_available.setText(context.getResources().getString(R.string.result_not_made));
//            holder.tv_view_report.setVisibility(View.GONE);
//            holder.tv_available.setVisibility(View.VISIBLE);
//
//        } else if (report.getResultAvailable() == 1) {
//            holder.tv_available.setText("");
//            holder.tv_available.setVisibility(View.GONE);
//        }
        holder.tv_view_report.setOnClickListener(v -> {
            if (!report.getOrdered_status().trim().equalsIgnoreCase("Verified")){
                showDailog();
                return;
            }
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();
            int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

            String pdfUrl=new UrlWithURLDecoder().getRadiology_pdf_url(mrn, URLEncoder.encode(report.getOrder_id()), report.getOrderNoLine(),
                    String.valueOf(hosp), "0");

            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name",report.getOrder_description());
            context.startActivity(intent);
        });

    }

    private void showDailog() {
        ViewGroup viewGroup = context.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(context.getString(R.string.result_not_published_yet));
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
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

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public   interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
