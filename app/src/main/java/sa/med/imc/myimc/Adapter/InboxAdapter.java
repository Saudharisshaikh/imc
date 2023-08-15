package sa.med.imc.myimc.Adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import sa.med.imc.myimc.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private View itemView;
    Activity activity;
    DisplayMetrics displayMetrics;

    private static final int TYPE_ITEM = 1;
    OnItemClickListener onItemClickListener;

    private ArrayList<String> messageArray;

    // Provide a reference to the views for each data item_detail_activity
    // Complex data items may need more than one view per item_detail_activity, and
    // you provide access to all the views for a data item_detail_activity in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_lay)
        RelativeLayout main_lay;

        public ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, itemView);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public InboxAdapter(Activity activity, ArrayList<String> list) {
        messageArray = list;
        this.activity = activity;

        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_inbox, parent, false);
        return new ViewHolder(itemView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position % 2 == 0) {
            holder.main_lay.setBackgroundResource(R.drawable.bt_inbox_white);
        } else {
            holder.main_lay.setBackgroundResource(R.drawable.bt_inbox_light_white);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    public void addNewMsg(String ms) {
        messageArray.add(0, ms);
        notifyDataSetChanged();
        //  lv_MyInbox.scrollToPosition(0);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 8;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
