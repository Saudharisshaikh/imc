package sa.med.imc.myimc.Physicians;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Login.LoginActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.adapter.PhysicianAdapter2;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.AllDoctoListener;
import sa.med.imc.myimc.Physicians.presenter.PhysicianPresenter;
import sa.med.imc.myimc.Physicians.view.BottomConfirmDialogNextAvailable;
import sa.med.imc.myimc.Physicians.view.BottomOptionDialog;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.DailogAlert;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.SelectDate;
import sa.med.imc.myimc.splash.SplashActivity;


public class AllDoctorFragment extends Fragment implements DailogAlert {

    Activity context;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_clinic)
    TextView tv_clinic;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_physicians)
    RecyclerView rvPhysicians;
    @BindView(R.id.tv_load_more)
    TextView tv_load_more;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.selected_date)
    TextView selected_date;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;

    @BindView(R.id.calenderBtn)
    ImageView calenderBtn;

    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;
    View v;
    boolean isFromLogin = false;

    int pos = -1, page = 0, total_items = 0;
    String depart_id = "", lang_id = "";
    boolean edit = false, isLoading = false, isSearched = false;
    PhysicianAdapter2 adapter;
    PhysicianPresenter physicianPresenter;
    List<PhysicianResponse.ProviderList> list = new ArrayList<>();
    BottomOptionDialog bottomOptionDialog;
    private boolean loadingEnd = false;
    BottomConfirmDialogNextAvailable mBottomConfirmDialogNextAvailable;
    List<String> specialityDescription = new ArrayList<>();
    List<String> specialityCode = new ArrayList<>();
    List<AllDoctorListModel.DoctorListBasedOnSpeciality> doctorListBasedOnSpecialities2 = new ArrayList<>();
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance(Locale.ENGLISH).getTime());

    String  searchKey="";

    BroadcastReceiver search_broadcast = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context1, Intent intent) {
            String key = intent.getStringExtra("key");
            searchKey=key;
            List<PhysicianResponse.ProviderList> result = new ArrayList<>();
            List<String> resultSpecialityDescription = new ArrayList<>();
            List<String> resultSpecialityCode = new ArrayList<>();

            List<AllDoctorListModel.DoctorListBasedOnSpeciality> doctorList = new ArrayList<>();

//            List<PhysicianResponse.ProviderList> result = list.stream()
//                    .filter(item -> item.getProviderDescription().toLowerCase().contains(key.toLowerCase()))
//                    .collect(Collectors.toList());

            for (int i = 0; i < list.size(); i++) {
                PhysicianResponse.ProviderList providerList = list.get(i);
                AllDoctorListModel.DoctorListBasedOnSpeciality doctorListBasedOnSpeciality = doctorListBasedOnSpecialities2.get(i);
                String lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

                if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                    if (providerList.getArabicProviderDescription().toLowerCase().contains(key.toLowerCase())) {
                        result.add(providerList);
                        doctorList.add(doctorListBasedOnSpeciality);
                        resultSpecialityDescription.add(doctorListBasedOnSpeciality.arabicSpecialityDescription);
                        resultSpecialityCode.add(doctorListBasedOnSpeciality.specialityCode);
                    }
                } else {
                    if (providerList.getProviderDescription().toLowerCase().contains(key.toLowerCase())) {
                        result.add(providerList);
                        doctorList.add(doctorListBasedOnSpeciality);
                        resultSpecialityDescription.add(doctorListBasedOnSpeciality.specialityDescription);
                        resultSpecialityCode.add(doctorListBasedOnSpeciality.specialityCode);
                    }
                }

            }


            if (result.size() > 0) {
                mainNoDataTrans.setVisibility(View.INVISIBLE);
            } else {
                mainNoDataTrans.setVisibility(View.VISIBLE);

            }
            Log.e("abcd", new Gson().toJson(list));
            Log.e("abcd", new Gson().toJson(result));

            adapter = new PhysicianAdapter2(context, result, resultSpecialityDescription, resultSpecialityCode,
                    AllDoctorFragment.this, timeStamp);
            rvPhysicians.setAdapter(adapter);
            adapter.setOnItemClickListener(new PhysicianAdapter2.OnItemClickListener() {
                @Override
                public void onViewProfile(int position) {
                    Intent intent = new Intent(getContext(), PhysicianFullDetailActivity.class);
                    intent.putExtra("code", result.get(position).getProviderCode());
                    intent.putExtra("data", new Gson().toJson(result.get(position)));
                    intent.putExtra("specialityCode", doctorList.get(position).getSpecialityCode());
                    intent.putExtra("specialityDescription", doctorList.get(position).getSpecialityDescription());
                    startActivity(intent);
                }

                @Override
                public void onBookAppointment(int position, boolean isTele) {
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_CLINIC_NAME,
                            doctorList.get(position).getSpecialityDescription());
                    AppointmentActivity.start(getActivity(),isTele, result.get(position),
                            doctorList.get(position).getSpecialityCode(),
                            doctorList.get(position).getSpecialityDescription(),
                            doctorList.get(position).getArabicSpecialityDescription());
                }

                @Override
                public void onBookNextAvailable(int position) {

                }
            });

        }
    };

    public AllDoctorFragment() {
        // Required empty public constructor
    }

    public AllDoctorFragment(Activity context, boolean isFromLogin1) {
        this.context = context;
        this.isFromLogin = isFromLogin1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(search_broadcast, new IntentFilter(Constants.physician_search));


        setUpPhysicianRecyclerView();
        loadAllDoctor();


        calenderBtn.setOnClickListener(v1 -> {
            Common.getDateFromPickerCU(getActivity(),getFragmentManager(), new SelectDate() {
                @Override
                public void onSelect(String dtae) {
                    timeStamp=dtae;
                    setUpPhysicianRecyclerView();
                    loadAllDoctor();
                }
            });


        });
    }


    private void loadAllDoctor() {

        new MyhttpMethod(context).callLoadDoctorByHospitalCode(SharedPreferencesUtils.getInstance().getValue(Constants.HOSPITAL_CODE, "IMC"), new AllDoctoListener() {
            @Override
            public void onSuccess(AllDoctorListModel response1) {
                list.clear();
                doctorListBasedOnSpecialities2.clear();
                specialityCode.clear();
                specialityDescription.clear();

                if (response1.getDoctorListBasedOnSpeciality().size() > 0) {
                    List<String> specialityDescriptionList = new ArrayList<>();
                    String lang = SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

                    List<AllDoctorListModel.DoctorListBasedOnSpeciality> doctorListBasedOnSpecialities = response1.getDoctorListBasedOnSpeciality();


                    Log.e("abcd", new Gson().toJson(doctorListBasedOnSpecialities));
                    for (int i = 0; i < doctorListBasedOnSpecialities.size(); i++) {


                        AllDoctorListModel.DoctorListBasedOnSpeciality doctorListBasedOnSpeciality = doctorListBasedOnSpecialities.get(i);


                        for (int i2 = 0; i2 < doctorListBasedOnSpeciality.getDoctorList().size(); i2++) {

                            PhysicianResponse.ProviderList providerList = new PhysicianResponse.ProviderList();

                            providerList.setProviderCode(doctorListBasedOnSpeciality.getDoctorList().get(i2).getProvider_code());
                            providerList.setProviderDescription(doctorListBasedOnSpeciality.getDoctorList().get(i2).getProvider_description());
                            providerList.setArabicProviderDescription(doctorListBasedOnSpeciality.getDoctorList().get(i2).getArabic_provider_description());


                            providerList.setPatientPortalDoctor(doctorListBasedOnSpeciality.getDoctorList().get(i2).getIs_patientportal_doctor());

                            if (providerList.isPatientPortalDoctor()) {
                                list.add(providerList);
                                doctorListBasedOnSpecialities2.add(doctorListBasedOnSpeciality);

                                specialityCode.add(doctorListBasedOnSpeciality.specialityCode);

                                if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                                    specialityDescription.add(doctorListBasedOnSpeciality.arabicSpecialityDescription);

                                } else {
                                    specialityDescription.add(doctorListBasedOnSpeciality.specialityDescription);

                                }
                            }

                        }


                    }

                } else {
                    rvPhysicians.setVisibility(View.GONE);
                }
                adapter.refresh(specialityDescription, specialityCode);

                if (!searchKey.isEmpty()){
                    Intent search = new Intent(Constants.physician_search);
                    search.putExtra("key", searchKey);
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(search);
                }
//                adapter = new PhysicianAdapter(context, list, "", specialityDescription, FindAllDoctorActivity.this);
//                rvPhysicians.setAdapter(adapter);

                Log.e("abcd", new Gson().toJson(list));
                if (list.size() < 1) {
                    mainNoDataTrans.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFaild(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialize Physician list view and add adapter to display data,
    void setUpPhysicianRecyclerView() {

        list = new ArrayList<>();
        adapter = new PhysicianAdapter2((Activity) context, list, specialityDescription, specialityCode, AllDoctorFragment.this,timeStamp);
        rvPhysicians.setHasFixedSize(true);
        rvPhysicians.setLayoutManager(new LinearLayoutManager(context));
        rvPhysicians.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhysicianAdapter2.OnItemClickListener() {
            @Override
            public void onViewProfile(int position) {
                if (isFromLogin) {
                    Intent i1 = new Intent(getContext(), LoginActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getContext(), PhysicianFullDetailActivity.class);
                    intent.putExtra("code", list.get(position).getProviderCode());
                    intent.putExtra("data", new Gson().toJson(list.get(position)));
                    intent.putExtra("specialityCode", doctorListBasedOnSpecialities2.get(position).getSpecialityCode());
                    intent.putExtra("specialityDescription", doctorListBasedOnSpecialities2.get(position).getSpecialityDescription());
                    startActivity(intent);
                }
            }

            @Override
            public void onBookAppointment(int position,boolean isTele) {
                if (isFromLogin) {
                    Intent i1 = new Intent(getContext(), LoginActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    getActivity().finish();
                } else {
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_CLINIC_NAME,
                            doctorListBasedOnSpecialities2.get(position).getSpecialityDescription());
                    AppointmentActivity.start(getActivity(),isTele, list.get(position),
                            doctorListBasedOnSpecialities2.get(position).getSpecialityCode(),
                            doctorListBasedOnSpecialities2.get(position).getSpecialityDescription(),
                            doctorListBasedOnSpecialities2.get(position).getArabicSpecialityDescription());
                }
            }

            @Override
            public void onBookNextAvailable(int position) {

//                startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });

        rvPhysicians.addOnScrollListener(

                new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvPhysicians.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (isLoading && loadingEnd) {
                        loadingEnd = false;
                        isLoading = false;
                    }
                }
            }
        });
    }


    @Override
    public void dailog(String content) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();

    }


}