package sa.med.imc.myimc.Physicians;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.adapter.PhysicianAdapter2;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.PhysicianImpl;
import sa.med.imc.myimc.Physicians.presenter.PhysicianPresenter;
import sa.med.imc.myimc.Physicians.view.BottomConfirmDialogNextAvailable;
import sa.med.imc.myimc.Physicians.view.BottomOptionDialog;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.Physicians.view.PhysicianViews;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.DailogAlert;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.interfaces.SelectDate;

public class FindDoctorFragment extends Fragment implements PhysicianViews, DailogAlert {


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
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;
    @BindView(R.id.selected_date)
    TextView selected_date;
    @BindView(R.id.calenderBtn)
    ImageView calenderBtn;
    View v;

    public String mParam1 = "", specialityCode = "", noShowStatus = "";
    int pos = -1, page = 0, total_items = 0;
    String depart_id = "", lang_id = "";
    boolean edit = false, isLoading = false, isSearched = false;
    PhysicianAdapter2 adapter;
    PhysicianPresenter physicianPresenter;
    List<PhysicianResponse.ProviderList> list = new ArrayList<>();
    CustomDailogBuilder progressDialog;
    BottomOptionDialog bottomOptionDialog;
    private boolean loadingEnd = false;
    BottomConfirmDialogNextAvailable mBottomConfirmDialogNextAvailable;
    String specialityDescription,arabicSpecialityDescription;
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance(Locale.ENGLISH).getTime());

    public FindDoctorFragment() {
        // Required empty public constructor
    }

    public FindDoctorFragment(String specialityCode1, String specialityDescription1,String arabicSpecialityDescription) {
        // Required empty public constructor
        this.specialityCode = specialityCode1;
        this.specialityDescription = specialityDescription1;
        this.arabicSpecialityDescription = arabicSpecialityDescription;
        Log.e("abcd", specialityCode1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_find_doctor, container, false);
        ButterKnife.bind(this, view);


        setUpPhysicianRecyclerView();
        progressDialog = new CustomDailogBuilder(getContext());

        physicianPresenter = new PhysicianImpl(this, getActivity());
        isSearched = false;
        edSearch.setText("");

        depart_id = "";
        lang_id = "";

        page = 0;
        callAPI();

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                page = 0;
//                callAPI();
                Common.hideSoftKeyboard(getActivity());

                return true;
            }
            return false;
        });

        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    page = 0;
                    callAPI();
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(() -> {
            depart_id = "";
            lang_id = "";
            page = 0;
            tv_load_more.setVisibility(View.GONE);
            callAPI();
        });


        calenderBtn.setOnClickListener(v1 -> {
            Common.getDateFromPickerCU(getActivity(),getFragmentManager(), new SelectDate() {
                @Override
                public void onSelect(String dtae) {
                    timeStamp=dtae;
                    setUpPhysicianRecyclerView();
                    callAPI();
                }
            });


        });
        return view;

        // Inflate the layout for this fragment
    }


    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_filter, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.tv_load_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {

//            case R.id.iv_back:
//                if (fragmentAdd != null)
//                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
//                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                    edSearch.clearFocus();


                    if (isSearched) {
                        edSearch.setText("");
                        page = 0;
                        callAPI();
                    }

                } else {
                    ivSearch.setImageResource(R.drawable.ic_close);
                    edSearch.setVisibility(View.VISIBLE);
                    edit = true;
                    edSearch.requestFocus();
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                break;

            case R.id.iv_filter:
                if (bottomOptionDialog == null)
                    bottomOptionDialog = BottomOptionDialog.newInstance();

                if (!bottomOptionDialog.isAdded())
                    bottomOptionDialog.show(getChildFragmentManager(), "DAILOG");
                break;

            case R.id.bt_try_again:
                page = 0;
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                page = 0;
                callAPI();
                break;

            case R.id.tv_load_more:
                tv_load_more.setEnabled(false);
                callAPI();
                break;
        }
    }


    // call API to get doctors
    void callAPI() {
        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
            physicianPresenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), specialityCode, edSearch.getText().toString(), lang_id, depart_id, Constants.RECORD_SET, String.valueOf(page), Constants.GUEST_TYPE,
                    SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.SELECTED_HOSPITAL, 0));

        } else
