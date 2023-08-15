package sa.med.imc.myimc.RetailModule.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.CartAdapter;
import sa.med.imc.myimc.AddressOrder.AddressOrderActivity;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.GuestHome.UserInfoOrderActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.OrderProfile.ProfileOrdersActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_cart_list)
    RecyclerView rvCartList;
    @BindView(R.id.tv_price_items_total)
    TextView tvPriceItemsTotal;
    @BindView(R.id.tv_delivery_fee)
    TextView tvDeliveryFee;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.content_item_detail)
    LinearLayout contentItemDetail;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_place_order)
    TextView tvPlaceOrder;
    @BindView(R.id.price_total_items)
    TextView priceTotalItems;

    CartAdapter adapter;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, CartActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CartActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

//        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER, "guest").equalsIgnoreCase("guest"))
//            ivProfile.setVisibility(View.GONE);
//        else
//            ivProfile.setVisibility(View.VISIBLE);


        toolbarTitle.setText(getString(R.string.my_cart, "2"));
        priceTotalItems.setText(getString(R.string.price_items, "2"));
        setUpCartsRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finish();
        CartActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.tv_place_order, R.id.iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_place_order:
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER_TYPE, "guest").equalsIgnoreCase("guest"))
                    UserInfoOrderActivity.startActivity(this);
                else
                    AddressOrderActivity.startActivity(this);

                break;

            case R.id.iv_profile:
                ProfileOrdersActivity.startActivity(this);
                break;
        }
    }

    // Initialize Carts list view and add adapter to display data,
    void setUpCartsRecyclerView() {
        adapter = new CartAdapter(this);
        rvCartList.setHasFixedSize(true);
        rvCartList.setLayoutManager(new LinearLayoutManager(this));
        rvCartList.setAdapter(adapter);
        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                removeItem();
            }

            @Override
            public void onItemClick(int position) {

            }
        });
    }


    // Remove Item ask dialog for confirmation.
    private void removeItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.remove_item));
        builder.setMessage(getResources().getString(R.string.remove_cart_item_content));
        builder.setPositiveButton(getResources().getString(R.string.remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showSnackBarMsg(getString(R.string.removed_success));

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            }
        });
        alert.show();
    }
}
