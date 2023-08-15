package sa.med.imc.myimc.HealthSummary.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import sa.med.imc.myimc.HealthSummary.view.VitalSignsFragment;
import sa.med.imc.myimc.HealthSummary.view.activity.vitalsigns.VitalSignsNewFragment;
import sa.med.imc.myimc.HealthSummary.view.activity.vitalsigns.VitalSignsOldFragment;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.cardiology.CardiologyReportNewFragment;
import sa.med.imc.myimc.Records.view.activity.cardiology.CardiologyReportOldFragment;

public class VitalSignsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);


        FragmentManager manager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframe, new VitalSignsNewFragment()).commit();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        FragmentManager manager1 = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.replace(R.id.mainframe, new VitalSignsNewFragment()).commit();


                        break;
                    case 1:
                        FragmentManager manager = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mainframe, new VitalSignsOldFragment()).commit();


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