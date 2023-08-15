package sa.med.imc.myimc.Profile.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;

import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Notifications.view.NotificationsActivity;
import sa.med.imc.myimc.Physicians.AllDoctorFragment;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Profile.presenter.ProfileImpl;
import sa.med.imc.myimc.Profile.presenter.ProfilePresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.InputValidations;
import sa.med.imc.myimc.Utils.NonSwipeViewPager;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.PatientIdListner;


public class ProfileFragment extends Fragment implements ProfileViews, BottomDependentListDialog.BottomDailogListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    //    @BindView(R.id.tv_edit_profile)
//    TextView tvEditProfile;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_notifications)
    ImageView ivNotifications;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_switch_to_dependent)
    TextView tvSwitchToDependent;

    private ViewPagerAdapter mViewPagerAdapter;

    ProfilePresenter profilePresenter;
    BottomDependentListDialog bottomDependentListDialog;
    ProfileResponse profileResponse;
    Dialog sucDialog;
    String email = "";
    String update_email = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    BroadcastReceiver profile_update_broadcast_receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view); //razia.absolvetech@gmail.com
        email = "";

        profilePresenter = new ProfileImpl(this, getActivity());

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes")) {
            tvSwitchToDependent.setText(getString(R.string.switch_to_my));
            tvSwitchToDependent.setBackgroundResource(R.drawable.btn_request_appointment);
            tvSwitchToDependent.setVisibility(View.VISIBLE);

        }

        profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

        fcHospitalCheck(view);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(profile_update_broadcast_receiver, new IntentFilter(Constants.profile_update));


        //ivLogo.setVisibility(View.GONE);

        SharedPreferences sp = getContext().getSharedPreferences("location", Context.MODE_PRIVATE);
        String hospitalLogo_white = sp.getString("hospitalLogo_white",null);
        Picasso.get().load(hospitalLogo_white)
                .error(R.drawable.logo)
                .placeholder(R.drawable.placeholder_icon).fit().into(ivLogo);



        return view;
    }

    private void fcHospitalCheck(View view) {
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            ImageView iv_logo = view.findViewById(R.id.iv_logo);

            if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                iv_logo.setImageResource(R.drawable.icon_fc_logo_arabic_new_white);
            } else {
                iv_logo.setImageResource(R.drawable.icon_logo_fc_new_white);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_edit, R.id.iv_notifications, R.id.tv_switch_to_dependent})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_edit:
                editProfileDialog(profileResponse.getPatientProfile().getEmail(),
                        profileResponse.getUserDetails().getAddress()
                        , profileResponse.getUserDetails().getAddress());
                break;

            case R.id.iv_notifications:
                NotificationsActivity.startActivity(getActivity());
                break;

            case R.id.tv_switch_to_dependent:
                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes")) {
                    onClear(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_ID, ""));
                } else {
                    if (bottomDependentListDialog == null)
                        bottomDependentListDialog = BottomDependentListDialog.newInstance(profileResponse);

                    if (!bottomDependentListDialog.isAdded())
                        bottomDependentListDialog.show(getChildFragmentManager(), "DAILOG");
                }
                break;

        }
    }

    private void setupViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        mViewPagerAdapter.addFragment(PersonalInfoFragment.newInstance("", ""), getResources().getString(R.string.personal_info));
//        mViewPagerAdapter.addFragment(MedicinesFragment.newInstance("", ""), getResources().getString(R.string.medicines));
//        mViewPagerAdapter.addFragment(ManageBookingsFragment.newInstance("", ""), getResources().getString(R.string.manage_bookings));
        viewPager.setAdapter(mViewPagerAdapter);

        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        tabs.setTabTextColors(getResources().getColor(R.color.text_grey_color), getResources().getColor(R.color.colorPrimaryDark));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if (mParam1.length() > 0) {
//            tabs.getTabAt(1).select();
//            viewPager.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem(1);
//                }
//            }, 100);

//        } else {
//            tabs.getTabAt(0).select();
//            viewPager.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem(0);
//                }
//            }, 100);
//        }
    }

    @Override
    public void onGetProfile(ProfileResponse response) {
        profileResponse = response;
//        iv_edit.setVisibility(View.VISIBLE);
        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_USER, response.getUserDetails());


        String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        /*if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (response.getUserDetails().getMiddle_name_ar() != null)
                edName.setText(response.getUserDetails().getFirst_name_ar() + " " + response.getUserDetails().getMiddle_name_ar() + " " + response.getUserDetails().getLast_name_ar() + " " + response.getUserDetails().getFamily_name_ar());
            else
                edName.setText(response.getUserDetails().getFirst_name_ar() + " " + response.getUserDetails().getLast_name_ar() + " " + response.getUserDetails().getFamily_name_ar());

        } else {
            if (response.getUserDetails().getMiddleName() != null)
                edName.setText(response.getUserDetails().getFirstName() + " " + response.getUserDetails().getMiddleName() + " " + response.getUserDetails().getLastName() + " " + response.getUserDetails().getFamilyName());
            else
                edName.setText(response.getUserDetails().getFirstName() + " " + response.getUserDetails().getLastName() + " " + response.getUserDetails().getFamilyName());
        }*/
        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            edName.setText(response.getUserDetails().getFirstName() + " " + response.getUserDetails().getLastName());
        } else {
            edName.setText(response.getUserDetails().getArabic_first_name() + " " + response.getUserDetails().getArabic_last_name());
        }

        // if dependent list
        if (response.getDepandant() != null) {
            if (response.getDepandant().size() > 0) {
                tvSwitchToDependent.setVisibility(View.VISIBLE);

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes")) {
                    tvSwitchToDependent.setText(getString(R.string.switch_to_my));
                    tvSwitchToDependent.setBackgroundResource(R.drawable.btn_request_appointment);

                } else {
                    tvSwitchToDependent.setText(getString(R.string.switch_to_dependent));
                    tvSwitchToDependent.setBackgroundResource(R.drawable.btn_sign_up);

                }

            } else {

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_GUARDIAN, "no").equalsIgnoreCase("yes")) {
                    tvSwitchToDependent.setText(getString(R.string.switch_to_my));
                    tvSwitchToDependent.setBackgroundResource(R.drawable.btn_request_appointment);
                    tvSwitchToDependent.setVisibility(View.VISIBLE);

                } else {
                    tvSwitchToDependent.setVisibility(View.GONE);
                    tvSwitchToDependent.setBackgroundResource(R.drawable.btn_sign_up);
                    tvSwitchToDependent.setText(getString(R.string.switch_to_dependent));
                }

            }
        }

        tvEmail.setText(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_PATIENT_EMAIL, ""));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        String mail_verify = tvEmail.getText().toString();
        editor.putString("UPDATE_MAIL", mail_verify);
        Log.e("UPDATE_MAIL", "UPDATE_MAIL" + mail_verify);
        editor.apply();
