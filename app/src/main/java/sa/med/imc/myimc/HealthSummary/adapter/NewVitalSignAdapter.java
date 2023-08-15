package sa.med.imc.myimc.HealthSummary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.R;

public class NewVitalSignAdapter extends RecyclerView.Adapter<NewVitalSignAdapter.MyViewHolder> {

    Context context;
    ArrayList<NewVitalModel.VitalList> vital_list;

    public NewVitalSignAdapter(Context context, ArrayList<NewVitalModel.VitalList> vital_list) {
        this.context = context;
        this.vital_list = vital_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.new_vital_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewVitalModel.VitalList vitalList=vital_list.get(position);

        holder.observation_item_description.setText(vitalList.getObservation_item_description());
        holder.observation_date.setText(vitalList.getObservation_date());
        holder.observation_time.setText(vitalList.getObservation_time());

        String unit="";

        try {
            if (vitalList.getUom()==null){
                unit="";
            }else {
                if (!vitalList.getUom().equalsIgnoreCase("NULL")){
                    unit=vitalList.getUom();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            unit="";
        }


        holder.value_uom.setText(vitalList.getValue().trim()+" "+unit);
    }

    @Override
    public int getItemCount() {
        return vital_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView observation_item_description,observation_date,observation_time,value_uom;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            observation_item_description=itemView.findViewById(R.id.observation_item_description);
            observation_date=itemView.findViewById(R.id.observation_date);
            observation_time=itemView.findViewById(R.id.observation_time);
            value_uom=itemView.findViewById(R.id.value_uom);
        }
    }
}
