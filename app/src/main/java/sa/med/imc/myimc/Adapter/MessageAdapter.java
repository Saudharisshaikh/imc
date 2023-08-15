package sa.med.imc.myimc.Adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import sa.med.imc.myimc.R;

import java.util.ArrayList;

/*
Messages list adapter for single chat window
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private View itemView;
    Activity activity;
    DisplayMetrics displayMetrics;

    private static final int TYPE_POST = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<String> messageArray;

    // Provide a reference to the views for each data item_detail_activity
    // Complex data items may need more than one view per item_detail_activity, and
    // you provide access to all the views for a data item_detail_activity in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout out_message_view, in_message_view;
        ImageView iv_photo, iv_photo_my;
        TextView tvSenderName, tvInMessage, tvInMessageTime, tvOutMessage, tvOutMessageTime;

        public ViewHolder(View convertView) {
            super(convertView);

            out_message_view = (LinearLayout) convertView.findViewById(R.id.out_message_view);
            in_message_view = (LinearLayout) convertView.findViewById(R.id.in_message_view);
//
//            iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
//            iv_photo_my = (ImageView) convertView.findViewById(R.id.iv_photo_my);
//
//            tvSenderName = (TextView) convertView.findViewById(R.id.tvSenderName);
            tvInMessage = (TextView) convertView.findViewById(R.id.tvInMessage);
//            tvInMessageTime = (TextView) convertView.findViewById(R.id.tvInDate);
//
            tvOutMessage = (TextView) convertView.findViewById(R.id.tvOutMessage);
//            tvOutMessageTime = (TextView) convertView.findViewById(R.id.tvOutDate);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(Activity activity, ArrayList<String> list) {
        messageArray = list;
        this.activity = activity;

        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }


    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_message, parent, false);

        return new ViewHolder(itemView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 1) {
            holder.out_message_view.setVisibility(View.VISIBLE);
            holder.in_message_view.setVisibility(View.GONE);

        } else if (position == 3) {
            holder.out_message_view.setVisibility(View.VISIBLE);
            holder.in_message_view.setVisibility(View.GONE);

        } else {
            holder.out_message_view.setVisibility(View.GONE);
            holder.in_message_view.setVisibility(View.VISIBLE);
        }
    }

    public void addNewMsg(String ms) {
        messageArray.add(0, ms);
        notifyDataSetChanged();
        //  lv_MyInbox.scrollToPosition(0);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 5;
    }

}
