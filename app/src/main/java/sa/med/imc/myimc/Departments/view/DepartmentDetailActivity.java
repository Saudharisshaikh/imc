package sa.med.imc.myimc.Departments.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.PhysicianOutsideAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Departments.model.DepartmentDoctorResponse;
import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.Departments.presenter.DepartmentImpl;
import sa.med.imc.myimc.Departments.presenter.DepartmentsPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.PhysicianModel;
import sa.med.imc.myimc.Physicians.view.PhysicianDetailActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
Department detail data shown in this activity
 */

public class DepartmentDetailActivity extends BaseActivity implements DepartmentViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_department_name)
    TextView tvDepartmentName;
    @BindView(R.id.card_top)
    CardView cardTop;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.iv_depart)
    ImageView ivDepart;
    DepartmentsPresenter presenter;
    DepartmentResponse.Department department;
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
    @BindView(R.id.rv_doctors)
    RecyclerView rvDoctors;
    PhysicianOutsideAdapter adapter;
    List<PhysicianModel> list = new ArrayList<>();

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(DepartmentDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        DepartmentDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_department_detail);
        ButterKnife.bind(this);
        tvContent.setBackgroundColor(Color.TRANSPARENT);

        department = (DepartmentResponse.Department) getIntent().getSerializableExtra("data");
        presenter = new DepartmentImpl(this, this);
        presenter.callDepartmentDoctors(String.valueOf(department.getId()));

    }

    // Initialize Physician list view and add adapter to display data,
    void setUpPhysicianRecyclerView() {
        adapter = new PhysicianOutsideAdapter(this, list);
        rvDoctors.setHasFixedSize(true);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhysicianOutsideAdapter.OnItemClickListener() {
            @Override
            public void onViewProfile(int position) {
                PhysicianDetailActivity.startActivity(DepartmentDetailActivity.this, String.valueOf(list.get(position).getDocCode()), "","");

            }

            @Override
            public void onBookAppointment(int position) {
                ContactOptionsActivity.startActivity(DepartmentDetailActivity.this);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
//        DepartmentDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onSuccess(DepartmentResponse response) {

    }

    @Override
    public void onDoctorSuccess(DepartmentDoctorResponse response) {
        if (SharedPreferencesUtils.getInstance(DepartmentDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            tvDepartmentName.setText(Html.fromHtml(department.getTitleAr()));
            String outhtml = department.getDescriptionAr();
            getData("rtl", outhtml);
//            tvContent.loadData("<html dir=\"rtl\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");

        } else {
            tvDepartmentName.setText(Html.fromHtml(department.getTitleEn()));
            String outhtml = department.getDescriptionEn();
            getData("ltr", outhtml);
//            tvContent.loadData("<html dir=\"ltr\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");

        }
        Picasso.get().load(department.getImage()).resize(Common.getScreenWidth(this), Common.getScreenWidth(this) - 50).centerCrop().into(ivDepart);

        if (response.getData() != null) {
            list = new ArrayList<>();
            list.addAll(response.getData());
            setUpPhysicianRecyclerView();
        }
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

        if (SharedPreferencesUtils.getInstance(DepartmentDetailActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            tvDepartmentName.setText(Html.fromHtml(department.getTitleAr()));
            String outhtml = department.getDescriptionAr();
            getData("rtl", outhtml);
//            tvContent.loadData("<html dir=\"rtl\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");

        } else {
            tvDepartmentName.setText(Html.fromHtml(department.getTitleEn()));
            String outhtml = department.getDescriptionEn();
            getData("ltr", outhtml);
//            tvContent.loadData("<html dir=\"ltr\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");

        }
        Picasso.get().load(department.getImage()).resize(Common.getScreenWidth(this), Common.getScreenWidth(this) - 50).centerCrop().into(ivDepart);

        if (msg.equalsIgnoreCase("timeout")) {
            mainContent.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.VISIBLE);
        }
//        else {
//            makeToast(msg);
//        }
    }

    @Override
    public void onNoInternet() {
        mainContent.setVisibility(View.GONE);
        mainNoNet.setVisibility(View.VISIBLE);
        mainTimeOut.setVisibility(View.GONE);

    }

    @OnClick({R.id.bt_try_again, R.id.bt_try_again_time_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_try_again:
                presenter.callDepartmentDoctors(String.valueOf(department.getId()));
                break;

            case R.id.bt_try_again_time_out:
                presenter.callDepartmentDoctors(String.valueOf(department.getId()));
                break;
        }
    }

    // load data with custom font
    void getData(String direction, String text) {
        String text_load = "<html  dir=" + direction + "><head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_res/font/sans_plain.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: medium;\n" +
                "    text-align: justify;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>" + text + "</body></html>";

        tvContent.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }
}
