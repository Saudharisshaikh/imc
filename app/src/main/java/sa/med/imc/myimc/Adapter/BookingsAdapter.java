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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Bookings adapter list.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.Viewholder> {
    List<String> list;
    Context context;
    int pos_select = -1;

    public BookingsAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_booking, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
//        holder.tv_clinic_name.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return 5;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_physician_name)
        TextView tv_physician_name;
        @BindView(R.id.tv_physician_speciality)
        TextView tv_physician_speciality;
        @BindView(R.id.tv_physician_address)
        TextView tv_physician_address;
        @BindView(R.id.tv_booking_date)
        TextView tv_booking_date;
        @BindView(R.id.tv_booking_time)
        TextView tv_booking_time;
        @BindView(R.id.iv_physician_pic)
        ImageView iv_physician_pic;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
