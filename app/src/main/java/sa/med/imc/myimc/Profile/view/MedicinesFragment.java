package sa.med.imc.myimc.Profile.view;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.MedicinesAdapter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.MedicineDetail.MedicineDetailActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Profile.presenter.ProfileImpl;
import sa.med.imc.myimc.Profile.presenter.ProfilePresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

/**
 *
 */
public class MedicinesFragment extends Fragment implements ProfileViews {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rv_medicines)
    RecyclerView rvMedicines;
    MedicinesAdapter adapter;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
    int page = 0, total_items = 0;

    boolean isLoading = false;
    @BindView(R.id.mainContent)
    RelativeLayout mainContent;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;

    private String mParam1;
    private String mParam2;
    String booking_id = "";
    FragmentListener fragmentAdd;

    private OnFragmentInteractionListener mListener;
    ProfilePresenter profilePresenter;
    List<MedicationRespone.MedicationModel> medicationModels = new ArrayList<>();

    public MedicinesFragment() {
        // Required empty public constructor
    }

    public static MedicinesFragment newInstance(String param1, String param2) {
        MedicinesFragment fragment = new MedicinesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MedicinesFragment newInstance() {
        MedicinesFragment fragment = new MedicinesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicines, container, false);
        ButterKnife.bind(this, view);
         mainToolbar.setVisibility(View.GONE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        page = 0;
        total_items = 0;
        isLoading = false;

        profilePresenter = new ProfileImpl(this, getActivity());
        setUpMedicinesRecyclerView();
        setUserVisibleHint(true);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {
            if (profilePresenter != null)
                if (medicationModels.size() == 0)
                    profilePresenter.callGetMedication(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                            booking_id, Constants.RECORD_SET, String.valueOf(page),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                    profilePresenter.callGetBookingIdApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

        }
    }

    void callAPI() {
        profilePresenter.callGetMedication(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                booking_id, Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    @Override
    public void onGetProfile(ProfileResponse response) {

    }

    @Override
    public void onGetMedication(MedicationRespone response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if (response.getMedicationModels() != null) {
            if (page == 0)
                medicationModels.clear();
            medicationModels.addAll(response.getMedicationModels());

            if (medicationModels.size() == 0) {
                rvMedicines.setVisibility(View.GONE);
                mainNoData.setVisibility(View.VISIBLE);

            } else {
                rvMedicines.setVisibility(View.VISIBLE);
                if (response.getTotalItems() != null)
                    if (response.getTotalItems().length() > 0) {
                        total_items = Integer.parseInt(response.getTotalItems());

                        if (medicationModels.size() < Integer.parseInt(response.getTotalItems())) {
                            tvLoadMore.setVisibility(View.GONE);//VISIBLE
                            page = page + 1;
                            isLoading = true;

                        } else {
                            tvLoadMore.setVisibility(View.GONE);
                        }
                    }
            }
        } else {
            rvMedicines.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateProfile(SimpleResponse response) {

    }

//    @Override
//    public void onGetBookingId(LastBookingResponse response) {
//        if (response.getData() != null) {
//            if (response.getData().getId() != null) {
//                booking_id = String.valueOf(response.getData().getId());
//
//                profilePresenter.callGetMedication(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                        booking_id, Constants.RECORD_SET, String.valueOf(page));
//            }
//        } else {
//            mainNoNet.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
//
//            rvMedicines.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//        }
//    }

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
        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                if (medicationModels.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    mainTimeOut.setVisibility(View.VISIBLE);
                } else {
                    isLoading = true;
                    Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
                }

            } else {
                Common.makeToast(getActivity(), msg);
                progressBar.setVisibility(View.GONE);

            }
    }


    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);

        if (medicationModels.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
        } else {
            isLoading = true;
            Common.noInternet(getActivity());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.tv_load_more:
                callAPI();
                break;

            case R.id.bt_try_again:
                progressBar.setVisibility(View.VISIBLE);
                callAPI();
//                profilePresenter.callGetBookingIdApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

                break;

            case R.id.bt_try_again_time_out:
                progressBar.setVisibility(View.VISIBLE);
                callAPI();
//                profilePresenter.callGetBookingIdApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Initialize Medicines list view and add adapter to display data,
    void setUpMedicinesRecyclerView() {
        medicationModels = new ArrayList<>();

        adapter = new MedicinesAdapter(getContext(), medicationModels);
        rvMedicines.setHasFixedSize(true);
        rvMedicines.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedicines.setAdapter(adapter);
        adapter.setOnItemClickListener(new MedicinesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                MedicineDetailActivity.startActivity(getActivity(), medicationModels.get(position));
            }
        });

        rvMedicines.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvMedicines.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (medicationModels.size() < total_items && isLoading) {
                        isLoading = false;
                        callAPI();
                    }
                }
            }
        });
    }
}
