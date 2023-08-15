package sa.med.imc.myimc.Physicians;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Login.LoginActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.AllDoctoListener;
import sa.med.imc.myimc.Physicians.presenter.PhysicianImpl;
import sa.med.imc.myimc.Physicians.presenter.PhysicianPresenter;
import sa.med.imc.myimc.Physicians.view.BottomConfirmDialogNextAvailable;
import sa.med.imc.myimc.Physicians.view.BottomOptionDialog;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.Physicians.view.PhysicianViews;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.CustomTypingEditText;
import sa.med.imc.myimc.Utils.DailogAlert;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.MyhttpMethod;

public class FindAllDoctorActivity extends AppCompatActivity implements DailogAlert{

    Context context=FindAllDoctorActivity.this;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_clinic)
    TextView tv_clinic;
    @BindView(R.id.ed_search)
    CustomTypingEditText edSearch;
    @BindView(R.id.rv_physicians)
    RecyclerView rvPhysicians;
    @BindView(R.id.tv_load_more)
    TextView tv_load_more;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.bt_try_again)
    Button btTryAgain;
    @BindView(R.id.main_no_net)
    RelativeLayout mainNoNet;
    @BindView(R.id.main_time_out)
    RelativeLayout mainTimeOut;
    @BindView(R.id.bt_try_again_time_out)
    Button btTryAgainTimeOut;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.main_no_data_trans)
    RelativeLayout mainNoDataTrans;
    View v;

    int pos = -1, page = 0, total_items = 0;
    String depart_id = "", lang_id = "";
    boolean edit = false, isLoading = false, isSearched = false;
    PhysicianAdapter adapter;
    PhysicianPresenter physicianPresenter;
    List<PhysicianResponse.ProviderList> list = new ArrayList<>();
    BottomOptionDialog bottomOptionDialog;
    private boolean loadingEnd = false;
    BottomConfirmDialogNextAvailable mBottomConfirmDialogNextAvailable;
    String specialityDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_all_doctor);
        ButterKnife.bind(this, FindAllDoctorActivity.this);


        setUpPhysicianRecyclerView();
        loadAllDoctor();
    }

    private void loadAllDoctor() {
//        new MyhttpMethod(this).callLoadDoctorByHospitalCode(SharedPreferencesUtils.getInstance().getValue(Constants.HOSPITAL_CODE, "IMC"), new AllDoctoListener() {
//            @Override
//            public void onSuccess(AllDoctorListModel response1) {
//                if (response1.getCareProvidersModelList().size() > 0) {
//                    List<AllDoctorListModel.CareProvidersModelList> careProvidersModelLists=response1.getCareProvidersModelList();
//
//                    for (int i = 0; i < careProvidersModelLists.size(); i++) {
//                        AllDoctorListModel.CareProvidersModelList careProvidersModelList=careProvidersModelLists.get(i);
//                        PhysicianResponse.ProviderList providerList = new PhysicianResponse.ProviderList();
//
//                        providerList.setProviderCode(careProvidersModelList.getProvider_code());
//                        providerList.setProviderDescription(careProvidersModelList.getProvider_description());
//                        providerList.setPatientPortalDoctor(careProvidersModelList.getIs_patientportal_doctor());
//
//                        if (providerList.isPatientPortalDoctor()) {
//                            list.add(providerList);
//                        }
//                    }
//
//                } else {
//                    rvPhysicians.setVisibility(View.GONE);
//                }
//                adapter.notifyDataSetChanged();
////                adapter = new PhysicianAdapter(context, list, "", specialityDescription, FindAllDoctorActivity.this);
////                rvPhysicians.setAdapter(adapter);
//
//                Log.e("abcd",new Gson().toJson(list));
//                if (list.size() < 1) {
//                    mainNoDataTrans.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFaild(String message) {
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // Initialize Physician list view and add adapter to display data,
    void setUpPhysicianRecyclerView() {
        list = new ArrayList<>();
        adapter = new PhysicianAdapter(context, list, "", specialityDescription, this);
        rvPhysicians.setHasFixedSize(true);
        rvPhysicians.setLayoutManager(new LinearLayoutManager(context));
        rvPhysicians.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhysicianAdapter.OnItemClickListener() {
            @Override
            public void onViewProfile(int position) {
                startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onBookAppointment(int position) {
                startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onBookNextAvailable(int position) {
                startActivity(new Intent(context, LoginActivity.class));

            }
        });

        rvPhysicians.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //get the recycler view position which is completely visible and first
                final int positionView = ((LinearLayoutManager) rvPhysicians.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int numItems = adapter.getItemCount() - 1;

                if (positionView >= numItems) {
                    if (isLoading && loadingEnd) {
                        loadingEnd = false;
                        isLoading = false;
                    }
                }
            }
        });
    }


    @Override
    public void dailog(String content) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();

    }
}