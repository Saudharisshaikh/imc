package sa.med.imc.myimc.Physicians.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.GuestHome.GuestHomeActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;


/**
 *
 */

public class BottomConfirmDialogNextAvailable extends BottomSheetDialogFragment {

    PhysicianDetailResponse.PhysicianData physician_full;
    NextTimeResponse.Datum session;
    PhysicianResponse.Physician physician;
    @BindView(R.id.iv_back_bottom)
    ImageView ivBackBottom;
    @BindView(R.id.tv_clinic_name)
    TextView tvClinicName;
    @BindView(R.id.tv_physician_name_b)
    TextView tvPhysicianName;
    @BindView(R.id.tv_physician_speciality)
    TextView tvPhysicianSpeciality;
    @BindView(R.id.tv_appointment_date)
    TextView tvAppointmentDate;
    @BindView(R.id.tv_appointment_time)
    TextView tvAppointmentTime;
    @BindView(R.id.tv_appointment_type)
    TextView tvAppointmentType;
    @BindView(R.id.tv_patient_pay)
    TextView tvPatientPay;
    @BindView(R.id.tv_patient_discount)
    TextView tvPatientDiscount;
    @BindView(R.id.tv_patient_vat)
    TextView tvPatientVat;
    @BindView(R.id.tv_appointment_price)
    TextView tvAppointmentPrice;
    @BindView(R.id.lay_price)
    LinearLayout layPrice;
    @BindView(R.id.lay_b)
    LinearLayout layB;
    @BindView(R.id.view_line_top)
    View viewLineTop;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.lay_item)
    RelativeLayout layItem;
    String phone = "";

    @NonNull
    public static BottomConfirmDialogNextAvailable newInstance() {
        BottomConfirmDialogNextAvailable astro = new BottomConfirmDialogNextAvailable();

        return astro;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // reusing...
        View view = inflater.inflate(R.layout.bottom_sheet_confirm_booking, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        ButterKnife.bind(this, view);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");
        if (getArguments() != null) {
            if (getArguments().containsKey("data")) {
                physician = (PhysicianResponse.Physician) getArguments().getSerializable("data");
                session = (NextTimeResponse.Datum) getArguments().getSerializable("session");
            }


            // check language and show data accordingly
            if (physician_full != null) {
                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                    setDataSheet(physician_full.getGivenNameAr() + " " + physician_full.getFamilyNameAr(), physician_full.getClincNameAr(), physician_full.getService().getDescAr(), session.getSessionDateShort(), session.getSessionTimeShort());
                else
                    setDataSheet(physician_full.getGivenName() + " " + physician_full.getFamilyName(), physician_full.getClinicNameEn(), physician_full.getService().getDesc(), session.getSessionDateShort(), session.getSessionTimeShort());


            } else {
                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
                    setDataSheet(physician.getGivenNameAr() + " " + physician.getFamilyNameAr(), physician.getClincNameAr(), physician.getService().getDescAr(), session.getSessionDateShort(), session.getSessionTimeShort());
                else
                    setDataSheet(physician.getGivenName() + " " + physician.getFamilyName(), physician.getClinicNameEn(), physician.getService().getDesc(), session.getSessionDateShort(), session.getSessionTimeShort());
            }

        }

        return view;
    }

    // Set data in confirm booking sheet
    void setDataSheet(String phy_name, String clinic, String service, String date, String time) {
        if (phy_name != null)
            tvPhysicianName.setText(phy_name);
        if (clinic != null)
            tvClinicName.setText(clinic);
        if (service != null)
            tvPhysicianSpeciality.setText(service);
        if (date != null)
            tvAppointmentDate.setText(Common.parseShortDate(date));
        if (time != null)
            tvAppointmentTime.setText(Common.parseShortTime(time));

        layPrice.setVisibility(View.GONE);
        tvAppointmentType.setText(getString(R.string.in_person));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick({R.id.iv_back_bottom, R.id.tv_yes, R.id.tv_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_bottom:
                dismiss();
                break;

            case R.id.tv_yes:
                String teleHealthFlag = "0";

                callBookAppointment(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                        String.valueOf(session.getSessionId()), session.getSessionTimeShort(), session.getSessionDateShort(),
                        teleHealthFlag, SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//              Set the value for SessionID to get the pdf report for Telr payment
                SharedPreferencesUtils.getInstance(getContext()).setValue(Constants.SESSION_ID, session.getSessionId());

//                mDialogListener.onConfirm(session.getSessionDateShort());

                break;

            case R.id.tv_no:
                dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void callBookAppointment(String token, String mrNumber, String sessionId, String timeSlot, String date, String telehealth, int hosp) {
        Common.showDialog(getActivity());
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("sessionId", sessionId);
            object.put("timeSlot", timeSlot.toLowerCase());
            object.put("date", date);
            object.put("bookingId", "");
            object.put("teleHealthFlag", telehealth);
            object.put("teleHealthFlag", telehealth);
            object.put("hosp", hosp);
            /* object.put("consent", "1");*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.bookAppointment(body);

        xxx.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    BookResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true") || response1.getStatus().equalsIgnoreCase("200")) {
                            onBookAppointmentSuccess(response1);
                        } else
                            onFail(response1.getMessage());
                    } else {
                        onFail(getActivity().getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    onFail(getActivity().getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Common.hideDialog();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    onNoInternet();
                } else {
                    message = "Unknown";
                    onFail(message);

                }
            }
        });

    }

    public void onBookAppointmentSuccess(BookResponse response) {
        String statusMsg = "", title = getString(R.string.failed);
        if (response.getPaymentUrl() != null) {
            if (response.getPaymentRef() != null) {
                TelrActivity.startActivity(getActivity(), response.getPaymentUrl(), response.getPaymentRef());
            } else {
                statusMsg = getString(R.string.booking_failed);//"bookings.failed";
                bookingStatusDialog(title, statusMsg, response.getData().getStatus());
            }
        } else {
            switch (response.getData().getStatus()) {
                case 99:
                    title = getString(R.string.success);
                    statusMsg = getString(R.string.success_booking);//"Booked successfully";
                    break;

                case -4:
                    statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                    break;

                case 0:
                    statusMsg = getString(R.string.booking_exist, phone);//"Bookings exists";
                    break;

                case -5:
                    statusMsg = getString(R.string.no_show_message);//"Bookings no_show";
                    break;

                case -2:
                    statusMsg = getString(R.string.booking_time_in_other_clinic);//"booking.exists_time_allowed";
                    break;

                // Max 3 bookings per day configurable from admin side in config table
                case -6:
                    statusMsg = getString(R.string.booking_max);//"booking.max_total_booking_reached";
                    break;

                case -7:
                    statusMsg = getString(R.string.booking_slot_taken);//"Bookings timeslot already taken";
                    break;

                case -8:
                    statusMsg = getString(R.string.booking_age_less);// "booking.age_less_than_required";
                    break;

                case -9:
                    statusMsg = getString(R.string.booking_age_more);//"booking.age_more_than_required";
                    break;

                case -10:
                    statusMsg = getString(R.string.booking_gender);//"booking.gender_conflict";
                    break;

                case -50:
                    statusMsg = response.getMessage();//"booking failed for guest user ";
                    break;

                case -51:
                    statusMsg = response.getMessage();//"booking failed for guest user max limit exceeds";
                    break;

                case 111:
                    statusMsg = getString(R.string.pending_pre_authorization);
                    break;


                default:
                    statusMsg = getString(R.string.booking_failed);//"bookings.failed";
                    break;
            }
            bookingStatusDialog(title, statusMsg, response.getData().getStatus());
        }
    }

    public void bookingStatusDialog(String title, String message, int status) {
        if (status == 99)
            showSnackBarMsg(getString(R.string.booking_confirmed));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (status == 99) {

                            if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_USER_TYPE, "").equalsIgnoreCase(Constants.GUEST_TYPE)) {
                                Intent i = new Intent(getActivity(), GuestHomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {

                                Intent i1 = new Intent(getActivity(), MainActivity.class);
                                i1.putExtra("restart", "yes");
                                i1.putExtra("My_Flag", "Unique");
                                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);


                            }
                            dismiss();
                        }


                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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


    protected void showSnackBarMsg(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    public void onFail(String msg) {
        Common.makeToast(getActivity(), msg);
    }


    public void onNoInternet() {
        Common.noInternet(getActivity());
    }
}
