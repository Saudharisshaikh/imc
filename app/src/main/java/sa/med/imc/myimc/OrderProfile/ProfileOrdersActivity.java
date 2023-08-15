package sa.med.imc.myimc.OrderProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import sa.med.imc.myimc.Adapter.OrdersAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileOrdersActivity extends BaseActivity {

    //    @BindView(R.id.iv_back)
//    ImageView ivBack;
//    @BindView(R.id.lay_my_orders)
//    LinearLayout layMyOrders;
//    @BindView(R.id.lay_my_address)
//    LinearLayout layMyAddress;
    @BindView(R.id.rv_orders)
    RecyclerView rvOrders;

    //    private BottomSheetBehavior behaviorBtmsheet;
    OrdersAdapter adapter;

    @Override
    protected Context getActivityContext() {
        return this;
    }


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileOrdersActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        ProfileOrdersActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProfileOrdersActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_my_orders);
        ButterKnife.bind(this);
        initBottomSheet();
    }

    @OnClick({R.id.iv_back_bottom})//R.id.iv_back, R.id.lay_my_orders, R.id.lay_my_address,
    public void onViewClicked(View view) {

        switch (view.getId()) {
//            case R.id.iv_back:
//                onBackPressed();
//                break;

//            case R.id.lay_my_orders:
//                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//                break;

//            case R.id.lay_my_address:
//                AddressOrderActivity.startActivity(this, "ere");
//                break;

            case R.id.iv_back_bottom:
                onBackPressed();
//                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
//                } else {
//                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
                break;

        }
    }


    private void initBottomSheet() {
//        View bottomSheet = findViewById(R.id.rl_orders);
//        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);
        adapter = new OrdersAdapter(this, "");

        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
        adapter.setOnItemClickListener(new OrdersAdapter.OnItemClickListener() {
            @Override
            public void onRateReviewClick(int position) {

            }

            @Override
            public void onItemClick(int position) {
                OrdersDetailActivity.startActivity(ProfileOrdersActivity.this);
            }
        });


//        behaviorBtmsheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        logger("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
//                        break;
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
////                        hideKeyboard();
////                        adapter.clear();
//                        break;
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
//            }
//        });

    }
}
