package sa.med.imc.myimc.Managebookings.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;


/**
 *
 */

public class BottomPaymentDialog extends BottomSheetDialogFragment {

    BottomDailogListener mDialogListener;
    @BindView(R.id.iv_back_bottom)
    ImageView ivBackBottom;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.tv_clinic_name)
    TextView tvClinicName;
    @BindView(R.id.tv_physician_name)
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
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_pay_cash)
    TextView tvPayCash;
    @BindView(R.id.lay_item)
    RelativeLayout layItem;
    Booking booking;

    @NonNull
    public static BottomPaymentDialog newInstance() {
        BottomPaymentDialog astro = new BottomPaymentDialog();

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
        View view = inflater.inflate(R.layout.bottom_sheet_confirm_payment, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable("booking");
            PriceResponse response = (PriceResponse) getArguments().getSerializable("price");

//            if (booking.isHidePayCheckin())
//                tvPayCash.setEnabled(false);
//            else
            tvPayCash.setEnabled(true);


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
            tvAppointmentDate.setText(date);
        if (time != null)
            tvAppointmentTime.setText(Common.parseShortTime(time));

        layPrice.setVisibility(View.VISIBLE);
        tvAppointmentType.setText(getString(R.string.in_person));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mDialogListener = (BottomDailogListener) parent;
        } else {
            mDialogListener = (BottomDailogListener) context;
        }
    }

    @Override
    public void onDetach() {
        mDialogListener = null;
        super.onDetach();
    }

    @OnClick({R.id.iv_back_bottom, R.id.tv_cancel, R.id.tv_pay_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_bottom:
                dismiss();
                break;

            case R.id.tv_cancel:
//                mDialogListener.onPayOnly(booking.getApptDateString());
                dismiss();
                break;

            case R.id.tv_pay_cash:
                mDialogListener.onPayAndCheckIN();
                dismiss();
                break;
        }
    }


    public interface BottomDailogListener {
        void onPayOnly(String date);

        void onPayAndCheckIN();
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
}
