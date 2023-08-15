package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.R;

/**
 * Item Assessment list.
 */

public class ItemAssessmentAdapter extends RecyclerView.Adapter<ItemAssessmentAdapter.Viewholder> {
    List<AssessmentModel.Map> list;
    Context context;
    int pos_select = -1;

    public ItemAssessmentAdapter(Context context, List<AssessmentModel.Map> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_promis, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.tv_question.setText(list.get(position).getDescription());
        if (position == pos_select)
            holder.tv_question.setChecked(true);
        else
            holder.tv_question.setChecked(false);


        holder.tv_question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (buttonView.isChecked())
                        pos_select = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        });

    }

   public int getSelected(){
        return pos_select;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_question)
        CheckBox tv_question;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    }
