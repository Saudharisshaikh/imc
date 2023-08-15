package sa.med.imc.myimc.Wellness;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Adapter.MessageAdapter;
import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import timber.log.Timber;


public class WellnessFragment extends BaseFragment {

    @BindView(R.id.recycler_chat)
    RecyclerView recyclerChat;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.txt_no_msg)
    TextView txtNoMsg;
    @BindView(R.id.edComment)
    EditText edComment;
    @BindView(R.id.iv_post_comment)
    ImageView ivPostComment;
    @BindView(R.id.pd_load)
    ProgressBar pdLoad;
    @BindView(R.id.web_view)
    WebView webView;

    MessageAdapter adapter;



    public WellnessFragment() {
        // Required empty public constructorInboxFragment
    }


    public static WellnessFragment newInstance() {
        WellnessFragment fragment = new WellnessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wellness, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.main_toolbar).setVisibility(View.GONE);
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC))
            loadWeb(WebUrl.WellnessArabic);
        else
            loadWeb(WebUrl.WellnessEnglish);

        return view;
    }

    void loadWeb(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
     //   webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
       WebView.setWebContentsDebuggingEnabled(true);
       webView.getSettings().setUseWideViewPort(true);
      webView.getSettings().setAllowContentAccess(true);
      webView.getSettings().setDomStorageEnabled(true);
       webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.loadUrl(url);
        this.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (BuildConfig.DEBUG) {
                    Timber.tag(WellnessFragment.this.getClass().getSimpleName()).d(consoleMessage.toString());
                }
                return false;
            }




            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                if (view.getHitTestResult().getExtra().equalsIgnoreCase("https://patientportal.imc.med.sa/")){
                    view.loadUrl("https://patientportal.imc.med.sa/imcportal/#/auth/login");
                }
                //getCallBack().openWellness("WelnessFragment","https://patientportal.imc.med.sa/imcportal/#/auth/login");
              //  webView2.setVisibility(View.VISIBLE);
           //     webView2.setWebViewClient(getClient());
//                webView.clearView();
//                WebView.HitTestResult result = view.getHitTestResult();
//                String data = result.getExtra();
//                webView.loadUrl(data);
//                Context context = view.getContext();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
//                context.startActivity(browserIntent);
                return true;
            }
        });

        this.webView.setWebViewClient(getClient());
    }

    void setupRecycler() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager1.setReverseLayout(true);
        recyclerChat.setLayoutManager(linearLayoutManager1);

        adapter = new MessageAdapter(getActivity(), new ArrayList<>());
        recyclerChat.setAdapter(adapter);
        recyclerChat.scrollToPosition(0);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

  private   WebViewClient getClient(){
        return new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+" + url.replace("tel:", ""))));
                else
                    view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                 pdLoad.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                         webView.setVisibility(View.VISIBLE);
                        pdLoad.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                return super.onRenderProcessGone(view, detail);
            }

            @Override
            public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
                super.onSafeBrowsingHit(view, request, threatType, callback);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        };
    }

    @Override
    protected Runnable onBackPressedAction() {
        return null;
    }
}
