package sa.med.imc.myimc.Managebookings.VisitDetail;

import static sa.med.imc.myimc.Utils.Common.toAMPM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Calculators.CalculatorsActivity;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Managebookings.model.VisitDetailReportResponse;
import sa.med.imc.myimc.Managebookings.presenter.BookingImpl;
import sa.med.imc.myimc.Managebookings.presenter.BookingPresenter;
import sa.med.imc.myimc.Managebookings.view.VisitDetailViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RateDoctor.RateDoctorActivity;
import sa.med.imc.myimc.Records.view.SingleFragmentActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

public class VisitCompletedAppointmentActivity extends BaseActivity implements VisitDetailViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_month_day)
    TextView tvMonthDay;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_time_am_pm)
    TextView tvTimeAmPm;
    @BindView(R.id.iv_profile)
    CircleImageView ivProfile;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_clinic_name)
    TextView tvClinicName;
    @BindView(R.id.lay_doc_profile)
    RelativeLayout layDocProfile;
    BookingAppoinment booking;
    @BindView(R.id.lay_lab_reports)
    RelativeLayout layLabReports;
    @BindView(R.id.lay_radiology)
    RelativeLayout layRadiology;
    @BindView(R.id.lay_sick_leave)
    RelativeLayout laySickLeave;

    BookingPresenter presenter;
    @BindView(R.id.lay_prescription)
    RelativeLayout layPrescription;

    public static void startActivity(Context activity, BookingAppoinment booking) {
        Intent intent = new Intent(activity, VisitCompletedAppointmentActivity.class);
        intent.putExtra("booking", new Gson().toJson(booking));
        activity.startActivity(intent);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(VisitCompletedAppointmentActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        VisitCompletedAppointmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_completed_appointment);
        ButterKnife.bind(this);

        booking = new Gson().fromJson(getIntent().getStringExtra("booking"), new TypeToken<BookingAppoinment>() {}.getType());

        String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        presenter = new BookingImpl(this, this);
//        presenter.callGetReportsCount(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
//                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
//                String.valueOf(booking.getId()), SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
            if (booking.getCareProviderDescription() != null)
                if (booking.getSpecialityDescription() != null)
                    tvDoctorName.setText(Html.fromHtml(getString(R.string.dr) + " " + booking.getCareProviderDescription()));
                else
                    tvDoctorName.setText(Html.fromHtml(getString(R.string.dr) + " " + booking.getCareProviderDescription()));

            if (booking.getSpecialityDescription() != null)
                tvClinicName.setText(Html.fromHtml(booking.getSpecialityDescription()));


        } else {
            if (booking.getCareProviderDescription() != null)
                if (booking.getCareProviderDescription() != null)
                    tvDoctorName.setText(Html.fromHtml(getString(R.string.dr) + " " + booking.getCareProviderDescription() ));
                else
                    tvDoctorName.setText(Html.fromHtml(getString(R.string.dr) + " " + booking.getCareProviderDescription()));

            if (booking.getSpecialityDescription() != null)
                tvClinicName.setText(Html.fromHtml(booking.getSpecialityDescription()));

        }

//        if (booking.getDocImg() != null)
//            if (booking.getDocImg().length() != 0) {
//                String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + booking.getDocImg();// + ".xhtml?In=default";
//                Picasso.get().load(url).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img).into(ivProfile);
//            }
        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(VisitCompletedAppointmentActivity.this, booking.getCareProviderCode()))
                .error(R.drawable.mdoc_placeholder)
                .placeholder(R.drawable.male_img).fit().into(ivProfile);



        if (booking.getApptDate() != null) {
            //2022-07-01 10:00:00
            String fullDate=booking.getApptDateString();
            String year=fullDate.substring(0,4);
            String month=fullDate.substring(5,7);
            String dayOfMonth=fullDate.substring(8,10);

            String time=fullDate.substring(11,16);

            String formatedTime=toAMPM(time);
            String onlyTime=formatedTime.substring(0,5);
            String amPm=formatedTime.substring(6);

            tvMonthDay.setText(dayOfMonth);
            tvMonth.setText(month+"/"+year);

            tvTime.setText(onlyTime);
            tvTimeAmPm.setText(amPm);

//            if (booking.getApptDate().length() > 0) {
//                tvMonth.setText(Common.getConvertToMonthHome(booking.getApptDateString()));
//                tvMonthDay.setText(Common.getConvertToDayHome(booking.getApptDateString()));
//                tvTime.setText(Common.getConvertToTimeHome(booking.getApptDateString()));
//                tvTimeAmPm.setText(Common.getConvertToAMHome(booking.getApptDateString()));
//            }
        }
        if (lang!=Constants.ENGLISH){
            try {
                if (!booking.getGivenNameAr().isEmpty()){
                    tvDoctorName.setText(booking.getGivenNameAr()+" "+booking.getFamilyNameAr());
                    tvClinicName.setText(booking.getClinicDescAr());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onBackPressed() {
        finish();
        VisitCompletedAppointmentActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @OnClick({R.id.lay_prescription, R.id.lay_btn_add_rating, R.id.iv_back, R.id.lay_lab_reports, R.id.lay_radiology, R.id.lay_sick_leave, R.id.lay_doc_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_prescription:
                SingleFragmentActivity.startActivity(VisitCompletedAppointmentActivity.this, getString(R.string.medication),
                        String.valueOf(booking.getId()), "");
                break;

            case R.id.lay_btn_add_rating:
                RateDoctorActivity.startActivity(this, booking);
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_lab_reports:
                openRecord(Constants.TYPE.RECORD_LAB);
                break;

            case R.id.lay_radiology:
                openRecord(Constants.TYPE.RECORD_RADIO);
                break;

            case R.id.lay_sick_leave:
                openRecord(Constants.TYPE.RECORD_SICK);
                break;

            case R.id.lay_doc_profile:
                PhysicianDetailActivity.startActivity(this, booking.getDocCode(), booking.getSpecialityCode(), booking.getId());
                break;
        }
    }

    // open particular reports screen
    void openRecord(String type) {
        SingleFragmentActivity.startActivity(this, type, String.valueOf(booking.getId()));
//        Intent i1 = new Intent(this, SingleFragmentActivity.class);
//        i1.putExtra(type, "dsfdsfsd");
//        i1.putExtra("e_id", String.valueOf(booking.getId()));
//        startActivity(i1);
    }

    @Override
    public void onGetReportsCount(VisitDetailReportResponse response) {

        if (response.getStatus().equalsIgnoreCase("true")) {
            if (response.getLabReportsCount() > 0)
                layLabReports.setVisibility(View.VISIBLE);

            if (response.getRadioLogyReportsCount() > 0)
                layRadiology.setVisibility(View.VISIBLE);

            if (response.getSickReportsCount() > 0)
                laySickLeave.setVisibility(View.VISIBLE);

            if (response.getPrescriptionCount() != null)
                if (response.getPrescriptionCount() > 0)
                    layPrescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {
        makeToast(msg);
    }

    @Override
    public void onNoInternet() {
        Common.noInternet(this);
    }
}
