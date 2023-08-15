package sa.med.imc.myimc.Clinics.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.ClinicsAdapter;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.presenter.ClinicImpl;
import sa.med.imc.myimc.Clinics.presenter.ClinicPresenter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Clinics list searched according to time slot.
 * Clinics name and a picture displayed.
 */
public class ClinicsFragment extends Fragment implements ClinicViews {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_clinics)
    RecyclerView rvClinics;
    @BindView(R.id.tv_load_more)
    TextView tv_load_more;

    ClinicsAdapter adapter;
    boolean edit = false;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;

    int page = 0, total_items = 0;
    boolean isLoading = false, isSearched = false;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    // TODO: Rename and change types of parameters
    private String mParam1;

    ClinicPresenter presenter;
    List<ClinicResponse.SpecialityList> list = new ArrayList<>();
    List<ClinicResponse.SpecialityList> listNew=new ArrayList<>();

    boolean isSearch=false;
    public ClinicsFragment() {
        // Required empty public constructor
    }

    FragmentListener fragmentAdd;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public static ClinicsFragment newInstance() {
        ClinicsFragment fragment = new ClinicsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clinic, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            // Clinic open form Dashboard then show back button
            if (mParam1.equalsIgnoreCase("ClinicFragmentC")) {
                ivLogo.setVisibility(View.GONE);
                ivBack.setVisibility(View.VISIBLE);
            }
            // Clinic open from Home tab then hind back and show logo
            else if (mParam1.equalsIgnoreCase("ClinicFragment")) {
                ivLogo.setVisibility(View.GONE);

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE))
                    ivBack.setVisibility(View.VISIBLE);
                else
                    ivBack.setVisibility(View.GONE);

            }
        }
        edSearch.setText("");
        edit = false;
        isSearched = false;
        ivSearch.setImageResource(R.drawable.ic_search);
        edSearch.setVisibility(View.GONE);


        page = 0;
        presenter = new ClinicImpl(this, getActivity());
        callAPI();
        setUpClinicsRecyclerView();

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            listNew = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                ClinicResponse.SpecialityList specialityList = list.get(i);
                if (specialityList.getSpecialityDescription().toLowerCase().contains(edSearch.getText().toString().toLowerCase()) |
                        specialityList.getArabicSpecialityDescription().toLowerCase().contains(edSearch.getText().toString().toLowerCase())) {
                    listNew.add(specialityList);
                }
            }
            adapter = new ClinicsAdapter(getActivity(), listNew);
            rvClinics.setHasFixedSize(true);
            rvClinics.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvClinics.setAdapter(adapter);

            // call api to search clinic
//                page = 0;
//                callAPI();
            isSearch=true;
            Common.hideSoftKeyboard(getActivity());

            return true;

        });

        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0 && edit) {
                    page = 0;
                    callAPI();
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            tv_load_more.setVisibility(View.GONE);
            callAPI();
        });


        return view;
    }


    @OnClick({R.id.iv_back, R.id.iv_search, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.tv_load_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                    edSearch.clearFocus();

                    // Clear search and load all clinics
                    // Check if search box empty or not
                    if (isSearched) {
                        edSearch.setText("");
                        page = 0;
                        callAPI();
                    }

                } else {
                    edSearch.setText("");
                    ivSearch.setImageResource(R.drawable.ic_close);
                    edSearch.setVisibility(View.VISIBLE);
                    edit = true;
                    edSearch.requestFocus();
                    edSearch.requestFocus();
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                break;

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;

            case R.id.tv_load_more:
                tv_load_more.setEnabled(false);

                callAPI();
                break;
        }
    }

    //Call API to get records
    void callAPI() {

        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;


        presenter.callGetAllClinics(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""), "", "", edSearch.getText().toString(), Constants.RECORD_SET
                , String.valueOf(page), SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Initialize clinics list view and add adapter to display data,
    void setUpClinicsRecyclerView() {
        list = new ArrayList<>();
        adapter = new ClinicsAdapter(getActivity(), list);
        rvClinics.setHasFixedSize(true);
        rvClinics.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvClinics.setAdapter(adapter);
        rvClinics.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isSearch){
                    if (fragmentAdd != null)
                        SharedPreferencesUtils.getInstance().setValue(Constants.KEY_CLINIC_NAME, listNew.get(position).getSpecialityDescription());
                    fragmentAdd.openPhysicianFragment("PhysiciansFragmentC", listNew.get(position).getSpecialityCode());
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_CLINIC_NAME_AR, listNew.get(position).getArabicSpecialityDescription());

                }else {
                    if (fragmentAdd != null)
                        SharedPreferencesUtils.getInstance().setValue(Constants.KEY_CLINIC_NAME, list.get(position).getSpecialityDescription());
                    fragmentAdd.openPhysicianFragment("PhysiciansFragmentC", list.get(position).getSpecialityCode());
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_CLINIC_NAME_AR, list.get(position).getArabicSpecialityDescription());

                }

            }
        }));

        rvClinics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvClinics.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (list.size() < total_items && isLoading) {
                        isLoading = false;
                        callAPI();
                    }
                }
            }
        });
    }

    @Override
    public void onGetClinicsList(ClinicResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getSpecialityList() != null) {
            if (page == 0)
                list.clear();

            list.addAll(response.getSpecialityList());
            adapter.setAllData();

            if (list.size() == 0) {
                rvClinics.setVisibility(View.GONE);
//                ivSearch.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
                rvClinics.setVisibility(View.VISIBLE);
//                ivSearch.setVisibility(View.VISIBLE);
                if (response.getSpecialityList() != null)
                    if (response.getSpecialityList().size() > 0) {
                        total_items = response.getSpecialityList().size();

                        if (list.size() < response.getSpecialityList().size()) {
                            page = page + 1;
                            isLoading = true;
                        }
                    }
            }
        } else {
            rvClinics.setVisibility(View.GONE);
//            ivSearch.setVisibility(View.VISIBLE);
        }
        tv_load_more.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetSearchClinicsList(ClinicResponse response) {
        if (response.getSpecialityList() != null) {
            list.clear();
            list.addAll(response.getSpecialityList());
            if (list.size() == 0) {
                onFail(getString(R.string.no_result));
            }
        } else {
            onFail(getString(R.string.no_result));
        }
        tv_load_more.setVisibility(View.GONE);
        tv_load_more.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (!swipeToRefresh.isRefreshing())
            Common.showDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        swipeToRefresh.setRefreshing(false);
        tv_load_more.setEnabled(true);

        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                if (list.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoDataTrans.setVisibility(View.GONE);

                } else {
                    if (getActivity() != null)
                        Common.makeToast(getActivity(), getActivity().getString(R.string.time_out_messgae));
                    isLoading = true;
                }

            } else {
                if (getActivity() != null)
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
            mainNoDataTrans.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(getActivity());
            isLoading = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