//            physicianPresenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), mParam2, edSearch.getText().toString(), lang_id, depart_id, Constants.RECORD_SET, String.valueOf(page), Constants.GUEST_TYPE,
//                    SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.SELECTED_HOSPITAL, 0));
//            PIYUSH
            physicianPresenter.callGetSearchDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), specialityCode, edSearch.getText().toString(), lang_id, depart_id, Constants.RECORD_SET, String.valueOf(page), "",
                    SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    // Initialize Physician list view and add adapter to display data,
    void setUpPhysicianRecyclerView() {

        String lang = Constants.ENGLISH;
        lang = SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        list = new ArrayList<>();

        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            adapter = new PhysicianAdapter2(getActivity(), list, specialityDescription, specialityCode, this,timeStamp);
        }else {
            adapter = new PhysicianAdapter2(getActivity(), list, arabicSpecialityDescription, specialityCode, this,timeStamp);
        }
        rvPhysicians.setHasFixedSize(true);
        rvPhysicians.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPhysicians.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhysicianAdapter2.OnItemClickListener() {
            @Override
            public void onViewProfile(int position) {
                Intent intent = new Intent(getContext(), PhysicianFullDetailActivity.class);
                intent.putExtra("code", list.get(position).getProviderCode());
                intent.putExtra("data", new Gson().toJson(list.get(position)));
                intent.putExtra("specialityCode", specialityCode);
                intent.putExtra("specialityDescription", specialityDescription);
                startActivity(intent);

            }

            @Override
            public void onBookAppointment(int position,boolean isTele) {
                pos = position;
                if (list.get(pos).getProviderCode() != null) {
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_CLINIC_NAME,
                            specialityDescription);
                    Log.e("frac",specialityCode+" "+specialityDescription);
                    AppointmentActivity.start(getActivity(),isTele, list.get(position), specialityCode, specialityDescription,arabicSpecialityDescription);

                } else
                    setNoServicesDialog(getActivity());
            }

            @Override
            public void onBookNextAvailable(int position) {
                pos = position;
                if (list.get(position).getProviderCode() != null) {
                    if (mBottomConfirmDialogNextAvailable == null)
                        mBottomConfirmDialogNextAvailable = BottomConfirmDialogNextAvailable.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list.get(pos));
                    bundle.putSerializable("session", list.get(position).getProviderCode());

                    mBottomConfirmDialogNextAvailable.setArguments(bundle);

                    if (!mBottomConfirmDialogNextAvailable.isAdded())
                        mBottomConfirmDialogNextAvailable.show(getChildFragmentManager(), "DAILOG");
                } else
                    setNoServicesDialog(getActivity());
            }
        });

        rvPhysicians.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        callAPI();
                    }
                }
            }
        });
    }

    @Override
    public void onGetPhysicianList(PhysicianResponse response) {
        list.clear();

        specialityCode = response.getSpecialityCode();
        specialityDescription = response.getSpecialityDescription();
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        noShowStatus = response.getNo_show_status();
        mainNoDataTrans.setVisibility(View.GONE);

        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (depart_id.length() > 0 || lang_id.length() > 0)
            ivFilter.setImageResource(R.drawable.ic_filter_green);
        else
            ivFilter.setImageResource(R.drawable.ic_filter);


        if (response.getProviderList().size() > 0) {
            for (int i = 0; i < response.getProviderList().size(); i++) {
                PhysicianResponse.ProviderList providerList = response.getProviderList().get(i);
                if (providerList.isPatientPortalDoctor()) {
                    list.add(providerList);
                }
            }

//            if (page == 0)
//                list.clear();
//
////            list.addAll(response.getProviderList());
//            loadingEnd = response.getProviderList().size() != 0;
//
//            if (list.size() == 0) {
//                rvPhysicians.setVisibility(View.GONE);
////                ivSearch.setVisibility(View.VISIBLE);
//                mainNoDataTrans.setVisibility(View.VISIBLE);
//
//            }
//            else {
//
//                isLoading = true;
//                rvPhysicians.setVisibility(View.VISIBLE);
////                ivSearch.setVisibility(View.VISIBLE);
//
//                if (response.getProviderList().size() > 0) {
//                    total_items = response.getProviderList().size();
//                    page = page + 1;
//                }
//
//            }
        } else {
            rvPhysicians.setVisibility(View.GONE);
//            ivSearch.setVisibility(View.VISIBLE);
        }
//
//        if (mParam2.length() > 0) {
//            if (SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
//                tv_clinic.setText(list.get(0).getClincNameAr());
//            else
//                tv_clinic.setText(list.get(0).getClinicNameEn());
//
//            ivFilter.setVisibility(View.GONE);
//        }


        tv_load_more.setEnabled(true);
        adapter.refresh(specialityDescription,specialityCode);

//        PIYUSH TRACKER
        /*if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getService() != null) {
                    physicianPresenter.callGetNextAvailableDateTime(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            list.get(i).getDocCode(), list.get(i).getClinicCode(), list.get(i).getService().getId(), list.get(i).getDeptCode(), i,
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                }
            }
        }*/
//        if (list.size()==0){
//            getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
//        }else {
//            getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
//        }
        if (list.size() < 1) {
            mainNoDataTrans.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetServiceList(DrServiceResponse response) {

    }

    @Override
    public void onGetPhysicianProfile(PhysicianDetailResponse response) {

    }


    @Override
    public void showLoading() {
        if (!swipeToRefresh.isRefreshing())
            progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onFail(String msg) {
        swipeToRefresh.setRefreshing(false);
        tv_load_more.setEnabled(true);

        if (msg.equalsIgnoreCase("timeout")) {
            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.GONE);

            } else {
                Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
                isLoading = true;
            }

        } else {
            Common.makeToast(getActivity(), msg);
        }
    }

    @Override
    public void onNoInternet() {
        swipeToRefresh.setRefreshing(false);
        tv_load_more.setEnabled(true);

        if (list.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainNoDataTrans.setVisibility(View.GONE);

        } else {
            Common.noInternet(getActivity());
            isLoading = true;
        }
    }

    // If doctor's service is null
    void setNoServicesDialog(Activity activity) {
        String phone = "";
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert))
                .setMessage(activity.getString(R.string.no_service_content, phone))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(dlg -> {
            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        alertDialog.show();
    }


    @Override
    public void onGetCMSPhysician(PhysicianCompleteDetailCMSResponse response) {

    }

    @Override
    public void onGetNextAvailableTIme(NextTimeResponse response, int pos) {
//        PIYUSH TRACKER
        /*if (pos < list.size()) {
            if (response.getData() != null) {
                if (response.getData().size() > 0) {
                    list.get(pos).setDate(response.getData().get(0).getSessionDateShort());
                    list.get(pos).setTime(response.getData().get(0).getSessionTimeShort());
                    list.get(pos).setSessionId(String.valueOf(response.getData().get(0).getSessionId()));
                    list.get(pos).setSession(response.getData().get(0));
                } else {
                    list.get(pos).setDate(null);
                    list.get(pos).setTime(null);
                    list.get(pos).setSessionId("0");
                }

            } else {
                list.get(pos).setDate(null);
                list.get(pos).setTime(null);
                list.get(pos).setSessionId("0");
            }
            adapter.notifyItemChanged(pos);
        }*/

    }


    @Override
    public void dailog(String content) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


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