package sa.med.imc.myimc.Records.view.activity;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactUsActivity;
import sa.med.imc.myimc.Utils.Common;

public class ReportListActivity extends AppCompatActivity {

    LinearLayout lay_lab_reports,lay_radiology,lay_sick_leave,lay_operative,lay_discharhe,lay_cardiology,request_report;

    LinearLayout l1,l2,l3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        initView();


        String hospital_code=SharedPreferencesUtils.getInstance(this).getValue(HOSPITAL_CODE, "IMC");

    }

    private void initView() {
        lay_lab_reports=findViewById(R.id.lay_lab_reports);
        lay_radiology=findViewById(R.id.lay_radiology);
        lay_sick_leave=findViewById(R.id.lay_sick_leave);
        lay_operative=findViewById(R.id.lay_operative);
        lay_discharhe=findViewById(R.id.lay_discharhe);
        lay_cardiology=findViewById(R.id.lay_cardiology);
        request_report=findViewById(R.id.request_report);


        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);

        findViewById(R.id.iv_back_list).setOnClickListener(view -> finish());

        lay_lab_reports.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, LabReportsActivity.class));
        });

        lay_radiology.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, RadiologyReportsActivity.class));
        });

        lay_sick_leave.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, SickLeaveActivity.class));
        });

        lay_operative.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, OperativeRecordsActivity.class));
        });
        lay_discharhe.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, DischargeRecordsActivity.class));
        });
        lay_cardiology.setOnClickListener(view -> {
            startActivity(new Intent(ReportListActivity.this, CardiologyActivity.class));
        });
        request_report.setOnClickListener(view -> {
            Intent intent=new Intent(ReportListActivity.this,ContactUsActivity.class);
            intent.putExtra("type", "report");
            someActivityResultLauncher.launch(intent);

//            ContactUsActivity.startActivity(ReportListActivity.this, "request");
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Common.messageDailog(ReportListActivity.this,result.getData().getStringExtra("result"));
                    }
                }
            });
}