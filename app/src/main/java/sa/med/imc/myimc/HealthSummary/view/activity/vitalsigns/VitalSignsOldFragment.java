package sa.med.imc.myimc.HealthSummary.view.activity.vitalsigns;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.VitalSignsAdapter;
import sa.med.imc.myimc.HealthSummary.model.AllergyResponse;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.HealthSummary.presenter.HealthImpl;
import sa.med.imc.myimc.HealthSummary.presenter.HealthPresenter;
import sa.med.imc.myimc.HealthSummary.view.BottomFilterReadingDialog;
import sa.med.imc.myimc.HealthSummary.view.HealthViews;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

public class VitalSignsOldFragment extends Fragment implements HealthViews, BottomFilterReadingDialog.BottomDialogListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.rv_vital_signs)
    RecyclerView rvVitalSigns;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    FragmentListener fragmentAdd;
    VitalSignsAdapter adapter;
    int page = 1;
    boolean isLoading = false, loadingEnd = false;
    String booking_id = "", from = "", to = "";

    HealthPresenter presenter;
    List<ReadingResponse.ReadingModel> list = new ArrayList<>();

    BottomFilterReadingDialog bottomFilterReadingDialog;



    public VitalSignsOldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vital_signs_old, container, false);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.GONE);
        ivFilter.setVisibility(View.VISIBLE);
        page = 1;
        isLoading = false;
        loadingEnd = false;
        booking_id = "";
        from = "";
        to = "";

        setUpVitalsRecyclerView();

        presenter = new HealthImpl(this, getActivity());
        callAPI();

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 1;
            callAPI();
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.iv_filter, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.iv_filter:
                if (bottomFilterReadingDialog == null)
                    bottomFilterReadingDialog = BottomFilterReadingDialog.newInstance();

                if (!bottomFilterReadingDialog.isAdded())
                    bottomFilterReadingDialog.show(getChildFragmentManager(), "DAILOG");
                break;

            case R.id.bt_try_again:
                page = 1;
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                page = 1;
                callAPI();
                break;
        }
    }

    // Initialize Vital Signs list view and add adapter to display data,
    void setUpVitalsRecyclerView() {
        adapter = new VitalSignsAdapter(getActivity(), list);
        rvVitalSigns.setHasFixedSize(true);
        rvVitalSigns.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvVitalSigns.setAdapter(adapter);

        rvVitalSigns.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvVitalSigns.getLayoutManager()).findLastCompletelyVisibleItemPosition();
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

    void callAPI() {

        presenter.callGetAllReadings(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), String.valueOf(page));

    }

    @Override
    public void onSuccess(AllergyResponse response) {

    }

    @Override
    public void onSuccessReadings(ReadingResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (booking_id.length() > 0 || from.length() > 0 || to.length() > 0)
            ivFilter.setImageResource(R.drawable.ic_filter_green);
        else
            ivFilter.setImageResource(R.drawable.ic_filter);

        if (response.getReadingModels() != null) {
            if (page == 1)
                list.clear();

            for (int i = 0; i < response.getReadingModels().size(); i++) {
                ReadingResponse.ReadingModel model = response.getReadingModels().get(i);
                if (model.getBodyTempture() != null || model.getPluseRate() != null ||
                        model.getBloodGlucose() != null || model.getOxSaturation() != null ||
                        model.getBloodPressure() != null || model.getBmi() != null ||
                        model.getBsa() != null || model.getBee() != null ||
                        model.getIbw() != null || model.getRespiratoryRate() != null)
                    list.add(model);
            }

            loadingEnd = response.getReadingModels().size() != 0;

            if (list.size() == 0) {
                rvVitalSigns.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
                rvVitalSigns.setVisibility(View.VISIBLE);
                page = page + 1;

//                if (response.getTotal_items() != null)
                int total_items = Integer.parseInt(response.getTotal_items());
                isLoading = total_items > list.size();

            }
        } else {
            rvVitalSigns.setVisibility(View.GONE);
        }

//        if (mParam2.length() > 0)
//            ivFilter.setVisibility(View.INVISIBLE);
//        else
//            ivFilter.setVisibility(View.VISIBLE);
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
        if (msg.equalsIgnoreCase("timeout")) {

            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
            } else {
                Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
            }

        } else {
            Common.makeToast(getActivity(), msg);
        }
    }

    @Override
    public void onNoInternet() {
        if (list.size() == 0) {
            swipeToRefresh.setRefreshing(false);
            mainContent.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.GONE);
        } else
            Common.noInternet(getActivity());
    }

    @Override
    public void onDone(String booking_id1, String from1, String to1) {
        booking_id = booking_id1;
        from = from1;
        to = to1;

        page = 1;
        callAPI();
    }

    @Override
    public void onClear(String text) {
        if (booking_id.length() > 0 || from.length() > 0 || to.length() > 0) {
            booking_id = "";
            from = "";
            to = "";

            page = 1;
            callAPI();
        }
    }
}