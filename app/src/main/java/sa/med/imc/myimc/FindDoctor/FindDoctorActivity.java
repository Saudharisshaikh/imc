package sa.med.imc.myimc.FindDoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Calculators.CalculatorsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindDoctorActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lay_item_doc)
    RelativeLayout layItemDoc;
    @BindView(R.id.lay_item_by_specialization)
    RelativeLayout layItemBySpecialization;

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, FindDoctorActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FindDoctorActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.lay_item_doc, R.id.lay_item_by_specialization})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_item_doc:
                PhysiciansActivity.startActivity(this);
                break;

            case R.id.lay_item_by_specialization:
                CalculatorsActivity.startActivity(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        FindDoctorActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
