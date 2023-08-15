package sa.med.imc.myimc.Physicians.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import de.hdodenhof.circleimageview.CircleImageView;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.LocationListAdapter;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DoctorFullDeatilsModel;
import sa.med.imc.myimc.Physicians.model.DoctorModel;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.FullDoctorDetailListner;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.PhysicianListner;

public class PhysicianFullDetailActivity extends AppCompatActivity {

    TextView doctor_description, doctor_speciality_description, license_number;

    TextView Languages, Specialities, Education, special_instructions;
    CardView license_layout;
    String code;
    ImageView iv_back;

    LinearLayout lay_btn_book;
    CircleImageView iv_physician_pic;
    PhysicianResponse.ProviderList providerList;
    String specialityCode = "",specialityDescription="";
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_full_detail);
        code = getIntent().getStringExtra("code");
        specialityCode = getIntent().getStringExtra("specialityCode");
        specialityDescription = getIntent().getStringExtra("specialityDescription");
        providerList=new Gson().fromJson(getIntent().getStringExtra("data"), new TypeToken<PhysicianResponse.ProviderList>() {}.getType());
        lang = SharedPreferencesUtils.getInstance(PhysicianFullDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        initView();
    }

    private void initView() {
        doctor_description = findViewById(R.id.doctor_description);
        doctor_speciality_description = findViewById(R.id.doctor_speciality_description);
        license_number = findViewById(R.id.license_number);
        license_layout = findViewById(R.id.license_layout);
        iv_back = findViewById(R.id.iv_back);
        Languages = findViewById(R.id.Languages);
        Specialities = findViewById(R.id.Specialities);
        Education = findViewById(R.id.Education);
        special_instructions = findViewById(R.id.special_instructions);
        iv_physician_pic = findViewById(R.id.iv_physician_pic);
        lay_btn_book = findViewById(R.id.lay_btn_book);


        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            doctor_description.setText(getText(R.string.dr)+" "+providerList.getProviderDescription());

        }else {
            doctor_description.setText(getText(R.string.dr)+" "+providerList.getArabicProviderDescription());

        }
        doctor_speciality_description.setText(specialityDescription);

        lay_btn_book.setOnClickListener(view -> {
            SharedPreferencesUtils.getInstance(PhysicianFullDetailActivity.this).setValue(Constants.KEY_CLINIC_NAME,
                    specialityDescription);

            AppointmentActivity.start(PhysicianFullDetailActivity.this,false, providerList, specialityCode, specialityDescription,specialityDescription);

        });

        iv_back.setOnClickListener(view -> {
            finish();
        });

        loadData();


    }

    private void loadData() {
        int hospitalCode = SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0);

        new MyhttpMethod(this).getFullPhysician(String.valueOf(hospitalCode), code,true, new FullDoctorDetailListner() {
            @Override
            public void onSuccess(DoctorFullDeatilsModel doctorFullDeatilsModel) {
                Log.e("abcd", new Gson().toJson(doctorFullDeatilsModel));
                try {

                    try {
                        if (doctorFullDeatilsModel.getPhysicianData().getGender().trim().isEmpty()){
                            Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(PhysicianFullDetailActivity.this, code))
                                    .error(R.drawable.mdoc_placeholder)
                                    .placeholder(R.drawable.male_img).fit().into(iv_physician_pic);
                        }
                        else if (!doctorFullDeatilsModel.getPhysicianData().getGender().trim().equalsIgnoreCase("M")){
                            Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(PhysicianFullDetailActivity.this, code))
                                    .error(R.drawable.fdoc_placeholder)
                                    .placeholder(R.drawable.male_img).fit().into(iv_physician_pic);

                        }else {
                            Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(PhysicianFullDetailActivity.this, code))
                                    .error(R.drawable.mdoc_placeholder)
                                    .placeholder(R.drawable.male_img).fit().into(iv_physician_pic);
                        }
                    } catch (Exception e) {
                        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(PhysicianFullDetailActivity.this, code))
                                .error(R.drawable.mdoc_placeholder)
                                .placeholder(R.drawable.male_img).fit().into(iv_physician_pic);
                    }


                    if (lang.equalsIgnoreCase(Constants.ENGLISH)) {

                        doctor_description.setText(getText(R.string.dr)+" "+doctorFullDeatilsModel.getPhysicianData().getGivenName().trim());
                        doctor_speciality_description.setText(doctorFullDeatilsModel.getPhysicianData().getClinicNameEn().trim());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Languages.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getLanguagesTxt().trim(), Html.FROM_HTML_MODE_COMPACT));
                            Specialities.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxt().trim(), Html.FROM_HTML_MODE_COMPACT));
                            doctor_speciality_description.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxt().trim(), Html.FROM_HTML_MODE_COMPACT));
                            Education.setText(Html.fromHtml(
                                    doctorFullDeatilsModel.getPhysicianData().getEducationTxt()
                                            .replace("\u0080\u0099", "'")
                                            .replace("â", "")));
