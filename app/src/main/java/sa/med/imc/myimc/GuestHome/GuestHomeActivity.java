package sa.med.imc.myimc.GuestHome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Clinics.view.ClinicsFragment;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.view.PhysiciansFragment;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Settings.SettingActivity;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.LocaleHelper;

import static sa.med.imc.myimc.Utils.LocaleHelper.onAttach;

/*Guest Dashboard have
   * Call us
   * Find Doctor
   * Clinics
   * Health Tips
   * Departments
   * Location Map
   * And other settings
 */

public class GuestHomeActivity extends BaseActivity implements FragmentListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.main_container_wrapper_guest)
    FrameLayout mainContainerWrapperGuest;
    @BindView(R.id.tv_english)
    TextView tvEnglish;
    @BindView(R.id.tv_arabic)
    TextView tvArabic;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    Fragment homeFragment, physiciansFragment, clinicsFragment;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        GuestHomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_user);
        ButterKnife.bind(this);

        setUpHomeFragment();

        navView.setNavigationItemSelectedListener(this);
        setUpDrawer();
        setUpHeaderValues();
        navView.getMenu().getItem(0).setChecked(true);

        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            setViewsChanges(false);
        } else {
            setViewsChanges(true);
        }

        changeLanguage(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, ""));



    }


    // set up navigation list
    private void setUpDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return false;
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    // setup navigation header values
    void setUpHeaderValues() {
        View hview = navView.getHeaderView(0);
        TextView name = hview.findViewById(R.id.tv_name);
        TextView tv_email = hview.findViewById(R.id.tv_email);
        tv_email.setVisibility(View.GONE);

        //ImageView iv_profile = hview.findViewById(R.id.iv_profile);
        if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            String nameAr = SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FIRST_NAME_AR, getString(R.string.guest_user)) + " " +
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FATHER_NAME_AR, getString(R.string.guest_user)) + " " +
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FAMILY_NAME_AR, getString(R.string.guest_user));
            name.setText(nameAr);
        } else {
            String name1 = SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FIRST_NAME, getString(R.string.guest_user)) + " " +
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FATHER_NAME, getString(R.string.guest_user)) + " " +
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.GUEST.FAMILY_NAME, getString(R.string.guest_user));
            name.setText(name1);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_dashboard:
                setUpHomeFragment();
                break;

