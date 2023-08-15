package sa.med.imc.myimc.Adapter;

import static sa.med.imc.myimc.Utils.Common.simpleDailog;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.ui.ManageBookingMainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.util.Constant;
import sa.med.imc.myimc.SYSQUO.util.Utility;
import sa.med.imc.myimc.Utils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.globle.activity.NotificationWebActivity;

/**
 * Notifications list.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Viewholder> {
    List<NotificationResponse.NotificationModel> list;
    Context context;
    int pos_select = -1;
    private boolean edit = false;
    OnItemClickListener onItemClickListener;
    String lang;

    public NotificationsAdapter(Context context, List<NotificationResponse.NotificationModel> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notifications, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    public void setEdit(boolean editt) {
        edit = editt;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        NotificationResponse.NotificationModel model = list.get(position);
        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (model.getDescpAr() != null)
                holder.tv_des.setText(model.getDescpAr());

            if (model.getTitleAr() != null)
                holder.tv_title.setText(model.getTitleAr());

        } else {
            if (model.getDescp() != null)
                holder.tv_des.setText(model.getDescp());

            if (model.getTitle() != null)
                holder.tv_title.setText(model.getTitle());
        }

        if (model.getCreatedAt() != null)
            holder.tv_date.setText(Common.longToDate(model.getCreatedAt()));//Common.getConvertDateTime(model.getNotifTime()));

        if (edit)
            holder.iv_delete.setVisibility(View.VISIBLE);
        else
            holder.iv_delete.setVisibility(View.GONE);

        if (model.getRead().equalsIgnoreCase("0")) {
            holder.lay_item.setBackgroundResource(R.drawable.light_blue_noti_bg);
        } else {
            holder.lay_item.setBackgroundResource(R.drawable.time_slot_bg);
        }

        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipe_layout.close(true);
                onItemClickListener.onDeleteClick(position);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.onItemClick(position);
//            }
//        });
        holder.front_layout.setOnClickListener(v -> {
            onItemClickListener.onItemClick(position);

            if (model.getType().trim().equalsIgnoreCase("B") |
                    model.getType().trim().equalsIgnoreCase("S")){
                context.startActivity(new Intent(context, MainActivity.class));
            }else if (model.getType().trim().equalsIgnoreCase("BC")){
//                try {
                    if (model.getHtml()!=null && model.getHtml().trim().isEmpty()){
                        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
                            simpleDailog(context,model.getTitle(),model.getDescp());
                        }else {
                            simpleDailog(context,model.getTitleAr(),model.getDescpAr());
                        }
                    }else {
                        context.startActivity(new Intent(context, NotificationWebActivity.class)
                                .putExtra("html",model.getHtml()));

                    }
//                } catch (Exception e) {
//                    if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
//                        simpleDailog(context,model.getTitle(),model.getDescp());
//                    }else {
//                        simpleDailog(context,model.getTitleAr(),model.getDescpAr());
//                    }
//                }

            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onItemClickListener.onItemLongClick(position);
//                return true;
//            }
//        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;

  @BindView(R.id.swipe_layout)
  SwipeRevealLayout swipe_layout;

        @BindView(R.id.delete_layout)
        FrameLayout delete_layout;
        @BindView(R.id.front_layout)
        FrameLayout front_layout;
        @BindView(R.id.tv_des)
        TextView tv_des;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.iv_delete)
        ImageView iv_delete;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);

        void onDeleteClick(int position);
    }
}
