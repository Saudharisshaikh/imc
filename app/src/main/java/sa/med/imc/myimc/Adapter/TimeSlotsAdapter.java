package sa.med.imc.myimc.Adapter;

import android.app.Activity;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Time slot list.
 */

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.Viewholder> {
//    List<DrTimeSlot.SlotList> list;
//    List<DrTimeSlot.SlotList> messagefilkterdlist;

    List<DrTimeSlot.SlotList> Drlist;
    List<DrTimeSlot.SlotList> Drmessagefilkterdlist;

    Activity context;
    int pos_select = -1;
    private String selected_date = "";
    private String current_date = "";
    private int bookingStartTime = 0;
    OnItemClickListener onItemClickListener;


    boolean isToday = false;
    boolean isDoctorList = false;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }


    public TimeSlotsAdapter(Activity context, List<DrTimeSlot.SlotList> list, String date, boolean isDoctorList) {
        this.context = context;
        this.Drlist = list;
        this.isDoctorList = isDoctorList;
        Drmessagefilkterdlist = list;
        selected_date = date;


        Calendar curr = Calendar.getInstance();
        current_date = Common.getDate(curr.get(Calendar.YEAR) + "-" + (curr.get(Calendar.MONTH) + 1) + "-" + curr.get(Calendar.DAY_OF_MONTH));

    }

    public void setSelectedDate(String date, int bookingStartTime) {
        selected_date = date;
        pos_select = -1;
        // this.bookingStartTime = bookingStartTime;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_slot, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        position = holder.getAdapterPosition();
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());

        Log.e("abcd", todayDate);
        Log.e("abcd", Drlist.get(position).getSlotDate());
        holder.lay_item.setBackgroundResource(R.drawable.white_gradient_bg);
        holder.lay_item.setClickable(true);
        holder.lay_item.setEnabled(true);

        if (todayDate.equalsIgnoreCase(Drlist.get(position).getSlotDate())) {
            isToday = true;
            String nowTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Calendar.getInstance().getTime());
            String slotTime = Drlist.get(position).getSlotFromTime();

            Log.e("abcd","nowTime "+nowTime);
            Log.e("abcd","slotTime "+slotTime);

            String mNowTime=nowTime.replace(":",".");
            String mSlotTime=slotTime.replace(":",".");

            Log.e("abcd","mNowTime "+mNowTime);
            Log.e("abcd","mSlotTime "+mSlotTime);

            Double dNowTime= Double.valueOf(mNowTime);
            Double dSlotTime= Double.valueOf(mSlotTime);

//            if(dNowTime>dSlotTime){
//                Log.e("abcd","slot Not avalable");
//                holder.lay_item.setClickable(false);
//                holder.lay_item.setEnabled(false);
//                holder.tv_slot_time.setClickable(false);
//                holder.tv_slot_time.setEnabled(false);
//
//                holder.lay_item.setBackgroundResource(R.drawable.grey_gradient_bg);
//            }else {
//                Log.e("abcd","slot avalable");
//
//            }

        } else {
            isToday = false;
        }
//        if (Drlist.size() > position)
//        {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tv_slot_time.setText(Drlist.get(position).getSlotFromTime());
        }else {
            holder.tv_slot_time.setText(Drlist.get(position).getSlotFromTime());
        }
        holder.tv_slot_time.setTextColor(Color.parseColor("#004e8c"));

