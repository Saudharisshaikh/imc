package sa.med.imc.myimc.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutAppActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_app_dat)
    TextView tvAppDat;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.pd_load)
    ProgressBar pdLoad;


    String lang = Constants.ENGLISH;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AboutAppActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(AboutAppActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        AboutAppActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);

        tvContent.setBackgroundColor(Color.TRANSPARENT);
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getData("rtl", getString(R.string.about_app_content));
        } else {
            getData("ltr", getString(R.string.about_app_content));

        }
        tvAppDat.setText(Html.fromHtml(getString(R.string.about_app_content)));


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView iv_lo=findViewById(R.id.iv_lo);
        lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        ImageView ic_logo_fc=findViewById(R.id.ic_logo_fc);
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0) == 4) {
            if (lang.equalsIgnoreCase(Constants.ARABIC)) {
                iv_lo.setVisibility(View.GONE);
                ic_logo_fc.setImageResource(R.drawable.logo_2);
            } else  {
                iv_lo.setVisibility(View.GONE);
                ic_logo_fc.setImageResource(R.drawable.logo_1);
            }
        } else {
            ic_logo_fc.setImageResource(R.drawable.logo_text);
        }

//        if (SharedPreferencesUtils.getInstance(getActivityContext()).getValue(Constants.SELECTED_HOSPITAL, 1) == 4){
//            ic_logo_fc.setImageDrawable(getResources().getDrawable(R.drawable.ic_first_clinic_blue_logo));
//        }else {
//            ic_logo_fc.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo_text_vec));
//
//        }

    }

    @Override
    public void onBackPressed() {
        finish();
        AboutAppActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        pdLoad.setVisibility(View.GONE);

    }
}
