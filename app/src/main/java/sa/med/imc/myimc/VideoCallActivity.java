package sa.med.imc.myimc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Base.BaseActivity;

import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoCallActivity extends BaseActivity {

    @BindView(R.id.lay_more)
    LinearLayout layMore;
    @BindView(R.id.lay_video)
    LinearLayout layVideo;
    @BindView(R.id.lay_camera)
    LinearLayout layCamera;
    @BindView(R.id.lay_call)
    LinearLayout layCall;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VideoCallActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        ButterKnife.bind(this);
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, VideoCallActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    public void onBackPressed() {
        finish();
        VideoCallActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.lay_more, R.id.lay_video, R.id.lay_camera, R.id.lay_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.lay_more:
                break;

            case R.id.lay_video:
                break;

            case R.id.lay_camera:
                break;

            case R.id.lay_call:
                onBackPressed();
                break;
        }
    }
}
