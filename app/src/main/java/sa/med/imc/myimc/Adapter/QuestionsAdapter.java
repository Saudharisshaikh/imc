package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.model.FormIdNameModel;
import sa.med.imc.myimc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Questions list.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.Viewholder> {
    private List<BookingAppoinment.PromisForm> list;
    private Context context;
    private int pos_select = 0;
    private OnClickListener onClickListener;

    public QuestionsAdapter(Context context, List<BookingAppoinment.PromisForm> list1) {
        this.context = context;
        this.list = list1;
    }

    public void setOnClickListener(OnClickListener onClickListe) {
        this.onClickListener = onClickListe;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_promis, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    public void addAll(List<BookingAppoinment.PromisForm> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.tv_question.setText(list.get(position).getFormName());
        if (position == pos_select)
            holder.tv_question.setChecked(true);
        else
            holder.tv_question.setChecked(false);

        if (pos_select == 0) {
            onClickListener.onChecked(pos_select);
        }
        holder.tv_question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (buttonView.isChecked()) {
                        pos_select = holder.getAdapterPosition();
                        onClickListener.onChecked(pos_select);
                    }
                    notifyDataSetChanged();
                }
            }
        });
//        holder.tv_yes.setBackgroundResource(R.drawable.unselected_ans);
//        holder.tv_yes.setTextColor(Color.parseColor("#0884e2"));
//
//        holder.tv_no.setBackgroundResource(R.drawable.unselected_ans);
//        holder.tv_no.setTextColor(Color.parseColor("#0884e2"));

//        if (pos_select == position) {
//            if (type.equalsIgnoreCase("y")) {
//                holder.tv_yes.setBackgroundResource(R.drawable.selected_ans);
//                holder.tv_yes.setTextColor(Color.WHITE);
//            }
//
//            if (type.equalsIgnoreCase("n")) {
//                holder.tv_no.setBackgroundResource(R.drawable.selected_ans);
//                holder.tv_no.setTextColor(Color.WHITE);
//            }
//            pos_select = -1;
//        }

//
//        holder.tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.tv_yes.setBackgroundResource(R.drawable.selected_ans);
//                holder.tv_yes.setTextColor(Color.WHITE);
//
//                holder.tv_no.setBackgroundResource(R.drawable.unselected_ans);
//                holder.tv_no.setTextColor(Color.parseColor("#0884e2"));
//
//            }
//        });
//
//        holder.tv_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.tv_yes.setBackgroundResource(R.drawable.unselected_ans);
//                holder.tv_yes.setTextColor(Color.parseColor("#0884e2"));
//
//                holder.tv_no.setBackgroundResource(R.drawable.selected_ans);
//                holder.tv_no.setTextColor(Color.WHITE);
//            }
//        });

    }

    public int getSelected() {
        return pos_select;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_question)
        CheckBox tv_question;
//        @BindView(R.id.tv_yes)
//        TextView tv_yes;
//        @BindView(R.id.tv_no)
//        TextView tv_no;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onChecked(int osition);
    }


}
