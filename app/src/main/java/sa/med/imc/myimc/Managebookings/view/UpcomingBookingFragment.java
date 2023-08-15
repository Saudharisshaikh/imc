package sa.med.imc.myimc.Managebookings.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Adapter.UpcomingBookingAdapter;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Interfaces.CheckLocation;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.presenter.BookingImpl;
import sa.med.imc.myimc.Managebookings.presenter.BookingPresenter;
import sa.med.imc.myimc.Managebookings.presenter.CheckInImpl;
import sa.med.imc.myimc.Managebookings.presenter.CheckInPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.Questionaires.view.QuestionareActivity;
import sa.med.imc.myimc.R;

import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.ViewModel.CreateRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Telr.UpcomingTelrFragment;

import sa.med.imc.myimc.Utils.Common;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;
import static sa.med.imc.myimc.Utils.Common.ReportTypes.SLIP;



public class UpcomingBookingFragment extends Fragment implements CheckInViews, BookingViews, BottomPaymentDialog.BottomDailogListener, BottomFilterBookingDialog.BottomDialogListener{

    private static final int CREATE_SLIP_FILE = 6;
    private ResponseBody responseBody;

    @BindView(R.id.rv_upcoming_booking)
    RecyclerView rvUpcomingBooking;

    UpcomingBookingAdapter adapter;
    List<Booking> upcomingList = new ArrayList<>();
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
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;

    Booking booking;

    BookingPresenter presenter;
    int selected_pos = -1;
    Boolean isLoading = false;
    int page = 0, total_items = 0;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    BottomFilterBookingDialog bottomFilterBookingDialog;
    ImageView filterReference;
    String doc_id = "", clinic_id = "", from = "", to = "", latitude = "", longitude = "", arrival = "0";
    int check = 0;
    BookingResponse response;
    BottomPaymentDialog bottomPaymentDialog;
    BottomBarcodeDialog mBottomBarcodeDialog;
    CheckInPresenter checkInPresenter;
    CheckLocation checkLocation;
    FragmentListener fragmentAdd;

    public static final String tag = "";

