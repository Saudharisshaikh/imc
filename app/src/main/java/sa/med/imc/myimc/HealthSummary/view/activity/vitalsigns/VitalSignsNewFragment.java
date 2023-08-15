package sa.med.imc.myimc.HealthSummary.view.activity.vitalsigns;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;

import sa.med.imc.myimc.HealthSummary.adapter.NewVitalSignAdapter;
import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.NewVitalListner;

public class VitalSignsNewFragment extends Fragment {

    RecyclerView rv_vital_signs;
    NewVitalSignAdapter newVitalSignAdapter;
    ArrayList<NewVitalModel.VitalList> vital_list=new ArrayList<>();
    int pageNumber=1;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;
    public VitalSignsNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_signs_n_ew, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_vital_signs=view.findViewById(R.id.rv_vital_signs);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        rv_vital_signs.setLayoutManager(gridLayoutManager);

        newVitalSignAdapter=new NewVitalSignAdapter(getActivity(),vital_list);
        rv_vital_signs.setAdapter(newVitalSignAdapter);

        rv_vital_signs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        new MyhttpMethod(getContext()).getNewVitalList(String.valueOf(pageNumber), new NewVitalListner() {
            @Override
            public void onSuccess(NewVitalModel newVitalModel) {
                vital_list.addAll(newVitalModel.getVital_list());
                newVitalSignAdapter.notifyDataSetChanged();
                pageNumber++;


                if (vital_list.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed() {

                if (vital_list.size()==0){
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.VISIBLE);
                }else {
                    getView().findViewById(R.id.main_no_data_trans).setVisibility(View.GONE);
                }
            }
        });

    }
}