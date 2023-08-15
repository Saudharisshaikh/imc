package sa.med.imc.myimc.Notifications.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sa.med.imc.myimc.Adapter.NotificationsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Notifications.presenter.NotificationImpl;
import sa.med.imc.myimc.Notifications.presenter.NotificationPresenter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
Notifications list got form IMC
Booking reminder
Report uploaded
 */
public class NotificationsActivity extends BaseActivity implements NotificationViews {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_notifications)
    RecyclerView rvNotifications;
    NotificationsAdapter adapter;
    NotificationPresenter presenter;
    List<NotificationResponse.NotificationModel> notificationModels = new ArrayList<>();
    int page = 0, total_items = 0, pos = -1;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.main_no_data)
    RelativeLayout mainNoData;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.iv_done)
    ImageView ivDone;
    @BindView(R.id.tv_clear_all)
    TextView tvClearAll;
    @BindView(R.id.iv_back_no)
    ImageView ivBackNo;
    boolean isLoading = false;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, NotificationsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        NotificationsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        pos = -1;
        page = 0;

        presenter = new NotificationImpl(this, this);
        callAPI();

        setUpNotificationsRecyclerView();

        swipeToRefresh.setOnRefreshListener(() -> {
            page = 0;
            callAPI();
        });
        if(SharedPreferencesUtils.getInstance(
                NotificationsActivity.this).getValue(Constants.KEY_LANGUAGE,Constants.ENGLISH)!=Constants.ENGLISH){
            ivDone.setScaleX(-1);
        }
    }

    void callAPI() {
        presenter.callGetNotifications(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_MRN, ""), Constants.RECORD_SET, String.valueOf(page),
                SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
        setResult(RESULT_OK, new Intent());

        NotificationsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Initialize Notifications list view and add adapter to display data,
    void setUpNotificationsRecyclerView() {
        adapter = new NotificationsAdapter(this, notificationModels);
        rvNotifications.setHasFixedSize(true);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
        adapter.setOnItemClickListener(new NotificationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (notificationModels.get(position).getRead().equalsIgnoreCase("0")) {
                    pos = position;
                    presenter.callReadNotification(SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_MRN, ""),
                            String.valueOf(notificationModels.get(position).getId()),
                            SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
                } else {
                    if (notificationModels.get(position).getType().equalsIgnoreCase("B")) {
                        ManageBookingsActivity.startActivity(NotificationsActivity.this);
                    } else if (notificationModels.get(position).getType().equalsIgnoreCase("L")) {
                        Intent i1 = new Intent(NotificationsActivity.this, MainActivity.class);
                        i1.putExtra("lab", "dfsdhfhnsd");
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1);

                    } else if (notificationModels.get(position).getType().equalsIgnoreCase("M")) {
                        Intent i1 = new Intent(NotificationsActivity.this, MainActivity.class);
                        i1.putExtra("radio", "dfsdhfhnsd");
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1);

                    }
                }
            }

            @Override
            public void onItemLongClick(int position) {
                ivDone.setVisibility(View.VISIBLE);
                tvClearAll.setVisibility(View.VISIBLE);
                adapter.setEdit(true);
            }

            @Override
            public void onDeleteClick(int position) {
                pos = position;
                presenter.callDeleteNotification(SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        String.valueOf(notificationModels.get(position).getId()),
                        SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));

            }
        });


        rvNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvNotifications.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (notificationModels.size() < total_items && isLoading) {
                        isLoading = false;
                        callAPI();
                    }
                }
            }
        });
    }

    @Override
    public void onGetCount(int count) {

    }

    @Override
    public void onSuccess(NotificationResponse response) {
        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoData.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.GONE);

        ivBackNo.setVisibility(View.GONE);
        swipeToRefresh.setRefreshing(false);

        if (response.getNotificationModels() != null) {
            if (page == 0)
                notificationModels.clear();
            notificationModels.addAll(response.getNotificationModels());

            if (notificationModels.size() == 0) {
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.VISIBLE);
                tvClearAll.setVisibility(View.GONE);
                ivBackNo.setVisibility(View.GONE);

            } else {
                mainNoNet.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
                mainNoDataTrans.setVisibility(View.GONE);
                tvClearAll.setVisibility(View.VISIBLE);

                if (response.getTotalItems() != null)
                    if (response.getTotalItems().length() > 0) {
                        total_items = Integer.parseInt(response.getTotalItems());
                        if (notificationModels.size() < Integer.parseInt(response.getTotalItems())) {
                            tvLoadMore.setVisibility(View.GONE);//VISIBLE
                            page = page + 1;
                            isLoading = true;

                        } else {
                            tvLoadMore.setVisibility(View.GONE);
                        }
                    }
            }
        } else {
            mainNoNet.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            mainNoData.setVisibility(View.GONE);
            ivBackNo.setVisibility(View.GONE);
            mainNoDataTrans.setVisibility(View.VISIBLE);
            tvClearAll.setVisibility(View.GONE);


        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(SimpleResponse response) {
//        onFail(getString(R.string.deleted));
        notificationModels.remove(pos);
        adapter.notifyDataSetChanged();
        pos = -1;

        if (notificationModels.size() == 0) {
            page = 0;
            callAPI();
        }
    }

    @Override
    public void onClearAll(SimpleResponse response) {
        notificationModels.clear();
        adapter.notifyDataSetChanged();
        page = 0;

        mainContent.setVisibility(View.VISIBLE);
        mainNoNet.setVisibility(View.GONE);
        mainTimeOut.setVisibility(View.GONE);
        mainNoDataTrans.setVisibility(View.VISIBLE);
        tvClearAll.setVisibility(View.GONE);
        ivBackNo.setVisibility(View.GONE);

    }

    @Override
    public void onRead(SimpleResponse response) {
        notificationModels.get(pos).setRead("1");

        if (notificationModels.get(pos).getType().equalsIgnoreCase("B")) {
            ManageBookingsActivity.startActivity(NotificationsActivity.this);

        } else if (notificationModels.get(pos).getType().equalsIgnoreCase("L")) {
            Intent i1 = new Intent(NotificationsActivity.this, MainActivity.class);
            i1.putExtra("lab", "dfsdhfhnsd");
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);

        } else if (notificationModels.get(pos).getType().equalsIgnoreCase("M")) {
            Intent i1 = new Intent(NotificationsActivity.this, MainActivity.class);
            i1.putExtra("radio", "dfsdhfhnsd");
            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        Common.showDialog(this);
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        swipeToRefresh.setRefreshing(false);

        if (msg.equalsIgnoreCase("timeout")) {
            if (notificationModels.size() == 0) {
                mainContent.setVisibility(View.GONE);
                mainNoNet.setVisibility(View.GONE);
                mainNoData.setVisibility(View.GONE);
                mainTimeOut.setVisibility(View.VISIBLE);
                ivBackNo.setVisibility(View.VISIBLE);

            } else {
                mainNoData.setVisibility(View.VISIBLE);
//                makeToast(getString(R.string.time_out_messgae));
                isLoading = true;

            }

        } else {
            mainNoData.setVisibility(View.VISIBLE);
//            makeToast(msg);
        }
    }

    @Override
    public void onNoInternet() {
        swipeToRefresh.setRefreshing(false);

        if (notificationModels.size() == 0) {
            mainContent.setVisibility(View.GONE);
            mainNoNet.setVisibility(View.VISIBLE);
            mainNoData.setVisibility(View.GONE);
            mainTimeOut.setVisibility(View.GONE);
            ivBackNo.setVisibility(View.VISIBLE);
        } else {
            isLoading = true;
            Common.noInternet(this);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_back_no, R.id.tv_clear_all, R.id.bt_try_again, R.id.bt_try_again_time_out, R.id.tv_load_more, R.id.iv_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_back_no:
                onBackPressed();
                break;

            case R.id.tv_clear_all:
                clearAll();
                break;

            case R.id.bt_try_again:
                callAPI();
                break;

            case R.id.bt_try_again_time_out:
                callAPI();
                break;

            case R.id.tv_load_more:
                callAPI();
                break;

            case R.id.iv_done:
                adapter.setEdit(false);
                ivDone.setVisibility(View.GONE);
                break;
        }
    }

    //Clear all notifications confirmation  alert
    void clearAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.clear_notifications));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.callClearAllNotification(SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                        SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_MRN, ""),
                        SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.SELECTED_HOSPITAL, 0));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

                try {
                    Typeface typeface = ResourcesCompat.getFont(NotificationsActivity.this, R.font.font_app);
                    ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (SharedPreferencesUtils.getInstance(NotificationsActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alert.show();
    }
}
