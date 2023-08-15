package sa.med.imc.myimc.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.model.Select_TypeResponse;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Select_TypeResponse.EpisodeList> item;
    private Context context;
    private View.OnClickListener mOnItemClickListener;
    String modified_date;
    String lang = Constants.ENGLISH;
    String formattedDate;
    String ourdate;
    String app_date;
    public RecyclerViewAdapter(Context context, List<Select_TypeResponse.EpisodeList> item) {
        Log.i("autolog", "RecyclerViewAdapter");
        this.item = item;
        this.context = context;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        // onItemClickListener =itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("autolog", "onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_simple, null);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("autolog", "onBindViewHolder");

         app_date = item.get(position).getEpisode_date();
       // String serverdateFormat = "yyyy-MM-dd'T'HH:mm:ss.000+0000";
        Log.e("DATE_IS", "DATA_IS" + app_date);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

//        Date d = null;
//        try {
//            d = df.parse(app_date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String formatted = toDf.format(d);


        if (lang.equalsIgnoreCase(Constants.ARABIC))
        {
            if (item.get(position).getCare_provider_description() != null) {
                holder.name.setText(app_date + " - " + item.get(position).getEpisodeType() + " - " + item.get(position).getCare_provider_description());
            }
            //  holder.tv_clinic_name.setText(model.getDescAr());


        }
        else
            {
            if (item.get(position).getCare_provider_description() != null) {
                holder.name.setText(app_date + " - " + item.get(position).getEpisodeType() + " - " + item.get(position).getCare_provider_description());
            }

        }
        //   holder.name.setText(modified_date+"-"+item.get(position).getConsultant());
        // holder.hobby.setText(item.get(position).getHobby());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.onItemClick(v,position);
//            }
//        });
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        Log.i("autolog", "getItemCount");
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.i("autolog", "ViewHolder");

            name = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

            //   hobby = (TextView) itemView.findViewById(R.id.hobby);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}