package sa.med.imc.myimc.Records.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.OperativeAdapter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

import static sa.med.imc.myimc.Utils.Common.ReportTypes.LAB;

public class DischargeRecordsFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_LAB_REPORT_FILE = 1;
    private ResponseBody responseBody;

    @BindView(R.id.rv_records)
    RecyclerView rvRecords;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
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
    RelativeLayout mainNoData;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    boolean edit = false;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.total_report_title)
    TextView totalReportTitle;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;

    private String mParam1;

    OperativeAdapter adapter;
    int page = 0, total_items = 0;
    boolean wasVisible = false, isSearched = false;
    String spec_number = "", mainTest = "", episodeId = "", from_date = "", to_date = "";

    ReportsPresenter presenter;
    List<OperativeResponse.Datum> reports = new ArrayList<>();

    FragmentListener fragmentAdd;
    Boolean isLoading = false;

    BottomReportFilterDialog bottomReportFilterDialog;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public DischargeRecordsFragment() {
        // Required empty public constructor
    }


    public static DischargeRecordsFragment newInstance() {
        DischargeRecordsFragment fragment = new DischargeRecordsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opertive, container, false);
        ButterKnife.bind(this, view);
        presenter = new ReportsImpl(this, getActivity());
        ivFilter.setImageResource(R.drawable.ic_filter);

        page = 0;
        reports = new ArrayList<>();

        isSearched = false;
        edit = false;
        ivSearch.setImageResource(R.drawable.ic_search);
        edSearch.setVisibility(View.GONE);

        bottomReportFilterDialog = null;

        from_date = "";
        to_date = "";
        isLoading = false;

        mParam1 = "";
        episodeId = "";
//
        if (getArguments() != null) {
            edSearch.setText("");

            mParam1 = getArguments().getString(ARG_PARAM1);
            episodeId = getArguments().getString(ARG_PARAM2);
        }

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (getActivity() != null)
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

        loadData();


        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            tvLoadMore.setVisibility(View.GONE);
            callAPI();
        });

        return view;
    }

    void loadData() {

        totalReportTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getString(R.string.discharge_reports));
        totalReportTitle.setText(getString(R.string.discharge_reports_total));
        setUpRecordsRecyclerView();


        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (reports.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        callAPI();
                    }
                }
            }
        });

        callAPI();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_search, R.id.iv_filter, R.id.iv_back, R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_back_list:
//
//                if (fragmentAdd != null)
//                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
//
//                break;
//
            case R.id.iv_back:
//                if (episodeId.length() > 0) {
                getActivity().finish();
