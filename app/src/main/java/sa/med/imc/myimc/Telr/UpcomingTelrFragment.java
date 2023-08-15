package sa.med.imc.myimc.Telr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Adapter.ClinicsAdapter;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.presenter.ClinicImpl;
import sa.med.imc.myimc.Clinics.presenter.ClinicPresenter;
import sa.med.imc.myimc.Clinics.view.ClinicViews;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.presenter.CheckInImpl;
import sa.med.imc.myimc.Managebookings.presenter.CheckInPresenter;
import sa.med.imc.myimc.Managebookings.view.BottomPaymentDialog;
import sa.med.imc.myimc.Managebookings.view.CheckInViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.presenter.SignUpPresenter;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

import static sa.med.imc.myimc.Home.HomeFragment.isGPSEnabled;

/*
 * Clinics list searched according to time slot.
 * Clinics name and a picture displayed.
 */
public class UpcomingTelrFragment extends Fragment implements ClinicViews,CheckInViews {

    private static final String ARG_PARAM1 = "param1";

    List<Booking> upcomingList = new ArrayList<>();
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
    private String arrival = "0";

    NationalityResponse nationalityResponse;
    SignUpPresenter presenter1;
    String country_nation = "";
    BottomPaymentDialog.BottomDailogListener mDialogListener;

    CheckInPresenter checkInPresenter;
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
    int selected_pos = -1;


    ImageView ivBackBottom,imageView,
            imageViewRecord, appoint_center_button, mayoimage, profileImage;
    TextView tvClinicName,tvPhysicianSpeciality,tvAppointmentDate,tvAppointmentTime,tvAppointmentPrice,btn_cancel;
    TextView tvPhysicianName,tvAppointmentType,tvPatientPay,tvPatientDiscount,tvPatientVat,tv_cancel,tvPayCash;
    LinearLayout layPrice,layB;
    View viewLineTop;
    RelativeLayout layItem;
    TextView btn_continue,edt5;

    public static final boolean isSecurityEnabled = true;
    private String amount = "350"; // Just for testing

    public static final String KEY = "pQ6nP-7rHt@5WRFv";        // TODO: Insert your Key here
    public static final String STORE_ID = "15996";    // TODO: Insert your Store ID here
    public static final String EMAIL= "Raheelkhokhar1994@gmail.com";     // TODO: Insert the customer email here



    int page = 0, total_items = 0;
    boolean isLoading = false, isSearched = false;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    // TODO: Rename and change types of parameters
    private String mParam1;

    ClinicPresenter presenter;
    List<ClinicResponse.SpecialityList> list = new ArrayList<>();

    public UpcomingTelrFragment() {
        // Required empty public constructor
    }

    FragmentListener fragmentAdd;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public static UpcomingTelrFragment newInstance() {
        UpcomingTelrFragment fragment = new UpcomingTelrFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_telr, container, false);
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
        btn_continue = view.findViewById(R.id.btn_continue);
        edt5 = view.findViewById(R.id.edt5);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        imageView =view.findViewById(R.id.homeimage);
        imageViewRecord =view.findViewById(R.id.record1);
        appoint_center_button =view.findViewById(R.id.appoint_center_button);
        mayoimage =view.findViewById(R.id.mayoimage);
        profileImage =view.findViewById(R.id.profileImage);
        checkInPresenter = new CheckInImpl( this, getActivity());



