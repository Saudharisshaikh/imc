package sa.med.imc.myimc.Records.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ai.medicus.insights.MedicusInsights;
//import ai.medicus.medicuscore.LoginWithPinDelegate;
//import ai.medicus.medicuscore.LoginWithPinErrors;
//import ai.medicus.medicuscore.OnlinePatient;
//import ai.medicus.medicuscore.Patient;
//import ai.medicus.medicuscore.SyncWithServerDelegate;
import ai.medicus.smartreport.SmartReport;
import ai.medicus.smartreport.SmartReportCallback;
import ai.medicus.utils.MedicusUtilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
import sa.med.imc.myimc.Adapter.RecordsAdapter;
import sa.med.imc.myimc.Adapter.SickLeaveAdapter;
import sa.med.imc.myimc.Adapter.SmartRecordsAdapter;
import sa.med.imc.myimc.Base.MCPatient;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.InsightListener;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Interfaces.OnAdapterItemClickListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.model.SmartLabReport;
import sa.med.imc.myimc.Records.presenter.ReportsImpl;
import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

import static sa.med.imc.myimc.Utils.Common.ReportTypes.LAB;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.MEDICAL;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.SICK;

//4 jan
/*import ai.medicus.insights.InsightButtonActionType;
import ai.medicus.insights.InsightCardActionType;
import ai.medicus.insights.InsightsCallback;*/
//import ai.medicus.medicuscore.Answer;
//import ai.medicus.medicuscore.DoPatientProgramItem;
//import ai.medicus.medicuscore.DoctorNote;
//import ai.medicus.medicuscore.LoginWithPinDelegate;
//import ai.medicus.medicuscore.LoginWithPinErrors;
//import ai.medicus.medicuscore.OnlinePatient;
//import ai.medicus.medicuscore.Patient;
//import ai.medicus.medicuscore.PatientCoachingStep;
//import ai.medicus.medicuscore.PatientInsight;
//import ai.medicus.medicuscore.ProfileQuestion;
//import ai.medicus.medicuscore.SyncWithServerDelegate;
//import ai.medicus.medicuscore.WBListInsight;
//4 jan
/*import ai.medicus.smartreport.SmartReport;
import ai.medicus.smartreport.SmartReportCallback;*/