    public UpcomingBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkLocation = (CheckLocation) getActivity();
//        fragmentAdd = (FragmentAdd) getActivity();
    }

    public static UpcomingBookingFragment newInstance() {
        UpcomingBookingFragment fragment = new UpcomingBookingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_booking, container, false);
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setRotationY(180);
        }

        ButterKnife.bind(this, view);

        latitude = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, "");
        longitude = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LONG, "");

        check = 0;
        arrival = "0";

        upcomingList = new ArrayList<>();
        checkInPresenter = new CheckInImpl(this, getActivity());

        presenter = new BookingImpl(this, getActivity());
        setUpBookingRecyclerView();
        page = 0;
        callAPI();

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
                        , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.UPCOMING);
            }

        });


        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (upcomingList.size() < total_items && isLoading) {
                        isLoading = false;
                        progressBarLoad.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(response.getTotalBookings()) > upcomingList.size())
                            if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
                                callFilterAPI();
                            } else
                                callAPI();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (upcomingList.size() > 0) {
            swipeToRefresh.setRefreshing(true);
            page = 0;
            callAPI();
        }
    }

    //Get Upcoming bookings data
    void callAPI() {
        if (page == 0) {
            progressBar.setVisibility(View.VISIBLE);
            rvUpcomingBooking.setVisibility(View.VISIBLE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        }

        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "")
                , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.UPCOMING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Initialize Upcoming Booking list view and add adapter to display data,
    void setUpBookingRecyclerView() {
//        adapter = new UpcomingBookingAdapter(getActivity(), upcomingList);
        rvUpcomingBooking.setHasFixedSize(true);
        rvUpcomingBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUpcomingBooking.setAdapter(adapter);


        adapter.setOnItemClickListener(new UpcomingBookingAdapter.OnItemClickListener() {
            @Override
            public void onRescheduleBooking(int position) {
                if (position < upcomingList.size())
                    if (upcomingList.get(position).getService() != null)
                        rescheduleBookingConfirmation(position);
                    else
                        setNoServicesDialog(getActivity());

            }

            @Override
            public void onCancelBooking(int position) {
                if (position < upcomingList.size())
                    cancelBookingConfirmation(position);
            }

            @Override
            public void onPrint(int position) {
                if (position < upcomingList.size()) {
                    presenter.callPrintSlipApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""), upcomingList.get(position).getId(),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
                }
            }

            @Override
            public void onCheck(int position) {
                selected_pos = position;

                String selected_date = Common.getConvertToPriceDate(upcomingList.get(position).getApptDateString());

                presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        String.valueOf(upcomingList.get(position).getSessionId()), selected_date,
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

            }

            @Override
            public void onConfirmArrival(int position) {
                selected_pos = position;
//                if (isGPSEnabled(getActivity()) && SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, "").length() > 0) {
//                    confirmArrival(0);
//                } else {
//                    if (checkLocation != null)
//                        checkLocation.checkLocation();
//                }

            }

            @Override
            public void onScanBarCode(int position) {
                if (mBottomBarcodeDialog == null)
                    mBottomBarcodeDialog = BottomBarcodeDialog.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(upcomingList.get(position).getId()));

                mBottomBarcodeDialog.setArguments(bundle);

                if (!mBottomBarcodeDialog.isAdded())
                    mBottomBarcodeDialog.show(getChildFragmentManager(), "DAILOG");
            }

            @Override
            public void onCheckinButtonListener(int position) {

                Log.d("UpcomingBookingFragment", "onCheckinButtonListener: on check in button listener" + position);
                selected_pos = position;
//                if (fragmentAdd != null)
//                    if (booking.getPaymentStatus() == 0) {
//                        String selected_date = Common.getConvertToPriceDate(booking.getApptDateString());
//                        presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                                String.valueOf(booking.getSessionId()), selected_date,
//                                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                    }
//
                String selected_date = Common.getConvertToPriceDate(upcomingList.get(position).getApptDateString());

                presenter.callGetAppointmentPrice(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        String.valueOf(upcomingList.get(position).getSessionId()), selected_date,
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

            }

            @Override
            public void onProfileClick(int position) {
                if (upcomingList.get(position).getDoc_isActive() == null || upcomingList.get(position).getDoc_isActive().equalsIgnoreCase("0"))
                    Common.doctorNotAvailableDialog(getActivity());
                else {
//                    PhysicianDetailActivity.startActivity(getActivity(), upcomingList.get(position).getDocCode(), upcomingList.get(position).getClinicCode());
                    Booking booking = upcomingList.get(position);
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
                    if (booking.getService() != null) {
                        if (booking.getService().getDeptCode() != null)
                            service.setDeptCode(booking.getService().getDeptCode());

                        if (booking.getService().getService_id() != null)
                            service.setId(booking.getService().getService_id());
                        service.setDesc(booking.getService().getDesc());
                        service.setDescAr(booking.getService().getDescAr());
                    }
                    physician.setService(service);

                    PhysicianDetailActivity.startActivity(getActivity(), String.valueOf(booking.getDocCode()), booking.getClinicCode(), physician);

                }

            }

            @Override
            public void onGotoConsult(int position, String DrName) {
//                if (upcomingList.get(position).getTeleHealthLink() != null) {
                if (upcomingList.get(position).getApptDateString() != null) {
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN, upcomingList.get(position).getDocCode());
                    SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
                    String bookingDateTime = upcomingList.get(position).getApptDateString();

//                    Date today = new Date();
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
//                    String currentDateTimeS = format.format(today);
//                    long differentInMiniutes = Common.findDifference(currentDateTimeS, bookingDateTime);
//                    if(differentInMiniutes <= 10){
                        gotoConsultConfirmation(position, upcomingList.get(position).getId(), bookingDateTime);
//                    }
//                    else
//                    {
//                        gotoCheckTiming(position, upcomingList.get(position).getId(), bookingDateTime);
//                    }
                }
            }

            @Override
            public void videoCall(int position, String DrName) {

                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN, upcomingList.get(position).getDocCode());
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
                clickToStartVideoCall(String.valueOf(upcomingList.get(position).getId()),upcomingList.get(position).getApptDateString());
            }

            @Override
            public void onStartAssessment(int position) {
//                QuestionareActivity.startActivity(getActivity(), upcomingList.get(position));

            }
        });
    }

    // cancel booking confirmation alert
    void cancelBookingConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_cancel_booking));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selected_pos = position;
                presenter.callCancelBooking(String.valueOf(upcomingList.get(position).getId()),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }


    // reschedule booking confirmation alert
    void rescheduleBookingConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_reschedule_booking));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                AppointmentActivity.start(getActivity(), upcomingList.get(position));

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    void gotoConsultConfirmation(int position, int bookingId, String bookingDateTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.want_go_consult));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
    //                PIYUSH-24-03-22
                    /*Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(upcomingList.get(position).getTeleHealthLink()));
                    startActivity(i);*/
    //                Intent in = new Intent(getActivity(), SelectionActivity.class);
    //                startActivity(in);
                String DrName = null;
                String lang = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                if (upcomingList.get(position).getGivenName() != null) {
//                    if (booking.getFamilyName() != null) {
//                        DrName =  "DR. " + booking.getGivenName() + " " + booking.getFamilyName();
//                    }
//                    else {
//                        DrName= "DR. " + booking.getGivenName();
//                    }
                    if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getGivenNameAr() + " - " + booking.getClinicDescAr();
                    }
                    else {
                        DrName = getResources().getString(R.string.dr) + " " + booking.getGivenName() + " - " + booking.getClinicDescEn();
                    }
                }
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN, upcomingList.get(position).getDocCode());
                SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_VIDEO_PHYSICIAN_NAME, DrName);
                clickToStartVideoCall(String.valueOf(bookingId), bookingDateTime);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
