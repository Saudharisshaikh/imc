package sa.med.imc.myimc.RateDoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateDoctorActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_physician_pic)
    CircleImageView ivPhysicianPic;
    @BindView(R.id.tv_physician_name)
    TextView tvPhysicianName;
    @BindView(R.id.tv_physician_speciality)
    TextView tvPhysicianSpeciality;
    @BindView(R.id.et_review)
    EditText etReview;
    @BindView(R.id.lay_btn_submit)
    LinearLayout layBtnSubmit;
    BookingAppoinment physician;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(RateDoctorActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        RateDoctorActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_doctor);
        ButterKnife.bind(this);

        physician = (BookingAppoinment) getIntent().getSerializableExtra("data");
        String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        if (physician != null) {
            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                if (physician.getCareProviderDescription() != null)
                    if (physician.getSpecialityDescription() != null)
                        tvPhysicianName.setText(Html.fromHtml(physician.getCareProviderDescription() + " " + physician.getSpecialityDescription()));
                    else
                        tvPhysicianName.setText(Html.fromHtml(physician.getCareProviderDescription()));

//                if (physician.getService() != null)
//                    if (physician.getService().getDescAr() != null)
//                        tvPhysicianSpeciality.setText(physician.getService().getDescAr());


            } else {
                if (physician.getCareProviderDescription() != null)
                    if (physician.getSpecialityDescription() != null)
                        tvPhysicianName.setText(Html.fromHtml(physician.getCareProviderDescription() + " " + physician.getSpecialityDescription()));
                    else
                        tvPhysicianName.setText(Html.fromHtml(physician.getCareProviderDescription()));


//                if (physician.getService() != null)
//                    if (physician.getService().getDesc() != null)
//                        tvPhysicianSpeciality.setText(physician.getService().getDesc());


            }

//            if (physician.getDocImg() != null)
//                if (physician.getDocImg().length() != 0) {
//                    String url = BuildConfig.IMAGE_UPLOAD_DOC_URL + physician.getDocImg();// + ".xhtml?In=default";
////                    Log.e("path  url  ", "" + url);
//                    Picasso.get().load(url).error(R.drawable.male_img).placeholder(R.drawable.male_img).into(ivPhysicianPic);
//                }

        }
    }

    public static void startActivity(Activity activity, BookingAppoinment physician) {
        Intent intent = new Intent(activity, RateDoctorActivity.class);
        intent.putExtra("data", physician);
        activity.startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.lay_btn_submit:
                if (physician != null)
                    callRateDoctor(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""),
                            physician.getDocCode(), physician.getSpecialityCode(), String.valueOf(ratingBar.getRating()),
                            SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        RateDoctorActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void callRateDoctor(String token, String mrNumber, String docCode, String clinicCode, String rating, int hosp) {
        Common.showDialog(this);
        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("docCode", docCode);
            object.put("clinicCode", clinicCode);
            object.put("rating", rating);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.rateDoctor(body);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            Common.makeToast(RateDoctorActivity.this, response1.getMessage());
                            onBackPressed();

                        } else {
                            Common.makeToast(RateDoctorActivity.this, response1.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Common.hideDialog();
                Common.makeToast(RateDoctorActivity.this, t.getMessage());

            }
        });

    }
}
