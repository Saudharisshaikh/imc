package sa.med.imc.myimc.splash;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.WelcomeActivity;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {

    Activity activity;
    List<HospitalModel.HospitalList> hospitalListList;

    public HospitalAdapter(Activity activity, List<HospitalModel.HospitalList> hospitalListList) {
        this.activity = activity;
        this.hospitalListList = hospitalListList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_hosp, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            HospitalModel.HospitalList hospitalList=hospitalListList.get(position);

            moveToNextScreen(position+1,hospitalList.getHospital_code());
        });

    }

    @Override
    public int getItemCount() {
        return hospitalListList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView hos_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hos_img=itemView.findViewById(R.id.hos_img);
        }
    }

    private void moveToNextScreen(int selectedHospital,String hospital_code) {
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.SELECTED_HOSPITAL, selectedHospital);
        SharedPreferencesUtils.getInstance(activity).setValue(HOSPITAL_CODE, hospital_code);
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
