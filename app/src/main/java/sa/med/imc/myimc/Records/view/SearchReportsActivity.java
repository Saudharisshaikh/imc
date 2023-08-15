package sa.med.imc.myimc.Records.view;

import android.Manifest;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
import sa.med.imc.myimc.Adapter.RecordsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReport;
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

import static sa.med.imc.myimc.Utils.Common.ReportTypes.LAB;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.MEDICAL;

public class SearchReportsActivity extends BaseActivity implements ReportsViews {


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
    EditText edSearch;
    boolean edit = false;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;


    RecordsAdapter adapter;
    int page = 0;
    boolean wasVisible = false;
    String spec_number = "", mainTest = "";
    ;

    ReportsPresenter presenter;
    List<LabReport> reports = new ArrayList<>();

    MedicalReportAdapter medicalReportAdapter;
    List<MedicalReport> medicalReports = new ArrayList<>();
    String product_code = "", orderNumLin = "", episodeId = "";

    // App database
    ImcDatabase db;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    private static final int CREATE_LAB_REPORT_FILE = 1;
    private static final int CREATE_MEDICAL_REPORT_FILE = 3;
    private ResponseBody responseBody;
    private int labPageNumber = 1;
    private String labBookingStatus = "2";

    @Override
    protected Context getActivityContext() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_reports);
        ButterKnife.bind(this);
        db = Room.databaseBuilder(this, ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if (getIntent().hasExtra("rad")) {
            edSearch.setHint(getString(R.string.ser_med));
            setUpMedRecordsRecyclerView();
        } else {
            edSearch.setHint(getString(R.string.ser_lab));
            setUpRecordsRecyclerView();

        }
        presenter = new ReportsImpl(this, this);

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edSearch.getText().toString().trim().length() > 0) {
                        page = 0;
                        if (getIntent().hasExtra("rad"))
                            callAPIMed();
                        else
                            callAPI();
                    } else {
                        page = 0;
                        if (getIntent().hasExtra("rad"))
                            callAPIMed();
                        else
                            callAPI();
//                        adapter.clearSearchData();
//                        rvRecords.smoothScrollToPosition(0);
//                        if (wasVisible)
//                            tvLoadMore.setVisibility(View.VISIBLE);
                    }
                    return true;
                }

                return false;
            }
        });


    }

    // call Lab reports API
    void callAPI() {
        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        presenter.callGetAllLabReports(String.valueOf(labPageNumber), labBookingStatus);

    }


    //Call Api to get reports
    void callAPIMed() {
        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);

            mainContent.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
        }
        presenter.callGetAllMedicalReports(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), edSearch.getText().toString().toLowerCase(),
                episodeId, Constants.RECORD_SET, String.valueOf(page), "", "",
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Initialize list view and add adapter to display data,
    void setUpRecordsRecyclerView() {
        adapter = new RecordsAdapter(this, reports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        rvRecords.setAdapter(adapter);
        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (reports.get(position).getStatus() == 0) {
//                    onFail(getString(R.string.no_result_available));
//                } else {
//                    // Fetch selected report from db
//                    spec_number = reports.get(position).getSpecimenNum();
//                    mainTest = reports.get(position).getMainTest();
//
//                    LabReport report = db.labReportDataDao().getSelectReport(spec_number, mainTest); //SharedPreferencesUtils.getInstance(SearchReportsActivity.this()).getValue(Constants.KEY_MRN, ""));
//
//                    // check if report path saved in local b then open from local rather than downloading again
//                    if (report != null) {
//                        if (report.getLocalPath().isEmpty())
//                            presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, ""),
//                                    reports.get(position).getReportType(), reports.get(position).getSpecimenNum(),
//                                    SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                        else {
//                            Intent intent = new Intent(SearchReportsActivity.this, UrlOpenActivity.class);
//                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));
//
//                            startActivity(intent);
//                        }
//                    } else {
//                        presenter.callGenerateLabPdf(SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, ""),
//                                reports.get(position).getReportType(), reports.get(position).getSpecimenNum(),
//                                SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//
//                    }
//                }
            }
        }));
    }

    // Initialize Medical list view and add adapter to display data,
    void setUpMedRecordsRecyclerView() {
        medicalReportAdapter = new MedicalReportAdapter(this, medicalReports);
        rvRecords.setHasFixedSize(true);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        rvRecords.setAdapter(medicalReportAdapter);
        rvRecords.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
//                            presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                    SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, ""),
//                                    medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine(),
//                                    SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                        else {
//                            Intent intent = new Intent(SearchReportsActivity.this, UrlOpenActivity.class);
//                            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                            intent.putExtra(Constants.IntentKey.INTENT_LINK, report.getLocalPath());
//                            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_reports));
//
//                            startActivity(intent);
//                        }
//                    } else {
//                        presenter.callGenerateMedicalPdf(SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, ""),
//                                medicalReports.get(position).getId().getOrderNo(), medicalReports.get(position).getId().getOrderNoLine(),
//                                SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
//                    }
//                }
            }
        }));
    }


    @Override
    public void onGetLabReports(LabReportResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        edSearch.setVisibility(View.VISIBLE);

        if (response.getOrder_list().size()>0) {
            if (page == 0)
                reports.clear();

            reports.addAll(response.getOrder_list());
            adapter.setAllData(response.getOrder_list());

            if (page == 0)
                if (reports.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    edSearch.setVisibility(View.VISIBLE);
                }


            if (Integer.parseInt(response.getItemCount()) > reports.size()) {
                tvLoadMore.setVisibility(View.VISIBLE);
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

//        if (response.getMessage().equalsIgnoreCase("no_net")) {
//            edSearch.setVisibility(View.GONE);
//        } else {
//            edSearch.setVisibility(View.VISIBLE);
//
//        }
        tvLoadMore.setEnabled(true);


    }

    @Override
    public void onGetMedicalReports(MedicalResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);


        if (response.getMedicalReports() != null) {
            medicalReports.addAll(response.getMedicalReports());
            medicalReportAdapter.setAllData(response.getMedicalReports());

            if (page == 0)
                if (medicalReports.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoData.setVisibility(View.VISIBLE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                } else {
                    mainContent.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    edSearch.setVisibility(View.VISIBLE);

                }


            if (Integer.parseInt(response.getTotal_items()) > medicalReports.size()) {
                tvLoadMore.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onGenerateMedicalPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(MEDICAL);

    }


    @Override
    public void onGenerateLabPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(LAB);

    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case LAB:
                String labFile = SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
                createFile(labFile, CREATE_LAB_REPORT_FILE);
                break;
            case MEDICAL:
                String medicalFile = SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
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
            ParcelFileDescriptor pfd = getContentResolver().
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
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == CREATE_LAB_REPORT_FILE) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);
                saveToLocal(uri);
            }
        } else if (requestCode == CREATE_MEDICAL_REPORT_FILE) {
            Uri uri = null;
            uri = resultData.getData();

            alterDocument(uri);
            saveToLocal(uri);
        }
    }

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
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        tvLoadMore.setEnabled(true);

        if (msg.equalsIgnoreCase("timeout")) {
            if (reports.size() == 0 && medicalReports.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                mainNoNet.setVisibility(View.GONE);
            } else {
                makeToast(getString(R.string.time_out_messgae));
            }

        } else {
            makeToast(msg);

        }
        makeToast(msg);
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        tvLoadMore.setEnabled(true);

        if (reports.size() == 0 && medicalReports.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(SearchReportsActivity.this);
        }
    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//            filename = SharedPreferencesUtils.getInstance(SearchReportsActivity.this).getValue(Constants.KEY_MRN, "") + "_report_" + System.currentTimeMillis() + ".pdf";
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
//            //onFail("Report Downloaded");
//            saveToLocal(path);
//
//            // Show download success and ask to open file
////            openFile(path);
//
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }


    // save path in db
    void saveToLocal(Uri uri) {
        if (getIntent().hasExtra("rad")) {
//            db.medicalReportDataDao().saveLabLocalPath(uri.toString(), product_code, orderNumLin); //SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
            product_code = "";
            orderNumLin="";
        }  else {
//            db.labReportDataDao().saveLabLocalPath(uri.toString(), spec_number, mainTest);// SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));
            spec_number = "";
            mainTest = "";
        }


        Intent intent = new Intent(SearchReportsActivity.this, UrlOpenActivity.class);
        intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
        intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
        if (getIntent().hasExtra("rad"))
            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.radiology_report));
        else
            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.lab_report));

        startActivity(intent);
    }

    // Show download success and ask to open file
//    void openFile(String filepath) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(SearchReportsActivity.this);
//        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.success));
//        builder.setMessage(getResources().getString(R.string.download_dialog));
//        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                Intent intent = new Intent(SearchReportsActivity.this, UrlOpenActivity.class);
//                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//                intent.putExtra(Constants.IntentKey.INTENT_LINK, filepath);
//                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.sick_levae));
//
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


    @OnClick({R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_load_more:
                tvLoadMore.setEnabled(false);

                progressBarLoad.setVisibility(View.VISIBLE);
                if (getIntent().hasExtra("rad"))
                    callAPIMed();
                else
                    callAPI();
                break;


            case R.id.bt_try_again:
                page = 0;
                if (getIntent().hasExtra("rad"))
                    callAPIMed();
                else
                    callAPI();
                break;


            case R.id.bt_try_again_time_out:
                page = 0;
                if (getIntent().hasExtra("rad"))
                    callAPIMed();
                else
                    callAPI();
                break;


            case R.id.iv_close:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
