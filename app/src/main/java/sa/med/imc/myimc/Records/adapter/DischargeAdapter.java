package sa.med.imc.myimc.Records.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.DischargeModel;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

public class DischargeAdapter extends RecyclerView.Adapter<DischargeAdapter.MyViewHolder> {


    Context context;
    List<DischargeModel> dischargeModelList;

    public DischargeAdapter(Context context, List<DischargeModel> dischargeModels) {
        this.context = context;
        this.dischargeModelList = dischargeModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discharge,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DischargeModel dischargeModel=dischargeModelList.get(position);
        holder.tv_report_examination.setText(dischargeModel.getAuthorizingUser());
        holder.tv_date.setText(dischargeModel.getAuthorizedDate());

        holder.tv_view_report.setOnClickListener(v -> {
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();

            String pdfUrl=new UrlWithURLDecoder().getDischarge_summary_pdf_url(dischargeModel.getDocumentId(), "0",
                    mrn,dischargeModel.getReportType(), dischargeModel.getHosp());
            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name",dischargeModel.getAuthorizingUser()+" Discharge Report");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dischargeModelList.size();
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
