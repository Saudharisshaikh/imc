 package sa.med.imc.myimc.Managebookings.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.CancelledBookingAdapter;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.presenter.BookingImpl;
import sa.med.imc.myimc.Managebookings.presenter.BookingPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;


public class CancelledBookingFragment extends Fragment implements BookingViews, BottomFilterBookingDialog.BottomDialogListener{

    @BindView(R.id.rv_canceled_booking)
    RecyclerView rvCanceledBooking;
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
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;

    String doc_id = "", clinic_id = "", from = "", to = "";
    Boolean isLoading = false;
    int page = 0, total_items = 0;

    CancelledBookingAdapter adapter;
    List<Booking> cancelledList = new ArrayList<>();

    BookingPresenter presenter;
    BookingResponse response;


    BottomFilterBookingDialog bottomFilterBookingDialog;
    ImageView filterReference;

    public CancelledBookingFragment() {
        // Required empty public constructor
    }

    public static CancelledBookingFragment newInstance() {
        CancelledBookingFragment fragment = new CancelledBookingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelled, container, false);
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setRotationY(180);
        }

        ButterKnife.bind(this, view);

        cancelledList = new ArrayList<>();
        presenter = new BookingImpl(this, getActivity());
        setUpBookingRecyclerView();

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                tvLoadMore.setVisibility(View.GONE);

                if (filterReference != null) {
                    filterReference.setImageResource(R.drawable.ic_filter_blue);
                }
                doc_id = "";
                clinic_id = "";
                from = "";
                to = "";

                presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "")
                        , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.CANCEL);
            }
        });


        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //Reaches to the bottom so it is time to updateRecyclerView!
                    Log.e("Last Item Wow !", " ===========");


                    if (cancelledList.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(response.getTotalBookings()) > cancelledList.size()) {
                            if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
                                callFilterAPI();
                            } else
                                callAPI();
                        }
                    }

                }
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {
            if (presenter != null)
                if (cancelledList.size() == 0)
                    callAPI();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_load_more:
                tvLoadMore.setEnabled(false);
                progressBarLoad.setVisibility(View.VISIBLE);
                if (Integer.parseInt(response.getTotalBookings()) > cancelledList.size()) {
                    if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
                        callFilterAPI();
                    } else
                        callAPI();
                }
                break;

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;
        }
    }

    //Get cancelled bookings data
    void callAPI() {
        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);
            rvCanceledBooking.setVisibility(View.VISIBLE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        }
        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "")
                , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.CANCEL);
    }

    // Initialize Completed Booking list view and add adapter to display data,
    void setUpBookingRecyclerView() {
//        adapter = new CancelledBookingAdapter(getActivity(), cancelledList);
        rvCanceledBooking.setHasFixedSize(true);
        rvCanceledBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCanceledBooking.setAdapter(adapter);
        adapter.setOnItemClickListener(new CancelledBookingAdapter.OnItemClickListener() {
            @Override
            public void onVisitDetail(int position) {

            }

            @Override
            public void onProfileClick(int position) {

                if (cancelledList.get(position).getDoc_isActive() == null || cancelledList.get(position).getDoc_isActive().equalsIgnoreCase("0"))
                    Common.doctorNotAvailableDialog(getActivity());
                else{
//                    PhysicianDetailActivity.startActivity(getActivity(), cancelledList.get(position).getDocCode(), cancelledList.get(position).getClinicCode());
                    Booking booking = cancelledList.get(position);
                    PhysicianResponse.Physician physician = new PhysicianResponse().new Physician();
                    physician.setClinicCode(booking.getClinicCode());
                    physician.setDocCode(String.valueOf(booking.getDocCode()));
                    physician.setGivenName(booking.getGivenName());
                    physician.setGivenNameAr(booking.getGivenNameAr());

                    physician.setFamilyName(booking.getFamilyName());
                    physician.setFamilyNameAr(booking.getFamilyNameAr());

                    physician.setFathersName(booking.getFathersName());
                    physician.setFathersNameAr(booking.getFatherNameAr());

                    physician.setClinicNameEn(booking.getClinicDescEn());
                    physician.setClincNameAr(booking.getClinicDescAr());
                    physician.setHrId(booking.getHrId());
                    physician.setTeleHealthEligibleFlag(booking.getTeleHealthLink());

                    PhysicianResponse.Service service = new PhysicianResponse().new Service();
                    if(booking.getService()!=null) {
                        if(booking.getService().getDeptCode()!=null)
                            service.setDeptCode(booking.getService().getDeptCode());

                        if(booking.getService().getService_id()!=null)
                            service.setId(booking.getService().getService_id());
                        service.setDesc(booking.getService().getDesc());
                        service.setDescAr(booking.getService().getDescAr());
                    }
                    physician.setService(service);

                    PhysicianDetailActivity.startActivity(getActivity(), String.valueOf(booking.getDocCode()), booking.getClinicCode(), physician);

                }
            }
        });

    }

    @Override
    public void onGetBookings(BookingResponse response) {
        this.response = response;

        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);


        if (page == 0)
            cancelledList.clear();


        if (swipeToRefresh.isRefreshing()) {
            cancelledList.clear();
            swipeToRefresh.setRefreshing(false);
        }

        if (response.getBookings() != null)
            cancelledList.addAll(response.getBookings());


        if (page == 0) {
            if (cancelledList.size() == 0) {
                rvCanceledBooking.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.VISIBLE);
            } else {
                rvCanceledBooking.setVisibility(View.VISIBLE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
            }
        }

        if (response.getTotalBookings() != null) {
            tvTotal.setText(response.getTotalBookings() + "");
            total_items = Integer.parseInt(response.getTotalBookings());

            if (Integer.parseInt(response.getTotalBookings()) > cancelledList.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                page = page + 1;
                isLoading = true;
            } else
                tvLoadMore.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetBookingsNew(BookingResponseNew response) {

    }

    @Override
    public void onReschedule(SimpleResponse response) {
    }

    @Override
    public void onCancel(SimpleResponse response) {
    }

    @Override
    public void onDownloadConfirmation(ResponseBody response, Headers headers) {

    }

    @Override
    public void onGetPrice(PriceResponse response) {

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
                if (cancelledList.size() == 0) {
                    rvCanceledBooking.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                } else {
                    isLoading = true;
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
        if (cancelledList.size() == 0) {
            rvCanceledBooking.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        } else {
            isLoading = true;
            Common.noInternet(getActivity());
        }
    }

//
//    @Override
//    public void onClick(int position, ImageView view) {
//        filterReference = view;
//
//        if (bottomFilterBookingDialog == null)
//            bottomFilterBookingDialog = BottomFilterBookingDialog.newInstance();
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("i", position);
//
//        bottomFilterBookingDialog.setArguments(bundle);
//
//        if (!bottomFilterBookingDialog.isAdded())
//            bottomFilterBookingDialog.show(getChildFragmentManager(), "DAILOG");
//    }

    @Override
    public void onDone(String doc_id, String clinic_id, String from, String to) {
        this.doc_id = doc_id;
        this.clinic_id = clinic_id;
        this.from = from;
        this.to = to;

        if (filterReference != null) {
            if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
                filterReference.setImageResource(R.drawable.ic_filter_green);
            } else {
                filterReference.setImageResource(R.drawable.ic_filter_blue);
            }
        }
        page = 0;
        callFilterAPI();
    }

    @Override
    public void onClear(String text) {
        if (filterReference != null) {
            filterReference.setImageResource(R.drawable.ic_filter_blue);
        }

        if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
            this.doc_id = "";
            this.clinic_id = "";
            this.from = "";
            this.to = "";

            page = 0;
            callFilterAPI();
        }
    }

    void callFilterAPI() {

        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);
            rvCanceledBooking.setVisibility(View.VISIBLE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        }

        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "")
                , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.CANCEL, clinic_id, doc_id, from, to);


    }
}
