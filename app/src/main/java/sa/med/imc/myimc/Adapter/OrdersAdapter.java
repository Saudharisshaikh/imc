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
 * Orders list.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.Viewholder> {
    List<String> list;
    List<Integer> images;

    Context context;
    OnItemClickListener onItemClickListener;
    String from = "";


    public OrdersAdapter(Context context, List<String> list, List<Integer> images) {
        this.context = context;
        this.list = list;
        this.images = images;

    }

    public OrdersAdapter(Context context, String from) {
        this.context = context;
        this.from = from;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_oders, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });


        holder.tv_rate_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRateReviewClick(position);
            }
        });

        if (from.length() > 0) {
            holder.view.setVisibility(View.GONE);
            holder.tv_rate_review.setVisibility(View.GONE);
        } else {
            if (position != 0) {
                holder.tv_date.setText("Delivered on Jul 15");
                holder.view.setVisibility(View.VISIBLE);
                holder.tv_rate_review.setVisibility(View.VISIBLE);
            } else {
                holder.view.setVisibility(View.GONE);
                holder.tv_rate_review.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_product_name)
        TextView tv_product_name;
        @BindView(R.id.tv_rate_review)
        TextView tv_rate_review;
        @BindView(R.id.view_)
        View view;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onRateReviewClick(int position);

        void onItemClick(int position);

    }
}