//        if (Drlist.get(position).getSlotFromTime() != null) {
//            holder.lay_item.setBackgroundResource(R.drawable.white_gradient_bg);
//            holder.lay_item.setEnabled(true);
//        } else {
//            holder.lay_item.setBackgroundResource(R.drawable.grey_gradient_bg);
//            holder.lay_item.setEnabled(false);
//        }

        // if current date and selected date are equal.
        if (selected_date.equalsIgnoreCase(current_date)) {
            Calendar curr = Calendar.getInstance();
            curr.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
//            String current_time = Common.getCurrentTime(curr.get(Calendar.HOUR_OF_DAY) + ":" + (curr.get(Calendar.MINUTE)) + ":" + curr.get(Calendar.SECOND), bookingStartTime);
//            String slot = Common.getCurrentTime24(Common.getConvertTimeSlotNew(Drlist.get(position).getSlotFromTime()));

//            if (current_time.compareTo(slot) > 0 || current_time.equalsIgnoreCase(slot)) {
//                holder.lay_item.setBackgroundResource(R.drawable.grey_gradient_bg);
//                holder.lay_item.setEnabled(false);
//
//            } else {
//                if (Drlist.get(position).getSlotFromTime() != null) {
//                    holder.lay_item.setBackgroundResource(R.drawable.white_gradient_bg);
//                    holder.lay_item.setEnabled(true);
//                } else {
//                    holder.lay_item.setBackgroundResource(R.drawable.grey_gradient_bg);
//                    holder.lay_item.setEnabled(false);
//                }
//            }
        }

        if (pos_select == position) {
            holder.tv_slot_time.setTextColor(Color.parseColor("#ffffff"));
            holder.lay_item.setBackgroundResource(R.drawable.time_slot_selection_gradient_bg);
        }

        int finalPosition = position;
        holder.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos_select==finalPosition){
                    onItemClickListener.onUnSelect(Drlist.get(finalPosition).getSlotBookingId(), Drlist.get(finalPosition).getSlotFromTime());
                    pos_select = -1;
                    notifyDataSetChanged();

                }else {
                    if (selected_date.equalsIgnoreCase(current_date)) {

                        // get current time
                        Calendar curr = Calendar.getInstance();
                        curr.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
                        String current_time = Common.getCurrentTime(curr.get(Calendar.HOUR_OF_DAY) + ":" + (curr.get(Calendar.MINUTE)) + ":" + curr.get(Calendar.SECOND), bookingStartTime);
                        String slot = Common.getCurrentTime24(Common.getConvertTimeSlotNew(Drlist.get(holder.getAdapterPosition()).getSlotFromTime()));

                        if (current_time.compareTo(slot) > 0 || current_time.equalsIgnoreCase(slot)) {
                            pos_select = -1;
                            notifyDataSetChanged();

                        } else {
                            pos_select = finalPosition;
                            notifyDataSetChanged();
                        }
                    } else {
                        pos_select = finalPosition;
                        notifyDataSetChanged();
                    }

                    onItemClickListener.onSlotClick(Drlist.get(finalPosition).getSlotBookingId(), Drlist.get(finalPosition).getSlotFromTime());

                }



            }
        });
//        }
    }

    public String getSessionId() {
        if (pos_select > -1)
            return Drlist.get(pos_select).getSlotBookingId();
        else
            return null;
    }


    public String getSelectedTime() {
        if (pos_select > -1)
            return Common.getConvertTimeSlotNew(Drlist.get(pos_select).getSlotFromTime());
        else return "";
    }

    public String getSelectedTime2() {
        if (pos_select > -1)
            return Drlist.get(pos_select).getSlotFromTime();
        else return "";
    }

    public String getSelectedDat() {
        if (pos_select > -1)
            return Drlist.get(pos_select).getSlotDate();
        else return "";
    }

    @Override
    public int getItemCount() {
        int size=Drlist.size();
        if (isDoctorList){
            if (size>6){
                size=6;
            }
        }

        return size;
    }

    /*public Filter getFilter(int type) {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                pos_select = -1;
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    list = messagefilkterdlist;
//                } else {
                List<DrTimeSlot.SlotList> filteredList = new ArrayList<>();
                for (DrTimeSlot.SlotList row : Drmessagefilkterdlist) {

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (filteredList.contains(row.getSlotFromTime())) {
                        filteredList.add(row);
                    }
                }

                Drlist = filteredList;
//                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Drlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Drlist = (ArrayList<DrTimeSlot.SlotList>) filterResults.values;

                if (Drlist.size() == 0)
//                    Common.makeToast(context, context.getString(R.string.no_time_slot));
                    Toast.makeText(context, context.getString(R.string.no_time_slot), Toast.LENGTH_LONG).show();

                notifyDataSetChanged();
            }
        };

    }*/


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_slot_time)
        TextView tv_slot_time;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;


        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onSlotClick(String SlotID, String slotTime);
        void onUnSelect(String SlotID, String slotTime);
    }
}
