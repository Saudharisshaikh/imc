package sa.med.imc.myimc.globle.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;

public class PaymentWebViewActivity extends AppCompatActivity {
    WebView webview;
    RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration;
    String ref, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_payment_web_view);

        ref = getIntent().getStringExtra("ref");
        url = getIntent().getStringExtra("url");
        requestBodyForPaymentUrlGeneration = new Gson().fromJson(getIntent().getStringExtra("requestBodyForPaymentUrlGeneration"),
                new TypeToken<RequestBodyForPaymentUrlGeneration>() {
                }.getType());

        webview = findViewById(R.id.web);

        //web settings

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        //loads webpages in webview instead of launching browser
        webview.setWebChromeClient(new ChromeClient());

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url1, Bitmap favicon) {
                super.onPageStarted(view, url1, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url1) {
                Log.e("abcd", url1);

                if (url1.contains("return")) {
                    Log.e("abcd", "ok");
                    new MyhttpMethod(PaymentWebViewActivity.this).verifyPayment(PaymentWebViewActivity.this,
                            ref, requestBodyForPaymentUrlGeneration, SharedPreferencesUtils.getInstance(PaymentWebViewActivity.this).getValue(Constants.SELECTED_HOSPITAL,0));
                } else if (url1.contains("payment/declined")) {
                    dailog("Payment Declined");
                } else if (url1.contains("payment/cancelled")) {
                    dailog("Payment Cancelled");
                } else if (url1.contains("doctor/declined")) {
                    dailog("Payment Declined");
                } else if (url1.contains("doctor/cancelled")) {
                    dailog("Payment Cancelled");
                }
//                else if (url1.contentEquals("pp2_acs_return")){
//
//                }
                super.onPageFinished(view, url1);
            }
        });

        //loading webpage
        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }



    private void dailog(String msg) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(PaymentWebViewActivity.this).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentWebViewActivity.this);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(msg);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}