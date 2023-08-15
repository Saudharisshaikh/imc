package sa.med.imc.myimc.HealthTips.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YoutubePlayerActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView webView;

    private View mCustomView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;


    @BindView(R.id.videoWebView)
    WebView videoWebView;
    @BindView(R.id.customViewContainer)
    FrameLayout customViewContainer;

    public static void startActivity(Activity activity, String id, String des, String title) {
        Intent intent = new Intent(activity, YoutubePlayerActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("des", des);
        activity.startActivity(intent);
    }


    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(YoutubePlayerActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        YoutubePlayerActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        ButterKnife.bind(this);
        webView.setBackgroundColor(Color.TRANSPARENT);

        videoWebView.getSettings().setJavaScriptEnabled(true);
        videoWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        videoWebView.getSettings().getAllowContentAccess();
        videoWebView.getSettings().getMediaPlaybackRequiresUserGesture();
        videoWebView.clearHistory();
        videoWebView.clearCache(true);

        mWebViewClient = new myWebViewClient();
        videoWebView.setWebViewClient(mWebViewClient);

        mWebChromeClient = new myWebChromeClient();

        videoWebView.setWebChromeClient(mWebChromeClient);//new  MyChromeBrowser());

        String outhtml = getIntent().getStringExtra("des");
        String videoID = getIntent().getStringExtra("id");

        tv_title.setText(getIntent().getStringExtra("title"));
        toolbarTitle.setText(getIntent().getStringExtra("title"));

        if (SharedPreferencesUtils.getInstance(YoutubePlayerActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getData("rtl", outhtml);
            getDataVideo("rtl", "<iframe width='100%' height='220px' frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; allowfullscreen; picture-in-picture\" allowfullscreen src=\"https://www.youtube.com/embed/" + videoID + "\"></iframe>");
        } else {
            getData("ltr", outhtml);
            getDataVideo("ltr", "<iframe width='100%' height='220px' frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; allowfullscreen; picture-in-picture\" allowfullscreen src=\"https://www.youtube.com/embed/" + videoID + "\"></iframe>");
        }

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // load data with custom font
    void getData(String direction, String text) {
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/font/sans_plain.ttf");

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

        webView.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }

    // load data with custom font
    void getDataVideo(String direction, String text) {
        String text_load = "<html  dir=" + direction + "><body>" + text + "</body></html>";

        videoWebView.loadDataWithBaseURL(null, text_load, "text/html", "utf-8", null);

    }

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        videoWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        videoWebView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        if (inCustomView()) {
            hideCustomView();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (inCustomView()) {
                hideCustomView();
                return true;
            }

            if ((mCustomView == null)) {
                onBackPressed();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    class myWebChromeClient extends WebChromeClient {
        //        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            videoWebView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

//            if (mVideoProgressView == null) {
//                LayoutInflater inflater = LayoutInflater.from(YoutubePlayerActivity.this);
//                mVideoProgressView = inflater.inflate(R.layout.video_progress, null);
//            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();    //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            videoWebView.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
