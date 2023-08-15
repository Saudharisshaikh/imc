package sa.med.imc.myimc.Chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.MessageAdapter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatFragment extends Fragment {

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

    public ChatFragment() {
        // Required empty public constructorInboxFragment
    }


    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC))
            loadWeb(WebUrl.MAYO_CLINIC_AR_URL);
        else
            loadWeb(WebUrl.MAYO_CLINIC_URL);

        return view;
    }

    void loadWeb(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl(url);

        this.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+" + url.replace("tel:", ""))));
                else
                    view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                pdLoad.setVisibility(View.GONE);
            }
        });
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
}
