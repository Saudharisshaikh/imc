package sa.med.imc.myimc.Physicians.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianAllDataReseponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.PhysicianImpl;
import sa.med.imc.myimc.Physicians.presenter.PhysicianPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * All Physicians of a Clinic.
 * Patient can view profile of doctor.
 * Patient can get appointment from doctor.
 */

public class PhysiciansActivity extends BaseActivity implements BottomOptionDialog.BottomDailogListener, PhysicianViews, BottomFilterDoctorDialog.BottomDialogListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_physicians)
    RecyclerView rvPhysicians;
    boolean edit = false;
    List<PhysicianResponse.ProviderList> list = new ArrayList<>();

    PhysicianPresenter physicianPresenter;
    PhysicianAdapter adapter;
    @BindView(R.id.tv_load_more)
    TextView tv_load_more;
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
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;
    @BindView(R.id.iv_back_no)
    ImageView ivBackNo;

    int page = 0;
    int total_items = 0;
    private boolean loadingEnd = false, isSearched = false;
    String depart_id = "", lang_id = "";
    LinearLayoutManager mLayoutManager;
    BottomOptionDialog bottomOptionDialog;
    Boolean isLoading = false;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, PhysiciansActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(PhysiciansActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        PhysiciansActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physicians);
        ButterKnife.bind(this);

        physicianPresenter = new PhysicianImpl(this, this);
        physicianPresenter.callGetAllDoctors("", "", edSearch.getText().toString(), "", Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

        setUpPhysicianRecyclerView();

        swipeToRefresh.setOnRefreshListener(() -> {
            tv_load_more.setVisibility(View.GONE);
            depart_id = "";
            lang_id = "";

            page = 0;
            callAPI(page);
        });

        edSearch.setOnTypingModified((view, isTyping) -> {
            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    page = 0;
                    callAPI(page);
                }
            }
        });

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(PhysiciansActivity.this);
//                page = 0;
//                callAPI(page);

                return true;
            }
            return false;
        });

    }

    @OnClick({R.id.iv_back, R.id.iv_back_no, R.id.iv_search, R.id.iv_filter, R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                    edSearch.clearFocus();

                    // Check if search box empty or not
                    if (isSearched) {
                        edSearch.setText("");
                        page = 0;
                        callAPI(page);
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
                if (bottomOptionDialog == null)
                    bottomOptionDialog = BottomOptionDialog.newInstance();

                if (!bottomOptionDialog.isAdded())
                    bottomOptionDialog.show(getSupportFragmentManager(), "DAILOG");
                break;

            case R.id.tv_load_more:
                tv_load_more.setEnabled(false);
                callAPI(page);
                break;

            case R.id.bt_try_again:
                page = 0;
                callAPI(page);
                break;

            case R.id.bt_try_again_time_out:
                page = 0;
                callAPI(page);
                break;


            case R.id.iv_back_no:
                onBackPressed();
                break;
        }
    }

    // Initialize Physician list view and add adapter to display data,
    void setUpPhysicianRecyclerView() {
//        PIYUSH TRACKER
        adapter = new PhysicianAdapter(this, list);
        rvPhysicians.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvPhysicians.setLayoutManager(mLayoutManager);

        rvPhysicians.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhysicianAdapter.OnItemClickListener() {
            @Override
            public void onViewProfile(int position) {

                Intent intent=new Intent(PhysiciansActivity.this, PhysicianFullDetailActivity.class);
                intent.putExtra("code",list.get(position).getProviderCode());
                startActivity(intent);            }

            @Override
            public void onBookAppointment(int position) {
                ContactOptionsActivity.startActivity(PhysiciansActivity.this);
            }

            @Override
            public void onBookNextAvailable(int position) {

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
                        callAPI(page);
                    }
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
        PhysiciansActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onGetPhysicianList(PhysicianResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.GONE);

        if (depart_id.length() > 0 || lang_id.length() > 0)
            ivFilter.setImageResource(R.drawable.ic_filter_green);
        else
            ivFilter.setImageResource(R.drawable.ic_filter);


        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getPhysicians() != null) {
            if (page == 0)
                list.clear();

//            list.addAll(response.getPhysicians());
            loadingEnd = response.getPhysicians().size() != 0;


            if (list.size() == 0) {
                rvPhysicians.setVisibility(View.GONE);
                ivSearch.setVisibility(View.VISIBLE);
                ivFilter.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
                isLoading = true;

                rvPhysicians.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
                ivFilter.setVisibility(View.VISIBLE);

                if (response.getTotal_items() != null)
                    total_items = Integer.parseInt(response.getTotal_items());

                page = page + 1;
                tv_load_more.setVisibility(View.GONE);

            }
        } else {
            rvPhysicians.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
            ivFilter.setVisibility(View.VISIBLE);
        }
        tv_load_more.setEnabled(true);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onGetServiceList(DrServiceResponse response) {

    }


    @Override
    public void onGetPhysicianProfile(PhysicianDetailResponse response) {

    }

    void callAPI(int page) {
        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;

        physicianPresenter.callGetAllDoctors("", depart_id, edSearch.getText().toString(), lang_id, Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }


    @Override
    public void onGetCMSPhysician(PhysicianCompleteDetailCMSResponse response) {

    }

    @Override
    public void onGetNextAvailableTIme(NextTimeResponse response,int pos) {

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
        tv_load_more.setEnabled(true);

        if (msg.equalsIgnoreCase("timeout")) {
            if (list.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.GONE);
                ivBackNo.setVisibility(View.VISIBLE);

            } else {
                makeToast(getString(R.string.time_out_messgae));
                isLoading = true;
            }

        } else {
            makeToast(msg);
        }
    }

    @Override
    public void onNoInternet() {
        tv_load_more.setEnabled(true);

//        if (list.size() == 0) {
        mainContent.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoNet.setVisibility(View.VISIBLE);
        mainNoDataTrans.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.VISIBLE);

//        } else {
//            Common.noInternet(this);
//            isLoading = true;
//        }
    }

    @Override
    public void onDone(String rat, String id) {
        depart_id = rat;
        lang_id = id;

        page = 0;
        callAPI(page);
    }

    @Override
    public void onClear(String text) {
        if (depart_id.length() > 0 || lang_id.length() > 0) {
            depart_id = "";
            lang_id = "";

            page = 0;
            callAPI(page);
        }
    }
}
