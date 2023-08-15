package sa.med.imc.myimc.Records.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.view.activity.Lab.LabReportNewFragment;
import sa.med.imc.myimc.Records.view.activity.Lab.LabreportOldFragment;
import sa.med.imc.myimc.Records.view.activity.radilogy.RadiologyReportsNewFragment;
import sa.med.imc.myimc.Records.view.activity.radilogy.RadiologyReportsOldFragment;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.RadiologyListner;

public class RadiologyReportsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiology_reports);

        FragmentManager manager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframe, new RadiologyReportsNewFragment()).commit();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:

                        FragmentManager manager = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mainframe, new RadiologyReportsNewFragment()).commit();

                        break;
                    case 1:

                        FragmentManager manager1 = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.replace(R.id.mainframe, new RadiologyReportsOldFragment()).commit();

                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}