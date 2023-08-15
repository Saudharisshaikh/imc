package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sa.med.imc.myimc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Order Status list.
 */

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.Viewholder> {
    List<String> list;

    Context context;


    public OrderStatusAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order_status, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.tv_status.setText(list.get(position));
        if (position == 0) {
            holder.view_line_top.setVisibility(View.INVISIBLE);
        } else {
            holder.view_line_top.setVisibility(View.VISIBLE);

        }

        if (position == list.size() - 1) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_status)
        TextView tv_status;
        @BindView(R.id.view_line_top)
        View view_line_top;
        @BindView(R.id.view_line)
        View view_line;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
