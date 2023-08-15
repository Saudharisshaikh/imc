package sa.med.imc.myimc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.HealthSummary.model.SimpleNameIDModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

/**
 * Vital Signs list.
 */

public class VitalSignsAdapter extends RecyclerView.Adapter<VitalSignsAdapter.Viewholder> {
    List<ReadingResponse.ReadingModel> list;
    Context context;
    int pos_select = -1;
    String type = "";

    public VitalSignsAdapter(Context context, List<ReadingResponse.ReadingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_vital_signs, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        position=holder.getAdapterPosition();
        if(list.size()>position) {
            ReadingResponse.ReadingModel model = list.get(position);
            holder.tv_reading_date.setText(Common.getConvertDateTime(model.getReadingDate()));
            List<SimpleNameIDModel> list = new ArrayList<>();

            // title
            if (SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                if (model.getEpsiodeLocationAr() != null)
                    holder.tv_title.setText(context.getResources().getString(R.string.vital_patient) + " - " + model.getEpsiodeLocationAr());
            } else {
                if (model.getEpsiodeLocationEn() != null)
                    holder.tv_title.setText(context.getResources().getString(R.string.vital_patient) + " - " + model.getEpsiodeLocationEn());

            }

// Height
            if (model.getHeight() > 0.0) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.height), model.getHeight() + " cm", R.drawable.ic_height));
            }

// Weight
            if (model.getWeight() > 0.0) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.weight), model.getWeight() + " kg", R.drawable.ic_weight));
            }

// BMI
            if (model.getBmi() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.bmi), "" + model.getBmi(), R.drawable.ic_bmi));
            }

// Body Temperature
            if (model.getBodyTempture() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.temperature), model.getBodyTempture() + "°C", R.drawable.ic_temprature));
            }

// Pulse Rate
            if (model.getPluseRate() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.pulse_rate), "" + model.getPluseRate(), R.drawable.ic_pulse_rate));

            }

// Blood Pressure
            if (model.getBloodPressure() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.bp), "" + model.getBloodPressure(), R.drawable.ic_blood_pressure));
            }

// Respiratory Rate
            if (model.getRespiratoryRate() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.respiratory), "" + model.getRespiratoryRate(), R.drawable.ic_respiratory));
            }

// Oxygen saturation
            if (model.getOxSaturation() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.ox_gen), model.getOxSaturation() + "%", R.drawable.ic_oxygen));
            }

//Blood Sugar
            if (model.getBloodGlucose() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.bloos_sugar), "" + model.getBloodGlucose(), R.drawable.ic_blood_sugar));
            }

// Notes
            if (model.getNotes() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.notes), "" + model.getNotes(), R.drawable.ic_note));
            }

// BSA
            if (model.getBsa() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.bsa), "" + model.getBsa(), R.drawable.ic_height));
            }
// IBW
            if (model.getIbw() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.ibw), "" + model.getIbw(), R.drawable.ic_weight));
            }
// BEE
            if (model.getBee() != null) {
                list.add(new SimpleNameIDModel(context.getResources().getString(R.string.bee), "" + model.getBee(), R.drawable.ic_bee));
            }

            GridRecyclerViewAdapter adapter = new GridRecyclerViewAdapter(context, list);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            holder.gridView.setLayoutManager(staggeredGridLayoutManager);
            holder.gridView.setHasFixedSize(true);
            holder.gridView.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_reading_date)
        TextView tv_reading_date;

        @BindView(R.id.gridView)
        RecyclerView gridView;
        @BindView(R.id.gridLayout)
        GridLayout gridLayout;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
