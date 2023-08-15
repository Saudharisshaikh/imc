package sa.med.imc.myimc.HealthTips.view;

import android.app.Activity;
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

import sa.med.imc.myimc.Adapter.HealthTipsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.HealthTips.model.HealthTipsResponse;
import sa.med.imc.myimc.HealthTips.presenter.HealthTipImpl;
import sa.med.imc.myimc.HealthTips.presenter.HealthTipPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthTipsActivity extends BaseActivity implements CategoryFilterDialog.BottomDailogListener, HealthTipViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_health_tips)
    RecyclerView rvHealthTips;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;

    RelativeLayout mainContent;

    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.iv_back_no)
    ImageView ivBackNo;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    boolean edit = false, isSearched = false;
    boolean isLoading = false;
    CategoryFilterDialog categoryFilterDialog;
    HealthTipsAdapter adapter;
    HealthTipsResponse.Data responseData;
    List<HealthTipsResponse.HealthModel> list = new ArrayList<>();
    HealthTipPresenter presenter;
    String category_id = "";

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, HealthTipsActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected Context getActivityContext() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(HealthTipsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        HealthTipsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);
        ButterKnife.bind(this);
        presenter = new HealthTipImpl(this, this);
        mainContent=findViewById(R.id.main_content);

        setUpRecyclerView();

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callAPI(1);
                    return true;
                }

                return false;
            }
        });

        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    callAPI(1);
                }
            }
        });
        swipeToRefresh.setOnRefreshListener(() -> {
            category_id = "";
            callAPI(1);
        });

        callAPI(1);
    }

    @OnClick({R.id.mayo_link, R.id.iv_back, R.id.iv_search, R.id.iv_filter, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.iv_back_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.mayo_link:
                if (SharedPreferencesUtils.getInstance(HealthTipsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC))
                    WebViewActivity.startActivity(this, WebUrl.MAYO_CLINIC_AR_URL);
                else
                    WebViewActivity.startActivity(this, WebUrl.MAYO_CLINIC_URL);

                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                    edSearch.clearFocus();

                    // check if search box empty or not
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

            case R.id.iv_filter:
                if (categoryFilterDialog == null)
                    categoryFilterDialog = CategoryFilterDialog.newInstance();

                if (!categoryFilterDialog.isAdded())
                    categoryFilterDialog.show((getSupportFragmentManager()), "DAILOG");
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
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        HealthTipsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Initialize Health tips list view and add adapter to display data,
    void setUpRecyclerView() {
        adapter = new HealthTipsAdapter(this, list);
        rvHealthTips.setHasFixedSize(true);
        rvHealthTips.setLayoutManager(new LinearLayoutManager(this));
        rvHealthTips.setAdapter(adapter);
        adapter.setOnItemClickListener(new HealthTipsAdapter.OnItemClickListener() {
            @Override
            public void onSingleClick(int position) {
                String videoID = list.get(position).getYoutubeUrl().replace("https://www.youtube.com/embed/", "");

                if (SharedPreferencesUtils.getInstance(HealthTipsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC))
                    YoutubePlayerActivity.startActivity(HealthTipsActivity.this, videoID, list.get(position).getDescriptionAr(), list.get(position).getTitleAr());
                else
                    YoutubePlayerActivity.startActivity(HealthTipsActivity.this, videoID, list.get(position).getDescriptionEn(), list.get(position).getTitleEn());
            }
        });

        rvHealthTips.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvHealthTips.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {

                    if (!responseData.getLastPage().equals(responseData.getCurrentPage())) {
                        if (responseData.getNextPageUrl() != null) {
                            if (responseData.getNextPageUrl().length() > 0 && isLoading) {
                                isLoading = false;
                                callAPI(Integer.parseInt(responseData.getNextPageUrl().split("=")[1]));

                            }
                        }
                    }
                }
            }
        });
    }

    void callAPI(int page) {

        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;

        presenter.callGetAllHealthTips(page, edSearch.getText().toString(), category_id);
    }


    @Override
    public void onDone(String cat_id) {
        category_id = cat_id;
        callAPI(1);
    }

    @Override
    public void onClear(String text) {
        if (!category_id.isEmpty()) {
            category_id = "";
            callAPI(1);
        }
    }

    @Override
    public void onSuccess(HealthTipsResponse response) {
        responseData = response.getData();

        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.GONE);
        responseData = response.getData();
        mainNoDataTrans.setVisibility(View.GONE);

        if (category_id.length() > 0)
            ivFilter.setImageResource(R.drawable.ic_filter_green);
        else
            ivFilter.setImageResource(R.drawable.ic_filter);


        if (response.getData().getData() != null) {
            if (responseData.getCurrentPage() == 1)
                list.clear();

            if (swipeToRefresh.isRefreshing()) {
                list.clear();
                swipeToRefresh.setRefreshing(false);
            }
            list.addAll(response.getData().getData());

            if (list.size() == 0) {
                mainNoDataTrans.setVisibility(View.VISIBLE);

                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                ivBackNo.setVisibility(View.VISIBLE);

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
        adapter.notifyDataSetChanged();

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
            mainNoDataTrans.setVisibility(View.VISIBLE);

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
