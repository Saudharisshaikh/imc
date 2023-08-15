package sa.med.imc.myimc.Calculators;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.CalculatorsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Records.view.SingleFragmentActivity;

public class CalculatorsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_calculators)
    RecyclerView rvCalculators;
    @BindView(R.id.no_result)
    TextView noResult;
    boolean edit = false;
    CalculatorsAdapter adapter;
    List<String> list;
    List<Integer> images;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CalculatorsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalculatorsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculators);
        ButterKnife.bind(this);
        setUpSpecialitiesRecyclerView();
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                } else {
                    ivSearch.setImageResource(R.drawable.ic_close);
                    edSearch.setVisibility(View.VISIBLE);
                    edit = true;
                }
                break;
        }
    }

    // Initialize Calculators list view and add adapter to display data,
    void setUpSpecialitiesRecyclerView() {
        setUpData();
        adapter = new CalculatorsAdapter(this, list, images);
        rvCalculators.setHasFixedSize(true);
        rvCalculators.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvCalculators.setAdapter(adapter);

        adapter.setOnItemClickListener(new CalculatorsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                SingleFragmentActivity.startActivity(CalculatorsActivity.this, list.get(position),"","");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        CalculatorsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    void setUpData() {
        list = new ArrayList<>();
        images = new ArrayList<>();

        list.add(getString(R.string.bmi));
        images.add(R.drawable.ic_bmi);

        list.add(getString(R.string.bmr));
        images.add(R.drawable.ic_bmr);

        list.add(getString(R.string.vital_capacity));
        images.add(R.drawable.ic_breath);

        list.add(getString(R.string.ideal_body_weight));
        images.add(R.drawable.ic_weight);

        list.add(getString(R.string.sleep_cycle));
        images.add(R.drawable.ic_sleep_cycle);

        list.add(getString(R.string.heart_rate));
        images.add(R.drawable.ic_cardiology_icon);

//        list.add("Cardiology");
//        images.add(R.drawable.ic_cardiology_icon);
//
//        list.add("Dermatology");
//        images.add(R.drawable.ic_dermatology_icon);
//
//        list.add("Digestive Diseases");
//        images.add(R.drawable.ic_digestive_icon);
//
//        list.add("Family Medicine");
//        images.add(R.drawable.ic_family_icon);
//
//        list.add("Ear Nose & Throat");
//        images.add(R.drawable.ic_ear_nose_throat_icon);
//
//        list.add("General Practice");
//        images.add(R.drawable.ic_general_icon);
//
//        list.add("Gynecology");
//        images.add(R.drawable.ic_gynecology_icon);
//
//        list.add("Pediatrics");
//        images.add(R.drawable.ic_pediatrics);
//
//        list.add("Eye Specialist");
//        images.add(R.drawable.ic_eye_specialist_icon);
//
//        list.add("General Surgery");
//        images.add(R.drawable.ic_general_surgery);
//
//        list.add("Dental Care");
//        images.add(R.drawable.ic_dental_care);
//
//        list.add("Bone & joints");
//        images.add(R.drawable.ic_bone_icon);

    }
}
