package sa.med.imc.myimc.Records.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.RadiologyOldModel;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

public class RadiologyOldAdapter extends RecyclerView.Adapter<RadiologyOldAdapter.MyViewHolder> {

    Activity context;
    List<RadiologyOldModel> radiologyOldModelList;

    public RadiologyOldAdapter(Activity context, List<RadiologyOldModel> radiologyOldModelList) {
        this.context = context;
        this.radiologyOldModelList = radiologyOldModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RadiologyOldModel radiologyOldModel=radiologyOldModelList.get(position);

        holder.tv_report_examination.setText(radiologyOldModel.getDesc());
        holder.tv_date.setText(new Common().convertDateForReport(radiologyOldModel.getOrderDate()));
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
            if (radiologyOldModel.getOrderStatusWeb()!=1){
                showDailog();
                return;
            }
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();

            String pdfUrl=new UrlWithURLDecoder().getRadiology_pdf_url(mrn,radiologyOldModel.getId().orderNo, radiologyOldModel.getId().orderNoLine,
                    String.valueOf(radiologyOldModel.getHosp()),"1");

            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name",radiologyOldModel.getDesc());
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
        return radiologyOldModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_report_examination)
        TextView tv_report_examination;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_available)
        TextView tv_available;
        @BindView(R.id.tv_view_report)
        TextView tv_view_report;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
