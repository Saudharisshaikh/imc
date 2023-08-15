package sa.med.imc.myimc.Records.view;

import android.Manifest;

import androidx.room.Room;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;

import static sa.med.imc.myimc.Utils.Common.ReportTypes.MEDICAL;

public class RadiologyReportsFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_MEDICAL_REPORT_FILE = 3;
    private ResponseBody responseBody;


    private String mParam1;
    private String mParam2;

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
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.fab_filter)
    FloatingActionButton fab_filter;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    boolean edit = false;
    Boolean isLoading = false;

    private OnFragmentInteractionListener mListener;


    String product_code = "", orderNumLin = "", episodeId = "0", from_date = "", to_date = "";

    // App database
    ImcDatabase db;


    int page = 0, total_items = 0;
    boolean wasVisible = false, isSearched = false;

    ReportsPresenter presenter;
    MedicalReportAdapter medicalReportAdapter;
    List<MedicalReport> medicalReports = new ArrayList<>();
    BottomReportFilterDialog bottomReportFilterDialog;

    public RadiologyReportsFragment() {
        // Required empty public constructor
    }


    public static RadiologyReportsFragment newInstance(String param1, String param2) {
        RadiologyReportsFragment fragment = new RadiologyReportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radiology_reports, container, false);
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setRotationY(180);
        }

        ButterKnife.bind(this, view);

        medicalReports = new ArrayList<>();
        page = 0;
        from_date = "";
        to_date = "";
        fab_filter.setImageResource(R.drawable.ic_filter);
        isSearched = false;
        edSearch.setText("");

        presenter = new ReportsImpl(this, getActivity());
        db = Room.databaseBuilder(getActivity(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        setUpMedRecordsRecyclerView();
        callAPI(Constants.RECORD_SET);
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    page = 0;
//                    callAPI(Constants.RECORD_SET);
                    if (getActivity() != null)
                        Common.hideSoftKeyboard(getActivity());

                    return true;
                }

                return false;
            }
        });
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearched) {
                    edSearch.setText("");
                    page = 0;
                    swipeToRefresh.setRefreshing(true);
                    callAPI(Constants.RECORD_SET);
                }
            }
        });
        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                    page = 0;
                    callAPI(Constants.RECORD_SET);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    page = 0;
                    callAPI(Constants.RECORD_SET);
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                tvLoadMore.setVisibility(View.GONE);

                presenter.callGetAllMedicalReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), "", episodeId,
                        Constants.RECORD_SET, "0", from_date, to_date,
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {
//            if (presenter != null)
//                if (medicalReports.size() == 0)
            // callAPI("", Constants.RECORD_SET);
        }
    }


    // Initialize Medical list view and add adapter to display data,
    void setUpMedRecordsRecyclerView() {
        medicalReportAdapter = new MedicalReportAdapter(getActivity(), medicalReports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecords.setAdapter(medicalReportAdapter);
        medicalReportAdapter.setOnItemClickListener(new MedicalReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


//        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (medicalReports.get(position).getResultAvailable() == 0) {
//                    onFail(getString(R.string.no_result_available));
//                } else {
//                    // Fetch selected report from db
//                    product_code = medicalReports.get(position).getId().getOrderNo();
//                    orderNumLin = medicalReports.get(position).getId().getOrderNoLine();
//                    MedicalReport report = db.medicalReportDataDao().getSelectReport(product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                    // check if report path saved in local b then open from local rather than downloading again
//
//                    if (report != null) {
//                        if (report.getLocalPath().isEmpty())
//                            presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                    medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine(),
//                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                        else {
//                            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
//
//                            startActivity(intent);
//                        }
//                    } else {
//                        presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine(),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                    }
//                }
            }
        });