//                } else {
//                    mainContent.setVisibility(View.GONE);
//                    layListReports.setVisibility(View.VISIBLE);
//
//                }
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);

                    // Clear search and load all reports
                    if (isSearched) {
                        edSearch.setText("");
                        page = 0;
                        callAPI();
                    }

                    edit = false;
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

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;

            case R.id.tv_load_more:
                tvLoadMore.setEnabled(false);

                progressBarLoad.setVisibility(View.VISIBLE);
                callAPI();
                break;

            case R.id.iv_filter:
                if (bottomReportFilterDialog == null)
                    bottomReportFilterDialog = BottomReportFilterDialog.newInstance();

                if (!bottomReportFilterDialog.isAdded())
                    bottomReportFilterDialog.show(getChildFragmentManager(), "DAILOG");
                break;

        }
    }

    // API for Discharge reports
    void callAPI() {
        isSearched = edSearch.getText().toString().length() > 0;
        if (page == 0) {
            if (edSearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        presenter.callGetDischargeReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(),
                episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date,
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Initialize list view and add adapter to display data,
    void setUpRecordsRecyclerView() {
        adapter = new OperativeAdapter(getContext(), reports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecords.setAdapter(adapter);
        adapter.setOnItemClickListener(new OperativeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

//        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
                if (reports.get(position).getStatus().equalsIgnoreCase("N")) {
                    onFail(getString(R.string.no_result_available));
                } else {
                    presenter.callGenerateDischargePdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                            String.valueOf(reports.get(position).getId()), reports.get(position).getReportType(),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                }
//            }
//        }));


            }
        });

    }


    @Override
    public void onGetLabReports(LabReportResponse response) {


    }


    @Override
    public void onGetMedicalReports(MedicalResponse response) {

    }

    @Override
    public void onGetSickLeaveReports(SickLeaveResponse response) {

    }

    @Override
    public void onGetOperativeReports(OperativeResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);

        if (response.getData() != null) {
            if (page == 0)
                reports.clear();

            if (swipeToRefresh.isRefreshing()) {
                reports.clear();
                swipeToRefresh.setRefreshing(false);

            }

            reports.addAll(response.getData());
            adapter.setAllData(response.getData());

            if (page == 0)
                if (reports.size() == 0) {
//                    onFail(getString(R.string.no_data_available));
                    tvTotal.setText("");
                    totalReportTitle.setVisibility(View.GONE);

//                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
                } else {
                    ivFilter.setVisibility(View.VISIBLE);
                    ivSearch.setVisibility(View.VISIBLE);

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    totalReportTitle.setVisibility(View.VISIBLE);
                    tvTotal.setText(response.getTotalItems() + "");
                }

            if (response.getTotalItems().length() > 0) {
                total_items = Integer.parseInt(response.getTotalItems());
                isLoading = true;
            }

            if (Integer.parseInt(response.getTotalItems()) > reports.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                wasVisible = true;
                page = page + 1;
            } else {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = false;
            }

            adapter.notifyDataSetChanged();
//        } else {
//            mainContent.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
        } else {
            tvTotal.setText("");
//            onFail(getString(R.string.no_data_available));
            totalReportTitle.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);


        }

        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);

        tvLoadMore.setEnabled(true);
    }

    @Override
    public void onGetCardioReports(CardioReportResponse reportResponse) {

    }


    @Override
    public void onGetSearchLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetSearchMedicalReports(MedicalResponse response) {

    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(LAB);
    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {

    }


    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case LAB:
                String labFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
                createFile(labFile, CREATE_LAB_REPORT_FILE);
                break;
        }


    }

    private void createFile(String fileName, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, requestCode);
    }

    private void alterDocument(Uri uri) {
        try {
            int count = -1;
            ParcelFileDescriptor pfd = getActivity().getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            InputStream input = new BufferedInputStream(responseBody.byteStream(), 8192);

            byte[] data = new byte[1024];

            while ((count = input.read(data)) != -1) {
                fileOutputStream.write(data, 0, count);
            }

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//    @Override
//    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {
//
//    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_LAB_REPORT_FILE) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.discharge_report));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> response, int totalItems) {
            // added by @Pm...
    }

    @Override
    public void onGetPin(PinResponse response) {

    }

    @Override
    public void onGenerateSmartReport(ResponseBody response, Headers headers) {

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
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);

        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                if (reports.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
                }

            } else {
                Common.makeToast(getActivity(), msg);
            }
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);

        if (reports.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(getActivity());
        }
    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
//            // download the file
//            InputStream input = new BufferedInputStream(responseBody.byteStream(), 8192);
//            String path = Environment.getExternalStorageDirectory().toString() + Constants.FOLDER_NAME;
//            File file = new File(path);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//            path = file.getPath() + "/" + filename;
//            // Output stream
//            OutputStream output = new FileOutputStream(path);
//
//            byte[] data = new byte[1024];
//
//            while ((count = input.read(data)) != -1) {
//                output.write(data, 0, count);
//            }
//
//            // flushing output
//            output.flush();
//
//            // closing streams
//            output.close();
//            input.close();
//            // Show download success and ask to open file
////            openFile(path);
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.discharge_report));
//
//            startActivity(intent);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }


    @Override
    public void onDone(String from, String to) {
        from_date = from;
        to_date = to;

        if (!from_date.isEmpty() && !to_date.isEmpty())
            ivFilter.setImageResource(R.drawable.ic_filter_green);
        page = 0;
        callAPI();
    }

    @Override
    public void onClear(String text) {
        if (from_date.length() > 0 || to_date.length() > 0) {
            from_date = "";
            to_date = "";
            ivFilter.setImageResource(R.drawable.ic_filter);

            page = 0;
            callAPI();
        }
    }


}
