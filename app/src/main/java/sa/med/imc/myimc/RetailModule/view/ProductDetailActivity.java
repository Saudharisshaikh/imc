package sa.med.imc.myimc.RetailModule.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.ReviewsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.ed_quantity)
    EditText edQuantity;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_minus)
    TextView tvMinus;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_add_to_cart)
    TextView tvAddToCart;
    @BindView(R.id.tv_reviews)
    TextView tvReviews;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.tv_empty_view)
    TextView tvEmptyView;
    @BindView(R.id.rl_reviews)
    RelativeLayout rlReviews;
    @BindView(R.id.fl_bottom_sheet_container)
    FrameLayout flBottomSheetContainer;
    @BindView(R.id.iv_back_bottom_bottom)
    ImageView ivBackBottomBottom;
    ReviewsAdapter reviewsAdapter;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.tv_count_cart)
    TextView tvCountCart;

    private BottomSheetBehavior behaviorBtmsheet;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProductDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        tvReviews.setText(getString(R.string.reviews, "5"));
        addWatcher();
        initBottomSheet();
    }

    @Override
    public void onBackPressed() {
        finish();
        ProductDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @OnClick({R.id.iv_back, R.id.iv_image, R.id.tv_add, R.id.tv_minus, R.id.tv_add_to_cart, R.id.tv_reviews, R.id.iv_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_image:
                break;

            case R.id.tv_add:
                if (edQuantity.getText().toString().trim().length() > 0) {
                    int qua = Integer.parseInt(edQuantity.getText().toString());
                    qua = qua + 1;
                    edQuantity.setText("" + qua);
                }
                break;

            case R.id.tv_minus:
                if (edQuantity.getText().toString().trim().length() > 0) {
                    int qua = Integer.parseInt(edQuantity.getText().toString());
                    if (qua > 1) {
                        qua = qua - 1;
                        edQuantity.setText("" + qua);
                    }
                }
                break;

            case R.id.tv_add_to_cart:
                if (tvAddToCart.getText().toString().equalsIgnoreCase(getString(R.string.add_to_cart))) {
                    vibrate();
                    showSnackBarMsg(getString(R.string.item_added));
                    tvAddToCart.setText(getString(R.string.go_to_cart));
                } else {
                    CartActivity.startActivity(this);
                }
                break;

            case R.id.tv_reviews:
                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;

            case R.id.iv_cart:
                CartActivity.startActivity(this);
                break;
        }
    }

    void addWatcher() {
        edQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    int qua = Integer.parseInt(edQuantity.getText().toString());
                    double total = qua * 120.90;
                    tvTotalPrice.setText("Total: $" + round(total, 2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(100, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.rl_reviews);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);
        reviewsAdapter = new ReviewsAdapter(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(mLayoutManager);
        rvReviews.setAdapter(reviewsAdapter);

        ivBackBottomBottom.setOnClickListener(vv -> {
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        behaviorBtmsheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        logger("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

    }


    void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}
