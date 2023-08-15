package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sa.med.imc.myimc.R;

import java.util.List;

/**
 * Reviews list.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.Viewholder> {
    List<String> list;
    List<Integer> images;

    Context context;


    public ReviewsAdapter(Context context, List<String> list, List<Integer> images) {
        this.context = context;
        this.list = list;
        this.images = images;

    }

    public ReviewsAdapter(Context context) {
        this.context = context;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_review, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class Viewholder extends RecyclerView.ViewHolder {


        Viewholder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }


}
