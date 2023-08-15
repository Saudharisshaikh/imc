package sa.med.imc.myimc.Profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.R;

public class InsuranceListAdapter extends RecyclerView.Adapter<InsuranceListAdapter.MyViewHolder> {

    Context context;
    List<ValidateResponse.InsuranceList> lists=new ArrayList<>();

    public InsuranceListAdapter(Context context, List<ValidateResponse.InsuranceList> lists2) {
        this.context = context;
//        this.lists = lists2;
        lists2.forEach(l -> {
            if (!l.getPayorDescription().equalsIgnoreCase("Self Pay")) {
                lists.add(l);
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.insurence_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ValidateResponse.InsuranceList insuranceList = lists.get(position);

        holder.ed_value_insurance.setText(insuranceList.getPayorDescription() + " (" + insuranceList.getCardNo() + ")\n" +
                insuranceList.getPlanDescription() + " (" + insuranceList.getPlanCode() + ")\n" +
                "Valid till " + insuranceList.getDateFrom());


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText ed_value_insurance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ed_value_insurance = itemView.findViewById(R.id.ed_value_insurance);
        }
    }
}
