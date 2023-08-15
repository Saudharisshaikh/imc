package sa.med.imc.myimc.RetailModule.view;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckOutOrderActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rd_debit)
    RadioButton rdDebit;
    @BindView(R.id.rd_credit)
    RadioButton rdCredit;
    @BindView(R.id.rd_medical_card)
    RadioButton rdMedicalCard;
    @BindView(R.id.rd_cod)
    RadioButton rdCod;
    @BindView(R.id.main_radio_card)
    RadioGroup mainRadioCard;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_exp_date)
    EditText etExpDate;
    @BindView(R.id.etcvv)
    EditText etcvv;
    @BindView(R.id.card_detail)
    LinearLayout cardDetail;
    @BindView(R.id.lay_btn_pay_now)
    LinearLayout layBtnPayNow;

    String type = "cod";

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CheckOutOrderActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        CheckOutOrderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_order);
        ButterKnife.bind(this);
        setUpRadio();


    }


    void setUpRadio() {
        mainRadioCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rd_cod:
                        type = "cod";
                        cardDetail.setVisibility(View.GONE);
                        break;

                    case R.id.rd_credit:
                        type = "credit";
                        cardDetail.setVisibility(View.VISIBLE);
                        break;

                    case R.id.rd_debit:
                        type = "debit";
                        cardDetail.setVisibility(View.VISIBLE);
                        break;

                    case R.id.rd_medical_card:
                        type = "card";
                        cardDetail.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    void paymentConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.payment_successful));
        builder.setIcon(R.drawable.ic_successful_icon_green);
        builder.setMessage(getResources().getString(R.string.payment_success));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i1 = new Intent(CheckOutOrderActivity.this, MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);

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

    @OnClick({R.id.iv_back, R.id.lay_btn_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.lay_btn_pay_now:
                if (type.equalsIgnoreCase("debit") || type.equalsIgnoreCase("credit")) {
                    if (etCardNumber.getText().toString().trim().length() == 0) {
                        etCardNumber.setError(getString(R.string.required));
                    } else if (etCardNumber.getText().toString().trim().length() < 14) {
                        etCardNumber.setError("Not valid");

                    } else if (etExpDate.getText().toString().trim().length() == 0) {
                        etExpDate.setError(getString(R.string.required));
                    } else if (etcvv.getText().toString().trim().length() == 0) {
                        etExpDate.setError(getString(R.string.required));
                    } else if (etcvv.getText().toString().trim().length() != 3) {
                        etExpDate.setError("Not valid");
                    } else {
                        paymentConfirm();

                    }

                } else {
                    paymentConfirm();
                }
                break;
        }
    }
}