public void clickToStartVideoCall(String bookingId, String bookingDateTime) {
    try {
        String lang;
        if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        }
        else {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        }
        Common.showDialog(getActivity());
        CreateRoomViewModel viewModel = ViewModelProviders.of(getActivity()).get(CreateRoomViewModel.class);
        viewModel.init();
        CreateRoomRequestModel createRoomRequestModel = new CreateRoomRequestModel();
        createRoomRequestModel.setPageNumber(0);
        createRoomRequestModel.setPageSize(0);
        String mrnNumber = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, null);
        String physician = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_VIDEO_PHYSICIAN, null);
        createRoomRequestModel.setRoomName(mrnNumber + "_" + physician);
        createRoomRequestModel.setUserEmail("xyz@gmail.com");
        createRoomRequestModel.setBookingId(bookingId);
        createRoomRequestModel.setDoctorId(physician);
        createRoomRequestModel.setUserId(mrnNumber);
        createRoomRequestModel.setBookingSlotTime(bookingDateTime);
        createRoomRequestModel.setDeviceType("Android");
        String deviceToken = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_DEVICE_ID, "");
        createRoomRequestModel.setDeviceToken(deviceToken);
        String token = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, null);
        viewModel.CreateRoom(token, lang,SharedPreferencesUtils.getInstance(getContext()).getValue(HOSPITAL_CODE, "IMC"), createRoomRequestModel);
        viewModel.getVolumesResponseLiveData().observe(getActivity(), response -> {

            if (response != null) {
                if (response.getStatus()) {
                    Common.hideDialog();
                    Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Dexter.withContext(getActivity()).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    SharedPreferencesUtils.getInstance(getActivity()).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ACCESS_TOKEN, response.getData().getAccessToken());
                                    SharedPreferencesUtils.getInstance(getActivity()).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_BACK_CLASS, "2");
//                                        if(!response.getBookingCompletedStatus()) {
                                    if(response.getValidBufferRange()) {
//                                                Common.hideDialog();
                                        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_EMERGENCY_CALL, false);
                                        Intent in = new Intent(getActivity(), VideoActivity.class);
                                        startActivity(in);
                                        getActivity().finish();
                                    }
                                    else
                                    {
//                                                Common.hideDialog();
                                        SharedPreferencesUtils.getInstance(getActivity()).setValue(Constants.KEY_EMERGENCY_CALL, false);
//                                                gotoCheckTiming("Message!", "Meetings can be joined up to "+response.getBufferRangeBefore()+" minutes before the start time.", 1);
                                        gotoCheckTiming(getResources().getString(R.string.informationMessage), response.getBufferRangeMessage(), 1);
                                    }
//                                        }
//                                        else
//                                        {
//                                            gotoCheckTiming(getResources().getString(R.string.alert), response.getBookingCompletedMessage(), 2);
//                                        }
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
                }
                else{
                    Common.hideDialog();
                }
            } else {
                Common.hideDialog();
                Toast.makeText(getActivity(), "Some thing went wrong ", Toast.LENGTH_SHORT).show();
            }
        });
    }catch (Exception e){
        Common.hideDialog();
        e.printStackTrace();
    }
}
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    // goto Consult booking confirmation alert
    void gotoCheckTiming(String title, String message, int E) {
//        PIYUSH 30-03-2022
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(E == 1) {
                    dialog.dismiss();
                    Intent in = new Intent(getActivity(), VideoActivity.class);
                    startActivity(in);
                    getActivity().finish();
                }
                else if( E == 2){
                    dialog.dismiss();
                }
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            }
        });

        alert.show();
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public void onGetBookings(BookingResponse response) {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        arrival = "0";
        if (page == 0)
            upcomingList.clear();

        if (swipeToRefresh.isRefreshing()) {
            upcomingList.clear();
            swipeToRefresh.setRefreshing(false);
        }

        this.response = response;
        if (response.getBookings() != null)
            upcomingList.addAll(response.getBookings());


        if (page == 0) {
            if (upcomingList.size() == 0) {
                rvUpcomingBooking.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.VISIBLE);
            } else {
                rvUpcomingBooking.setVisibility(View.VISIBLE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);

            }
        }

        if (response.getTotalBookings() != null) {
            tvTotal.setText(response.getTotalBookings() + "");
            total_items = Integer.parseInt(response.getTotalBookings());

            if (Integer.parseInt(response.getTotalBookings()) > upcomingList.size()) {
                tvLoadMore.setVisibility(View.GONE);//VISIBLE
                page = page + 1;
                isLoading = true;

            } else
                tvLoadMore.setVisibility(View.GONE);
        }

        if (response.getBookings().size() > 0) {
            if (response.getBookings().get(0).getBookingStatus() == 2){

                booking = response.getBookings().get(0);
            }


        }

                tvLoadMore.setEnabled(true);

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

        onFail(response.getMessage());
        upcomingList.remove(selected_pos);
        adapter.notifyDataSetChanged();

        if (upcomingList.size() == 0) {
            mainNoData.setVisibility(View.VISIBLE);
        } else {
            mainNoData.setVisibility(View.GONE);
        }

