package sa.med.imc.myimc.UrlOpen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;

import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.multidex.MultiDex;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
Get contents of Privacy policy,
About us
Terms and Conditions
 */

public class UrlOpenActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_more)
    ImageView iv_more;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
//    @BindView(R.id.ic_logo_fc)
//    TextView ic_logo_fc;
    @BindView(R.id.pd_load)
    ProgressBar pdLoad;
    @BindView(R.id.web_view)
    WebView webView;

    String title, link;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.main_about_us)
    RelativeLayout mainAboutUs;
    @BindView(R.id.layPdf)
    RelativeLayout layPdf;

    private Context primaryBaseActivity;//THIS WILL KEEP ORIGINAL INSTANCE
    String lang = Constants.ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(UrlOpenActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        UrlOpenActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_open);
        ButterKnife.bind(this);
        ivRecord.setVisibility(View.GONE);
        tvContent.setBackgroundColor(Color.TRANSPARENT);
        ImageView ic_logo_fc=findViewById(R.id.ic_logo_fc);
        ImageView iv_lo = findViewById(R.id.iv_lo);
        lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

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

        title = getIntent().getStringExtra(Constants.IntentKey.INTENT_TYPE);

        if (title.equalsIgnoreCase(Constants.IntentKey.INTENT_PRIVACY)) {
            toolbarTitle.setText(getString(R.string.privacy_policy));
            callGetContent("2");
        } else if (title.equalsIgnoreCase(Constants.IntentKey.INTENT_TERMS)) {
            toolbarTitle.setText(getString(R.string.terms_condition));
            callGetContent("3");
        } else if (title.equalsIgnoreCase(Constants.IntentKey.INTENT_ABOUT_US)) {
            toolbarTitle.setText(getString(R.string.about_us));
            callGetContent("1");
        } else if (title.equalsIgnoreCase(Constants.IntentKey.INTENT_FILE)) {
            link = getIntent().getStringExtra(Constants.IntentKey.INTENT_LINK);

            layPdf.setVisibility(View.VISIBLE);
            pdfView.fromUri(Uri.parse(link))
                    .spacing(10)
                    .load();

//            pdfView.fromUri(Uri.parse("http://www.africau.edu/images/default/sample.pdf"));

            ivRecord.setVisibility(View.GONE);
            iv_more.setVisibility(View.VISIBLE);
            toolbarTitle.setText(getIntent().getStringExtra(Constants.IntentKey.INTENT_REPORT_TYPE));
        }

        iv_more.setOnClickListener(v -> showPopUpMenu());



    }

    @Override
    protected void attachBaseContext(Context base) {
        primaryBaseActivity = base;//SAVE ORIGINAL INSTANCE
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    // open file with print preview to print job
    public void open_File() {
//        PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
//        String jobName = UrlOpenActivity.this.getString(R.string.app_name) + " Document";
//        printManager.print(jobName, pda, null);

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfOpenintent.setData(Uri.parse(link));
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Print Document Adapter
    PrintDocumentAdapter pda = new PrintDocumentAdapter() {

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            InputStream input = null;
            OutputStream output = null;
            FileOutputStream fileOutputStream = null;

            try {

//                input = new FileInputStream(new File(link));
//                output = new FileOutputStream(destination.getFileDescriptor());

                InputStream  inputStream = getContentResolver().openInputStream(Uri.parse(link));

                ParcelFileDescriptor pfd = getContentResolver().
                        openFileDescriptor(Uri.parse(link), "w");
                fileOutputStream =
                        new FileOutputStream(pfd.getFileDescriptor());

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buf)) > 0) {
                    fileOutputStream.write(buf, 0, bytesRead);
                }

                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

            } catch (FileNotFoundException ee) {
                //Catch exception
                Log.d("IMCEXCEPTION", ee.getMessage());
            } catch (Exception e) {
                //Catch exception
                Log.d("IMCEXCEPTION", e.getMessage());
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
            callback.onLayoutFinished(pdi, true);
        }
    };

    // Show pop up of filter value list.
    private void showPopUpMenu() {
        View menuItemView = findViewById(R.id.iv_more);
        PopupMenu popup = new PopupMenu(this, menuItemView);

        MenuInflater inflate = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflate.inflate(R.menu.option_menu, popup.getMenu());
        popup.show();

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    // Get content API request
    public void callGetContent(String id) {
        pdLoad.setVisibility(View.VISIBLE);

        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
        Call<UrlContentResponse> xxx = webService.getContents(id);

        xxx.enqueue(new Callback<UrlContentResponse>() {
            @Override
            public void onResponse(Call<UrlContentResponse> call, Response<UrlContentResponse> response) {
                pdLoad.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    UrlContentResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus()) {
                            tvContent.setVisibility(View.VISIBLE);
                            if (id.equalsIgnoreCase("1")) {
                                mainAboutUs.setVisibility(View.VISIBLE);
                            }

                            if (SharedPreferencesUtils.getInstance(UrlOpenActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
                                String outhtml = response1.getData().getDescriptionAr();

                                if (id.equalsIgnoreCase("1")) {
                                    outhtml = "<b>" + response1.getData().getHeading1Ar() + "</b>\n"
                                            + response1.getData().getDescription1Ar() + "\n\n"
                                            + "<b>" + response1.getData().getHeading2Ar() + "</b>\n"
                                            + response1.getData().getDescription2Ar() + "\n\n"
                                            + "<b>" + response1.getData().getHeading3Ar() + "</b>\n"
                                            + response1.getData().getDescription3Ar() + "\n\n";
                                }

                                getData("rtl", outhtml);
//                                tvContent.loadData("<html dir=\"rtl\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");

                            } else {
                                String outhtml = response1.getData().getDescriptionEn();

                                if (id.equalsIgnoreCase("1")) {
                                    outhtml = "<b>" + response1.getData().getHeading1En() + "</b>\n"
                                            + response1.getData().getDescription1En() + "\n\n"
                                            + "<b>" + response1.getData().getHeading2En() + "</b>\n"
                                            + response1.getData().getDescription2En() + "\n\n"
                                            + "<b>" + response1.getData().getHeading3En() + "</b>\n"
                                            + response1.getData().getDescription3En() + "\n\n";
                                }

                                getData("ltr", outhtml);
//                                tvContent.loadData("<html dir=\"ltr\"><body style=\"text-align:justify;\">" + outhtml + "</body></html>", "text/html", "UTF-8");
                            }
                        } else {
                            makeToast(response1.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UrlContentResponse> call, Throwable t) {
                Log.e("ok error", "" + t.getMessage() + "\n " + t.getLocalizedMessage());
                pdLoad.setVisibility(View.GONE);
                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    makeToast(getString(R.string.time_out_messgae));
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    Common.noInternet(UrlOpenActivity.this);
                } else
                    makeToast("Server not responding");
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.print:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    open_File();
                } else {
                    makeToast(getString(R.string.no_service_print));
                }
                break;

            case R.id.download:
                openFile(link);
                break;
        }
        return true;
    }

    // Show download success and ask to open file
    void openFile(String filepath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.success));
        if (getIntent().getStringExtra(Constants.IntentKey.INTENT_REPORT_TYPE).equalsIgnoreCase(getString(R.string.prescription_)))
            builder.setMessage(getResources().getString(R.string.download_prescription));
        else
            builder.setMessage(getResources().getString(R.string.download_dialog));

        builder.setPositiveButton(getResources().getString(R.string.open), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                File file = new File(filepath);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                Uri apkURI = FileProvider.getUriForFile(
//                        UrlOpenActivity.this,
//                        getApplicationContext().getPackageName() + ".fileprovider", file);
                pdfOpenintent.setDataAndType(Uri.parse(filepath), "application/pdf");
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
                    Typeface typeface = ResourcesCompat.getFont(UrlOpenActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(UrlOpenActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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