public class LMSRecordsFragment extends Fragment implements ReportsViews, OnAdapterItemClickListener,BottomReportFilterDialog.BottomDailogListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_FILE = 1;
    private static final int CREATE_LAB_REPORT_FILE = 1;
    private static final int CREATE_SICK_REPORT_FILE = 2;
    private static final int CREATE_MEDICAL_REPORT_FILE = 3;
    private static final int CREATE_SMART_REPORT_FILE = 4;
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
//    @BindView(R.id.ed_search)
//    CustomTypingEditText edSearch;
    boolean edit = false;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;
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
    @BindView(R.id.main_toolbar)
    RelativeLayout maintoolbar;

    CustomTypingEditText edsearch;
    ProgressBar progressBar;


    private String mParam1;
    private Activity activity;

    RecordsAdapter adapter;
    SmartRecordsAdapter smartadapter;
    LabReportHeaderAdapter mLabReportHeaderAdapter;
    LabReportsMedicus labReportsMedicus;

    int page = 0, total_items = 0, totalReportsItems = 0;
    boolean wasVisible = false, isSearched = false;
    String spec_number = "", mainTest = "", episodeId = "", from_date = "", to_date = "";

    // App database
    ImcDatabase db;

    ReportsPresenter presenter;
    List<LabReport> reports = new ArrayList<>();
    List<SmartLabReport> smartreports = new ArrayList<>();
    List<LabReportsParentMedicus> reportsMedicus = new ArrayList<>();

    FragmentListener fragmentAdd;

    MedicalReportAdapter medicalReportAdapter;
    List<MedicalReport> medicalReports = new ArrayList<>();
    String product_code = "", orderNumLin = "", orderNum = "";
    Boolean isLoading = false;

    int sick_leave_id = 0;

    SickLeaveAdapter sick_adapter;
    List<SickLeave> sick_reports = new ArrayList<>();
    BottomReportFilterDialog bottomReportFilterDialog;

    private static boolean showMedicus = true; // todo: for live it should be FALSE, For debug it should be TRUE
    private Unbinder unBinder;
    private String mAccessionNum;
    public View view;
    ReportsViews views;

    private int labPageNumber = 1;
    private String labBookingStatus = "2";
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public LMSRecordsFragment() {
        // Required empty public constructor
    }


    public static LMSRecordsFragment newInstance() {
        LMSRecordsFragment fragment = new LMSRecordsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lms, container, false);
        unBinder = ButterKnife.bind(this, view);

        edsearch=view.findViewById(R.id.ed_search);
        progressBar=view.findViewById(R.id.progress_bar);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        presenter = new ReportsImpl(this, getActivity());
        db = Room.databaseBuilder(getActivity(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        ivFilter.setImageResource(R.drawable.ic_filter);

        page = 0;
        reports = new ArrayList<>();
        medicalReports = new ArrayList<>();
        sick_reports = new ArrayList<>();
        isSearched = false;
        edit = false;
        ivSearch.setImageResource(R.drawable.ic_search);
        edsearch.setVisibility(View.GONE);

        bottomReportFilterDialog = null;

        from_date = "";
        to_date = "";
        isLoading = false;

        mParam1 = "";
        episodeId = "";
//
        if (getArguments() != null) {
            edsearch.setText("");

            mParam1 = getArguments().getString(ARG_PARAM1);
            episodeId = getArguments().getString(ARG_PARAM2);
        }
        loadData();

        edsearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                page = 0;
//                callAPI();
                if (getActivity() != null)
                    Common.hideSoftKeyboard(getActivity());

                return true;
            }

            return false;
        });

        edsearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edsearch.getText().toString().trim().length() > 0) {
                    page = 0;
                    callAPI();
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            tvLoadMore.setVisibility(View.GONE);
            callAPI();
        });


    }

    void loadData() {

        totalReportTitle.setVisibility(View.VISIBLE);
        if (mParam1.equalsIgnoreCase("lab")) {
            toolbarTitle.setText(getString(R.string.lab_reports));
            totalReportTitle.setText(getString(R.string.total_lab_reports));
            setUpRecordsRecyclerView();

        } else if (mParam1.equalsIgnoreCase("radio")) {
            toolbarTitle.setText(getString(R.string.radiology_reports));
            totalReportTitle.setText(getString(R.string.total_lab_radio));
            setUpMedRecordsRecyclerView();

        } else if (mParam1.equalsIgnoreCase("sick")) {
            toolbarTitle.setText(getString(R.string.sick_leave_report));
            totalReportTitle.setText(getString(R.string.total_lab_sick));
            setUpSickRecordsRecyclerView();
        }

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {

                    //Reaches to the bottom so it is time to updateRecyclerView!
                    if (mParam1.equalsIgnoreCase("lab")) {
                        if (showMedicus) {
                            if (reportsMedicus.size() < totalReportsItems && isLoading) {
                                isLoading = false;
                                progressBarLoad.setVisibility(View.VISIBLE);
                                callAPI();
                            }
                        } else {
                            if (reports.size() < total_items && isLoading) {
                                isLoading = false;
                                progressBarLoad.setVisibility(View.VISIBLE);
                                callAPI();
                            }
                        }

                    } else if (mParam1.equalsIgnoreCase("radio")) {
                        if (medicalReports.size() < total_items && isLoading) {
                            isLoading = false;
                            progressBarLoad.setVisibility(View.VISIBLE);
                            callAPI();
                        }
                    } else if (mParam1.equalsIgnoreCase("sick")) {
                        if (sick_reports.size() < total_items && isLoading) {
                            isLoading = false;
                            progressBarLoad.setVisibility(View.VISIBLE);
                            callAPI();
                        }
                    }
                }
            }
        });

        callAPI();

    }

    @Override
    public void onDestroyView() {
        if (unBinder != null) unBinder.unbind();
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
                    edsearch.setVisibility(View.GONE);

                    // Clear search and load all reports
                    if (isSearched) {
                        edsearch.setText("");

                        if (mParam1.equalsIgnoreCase("lab")) {
                            page = 0;
                            callAPI();
                        } else if (mParam1.equalsIgnoreCase("radio")) {
                            page = 0;
                            callAPI();
                        }
                    }

                    edit = false;
                } else {
                    ivSearch.setImageResource(R.drawable.ic_close);
                    edsearch.setVisibility(View.VISIBLE);
                    edit = true;
                    edsearch.requestFocus();
                    if (getActivity() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edsearch, InputMethodManager.SHOW_IMPLICIT);
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

    //Call Api to get reports
    void callAPI() {
        isSearched = edsearch.getText().toString().length() > 0;

        if (mParam1.equalsIgnoreCase("lab")) {
            callAPILab();
        } else if (mParam1.equalsIgnoreCase("radio")) {
            callAPIRadio();
        } else if (mParam1.equalsIgnoreCase("sick")) {
            callAPISick();
        }

    }

    // API for radio reports
    void callAPIRadio() {
        if (page == 0) {
            if (edsearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        presenter.callGetAllMedicalReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                edsearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date,
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    // API for lab reports
    void callAPILab() {
        if (page == 0) {
            if (edsearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }

        if (showMedicus == false) {
            presenter.callGetAllLabReports(String.valueOf(labPageNumber), labBookingStatus);

        } else {
            presenter.callGetAllLabReportsMedicus(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edsearch.getText().toString().toLowerCase(),
                    episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date,
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }

    //Call Api to get sick reports
    void callAPISick() {
        if (page == 0) {
            if (edsearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }

        presenter.callGetAllSickLeaveReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                edsearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date,
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }


    // Initialize list view and add adapter to display data;
    // below recycler view is for lab reports
    void setUpRecordsRecyclerView() {
        if (showMedicus) {
//            Log.d("LMS Records Fragment", "setUpRecordsRecyclerView: Family name -- "+reportsMedicus.get(0).getFamilyName());
//            Log.d("LMS Records Fragment", "setUpRecordsRecyclerView: Given name -- "+reportsMedicus.get(0).getGivenName());
            mLabReportHeaderAdapter = new LabReportHeaderAdapter(getActivity(), reportsMedicus);
            mLabReportHeaderAdapter.setCurrentlyAttachedFragment(LMSRecordsFragment.this);
            rvRecords.setAdapter(mLabReportHeaderAdapter);
            mLabReportHeaderAdapter.setOnItemClickListener(new LabReportHeaderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int outerPosition, int innerPosition, LabReportsMedicus model) {
                    //  Toast.makeText(getActivity(), "pos:1 ", Toast.LENGTH_LONG).show();
                    if (reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getStatus() == 0) {
                        onFail(getString(R.string.no_result_available));
                    } else {
                        // Fetch selected report from db
                        spec_number = reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum();
                        mainTest = reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getMainTest();
//                        LabReport report = db.labReportDataDao().getSelectReport(spec_number, mainTest); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

                        // check if report path saved in local b then open from local rather than downloading again
//                        if (report != null) {
//                            if (report.getLocalPath().isEmpty())
//                                presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                        reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getReportType(),
//                                        reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum(),
////                                                    Integer.parseInt(reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getHosp_ID()));
//                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                            else {
//                                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//                                startActivity(intent);
//                            }
//                        } else {
//                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                    reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getReportType(),
//                                    reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum(),
//                                    Integer.parseInt(reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getHosp_ID()));
//
//                        }
                    }
                }
//                @Override
//                public void onItemMoreClick(LabReportsParentMedicus model, int item) {
////                    if (MCPatient.getInstance().getPatientRecord() != null) {
////                        openSmartReports();
////                    } else {
//                        presenter.callGetPin(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
////                    }
//
//                }

                @Override
                public void onItemMoreClick(LabReportsParentMedicus model, int position) {
                    // accession number, mr number, order number
                    // reports medicus not working



                    ((SingleFragmentActivity) getContext()).addFragment(model.getLstOfChildMedicus().get(0).getAccessionNum());

//                    if (MCPatient.getInstance().getPatientRecord() != null) {
//                        openSmartReports();
//                    } else
//                        presenter.callGetPin(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));


//                    Log.d("LMS Records Fragment", "Inside onItemMoreClick");
//                    Log.d("LMS Records Fragment", "Inside onItemMoreClick Position " + position);
//                    Log.d("LMS Records Fragment", "Inside onItemMoreClick Model " + model.getAptTime());
//                    Log.d("LMS Records Fragment", "onItemMoreClick: MR Number " + SharedPreferencesUtils.getInstance(
//
//                            getActivity()).
//
//                            getValue(Constants.KEY_MRN, ""));
////                    Log.d("LMS Records Fragment", "onItemMoreClick: Accession number "+ reports.get(0).getAccessionNum());
//                    Log.d("LMS Records Fragment", "Model Order Number: " + model.getOrderNum());
//                    Log.d("LMS Records Fragment", "onItemMoreClick: Accession number (1)" + model.getLstOfChildMedicus().
//
//                            get(0).
//
//                            getAccessionNum());
//
//                    Log.d("LMS Records Fragment", "onItemMoreClick: Order size " + model.getLstOfChildMedicus().
//
//                            size());
////                    Log.d("LMS Records Fragment", "onItemMoreClick: Order number "+model.getLstOfChildMedicus().get(0).getOrderNum());
////                    Log.d("LMS Records Fragment", "onItemMoreClick: Order Line Number "+model.getLstOfChildMedicus().get(0).getOrderNumLine());
//
////                    Log.d("LMS Records Fragment", "onItemMoreClick: "+model.getLstOfChildMedicus());
//
//                    Dexter.withContext(
//
//                            getActivity())
//                            .
//
//                                    withPermissions(
//                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                            Manifest.permission.READ_EXTERNAL_STORAGE
//                                    ).
//
//                            withListener(new MultiplePermissionsListener() {
//                                @Override
//                                public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//
//                                    if (reportP.areAllPermissionsGranted()) {
//                                        // Fetch selected report from db
////                                spec_number = reportsMedicus.get(0).getLstOfChildMedicus().get(0).getSpecimenNum();
////                                mainTest = reportsMedicus.get(0).getLstOfChildMedicus().get(0).getMainTest();
//                                        orderNum = model.getLstOfChildMedicus().get(0).getAccessionNum();
////                                SmartLabReport report = db.smartLabReportDataDao().getSelectReport(orderNum); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//                                        presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                orderNum,
//                                                "en");
//
//
//
//                                /*// check if report path saved in local b then open from local rather than downloading again
//                                if (report != null) {
//                                    if (report.getLocalPath().isEmpty()) {
//                                        // token, mr_number, order_no, language
//                                        // note: order_no is the accession number
//                                        presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                orderNum,
//                                                "en");
//                                    }
//                                    else {
//                                        Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                        intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                        intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//                                        startActivity(intent);
//                                    }
//                                } else {
//                                    // token, mr_number, order_no, language
//                                    // note: order_no is the accession number
//                                    presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                            orderNum,
//                                            "en");
//                                }*/
//                                    } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                                        Common.permissionDialog(getActivity());
//                                    }
//
//                                }
//
//                                @Override
//                                public void onPermissionRationaleShouldBeShown
//                                        (List<PermissionRequest> permissions, PermissionToken token) {
//                                    token.continuePermissionRequest();
//                                    /* ... */
//                                }
//                            }).
//
//                            check();
//                    /*mAccessionNum = model.getLstOfChildMedicus().get(0).getAccessionNum();
//                    //           Toast.makeText(getActivity(), "" + mAccessionNum, Toast.LENGTH_LONG).show();
//                    presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                            mAccessionNum, "en");
//
//                    Dexter.withContext(getActivity())
//                            .withPermissions(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE
//                            ).withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//                            if (reportP.areAllPermissionsGranted()) {
//
//                                callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                        mAccessionNum, "en");
//
//
//                            } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                                Common.permissionDialog(getActivity());
//                            }
//
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            token.continuePermissionRequest();
//                        }
//                    }).check();*/

                                /*// check if report path saved in local b then open from local rather than downloading again
                                if (report != null) {
                                    if (report.getLocalPath().isEmpty()) {
                                        // token, mr_number, order_no, language
                                        // note: order_no is the accession number
                                        presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                                                orderNum,
                                                "en");
                                    }
                                    else {
                                        Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                                        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                                        intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
                                        intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
                                        startActivity(intent);
                                    }
                                } else {
                                    // token, mr_number, order_no, language
                                    // note: order_no is the accession number
                                    presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                                            orderNum,
                                            "en");
                                }*/

                    /*mAccessionNum = model.getLstOfChildMedicus().get(0).getAccessionNum();
                    //           Toast.makeText(getActivity(), "" + mAccessionNum, Toast.LENGTH_LONG).show();
                    presenter.callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                            mAccessionNum, "en");

                    Dexter.withContext(getActivity())
                            .withPermissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport reportP) {

                            if (reportP.areAllPermissionsGranted()) {

                                callGenerateSmartLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                                        mAccessionNum, "en");


                            } else if (reportP.isAnyPermissionPermanentlyDenied()) {
                                Common.permissionDialog(getActivity());
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();*/

                }
            });

        } else {
            adapter = new RecordsAdapter(getContext(), reports);
            adapter.setOnItemClickListener(new RecordsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

//                    if (reports.get(position).getStatus() == 0) {
//                        onFail(getString(R.string.no_result_available));
//                    } else {
//                        // Fetch selected report from db
//                        spec_number = reports.get(position).getSpecimenNum();
//                        mainTest = reports.get(position).getMainTest();
//                        LabReport report = db.labReportDataDao().getSelectReport(spec_number, reports.get(position).getMainTest()); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                        // check if report path saved in local b then open from local rather than downloading again
//                        if (report != null) {
//                            if (report.getLocalPath().isEmpty())
//                                presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                        reports.get(position).getReportType(), reports.get(position).getSpecimenNum(),
//                                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                            else {
//                                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//                                startActivity(intent);
//                            }
//                        } else {
//                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                    reports.get(position).getReportType(), reports.get(position).getSpecimenNum(),
//                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//
//                        }
                    }
//                }


            });
            rvRecords.setAdapter(adapter);
        }

        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new

                LinearLayoutManager(getContext()));

    }


    // Initialize Medical list view and add adapter to display data,
    void setUpMedRecordsRecyclerView() {
//        medicalReportAdapter = new MedicalReportAdapter(getContext(), medicalReports);
//        rvRecords.setHasFixedSize(true);
//        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvRecords.setAdapter(medicalReportAdapter);
//        medicalReportAdapter.setOnItemClickListener(new MedicalReportAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
////        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
////            @Override
////            public void onItemClick(View view, int position) {
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
////            }
////
////        }));
//
//            }
//        });
    }

    // Initialize list view and add adapter to display data,
    void setUpSickRecordsRecyclerView() {
        sick_adapter = new SickLeaveAdapter(getContext(), sick_reports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecords.setAdapter(sick_adapter);
        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Fetch selected sick leave from db
//                sick_leave_id = sick_reports.get(position).getId();
//                SickLeave report = db.sickLeaveDataDao().getSelectSickLeave(sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                // check if report path saved in local db then open from local rather than downloading again
//                if (report != null) {
//                    if (report.getLocalPath().isEmpty())
//                        diagnosis(String.valueOf(sick_reports.get(position).getId()),String.valueOf(sick_reports.get(position).getHosp()));
//                    else {
//                        Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                        intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                        intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//                    }
//                } else {
//                    diagnosis(String.valueOf(sick_reports.get(position).getId()),String.valueOf(sick_reports.get(position).getHosp()));
//                }
            }
        }));
    }


    @Override
    public void onGetLabReports(LabReportResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);

        if (response.getOrder_list().size()>0) {
            if (page == 0)
                reports.clear();

            if (swipeToRefresh.isRefreshing()) {
                reports.clear();
                swipeToRefresh.setRefreshing(false);

            }

            reports.addAll(response.getOrder_list());
            adapter.setAllData(response.getOrder_list());

            if (page == 0)
                if (reports.size() == 0) {
//                    onFail(getString(R.string.no_data_available));
                    tvTotal.setText("");
                    totalReportTitle.setVisibility(View.GONE);

                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    ivFilter.setVisibility(View.VISIBLE);
                    ivSearch.setVisibility(View.VISIBLE);

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    totalReportTitle.setVisibility(View.VISIBLE);
//                    tvTotal.setText(response.getTotal_items() + "");
                }

//            if (response.getTotal_items().length() > 0) {
//                total_items = Integer.parseInt(response.getTotal_items());
//                isLoading = true;
//            }
//
//            if (Integer.parseInt(response.getTotal_items()) > reports.size()) {
//                tvLoadMore.setVisibility(View.GONE);//VISIBLE
//                wasVisible = true;
//                page = page + 1;
//            } else {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = false;
//            }

            adapter.notifyDataSetChanged();
        } else {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
//        else {
//            tvTotal.setText("");
////            onFail(getString(R.string.no_data_available));
//            totalReportTitle.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//
//
//        }

//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);

        tvLoadMore.setEnabled(true);

    }


    void openSmartReports() {
        Log.e("LabReportFragment", "openSmartReports method called ...");
        // open smart reports................
        //   showLoading();
//        MCPatient.getInstance().get().generateMetadata();
//        MCPatient.getInstance().get().generateInsights();


        //4 jan

//        MedicusUtilities.initUtileco(((SingleFragmentActivity) getContext()), MCPatient.getInstance().get());
//        SmartReport.Companion.configure(MCPatient.getInstance().get(), new InsightListener(), new SmartReportCallback() {
//                    @Override
//                    public void onEditReportClicked(Activity activity, int reportId) {
//
//                    }
//
//                    @Override
//                    public void onOpenProfilePopupClicked(Activity activity) {
//
//                    }
//
//                    @Override
//                    public void onOpenPopup(Activity activity, Context context, String symbol) {
//
//                    }
//
//                    @Override
//                    public void onShouldUserOpenCreateAccountActivity(Activity activity) {
//
//                    }
//
//                    @Override
//                    public void getPanelDetailsFragment(int i, int i1) {
//
//                    }
//
//                    @Override
//                    public void getBiomarkerDetailsFragment(int i, String s) {
//
//                    }
//                }
//                , false, false);
//        MedicusInsights.configure(MCPatient.getInstance().get(), new InsightListener(),
//                "en", " IMC", false);
//        SmartReport.Companion.openReportsScreen(((SingleFragmentActivity) getContext()));
    }

    @Override
    public void onGetMedicalReports(MedicalResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);


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
                if (medicalReports.size() == 0) {
                    tvTotal.setText("");
                    totalReportTitle.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                } else {
                    totalReportTitle.setVisibility(View.VISIBLE);
                    tvTotal.setText(response.getTotal_items() + "");
                    ivFilter.setVisibility(View.VISIBLE);
                    ivSearch.setVisibility(View.VISIBLE);

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);

                }

            if (response.getTotal_items().length() > 0) {
                total_items = Integer.parseInt(response.getTotal_items());
                isLoading = true;
            }

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
            tvTotal.setText("");
            totalReportTitle.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
        }
        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);

        tvLoadMore.setEnabled(true);

    }

    @Override
    public void onGetSickLeaveReports(SickLeaveResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);

        if (page == 0)
            sick_reports.clear();

        if (swipeToRefresh.isRefreshing()) {
            sick_reports.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getSickLeaves() != null) {
            sick_reports.addAll(response.getSickLeaves());
            sick_adapter.setAllData(response.getSickLeaves());

            if (page == 0)
                if (sick_reports.size() == 0) {
                    tvTotal.setText("");
                    totalReportTitle.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);

                } else {
                    totalReportTitle.setVisibility(View.VISIBLE);
                    tvTotal.setText(response.getTotalItems() + "");
                    ivFilter.setVisibility(View.VISIBLE);

                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                }

            if (response.getTotalItems().length() > 0) {
                total_items = Integer.parseInt(response.getTotalItems());
                isLoading = true;
            }
            if (Integer.parseInt(response.getTotalItems()) > sick_reports.size()) {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = true;
                page = page + 1;
            } else {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = false;
            }

            sick_adapter.notifyDataSetChanged();
        } else {
            tvTotal.setText("");
            totalReportTitle.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);

        }

        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);

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
        if (response.getOrder_list().size()>0) {
            reports.clear();
            reports.addAll(response.getOrder_list());
            tvLoadMore.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetSearchMedicalReports(MedicalResponse response) {
        if (response.getMedicalReports() != null) {
            medicalReports.clear();
            medicalReports.addAll(response.getMedicalReports());
            tvLoadMore.setVisibility(View.GONE);
            medicalReportAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(MEDICAL);
    }

    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
        Log.d("LMS Records Fragment", "onGenerateLabPdf");
        Log.d("LMS Records Fragment", "Response is" + response.toString());
        if (mParam1.equalsIgnoreCase("lab")) {
            responseBody = response;
            initDownloadFileFlow(LAB);
        } else {
            responseBody = response;
            initDownloadFileFlow(SICK);
        }
    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {

    }

//    @Override
//    public void onSmartReportGenerateLabPdf(ResponseBody response, Headers headers) {
//        downloadFileSmart(response, headers);
//    }


    @Override
    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> lstOfLabReportParentMedicus, int totalItems) {
        // added by @Pm...
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        edsearch.setVisibility(View.GONE);
        mainNoData.setVisibility(View.GONE);

        if (lstOfLabReportParentMedicus != null && !lstOfLabReportParentMedicus.isEmpty()) {
            if (page == 0)
                reportsMedicus.clear();

            if (swipeToRefresh.isRefreshing()) {
                reportsMedicus.clear();
                swipeToRefresh.setRefreshing(false);

            }

            reportsMedicus.addAll(lstOfLabReportParentMedicus);
//            reportsMedicus.get(0).getLstOfChildMedicus().get(0).getAccessionNum();
            mLabReportHeaderAdapter.setAllData(lstOfLabReportParentMedicus);

            if (page == 0)
                if (reportsMedicus.size() == 0 && edsearch.getText().toString().trim().isEmpty()) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    if (reportsMedicus.size() == 0 && !edsearch.getText().toString().trim().isEmpty()) {
                        mainNoData.setVisibility(View.VISIBLE);
                    } else {
                        ivFilter.setVisibility(View.VISIBLE);
                        mainContent.setVisibility(View.VISIBLE);
                        mainNoData.setVisibility(View.GONE);
                        mainTimeOut.setVisibility(View.GONE);
                        mainNoNet.setVisibility(View.GONE);
                        edsearch.setVisibility(View.VISIBLE);
                    }
                    tvTotal.setText(totalItems + "");

                }

            total_items = lstOfLabReportParentMedicus.size();
            totalReportsItems = totalItems;

            //in case of showing button again
            if (totalItems > reportsMedicus.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                wasVisible = true;
                page = page + 1;
                isLoading = true;

            } else {
                tvLoadMore.setVisibility(View.GONE);
                wasVisible = false;
            }

            mLabReportHeaderAdapter.notifyDataSetChanged();
        } else {
            rvRecords.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
            totalReportTitle.setVisibility(View.GONE);
            tvTotal.setVisibility(View.GONE);


        }

//        if (response.getMessage().equalsIgnoreCase("no_net")) {
//            edSearch.setVisibility(View.GONE);
//        } else {
//            edSearch.setVisibility(View.VISIBLE);
//        }

//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//        tvLoadMore.setEnabled(true);
        tvLoadMore.setEnabled(true);

    }

    @Override
    public void onGetPin(PinResponse response) {
        Log.e("LabReportsFragment", "onGetPin method called....");
        handleLogin(response.getData().getPin());
    }

    @Override
    public void onGenerateSmartReport(ResponseBody response, Headers headers) {
        Log.d("LMS Records Fragment", "Inside onGenerateSmartReport: ");
        downloadSmartFile(response, headers);
    }

    private void downloadFileSmart(List<SmartLabReport> lstOfParentLabReports) {
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
                if (reports.size() == 0 && medicalReports.size() == 0 && sick_reports.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
//                    Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }

            } else {
//                Common.makeToast(getActivity(), msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);

        if (reports.size() == 0 && medicalReports.size() == 0 && sick_reports.size() == 0) {
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
//            //onFail("Report Downloaded");
//            saveToLocal(path);
//
//            // Show download success and ask to open file
////            openFile(path);
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//            startActivity(intent);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }


    // save path in db
//    void saveToLocal(Uri uri) {
//        db.labReportDataDao().saveLabLocalPath(uri.toString(), spec_number, mainTest);// SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        spec_number = "";
//        mainTest = "";
//    }


    //Save lab report data in storage
//    void downloadFileR(ResponseBody responseBody, Headers headers) {
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
//            saveToLocalR(path);
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

    /*
     * save smart report data in storage
     * */
    void downloadSmartFile(ResponseBody responseBody, Headers headers) {
        Log.d("LMS Records Fragment", "Inside downloadSmartFile: ");

        String jsonFile = "";
        try {

//            SmartReportResponseResult response = new SmartReportResponseResult();
            JSONObject jsonObject = new JSONObject(responseBody.string());

            Log.d("LMS Records Fragment", "downloadSmartFile: json Data length" + jsonObject.length());
            Log.d("LMS Records Fragment", "downloadSmartFile: json Data " + jsonObject.get("data"));
            JSONObject jsonDataObject = jsonObject.getJSONObject("data");
            jsonFile = jsonDataObject.getString("file");
            Log.d("LMS Records Fragment", "downloadSmartFile: JSON File");
            Log.d("LMS Records Fragment", "downloadSmartFile: JSON File --> " + jsonFile);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d("LMS Records Fragment", "downloadSmartFile: Error: " + e.getLocalizedMessage());
        }

        storetoPdfandOpen(getContext(), jsonFile);

        /*try {
            int count = 0;
            String depo = headers.get("Content-Disposition");
            String[] depoSplit = depo.split("filename=");
            String filename = String.valueOf(System.currentTimeMillis());//depoSplit[1].replace("filename=", "").replace("\"", "").trim();
            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";

            byte[] pdfData = Base64.encode(jsonFile.getBytes(), Base64.DEFAULT);


            // download the file
            InputStream input = new BufferedInputStream(new ByteArrayInputStream(pdfData), 8192);
            String path = Environment.getExternalStorageDirectory().toString() + Constants.FOLDER_NAME;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            path = file.getPath() + "/" + filename;
            // Output stream
            OutputStream output = new FileOutputStream(path);

            byte[] data = new byte[1024];

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            //onFail("Report Downloaded");
            saveToLocalSmartReport(path);

            Log.d("LMS Records Fragment", "downloadSmartFile: Path is "+path);

            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));

            startActivity(intent);

//            openFile(path);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }*/

    }


    // save path in db
    void saveToLocalR(Uri uri) {
//        db.medicalReportDataDao().saveLabLocalPath(uri.toString(), product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        product_code = "";
//        orderNumLin = "";
    }

    // to ask include diagnosis or not
    void diagnosis(String leave_id, String hospId) {
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
                        Integer.parseInt(hospId)
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
                        Integer.parseInt(hospId));
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#005497"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            }
        });
        alert.show();
    }

