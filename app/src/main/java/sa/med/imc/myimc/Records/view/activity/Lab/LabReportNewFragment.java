package sa.med.imc.myimc.Records.view.activity.Lab;

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

import sa.med.imc.myimc.Adapter.RecordsAdapter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.LabreportListner;

public class LabReportNewFragment extends Fragment {


    RecyclerView rv_records;
    TextView tv_total;
    int pageNumber=1;
    String bookingStatus="2";
    List<LabReport> reports = new ArrayList<>();
    RecordsAdapter adapter;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;


    boolean isReload=true;
    public LabReportNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lab_report_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        rv_records=getView().findViewById(R.id.rv_records);
        tv_total=getView().findViewById(R.id.tv_total);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        rv_records.setLayoutManager(gridLayoutManager);
        adapter = new RecordsAdapter(getContext(), reports);
        rv_records.setAdapter(adapter);

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
        new MyhttpMethod(getContext()).loadLabReport(String.valueOf(pageNumber), bookingStatus, new LabreportListner() {
            @Override
            public void onSuccess(LabReportResponse labReportResponse) {
                reports.addAll(labReportResponse.getOrder_list());
                tv_total.setText(labReportResponse.getItemCount()+"");
                adapter.notifyDataSetChanged();
                pageNumber++;

                if (reports.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed() {

                if (isReload){
                    loadData();
                }
                isReload=false;
                if (reports.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });


    }
}