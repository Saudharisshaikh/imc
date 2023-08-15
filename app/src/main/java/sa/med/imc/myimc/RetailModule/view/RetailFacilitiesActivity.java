package sa.med.imc.myimc.RetailModule.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.RetailAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetailFacilitiesActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_categories)
    RecyclerView rvCategories;
    RetailAdapter adapter;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.top_card)
    CardView topCard;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_other_time)
    TextView tvOtherTime;
    @BindView(R.id.tv_friday_time)
    TextView tvFridayTime;
    @BindView(R.id.tv_employees)
    TextView tvEmployees;
    @BindView(R.id.fl_bottom_sheet_container)
    FrameLayout flBottomSheetContainer;
    @BindView(R.id.rl_confirm)
    RelativeLayout rlConfirm;
    @BindView(R.id.contact)
    TextView contact;
    private BottomSheetBehavior behaviorBtmsheet;
    int position = -1;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, RetailFacilitiesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(RetailFacilitiesActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        RetailFacilitiesActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_facilities);
        ButterKnife.bind(this);

        setUpRetailRecyclerView();
        initBottomSheet();
    }

    // Initialize Retail list view and add adapter to display data,
    void setUpRetailRecyclerView() {
        adapter = new RetailAdapter(this);
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvCategories.setAdapter(adapter);
        adapter.setOnItemClickListener(new RetailAdapter.OnItemClickListener() {
            @Override
            public void onCategoryClick(int position1) {
                ivImage.setImageResource(adapter.getImage(position1));
                tvName.setText(getString(adapter.getName(position1)));

                switch (adapter.getName(position1)) {

                    case R.string.fine_chocolate:
                        tvEmployees.setText(getString(R.string.fine_chocolate_emp));
                        tvOtherTime.setText(getString(R.string.everyday)+" 07:00AM - 10:45PM");
//                        tvFridayTime.setText(getString(R.string.fri)+" 2PM - 10PM");
                        contact.setVisibility(View.GONE);
                        tvFridayTime.setVisibility(View.GONE);

                        break;

                    case R.string.gardenia:
                        tvEmployees.setText(getString(R.string.gardenia_emp));
                        tvOtherTime.setText(getString(R.string.sat_sun)+" 9:30AM - 10:30PM");
                        tvFridayTime.setText(getString(R.string.fridays)+" 5:00PM - 10PM");
                        tvFridayTime.setVisibility(View.VISIBLE);
                        contact.setText(getString(R.string.contact)+"0595949193");
                        contact.setVisibility(View.VISIBLE);
                        break;

                    case R.string.baby_fitaihi:
                        tvEmployees.setText(getString(R.string.baby_fitaihi_emp));
                        tvOtherTime.setText(getString(R.string.everyday)+" 10AM - 10PM");
//                        tvFridayTime.setText(getString(R.string.fri)+" 5PM - 10PM");
                        contact.setText(getString(R.string.contact)+"0126509000 Ext 9090");
                        contact.setVisibility(View.VISIBLE);
                        tvFridayTime.setVisibility(View.GONE);
                        break;
                }
                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else
            finish();
    }

    // intiaite BottomSheet
    private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.rl_confirm);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);

        findViewById(R.id.iv_close).setOnClickListener(vv -> {
            if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behaviorBtmsheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    void setData() {

    }

}
