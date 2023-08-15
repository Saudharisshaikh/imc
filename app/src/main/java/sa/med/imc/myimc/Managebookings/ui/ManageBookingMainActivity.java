package sa.med.imc.myimc.Managebookings.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.Lab.LabReportNewFragment;
import sa.med.imc.myimc.Records.view.activity.Lab.LabreportOldFragment;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

public class ManageBookingMainActivity extends AppCompatActivity {
    public static boolean isUpdated = false;
    static TabLayout tabLayout;

    static String hospCode = "IMC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking_main);
        FragmentManager manager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframe, new ManageBookingFragment(new UrlWithURLDecoder().getBookings_all(), true)).commit();

        tabLayout = findViewById(R.id.tab_layout);

        hospCode = SharedPreferencesUtils.getInstance(this).getValue(Constants.HOSPITAL_CODE, "IMC");


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:

                        FragmentManager manager = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mainframe, new ManageBookingFragment(new UrlWithURLDecoder().getBookings_all(), true)).commit();

                        break;
                    case 1:

                        FragmentManager manager1 = getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction1 = manager1.beginTransaction();
                        transaction1.replace(R.id.mainframe, new ManageBookingFragment(new UrlWithURLDecoder().getBookings_old(), false)).commit();

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

        removeHistory();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (isUpdated) {
            setResult(Activity.RESULT_OK, returnIntent);
        }
//        else {
//            setResult(Activity.RESULT_CANCELED, returnIntent);
//
//        }
        finish();

    }

    public static void addHistory() {

        if(!hospCode.equals("KH")) {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void removeHistory() {
        if(!hospCode.equals("KH")) {
            tabLayout.setVisibility(View.GONE);
        }

    }

}