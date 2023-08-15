package sa.med.imc.myimc.webView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.databinding.FragmentWebViewBinding;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadPresenter;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadPresenterImpl;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadView;
import sa.med.imc.myimc.webView.model.WebViewRequest;


public class WebViewFragment extends BaseFragment implements WebViewViews , PrescriptionInvoiceDownloadView {
    private FragmentWebViewBinding binding;
    private WebViewPresenter webViewPresenter;
    private WebViewRequest request;
    private final PrescriptionInvoiceDownloadPresenter invoiceDownloadPresenter = new PrescriptionInvoiceDownloadPresenterImpl(this);
    public static String LINK = "link";
    public static String TITLE = "title";
    public static String REF = "ref";


    public WebViewFragment() {

    }


    public WebViewFragment newInstance(String link, String title) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(LINK, link);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewFragment newInstance(String link, String title,String ref) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(LINK, link);
        args.putString(TITLE, title);
        args.putString(REF, ref);
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewFragment newInstance(WebViewRequest webViewRequest) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(LINK, webViewRequest.getWebLink());
        args.putString(TITLE, webViewRequest.getTitle());
        args.putString(REF, webViewRequest.getPaymentRef());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewPresenter= new WebViewPresenterImpl(requireActivity(),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWebViewBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // getCallBack().hideNavigation();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getArguments()!=null){
            if (getArguments().containsKey(TITLE)){
                binding.toolbar.setTitle(getArguments().getString(TITLE));
            }
            if (request.getWebLink()!=null){
                invoiceDownloadPresenter.setSelectedOrderId(request.getBundle().getInt(Constants.Bundle.orderId));
                loadWebView(request.getWebLink());
            }
        }
        binding.toolbar.setNavigationOnClickListener(v->{
            onBackPressedAction().run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  getCallBack().showNavigation();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void loadWebView(String url) {
        WebView webView = binding.webView;
        webView.invalidate();
//        webView.clearCache(true);
//        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(50);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setLayerType(View.LAYER_TYPE_NONE, null);
        webView.getSettings().setDomStorageEnabled(true);
        if (url.contains("matterport")) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("prescription-payement-authorized")){
                    if (getArguments()!=null && getArguments().containsKey(REF)) {
                        webViewPresenter.validatePaymentRef(getArguments().getString(REF));
                    }
                    else{
                        getCallBack().openHome();
                    }

                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    @Override
    public void onValidatePaymentRef() {
        onSuccess();
    }

    private void onSuccess(){
        binding.webView.setVisibility(View.GONE);
        AlertDialog dialog = Common.customDialog(((AppCompatActivity) requireActivity()));
        dialog.setTitle(request.getTitle());
        dialog .setCancelable(false);
        dialog.setMessage(request.getMessage());
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,getString(R.string.ok), (dialog1, which) -> {
                     dialog1.dismiss();
                     getCallBack().openHome();
                 });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.txt_download_receipt), (dialog1, which) -> {
                     invoiceDownloadPresenter.createFile(this);
                 });
        dialog.show();

    }

    private void onDeclined(){
        onBackPressedAction().run();
    }

    @Override
    protected Runnable onBackPressedAction() {
        return ()->{
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        invoiceDownloadPresenter.onActivityResult(requireActivity(),request.getBundle(),requestCode,resultCode,data);
    }

    @Override
    public void onOpenFile() {
        getCallBack().openHome();
    }

    @Override
    public void onCancelFile() {
        getCallBack().openHome();
    }

    @Override
    public void showLoading(int i) {
        Common.showDialog(requireContext());
    }

    @Override
    public void hideLoading(int i) {
        Common.hideDialog();
    }
}