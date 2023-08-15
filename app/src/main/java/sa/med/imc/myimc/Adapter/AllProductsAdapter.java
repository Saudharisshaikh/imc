package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Products list.
 */

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.Viewholder> {
    List<String> list;
    List<Integer> images;

    Context context;
    OnItemClickListener onItemClickListener;


    public AllProductsAdapter(Context context, List<String> list, List<Integer> images) {
        this.context = context;
        this.list = list;
        this.images = images;

    }

    public AllProductsAdapter(Context context) {
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProductClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_view_detail)
        TextView tv_view_detail;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onViewProductClick(int position);
    }
}
