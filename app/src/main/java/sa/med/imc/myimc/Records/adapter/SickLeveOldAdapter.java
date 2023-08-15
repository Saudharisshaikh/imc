package sa.med.imc.myimc.Records.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.SickReportOldModel;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

public class SickLeveOldAdapter extends RecyclerView.Adapter<SickLeveOldAdapter.MyViewHolder> {

    Context context;
    ArrayList<SickReportOldModel.Content> contentArrayList;
    String lang = "";

    public SickLeveOldAdapter(Context context, ArrayList<SickReportOldModel.Content> contentArrayList) {
        this.context = context;
        this.contentArrayList = contentArrayList;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SickReportOldModel.Content content=contentArrayList.get(position);
        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            holder.tv_report_examination.setText(content.getLocationNameAr());

        } else {
            holder.tv_report_examination.setText(content.getLocationName());

        }

        holder.tv_date.setText(new Common().convertDateForReport(content.getLeaveActualLeaveDate()));

        holder.tv_available.setVisibility(View.VISIBLE);
        holder.tv_available.setTextColor(Color.parseColor("#818181"));
        holder.tv_view_report.setVisibility(View.VISIBLE);

        holder.tv_view_report.setOnClickListener(view -> {
           showConfirmDailo(content);
        });

    }



    void goToReport(String daig,SickReportOldModel.Content content){
        ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
        String mrn = user.getMrn();
        int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

        String pdfUrl=new UrlWithURLDecoder().getSick_leave_pdf_url(String.valueOf(content.getId()), "1", mrn, daig, String.valueOf(hosp));

        Intent intent=new Intent(context, PdfWebViewActivity.class);
        intent.putExtra("url",pdfUrl);
        intent.putExtra("name","Sick_Leave_"+content.getLeaveType()+" "+content.getAmendBy());
        context.startActivity(intent);
    }

    private void showConfirmDailo(SickReportOldModel.Content content) {
        Dialog sucDialog = new Dialog(context); // Context, this, etc.
        sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sucDialog.setContentView(R.layout.dialog_confirm_arrival);

        TextView toolbar_title = sucDialog.findViewById(R.id.toolbar_title);
        TextView tv_arrival = sucDialog.findViewById(R.id.tv_arrival);
        TextView yes = sucDialog.findViewById(R.id.tv_yes);
        TextView tv_no = sucDialog.findViewById(R.id.tv_no);
        ImageView iv_back_bottom = sucDialog.findViewById(R.id.iv_back_bottom);

        toolbar_title.setText(context.getString(R.string.confirmation));
        tv_arrival.setText(context.getString(R.string.want_include_diagnosis));

        sucDialog.setCancelable(false);

        yes.setOnClickListener(v -> {
            sucDialog.dismiss();
            goToReport("1",content);
        });
        tv_no.setOnClickListener(v -> {
            sucDialog.dismiss();
            goToReport("0",content);

        });

        iv_back_bottom.setOnClickListener(v -> sucDialog.cancel());

        sucDialog.show();

    }


    @Override
    public int getItemCount() {
        return contentArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_report_examination)
        TextView tv_report_examination;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_available)
        TextView tv_available;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;
        @BindView(R.id.tv_view_report)
        TextView tv_view_report;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
