package sa.med.imc.myimc.MedicineDetail.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.MedicineDetail.adapter.MedicineAdapter;
import sa.med.imc.myimc.MedicineDetail.model.MedicineModel;
import sa.med.imc.myimc.MedicineDetail.model.MedicineResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.MedicineListner;

public class MedicineActivity extends AppCompatActivity {

    List<MedicineModel> medicineModelList =new ArrayList<>();
    MedicineAdapter medicineAdapter;
    RecyclerView rv_medicines;
    ImageView iv_back;
    int pageNumber=1;
    int currentItem,totalItem,scrollOutItem;
    Boolean isScrolling=false;
    Boolean isLoading=false;
    Boolean isLast=false;

    boolean reLoad=true;
    RelativeLayout main_no_data_trans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        initView();
    }

    private void initView() {
        rv_medicines=findViewById(R.id.rv_medicines);
        main_no_data_trans=findViewById(R.id.main_no_data_trans);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(view -> finish());

        medicineAdapter=new MedicineAdapter(this, medicineModelList);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        rv_medicines.setLayoutManager(gridLayoutManager);
        rv_medicines.setAdapter(medicineAdapter);

        rv_medicines.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (isLoading==false & isLast==false) {
                        loadData();
                    }
                }
            }
        });
        loadData();
    }


    private void loadData() {
        isLoading=true;
        new MyhttpMethod(this).getMedicineList(String.valueOf(pageNumber), new MedicineListner() {


            @Override
            public void onSuccess(MedicineResponse medicineResponse) {
                isLoading=false;
                medicineModelList.addAll(medicineResponse.getMedication_list());
                medicineAdapter.notifyDataSetChanged();
                pageNumber++;
                if (medicineModelList.size()==0){
                    main_no_data_trans.setVisibility(View.VISIBLE);
                }
                if (medicineResponse.getMedication_list().size()==0){
                    isLast=true;
                }
            }

            @Override
            public void onFail() {
                if (reLoad){
                    loadData();
                }
                Log.e("abcd","onFail");
                isLoading=false;
                reLoad=false;
            }
        });

    }
}