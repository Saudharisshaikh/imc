package sa.med.imc.myimc.Telr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Adapter.ClinicsAdapter;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.presenter.ClinicImpl;
import sa.med.imc.myimc.Clinics.presenter.ClinicPresenter;
import sa.med.imc.myimc.Clinics.view.ClinicViews;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.view.BottomPaymentDialog;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

/*
 * Clinics list searched according to time slot.
 * Clinics name and a picture displayed.
 */
public class PaymentInfoFragment extends Fragment implements ClinicViews {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_clinics)
    RecyclerView rvClinics;
    @BindView(R.id.tv_load_more)
    TextView tv_load_more;

    ClinicsAdapter adapter;
    boolean edit = false;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;

    BottomPaymentDialog.BottomDailogListener mDialogListener;
//    @BindView(R.id.iv_back_bottom)
//    ImageView ivBackBottom;
//    @BindView(R.id.tv_clinic_name)
//    TextView tvClinicName;
//    @BindView(R.id.tv_physician_name)
//    TextView tvPhysicianName;
//    @BindView(R.id.tv_physician_speciality)
//    TextView tvPhysicianSpeciality;
//    @BindView(R.id.tvAppointmentDate)
//    TextView tvAppointmentDate;
//    @BindView(R.id.tv_appointment_time)
//    TextView tvAppointmentTime;
//    @BindView(R.id.tv_appointment_type)
//    TextView tvAppointmentType;
//    @BindView(R.id.tv_patient_pay)
//    TextView tvPatientPay;
//    @BindView(R.id.tv_patient_discount)
//    TextView tvPatientDiscount;
//    @BindView(R.id.tv_patient_vat)
//    TextView tvPatientVat;
//    @BindView(R.id.tv_appointment_price)
//    TextView tvAppointmentPrice;
//    @BindView(R.id.lay_price)
//    LinearLayout layPrice;
//    @BindView(R.id.lay_b)
//    LinearLayout layB;
//    @BindView(R.id.view_line_top)
//    View viewLineTop;
//    @BindView(R.id.tv_cancel)
//    TextView tv_cancel;
//    @BindView(R.id.tv_pay_cash)
//    TextView tvPayCash;
//    @BindView(R.id.lay_item)
//    RelativeLayout layItem;
    Booking booking;
    PaymentResponse paymentresponse;

    ImageView ivBackBottom;
    TextView tvClinicName,tvPhysicianSpeciality,tvAppointmentDate,tvAppointmentTime,tvAppointmentPrice;
    TextView tvPhysicianName,tvAppointmentType,tvPatientPay,tvPatientDiscount,tvPatientVat,tv_cancel,tvPayCash;
    LinearLayout layPrice,layB;
    View viewLineTop;
    RelativeLayout layItem;
    WebView webViewtelr;
    Button btn_continue;


    int page = 0, total_items = 0;
    boolean isLoading = false, isSearched = false;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    // TODO: Rename and change types of parameters
    private String mParam1;

    ClinicPresenter presenter;
    List<ClinicResponse.SpecialityList> list = new ArrayList<>();

    public PaymentInfoFragment() {
        // Required empty public constructor
    }

    FragmentListener fragmentAdd;

    public static void startActivity(Activity activity, String link, String ref, String from, String id, String arrival) {
        Intent intent = new Intent(activity, PaymentInfoFragment.class);
        intent.putExtra("link", link);
        intent.putExtra("ref", ref);
        intent.putExtra("id", id);
        intent.putExtra("arrival", arrival);
        intent.putExtra("from", from);
        activity.startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public static PaymentInfoFragment newInstance() {
        PaymentInfoFragment fragment = new PaymentInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_info, container, false);
        ButterKnife.bind(this, view);

        ivBackBottom = view.findViewById(R.id.iv_back_bottom);
        tvClinicName = view.findViewById(R.id.tv_clinic_name);
        tvPhysicianSpeciality = view.findViewById(R.id.tv_physician_speciality);
        tvAppointmentDate = view.findViewById(R.id.tv_appointment_date);
        tvAppointmentTime = view.findViewById(R.id.tv_appointment_time);
        tvAppointmentPrice = view.findViewById(R.id.tv_appointment_price);
        tvPhysicianName = view.findViewById(R.id.tv_physician_name);
        tvAppointmentType = view.findViewById(R.id.tv_appointment_type);
        tvPatientPay = view.findViewById(R.id.tv_patient_pay);
        tvPatientDiscount = view.findViewById(R.id.tv_patient_discount);
        tvPatientVat = view.findViewById(R.id.tv_patient_vat);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tvPayCash = view.findViewById(R.id.tv_pay_cash);
        layPrice = view.findViewById(R.id.lay_price);
        layB = view.findViewById(R.id.lay_b);
        viewLineTop = view.findViewById(R.id.view_line_top);
        layItem = view.findViewById(R.id.lay_item);
        webViewtelr = view.findViewById(R.id.webViewtelr);


        ivSearch.setVisibility(View.GONE);
        if (getArguments() != null) {

//            mainToolbar.setVisibility(View.VISIBLE);
//            booking = (Booking) getArguments().getSerializable("booking");
//            PriceResponse response = (PriceResponse) getArguments().getSerializable("price");
            paymentresponse = (PaymentResponse) getArguments().getSerializable("reponse");

            loadWeb(paymentresponse.getPaymentUrl());


//            if (booking.isHidePayCheckin())
//                tvPayCash.setEnabled(false);
//            else
//            tvPayCash.setEnabled(true);


//            if (response.getItemPrice() != null) {
//                // if (response.getItemPrice() > 0) {
//                if (Double.compare(response.getItemPrice(), Double.valueOf(0.0)) > 0) {
//                    tvPatientPay.setText(response.getPatientPay() + "");
//                    tvPatientDiscount.setText(response.getPaientDiscount() + "");
//                    tvPatientVat.setText(response.getPatientVAT() + "");
//                    tvAppointmentPrice.setText(response.getItemPrice() + "");
//                }
//
//                else
//                {
//                    tvPatientPay.setText("0.0" + "");
//                    tvPatientDiscount.setText("0.0" + "");
//                    tvPatientVat.setText("0.0" + "");
//                    tvAppointmentPrice.setText("0.0" + "");
//                }
//            }
//            else
//            {
//                tvPatientPay.setText("0.0" + "");
//                tvPatientDiscount.setText("0.0" + "");
//                tvPatientVat.setText("0.0" + "");
//                tvAppointmentPrice.setText("0.0" + "");
//            }
//            if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
//
//                setDataSheet(booking.getGivenNameAr() + " " + booking.getFamilyNameAr(), booking.getClinicDescAr(), booking.getService().getDescAr(),
//                        Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));
//            else
//                setDataSheet(booking.getGivenName() + " " + booking.getFamilyName(), booking.getClinicDescEn(), booking.getService().getDesc(),
//                        Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));

        }

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);

            // Clinic open form Dashboard then show back button
