package sa.med.imc.myimc.Records.view.activity.cardiology;

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
import sa.med.imc.myimc.Records.adapter.CardiologyAdapter;
import sa.med.imc.myimc.Records.model.CardiologyModel;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.CardiologyListner;

public class CardiologyReportNewFragment extends Fragment {

    CardiologyAdapter cardiologyAdapter;
    List<CardiologyModel> cardiologyModelList=new ArrayList<>();

    RecyclerView rv_records;
    TextView tv_total;
    int pageNumber=1;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;
    public CardiologyReportNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cardiology_report_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        tv_total=getView().findViewById(R.id.tv_total);
        rv_records=getView().findViewById(R.id.rv_records);

        cardiologyAdapter =new CardiologyAdapter(getContext(),cardiologyModelList);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        rv_records.setLayoutManager(gridLayoutManager);
        rv_records.setAdapter(cardiologyAdapter);


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
//                    loadData();
                }
            }
        });

        loadData();
    }

    private void loadData() {
        new MyhttpMethod(getContext()).cardiologyReport(String.valueOf(pageNumber), new CardiologyListner() {
            @Override
            public void onSuccess(List<CardiologyModel> cardiologyModelList1) {
                cardiologyModelList.addAll(cardiologyModelList1);
                tv_total.setText(cardiologyModelList.size()+"");
                cardiologyAdapter.notifyDataSetChanged();
                pageNumber++;

                if (cardiologyModelList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed() {

                if (cardiologyModelList.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });
    }
}