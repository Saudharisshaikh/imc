package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

/**
 * Completed Assessment Forms list.
 */

public class CompletedAssessmentAdapter extends RecyclerView.Adapter<CompletedAssessmentAdapter.Viewholder> {
    List<CompletedResponse.Assessment> list;
    Context context;
    OnItemClickListener onItemClickListener;

    public CompletedAssessmentAdapter(Context context, List<CompletedResponse.Assessment> list) {
        this.context = context;
        this.list = list;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_completed_assessment, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder,  int position) {
        CompletedResponse.Assessment assessment = list.get(position);
        holder.tv_form_name.setText(assessment.getFormName());
        holder.tv_submission_date.setText(context.getString(R.string.submission_date)+" "+Common.completedAssessmentDateTime(assessment.getUpdateDateTime()));

        holder.tv_view_result.setOnClickListener(v -> onItemClickListener.onItemClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_form_name)
        TextView tv_form_name;
        @BindView(R.id.tv_view_result)
        TextView tv_view_result;
        @BindView(R.id.tv_submission_date)
        TextView tv_submission_date;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void  onItemClick(int position);
    }
}
