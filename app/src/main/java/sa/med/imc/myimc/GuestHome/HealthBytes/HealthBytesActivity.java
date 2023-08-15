package sa.med.imc.myimc.GuestHome.HealthBytes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.HealthBytesAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthBytesActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv_health_bytes)
    RecyclerView rvHealthBytes;
    @BindView(R.id.no_result)
    TextView noResult;

    HealthBytesAdapter adapter;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, HealthBytesActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HealthBytesActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_bytes);
        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finish();
        HealthBytesActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    // Initialize HealthBytes list view and add adapter to display data,
    void setUpRecyclerView() {
        adapter = new HealthBytesAdapter(this, new ArrayList<String>());
        rvHealthBytes.setHasFixedSize(true);
        rvHealthBytes.setLayoutManager(new LinearLayoutManager(this));
        rvHealthBytes.setAdapter(adapter);
        adapter.setOnItemClickListener(new HealthBytesAdapter.OnItemClickListener() {
            @Override
            public void onReadMore(int position) {

            }
        });
    }

}