//            if (mParam1.equalsIgnoreCase("ClinicFragmentC")) {
//                ivLogo.setVisibility(View.GONE);
//                ivBack.setVisibility(View.VISIBLE);
//            }
//            // Clinic open from Home tab then hind back and show logo
//            else if (mParam1.equalsIgnoreCase("ClinicFragment")) {
//                ivLogo.setVisibility(View.GONE);
//
//                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE))
//                    ivBack.setVisibility(View.VISIBLE);
//                else
//                    ivBack.setVisibility(View.GONE);
//
//            }
//        }
//        edSearch.setText("");
//        edit = false;
//        isSearched = false;
//        ivSearch.setImageResource(R.drawable.ic_search);
//        edSearch.setVisibility(View.GONE);


        page = 0;
        presenter = new ClinicImpl(this, getActivity());
        callAPI();
//        setUpClinicsRecyclerView();

        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // call api to search clinic
//                page = 0;
//                callAPI();
                Common.hideSoftKeyboard(getActivity());

                return true;
            }

            return false;
        });

        edSearch.setOnTypingModified((view1, isTyping) -> {

            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0 && edit) {
                    page = 0;
                    callAPI();
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            tv_load_more.setVisibility(View.GONE);
            callAPI();
        });


        return view;
    }


    void loadWeb(String url) {

        webViewtelr.invalidate();
        webViewtelr.getSettings().setJavaScriptEnabled(true);
        webViewtelr.getSettings().setLoadWithOverviewMode(true);
        webViewtelr.getSettings().setUseWideViewPort(true);
        webViewtelr.getSettings().setAllowContentAccess(true);
        webViewtelr.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webViewtelr.setLayerType(View.LAYER_TYPE_NONE, null);
        webViewtelr.getSettings().setDomStorageEnabled(true);
        webViewtelr.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (url!= null) {
            webViewtelr.loadUrl(url);
        }

        this.webViewtelr.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("tel:"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+" + url.replace("tel:", ""))));
                else
                    view.loadUrl(url);

                if (getActivity().getIntent().hasExtra("from")) {
                    if (url.contains("selfcheckin/payement-authorized")) {//|| url.contains("selfcheckin/payement-declined")
                        webViewtelr.setVisibility(View.GONE);
                        verifyPaymentCheckIn(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                                getActivity().getIntent().getStringExtra("ref"), "back", getActivity().getIntent().getStringExtra("id"), getActivity().getIntent().getStringExtra("arrival"),
                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                    }else
                        view.loadUrl(url);


                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                webViewtelr.loadUrl("\"javascript:(function(){ document.body.style.paddingTop  = '100px'})();\"");
                if (url.contains("http://192.168.1.49:8080") || url.contains("https://patientportal.imc.med.sa")) {
                    webViewtelr.setVisibility(View.VISIBLE);
                } else {
                    webViewtelr.setVisibility(View.VISIBLE);

                }

            }
        });
    }



