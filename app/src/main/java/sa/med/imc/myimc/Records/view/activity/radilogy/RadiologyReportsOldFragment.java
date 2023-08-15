package sa.med.imc.myimc.Records.view.activity.radilogy;

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

import sa.med.imc.myimc.Adapter.MedicalReportAdapter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.adapter.RadiologyOldAdapter;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.RadiologyOldModel;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.RadiologyListner;
import sa.med.imc.myimc.globle.interfaces.RadiologyOldListner;

public class RadiologyReportsOldFragment extends Fragment {


    RecyclerView rv_records;
    TextView tv_total;
    RadiologyOldAdapter radiologyOldAdapter;
    List<RadiologyOldModel> radiologyOldModelList = new ArrayList<>();
    int pageNumber=1;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;

    boolean isReload=true;

    public RadiologyReportsOldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radiology_reports_old, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tv_total=getView().findViewById(R.id.tv_total);
        rv_records=getView().findViewById(R.id.rv_records);
        radiologyOldAdapter = new RadiologyOldAdapter(getActivity(), radiologyOldModelList);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        rv_records.setLayoutManager(gridLayoutManager);
        rv_records.setAdapter(radiologyOldAdapter);

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
        new MyhttpMethod(getContext()).radiologyOldReport(String.valueOf(pageNumber), new RadiologyOldListner() {
            @Override
            public void onSuccess(List<RadiologyOldModel> radiologyOldModelList1,String total_items) {
                radiologyOldModelList.addAll(radiologyOldModelList1);
                tv_total.setText(total_items);
                radiologyOldAdapter.notifyDataSetChanged();
                pageNumber++;

                if (radiologyOldModelList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFaild() {

                if (isReload){
                    loadData();
                }
                isReload=false;
                if (radiologyOldModelList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });

    }
}