//            }
//        }));


        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (medicalReports.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        callAPI(Constants.RECORD_SET);
                    }
                }
            }
        });
    }


    @Override
    public void onGetLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetMedicalReports(MedicalResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (page == 0)
            medicalReports.clear();

        if (swipeToRefresh.isRefreshing()) {
            medicalReports.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getMedicalReports() != null) {
            medicalReports.addAll(response.getMedicalReports());
            medicalReportAdapter.setAllData(response.getMedicalReports());

            if (page == 0)
                if (medicalReports.size() == 0 && edSearch.getText().toString().trim().isEmpty()) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {

                    if (medicalReports.size() == 0 && !edSearch.getText().toString().trim().isEmpty()) {
                        mainNoDataTrans.setVisibility(View.VISIBLE);

                    }
                    isLoading = true;

                    tvTotal.setText(response.getTotal_items() + "");
                    fab_filter.setVisibility(View.VISIBLE);

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    edSearch.setVisibility(View.VISIBLE);
                }

            total_items = Integer.parseInt(response.getTotal_items());

            if (Integer.parseInt(response.getTotal_items()) > medicalReports.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                page = page + 1;
                wasVisible = true;
            } else {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = false;
            }

            medicalReportAdapter.notifyDataSetChanged();
        } else {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);

        tvLoadMore.setEnabled(true);

    }

    @Override
    public void onGetSickLeaveReports(SickLeaveResponse response) {

    }

    @Override
    public void onGetOperativeReports(OperativeResponse response) {

    }

    @Override
    public void onGetCardioReports(CardioReportResponse reportResponse) {

    }

    @Override
    public void onGetSearchLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetSearchMedicalReports(MedicalResponse response) {
        if (response.getMedicalReports() != null) {
            medicalReports.clear();
            medicalReports.addAll(response.getMedicalReports());
            tvLoadMore.setVisibility(View.GONE);
            medicalReportAdapter.notifyDataSetChanged();

            rvRecords.smoothScrollToPosition(0);

        }
    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(MEDICAL);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case MEDICAL:
                String medicalFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
                createFile(medicalFile, CREATE_MEDICAL_REPORT_FILE);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_MEDICAL_REPORT_FILE) {
            Uri uri = null;
            uri = resultData.getData();

            alterDocument(uri);
            saveToLocal(uri);

            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
            intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
            startActivity(intent);

        }
    }

    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {

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

        if (msg.equalsIgnoreCase("timeout")) {
            fab_filter.setVisibility(View.GONE);

            if (medicalReports.size() == 0) {
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
        Common.makeToast(getActivity(), msg);
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);
        fab_filter.setVisibility(View.GONE);

        if (medicalReports.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(getActivity());
        }
    }

    //Call Api to get reports
    void callAPI(String records) {
        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;

        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        presenter.callGetAllMedicalReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString(),
                episodeId, records, String.valueOf(page), from_date, to_date,
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
//
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
//
//            //onFail("Report Downloaded");
//            saveToLocal(path);
//
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
//
//            startActivity(intent);
//
////            openFile(path);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }


    // save path in db
    void saveToLocal(Uri uri) {
//        db.medicalReportDataDao().saveLabLocalPath(uri.toString(), product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
        product_code = "";
        orderNumLin = "";
    }


    // Show download success and ask to open file
    void openFile(String filepath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        builder.setMessage(getResources().getString(R.string.download_dialog));
        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, filepath);
                startActivity(intent);

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            }
        });
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.fab_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_load_more:
                tvLoadMore.setEnabled(false);

                progressBarLoad.setVisibility(View.VISIBLE);
                callAPI(Constants.RECORD_SET);
                break;

            case R.id.bt_try_again:
                callAPI(Constants.RECORD_SET);
                break;

            case R.id.bt_try_again_time_out:
                callAPI(Constants.RECORD_SET);
                break;

            case R.id.fab_filter:
                if (bottomReportFilterDialog == null)
                    bottomReportFilterDialog = BottomReportFilterDialog.newInstance();

                if (!bottomReportFilterDialog.isAdded())
                    bottomReportFilterDialog.show(getChildFragmentManager(), "DAILOG");
                break;
        }
    }

    @Override
    public void onDone(String from, String to) {
        from_date = from;
        to_date = to;
        if (!from_date.isEmpty() && !to_date.isEmpty())
            fab_filter.setImageResource(R.drawable.ic_filter_green);

        page = 0;
        callAPI(Constants.RECORD_SET);
    }

    @Override
    public void onClear(String text) {
        if (from_date.length() > 0 || to_date.length() > 0) {

            from_date = "";
            to_date = "";
            fab_filter.setImageResource(R.drawable.ic_filter);

            page = 0;
            callAPI(Constants.RECORD_SET);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
