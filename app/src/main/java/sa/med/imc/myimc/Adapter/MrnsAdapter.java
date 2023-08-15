package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MRN list adapter while log in with ID.
 */

public class MrnsAdapter extends RecyclerView.Adapter<MrnsAdapter.Viewholder> {
    List<LoginResponse.UsersList> list;
    List<Integer> images;
    String lang = "";

    Context context;


    public MrnsAdapter(Context context, List<LoginResponse.UsersList> list) {
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
        holder.tv_mrn.setText(list.get(position).getId());

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            holder.tv_name.setText(list.get(position).getFNameAr() + " " + list.get(position).getMNameAr() + " " + list.get(position).getLNameAr() + " " + list.get(position).getFamilyNameAr());
        } else
            holder.tv_name.setText(list.get(position).getFNameEn() + " " + list.get(position).getMNameEn() + " " + list.get(position).getLNameEn() + " " + list.get(position).getFamilyNameEn());

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
