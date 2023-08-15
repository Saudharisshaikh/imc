package sa.med.imc.myimc.AddressOrder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.AddressAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RetailModule.view.CheckOutOrderActivity;
import sa.med.imc.myimc.RetailModule.model.AddressValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressOrderActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.no_result)
    TextView noResult;
    @BindView(R.id.float_add)
    FloatingActionButton floatAdd;
    AddressAdapter adapter;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_back_bottom)
    ImageView ivBackBottom;
    @BindView(R.id.main_toolbar_bottom)
    RelativeLayout mainToolbarBottom;
    @BindView(R.id.toolbar_title_add)
    TextView toolbarTitleAdd;
    @BindView(R.id.et_pincode)
    EditText etPincode;
    @BindView(R.id.et_house_no_villa_no_building)
    EditText etHouseNoVillaNoBuilding;
    @BindView(R.id.et_road_name_area)
    EditText etRoadNameArea;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_state)
    EditText etState;
    @BindView(R.id.et_landmark)
    EditText etLandmark;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.lay_mrn)
    LinearLayout layMrn;
    @BindView(R.id.lay)
    RelativeLayout lay;
    @BindView(R.id.lay_btn_save)
    LinearLayout layBtnSave;
    @BindView(R.id.fl_bottom_sheet_add_edit_address)
    FrameLayout flBottomSheetAddEditAddress;
    @BindView(R.id.rl_add_edit_address)
    RelativeLayout rlAddEditAddress;

    boolean edit = false;

    private BottomSheetBehavior behaviorBtmsheet;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_order);
        ButterKnife.bind(this);
        setUpAddressRecyclerView();
        initBottomSheet();
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AddressOrderActivity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, String from) {
        Intent intent = new Intent(activity, AddressOrderActivity.class);
        intent.putExtra("from", from);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        AddressOrderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.float_add, R.id.iv_back_bottom, R.id.lay_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.float_add:
                toolbarTitleAdd.setText(getString(R.string.add_address));
                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;

            case R.id.iv_back_bottom:
                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

            case R.id.lay_btn_save:

                if (isValidAll()) {
                    if (edit) {
                        showSnackBarMsg(getString(R.string.address_updated));
                        edit = false;

                    } else {
                        showSnackBarMsg(getString(R.string.address_added));
                    }

                    if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        behaviorBtmsheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }

                }
                break;
        }

    }

    // Initialize Address list view and add adapter to display data,
    void setUpAddressRecyclerView() {
        if (getIntent().hasExtra("from"))
            adapter = new AddressAdapter(this, "");
        else
            adapter = new AddressAdapter(this, "cart");

        rvAddress.setHasFixedSize(true);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                removeAddress();
            }

            @Override
            public void onEditClick(int position) {
                toolbarTitleAdd.setText(getString(R.string.edit_address));
                if (behaviorBtmsheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behaviorBtmsheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                edit = true;

            }

            @Override
            public void onItemClick(int position) {
                CheckOutOrderActivity.startActivity(AddressOrderActivity.this);
            }
        });
    }

    boolean isValidAll() {
        AddressValidation validation = new AddressValidation(this);

        boolean nameValid = validation.isNameValid(etName);
        boolean pinValid = validation.isPinValid(etPincode);
        boolean hNoValid = validation.isHNOValid(etHouseNoVillaNoBuilding);
        boolean areaValid = validation.isRoadValid(etRoadNameArea);
        boolean cityValid = validation.isCityStateValid(etCity);
        boolean stateValid = validation.isCityStateValid(etState);
        boolean phoneValid = validation.isPhoneValid(etPhoneNumber);

        return nameValid && pinValid && hNoValid && areaValid && cityValid && stateValid && phoneValid;
    }


    // Remove Address ask dialog for confirmation.
    private void removeAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.remove_address));
        builder.setMessage(getResources().getString(R.string.remove_address_item_content));
        builder.setPositiveButton(getResources().getString(R.string.remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showSnackBarMsg(getString(R.string.removed_success_address));

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


    private void initBottomSheet() {
        View bottomSheet = findViewById(R.id.rl_add_edit_address);
        behaviorBtmsheet = BottomSheetBehavior.from(bottomSheet);


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
                        hideKeyboard();

                        etName.clearComposingText();
                        etPhoneNumber.clearComposingText();
                        etCity.clearComposingText();
                        etHouseNoVillaNoBuilding.clearComposingText();
                        etLandmark.clearComposingText();
                        etRoadNameArea.clearComposingText();
                        etState.clearComposingText();
                        etPhoneNumber.clearComposingText();

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
}
