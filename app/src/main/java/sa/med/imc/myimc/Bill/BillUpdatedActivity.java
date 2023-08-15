package sa.med.imc.myimc.Bill;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class BillUpdatedActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_co_payment_value)
    TextView tvCoPaymentValue;
    @BindView(R.id.tv_nin_reimbursable_value)
    TextView tvNinReimbursableValue;
    @BindView(R.id.tv_total_value)
    TextView tvTotalValue;
    @BindView(R.id.tv_insurance)
    TextView tvInsurance;
    @BindView(R.id.tv_pay_now)
    TextView tvPayNow;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, BillUpdatedActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BillUpdatedActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_updated);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_insurance, R.id.tv_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_insurance:
                showUploadMedicalCard();
                break;

            case R.id.tv_pay_now:
                showSelectCard();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        BillUpdatedActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    void showSelectCard() {

        final Dialog sucDialog = new Dialog(this); // Context, this, etc.
        sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sucDialog.setContentView(R.layout.dialog_select_payment_method);

        TextView bt_ok = sucDialog.findViewById(R.id.bt_ok);
        ImageView iv_cancel = sucDialog.findViewById(R.id.iv_cancel);

        RadioButton radio_cash = sucDialog.findViewById(R.id.radio_cash);
        RadioButton radio_credit = sucDialog.findViewById(R.id.radio_credit);
        RadioButton radio_medical_card = sucDialog.findViewById(R.id.radio_medical_card);

        sucDialog.setCancelable(false);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucDialog.dismiss();
                if (radio_cash.isChecked())
                    showCheckin(getString(R.string.visit_reception), "2");
                else if (radio_medical_card.isChecked())
                    showUploadMedicalCard();
                else if (radio_credit.isChecked()) {
                    SelectCardActivity.startActivity(BillUpdatedActivity.this);
                }

            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sucDialog.cancel();
            }
        });
        sucDialog.show();
    }


    void showUploadMedicalCard() {

        final Dialog sucDialog = new Dialog(this); // Context, this, etc.
        sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sucDialog.setContentView(R.layout.dialog_upload_medical_card);

        TextView bt_goto_ocr = sucDialog.findViewById(R.id.bt_goto_ocr);
        ImageView iv_cancel = sucDialog.findViewById(R.id.iv_cancel);

        sucDialog.setCancelable(false);

        bt_goto_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucDialog.dismiss();
                paymentStatus(getString(R.string.payment_successful), "s");
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sucDialog.cancel();
            }
        });
        sucDialog.show();
    }

    void showCheckin(String title, String type) {

        final Dialog sucDialog = new Dialog(this); // Context, this, etc.
        sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sucDialog.setContentView(R.layout.dialog_checkin);

        TextView tv_title = sucDialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView bt_ok = sucDialog.findViewById(R.id.bt_ok);

        ImageView iv_cancel = sucDialog.findViewById(R.id.iv_cancel);

        sucDialog.setCancelable(false);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sucDialog.dismiss();
                paymentStatus(getString(R.string.payment_unsuccess), "f");


            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sucDialog.cancel();
            }
        });

        sucDialog.show();
    }

    void paymentStatus(String title, String type) {

        final Dialog sucDialog = new Dialog(this); // Context, this, etc.
        sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sucDialog.setContentView(R.layout.dialog_payment_status);

        TextView tv_status = sucDialog.findViewById(R.id.tv_status);
        tv_status.setText(title);
//        TextView bt_ok = sucDialog.findViewById(R.id.bt_ok);

        ImageView iv_cancel = sucDialog.findViewById(R.id.iv_cancel);
        ImageView image_view = sucDialog.findViewById(R.id.image_view);


        if (type.equalsIgnoreCase("s")) {
            image_view.setImageResource(R.drawable.ic_successful_icon_green);
        } else {
            image_view.setImageResource(R.drawable.ic_unsuccessful_icon);
        }

        sucDialog.setCancelable(false);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sucDialog.cancel();
            }
        });

        sucDialog.show();
    }

}
