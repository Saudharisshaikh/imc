package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Retail list.
 */

public class RetailAdapter extends RecyclerView.Adapter<RetailAdapter.Viewholder> {
    Integer[] list = {

            R.string.fine_chocolate,
            R.string.gardenia,
            R.string.baby_fitaihi};

    private Integer[] images = {

            R.drawable.godaiva,
            R.drawable.gardinia,
            R.drawable.baby_fitaihi_update};

    Context context;
    OnItemClickListener onItemClickListener;

    public RetailAdapter(Context context) {
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_retail, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.tv_name.setText(context.getString(list[position]));
        holder.iv_image.setImageResource(images[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onCategoryClick(position);
            }
        });

    }

    public int getImage(int pos){
        return images[pos];
    }

    public int getName(int pos){
        return list[pos];
    }

    @Override
    public int getItemCount() {
        return list.length;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_image)
        ImageView iv_image;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onCategoryClick(int position);
    }

}