//    void oldDownloadFileS(ResponseBody responseBody, Headers headers) {
//                try {
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
//            saveToLocalS(path);
//
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//
//            startActivity(intent);
//            openFile(path);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case LAB:
                String labFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
                createFile(labFile, CREATE_LAB_REPORT_FILE);
                break;
            case SICK:
                String sickFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_sick_leave_" + System.currentTimeMillis() + ".pdf";
                createFile(sickFile, CREATE_SICK_REPORT_FILE);
                break;
            case MEDICAL:
                String medicalFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
                createFile(medicalFile, CREATE_MEDICAL_REPORT_FILE);
                break;
            case SMART:
//                startActivityForResult(intent, CREATE_SMART_REPORT_FILE);
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
                saveToLocalS(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
                startActivity(intent);
            }
        } else if (requestCode == CREATE_LAB_REPORT_FILE) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);
//                saveToLocal(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
                startActivity(intent);
            }
        } else if (requestCode == CREATE_MEDICAL_REPORT_FILE) {
            Uri uri = null;
            uri = resultData.getData();

            alterDocument(uri);
            saveToLocalR(uri);

            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
            intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
            startActivity(intent);

        } else if (requestCode == CREATE_SMART_REPORT_FILE) {

        }
    }

    // save path in db
    void saveToLocalS(String path) {
//        db.sickLeaveDataDao().saveSickLeavePath(path, sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        sick_leave_id = 0;
    }

    void saveToLocalS(Uri uri) {
//        db.sickLeaveDataDao().saveSickLeavePath(uri.toString(), sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        sick_leave_id = 0;
    }

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

    private void handleLogin(String pin) {
//        String mrnNo = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "");
//        showLoading();
//        OnlinePatient onlinePatient = OnlinePatient.create(false);
//        onlinePatient.loginWithPinAsync(mrnNo,//"Razia" 089599
//                pin, // "23A593K",//2A46FKD
//                SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH),
//                new LoginWithPinDelegate() {
//                    @Override
//                    public void onLoginWithPinSuccess(Patient patient) {
//                        Log.e("LabReportFragment", "onLoginWithPinSuccess method called ...");
////                          requireActivity().runOnUiThread(() -> hideLoading());
//
//
//                        int id = patient.getPatientRecord().getId();
//                        SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_PATIENT_ID, id);
//                        MCPatient.getInstance().setCurrent(patient);
////
//
//                        MCPatient.getInstance().get().syncWithServer(new SyncWithServerDelegate() {
//                            @Override
//                            public void onSyncWithServerSuccess() {
//                                Log.e("LabReportFragment", "onSyncWithServerSuccess method called ...");
//                                //    openSmartReports();
//                                requireActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        openSmartReports();
//                                        hideLoading();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onSyncWithServerFailure(String errorMessage) {
//                                Log.e("LabReportFragment", "onSyncWithServerFailure method called ...");
//                                Log.e("LabReportFragment", "onSyncWithServerFailure errormesg : " + errorMessage);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onLoginWithPinFailure(LoginWithPinErrors errors) {
//                        Log.e("LabReportFragment", "onLoginWithPinFailure method called ...");
//                        requireActivity().runOnUiThread(() -> {
//                            hideLoading();
//                            onFail(errors.getErrorMessage());
//                            //....//
//                        });
//                    }
//                });
    }

