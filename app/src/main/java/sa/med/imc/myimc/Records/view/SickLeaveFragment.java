package sa.med.imc.myimc.Records.view;

import android.Manifest;

import androidx.room.Room;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.SickLeaveAdapter;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

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
import static sa.med.imc.myimc.Utils.Common.ReportTypes.SICK;


public class SickLeaveFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_SICK_REPORT_FILE = 2;
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
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
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


    int page = 0, total_items = 0;
    boolean wasVisible = false;
    String episodeId = "0", from_date = "", to_date = "";
    Boolean isLoading = false;

    // App database
    ImcDatabase db;

    int sick_leave_id = 0;

    SickLeaveAdapter adapter;
    List<SickLeave> reports = new ArrayList<>();

    ReportsPresenter presenter;
    private OnFragmentInteractionListener mListener;
    BottomReportFilterDialog bottomReportFilterDialog;


    public SickLeaveFragment() {
        // Required empty public constructor
    }

    public static SickLeaveFragment newInstance(String param1, String param2) {
        SickLeaveFragment fragment = new SickLeaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sick_leave, container, false);
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setRotationY(180);
        }

        ButterKnife.bind(this, view);
        reports = new ArrayList<>();
        page = 0;
        from_date = "";
        to_date = "";

        db = Room.databaseBuilder(getActivity(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        fab_filter.setImageResource(R.drawable.ic_filter);

        presenter = new ReportsImpl(this, getActivity());
        setUpRecordsRecyclerView();
        if (reports.size() == 0)
            callAPI();

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            tvLoadMore.setVisibility(View.GONE);

            presenter.callGetAllSickLeaveReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), "", episodeId, Constants.RECORD_SET,
                    String.valueOf(page), from_date, to_date,
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {
//            if (presenter != null)
//                if (reports.size() == 0)
//                    callAPI();
        }
    }


    // Initialize list view and add adapter to display data,
    void setUpRecordsRecyclerView() {
        adapter = new SickLeaveAdapter(getContext(), reports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecords.setAdapter(adapter);
        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Fetch selected sick leave from db
//                sick_leave_id = reports.get(position).getId();
//                    SickLeave report = db.sickLeaveDataDao().getSelectSickLeave(sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

                // check if report path saved in local db then open from local rather than downloading again
//                    if (report != null) {
//
//                        if (report.getLocalPath().isEmpty())
//                            diagnosis(String.valueOf(reports.get(position).getId()));
//                        else {
//                            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//
//                            startActivity(intent);
//                        }
//                    } else {
//                diagnosis(String.valueOf(reports.get(position).getId()));
//                    }
            }
        }));


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
    }

    //Call Api to get reports
    void callAPI() {
        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }

        presenter.callGetAllSickLeaveReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), "", episodeId, Constants.RECORD_SET,
                String.valueOf(page), from_date, to_date,
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    @Override
    public void onGetLabReports(LabReportResponse response) {

    }

    @Override
    public void onGetMedicalReports(MedicalResponse response) {

    }

    @Override
    public void onGetSickLeaveReports(SickLeaveResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.GONE);

        if (page == 0)
            reports.clear();

        if (swipeToRefresh.isRefreshing()) {
            reports.clear();
            swipeToRefresh.setRefreshing(false);

        }
        if (response.getSickLeaves() != null) {
            reports.addAll(response.getSickLeaves());
            adapter.setAllData(response.getSickLeaves());

            if (page == 0)
                if (reports.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    fab_filter.setVisibility(View.VISIBLE);
                    isLoading = true;
                    tvTotal.setText(response.getTotalItems() + "");

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                }
            total_items = Integer.parseInt(response.getTotalItems());

            if (Integer.parseInt(response.getTotalItems()) > reports.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                wasVisible = true;
                page = page + 1;
            } else {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = false;
            }

            adapter.notifyDataSetChanged();
        } else {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }

        tvLoadMore.setEnabled(true);
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

    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(SICK);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case SICK:
                String sickFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_sick_leave_" + System.currentTimeMillis() + ".pdf";
                createFile(sickFile, CREATE_SICK_REPORT_FILE);
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
        if (requestCode == CREATE_SICK_REPORT_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);
                saveToLocal(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {
//        downloadFile(response, headers);
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
        progressBarLoad.setVisibility(View.GONE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);

        if (msg.equalsIgnoreCase("timeout")) {
            fab_filter.setVisibility(View.GONE);

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
        Common.makeToast(getActivity(), msg);
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.GONE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);
        fab_filter.setVisibility(View.GONE);

        if (reports.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(getActivity());
        }
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

                callAPI();
                break;

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;

            case R.id.fab_filter:
                if (bottomReportFilterDialog == null)
                    bottomReportFilterDialog = BottomReportFilterDialog.newInstance();

                if (!bottomReportFilterDialog.isAdded())
                    bottomReportFilterDialog.show(getChildFragmentManager(), "DAILOG");
                break;
        }
    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_sick_leave_" + System.currentTimeMillis() + ".pdf";
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
//            //Save to db
//            saveToLocal(path);
//
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//
//            startActivity(intent);
////            openFile(path);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    // save path in db
    void saveToLocal(Uri uri) {
//        db.sickLeaveDataDao().saveSickLeavePath(uri.toString(), sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
        sick_leave_id = 0;
    }


    // to ask include diagnosis or not
    void diagnosis(String leave_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.diagnosis));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.callGenerateSickLeavePdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        leave_id, "1",
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0)
                );

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.callGenerateSickLeavePdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        leave_id, "0",
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0)

                );


            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#005497"));
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }

    @Override
    public void onDone(String from, String to) {
        from_date = from;
        to_date = to;
        if (!from_date.isEmpty() && !to_date.isEmpty())
            fab_filter.setImageResource(R.drawable.ic_filter_green);

        page = 0;
        callAPI();
    }

    @Override
    public void onClear(String text) {
        if (from_date.length() > 0 || to_date.length() > 0) {
            from_date = "";
            to_date = "";
            fab_filter.setImageResource(R.drawable.ic_filter);

            page = 0;
            callAPI();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

