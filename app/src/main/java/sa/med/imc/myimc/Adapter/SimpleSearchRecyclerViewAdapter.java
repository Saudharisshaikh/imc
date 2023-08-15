package sa.med.imc.myimc.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Network.IDNameModel;
import sa.med.imc.myimc.R;

public class SimpleSearchRecyclerViewAdapter extends RecyclerView.Adapter<SimpleSearchRecyclerViewAdapter.Viewholder> {

    Context context;
    OnItemClickListener onItemClickListener;
    List<IDNameModel> list, messagefilkterdlist;
    String lastValue = "";

    public SimpleSearchRecyclerViewAdapter(Context context, List<IDNameModel> list, String lastValue) {
        this.context = context;
        this.list = list;
        this.lastValue = lastValue;
        messagefilkterdlist = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_simple, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        if (lastValue.length() > 0) {
            if (lastValue.equalsIgnoreCase(list.get(position).getName())) {
                holder.tv_name.setTextColor(Color.BLACK);
            } else {
                holder.tv_name.setTextColor(Color.WHITE);

            }
        } else {
            holder.tv_name.setTextColor(Color.WHITE);

        }

        holder.tv_name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Expense list item view holder
    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_customer_name)
//        TextView tvCustomerName;
//        @BindView(R.id.tv_due_date)
//        TextView tvDueDate;
//        @BindView(R.id.tv_status)
//        TextView tvStatus;
//        @BindView(R.id.total_amount)
//        TextView total_amount;


        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = messagefilkterdlist;
                } else {
                    List<IDNameModel> filteredList = new ArrayList<>();
                    for (IDNameModel row : messagefilkterdlist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase(Locale.getDefault()).contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<IDNameModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    public  IDNameModel getName(int pos) {
        return list.get(pos);
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