//        if (!response.getUserDetails().getMobileNumber().equalsIgnoreCase("0"))
//            tvPhone.setText(response.getUserDetails().getMobileNumber());

        setupViewPager();

    }

    @Override
    public void onGetMedication(MedicationRespone response) {

    }

    @Override
    public void onUpdateProfile(SimpleResponse response) {
        sucDialog.dismiss();
        Common.makeToast(getActivity(), getResources().getString(R.string.profile_updated));

        try {
            Intent refresh = new Intent(Constants.Filter.UPDATE_HOME);
            refresh.putExtra("email", email);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(refresh);
        } catch (Exception e) {
            e.printStackTrace();
        }

        profilePresenter.callGetUserProfileApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    @Override
    public void showLoading() {
        Common.showDialog(getActivity());

    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        Common.makeToast(getActivity(), msg);
        iv_edit.setVisibility(View.GONE);

    }

    @Override
    public void onNoInternet() {
        Common.noInternet(getActivity());
        iv_edit.setVisibility(View.GONE);

    }

    public String updated_mail(String email) {

        Log.e("UPDATE_MAIL", "UPDATE_MAIL" + email);
        return email;
    }

    @Override
    public void onDone(String MRN, int pos) {
        switchingMRN(MRN, pos);
    }

    @Override
    public void onClear(String MRN) {
        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_GUARDIAN, "no");
        tvSwitchToDependent.setText(getString(R.string.switch_to_dependent));
        tvSwitchToDependent.setBackgroundResource(R.drawable.btn_sign_up);

        ValidateResponse validateResponse = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_MAIN);
        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_PATIENT_ID, validateResponse.getPatientId());
        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_MRN, validateResponse.getMrn());
        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_USER, SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_MAIN));

        Intent i1 = new Intent(getActivity(), MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        getActivity().finish();
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    // Switch to dependent dialog.
    private void switchingMRN(String MRN, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.switch_to_dependent_content, MRN));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_GUARDIAN, "yes");
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_MRN, MRN);
                ValidateResponse validateResponse = profileResponse.getDepandant().get(pos);

                new MyhttpMethod(getContext()).getPatientId(validateResponse.getMrn(), new PatientIdListner() {
                    @Override
                    public void onSuccess(String patientId) {

                        validateResponse.setPatientId(patientId);
                        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_PATIENT_ID, patientId);
                        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_USER, validateResponse);
                        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_USER_MAIN, profileResponse.getUserDetails());

                        tvSwitchToDependent.setText(getString(R.string.switch_to_my));
                        tvSwitchToDependent.setBackgroundResource(R.drawable.btn_request_appointment);

                        dialog.dismiss();

                        Intent i1 = new Intent(getActivity(), MainActivity.class);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getContext(), "Something went wrong ", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }

    //edit profile some data only email and address
    void editProfileDialog(String email, String address, String address_ar) {

        try {
            sucDialog = new Dialog(getActivity()); // Context, this, etc.
            sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            sucDialog.setContentView(R.layout.dialog_edit_profile);

            EditText et_email = sucDialog.findViewById(R.id.et_email);
            if (email != null)
                et_email.setText(email);

            EditText et_address = sucDialog.findViewById(R.id.et_address);
            if (address != null)
                et_address.setText(address);

            EditText et_address_ar = sucDialog.findViewById(R.id.et_address_ar);
            if (address != null)
                et_address_ar.setText(address_ar);


            ImageView iv_cancel = sucDialog.findViewById(R.id.iv_cancel);
            LinearLayout lay_btn_save = sucDialog.findViewById(R.id.lay_btn_save);

            sucDialog.setCancelable(false);

            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sucDialog.cancel();
//                    iv_edit.setVisibility(View.VISIBLE);

                }
            });

            lay_btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValid(et_email, et_address)) {
                        ProfileFragment.this.email = et_email.getText().toString();

                        profilePresenter.callUpdateProfileApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                et_email.getText().toString(), et_address.getText().toString(), et_address_ar.getText().toString(),
                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                    }
                }
            });

            sucDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isValid(EditText et_email, EditText et_address) {
        boolean valid = true;

        if (et_email.getText().toString().isEmpty()) {
            et_email.setError(getString(R.string.required));
            valid = false;
        } else if (!InputValidations.isInputNotEmail(et_email.getText().toString())) {
            et_email.setError(getString(R.string.invalid));
            valid = false;
        }
//
//        if (et_address.getText().toString().isEmpty()) {
//            et_address.setError(getString(R.string.required));
//            valid = false;
//        }
        return valid;
    }


}
