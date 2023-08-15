package sa.med.imc.myimc.Records.view;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
//4 jan
/*import ai.medicus.insights.InsightButtonActionType;
import ai.medicus.insights.InsightCardActionType;
import ai.medicus.insights.InsightsCallback;*/
/*import ai.medicus.medicuscore.Answer;
import ai.medicus.medicuscore.DoPatientProgramItem;
import ai.medicus.medicuscore.DoctorNote;
import ai.medicus.medicuscore.LoginWithPinDelegate;
import ai.medicus.medicuscore.LoginWithPinErrors;
import ai.medicus.medicuscore.OnlinePatient;
import ai.medicus.medicuscore.Patient;
import ai.medicus.medicuscore.PatientCoachingStep;
import ai.medicus.medicuscore.PatientInsight;
import ai.medicus.medicuscore.PatientReport;
import ai.medicus.medicuscore.ProfileQuestion;
import ai.medicus.medicuscore.SyncWithServerDelegate;
import ai.medicus.medicuscore.WBListInsight;*/
//4 jan
/*import ai.medicus.smartreport.SmartReport;
import ai.medicus.smartreport.SmartReportCallback;
import ai.medicus.smartreport.delegates.SmartReportActionsDelegate;
import ai.medicus.utils.MedicusUtilities;*/
import ai.medicus.insights.InsightButtonActionType;
import ai.medicus.insights.InsightCardActionType;
import ai.medicus.insights.InsightsCallback;
//import ai.medicus.medicuscore.Answer;
//import ai.medicus.medicuscore.DoPatientProgramItem;
//import ai.medicus.medicuscore.DoctorNote;
//import ai.medicus.medicuscore.PatientCoachingStep;
//import ai.medicus.medicuscore.PatientInsight;
//import ai.medicus.medicuscore.ProfileQuestion;
//import ai.medicus.medicuscore.WBListInsight;
import ai.medicus.smartreport.SmartReport;
import ai.medicus.smartreport.SmartReportCallback;
import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
import sa.med.imc.myimc.Adapter.RecordsAdapter;
import sa.med.imc.myimc.Base.MCPatient;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsMedicus;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.model.SmartLabReportResponse;
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

import static sa.med.imc.myimc.Utils.Common.ReportTypes.LAB;


public class LabReportsFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {

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
    @BindView(R.id.main_no_data)
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
    @BindView(R.id.fab_filter)
    FloatingActionButton fab_filter;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;


    RecordsAdapter adapter;
    LabReportHeaderAdapter mLabReportHeaderAdapter;


    int page = 0, total_items = 0;
    boolean wasVisible = false, isSearched = false;
    String spec_number = "", mainTest = "", episodeId = "0", from_date = "", to_date = "";
    Boolean isLoading = false;

    ReportsPresenter presenter;
    List<LabReport> reports = new ArrayList<>();
    List<LabReportsParentMedicus> reportsMedicus = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    BottomReportFilterDialog bottomReportFilterDialog;

    private static boolean showMedicus = true;// todo: for live it should be FALSE ,For debug it should be  TRUE
    private String mAccessionNum;
    private int labPageNumber = 1;
    private String labBookingStatus = "2";

    public LabReportsFragment() {
        // Required empty public constructor
    }


    public static LabReportsFragment newInstance(String param1, String param2) {
        LabReportsFragment fragment = new LabReportsFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab_reports, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setRotationY(180);
        }

        presenter = new ReportsImpl(this, getActivity());
        fab_filter.setImageResource(R.drawable.ic_filter);

        page = 0;
        from_date = "";
        to_date = "";
        isSearched = false;
        edSearch.setText("");
        edSearch.clear();

        reports = new ArrayList<>();
        initRecordsRecyclerView();

        if (reports.size() == 0)
            callAPI();


