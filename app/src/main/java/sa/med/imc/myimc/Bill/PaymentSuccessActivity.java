package sa.med.imc.myimc.Bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentSuccessActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.lay_full)
    LinearLayout layFull;

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, PaymentSuccessActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PaymentSuccessActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        ButterKnife.bind(this);

        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.success_anim);

        layFull.startAnimation(animSlide);
    }

    @Override
    public void onBackPressed() {
        finish();
        PaymentSuccessActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
