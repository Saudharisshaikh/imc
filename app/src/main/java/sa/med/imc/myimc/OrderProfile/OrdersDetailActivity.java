package sa.med.imc.myimc.OrderProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.OrderStatusAdapter;
import sa.med.imc.myimc.Adapter.OrdersAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_product_pic)
    ImageView ivProductPic;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rv_orders_status)
    RecyclerView rvOrdersStatus;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rv_orders)
    RecyclerView rvOrders;
    @BindView(R.id.price_total_items)
    TextView priceTotalItems;
    @BindView(R.id.tv_price_items_total)
    TextView tvPriceItemsTotal;
    @BindView(R.id.tv_delivery_fee)
    TextView tvDeliveryFee;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.content_item_detail)
    LinearLayout contentItemDetail;
    @BindView(R.id.tv_payment_mode)
    TextView tvPaymentMode;
    @BindView(R.id.tv_cancel_review)
    TextView tvCancelReview;
    @BindView(R.id.tv_need_help)
    TextView tvNeedHelp;
    OrdersAdapter adapter;
    OrderStatusAdapter statusAdapter;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, OrdersDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        OrdersDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OrdersDetailActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);
        ButterKnife.bind(this);

        setUpOtherItemsInOrder();
        setUpStatusOrder();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel_review, R.id.tv_need_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_cancel_review:
                onBackPressed();
                break;

            case R.id.tv_need_help:
                break;
        }
    }

    void setUpOtherItemsInOrder() {
        adapter = new OrdersAdapter(this, "ff");

        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
    }

    void setUpStatusOrder() {
        List<String> list = new ArrayList<>();
        list.add("Ordered");
        list.add("Packed");
        list.add("Shipped");
        list.add("Delivery");

        statusAdapter = new OrderStatusAdapter(this, list);

        rvOrdersStatus.setHasFixedSize(true);
        rvOrdersStatus.setLayoutManager(new LinearLayoutManager(this));
        rvOrdersStatus.setAdapter(statusAdapter);
    }
}