//                    Education.loadDataWithBaseURL(null, doctorFullDeatilsModel.getPhysicianData().getEducationTxt().replace("\u0080\u0099"," "), "text/html", "utf-8", null);

                        } else {
                            Languages.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getLanguagesTxt().trim()));
                            Specialities.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxt().trim()));
                            doctor_speciality_description.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxt().trim()));
                            Education.setText(Html.fromHtml(
                                    doctorFullDeatilsModel.getPhysicianData().getEducationTxt()
                                            .replace("\u0080\u0099", "'")
                                            .replace("â", "")));

                        }


                        try {
                            String sp=doctorFullDeatilsModel.getPhysicianData().getspecInstructEn();
                            if (sp==null){
                                special_instructions.setVisibility(View.GONE);
                            }else {
                                special_instructions.setText(sp.trim());
                                special_instructions.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            special_instructions.setVisibility(View.GONE);
                        }

                    } else {

                        doctor_description.setText(getText(R.string.dr)+" "+doctorFullDeatilsModel.getPhysicianData().getGivenNameAr().trim()+" "+doctorFullDeatilsModel.getPhysicianData().getFamilyNameAr().trim());


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Languages.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getLanguagesTxtAr().trim(), Html.FROM_HTML_MODE_COMPACT));
                            Specialities.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxtAr().trim(), Html.FROM_HTML_MODE_COMPACT));
                            doctor_speciality_description.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxtAr().trim(), Html.FROM_HTML_MODE_COMPACT));
                            Education.setText(Html.fromHtml(
                                    doctorFullDeatilsModel.getPhysicianData().getEducationTxtAr()
                                            .replace("\u0080\u0099", "'")
                                            .replace("â", "")));
//                    Education.loadDataWithBaseURL(null, doctorFullDeatilsModel.getPhysicianData().getEducationTxtAr().replace("\u0080\u0099","'"), "text/html", "utf-8", null);

                        } else {
                            Languages.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getLanguagesTxtAr().trim()));
                            Specialities.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxtAr().trim()));
                            doctor_speciality_description.setText(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxtAr().trim()));
                            Education.setText(Html.fromHtml(
                                    doctorFullDeatilsModel.getPhysicianData().getEducationTxtAr()
                                            .replace("\u0080\u0099", "'")
                                            .replace("â", "")));

                        }
                        try {
                            String spAr=doctorFullDeatilsModel.getPhysicianData().getSpecInstructAr();
                            if (spAr==null){
                                special_instructions.setVisibility(View.GONE);
                            }else {
                                special_instructions.setText(spAr.trim());
                                special_instructions.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            special_instructions.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Common.messageDailog(PhysicianFullDetailActivity.this,getString(R.string.profile_data_not_avalible));
                }
            }

            @Override
            public void onFailed() {
//                Common.messageDailog(PhysicianFullDetailActivity.this,getString(R.string.profile_data_not_avalible));

            }
        });

//
//        new MyhttpMethod(this).getPhysician(code, new PhysicianListner() {
//            @Override
//            public void onSuccess(DoctorModel doctorModel) {
//
//                if (doctorModel.getLicense_list().size()>0) {
//                    license_layout.setVisibility(View.VISIBLE);
//                    license_number.setText(doctorModel.getLicense_list().get(0).getLicense_number());
//                }
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });
    }
}