        if (getArguments() != null) {

//            mainToolbar.setVisibility(View.VISIBLE);
            booking = (Booking) getArguments().getSerializable("booking");
            PriceResponse response = (PriceResponse) getArguments().getSerializable("Response");

//            if (booking.isHidePayCheckin())
//                tvPayCash.setEnabled(false);
//            else
//            tvPayCash.setEnabled(true);


            if (response.getItemPrice() != null) {
                // if (response.getItemPrice() > 0) {
                if (Double.compare(response.getItemPrice(), Double.valueOf(0.0)) > 0) {
                    tvPatientPay.setText(response.getPatientPay() + "");
                    tvPatientDiscount.setText(response.getPaientDiscount() + "");
                    tvPatientVat.setText(response.getPatientVAT() + "");
                    tvAppointmentPrice.setText(response.getItemPrice() + "");
                }

                else
                {
                    tvPatientPay.setText("0.0" + "");
                    tvPatientDiscount.setText("0.0" + "");
                    tvPatientVat.setText("0.0" + "");
                    tvAppointmentPrice.setText("0.0" + "");
                }
            }
            else
            {
                tvPatientPay.setText("0.0" + "");
                tvPatientDiscount.setText("0.0" + "");
                tvPatientVat.setText("0.0" + "");
                tvAppointmentPrice.setText("0.0" + "");
            }
            if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))

                setDataSheet(booking.getGivenNameAr() + " " + booking.getFamilyNameAr(), booking.getClinicDescAr(), booking.getService().getDescAr(),
                        Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));
            else
                setDataSheet(booking.getGivenName() + " " + booking.getFamilyName(), booking.getClinicDescEn(), booking.getService().getDesc(),
                        Common.getConvertToDate(booking.getApptDateString()), Common.getCurrentTime12(booking.getApptDateString()));

        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

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
        }
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

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isGPSEnabled(requireActivity()) && SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, "").length() > 0) {
                    checkInPresenter.callCheckLocation(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LONG, ""),
                            Common.getConvertToCheckIn(booking.getApptDateString()),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                }


            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("HomeFragment", 1);
                startActivity(intent);

            }
        });

        imageViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("RecordFragment", 2);
                startActivity(intent);
            }
        });

        appoint_center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("AppointmentFragment", 3);
                startActivity(intent);
            }
        });

        mayoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("MayoFragment", 4);
                startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("ProfileFragment", 5);
                startActivity(intent);
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }



    void setDataSheet(String phy_name, String clinic, String service, String date, String time) {
        if (phy_name != null)
            tvPhysicianName.setText(phy_name);
        if (clinic != null)
            tvClinicName.setText(clinic);
        if (service != null)
            tvPhysicianSpeciality.setText(service);
        if (date != null)
            tvAppointmentDate.setText(date);
        if (time != null)
            tvAppointmentTime.setText(Common.parseShortTime(time));

        layPrice.setVisibility(View.VISIBLE);
        tvAppointmentType.setText(getString(R.string.in_person));
    }


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
                ivSearch.setVisibility(View.VISIBLE);
                mainNoDataTrans.setVisibility(View.VISIBLE);

            } else {
//                rvClinics.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
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
            ivSearch.setVisibility(View.VISIBLE);
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
//                    Common.makeToast(getActivity(), msg);
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

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

    @Override
    public void onGetLocation(SimpleResponse response) {
        if (booking.getPaymentStatus() == 1) {//1
            checkInPresenter.callArrival(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getId()),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        } else {
            String time = Common.getConvertToCheckInTime(booking.getApptDateString());
            String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());

            checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(booking.getSessionId()), time, selected_date, String.valueOf(booking.getId()), "0", "1", "0"
                    , SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }



    @Override
    public void onGetPaymentSuccess(PaymentResponse response) {
        if (response.getPaymentUrl() != null) {
            TelrActivity.startActivityForResult(this, response.getPaymentUrl(), response.getPaymentRef(), "upcoming",
                    String.valueOf(booking.getId()), arrival);
//            if(fragmentAdd != null){
//                    fragmentAdd.openPaymentInfoFragment(response,"");
//                }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(false);
            builder.setTitle("");
            builder.setMessage(response.getMessage());
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i1 = new Intent(getActivity(), MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    getActivity().finish();
                    dialog.dismiss();

//                    AppointmentActivity.start(getActivity(), booking);
                }
            });

            final AlertDialog alert = builder.create();

            alert.show();
        }
    }

    @Override
    public void onArrived(SimpleResponse response) {

    }
}
