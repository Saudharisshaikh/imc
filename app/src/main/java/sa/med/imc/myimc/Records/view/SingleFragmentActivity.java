package sa.med.imc.myimc.Records.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Calculators.BMIFragment;
import sa.med.imc.myimc.Calculators.BMRFragment;
import sa.med.imc.myimc.Calculators.IdealBodyWeightFragment;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.ViewReportActivity;
import sa.med.imc.myimc.healthcare.HealthCareFragment;
import sa.med.imc.myimc.medprescription.view.PrescriptionsFragment;

public class SingleFragmentActivity extends BaseActivity implements FragmentListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;


    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity, String type, String e_id) {
        Intent intent = new Intent(activity, SingleFragmentActivity.class);
        intent.putExtra(type, "dsfdsfsd");
        intent.putExtra("type", Constants.TYPE.REPORT_TYPE);
        intent.putExtra("e_id", e_id);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String type, String e_id, String id) {
        Intent intent = new Intent(activity, SingleFragmentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("e_id", e_id);

        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String type) {
        Intent intent = new Intent(activity, SingleFragmentActivity.class);
        intent.putExtra(type, type);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(SingleFragmentActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        SingleFragmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equalsIgnoreCase(Constants.TYPE.REPORT_TYPE)) {

            Fragment lMSRecordsFragment = LMSRecordsFragment.newInstance();
            Bundle args = new Bundle();
            if (getIntent().hasExtra(Constants.TYPE.RECORD_LAB)) {
                args.putString("param1", Constants.TYPE.RECORD_LAB);
                args.putString("param2", getIntent().getStringExtra("e_id"));
                lMSRecordsFragment.setArguments(args);
                startFragment(lMSRecordsFragment);

            } else if (getIntent().hasExtra(Constants.TYPE.RECORD_RADIO)) {
                args.putString("param1", Constants.TYPE.RECORD_RADIO);
                args.putString("param2", getIntent().getStringExtra("e_id"));
                lMSRecordsFragment.setArguments(args);
                startFragment(lMSRecordsFragment);

            } else if (getIntent().hasExtra(Constants.TYPE.RECORD_SICK)) {
                args.putString("param1", Constants.TYPE.RECORD_SICK);
                args.putString("param2", getIntent().getStringExtra("e_id"));
                lMSRecordsFragment.setArguments(args);
                startFragment(lMSRecordsFragment);

            } else if (getIntent().hasExtra(Constants.TYPE.RECORD_OPERATIVE)) {
                Fragment mOperativeRecordsFragment = OperativeRecordsFragment.newInstance();
                Bundle args1 = new Bundle();
                args1.putString("param1", Constants.TYPE.RECORD_OPERATIVE);
                args1.putString("param2", getIntent().getStringExtra("e_id"));
                mOperativeRecordsFragment.setArguments(args1);
                startFragment(mOperativeRecordsFragment);
            } else if (getIntent().hasExtra(Constants.TYPE.RECORD_DISCHARGE)) {
                Fragment mDischargeRecordsFragment = DischargeRecordsFragment.newInstance();
                Bundle args1 = new Bundle();
                args1.putString("param1", Constants.TYPE.RECORD_DISCHARGE);
                args1.putString("param2", getIntent().getStringExtra("e_id"));
                mDischargeRecordsFragment.setArguments(args1);
                startFragment(mDischargeRecordsFragment);
            } else if (getIntent().hasExtra(Constants.TYPE.RECORD_CARDIC)) {
                Fragment mCardioRecordsFragment = CardioRecordsFragment.newInstance();
                Bundle args1 = new Bundle();
                args1.putString("param1", Constants.TYPE.RECORD_CARDIC);
                args1.putString("param2", getIntent().getStringExtra("e_id"));
                mCardioRecordsFragment.setArguments(args1);
                startFragment(mCardioRecordsFragment);
            }


        }
        else if (getIntent().hasExtra(Constants.TYPE.HHC)) {
            startFragment(HealthCareFragment.newInstance());
        }
        else if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equalsIgnoreCase(getString(R.string.bmi))) {
            // open BMI fragment
            startFragment(BMIFragment.newInstance("", ""));
        } else if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equalsIgnoreCase(getString(R.string.bmr))) {
            // open BMR fragment
            startFragment(BMRFragment.newInstance("", ""));
        } else if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equalsIgnoreCase(getString(R.string.ideal_body_weight))) {
            // open IBW fragment
            startFragment(IdealBodyWeightFragment.newInstance());
        } else if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equalsIgnoreCase(getString(R.string.medication))) {
            mainToolbar.setVisibility(View.VISIBLE);
            startFragment(PrescriptionsFragment.newInstance("", getIntent().getStringExtra("e_id")));
        }
    }


    public void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        fragmentTransaction.commit();
    }

    void addFragment(String accsessionNum){
        Intent i = new Intent(this, ViewReportActivity.class);
        i.putExtra("accsessionNum",accsessionNum);
        startActivity(i);
    }


    @Override
    public void openProfileMedicineFragment(String value) {

    }

    @Override
    public void openPhysicianFragment(String value, String clinic_id) {

    }

    @Override
    public void openClinicFragment(String value) {

    }



    @Override
    public void openPaymentInfoFragment(Serializable value, String key) {

    }


    @Override
    public void openTelrFragment(Serializable book, Serializable res, String value) {

    }

    @Override
    public void openMedicineFragment(String type, String value) {

    }

    @Override
    public void backPressed(String type) {
        onBackPressed();
    }

    @Override
    public void openLMSRecordFragment(String value, String type, String episodeId) {

    }

    @Override
    public void startTask(int time) {

    }

    @Override
    public void openHealthSummary(String value) {

    }

    @Override
    public void openFitness(String value) {

    }

    @Override
    public void openWellness(String value) {

    }

    @Override
    public void openBodyMeasurement(String value) {

    }

    @Override
    public void openActivity(String value) {

    }

    @Override
    public void openHeatAndVitals(String value) {

    }

    @Override
    public void openSleepCycle(String value) {

    }

    @Override
    public void openAllergies(String value) {

    }

    @Override
    public void openVitalSigns(String value) {

    }

    @Override
    public void checkLocation() {

    }

    @Override
    public void onBackPressed() {

        finish();
        SingleFragmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