//        swipeToRefresh.setRefreshing(true);
        selected_pos = -1;

        try {
            Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
            refresh.putExtra("cancel", "cancel");
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(refresh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadConfirmation(ResponseBody response, Headers headers) {
        responseBody = response;
        initDownloadFileFlow(SLIP);
    }

    private void initDownloadFileFlow(Common.ReportTypes reportTypes) {
        switch (reportTypes) {
            case SLIP:
                String slipFile = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_booking_confirmation_" + System.currentTimeMillis() + ".pdf";
                createFile(slipFile, CREATE_SLIP_FILE);
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
        if (requestCode == CREATE_SLIP_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                alterDocument(uri);
                openFile(uri);
            }
        }
    }

    @Override
    public void onGetPrice(PriceResponse response) {
//        ((ManageBookingsActivity)getContext()).openTelrFragment(booking,response,"telrfragment");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Response", response);
        bundle.putSerializable("booking",booking);
        UpcomingTelrFragment fragInfo = new UpcomingTelrFragment();
        fragInfo.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.upcoming_telr_frag, fragInfo).
//                addToBackStack("").commit();



//        if (bottomPaymentDialog == null)
//            bottomPaymentDialog = BottomPaymentDialog.newInstance();
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("booking", upcomingList.get(selected_pos));
//        bundle.putSerializable("price", response);
//
//        bottomPaymentDialog.setArguments(bundle);
//
//        if (!bottomPaymentDialog.isAdded())
//            bottomPaymentDialog.show(getChildFragmentManager(), "DAILOG");

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
                if (upcomingList.size() == 0) {
                    rvUpcomingBooking.setVisibility(View.GONE);
                    mainNoNet.setVisibility(View.GONE);
                    mainTimeOut.setVisibility(View.VISIBLE);
                    mainNoData.setVisibility(View.GONE);
                } else {
                    Common.makeToast(getActivity(), getString(R.string.time_out_messgae));
                    isLoading = true;
                }
            } else {
                if (msg.equalsIgnoreCase(getString(R.string.plesae_try_again)))
                    Common.makeToast(getActivity(), msg);
                else
                    Common.showAlert(getActivity(), msg);
            }
    }

    @Override
    public void onNoInternet() {
        progressBar.setVisibility(View.GONE);
        progressBarLoad.setVisibility(View.INVISIBLE);
        swipeToRefresh.setRefreshing(false);
        tvLoadMore.setEnabled(true);
        if (upcomingList.size() == 0) {
            rvUpcomingBooking.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        } else {
            Common.noInternet(getActivity());
            isLoading = true;
        }

    }

    @OnClick({R.id.tv_load_more, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_load_more:
                tvLoadMore.setEnabled(false);

                progressBarLoad.setVisibility(View.VISIBLE);
                if (Integer.parseInt(response.getTotalBookings()) > upcomingList.size())
                    if (doc_id.length() > 0 || clinic_id.length() > 0 || from.length() > 0 || to.length() > 0) {
                        callFilterAPI();
                    } else
                        callAPI();
                break;

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;
        }
    }

//    @Override
//    public void onClick(int position, ImageView view) {
//        filterReference = view;
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
            rvUpcomingBooking.setVisibility(View.VISIBLE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
        }

        presenter.callGetAllBookingsApi(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "")
                , Constants.RECORD_SET, String.valueOf(page), Constants.BookingStatus.UPCOMING, clinic_id, doc_id, from, to);


    }

    //Save lab report data in storage
