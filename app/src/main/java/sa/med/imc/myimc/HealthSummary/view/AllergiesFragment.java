package sa.med.imc.myimc.HealthSummary.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.HealthSummaryAdapter;
import sa.med.imc.myimc.HealthSummary.model.AllergyResponse;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.HealthSummary.presenter.HealthImpl;
import sa.med.imc.myimc.HealthSummary.presenter.HealthPresenter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllergiesFragment extends Fragment implements HealthViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv_health_summary)
    RecyclerView rvHealthSummary;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
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
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
    @BindView(R.id.iv_back_no)
    ImageView ivBackNo;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    List<AllergyResponse.AllergyModel> list = new ArrayList<>();
    HealthSummaryAdapter adapter;
    FragmentListener fragmentAdd;

    HealthPresenter presenter;

    public AllergiesFragment() {
        // Required empty public constructor
    }

    public static AllergiesFragment newInstance() {
        AllergiesFragment fragment = new AllergiesFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allergies, container, false);
        ButterKnife.bind(this, view);
        list = new ArrayList<>();

        ValidateResponse user=SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_USER);
        String patientId = user.getPatientId();
        String mrn = user.getMrn();

        Log.e("abcd",mrn+"  "+patientId);

        presenter = new HealthImpl(this, getActivity());
        presenter.callGetAllAllergies(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                patientId, mrn);

        swipeToRefresh.setOnRefreshListener(() -> {
            tvLoadMore.setVisibility(View.GONE);
            presenter.callGetAllAllergies(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    patientId, mrn);

        });


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.iv_back_no, R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.iv_back_no:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.tv_load_more:
                break;

            case R.id.bt_try_again:
                break;

            case R.id.bt_try_again_time_out:
                break;
        }
    }

    // Initialize Health Summary list view and add adapter to display data,
    void setUpHealthSummaryRecyclerView() {
        adapter = new HealthSummaryAdapter(getActivity(), list);
        rvHealthSummary.setHasFixedSize(true);
        rvHealthSummary.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHealthSummary.setAdapter(adapter);

    }


    @Override
    public void onSuccess(AllergyResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.GONE);

        swipeToRefresh.setRefreshing(false);
        Log.e("abcd",response.getData().size()+"");
        if (response.getData() != null) {
            list.clear();
            list.addAll(response.getData());

            if (list.size() == 0) {
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
//                ivBackNo.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.VISIBLE);
            }

        } else {
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.VISIBLE);
//            ivBackNo.setVisibility(View.VISIBLE);

        }
        setUpHealthSummaryRecyclerView();

    }

    @Override
    public void onSuccessReadings(ReadingResponse response) {

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
        swipeToRefresh.setRefreshing(false);
        if (msg.equalsIgnoreCase("timeout")) {

            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.GONE);
                ivBackNo.setVisibility(View.VISIBLE);
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
            ivBackNo.setVisibility(View.VISIBLE);
            mainNoDataTrans.setVisibility(View.GONE);
        } else
            Common.noInternet(getActivity());
    }

}
