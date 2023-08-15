package sa.med.imc.myimc.RetailModule.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.AllCategoriesAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllCategoriesActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_categories)
    RecyclerView rvCategories;
    @BindView(R.id.no_result)
    TextView noResult;
    boolean edit = false;

    AllCategoriesAdapter allCategoriesAdapter;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.tv_count_cart)
    TextView tvCountCart;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AllCategoriesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AllCategoriesActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        ButterKnife.bind(this);

        setUpCategoriesRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finish();
        AllCategoriesActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_cart})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search);
                    edSearch.setVisibility(View.GONE);
                    edit = false;
                } else {
                    ivSearch.setImageResource(R.drawable.ic_close);
                    edSearch.setVisibility(View.VISIBLE);
                    edit = true;
                }
                break;

            case R.id.iv_cart:
                CartActivity.startActivity(this);
                break;
        }
    }

    // Initialize Categories list view and add adapter to display data,
    void setUpCategoriesRecyclerView() {
        allCategoriesAdapter = new AllCategoriesAdapter(this);
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvCategories.setAdapter(allCategoriesAdapter);
        allCategoriesAdapter.setOnItemClickListener(new AllCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onCategoryClick(int position) {
                ProductsListActivity.startActivity(AllCategoriesActivity.this);
            }
        });

    }
}
