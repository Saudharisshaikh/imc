package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

/**
 * Dependent MRN list adapter.
 */

public class DependentMrnsAdapter extends RecyclerView.Adapter<DependentMrnsAdapter.Viewholder> {
    List<ValidateResponse> list;
    List<Integer> images;
    String lang = "";

    Context context;


    public DependentMrnsAdapter(Context context, List<ValidateResponse> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, "");
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_mrn, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.tv_mrn.setText(list.get(position).getMrn());

        /*if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            holder.tv_name.setText(list.get(position).getFirst_name_ar() + " " + list.get(position).getMiddle_name_ar() + " " + list.get(position).getLast_name_ar() + " " + list.get(position).getFamily_name_ar());
        } else
            holder.tv_name.setText(list.get(position).getFirstName() + " " + list.get(position).getMiddleName() + " " + list.get(position).getLastName() + " " + list.get(position).getFamilyName());*/

        holder.tv_name.setText(list.get(position).getFirstName() + " " +  list.get(position).getLastName() );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_mrn)
        TextView tv_mrn;
        @BindView(R.id.tv_name)
        TextView tv_name;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
