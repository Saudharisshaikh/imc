package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Address list.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Viewholder> {
    List<String> list;
    List<Integer> images;

    Context context;
    OnItemClickListener onItemClickListener;
    String from = "";


    public AddressAdapter(Context context, List<String> list, List<Integer> images) {
        this.context = context;
        this.list = list;
        this.images = images;

    }

    public AddressAdapter(Context context, String from) {
        this.context = context;
        this.from = from;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_address, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        if (from.equalsIgnoreCase("cart")) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRemoveClick(position);
            }
        });


        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onEditClick(position);
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
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_phone)
        TextView tv_phone;
        @BindView(R.id.tv_edit)
        TextView tv_edit;
        @BindView(R.id._lay_operation)
        LinearLayout _lay_operation;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onRemoveClick(int position);

        void onEditClick(int position);

        void onItemClick(int position);

    }
}
