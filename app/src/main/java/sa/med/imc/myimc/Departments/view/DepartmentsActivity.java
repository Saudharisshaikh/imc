package sa.med.imc.myimc.Departments.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sa.med.imc.myimc.Adapter.DepartmentAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Departments.model.DepartmentDoctorResponse;
import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.Departments.presenter.DepartmentImpl;
import sa.med.imc.myimc.Departments.presenter.DepartmentsPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

import java.util.ArrayList;
import java.util.List;

/*
All Department list shown in this activity

 */
public class DepartmentsActivity extends BaseActivity implements DepartmentViews {

    ImageView ivBack;
    TextView toolbarTitle;
    RecyclerView rvDeparts;
    RelativeLayout mainContent;
    Button btTryAgain;
    RelativeLayout mainNoNet;
    Button btTryAgainTimeOut;
    RelativeLayout mainTimeOut;
    ImageView ivBackNo;
    ImageView ivSearch;
    CustomTypingEditText edSearch;
    TextView tvLoadMore;
    SwipeRefreshLayout swipeToRefresh;
    RelativeLayout mainNoDataTrans;

    int page = 0;
    boolean isLoading = false, isSearched = false;
    DepartmentResponse.Data responseData;
    List<DepartmentResponse.Department> list = new ArrayList<>();

    DepartmentAdapter adapter;
    DepartmentsPresenter presenter;

    boolean edit = false;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(DepartmentsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        DepartmentsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);
//        ButterKnife.bind(this);
        initView();
        setUpRecyclerView();

        presenter = new DepartmentImpl(this, this);
        callAPI(1);

//         Search department by name
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // call api to search clinic
                    page = 0;
                    callAPI(1);