        initListener();
    }

    private void initListener() {
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    page = 0;
//                    callAPI();
                if (getActivity() != null)
                    Common.hideSoftKeyboard(getActivity());

                return true;
            }

            return false;
        });
        iv_clear.setOnClickListener(v -> {
            if (isSearched) {
                edSearch.setText("");
                page = 0;
                swipeToRefresh.setRefreshing(true);
                callAPI();
            }
        });

        edSearch.setOnTypingModified((view1, isTyping) -> {
            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
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

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {
            if (presenter != null)
                if (reports.size() == 0)
                    callAPI();
        }
    }

    //call lab report API
    void callAPI() {

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
        if (showMedicus == false) {
            presenter.callGetAllLabReports(String.valueOf(labPageNumber), labBookingStatus);
        } else {
            presenter.callGetAllLabReportsMedicus(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(),
                    episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date,
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        }

    }

    // Initialize list view and add adapter to display data,
    void initRecordsRecyclerView() {
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));

        if (!showMedicus) {
            adapter = new RecordsAdapter(getContext(), reports);
            rvRecords.setAdapter(adapter);
            adapter.setOnItemClickListener(new RecordsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    // Fetch selected report from db
                    spec_number = reports.get(position).getOrder_id();
                    mainTest = reports.get(position).getOrder_description();
//                    LabReport report = db.labReportDataDao().getSelectReport(spec_number, reports.get(position).getOrderDescription()); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

                    // check if report path saved in local b then open from local rather than downloading again
//                    if (report != null) {
////                        if (report.getLocalPath().isEmpty())
////                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
////                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
////                                    reports.get(position).getOrderDescription(), reports.get(position).getOrderId(),
////                                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
////                        else {
                            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
////
////                            startActivity(intent);
////                        }
//                    } else {
////                        presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
////                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
////                                reports.get(position).getReportType(), reports.get(position).getSpecimenNum(),
////                                Integer.parseInt(reports.get(position).getHosp_ID()));
//
//                    }

                }
            });
            nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {


                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (reports.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        callAPI();
                    }
                }
            });
        } else {
            mLabReportHeaderAdapter = new LabReportHeaderAdapter(getActivity(), reportsMedicus);
            mLabReportHeaderAdapter.setOnItemClickListener(new LabReportHeaderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int outerPosition, int innerPosition, LabReportsMedicus model) {
                    Log.d("LabReportsFragment", "onItemClick: Order num " + model.getOrderNum());
                    Log.d("LabReportsFragment", "onItemClick: Order Line unum " + model.getOrderNumLine());
                    //  Toast.makeText(getActivity(),"pos: "+position,Toast.LENGTH_LONG).show();
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
//                        }
                    }
                }

                @Override
                public void onItemMoreClick(LabReportsParentMedicus model, int item) {

                    ((MainActivity) getContext()).addFragmentforRecord();
//                    mAccessionNum = model.getLstOfChildMedicus().get(0).getAccessionNum();
//                    //  Toast.makeText(getActivity(),""+mAccessionNum,Toast.LENGTH_LONG).show();
//                    // if (MCPatient.getInstance().getPatientRecord() != null) {
//                    openSmartReports();
////                    }
////                    else
//                    presenter.callGetPin(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
                }
            });

            rvRecords.setAdapter(mLabReportHeaderAdapter);
            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {

                        if (reportsMedicus.size() < total_items && isLoading) {
                            isLoading = false;
                            progressBarLoad.setVisibility(View.VISIBLE);
                            callAPI();
                        }
                    }
                }
            });
        }


    }

    /* void openSmartReports() {
         Log.e("LabReportFragment","openSmartReports method called ..." );
         // open smart reports................
         //   showLoading();
         //  MCPatient.getInstance().get().generateMetadata();
         //   MCPatient.getInstance().get().generateInsi  ghts();
         SmartReport.Companion.configure(MCPatient.getInstance().get(), new InsightsCallback() {
             @Override
             public void onInsightClicked(Activity activity, PatientInsight patientInsight, WBListInsight wbListInsight, DoctorNote doctorNote, InsightCardActionType insightCardActionType, String s, Integer integer) {



             }

             @Override
             public void onInsightButtonClicked(Activity activity, PatientInsight patientInsight, InsightButtonActionType insightButtonActionType) {

             }

             @Override
             public void onInsightSwiped(Activity activity, PatientInsight patientInsight) {

             }

             @Override
             public void onIconClicked(Activity activity, PatientInsight patientInsight) {

             }

             @Override
             public void onCancelDoctorAppointment(Activity activity, PatientInsight patientInsight, Integer integer) {

             }

             @Override
             public void onListQuestionAnswered(Activity activity, boolean b, String s, Integer integer) {

             }

             @Override
             public void onFamilyHistoryQuestionAnswered(Activity activity, boolean b, Answer answer) {

             }

             @Override
             public void onNormalQuestionAnswered(Activity activity, String s, String s1) {

             }

             @Override
             public void onUnderneathBiomarkerClicked(Activity activity, String s, String s1, int i) {

             }

             @Override
             public void onStepsNeeded(ViewGroup viewGroup) {

             }

             @Override
             public void configureSteps(ArrayList<PatientCoachingStep> arrayList) {

             }

             @Override
             public void onCoachingProgramsViewHolderNeeded(ViewGroup viewGroup, Activity activity, String s) {

             }

             @Override
             public void configureCoachingPrograms(ArrayList<DoPatientProgramItem> arrayList) {

             }

             @Override
             public void onProgramsHeaderNeeded(ViewGroup viewGroup) {

             }

             @Override
             public void configureProgramsHeader() {

             }

             @Override
             public void onBiomarkerInfoClicked(String s, Activity activity) {

             }

             @Override
             public void onAddBiomarkerClicked(String s, Activity activity) {

             }

             @Override
             public void onGoToProfileClicked(ProfileQuestion profileQuestion, Activity activity) {

             }

             @Override
             public void onRescheduleAllNotifications() {

             }
         }, new SmartReportCallback() {
             @Override
             public void onEditReportClicked(Activity activity, int i) {



             }
             @Override
             public void onOpenProfilePopupClicked(Activity activity) {


             }
         },false,false);
         // hideLoading();
         Log.e("LabReportFragment","openSmartReports before openReportScreen");
         SmartReport.Companion.openReportDetailsScreenByNumber(requireActivity(),mAccessionNum); //B557892  //B4359156
         Log.e("LabReportFragment","openSmartReports after openReportSCreen");

     }*/
  /*  void openSmartReports() {
        Log.e("LabReportFragment", "openSmartReports method called ...");
        // open smart reports................
        //   showLoading();
          MCPatient.getInstance().get().generateMetadata();
           MCPatient.getInstance().get().generateInsights();


        //4 jan
       SmartReport.Companion.configure(MCPatient.getInstance().get(), new InsightsCallback() {


           @Override
           public void onInsightClicked(Activity activity, PatientInsight patientInsight, WBListInsight wbListInsight, String s, InsightCardActionType insightCardActionType, String s1, Integer integer) {

           }

           @Override
           public void onInsightButtonClicked(Activity activity, PatientInsight patientInsight, InsightButtonActionType insightButtonActionType) {

           }

           @Override
           public void onInsightSwiped(Activity activity, PatientInsight patientInsight) {

           }

           @Override
           public void onIconClicked(Activity activity, PatientInsight patientInsight) {

           }

           @Override
           public void onCancelDoctorAppointment(Activity activity, PatientInsight patientInsight, Integer integer) {

           }

           @Override
           public void onListQuestionAnswered(Activity activity, boolean b, String s, Integer integer) {

           }

           @Override
           public void onFamilyHistoryQuestionAnswered(Activity activity, boolean b, Answer answer) {

           }

           @Override
           public void onNormalQuestionAnswered(Activity activity, String s, String s1) {

           }

           @Override
           public void onUnderneathBiomarkerClicked(Activity activity, String s, String s1, int i) {

           }

           @Override
           public void onStepsNeeded(ViewGroup viewGroup) {

           }

           @Override
           public void configureSteps(ArrayList<PatientCoachingStep> arrayList) {

           }

           @Override
           public void onCoachingProgramsViewHolderNeeded(ViewGroup viewGroup, Activity activity, String s) {

           }

           @Override
           public void configureCoachingPrograms(ArrayList<DoPatientProgramItem> arrayList) {

           }

           @Override
           public void onProgramsHeaderNeeded(ViewGroup viewGroup) {

           }

           @Override
           public void configureProgramsHeader() {

           }

           @Override
           public void onBiomarkerInfoClicked(String s, Activity activity) {

           }

           @Override
           public void onAddBiomarkerClicked(String s, Activity activity) {

           }

           @Override
           public void onGoToProfileClicked(ProfileQuestion profileQuestion, Activity activity) {

           }

           @Override
           public void onRescheduleAllNotifications() {

           }
       }, new SmartReportCallback() {
           @Override
           public void onEditReportClicked(Activity activity, int i) {



           }
           @Override
           public void onOpenProfilePopupClicked(Activity activity) {


           }

           @Override
           public void onOpenPopup(Activity activity, Context context, String s) {

           }

           @Override
           public void onShouldUserOpenCreateAccountActivity(Activity activity) {

           }
       },false,false);
       // hideLoading();
       Log.e("LabReportFragment","openSmartReports before openReportScreen");
        SmartReport.Companion.openReportsScreen(requireActivity()); //B557892  //B4359156
       Log.e("LabReportFragment","openSmartReports after openReportSCreen");

    }*/

    @Override
    public void onGetLabReports(LabReportResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        edSearch.setVisibility(View.VISIBLE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (response.getOrder_list().size() > 0) {
            if (page == 0)
                reports.clear();

            if (swipeToRefresh.isRefreshing()) {
                reports.clear();
                swipeToRefresh.setRefreshing(false);

            }

            reports.addAll(response.getOrder_list());
            adapter.setAllData(response.getOrder_list());

            if (page == 0)
                if (reports.size() == 0 && edSearch.getText().toString().trim().isEmpty()) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    if (reports.size() == 0 && !edSearch.getText().toString().trim().isEmpty()) {
                        mainNoDataTrans.setVisibility(View.VISIBLE);
                    } else {
                        fab_filter.setVisibility(View.VISIBLE);
                        mainContent.setVisibility(View.VISIBLE);
                        mainNoData.setVisibility(View.GONE);
                        mainTimeOut.setVisibility(View.GONE);
                        mainNoNet.setVisibility(View.GONE);
                        edSearch.setVisibility(View.VISIBLE);
                    }
                    tvTotal.setText(response.getItemCount() + "");

                }

            total_items = Integer.parseInt(response.getItemCount());
            if (Integer.parseInt(response.getItemCount()) > reports.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                wasVisible = true;
                page = page + 1;
                isLoading = true;

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

//        if (response.getMessage().equalsIgnoreCase("no_net")) {
//            edSearch.setVisibility(View.GONE);
//        } else {
//            edSearch.setVisibility(View.VISIBLE);
//        }

        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);
        tvLoadMore.setEnabled(true);
    }


    @Override
    public void onGetMedicalReports(MedicalResponse response) {
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
        if (response.getOrder_list().size() > 0) {
            reports.clear();
            reports.addAll(response.getOrder_list());
            tvLoadMore.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            rvRecords.smoothScrollToPosition(0);
        }
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_LAB_REPORT_FILE) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);
                saveToLocal(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onGeneratePaymentPdf(ResponseBody response, Headers headers) {

    }


    @Override
    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> lstOfLabReportParentMedicus, int totalItems) {
        // added by @Pm...
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        edSearch.setVisibility(View.VISIBLE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (lstOfLabReportParentMedicus != null && !lstOfLabReportParentMedicus.isEmpty()) {
            if (page == 0)
                reportsMedicus.clear();

            if (swipeToRefresh.isRefreshing()) {
                reportsMedicus.clear();
                swipeToRefresh.setRefreshing(false);

            }

            reportsMedicus.addAll(lstOfLabReportParentMedicus);
            mLabReportHeaderAdapter.setAllData(lstOfLabReportParentMedicus);

            if (page == 0)
                if (reportsMedicus.size() == 0 && edSearch.getText().toString().trim().isEmpty()) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    if (reportsMedicus.size() == 0 && !edSearch.getText().toString().trim().isEmpty()) {
                        mainNoDataTrans.setVisibility(View.VISIBLE);
                    } else {
                        fab_filter.setVisibility(View.VISIBLE);
                        mainContent.setVisibility(View.VISIBLE);
                        mainNoData.setVisibility(View.GONE);
                        mainTimeOut.setVisibility(View.GONE);
                        mainNoNet.setVisibility(View.GONE);
                        edSearch.setVisibility(View.VISIBLE);
                    }
                    tvTotal.setText(lstOfLabReportParentMedicus.size() + "");

                }

            total_items = lstOfLabReportParentMedicus.size();
            if (lstOfLabReportParentMedicus.size() > reportsMedicus.size()) {
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
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }

//        if (response.getMessage().equalsIgnoreCase("no_net")) {
//            edSearch.setVisibility(View.GONE);
//        } else {
//            edSearch.setVisibility(View.VISIBLE);
//        }

        if (page == 0 || page == 1)
            nestedScroll.scrollTo(0, 0);
        tvLoadMore.setEnabled(true);


    }

    @Override
    public void onGetPin(PinResponse response) {
        Log.e("LabReportsFragment", "onGetPin method called....");
        handleLogin(response.getData().getPin());

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

            if (reports.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                mainNoNet.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.GONE);
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

        if (reports.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainNoDataTrans.setVisibility(View.GONE);

        } else {
            Common.noInternet(getActivity());
        }
    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            Log.e("fileName", depo);
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
    void saveToLocal(Uri uri) {
//        db.labReportDataDao().saveLabLocalPath(uri.toString(), spec_number, mainTest);// SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
        spec_number = "";
        mainTest = "";
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

    private void handleLogin(String pin) {
        String mrnNo = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "");
        showLoading();
        //   OnlinePatient onlinePatient = OnlinePatient.create(false);
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
    }
}


//package sa.med.imc.myimc.Records.view;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.room.Room;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import androidx.fragment.app.Fragment;
//import androidx.core.widget.NestedScrollView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.jetbrains.annotations.NotNull;
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
//import ai.medicus.medicuscore.PatientReport;
//import ai.medicus.medicuscore.ProfileQuestion;
//import ai.medicus.medicuscore.SyncWithServerDelegate;
//import ai.medicus.medicuscore.WBListInsight;
//import ai.medicus.smartreport.SmartReport;
//import ai.medicus.smartreport.SmartReportCallback;
//import ai.medicus.smartreport.delegates.SmartReportActionsDelegate;
//import ai.medicus.utils.MedicusUtilities;
//import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
//import sa.med.imc.myimc.Adapter.RecordsAdapter;
//import sa.med.imc.myimc.Base.MCPatient;
//import sa.med.imc.myimc.Database.ImcDatabase;
//import sa.med.imc.myimc.Network.Constants;
//import sa.med.imc.myimc.Network.ImcApplication;
//import sa.med.imc.myimc.Network.SharedPreferencesUtils;
//import sa.med.imc.myimc.R;
//import sa.med.imc.myimc.Records.model.CardioReportResponse;
//import sa.med.imc.myimc.Records.model.LabReport;
//import sa.med.imc.myimc.Records.model.LabReportResponse;
//import sa.med.imc.myimc.Records.model.LabReportsMedicus;
//import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
//import sa.med.imc.myimc.Records.model.MedicalResponse;
//import sa.med.imc.myimc.Records.model.OperativeResponse;
//import sa.med.imc.myimc.Records.model.PinResponse;
//import sa.med.imc.myimc.Records.model.SickLeaveResponse;
//import sa.med.imc.myimc.Records.presenter.ReportsImpl;
//import sa.med.imc.myimc.Records.presenter.ReportsPresenter;
//import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
//import sa.med.imc.myimc.Utils.Common;
//import sa.med.imc.myimc.Utils.CustomTypingEditText;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.Headers;
//import okhttp3.ResponseBody;
//
//
//public class LabReportsFragment extends Fragment implements ReportsViews, BottomReportFilterDialog.BottomDailogListener {
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
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
//    @BindView(R.id.main_no_data)
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
//    @BindView(R.id.fab_filter)
//    FloatingActionButton fab_filter;
//    @BindView(R.id.iv_clear)
//    ImageView iv_clear;
//    @BindView(R.id.nestedScroll)
//    NestedScrollView nestedScroll;
//    @BindView(R.id.main_no_data_trans)
//    RelativeLayout mainNoDataTrans;
//
//
//    RecordsAdapter adapter;
//    LabReportHeaderAdapter mLabReportHeaderAdapter;
//
//
//    int page = 0, total_items = 0;
//    boolean wasVisible = false, isSearched = false;
//    String spec_number = "", mainTest = "", episodeId = "0", from_date = "", to_date = "";
//    Boolean isLoading = false;
//
//    // App database
//    ImcDatabase db;
//
//    ReportsPresenter presenter;
//    List<LabReport> reports = new ArrayList<>();
//    List<LabReportsParentMedicus> reportsMedicus = new ArrayList<>();
//
//    private OnFragmentInteractionListener mListener;
//    BottomReportFilterDialog bottomReportFilterDialog;
//
//    private static boolean showMedicus = true;// for live it should be FALSE ,For debug it should be  TRUE
//    private String mAccessionNum;
//
//    public LabReportsFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static LabReportsFragment newInstance(String param1, String param2) {
//        LabReportsFragment fragment = new LabReportsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_lab_reports, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
//            view.setRotationY(180);
//        }
//
//
//        presenter = new ReportsImpl(this, getActivity());
//        db = Room.databaseBuilder(getActivity(), ImcDatabase.class, "imc_database").allowMainThreadQueries().build();
//        fab_filter.setImageResource(R.drawable.ic_filter);
//
//        page = 0;
//        from_date = "";
//        to_date = "";
//        isSearched = false;
//        edSearch.setText("");
//        edSearch.clear();
//
//        reports = new ArrayList<>();
//        initRecordsRecyclerView();
//
//        if (reports.size() == 0)
//            callAPI();
//
//
//        initListener();
//    }
//
//    private void initListener() {
//        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                    page = 0;
////                    callAPI();
//                    if (getActivity() != null)
//                        Common.hideSoftKeyboard(getActivity());
//
//                    return true;
//                }
//
//                return false;
//            }
//        });
//        iv_clear.setOnClickListener(v -> {
//            if (isSearched) {
//                edSearch.setText("");
//                page = 0;
//                swipeToRefresh.setRefreshing(true);
//                callAPI();
//            }
//        });
//
//        edSearch.setOnTypingModified((view1, isTyping) -> {
//            if (!isTyping) {
//                if (edSearch.getText().toString().trim().length() > 0) {
//                    iv_clear.setVisibility(View.VISIBLE);
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
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isUserVisible) {
//        super.setUserVisibleHint(isUserVisible);
//        // when fragment visible to user and view is not null then enter here.
//        if (isUserVisible) {
//            if (presenter != null)
//                if (reports.size() == 0)
//                    callAPI();
//        }
//    }
//
//    //call lab report API
//    void callAPI() {
//
//        if (edSearch.getText().toString().length() > 0)
//            isSearched = true;
//        else
//            isSearched = false;
//
//        if (page == 0) {
//            progressBar.setVisibility(View.VISIBLE);
//            mainContent.setVisibility(View.VISIBLE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//        if (showMedicus == false) {
//            presenter.callGetAllLabReports(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//        } else {
//            presenter.callGetAllLabReportsMedicus(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(), episodeId, Constants.RECORD_SET, String.valueOf(page), from_date, to_date);
//        }
//
//
//    }
//
//    // Initialize list view and add adapter to display data,
//    void initRecordsRecyclerView() {
//        rvRecords.setHasFixedSize(true);
//        rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        if (showMedicus == false)
//        {
//            adapter = new RecordsAdapter(getContext(), reports);
//            rvRecords.setAdapter(adapter);
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
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                                /* ... */
//                            }
//                        }).check();
//                    }
//                }
//            });
//            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//
//                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
//
//                        if (reports.size() < total_items && isLoading) {
//                            isLoading = false;
//                            progressBarLoad.setVisibility(View.VISIBLE);
//                            callAPI();
//                        }
//                    }
//                }
//            });
//        }
//        else {
//            mLabReportHeaderAdapter = new LabReportHeaderAdapter(reportsMedicus);
//            mLabReportHeaderAdapter.setOnItemClickListener(new LabReportHeaderAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int outerPosition, int innerPosition, LabReportsMedicus model) {
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
//                                    mainTest = reportsMedicus.get(outerPosition).getLstOfChildMedicus().get(innerPosition).getMainTest();
//                                    LabReport report = db.labReportDataDao().getSelectReport(spec_number, mainTest); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
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
//                  //  Toast.makeText(getActivity(),""+mAccessionNum,Toast.LENGTH_LONG).show();
//                    if (MCPatient.getInstance().getPatientRecord() != null) {
//                        openSmartReports();
//                    } else
//                        presenter.callGetPin(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//                }
//            });
//
//            rvRecords.setAdapter(mLabReportHeaderAdapter);
//            nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//
//                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
//
//                        if (reportsMedicus.size() < total_items && isLoading) {
//                            isLoading = false;
//                            progressBarLoad.setVisibility(View.VISIBLE);
//                            callAPI();
//                        }
//                    }
//                }
//            });
//        }
//
//
//    }
//
//    void openSmartReports() {
//        Log.e("LabReportFragment","openSmartReports method called ..." );
//        // open smart reports................
//     //   showLoading();
//    //  MCPatient.getInstance().get().generateMetadata();
//   //   MCPatient.getInstance().get().generateInsights();
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
//       // hideLoading();
//        Log.e("LabReportFragment","openSmartReports before openReportScreen");
//        SmartReport.Companion.openReportDetailsScreenByNumber(requireActivity(),mAccessionNum); //B557892  //B4359156
//        Log.e("LabReportFragment","openSmartReports after openReportSCreen");
//
//    }
//
//
//    @Override
//    public void onGetLabReports(LabReportResponse response) {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        edSearch.setVisibility(View.VISIBLE);
//        mainNoDataTrans.setVisibility(View.GONE);
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
//                if (reports.size() == 0 && edSearch.getText().toString().trim().isEmpty()) {
//                    mainContent.setVisibility(View.GONE);
//                    mainNoData.setVisibility(View.VISIBLE);
//                    mainTimeOut.setVisibility(View.GONE);
//                    mainNoNet.setVisibility(View.GONE);
//                } else {
//                    if (reports.size() == 0 && !edSearch.getText().toString().trim().isEmpty()) {
//                        mainNoDataTrans.setVisibility(View.VISIBLE);
//                    } else {
//                        fab_filter.setVisibility(View.VISIBLE);
//                        mainContent.setVisibility(View.VISIBLE);
//                        mainNoData.setVisibility(View.GONE);
//                        mainTimeOut.setVisibility(View.GONE);
//                        mainNoNet.setVisibility(View.GONE);
//                        edSearch.setVisibility(View.VISIBLE);
//                    }
//                    tvTotal.setText(response.getTotal_items() + "");
//
//                }
//
//            total_items = Integer.parseInt(response.getTotal_items());
//            if (Integer.parseInt(response.getTotal_items()) > reports.size()) {
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
//            adapter.notifyDataSetChanged();
//        } else {
//            mainContent.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.VISIBLE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.GONE);
//        }
//
//        if (response.getMessage().equalsIgnoreCase("no_net")) {
//            edSearch.setVisibility(View.GONE);
//        } else {
//            edSearch.setVisibility(View.VISIBLE);
//        }
//
//        if (page == 0 || page == 1)
//            nestedScroll.scrollTo(0, 0);
//        tvLoadMore.setEnabled(true);
//    }
//
//    @Override
//    public void onGetMedicalReports(MedicalResponse response) {
//    }
//
//    @Override
//    public void onGetSickLeaveReports(SickLeaveResponse response) {
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
//    @Override
//    public void onGetSearchLabReports(LabReportResponse response) {
//        if (response.getLabReports() != null) {
//            reports.clear();
//            reports.addAll(response.getLabReports());
//            tvLoadMore.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged();
//            rvRecords.smoothScrollToPosition(0);
//        }
//    }
//
//    @Override
//    public void onGetSearchMedicalReports(MedicalResponse response) {
//    }
//
//    @Override
//    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {
//    }
//
//    @Override
//    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
//        downloadFile(response, headers);
//    }
//
//    @Override
//    public void onGetLabReportsMedicus(List<LabReportsParentMedicus> lstOfLabReportParentMedicus) {
//        // added by @Pm...
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        edSearch.setVisibility(View.VISIBLE);
//        mainNoDataTrans.setVisibility(View.GONE);
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
//                        mainNoDataTrans.setVisibility(View.VISIBLE);
//                    } else {
//                        fab_filter.setVisibility(View.VISIBLE);
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
//
//    }
//
//    @Override
//    public void onGetPin(PinResponse response) {
//        Log.e("LabReportsFragment","onGetPin method called....");
//        handleLogin(response.getData().getPin());
//
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
//        if (msg.equalsIgnoreCase("timeout")) {
//            fab_filter.setVisibility(View.GONE);
//
//            if (reports.size() == 0) {
//                mainContent.setVisibility(View.GONE);
//                mainNoData.setVisibility(View.GONE);
//                mainTimeOut.setVisibility(View.VISIBLE);
//                mainNoNet.setVisibility(View.GONE);
//                mainNoDataTrans.setVisibility(View.GONE);
//            } else {
//                Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
//            }
//
//        } else {
//            Common.makeToast(getActivity(), msg);
//        }
//        Common.makeToast(getActivity(), msg);
//    }
//
//    @Override
//    public void onNoInternet() {
//        progressBar.setVisibility(View.GONE);
//        progressBarLoad.setVisibility(View.INVISIBLE);
//        swipeToRefresh.setRefreshing(false);
//        tvLoadMore.setEnabled(true);
//        fab_filter.setVisibility(View.GONE);
//
//        if (reports.size() == 0) {
//            mainContent.setVisibility(View.GONE);
//            mainNoData.setVisibility(View.GONE);
//            mainTimeOut.setVisibility(View.GONE);
//            mainNoNet.setVisibility(View.VISIBLE);
//            mainNoDataTrans.setVisibility(View.GONE);
//
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
//    // save path in db
//    void saveToLocal(String path) {
//        db.labReportDataDao().saveLabLocalPath(path, spec_number, mainTest);// SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
//        spec_number = "";
//        mainTest = "";
//    }
//
//    // Show download success and ask to open file
//    void openFile(String filepath) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.success));
//        builder.setMessage(getResources().getString(R.string.download_dialog));
//        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                intent.putExtra(Constants.IntentKey.INTENT_LINK, filepath);
//                startActivity(intent);
//            }
//        });
//        builder.setNegativeButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        final AlertDialog alert = builder.create();
//
//        alert.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface arg0) {
//                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
//                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
//            }
//        });
//        alert.show();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @OnClick({R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.fab_filter})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_load_more:
//                tvLoadMore.setEnabled(false);
//
//                progressBarLoad.setVisibility(View.VISIBLE);
//                callAPI();
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
//            case R.id.fab_filter:
//                if (bottomReportFilterDialog == null)
//                    bottomReportFilterDialog = BottomReportFilterDialog.newInstance();
//
//                if (!bottomReportFilterDialog.isAdded())
//                    bottomReportFilterDialog.show(getChildFragmentManager(), "DAILOG");
//                break;
//        }
//    }
//
//    @Override
//    public void onDone(String from, String to) {
//        from_date = from;
//        to_date = to;
//        if (!from_date.isEmpty() && !to_date.isEmpty())
//            fab_filter.setImageResource(R.drawable.ic_filter_green);
//
//        page = 0;
//        callAPI();
//    }
//
//    @Override
//    public void onClear(String text) {
//        if (from_date.length() > 0 || to_date.length() > 0) {
//            from_date = "";
//            to_date = "";
//            fab_filter.setImageResource(R.drawable.ic_filter);
//
//            page = 0;
//            callAPI();
//        }
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
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
//                      //  requireActivity().runOnUiThread(() -> hideLoading());
//
//                        int id = patient.getPatientRecord().getId();
//                        SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).setValue(Constants.KEY_PATIENT_ID, id);
//                        MCPatient.getInstance().setCurrent(patient);
//
//                        MCPatient.getInstance().get().syncWithServer(new SyncWithServerDelegate() {
//                            @Override
//                            public void onSyncWithServerSuccess() {
//                                Log.e("LabReportFragment","onSyncWithServerSuccess method called ..." );
//                            //    openSmartReports();
//                              requireActivity().runOnUiThread(new Runnable() {
//                                  @Override
//                                  public void run() {
//                                      openSmartReports();
//                                      hideLoading();
//                                  }
//                              });
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
//}