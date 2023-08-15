package sa.med.imc.myimc.GuestHome;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoOrderActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_myseld)
    TextView tvMyseld;
    @BindView(R.id.tv_someone_else)
    TextView tvSomeoneElse;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.et_relationship)
    EditText etRelationship;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.lay_btn_done)
    LinearLayout layBtnDone;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, UserInfoOrderActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        UserInfoOrderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserInfoOrderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_order);
        ButterKnife.bind(this);
    }

    // order placed success dialog.
    private void orderPlaced() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.order_placed));
        builder.setMessage(getResources().getString(R.string.order_placed_content));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            }
        });
        alert.show();
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_done:
                orderPlaced();
                break;
        }
    }
}
