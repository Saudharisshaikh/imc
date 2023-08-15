package sa.med.imc.myimc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Utils.Common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.activity.PdfWebViewActivity;

/**
 * Sick Leave list.
 */

public class SickLeaveAdapter extends RecyclerView.Adapter<SickLeaveAdapter.Viewholder> {
    List<SickLeave> list;
    private List<SickLeave> All_list;

    Context context;
    int pos_select = -1;

    public SickLeaveAdapter(Context context, List<SickLeave> list) {
        this.context = context;
        this.list = list;
    }

    public void clearSearchData() {
        list.clear();
        list.addAll(All_list);
        notifyDataSetChanged();
    }

    public void setAllData(List<SickLeave> list) {
        All_list = new ArrayList<>();
        this.All_list.addAll(list);

    }

//    public void bind(final SickLeave model){
//       if(model.getHosp().equals("4")){
//           lay_item.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ed_blue));
//       }
//    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_record, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        SickLeave report = list.get(position);
        holder.tv_report_examination.setText(report.getClinic_description());
//        holder.tv_date.setText(Common.getConvertDateTime(report.getAmendDate()));

        holder.tv_available.setVisibility(View.VISIBLE);
        holder.tv_available.setTextColor(Color.parseColor("#818181"));
        holder.tv_view_report.setVisibility(View.VISIBLE);

        holder.tv_view_report.setOnClickListener(view -> {
            ValidateResponse user= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);
            String mrn = user.getMrn();
            int hosp=SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

            String daig="1";

            String pdfUrl= null;
            try {
                pdfUrl = new UrlWithURLDecoder().getSick_leave_pdf_url(URLEncoder.encode(report.getDocument_id(),"UTF-8"), "0", mrn, daig, String.valueOf(hosp));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Intent intent=new Intent(context, PdfWebViewActivity.class);
            intent.putExtra("url",pdfUrl);
            intent.putExtra("name","Sick_Leave_"+report.getClinic_description());
            context.startActivity(intent);
        });

//        holder.tv_available.setText(report.getClinic_description());
//        if (list.get(position).getHosp().equalsIgnoreCase("4")){
//            holder.lay_item.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ed_blue));
//        }
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
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;
        @BindView(R.id.tv_view_report)
        TextView tv_view_report;



        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
