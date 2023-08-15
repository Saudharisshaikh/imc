package sa.med.imc.myimc.Records.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.DischargeOldModel;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

public class DischargeOldAdapter extends RecyclerView.Adapter<DischargeOldAdapter.MyViewHolder> {

    Context context;
    ArrayList<DischargeOldModel.Data> dataArrayList;

    public DischargeOldAdapter(Context context, ArrayList<DischargeOldModel.Data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discharge,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DischargeOldModel.Data data=dataArrayList.get(position);
        holder.tv_report_examination.setText(data.getCreatedBy());
//        holder.tv_date.setText(new Common().convertDateForReport(data.getCreatedDate()));
        holder.tv_date.setText("");

        holder.tv_view_report.setOnClickListener(v -> {
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();
            int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

            String pdfUrl=new UrlWithURLDecoder().getDischarge_summary_pdf_url(String.valueOf(data.getId()), "1",
                    mrn,data.getReportType(), String.valueOf(hosp));
            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name",data.getCreatedBy()+" Discharge Report");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
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
