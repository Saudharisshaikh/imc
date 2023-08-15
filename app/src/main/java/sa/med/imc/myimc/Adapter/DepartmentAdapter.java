package sa.med.imc.myimc.Adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Departments list.
 */

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.Viewholder> {
    List<DepartmentResponse.Department> list;
    Activity context;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;
    String lang;

    public DepartmentAdapter(Activity context, List<DepartmentResponse.Department> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_deprtment, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            holder.tv_department_name.setText(Html.fromHtml(list.get(position).getTitleAr()));
            holder.tv_content.setText(Html.fromHtml(list.get(position).getDescriptionAr(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tv_department_name.setText(Html.fromHtml(list.get(position).getTitleEn()));
            holder.tv_content.setText(Html.fromHtml(list.get(position).getDescriptionEn(), Html.FROM_HTML_MODE_LEGACY));
        }
        Picasso.get().load(list.get(position).getImage())
                .resize(Common.getScreenWidth(context), Common.getScreenWidth(context) - 50)
                .centerCrop().into(holder.iv_depart);


        holder.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onReadMore(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.lay_item)
        LinearLayout lay_item;
        @BindView(R.id.tv_department_name)
        TextView tv_department_name;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_depart)
        ImageView iv_depart;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onReadMore(int position);
    }
}