//    void downloadFile(ResponseBody responseBody, Headers headers) {
//        try {
//            int count = 0;
//            String depo = headers.get("Content-Disposition");
//            String[] depoSplit = depo.split("filename=");
//            String filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
//
//            filename = SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, "") + "_booking_confirmation_" + System.currentTimeMillis() + ".pdf";
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
//            openFile(path);
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
//    }

    // Show download success and ask to open file
    void openFile(Uri uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        builder.setMessage(getResources().getString(R.string.download_confirmation));
        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfOpenintent.setDataAndType(uri, "application/pdf");
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

    // If doctor's service is null
    void setNoServicesDialog(Activity activity) {
        String phone = "";
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert))
                .setMessage(activity.getString(R.string.no_service_content, phone))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        alertDialog.show();
    }

    @Override
    public void onPayAndCheckIN() {
        arrival = "1";
        confirmArrival(0);
    }

    @Override
    public void onPayOnly(String date) {
        arrival = "0";
        String time = Common.getConvertToCheckInTime(upcomingList.get(selected_pos).getApptDateString());
        String selected_date = Common.getConvertToPriceDate(upcomingList.get(selected_pos).getApptDateString());

        checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                String.valueOf(upcomingList.get(selected_pos).getSessionId()), time, selected_date, String.valueOf(upcomingList.get(selected_pos).getId()), "0", "1", "0",
                SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

    }

    //confirm arrival dialog
    void confirmArrival(int check) {
        this.check = check;
        try {
            Dialog sucDialog = new Dialog(getActivity()); // Context, this, etc.
            sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            sucDialog.setContentView(R.layout.dialog_confirm_arrival);

            TextView yes = sucDialog.findViewById(R.id.tv_yes);
            TextView tv_no = sucDialog.findViewById(R.id.tv_no);
            ImageView iv_back_bottom = sucDialog.findViewById(R.id.iv_back_bottom);

            sucDialog.setCancelable(false);

            yes.setOnClickListener(v -> {
                sucDialog.dismiss();
//                if (check == 0)
                checkInPresenter.callCheckLocation(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LAT, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LONG, ""), Common.getConvertToCheckIn(upcomingList.get(selected_pos).getApptDateString()), SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                else
//                    checkInPresenter.callArrival(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                            String.valueOf(upcomingList.get(selected_pos).getId()));

//                    String selected_date = Common.getConvertToPriceDate(upcomingList.get(selected_pos).getApptDateString());
//                    String time = Common.getConvertToCheckInTime(upcomingList.get(selected_pos).getApptDateString());
//
//
//                    checkInPresenter.callPayAndCheckIn(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
//                            String.valueOf(upcomingList.get(selected_pos).getSessionId()), time, selected_date, String.valueOf(upcomingList.get(selected_pos).getId()), "0", "1");
            });

            tv_no.setOnClickListener(v -> sucDialog.dismiss());
            iv_back_bottom.setOnClickListener(v -> sucDialog.cancel());

            sucDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetLocation(SimpleResponse response) {
        if (upcomingList.get(selected_pos).getPaymentStatus() == 1) {//1
            checkInPresenter.callArrival(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(upcomingList.get(selected_pos).getId()),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        } else {
            String time = Common.getConvertToCheckInTime(upcomingList.get(selected_pos).getApptDateString());
            String selected_date = Common.getConvertToPriceDate(upcomingList.get(selected_pos).getApptDateString());

            checkInPresenter.callPayment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    String.valueOf(upcomingList.get(selected_pos).getSessionId()), time, selected_date, String.valueOf(upcomingList.get(selected_pos).getId()), "0", "1", "0",
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
        }
    }

    @Override
    public void onGetPaymentSuccess(PaymentResponse response) {
        if (response.getPaymentUrl() != null) {
            TelrActivity.startActivity(getActivity(), response.getPaymentUrl(), response.getPaymentRef(), "upcoming", String.valueOf(upcomingList.get(selected_pos).getId()), arrival);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(false);
            builder.setTitle("");
            builder.setMessage(response.getMessage());
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i1 = new Intent(getActivity(), ManageBookingsActivity.class);
                    startActivity(i1);
                    getActivity().finish();
                    dialog.dismiss();
                }
            });

            final AlertDialog alert = builder.create();

            alert.show();
        }

    }

    @Override
    public void onArrived(SimpleResponse response) {
        if (upcomingList.size() > 0) {
            swipeToRefresh.setRefreshing(true);
            page = 0;
            callAPI();

            try {
                Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
                refresh.putExtra("cancel", "cancel");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(refresh);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
