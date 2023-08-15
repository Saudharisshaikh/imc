package sa.med.imc.myimc.Physicians;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sa.med.imc.myimc.Physicians.model.LocationList;
import sa.med.imc.myimc.R;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.MyViewHolder> {

    Context context;
    List<LocationList> location_list;

    public LocationListAdapter(Context context, List<LocationList> location_list) {
        this.context = context;
        this.location_list = location_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_item_location,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.location_description.setText(location_list.get(position).getLocation_description());
    }

    @Override
    public int getItemCount() {
        return location_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView location_description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            location_description=itemView.findViewById(R.id.location_description);
        }
    }
}
