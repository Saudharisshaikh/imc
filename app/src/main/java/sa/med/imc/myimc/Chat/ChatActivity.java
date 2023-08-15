package sa.med.imc.myimc.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.MessageAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
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

    MessageAdapter adapter;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity, String name) {
        Intent intent = new Intent(activity, ChatActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChatActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        toolbarTitle.setText("Keven Feil");
        setupRecycler();

    }

    @Override
    public void onBackPressed() {
        finish();
        ChatActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.iv_post_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_post_comment:
                break;
        }
    }

    void setupRecycler() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager1.setReverseLayout(true);
        recyclerChat.setLayoutManager(linearLayoutManager1);

        adapter = new MessageAdapter(this, new ArrayList<>());
        recyclerChat.setAdapter(adapter);
        recyclerChat.scrollToPosition(0);
    }
}