//            case R.id.nav_retail:
//                RetailFacilitiesActivity.startActivity(this);
//                break;

            case R.id.nav_settings:
                SettingActivity.startSetting(GuestHomeActivity.this);
                break;

            case R.id.nav_about_us:
                Intent intent = new Intent(this, UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_ABOUT_US);
                startActivity(intent);

                break;

            case R.id.nav_help:
                ContactOptionsActivity.startActivity(this);
                break;

            case R.id.nav_logout:
                askFirst();
                break;

        }

        runOnUiThread(() -> drawerLayout.closeDrawer(GravityCompat.START));
        return true;
    }


    // Logout ask dialog for confirmation.
    private void askFirst() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuestHomeActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.are_sure_logout));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPreferencesUtils.getInstance(GuestHomeActivity.this).clearAll(GuestHomeActivity.this);

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(GuestHomeActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();


    }

    // on device back press drawer closed if opened and fragment pull from back stack.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressed(Common.CONTAINER_FRAGMENT);
        }
    }

    // Open dashboard Home
    private void setUpHomeFragment() {
        Common.CONTAINER_FRAGMENT = "HomeFragment";
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);

        if (homeFragment == null)
            homeFragment = new GuestHomeFragment();
        String backStateName = homeFragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container_wrapper_guest, homeFragment, backStateName);
        fragmentTransaction.addToBackStack(backStateName).commit();
        mainToolbar.setVisibility(View.VISIBLE);

    }

    // load particular fragment.
    private void startFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag(backStateName);
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).commit();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            Fragment currentFragment = fragmentManager.findFragmentByTag(backStateName);
            if (currentFragment != null) {
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.addToBackStack(backStateName).commit();
            } else {
                fragmentTransaction.replace(R.id.main_container_wrapper_guest, fragment);
                fragmentTransaction.addToBackStack(backStateName).commit();
            }
        } else {
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.main_container_wrapper_guest, fragment);
            fragmentTransaction.addToBackStack(backStateName).commit();

        }
    }

    void setViewsChanges(boolean eng) {

        if (eng) {
            tvArabic.setBackgroundResource(R.drawable.btn_ar_unselected);
            tvArabic.setTextColor(getResources().getColor(R.color.text_blue_color));

            tvEnglish.setBackgroundResource(R.drawable.btn_en_selected);
            tvEnglish.setTextColor(getResources().getColor(R.color.colorWhite));

            tvEnglish.setEnabled(false);

        } else {
            tvArabic.setBackgroundResource(R.drawable.btn_ar_selected);
            tvArabic.setTextColor(getResources().getColor(R.color.colorWhite));

            tvEnglish.setBackgroundResource(R.drawable.btn_en_unselected);
            tvEnglish.setTextColor(getResources().getColor(R.color.text_blue_color));

            tvArabic.setEnabled(false);

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(onAttach(base, SharedPreferencesUtils.getInstance(base).getValue(Constants.KEY_LANGUAGE, "")));
    }

    // change language ask dialog for confirmation.
    private void askLanguageChange(String lang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuestHomeActivity.this);
        builder.setCancelable(false);

        try {
            if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                builder.setTitle("Language!");
                builder.setMessage("Do you want to change language?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        changeLanguage(lang);
                        callIntent();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            } else {
                builder.setTitle("اللغة!");
                builder.setMessage("هل ترغب في تغيير اللغة؟");

                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        changeLanguage(lang);
                        callIntent();
                    }
                });
                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(GuestHomeActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to LTR
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }

    // change language
    void changeLanguage(String lan) {
        if (lan.equalsIgnoreCase(Constants.ARABIC)) {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ARABIC);
            LocaleHelper.setLocale(this, Constants.ARABIC);
            setViewsChanges(false);
        } else {
            SharedPreferencesUtils.getInstance(getApplicationContext()).setValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
            LocaleHelper.setLocale(this, Constants.ENGLISH);
            setViewsChanges(true);
        }
    }

    // after change language
    void callIntent() {
        Intent i1 = new Intent(GuestHomeActivity.this, GuestHomeActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        finish();
    }


    @Override
    public void openProfileMedicineFragment(String value) {

    }

    @Override
    public void openPhysicianFragment(String value, String clinic_id) {

        physiciansFragment = PhysiciansFragment.newInstance();

        Bundle args = new Bundle();
        args.putString("param2", clinic_id);
        physiciansFragment.setArguments(args);

        startFragment(physiciansFragment);
        Common.CONTAINER_FRAGMENT = value;
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openClinicFragment(String value) {
        if (clinicsFragment == null)
            clinicsFragment = ClinicsFragment.newInstance();

        Bundle args = new Bundle();

        args.putString("param1", "ClinicFragment");
        Common.CONTAINER_FRAGMENT = "ClinicFragment";

        args.putString("param2", "");
        clinicsFragment.setArguments(args);

        startFragment(clinicsFragment);
        mainToolbar.setVisibility(View.GONE);
    }

    @Override
    public void openPaymentInfoFragment(Serializable value, String key) {

    }


    @Override
    public void openTelrFragment(Serializable book,Serializable res, String value) {

    }

    @Override
    public void openMedicineFragment(String type, String value) {

    }


    @Override
    public void backPressed(String type) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (type) {

            case "BaseFragment":
                exitDialog();
                break;

            case "HomeFragment":
                exitDialog();
                break;

            case "PhysiciansFragment":
                setUpHomeFragment();
                break;

            case "PhysiciansFragmentC":
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    Common.CONTAINER_FRAGMENT = "ClinicFragment";

                } else {
                    openClinicFragment("ClinicFragment");
                }

                break;

            default:
                setUpHomeFragment();
                break;

        }
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

    // exit alert dialog.
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuestHomeActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.exit_alert));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.CONTAINER_FRAGMENT = "BaseFragment";
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(GuestHomeActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (SharedPreferencesUtils.getInstance(GuestHomeActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();
    }

    @OnClick({R.id.tv_english, R.id.tv_arabic})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_english:
                askLanguageChange(Constants.ENGLISH);
                break;

            case R.id.tv_arabic:
                askLanguageChange(Constants.ARABIC);
                break;
        }
    }

}
