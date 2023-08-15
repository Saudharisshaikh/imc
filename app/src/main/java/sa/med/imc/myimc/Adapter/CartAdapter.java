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
 * Cart list.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    List<String> list;
    List<Integer> images;

    Context context;
    OnItemClickListener onItemClickListener;


    public CartAdapter(Context context, List<String> list, List<Integer> images) {
        this.context = context;
        this.list = list;
        this.images = images;

    }

    public CartAdapter(Context context) {
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cart, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRemoveClick(position);
            }
        });

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ed_quantity.getText().toString().trim().length() > 0) {
                    int qua = Integer.parseInt(holder.ed_quantity.getText().toString());
                    qua = qua + 1;
                    holder.ed_quantity.setText("" + qua);
                }
            }
        });

        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ed_quantity.getText().toString().trim().length() > 0) {
                    int qua = Integer.parseInt(holder.ed_quantity.getText().toString());
                    if (qua > 1) {
                        qua = qua - 1;
                        holder.ed_quantity.setText("" + qua);
                    }
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_remove)
        TextView tv_remove;
        @BindView(R.id.ed_quantity)
        TextView ed_quantity;
        @BindView(R.id.tv_add)
        TextView tv_add;
        @BindView(R.id.tv_minus)
        TextView tv_minus;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onRemoveClick(int position);

        void onItemClick(int position);

    }
}
