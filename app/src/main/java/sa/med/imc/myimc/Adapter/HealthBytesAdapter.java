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
 * HealthBytes list.
 */

public class HealthBytesAdapter extends RecyclerView.Adapter<HealthBytesAdapter.Viewholder> {
    List<String> list;
    Context context;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;

    public HealthBytesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_health_bytes, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {


        holder.tv_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onReadMore(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_read_more)
        TextView tv_read_more;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onReadMore(int position);
    }
}