/*    public void callGenerateSmartLabPdf(String token, String mr_number, String orderNumber, String language) {

        //  views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mr_number);
            object.put("orderNumber", orderNumber);
            object.put("language", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        Call<ResponseBody> xxx = webService.generateSmartLabReport(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        *//*JSONObject json = new JSONObject();
                        JSONObject json_LL = null;
                        try {
                           *//**//* json_LL = json.getJSONObject("data");
                            String str_value = json_LL.getString("file");
                            Log.e("FILE_IS", "FILE_IS" + str_value);*//**//*
                            downloadFileSmart(response1, headers);

                        } catch (Exception e) {
                            Log.d("LMS Records Fragment: ", "onResponse: Exception is: "+ e.toString());
                            e.printStackTrace();
                        }*//*
                    } else {
                        views.onFail(getActivity().getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(getActivity().getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("LMS Records Fragment: ", "onFailure: "+call.toString());
//                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(getActivity());
//                    message = activity.getString(R.string.no_net_title);
//                    views.onFail(message);
                } else {
                    message = "Unknown";
                    views.onFail(message);

                }
            }
        });
//        views.hideLoading();
    }*/

    //Save lab report data in storage
//    void downloadFileSmart(ResponseBody responseBody, Headers headers) {
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
//            //onFail("Report Downloaded");
//            saveToLocal(path);
//
//            // Show download success and ask to open file
////            openFile(path);
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//            startActivity(intent);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    public void storetoPdfandOpen(Context context, String base) {
        String root = Environment.getExternalStorageDirectory().toString();

        Log.d("ResponseEnv", root);

        File myDir = new File(root + Constants.FOLDER_NAME);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String fname = "imc-" + System.currentTimeMillis() + ".pdf";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {

            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = Base64.decode(base, 0);
            out.write(pdfAsBytes);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

//        String path = Environment.getExternalStorageDirectory().toString() + Constants.FOLDER_NAME;

        File dir = new File(Environment.getExternalStorageDirectory(), Constants.FOLDER_NAME);
        File imgFile = new File(dir, fname);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);

        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse("file://" + imgFile); // My work-around for new SDKs, causes ActivityNotFoundException in API 10.
        }

        Log.d("LMS Records Fragment", "storetoPdfandOpen: absolute Path " + imgFile.getAbsolutePath());
        Log.d("LMS Records Fragment", "storetoPdfandOpen: get Path " + imgFile.getPath());

        Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
        intent.putExtra(Constants.IntentKey.INTENT_LINK, imgFile.getPath());
        intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));

        startActivity(intent);

/*        sendIntent.setDataAndType(uri, "application/pdf");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(sendIntent);*/
    }

    @Override
    public void onItemClick(int position, View view, String tappedItemName, int quantity) {
        switch (view.getId()) {
            case R.id.btn_smart_report:
//                ((SingleFragmentActivity) getContext()).addFragment(tappedItemName);
        }
    }
}

