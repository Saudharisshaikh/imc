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
import sa.med.imc.myimc.HealthSummary.model.SimpleNameIDModel;
import sa.med.imc.myimc.R;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.Viewholder> {

    Context context;
    List<SimpleNameIDModel> list;

    GridRecyclerViewAdapter(Context context, List<SimpleNameIDModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_vital_signs_grid_item, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.tv_value.setText(list.get(position).getName());
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_title.setCompoundDrawablesWithIntrinsicBounds(0, list.get(position).getIcon(), 0, 0);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    // Expense list item view holder
    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_value)
        TextView tv_value;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}