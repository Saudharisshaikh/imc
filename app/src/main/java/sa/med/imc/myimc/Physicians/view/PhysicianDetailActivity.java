package sa.med.imc.myimc.Physicians.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.PhysicianImpl;
import sa.med.imc.myimc.Physicians.presenter.PhysicianPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Utils.Common;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Physician detail.
 * Available dates and time slots added by that Physician.
 * User can get appointment by clicking book.
 */

public class PhysicianDetailActivity extends BaseActivity implements PhysicianViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_physician_distance)
    TextView tvPhysicianDistance;
    @BindView(R.id.lay_b)
    LinearLayout layB;
    @BindView(R.id.lay_item)
    RelativeLayout layItem;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_physician_pic)
    CircleImageView ivPhysicianPic;
    @BindView(R.id.tv_physician_name)
    TextView tvPhysicianName;
    @BindView(R.id.tv_physician_speciality)
    TextView tvPhysicianSpeciality;
    @BindView(R.id.tv_physician_address)
    TextView tvPhysicianAddress;
    @BindView(R.id.cardDetail)
    CardView cardDetail;
    @BindView(R.id.cardDates)
    CardView cardDates;
    @BindView(R.id.lay_btn_book)
    LinearLayout layBtnBook;
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

    PhysicianPresenter physicianPresenter;
    //    PhysicianDetailResponse.PhysicianData physician;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_specialities)
    TextView tvSpecialities;
    @BindView(R.id.tv_affilations)
    TextView tvAffilations;
    @BindView(R.id.tv_research)
    TextView tvResearch;
    @BindView(R.id.tv_languages)
    TextView tvLanguages;
    @BindView(R.id.tv_clinic_service)
    TextView tv_clinic_service;
    String lang;
    @BindView(R.id.rating)
    RatingBar rating;

    String physicianD, hrId;
    PhysicianDetailResponse.PhysicianData physician;

    @Override
    protected Context getActivityContext() {
        return null;
    }

    public static void startActivity(Activity activity, String id, String clinic_id, String hrId) {
        Intent intent = new Intent(activity, PhysicianDetailActivity.class);
        intent.putExtra("id", id);

        if (hrId.length() > 0)
            intent.putExtra("hrId", hrId);

        intent.putExtra("depart_id", clinic_id);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String id, String clinic_id, PhysicianResponse.Physician physician) {
        Intent intent = new Intent(activity, PhysicianDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("physician", physician);
        intent.putExtra("depart_id", clinic_id);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String id, String clinic_id) {
        Intent intent = new Intent(activity, PhysicianDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("depart_id", clinic_id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(PhysicianDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        PhysicianDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_detail);
        ButterKnife.bind(this);
        physicianD = getIntent().getStringExtra("id");

        if (getIntent().hasExtra("hrId"))
            hrId = getIntent().getStringExtra("hrId");
        else if (getIntent().hasExtra("physician")) {
            PhysicianResponse.Physician physician = (PhysicianResponse.Physician) getIntent().getSerializableExtra("physician");
            hrId = physician.getHrId();
        }
        lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        physicianPresenter = new PhysicianImpl(this, this);
        if (getIntent().getStringExtra("depart_id").length() > 0) {
//            if (hrId == null) {
            physicianPresenter.callGetDoctorsInfo(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    physicianD, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                    getIntent().getStringExtra("depart_id"),
                    SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
//            } else {
//                long id = Long.parseLong(hrId);
//                physicianPresenter.callGetCMSDoctorProfileData(String.valueOf(id));
//            }
        } else
            physicianPresenter.callGetCMSDoctorProfileData(physicianD);
    }


    @Override
    public void onBackPressed() {
        finish();
        PhysicianDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (layBtnBook != null)
            layBtnBook.setEnabled(true);

    }

    @OnClick({R.id.iv_back, R.id.lay_btn_book, R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_book:
                layBtnBook.setEnabled(false);
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0 &&
                        SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "").length() == 0) {
                    ContactOptionsActivity.startActivity(this);
                } else {
                    if (physician != null)
                        if (physician.getIsActive() != null && physician.getIsActive().equalsIgnoreCase("0")) {
                            doctorNotAvailableDialog(this);
                        } else {
                            if (physician.getService() != null)
                                AppointmentActivity.start(this, physician);
                            else
                                setNoServicesDialog(this);
                        }
                    else
                        ContactOptionsActivity.startActivity(this);
                }
                break;

            case R.id.bt_try_again:
                if (getIntent().getStringExtra("depart_id").length() > 0) {
                    physicianPresenter.callGetDoctorsInfo(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            physicianD, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                            getIntent().getStringExtra("depart_id"),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else
                    physicianPresenter.callGetCMSDoctorProfileData(physicianD);

                break;

            case R.id.bt_try_again_time_out:
                if (getIntent().getStringExtra("depart_id").length() > 0) {
                    physicianPresenter.callGetDoctorsInfo(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            physicianD, SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                            getIntent().getStringExtra("depart_id"),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else
                    physicianPresenter.callGetCMSDoctorProfileData(physicianD);

                break;
        }
    }


    void doctorNotAvailableDialog(Activity activity) {
        String phone = "";
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert))
                .setMessage(activity.getString(R.string.doctor_not_available, phone))
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
                    if (SharedPreferencesUtils.getInstance(PhysicianDetailActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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
    public void onGetPhysicianList(PhysicianResponse response) {

    }

    @Override
    public void onGetServiceList(DrServiceResponse response) {

    }

    @Override
    public void onGetCMSPhysician(PhysicianCompleteDetailCMSResponse response) {
        PhysicianCompleteDetailCMSResponse.Datum physician = response.getData().get(0);
        if (response == null)
            return;

        if (hrId != null) {
            if (lang.equalsIgnoreCase(Constants.ARABIC)) {

                if (physician.getSpecialitiesTxt() != null) {
                    Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxtAr());
                    String trimmed = spannableString.toString().trim();
                    tvSpecialities.setText(trimmed);
                }

            } else {
                if (physician.getSpecialitiesTxt() != null) {
                    Spanned spannableString = Html.fromHtml(physician.getSpecialitiesTxt());
                    String trimmed = spannableString.toString().trim();
                    tvSpecialities.setText(trimmed);
                }
            }
        } else {
            if (physician != null) {
                if (lang.equalsIgnoreCase(Constants.ARABIC)) {

                    if (physician.getGivenNameAr() != null)
                        if (physician.getFamilyNameAr() != null)
                            tvPhysicianName.setText(Html.fromHtml(physician.getTitleAr() + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr()));
                        else
                            tvPhysicianName.setText(Html.fromHtml(physician.getTitleAr() + " " + physician.getGivenNameAr()));

                    if (physician.getDesignationAr() != null)
                        tvPhysicianSpeciality.setText(physician.getDesignationAr());


                    if (physician.getTitleDescAr() != null)
                        tv_clinic_service.setText(Html.fromHtml(physician.getTitleDescAr()));

                    if (physician.getEducationTxtAr() != null)
                        tvEducation.setText(Html.fromHtml(physician.getEducationTxtAr()));

                    if (physician.getSpecialitiesTxtAr() != null)
                        tvSpecialities.setText(Html.fromHtml(physician.getSpecialitiesTxtAr()));


                    if (physician.getAffilationsTxtAr() != null)
                        tvAffilations.setText(Html.fromHtml(physician.getAffilationsTxtAr()));


                    if (physician.getResearchTxtAr() != null)
                        tvResearch.setText(Html.fromHtml(physician.getResearchTxtAr()));

//            if (physician.getMembershipAr() != null)
//                tvLanguages.setText(Html.fromHtml(physician.getMembershipAr()));

                } else {

                    if (physician.getGivenName() != null)
                        if (physician.getFamilyName() != null)
                            tvPhysicianName.setText(Html.fromHtml(physician.getTitle() + " " + physician.getGivenName() + " " + physician.getFamilyName()));
                        else
                            tvPhysicianName.setText(Html.fromHtml(physician.getTitle() + " " + physician.getGivenName()));

                    if (physician.getDesignation() != null)
                        tvPhysicianSpeciality.setText(physician.getDesignation());

                    if (physician.getTitleDesc() != null)
                        tv_clinic_service.setText(Html.fromHtml(physician.getTitleDesc()));

                    if (physician.getEducationTxt() != null)
                        tvEducation.setText(Html.fromHtml(physician.getEducationTxt()));

                    if (physician.getSpecialitiesTxt() != null)
                        tvSpecialities.setText(Html.fromHtml(physician.getSpecialitiesTxt()));


                    if (physician.getAffilationsTxt() != null)
                        tvAffilations.setText(Html.fromHtml(physician.getAffilationsTxt()));


                    if (physician.getResearchTxt() != null)
                        tvResearch.setText(Html.fromHtml(physician.getResearchTxt()));
                }

                if (physician.getImgUrl() != null)
                    if (physician.getImgUrl().length() != 0) {

                        Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                            }
                        }).build();

                        picasso.load(physician.getImgUrl()).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img)
                                .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivPhysicianPic);
                    }
            }

        }
//
    }

    @Override
    public void onGetNextAvailableTIme(NextTimeResponse response,int pos) {

    }

    @Override
    public void onGetPhysicianProfile(PhysicianDetailResponse response) {
        physician = response.getPhysicianData();
        if (physician == null)
            return;


        if (physician.getDocRating() != null) {
            rating.setRating(Float.parseFloat(physician.getDocRating()));
        }

        if (lang.equalsIgnoreCase(Constants.ARABIC)) {

            if (physician.getGivenNameAr() != null)
                if (physician.getFamilyNameAr() != null)
                    tvPhysicianName.setText(Html.fromHtml(getString(R.string.dr) + " " + physician.getGivenNameAr() + " " + physician.getFamilyNameAr()));
                else
                    tvPhysicianName.setText(Html.fromHtml(getString(R.string.dr) + " " + physician.getGivenNameAr()));

            if (physician.getService() != null)
                if (physician.getService().getDescAr() != null)
                    tvPhysicianSpeciality.setText(physician.getService().getDescAr());


            if (physician.getClincNameAr() != null) {
                if (physician.getDeptNameAr() != null)
                    tv_clinic_service.setText(physician.getClincNameAr() + " | " + physician.getDeptNameAr());
                else
                    tv_clinic_service.setText(physician.getClincNameAr());

            } else if (physician.getDeptNameAr() != null) {
                tv_clinic_service.setText(physician.getDeptNameAr());
            }


//            if (response.getPhysicianData().getEducationTxtAr() != null)
//                tvEducation.setText(Html.fromHtml(response.getPhysicianData().getEducationTxtAr()));

            if (response.getPhysicianData().getEducationTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getEducationTxtAr());
                String trimmed = spannableString.toString().trim();
                tvEducation.setText(trimmed);
            }

            if (response.getPhysicianData().getSpecialitiesTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getSpecialitiesTxtAr());
                String trimmed = spannableString.toString().trim();
                tvSpecialities.setText(trimmed);
            }

            if (response.getPhysicianData().getAffilationsTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getAffilationsTxtAr());
                String trimmed = spannableString.toString().trim();
                tvAffilations.setText(trimmed);
            }


            if (response.getPhysicianData().getResearchTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getResearchTxtAr());
                String trimmed = spannableString.toString().trim();
                tvResearch.setText(trimmed);
            }

            if (response.getPhysicianData().getLanguagesTxtAr() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getLanguagesTxtAr());
                String trimmed = spannableString.toString().trim();
                tvLanguages.setText(trimmed);
            }

        } else {

            if (physician.getGivenName() != null)
                if (physician.getFamilyName() != null)
                    tvPhysicianName.setText(Html.fromHtml(getString(R.string.dr) + " " + physician.getGivenName() + " " + physician.getFamilyName()));
                else
                    tvPhysicianName.setText(Html.fromHtml(getString(R.string.dr) + " " + physician.getGivenName()));


            if (physician.getService() != null)
                if (physician.getService().getDesc() != null)
                    tvPhysicianSpeciality.setText(physician.getService().getDesc());


            if (physician.getClinicNameEn() != null) {
                if (physician.getDeptNameEn() != null)
                    tv_clinic_service.setText(physician.getClinicNameEn() + " | " + physician.getDeptNameEn());
                else
                    tv_clinic_service.setText(physician.getClinicNameEn());

            } else if (physician.getDeptNameEn() != null) {
                tv_clinic_service.setText(physician.getDeptNameEn());
            }


            if (response.getPhysicianData().getEducationTxt() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getEducationTxt());
                String trimmed = spannableString.toString().trim();
                tvEducation.setText(trimmed);
            }

            if (response.getPhysicianData().getSpecialitiesTxt() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getSpecialitiesTxt());
                String trimmed = spannableString.toString().trim();
                tvSpecialities.setText(trimmed);
            }

            if (response.getPhysicianData().getAffilationsTxt() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getAffilationsTxt());
                String trimmed = spannableString.toString().trim();
                tvAffilations.setText(trimmed);
            }


            if (response.getPhysicianData().getResearchTxt() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getResearchTxt());
                String trimmed = spannableString.toString().trim();
                tvResearch.setText(trimmed);
            }

            if (response.getPhysicianData().getLanguagesTxt() != null) {
                Spanned spannableString = Html.fromHtml(response.getPhysicianData().getLanguagesTxt());
                String trimmed = spannableString.toString().trim();
                tvLanguages.setText(trimmed);
            }
        }

        if (physician.getDocImg() != null)
            if (physician.getDocImg().length() != 0 && SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.SELECTED_HOSPITAL,0)==1) {
                String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + physician.getDocImg();// + ".xhtml?In=default";
                Picasso.get().load(url).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img)
                        .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivPhysicianPic);
            }else if (physician.getDocImg().length() != 0 && SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.SELECTED_HOSPITAL,0)==4){
                String url = BuildConfig.IMAGE_UPLOAD_DOC_URL_FC + physician.getDocImg();// + ".xhtml?In=default";
                Picasso.get().load(url).error(R.drawable.mdoc_placeholder).placeholder(R.drawable.male_img)
                        .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivPhysicianPic);
            }

//        if (hrId != null) {
//            long id = Long.parseLong(hrId);
//            physicianPresenter.callGetCMSDoctorProfileData(String.valueOf(id));
//        }
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
        if (msg != null)
            if (msg.equalsIgnoreCase("timeout")) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
            } else {
                makeToast(msg);
            }
    }

    @Override
    public void onNoInternet() {
        mainContent.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoNet.setVisibility(View.VISIBLE);
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

                    if (SharedPreferencesUtils.getInstance(PhysicianDetailActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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
}
