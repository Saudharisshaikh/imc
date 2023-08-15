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

import sa.med.imc.myimc.Adapter.LabReportHeaderAdapter;
import sa.med.imc.myimc.Adapter.RecordsAdapter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.adapter.LabReportOldAdapter;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LaboratoryHistoryModel;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.LabreportListner;
import sa.med.imc.myimc.globle.interfaces.LabreportOldDataListner;

public class LabreportOldFragment extends Fragment {


    RecyclerView rv_records;
    TextView tv_total;
    int pageNumber = 1;
    String bookingStatus = "2";
    List<LaboratoryHistoryModel> laboratoryHistoryModelList = new ArrayList<>();
    LabReportOldAdapter labReportOldAdapter;
    int currentItem, totalItem, scrollOutItem;
    Boolean isScrolling = false;

    boolean isReload=true;

    public LabreportOldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_labreport_old, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }


    private void initView() {
        rv_records = getView().findViewById(R.id.rv_records);
        tv_total = getView().findViewById(R.id.tv_total);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rv_records.setLayoutManager(gridLayoutManager);
        labReportOldAdapter = new LabReportOldAdapter(getActivity(), laboratoryHistoryModelList);
        rv_records.setAdapter(labReportOldAdapter);

        rv_records.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItem == totalItem)) {
                    loadData();
                }
            }
        });

        loadData();


    }

    private void loadData() {
        new MyhttpMethod(getContext()).loadLabReportOld(String.valueOf(pageNumber), new LabreportOldDataListner() {
            @Override
            public void onSuccess(List<LaboratoryHistoryModel> laboratoryHistoryModelList1,String total) {
                laboratoryHistoryModelList.addAll(laboratoryHistoryModelList1);
                try {
                    int i= Integer.parseInt(total);
                    tv_total.setText(String.valueOf(i));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                labReportOldAdapter.notifyDataSetChanged();
                pageNumber++;

                if (laboratoryHistoryModelList.size()==0){
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
                if (laboratoryHistoryModelList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });

    }
}