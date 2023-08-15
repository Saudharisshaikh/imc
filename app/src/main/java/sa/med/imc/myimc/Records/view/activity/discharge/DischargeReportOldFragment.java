package sa.med.imc.myimc.Records.view.activity.discharge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.adapter.DischargeAdapter;
import sa.med.imc.myimc.Records.adapter.DischargeOldAdapter;
import sa.med.imc.myimc.Records.model.DischargeModel;
import sa.med.imc.myimc.Records.model.DischargeOldModel;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.DischageOdListner;
import sa.med.imc.myimc.globle.interfaces.DischargeReportListner;

public class DischargeReportOldFragment extends Fragment {

    DischargeOldAdapter dischargeOldAdapter;
    ArrayList<DischargeOldModel.Data> dataArrayList=new ArrayList<>();


    RecyclerView rv_records;
    TextView tv_total;
    int pageNumber=1;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;

    public DischargeReportOldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discharge_report_old, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tv_total=getView().findViewById(R.id.tv_total);
        rv_records=getView().findViewById(R.id.rv_records);

        dischargeOldAdapter =new DischargeOldAdapter(getContext(),dataArrayList);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        rv_records.setLayoutManager(gridLayoutManager);
        rv_records.setAdapter(dischargeOldAdapter);


        rv_records.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem=gridLayoutManager.getChildCount();
                totalItem=gridLayoutManager.getItemCount();
                scrollOutItem=gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (isScrolling&&(currentItem+scrollOutItem==totalItem)){
                    loadData();
                }
            }
        });

        loadData();
    }

    private void loadData() {
        new MyhttpMethod(getContext()).dischargeOldReport(String.valueOf(pageNumber), new DischageOdListner() {
            @Override
            public void onSuccess(DischargeOldModel dischargeOldModel) {
                dataArrayList.addAll(dischargeOldModel.data);
                tv_total.setText(dischargeOldModel.total_items);
                dischargeOldAdapter.notifyDataSetChanged();
                pageNumber++;

                if (dataArrayList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed() {
                if (dataArrayList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });

    }
}