//    void setDataSheet(String phy_name, String clinic, String service, String date, String time) {
//        if (phy_name != null)
//            tvPhysicianName.setText(phy_name);
//        if (clinic != null)
//            tvClinicName.setText(clinic);
//        if (service != null)
//            tvPhysicianSpeciality.setText(service);
//        if (date != null)
//            tvAppointmentDate.setText(date);
//        if (time != null)
//            tvAppointmentTime.setText(Common.parseShortTime(time));
//
//        layPrice.setVisibility(View.VISIBLE);
//        tvAppointmentType.setText(getString(R.string.in_person));
//    }

    void verifyPaymentCheckIn(String token, String refId, String url, String bookingId, String updateArrival,int hosp) {
        Common.showDialog(getContext());
        JSONObject object = new JSONObject();
        try {
            object.put("refId", refId);
            object.put("bookingId", bookingId);
            object.put("updateArrival", updateArrival);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.verifyPaymentCheckIn(body);

        xxx.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    BookResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getMethod() == null) {
                            if (response1.getStatus().equalsIgnoreCase("true")) {
                                showAlert(getActivity(), getString(R.string.success), response1.getMessage(), R.drawable.ic_successful_icon_green);
                            } else {
                                showAlert(getActivity(), getString(R.string.failed), response1.getMessage(), R.drawable.ic_unsuccessful_icon);
//                                makeToast(response1.getMessage());
                            }
                        } else {
                            if (url.equalsIgnoreCase("back")) {
                                getActivity().finish();
                            } else {
                                if (response1.getOrder() != null)
                                    if (response1.getOrder().getTransaction() != null)
                                        if (response1.getOrder().getTransaction().getMessage() != null)
                                            showAlert(getActivity(), getString(R.string.failed), response1.getOrder().getTransaction().getMessage(), R.drawable.ic_unsuccessful_icon);
                                        else
                                            showAlert(getActivity(), getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                    else
                                        showAlert(getActivity(), getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                                else
                                    showAlert(getActivity(), getString(R.string.failed), getString(R.string.payment_unsuccess), R.drawable.ic_unsuccessful_icon);
                            }
                        }
                    } else {
//                        makeToast(getResources().getString(R.string.plesae_try_again));
                        Toast.makeText(getContext(), getResources().getString(R.string.plesae_try_again), Toast.LENGTH_LONG).show();
                    }
                } else {
//                    makeToast(getResources().getString(R.string.plesae_try_again));
                    Toast.makeText(getContext(), getResources().getString(R.string.plesae_try_again), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Common.hideDialog();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
//                   makeToast(message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(getActivity());
                } else {
                    message = "Unknown";
//                    makeToast(message);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void showAlert(Activity activity, String title, String msg, int logo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setIcon(logo);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            if (title.equalsIgnoreCase(getString(R.string.success))) {
                try {
                    Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                    refresh.putExtra("cancel", "cancel");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(refresh);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            getActivity().finish();
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        alert.show();
    }

//
//    @OnClick({R.id.iv_back,})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {

//            case R.id.iv_back:
//                if (fragmentAdd != null)
//                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
//                break;
//
//            case R.id.iv_search:
//                if (edit) {
//                    ivSearch.setImageResource(R.drawable.ic_search);
//                    edSearch.setVisibility(View.GONE);
//                    edit = false;
//                    edSearch.clearFocus();
//
//                    // Clear search and load all clinics
//                    // Check if search box empty or not
//                    if (isSearched) {
//                        edSearch.setText("");
//                        page = 0;
//                        callAPI();
//                    }
//
//                } else {
//                    edSearch.setText("");
//                    ivSearch.setImageResource(R.drawable.ic_close);
//                    edSearch.setVisibility(View.VISIBLE);
//                    edit = true;
//                    edSearch.requestFocus();
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
//                tv_load_more.setEnabled(false);
//
////                callAPI();
//                break;
//        }
//    }

    //Call API to get records
    void callAPI() {

        if (edSearch.getText().toString().length() > 0)
            isSearched = true;
        else
            isSearched = false;


        presenter.callGetAllClinics(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""), "", "", edSearch.getText().toString(), Constants.RECORD_SET
                , String.valueOf(page), SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    // Initialize clinics list view and add adapter to display data,
    void setUpClinicsRecyclerView() {
        list = new ArrayList<>();
        adapter = new ClinicsAdapter(getActivity(), list);
        rvClinics.setHasFixedSize(true);
        rvClinics.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvClinics.setAdapter(adapter);
        rvClinics.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fragmentAdd != null)
                    fragmentAdd.openPhysicianFragment("PhysiciansFragmentC", list.get(position).getSpecialityCode());
            }
        }));

        rvClinics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvClinics.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (list.size() < total_items && isLoading) {
                        isLoading = false;
                        callAPI();
                    }
                }
            }
        });
    }

    @Override
    public void onGetClinicsList(ClinicResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);

        if (swipeToRefresh.isRefreshing()) {
            list.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getSpecialityList() != null) {
            if (page == 0)
                list.clear();

            list.addAll(response.getSpecialityList());
//            adapter.setAllData();

            if (list.size() == 0) {
//                rvClinics.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
//                rvClinics.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.GONE);
                if (response.getSpecialityList() != null)
                    if (response.getSpecialityList().size() > 0) {
                        total_items = response.getSpecialityList().size();

                        if (list.size() < response.getSpecialityList().size()) {
                            page = page + 1;
                            isLoading = true;
                        }
                    }
            }
        } else {
//            rvClinics.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
        }
        tv_load_more.setEnabled(true);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetSearchClinicsList(ClinicResponse response) {
        if (response.getSpecialityList() != null) {
            list.clear();
            list.addAll(response.getSpecialityList());
            if (list.size() == 0) {
                onFail(getString(R.string.no_result));
            }
        } else {
            onFail(getString(R.string.no_result));
        }
        tv_load_more.setVisibility(View.GONE);
        tv_load_more.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        if (!swipeToRefresh.isRefreshing())
            Common.showDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        swipeToRefresh.setRefreshing(false);
        tv_load_more.setEnabled(true);

        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                if (list.size() == 0) {
                    mainContent.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoDataTrans.setVisibility(View.GONE);

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
        tv_load_more.setEnabled(true);

        if (list.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
        } else {
            Common.noInternet(getActivity());
            isLoading = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
