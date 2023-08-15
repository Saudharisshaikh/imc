package sa.med.imc.myimc.Records.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardiologyModel;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

public class CardiologyAdapter extends RecyclerView.Adapter<CardiologyAdapter.myViewHolder> {

    Context context;
    List<CardiologyModel> cardiologyModelList;

    public CardiologyAdapter(Context context, List<CardiologyModel> cardiologyModelList) {
        this.context = context;
        this.cardiologyModelList = cardiologyModelList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cardiology,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CardiologyModel cardiologyModel=cardiologyModelList.get(position);
        holder.tv_report_examination.setText(cardiologyModel.getOrder_description());
        holder.tv_date.setText(cardiologyModel.getResult_date());

        holder.tv_view_report.setOnClickListener(v -> {
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();
            int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

            String pdfUrl=new UrlWithURLDecoder().getReports_cardiology(URLEncoder.encode(cardiologyModel.getOrder_id()),mrn,hosp,"0",null);
            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name",cardiologyModel.getOrder_description());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return cardiologyModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
