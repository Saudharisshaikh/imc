package sa.med.imc.myimc.Adapter;

import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
        import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.splash.SplashActivity;

public class SplashAdapter extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater = null;

    public interface SplashAdapterInterface{
        public void updateSelection(int selectedHospital,String hospitalCode);
    }

    private SplashAdapterInterface updateSelection;

    public SplashAdapter(Context context,
                           List<? extends Map<String, ?>> data, int resource, String[] from,
                           int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_splash, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        String image = data.get("hospitalLogo").toString();

        ImageView hospitalLogo = vi.findViewById(R.id.hospitalLogo);

        Picasso.get().load(image)
                .error(R.drawable.error_icon)
                .placeholder(R.drawable.placeholder_icon).fit().into(hospitalLogo);

        hospitalLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = mContext.getSharedPreferences("location", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("hospitalId", data.get("hospitalId").toString());
                editor.putString("hospitalCode", data.get("hospitalCode").toString());
                editor.putString("hospitalName", data.get("hospitalName").toString());
                editor.putString("hospitalName_ar", data.get("hospitalName_ar").toString());
                editor.putString("hospitalLogo", data.get("hospitalLogo").toString());
                editor.putString("hospitalLargeImage", data.get("hospitalLargeImage").toString());
                editor.putString("hospitalLogo_white", data.get("hospitalLogo_white").toString());
                editor.putString("specialMessage", data.get("specialMessage").toString());
                editor.putString("specialMessage_ar", data.get("specialMessage_ar").toString());


                editor.commit();


                ((SplashActivity) mContext).updateSelection(Integer.parseInt(data.get("hospitalId").toString()),data.get("hospitalCode").toString());


            }
        });

        return vi;
    }


}