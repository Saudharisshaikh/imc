package sa.med.imc.myimc.Questionaires.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.CompletedAssessmentAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentImpl;
import sa.med.imc.myimc.Questionaires.presenter.AssessmentPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.view.SignUpStep1Activity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

/*
History of past completed assessment list with results
 * Form names with result link
 * Completed API called
 */
public class CompletedFormsListActivity extends BaseActivity implements AssessmentViews {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv_history_assessment)
    RecyclerView rvHistoryAssessment;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
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
    CompletedAssessmentAdapter adapter;
    AssessmentPresenter presenter;
    int page = 0;
    boolean isLoading = false, loadingEnd = false;

    List<CompletedResponse.Assessment> assessmentList = new ArrayList<>();

    //Start activity from health summary appointment
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CompletedFormsListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(CompletedFormsListActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        CompletedFormsListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_forms_list);
        ButterKnife.bind(this);
        page = 0;
        isLoading = false;
        loadingEnd = false;
        assessmentList = new ArrayList<>();

        presenter = new AssessmentImpl(this, this);
        setUpRecyclerView();

        callAPI();

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            callAPI();
        });
    }

    // Call completed API and fetch completed assessment list
    void callAPI() {
        presenter.callGetListCompletedAssessment(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Completed forms list set up
    void setUpRecyclerView() {
        progressBar.setVisibility(View.GONE);
        adapter = new CompletedAssessmentAdapter(this, assessmentList);
        rvHistoryAssessment.setHasFixedSize(true);
        rvHistoryAssessment.setLayoutManager(new LinearLayoutManager(this));
        rvHistoryAssessment.setAdapter(adapter);
        /*
        * below function is used to display the form and result in the promiss result activity
        *
        * */
        adapter.setOnItemClickListener(position -> PromissResultsActivity.startActivityFromCompletedAssessment(CompletedFormsListActivity.this, assessmentList.get(position).getFormName(), assessmentList.get(position).getResult()));

        rvHistoryAssessment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvHistoryAssessment.getLayoutManager()).findLastCompletelyVisibleItemPosition();
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


    @OnClick({R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_try_again:
                page = 0;
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                page = 0;
                callAPI();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onGetAssessmentFirst(AssessmentResponse response) {

    }

    @Override
    public void onGetAssessmentNext(AssessmentResponse response) {

    }

    @Override
    public void onGetListAssessments(CompletedResponse response) {
        Log.e("abcd",new Gson().toJson(response));

        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (swipeToRefresh.isRefreshing()) {
            assessmentList.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getAssessments() != null) {
            if (page == 0)
                assessmentList.clear();

            assessmentList.addAll(response.getAssessments());
            loadingEnd = response.getAssessments().size() != 0;

            if (assessmentList.size() == 0) {
                rvHistoryAssessment.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
                rvHistoryAssessment.setVisibility(View.VISIBLE);
                page = page + 1;

//                int total_items = Integer.parseInt(response.getTotalItems());
                isLoading = false;
            }
        } else {
            rvHistoryAssessment.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        swipeToRefresh.setRefreshing(false);
        if (msg.equalsIgnoreCase("timeout")) {

            if (assessmentList.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
            } else {
                makeToast(getString(R.string.time_out_messgae));
            }
        } else {
            makeToast(msg);
        }
    }

    @Override
    public void onNoInternet() {
        if (assessmentList.size() == 0) {
            swipeToRefresh.setRefreshing(false);
            mainContent.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.GONE);
        } else
            Common.noInternet(this);
    }
}
