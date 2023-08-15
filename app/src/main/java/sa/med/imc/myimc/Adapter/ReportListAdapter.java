package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.HealthSummary.model.SimpleNameIDModel;
import sa.med.imc.myimc.R;

/**
 *
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.Viewholder> {
    List<SimpleNameIDModel> list;

    Context context;
    OnItemClickListener onItemClickListener;
    String from = "";


    public ReportListAdapter(Context context, List<SimpleNameIDModel> list) {
        this.context = context;
        this.list = list;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_report, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.iv_icon.setImageResource(list.get(position).getIcon());
        holder.tv_report_name.setText(list.get(position).getTitle());
        if (list.get(position).getTitle().equalsIgnoreCase(context.getString(R.string.sick_levae)))
            holder.tv_description.setText(list.get(position).getName());
        else
            holder.tv_description.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(list.get(holder.getAdapterPosition()).getTitle());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_description)
        TextView tv_description;
        @BindView(R.id.tv_report_name)
        TextView tv_report_name;
        @BindView(R.id.iv_icon)
        ImageView iv_icon;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String position);

    }
}