                    return true;
                }

                return false;
            }
        });

        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    page = 0;
                    callAPI(1);
                }
            }
        });
        swipeToRefresh.setOnRefreshListener(() -> {
            tvLoadMore.setVisibility(View.GONE);
            callAPI(1);
        });


    }

    private void initView() {
         ivBack=findViewById(R.id.iv_back);
         toolbarTitle=findViewById(R.id.toolbar_title);
         rvDeparts=  findViewById(R.id.rv_departs);
         mainContent=findViewById(R.id.main_content);
         btTryAgain=findViewById(R.id.bt_try_again);
         mainNoNet=findViewById(R.id.main_no_net);
         btTryAgainTimeOut=findViewById(R.id.bt_try_again_time_out);
         mainTimeOut=findViewById(R.id.main_time_out);
         ivBackNo=findViewById(R.id.iv_back_no);
        ivSearch =findViewById(R.id.iv_search);
        edSearch=findViewById(R.id.ed_search);
        tvLoadMore=findViewById(R.id.tv_load_more);
        swipeToRefresh=findViewById(R.id.swipeToRefresh1);
        mainNoDataTrans =findViewById(R.id.main_no_data_trans);


        ivBack.setOnClickListener(mClickListener);
        btTryAgain.setOnClickListener(mClickListener);
        btTryAgainTimeOut.setOnClickListener(mClickListener);
        ivBackNo.setOnClickListener(mClickListener);
        ivSearch.setOnClickListener(mClickListener);
        tvLoadMore.setOnClickListener(mClickListener);



    }

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.iv_back:
                    onBackPressed();
                    break;

                case R.id.bt_try_again:
                    callAPI(1);
                    break;

                case R.id.bt_try_again_time_out:
                    callAPI(1);
                    break;

                case R.id.iv_back_no:
                    onBackPressed();
                    break;

                case R.id.iv_search:
                    if (edit) {
                        ivSearch.setImageResource(R.drawable.ic_search);
                        edSearch.setVisibility(View.GONE);
                        edit = false;
                        edSearch.clearFocus();

//                 Clear search and load all departments
                        if (isSearched) {
                            edSearch.setText("");
                            callAPI(1);
                        }

                    } else {
                        ivSearch.setImageResource(R.drawable.ic_close);
                        edSearch.setVisibility(View.VISIBLE);
                        edit = true;
                        edSearch.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
                    }
                    break;

                case R.id.tv_load_more:
                    if (responseData.getNextPageUrl() != null) {
                        if (responseData.getNextPageUrl().length() > 0) {
                            tvLoadMore.setEnabled(false);
                            callAPI(Integer.parseInt(responseData.getNextPageUrl().split("=")[1]));
                        }
                    }
                    break;
            }


        }
    };

    void callAPI(int page) {

        isSearched = edSearch.getText().toString().length() > 0;

        presenter.callAllDepartments(page, edSearch.getText().toString());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // Initialize Department list view and add adapter to display data,
    void setUpRecyclerView() {
        adapter = new DepartmentAdapter(this, list);
        rvDeparts.setHasFixedSize(true);
        rvDeparts.setLayoutManager(new LinearLayoutManager(this));
        rvDeparts.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(DepartmentsActivity.this, DepartmentDetailActivity.class);
            intent.putExtra("data", list.get(position));
            startActivity(intent);
        });

        rvDeparts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvDeparts.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {

                    if (!responseData.getLastPage().equals(responseData.getCurrentPage())) {

                        if (responseData.getNextPageUrl() != null) {
                            if (responseData.getNextPageUrl().length() > 0 && isLoading) {
                                tvLoadMore.setEnabled(false);
                                isLoading = false;
                                callAPI(Integer.parseInt(responseData.getNextPageUrl().split("=")[1]));

                            }
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onSuccess(DepartmentResponse response) {

        try {
            mainContent.setVisibility(View.VISIBLE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            ivBackNo.setVisibility(View.GONE);
            responseData = response.getData();
            mainNoDataTrans.setVisibility(View.GONE);
        }catch (NullPointerException ex){
            Toast.makeText(this, "1--->"+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
        if (responseData != null &&  responseData.getLastPage()!=null && responseData.getCurrentPage() !=null) {
            if (responseData.getLastPage().equals(responseData.getCurrentPage())) {
                tvLoadMore.setVisibility(View.GONE);
            } else {
                tvLoadMore.setVisibility(View.GONE);  // VISIBLE
            }
        }
        }catch (NullPointerException ex){
            Toast.makeText(this, "2--->"+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
//        try {
        if (response.getData().getData() != null && responseData.getCurrentPage() !=null) {


            if (responseData.getCurrentPage() == 1)
                list.clear();

            if (swipeToRefresh.isRefreshing()) {
                list.clear();
                swipeToRefresh.setRefreshing(false);
            }

            list.addAll(response.getData().getData());


            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                ivBackNo.setVisibility(View.VISIBLE);
                tvLoadMore.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
                isLoading = true;
                mainContent.setVisibility(View.VISIBLE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
            }
        } else {
            mainContent.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            ivBackNo.setVisibility(View.VISIBLE);

        }


        tvLoadMore.setEnabled(true);
        adapter.notifyDataSetChanged();

        if (responseData.getNextPageUrl() != null) {
            if (responseData.getNextPageUrl().length() > 0) {
                page = Integer.parseInt(responseData.getNextPageUrl().split("=")[1]);
            }
        }



//        makeToast("response",response.toString());

    }

    @Override
    public void onDoctorSuccess(DepartmentDoctorResponse response) {

    }

    @Override
    public void showLoading() {
        if (!swipeToRefresh.isRefreshing())
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
            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                ivBackNo.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.GONE);

            } else {
                makeToast(getString(R.string.time_out_messgae));
                isLoading = true;

            }

        } else if (msg.equalsIgnoreCase("Data Not Found")) {

            list.clear();
            adapter.notifyDataSetChanged();
            tvLoadMore.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.VISIBLE);
//            makeToast(msg);

        } else {
            makeToast(msg);
        }
    }

    @Override
    public void onNoInternet() {
        swipeToRefresh.setRefreshing(false);
//        if (list.size() == 0) {
        mainContent.setVisibility(View.GONE);
        mainNoNet.setVisibility(View.VISIBLE);
        mainTimeOut.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.VISIBLE);
//        } else {
//            Common.noInternet(this);
//            isLoading = true;
//        }
    }
}