//package sa.med.imc.myimc.Records.view;
//
//import android.Manifest;
//
//import androidx.annotation.NonNull;
//import androidx.room.Room;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Environment;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.core.widget.NestedScrollView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import ai.medicus.insights.InsightButtonActionType;
//import ai.medicus.insights.InsightCardActionType;
//import ai.medicus.insights.InsightsCallback;
//import ai.medicus.medicuscore.Answer;
//import ai.medicus.medicuscore.DoPatientProgramItem;
//import ai.medicus.medicuscore.DoctorNote;
//import ai.medicus.medicuscore.LoginWithPinDelegate;
//import ai.medicus.medicuscore.LoginWithPinErrors;
//import ai.medicus.medicuscore.OnlinePatient;
//import ai.medicus.medicuscore.Patient;
//import ai.medicus.medicuscore.PatientCoachingStep;
//import ai.medicus.medicuscore.PatientInsight;
//import ai.medicus.medicuscore.ProfileQuestion;
//import ai.medicus.medicuscore.SyncWithServerDelegate;
//import ai.medicus.medicuscore.WBListInsight;
//import ai.medicus.smartreport.SmartReport;
//import ai.medicus.smartreport.SmartReportCallback;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import okhttp3.Headers;
//import okhttp3.ResponseBody;
//import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
//import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
//import sa.med.imc.myimc.Adapter.RecordsAdapter;
//import sa.med.imc.myimc.Adapter.SickLeaveAdapter;
//import sa.med.imc.myimc.Base.MCPatient;
//import sa.med.imc.myimc.Database.ImcDatabase;
//import sa.med.imc.myimc.Interfaces.FragmentAdd;
//import sa.med.imc.myimc.Network.Constants;
//import sa.med.imc.myimc.Network.ImcApplication;
//import sa.med.imc.myimc.Network.SharedPreferencesUtils;
//import sa.med.imc.myimc.R;
//import sa.med.imc.myimc.Records.model.CardioReportResponse;
//import sa.med.imc.myimc.Records.model.LabReport;
//import sa.med.imc.myimc.Records.model.LabReportResponse;
//import sa.med.imc.myimc.Records.model.LabReportsMedicus;
//import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
//import sa.med.imc.myimc.Records.model.MedicalReport;
//import sa.med.imc.myimc.Records.model.MedicalResponse;
//import sa.med.imc.myimc.Records.model.OperativeResponse;
//import sa.med.imc.myimc.Records.model.PinResponse;
//import sa.med.imc.myimc.Records.model.SickLeave;
//import sa.med.imc.myimc.Records.model.SickLeaveResponse;
//import sa.med.imc.myimc.Records.presenter.ReportsImpl;
//import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
//import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
//import sa.med.imc.myimc.Utils.Common;
//import sa.med.imc.myimc.Utils.CustomTypingEditText;
//import sa.med.imc.myimc.Utils.RecyclerItemClickListener;
//
//public class LMSRecordsFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    @BindView(R.id.rv_records)
//    RecyclerView rvRecords;
//    @BindView(R.id.tv_load_more)
//    TextView tvLoadMore;
//    @BindView(R.id.main_content)
//    RelativeLayout mainContent;
//    @BindView(R.id.bt_try_again)
//    Button btTryAgain;
//    @BindView(R.id.main_no_net)
//    RelativeLayout mainNoNet;
//    @BindView(R.id.bt_try_again_time_out)
//    Button btTryAgainTimeOut;
//    @BindView(R.id.main_time_out)
//    RelativeLayout mainTimeOut;
//    @BindView(R.id.main_no_data_trans)
//    RelativeLayout mainNoData;
//    @BindView(R.id.ed_search)
//    CustomTypingEditText edSearch;
//    boolean edit = false;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;
//    @BindView(R.id.progress_bar_load)
//    ProgressBar progressBarLoad;
//    @BindView(R.id.tv_total)
//    TextView tvTotal;
//    @BindView(R.id.swipeToRefresh)
//    SwipeRefreshLayout swipeToRefresh;
//    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.iv_search)
//    ImageView ivSearch;
//    @BindView(R.id.iv_filter)
//    ImageView ivFilter;
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.total_report_title)
//    TextView totalReportTitle;
//    @BindView(R.id.nestedScroll)
//    NestedScrollView nestedScroll;
//
//
//    private String mParam1;
//
//    RecordsAdapter adapter;
//    LabReportHeaderAdapter mLabReportHeaderAdapter;
//
//
//    int page = 0, total_items = 0;
//    boolean wasVisible = false, isSearched = false;
//    String spec_number = "", mainTest = "", episodeId = "", from_date = "", to_date = "";
//
//    // App database
//    ImcDatabase db;
//
//    ReportsPresenter presenter;
//    List<LabReport> reports = new ArrayList<>();
//    List<LabReportsParentMedicus> reportsMedicus = new ArrayList<>();
//
//
//    FragmentAdd fragmentAdd;
//
//    MedicalReportAdapter medicalReportAdapter;
//    List<MedicalReport> medicalReports = new ArrayList<>();
//    String product_code = "", orderNumLin = "";
//    Boolean isLoading = false;
//
//    int sick_leave_id = 0;
//
//    SickLeaveAdapter sick_adapter;
//    List<SickLeave> sick_reports = new ArrayList<>();
//    BottomReportFilterDialog bottomReportFilterDialog;
//
//    private static boolean showMedicus =true;// for live it should be FALSE ,For debug it should be  TRUE
//    private Unbinder unBinder;
//    private String mAccessionNum;
//
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        fragmentAdd = (FragmentAdd) getActivity();
//    }
//
//
//    public LMSRecordsFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static LMSRecordsFragment newInstance() {
//        LMSRecordsFragment fragment = new LMSRecordsFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         View view = inflater.inflate(R.layout.fragment_lms, container, false);
//         unBinder=ButterKnife.bind(this, view);
//         return view;
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        presenter = new ReportsImpl(this, getActivity());
//        db = Room.databaseBuilder(getActivity(), ImcDatabase.class, "imc_database").allowMainThreadQueries().build();
//        ivFilter.setImageResource(R.drawable.ic_filter);
//
//        page = 0;
//        reports = new ArrayList<>();
//        medicalReports = new ArrayList<>();
//        sick_reports = new ArrayList<>();
//        isSearched = false;
//        edit = false;
//        ivSearch.setImageResource(R.drawable.ic_search);
//        edSearch.setVisibility(View.GONE);
//
//        bottomReportFilterDialog = null;
//
//        from_date = "";
//        to_date = "";
//        isLoading = false;
//
//        mParam1 = "";
//        episodeId = "";
////
//        if (getArguments() != null) {
//            edSearch.setText("");
//
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            episodeId = getArguments().getString(ARG_PARAM2);
//        }
//        loadData();
//
//        edSearch.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                page = 0;
////                callAPI();
//                if (getActivity() != null)
//                    Common.hideSoftKeyboard(getActivity());
//
//                return true;
//            }
//
//            return false;
//        });
//
//        edSearch.setOnTypingModified((view1, isTyping) -> {
//
//            if (!isTyping) {
//                if (edSearch.getText().toString().trim().length() > 0) {
//                    page = 0;
//                    callAPI();
//                }
//            }
//        });
//
//        swipeToRefresh.setOnRefreshListener(() -> {
//            page = 0;
//            tvLoadMore.setVisibility(View.GONE);
//            callAPI();
//        });
//
//
//    }
//
//    void loadData() {
//
//        totalReportTitle.setVisibility(View.GONE);
//        if (mParam1.equalsIgnoreCase("lab")) {
//            toolbarTitle.setText(getString(R.string.lab_reports));
//            totalReportTitle.setText(getString(R.string.total_lab_reports));
//            setUpRecordsRecyclerView();
//
//        } else if (mParam1.equalsIgnoreCase("radio")) {
//            toolbarTitle.setText(getString(R.string.radiology_reports));
//            totalReportTitle.setText(getString(R.string.total_lab_radio));
//            setUpMedRecordsRecyclerView();
//
//        } else if (mParam1.equalsIgnoreCase("sick")) {
//            toolbarTitle.setText(getString(R.string.sick_leave_report));
//            totalReportTitle.setText(getString(R.string.total_lab_sick));
//            setUpSickRecordsRecyclerView();
//        }
//
//        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
//
//                    //Reaches to the bottom so it is time to updateRecyclerView!
//                    if (mParam1.equalsIgnoreCase("lab")) {
//
//                        if(showMedicus){
//                            if (reportsMedicus.size() < total_items && isLoading) {
//                                isLoading = false;
//                                progressBarLoad.setVisibility(View.VISIBLE);
//                                callAPI();
//                            }
//                        }else{
//                            if (reports.size() < total_items && isLoading) {
//                                isLoading = false;
//                                progressBarLoad.setVisibility(View.VISIBLE);
//                                callAPI();
//                            }
//
//                        }
//
//
//                    } else if (mParam1.equalsIgnoreCase("radio")) {
//                        if (medicalReports.size() < total_items && isLoading) {
//                            isLoading = false;
//                            progressBarLoad.setVisibility(View.VISIBLE);
//                            callAPI();
//                        }
//                    } else if (mParam1.equalsIgnoreCase("sick")) {
//                        if (sick_reports.size() < total_items && isLoading) {
//                            isLoading = false;
//                            progressBarLoad.setVisibility(View.VISIBLE);
//                            callAPI();
//                        }
//                    }
//                }
//            }
//        });
//
//        callAPI();
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        if(unBinder!=null) unBinder.unbind();
//        super.onDestroyView();
//    }
//
//    @OnClick({R.id.iv_search, R.id.iv_filter, R.id.iv_back, R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
////            case R.id.iv_back_list:
////
////                if (fragmentAdd != null)
////                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
////
////                break;
////
//            case R.id.iv_back:
////                if (episodeId.length() > 0) {
//                getActivity().finish();
////                } else {
////                    mainContent.setVisibility(View.GONE);
////                    layListReports.setVisibility(View.VISIBLE);
////
////                }
//                break;
//
//            case R.id.iv_search:
//                if (edit) {
//                    ivSearch.setImageResource(R.drawable.ic_search);
//                    edSearch.setVisibility(View.GONE);
//
//                    // Clear search and load all reports
//                    if (isSearched) {
//                        edSearch.setText("");
//
//                        if (mParam1.equalsIgnoreCase("lab")) {
//                            page = 0;
//                            callAPI();
//                        } else if (mParam1.equalsIgnoreCase("radio")) {
//                            page = 0;
//                            callAPI();
//                        }
//                    }
//
//                    edit = false;
//                } else {
//                    ivSearch.setImageResource(R.drawable.ic_close);
//                    edSearch.setVisibility(View.VISIBLE);
//                    edit = true;
//                    edSearch.requestFocus();
//                    if (getActivity() != null) {
//                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
//                    }
//                }
//                break;
//
//            case R.id.bt_try_again:
//                callAPI();
//                break;
//
//            case R.id.bt_try_again_time_out:
//                callAPI();
//                break;
//
//            case R.id.tv_load_more:
//                tvLoadMore.setEnabled(false);
//
//                progressBarLoad.setVisibility(View.VISIBLE);
//                callAPI();
//                break;
//
//            case R.id.iv_filter:
//                if (bottomReportFilterDialog == null)
//                    bottomReportFilterDialog = BottomReportFilterDialog.newInstance();
//
//                if (!bottomReportFilterDialog.isAdded())
//                    bottomReportFilterDialog.show(getChildFragmentManager(), "DAILOG");
//                break;
//
//        }
//    }
//
//    //Call Api to get reports
//    void callAPI() {
//        isSearched = edSearch.getText().toString().length() > 0;
//
//        if (mParam1.equalsIgnoreCase("lab")) {
//            callAPILab();
//        } else if (mParam1.equalsIgnoreCase("radio")) {
//            callAPIRadio();
//        } else if (mParam1.equalsIgnoreCase("sick")) {
//            callAPISick();
//        }
//
//    }
//
//    // API for radio reports
//    void callAPIRadio() {
//        if (page == 0) {
//            if (edSearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
//                progressBar.setVisibility(View.VISIBLE);
//            else
//                progressBar.setVisibility(View.GONE);
//
//            mainContent.setVisibility(View.VISIBLE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//        presenter.callGetAllMedicalReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//
//    }
//
//    // API for lab reports
//    void callAPILab() {
//        if (page == 0) {
//            if (edSearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
//                progressBar.setVisibility(View.VISIBLE);
//            else
//                progressBar.setVisibility(View.GONE);
//
//            mainContent.setVisibility(View.VISIBLE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//
//        if(showMedicus==false){
//            presenter.callGetAllLabReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//
//        }else{
//            presenter.callGetAllLabReportsMedicus(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//
//
//        }
//    }
//
//    //Call Api to get sick reports
//    void callAPISick() {
//        if (page == 0) {
//            if (edSearch.getText().toString().length() > 0 && !swipeToRefresh.isRefreshing())
//                progressBar.setVisibility(View.VISIBLE);
//            else
//                progressBar.setVisibility(View.GONE);
//
//            mainContent.setVisibility(View.VISIBLE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//
//        presenter.callGetAllSickLeaveReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//
//    }
//
//
//    // Initialize list view and add adapter to display data,
//    void setUpRecordsRecyclerView() {
//        if(showMedicus){
//            mLabReportHeaderAdapter=new LabReportHeaderAdapter(reportsMedicus);
//            rvRecords.setAdapter(mLabReportHeaderAdapter);
//            mLabReportHeaderAdapter.setOnItemClickListener(new LabReportHeaderAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int outerPosition,int innerPosition, LabReportsMedicus model) {
//                    //  Toast.makeText(getActivity(),"pos: "+position,Toast.LENGTH_LONG).show();
//                    if (reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getStatus() == 0) {
//                        onFail(getString(R.string.no_result_available));
//                    } else {
//
//                        Dexter.withContext(getActivity())
//                                .withPermissions(
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                        Manifest.permission.READ_EXTERNAL_STORAGE
//                                ).withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//
//                                if (reportP.areAllPermissionsGranted()) {
//                                    // Fetch selected report from db
//                                    spec_number = reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum();
//                                    mainTest =  reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getMainTest();
//                                    LabReport report = db.labReportDataDao().getSelectReport(spec_number,mainTest); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                                    // check if report path saved in local b then open from local rather than downloading again
//                                    if (report != null) {
//                                        if (report.getLocalPath().isEmpty())
//                                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                    reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getReportType(),
//                                                    reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum());
//                                        else {
//                                            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//                                            startActivity(intent);
//                                        }
//                                    } else {
//                                        presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getReportType(),
//                                                reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getSpecimenNum());
//
//                                    }
//                                } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                                    Common.permissionDialog(getActivity());
//                                }
//
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                                /* ... */
//                            }
//                        }).check();
//                    }
//                }
//
//                @Override
//                public void onItemMoreClick(LabReportsParentMedicus model, int item) {
//                    mAccessionNum=model.getLstOfChildMedicus().get(0).getAccessionNum();
//                    //  Toast.makeText(getActivity(),""+mAccessionNum,Toast.LENGTH_LONG).show();
//                    if (MCPatient.getInstance().getPatientRecord() != null) {
//                        openSmartReports();
//                    } else
//                        presenter.callGetPin(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                }
//            });
//
//        }else {
//            adapter = new RecordsAdapter(getContext(), reports);
//            adapter.setOnItemClickListener(new RecordsAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//
//                    if (reports.get(position).getStatus() == 0) {
//                        onFail(getString(R.string.no_result_available));
//                    } else {
//
//                        Dexter.withContext(getActivity())
//                                .withPermissions(
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                        Manifest.permission.READ_EXTERNAL_STORAGE
//                                ).withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//                                if (reportP.areAllPermissionsGranted()) {
//                                    // Fetch selected report from db
//                                    spec_number = reports.get(position).getSpecimenNum();
//                                    mainTest = reports.get(position).getMainTest();
//                                    LabReport report = db.labReportDataDao().getSelectReport(spec_number, reports.get(position).getMainTest()); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                                    // check if report path saved in local b then open from local rather than downloading again
//                                    if (report != null) {
//                                        if (report.getLocalPath().isEmpty())
//                                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                    reports.get(position).getReportType(), reports.get(position).getSpecimenNum());
//                                        else {
//                                            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//                                            startActivity(intent);
//                                        }
//                                    } else {
//                                        presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                reports.get(position).getReportType(), reports.get(position).getSpecimenNum());
//
//                                    }
//                                } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                                    Common.permissionDialog(getActivity());
//                                }
//
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
//                                    token) {
//                                token.continuePermissionRequest();
//                                /* ... */
//                            }
//                        }).check();
//                    }
//                }
//
//
//            });
//            rvRecords.setAdapter(adapter);
//        }
//
//        rvRecords.setHasFixedSize(true);
//        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
//
//    }
//
//
//    // Initialize Medical list view and add adapter to display data,
//    void setUpMedRecordsRecyclerView() {
//        medicalReportAdapter = new MedicalReportAdapter(getContext(), medicalReports);
//        rvRecords.setHasFixedSize(true);
//        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvRecords.setAdapter(medicalReportAdapter);
//        medicalReportAdapter.setOnItemClickListener(new MedicalReportAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
////        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
////            @Override
////            public void onItemClick(View view, int position) {
//                if (medicalReports.get(position).getResultAvailable() == 0) {
//                    onFail(getString(R.string.no_result_available));
//                } else {
//
//                    Dexter.withContext(getActivity())
//                            .withPermissions(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE
//                            ).withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//                            if (reportP.areAllPermissionsGranted()) {
//                                // Fetch selected report from db
//                                product_code = medicalReports.get(position).getId().getOrderNo();
//                                orderNumLin = medicalReports.get(position).getId().getOrderNoLine();
//                                MedicalReport report = db.medicalReportDataDao().getSelectReport(product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                                // check if report path saved in local b then open from local rather than downloading again
//
//                                if (report != null) {
//                                    if (report.getLocalPath().isEmpty())
//                                        presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                                medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine());
//                                    else {
//                                        Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                        intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                        intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
//
//                                        startActivity(intent);
//                                    }
//                                } else {
//                                    presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                            medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine());
//                                }
//
//                            } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                                Common.permissionDialog(getActivity());
//                            }
//
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
//                                token) {
//                            token.continuePermissionRequest();/* ... */
//                        }
//                    }).check();
//                }
////            }
////
////        }));
//
//            }
//        });
//    }
//
//    // Initialize list view and add adapter to display data,
//    void setUpSickRecordsRecyclerView() {
//        sick_adapter = new SickLeaveAdapter(getContext(), sick_reports);
//        rvRecords.setHasFixedSize(true);
//        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvRecords.setAdapter(sick_adapter);
//        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Dexter.withContext(getActivity())
//                        .withPermissions(
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE
//                        ).withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport reportP) {
//
//                        if (reportP.areAllPermissionsGranted()) {
//
//                            // Fetch selected sick leave from db
//                            sick_leave_id = sick_reports.get(position).getId();
//                            SickLeave report = db.sickLeaveDataDao().getSelectSickLeave(sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//
//                            // check if report path saved in local db then open from local rather than downloading again
//                            if (report != null) {
//
//                                if (report.getLocalPath().isEmpty())
//                                    diagnosis(String.valueOf(sick_reports.get(position).getId()));
//                                else {
//                                    Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                                    intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                                    intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                                    intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//
//                                    startActivity(intent);
//                                }
//                            } else {
//                                diagnosis(String.valueOf(sick_reports.get(position).getId()));
//                            }
//
//                        } else if (reportP.isAnyPermissionPermanentlyDenied()) {
//                            Common.permissionDialog(getActivity());
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken
//                            token) {
//                        token.continuePermissionRequest();
//                        /* ... */
//                    }
//                }).check();
//            }
//        }));
//    }
//
//
//    @Override
//    public void onGetLabReports(LabReportResponse response) {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//
//        if (response.getLabReports() != null) {
//            if (page == 0)
//                reports.clear();
//
//            if (swipeToRefresh.isRefreshing()) {
//                reports.clear();
//                swipeToRefresh.setRefreshing(false);
//
//            }
//
//            reports.addAll(response.getLabReports());
//            adapter.setAllData(response.getLabReports());
//
//            if (page == 0)
//                if (reports.size() == 0) {
////                    onFail(getString(R.string.no_data_available));
//                    tvTotal.setText("");
//                    totalReportTitle.setVisibility(View.GONE);
//
////                    mainContent.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.VISIBLE);
////                    mainTimeOut.setVisibility(View.GONE);
////                    mainNoNet.setVisibility(View.GONE);
//                } else {
//                    ivFilter.setVisibility(View.VISIBLE);
//                    ivSearch.setVisibility(View.VISIBLE);
//
//                    mainContent.setVisibility(View.VISIBLE);
//                    mainNoData.setVisibility(View.GONE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
//                    totalReportTitle.setVisibility(View.VISIBLE);
//                    tvTotal.setText(response.getTotal_items() + "");
//                }
//
//            if (response.getTotal_items().length() > 0) {
//                total_items = Integer.parseInt(response.getTotal_items());
//                isLoading = true;
//            }
//
//            if (Integer.parseInt(response.getTotal_items()) > reports.size()) {
//                tvLoadMore.setVisibility(View.GONE);//VISIBLE
//                wasVisible = true;
//                page = page + 1;
//            } else {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = false;
//            }
//
//            adapter.notifyDataSetChanged();
////        } else {
////            mainContent.setVisibility(View.GONE);
////            mainNoData.setVisibility(View.VISIBLE);
////            mainTimeOut.setVisibility(View.GONE);
////            mainNoNet.setVisibility(View.GONE);
//        } else {
//            tvTotal.setText("");
////            onFail(getString(R.string.no_data_available));
//            totalReportTitle.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//
//
//        }
//
//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//
//        tvLoadMore.setEnabled(true);
//
//    }
//    void openSmartReports() {
//        Log.e("LabReportFragment","openSmartReports method called ..." );
//        // open smart reports................
//        //   showLoading();
//        //  MCPatient.getInstance().get().generateMetadata();
//        //   MCPatient.getInstance().get().generateInsights();
//        SmartReport.Companion.configure(MCPatient.getInstance().get(), new InsightsCallback() {
//            @Override
//            public void onInsightClicked(Activity activity, PatientInsight patientInsight, WBListInsight wbListInsight, DoctorNote doctorNote, InsightCardActionType insightCardActionType, String s, Integer integer) {
//
//
//
//            }
//
//            @Override
//            public void onInsightButtonClicked(Activity activity, PatientInsight patientInsight, InsightButtonActionType insightButtonActionType) {
//
//            }
//
//            @Override
//            public void onInsightSwiped(Activity activity, PatientInsight patientInsight) {
//
//            }
//
//            @Override
//            public void onIconClicked(Activity activity, PatientInsight patientInsight) {
//
//            }
//
//            @Override
//            public void onCancelDoctorAppointment(Activity activity, PatientInsight patientInsight, Integer integer) {
//
//            }
//
//            @Override
//            public void onListQuestionAnswered(Activity activity, boolean b, String s, Integer integer) {
//
//            }
//
//            @Override
//            public void onFamilyHistoryQuestionAnswered(Activity activity, boolean b, Answer answer) {
//
//            }
//
//            @Override
//            public void onNormalQuestionAnswered(Activity activity, String s, String s1) {
//
//            }
//
//            @Override
//            public void onUnderneathBiomarkerClicked(Activity activity, String s, String s1, int i) {
//
//            }
//
//            @Override
//            public void onStepsNeeded(ViewGroup viewGroup) {
//
//            }
//
//            @Override
//            public void configureSteps(ArrayList<PatientCoachingStep> arrayList) {
//
//            }
//
//            @Override
//            public void onCoachingProgramsViewHolderNeeded(ViewGroup viewGroup, Activity activity, String s) {
//
//            }
//
//            @Override
//            public void configureCoachingPrograms(ArrayList<DoPatientProgramItem> arrayList) {
//
//            }
//
//            @Override
//            public void onProgramsHeaderNeeded(ViewGroup viewGroup) {
//
//            }
//
//            @Override
//            public void configureProgramsHeader() {
//
//            }
//
//            @Override
//            public void onBiomarkerInfoClicked(String s, Activity activity) {
//
//            }
//
//            @Override
//            public void onAddBiomarkerClicked(String s, Activity activity) {
//
//            }
//
//            @Override
//            public void onGoToProfileClicked(ProfileQuestion profileQuestion, Activity activity) {
//
//            }
//
//            @Override
//            public void onRescheduleAllNotifications() {
//
//            }
//        }, new SmartReportCallback() {
//            @Override
//            public void onEditReportClicked(Activity activity, int i) {
//
//
//
//            }
//            @Override
//            public void onOpenProfilePopupClicked(Activity activity) {
//
//
//            }
//        },false,false);
//        // hideLoading();
//        Log.e("LabReportFragment","openSmartReports before openReportScreen");
//        SmartReport.Companion.openReportDetailsScreenByNumber(requireActivity(),mAccessionNum); //B557892  //B4359156
//        Log.e("LabReportFragment","openSmartReports after openReportSCreen");
//
//    }
//
//
//    @Override
//    public void onGetMedicalReports(MedicalResponse response) {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//
//
//        if (page == 0)
//            medicalReports.clear();
//
//        if (swipeToRefresh.isRefreshing()) {
//            medicalReports.clear();
//            swipeToRefresh.setRefreshing(false);
//
//        }
//
//        if (response.getMedicalReports() != null) {
//            medicalReports.addAll(response.getMedicalReports());
//            medicalReportAdapter.setAllData(response.getMedicalReports());
//
//            if (page == 0)
//                if (medicalReports.size() == 0) {
//                    tvTotal.setText("");
//                    totalReportTitle.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.VISIBLE);
//                } else {
//                    totalReportTitle.setVisibility(View.VISIBLE);
//                    tvTotal.setText(response.getTotal_items() + "");
//                    ivFilter.setVisibility(View.VISIBLE);
//                    ivSearch.setVisibility(View.VISIBLE);
//
//                    mainContent.setVisibility(View.VISIBLE);
//                    mainNoData.setVisibility(View.GONE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
//
//                }
//
//            if (response.getTotal_items().length() > 0) {
//                total_items = Integer.parseInt(response.getTotal_items());
//                isLoading = true;
//            }
//
//            if (Integer.parseInt(response.getTotal_items()) > medicalReports.size()) {
//                tvLoadMore.setVisibility(View.GONE);//VISIBLE
//                page = page + 1;
//                wasVisible = true;
//            } else {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = false;
//            }
//
//            medicalReportAdapter.notifyDataSetChanged();
//        } else {
//            tvTotal.setText("");
//            totalReportTitle.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//        }
//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//
//        tvLoadMore.setEnabled(true);
//
//    }
//
//    @Override
//    public void onGetSickLeaveReports(SickLeaveResponse response) {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.GONE);
//        ivSearch.setVisibility(View.GONE);
//
//        if (page == 0)
//            sick_reports.clear();
//
//        if (swipeToRefresh.isRefreshing()) {
//            sick_reports.clear();
//            swipeToRefresh.setRefreshing(false);
//        }
//
//        if (response.getSickLeaves() != null) {
//            sick_reports.addAll(response.getSickLeaves());
//            sick_adapter.setAllData(response.getSickLeaves());
//
//            if (page == 0)
//                if (sick_reports.size() == 0) {
//                    tvTotal.setText("");
//                    totalReportTitle.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.VISIBLE);
//
//                } else {
//                    totalReportTitle.setVisibility(View.VISIBLE);
//                    tvTotal.setText(response.getTotalItems() + "");
//                    ivFilter.setVisibility(View.VISIBLE);
//
//                    mainContent.setVisibility(View.VISIBLE);
//                    mainNoData.setVisibility(View.GONE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
//                }
//
//            if (response.getTotalItems().length() > 0) {
//                total_items = Integer.parseInt(response.getTotalItems());
//                isLoading = true;
//            }
//            if (Integer.parseInt(response.getTotalItems()) > sick_reports.size()) {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = true;
//                page = page + 1;
//            } else {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = false;
//            }
//
//            sick_adapter.notifyDataSetChanged();
//        } else {
//            tvTotal.setText("");
//            totalReportTitle.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//
//        }
//
//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//
//        tvLoadMore.setEnabled(true);
//    }
//
//    @Override
//    public void onGetOperativeReports(OperativeResponse response) {
//
//    }
//
//    @Override
//    public void onGetCardioReports(CardioReportResponse reportResponse) {
//
//    }
//
//
//    @Override
//    public void onGetSearchLabReports(LabReportResponse response) {
//        if (response.getLabReports() != null) {
//            reports.clear();
//            reports.addAll(response.getLabReports());
//            tvLoadMore.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onGetSearchMedicalReports(MedicalResponse response) {
//        if (response.getMedicalReports() != null) {
//            medicalReports.clear();
//            medicalReports.addAll(response.getMedicalReports());
//            tvLoadMore.setVisibility(View.GONE);
//            medicalReportAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {
//        downloadFileR(response, headers);
//
//    }
//
//    @Override
//    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
//        if (mParam1.equalsIgnoreCase("lab")) {
//            downloadFile(response, headers);
//        } else {
//            downloadFileS(response, headers);
//        }
//    }
//
//    @Override
//    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> lstOfLabReportParentMedicus) {
//        // added by @Pm...
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        edSearch.setVisibility(View.VISIBLE);
//        mainNoData.setVisibility(View.GONE);
//
//        if (lstOfLabReportParentMedicus != null && !lstOfLabReportParentMedicus.isEmpty()) {
//            if (page == 0)
//                reportsMedicus.clear();
//
//            if (swipeToRefresh.isRefreshing()) {
//                reportsMedicus.clear();
//                swipeToRefresh.setRefreshing(false);
//
//            }
//
//            reportsMedicus.addAll(lstOfLabReportParentMedicus);
//            mLabReportHeaderAdapter.setAllData(lstOfLabReportParentMedicus);
//
//            if (page == 0)
//                if (reportsMedicus.size() == 0 && edSearch.getText().toString().trim().isEmpty()) {
//                    mainContent.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.VISIBLE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
//                } else {
//                    if (reportsMedicus.size() == 0 && !edSearch.getText().toString().trim().isEmpty()) {
//                        mainNoData.setVisibility(View.VISIBLE);
//                    } else {
//                        ivFilter.setVisibility(View.VISIBLE);
//                        mainContent.setVisibility(View.VISIBLE);
//                        mainNoData.setVisibility(View.GONE);
//                        mainTimeOut.setVisibility(View.GONE);
//                        mainNoNet.setVisibility(View.GONE);
//                        edSearch.setVisibility(View.VISIBLE);
//                    }
//                    tvTotal.setText(lstOfLabReportParentMedicus.size() + "");
//
//                }
//
//            total_items = lstOfLabReportParentMedicus.size();
//            if (lstOfLabReportParentMedicus.size() > reportsMedicus.size()) {
//                tvLoadMore.setVisibility(View.GONE);//VISIBLE
//                wasVisible = true;
//                page = page + 1;
//                isLoading = true;
//
//            } else {
//                tvLoadMore.setVisibility(View.GONE);
//                wasVisible = false;
//            }
//
//            mLabReportHeaderAdapter.notifyDataSetChanged();
//        } else {
//            mainContent.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//
////        if (response.getMessage().equalsIgnoreCase("no_net")) {
////            edSearch.setVisibility(View.GONE);
////        } else {
////            edSearch.setVisibility(View.VISIBLE);
////        }
//
//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//        tvLoadMore.setEnabled(true);
//
//    }
//
//    @Override
//    public void onGetPin(PinResponse response) {
//        Log.e("LabReportsFragment","onGetPin method called....");
//        handleLogin(response.getData().getPin());
//    }
//
//    @Override
//    public void showLoading() {
//        Common.showDialog(getActivity());
//    }
//
//    @Override
//    public void hideLoading() {
//        Common.hideDialog();
//    }
//
//    @Override
//    public void onFail(String msg) {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        swipeToRefresh.setRefreshing(false);
//        tvLoadMore.setEnabled(true);
//
//        if (msg != null)
//            if (msg.equalsIgnoreCase("timeout")) {
//                if (reports.size() == 0 && medicalReports.size() == 0 && sick_reports.size() == 0) {
//                    mainContent.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.GONE);
//                    mainTimeOut.setVisibility(View.VISIBLE);
//                    mainNoNet.setVisibility(View.GONE);
//                } else {
//                    Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
//                }
//
//            } else {
//                Common.makeToast(getActivity(), msg);
//            }
//    }
//
//    @Override
//    public void onNoInternet() {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        swipeToRefresh.setRefreshing(false);
//        tvLoadMore.setEnabled(true);
//
//        if (reports.size() == 0 && medicalReports.size() == 0 && sick_reports.size() == 0) {
//            mainContent.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.VISIBLE);
//        } else {
//            Common.noInternet(getActivity());
//        }
//    }
//
//    //Save lab report data in storage
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
//            //onFail("Report Downloaded");
//            saveToLocal(path);
//
//            // Show download success and ask to open file
////            openFile(path);
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//            startActivity(intent);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }
//
//
//    // save path in db
//    void saveToLocal(String path) {
//        db.labReportDataDao().saveLabLocalPath(path, spec_number, mainTest);// SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        spec_number = "";
//        mainTest = "";
//    }
//
//
//    //Save lab report data in storage
//    void downloadFileR(ResponseBody responseBody, Headers headers) {
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
//            saveToLocalR(path);
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
//
//    // save path in db
//    void saveToLocalR(String path) {
//        db.medicalReportDataDao().saveLabLocalPath(path, product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        product_code = "";
//        orderNumLin = "";
//    }
//
//
//    // to ask include diagnosis or not
//    void diagnosis(String leave_id) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.confirmation));
//        builder.setMessage(getResources().getString(R.string.diagnosis));
//        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                presenter.callGenerateSickLeavePdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                        leave_id, "1");
//            }
//        });
//        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                presenter.callGenerateSickLeavePdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                        leave_id, "0");
//            }
//        });
//
//        final AlertDialog alert = builder.create();
//
//        alert.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface arg0) {
//                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#005497"));
//                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
//            }
//        });
//        alert.show();
//    }
//
//
//    //Save sick report data in storage
//    void downloadFileS(ResponseBody responseBody, Headers headers) {
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
//            saveToLocalS(path);
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
//
//    // save path in db
//    void saveToLocalS(String path) {
//        db.sickLeaveDataDao().saveSickLeavePath(path, sick_leave_id); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        sick_leave_id = 0;
//    }
//
//    @Override
//    public void onDone(String from, String to) {
//        from_date = from;
//        to_date = to;
//
//        if (!from_date.isEmpty() && !to_date.isEmpty())
//            ivFilter.setImageResource(R.drawable.ic_filter_green);
//        page = 0;
//        callAPI();
//    }
//
//    @Override
//    public void onClear(String text) {
//        if (from_date.length() > 0 || to_date.length() > 0) {
//            from_date = "";
//            to_date = "";
//            ivFilter.setImageResource(R.drawable.ic_filter);
//
//            page = 0;
//            callAPI();
//        }
//    }
//    private void handleLogin(String pin) {
//        String mrnNo = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "");
//        showLoading();
//        OnlinePatient onlinePatient = OnlinePatient.create(false);
//        onlinePatient.loginWithPinAsync(mrnNo,//"Razia" 089599
//                pin, // "23A593K",//2A46FKD
//                SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH),
//                new LoginWithPinDelegate() {
//                    @Override
//                    public void onLoginWithPinSuccess(Patient patient) {
//                        Log.e("LabReportFragment","onLoginWithPinSuccess method called ..." );
//                        //  requireActivity().runOnUiThread(() -> hideLoading());
//
//                        int id = patient.getPatientRecord().getId();
//                        SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_PATIENT_ID, id);
//                        MCPatient.getInstance().setCurrent(patient);
//
//                        MCPatient.getInstance().get().syncWithServer(new SyncWithServerDelegate() {
//                            @Override
//                            public void onSyncWithServerSuccess() {
//                                Log.e("LabReportFragment","onSyncWithServerSuccess method called ..." );
//                                //    openSmartReports();
//                                requireActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        openSmartReports();
//                                        hideLoading();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onSyncWithServerFailure(String errorMessage) {
//                                Log.e("LabReportFragment","onSyncWithServerFailure method called ..." );
//                                Log.e("LabReportFragment","onSyncWithServerFailure errormesg : "+errorMessage );
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onLoginWithPinFailure(LoginWithPinErrors errors) {
//                        Log.e("LabReportFragment","onLoginWithPinFailure method called ..." );
////                        requireActivity().runOnUiThread(() -> {
////                            hideLoading();
////                            onFail(errors.getErrorMessage());
////                            //....//
////                        });
//                    }
//                });
//    }
//
//}