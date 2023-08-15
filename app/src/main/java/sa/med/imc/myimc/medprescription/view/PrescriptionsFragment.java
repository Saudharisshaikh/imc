package sa.med.imc.myimc.medprescription.view;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.Unbinder;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.PerscriptionAdapter;
import sa.med.imc.myimc.HealthSummary.view.BottomFilterReadingDialog;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.UrlOpen.UrlOpenActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.medprescription.invoice.PrescriptionInvoiceSheet;
import sa.med.imc.myimc.medprescription.model.PrescriptionResponse;
import sa.med.imc.myimc.medprescription.presenter.PrescriptionImpl;
import sa.med.imc.myimc.medprescription.presenter.PrescriptionPresenter;

import static sa.med.imc.myimc.Utils.Common.ReportTypes.MEDICAL;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.PRESCRIPTION;


public class PrescriptionsFragment extends Fragment implements PrescriptionViews, BottomFilterReadingDialog.BottomDialogListener {

    PerscriptionAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREATE_PRESCRIPTION_REPORT_FILE = 5;
    private ResponseBody responseBody;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_prescriptions)
    RecyclerView rvPrescriptions;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.mainContent)
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
    Unbinder unbinder;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.progress_bar_load)
    ProgressBar progressBarLoad;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.fab_filter)
    FloatingActionButton fabFilter;

    private String mParam1;
    private String e_id = "", from = "", to = "";
    int page = 0, total_items = 0;
    boolean isLoading = false;
    BottomFilterReadingDialog bottomFilterReadingDialog;

    List<PrescriptionResponse.Prescription> list = new ArrayList<>();

    PrescriptionPresenter prescriptionPresenter;

    public PrescriptionsFragment() {
        // Required empty public constructor
    }

    public static PrescriptionsFragment newInstance(String param1, String param2) {
        PrescriptionsFragment fragment = new PrescriptionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_prescriptions, container, false);
        ButterKnife.bind(this, view);
        isLoading = false;
        e_id = "";
        from = "";
        to = "";
        list = new ArrayList<>();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            e_id = getArguments().getString(ARG_PARAM2);
        }
        prescriptionPresenter = new PrescriptionImpl(this, getActivity());
        setUpRecyclerView();
        page = 0;
        callAPI();

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            callAPI();
        });
        return view;
    }

    // Call API to fetch prescription list
    void callAPI() {
        if (page > 0)
            progressBarLoad.setVisibility(View.VISIBLE);

        prescriptionPresenter.callGetPrescriptionApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                e_id, from, to,
                Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Initialize list view and add adapter to display data,
    void setUpRecyclerView() {
        adapter = new PerscriptionAdapter(getActivity(), list);
        rvPrescriptions.setHasFixedSize(true);
        rvPrescriptions.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPrescriptions.setAdapter(adapter);
        adapter.setOnClickListener(new PerscriptionAdapter.OnItemClickListner() {
            @Override
            public void clickPrescription(int position) {
                prescriptionPresenter.callGeneratePrescriptionPdf(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        list.get(position).getPrecriptionNo(), String.valueOf(list.get(position).getEpisodeNumber()),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

            }

            @Override
            public void clickPharmacy(PrescriptionResponse.Prescription prescription) {
                MedicinesPrescriptionsFragment fragment = (MedicinesPrescriptionsFragment) getParentFragment();
                if (fragment==null){
                    return;
                }
//                fragment.fragmentAdd.openPharmacyFragment(prescription.getEpisodeType(),prescription.getPrecriptionNo(),String.valueOf(prescription.getEpisodeNumber()),prescription.getOrderNumber());
                fragment.fragmentAdd.openPharmacyFragment(prescription.getPrecriptionNo(),String.valueOf(prescription.getEpisodeNumber()));
            }

            @Override
            public void onDownload(PrescriptionResponse.Prescription prescription) {
                PrescriptionInvoiceSheet.newInstance(prescription.getPrecriptionNo(),prescription.getEpisodeNumber(),prescription.getOrderNumber(),prescription.getEpisodeType()).show(getChildFragmentManager(),PrescriptionInvoiceSheet.class.getSimpleName());
            }
        });
//        rvPrescriptions.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                //get the recycler view position which is completely visible and first
//                final int positionView = ((LinearLayoutManager) rvPrescriptions.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                int numItems = adapter.getItemCount() - 1;
//
//                if (positionView >= numItems) {
//                    if (list.size() < total_items && isLoading) {
//                        isLoading = false;
//                        callAPI();
//                    }
//                }
//            }
//        });

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (list.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        callAPI();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.fab_filter, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.bt_try_again:

            case R.id.bt_try_again_time_out:
                page = 0;
                callAPI();
                break;

            case R.id.fab_filter:
                if (bottomFilterReadingDialog == null)
                    bottomFilterReadingDialog = BottomFilterReadingDialog.newInstance();

                if (!bottomFilterReadingDialog.isAdded())
                    bottomFilterReadingDialog.show(getChildFragmentManager(), "DAILOG");
                break;
        }
    }

    @Override
    public void onGetPrescription(PrescriptionResponse response) {
        progressBarLoad.setVisibility(View.GONE);

        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoData.setVisibility(View.GONE);
        fabFilter.setVisibility(View.VISIBLE);

        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getPrescription() != null) {
            if (page == 0) {
                list.clear();
            }
            list.addAll(response.getPrescription());
            if (list.size() == 0) {
                rvPrescriptions.setVisibility(View.GONE);
                mainNoData.setVisibility(View.VISIBLE);


            } else {
                rvPrescriptions.setVisibility(View.VISIBLE);

                if (response.getTotalItems() != null)
                    if (response.getTotalItems().length() > 0) {
                        total_items = Integer.parseInt(response.getTotalItems());

                        if (list.size() < Integer.parseInt(response.getTotalItems())) {
                            page = page + 1;
                            isLoading = true;
                        }
                    }
            }
        } else {
            rvPrescriptions.setVisibility(View.GONE);
            mainNoData.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onGeneratePrescriptionPdf(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(PRESCRIPTION);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case PRESCRIPTION:
                String prescriptionFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_prescription_" + System.currentTimeMillis() + ".pdf";
                createFile(prescriptionFile, CREATE_PRESCRIPTION_REPORT_FILE);
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
        if (requestCode == CREATE_PRESCRIPTION_REPORT_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                alterDocument(uri);

                Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
                intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
                intent.putExtra(Constants.IntentKey.INTENT_LINK, uri.toString());
                intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.prescription_));
                startActivity(intent);
            }
        }
    }

    @Override
    public void showLoading() {
        if (page == 0)
            progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onFail(String msg) {
        progressBarLoad.setVisibility(View.GONE);

        swipeToRefresh.setRefreshing(false);

        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                if (list.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                    fabFilter.setVisibility(View.GONE);


                } else {
                    if (getActivity() != null)
                        Common.makeToast(getActivity(), getActivity().getString(R.string.time_out_messgae));
                    isLoading = true;
                }

            } else {
                if (getActivity() != null)
                    Common.makeToast(getActivity(), msg);

            }
    }

    @Override
    public void onNoInternet() {
        swipeToRefresh.setRefreshing(false);
        progressBarLoad.setVisibility(View.GONE);

        if (list.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            fabFilter.setVisibility(View.GONE);

        } else {
            Common.noInternet(getActivity());
            isLoading = true;
        }
    }

    @Override
    public void onDone(String booking_id1, String from1, String to1) {
        e_id = booking_id1;
        from = from1;
        to = to1;

        page = 0;
        callAPI();
    }

    @Override
    public void onClear(String text) {
        if (e_id.length() > 0 || from.length() > 0 || to.length() > 0) {
            e_id = "";
            from = "";
            to = "";

            page = 0;
            callAPI();
        }
    }

    //Save lab report data in storage
//    void downloadFileR(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_prescription_" + System.currentTimeMillis() + ".pdf";
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
//            Intent intent = new Intent(getActivity(), UrlOpenActivity.class);
//            intent.putExtra(Constants.IntentKey.INTENT_TYPE, Constants.IntentKey.INTENT_FILE);
//            intent.putExtra(Constants.IntentKey.INTENT_LINK, path);
//            intent.putExtra(Constants.IntentKey.INTENT_REPORT_TYPE, getString(R.string.prescription_));
//            startActivity(intent);
//
////            openFile(path);
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    // Show download success and ask to open file
    void openFile(String filepath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        builder.setMessage(getResources().getString(R.string.download_prescription));
        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                File file = new File(filepath);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Uri apkURI = FileProvider.getUriForFile(
                        getActivity(),
                        getActivity().getPackageName() + ".fileprovider", file);
                pdfOpenintent.setDataAndType(apkURI, "application/pdf");
                try {
                    startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
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
                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();

    }

}
