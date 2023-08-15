package sa.med.imc.myimc.MedicineDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.MedicineDetail.model.MedicineModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicineDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_medicine_desc)
    TextView tvMedicineDesc;
    @BindView(R.id.tv_medicine_name)
    TextView tvMedicineName;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_delivery_date)
    TextView tvDeliveryDate;
    @BindView(R.id.tv_delivery_number)
    TextView tvDeliveryNumber;
    @BindView(R.id.tv_value_description)
    TextView tvValueDescription;
    @BindView(R.id.tv_value_days)
    TextView tvValueDays;
    @BindView(R.id.tv_value_urgency)
    TextView tvValueUrgency;
    @BindView(R.id.tv_value_delived)
    TextView tvValueDelived;

    @BindView(R.id.iv_set_reminder)
    ImageView iv_set_reminder;
//    @BindView(R.id.tv_set_reminder)
//    TextView tv_set_reminder;


    MedicineModel data;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MedicineDetailActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Context activity, MedicineModel data) {
        Intent intent = new Intent(activity, MedicineDetailActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MedicineDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (SharedPreferencesUtils.getInstance(MedicineDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail_page);
        ButterKnife.bind(this);

        data = (MedicineModel) getIntent().getSerializableExtra("data");


        tvValueDescription.setText(data.getMedication_description());
        tvMedicineDesc.setText(data.getMedication_description());
        tvQuantity.setText(data.getQuantity());
        tvValueDays.setText(data.getDuration());
        String lang = SharedPreferencesUtils.getInstance(MedicineDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            tvValueUrgency.setText(data.getEnglish_instruction());
        }else {
            tvValueUrgency.setText(data.getArabic_instruction());
        }
        tvDeliveryNumber.setText(data.getPrescription_number());
        tvDeliveryDate.setText(data.getOrder_start_date());
        tvValueDelived.setText(data.getOrder_status());

//        if (data.getInActiveMed() != null) {
//            if (data.getInActiveMed().equalsIgnoreCase("1")) {
//                tv_set_reminder.setVisibility(View.GONE);
//            } else {
//        if (BuildConfig.DEBUG)
//            tv_set_reminder.setVisibility(View.VISIBLE);
//        else
//            tv_set_reminder.setVisibility(View.GONE);

//            }
//        }


    }

    @Override
    public void onBackPressed() {
        finish();
        MedicineDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

  //  @OnClick({R.id.iv_back, R.id.tv_set_reminder})
    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

//            case R.id.tv_set_reminder:
//                MedicineReminderActivity.startActivity(this, data);
//                break;
        }
    }
}
