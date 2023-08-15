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

import sa.med.imc.myimc.Adapter.AllProductsAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsListActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.no_result)
    TextView noResult;
    boolean edit = false;

    AllProductsAdapter adapter;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.tv_count_cart)
    TextView tvCountCart;

    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ProductsListActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProductsListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        ButterKnife.bind(this);

        setUpProductsRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finish();
        ProductsListActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_filter, R.id.iv_cart})
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

            case R.id.iv_filter:
                break;

            case R.id.iv_cart:
                CartActivity.startActivity(this);
                break;
        }
    }

    // Initialize Products list view and add adapter to display data,
    void setUpProductsRecyclerView() {
        adapter = new AllProductsAdapter(this);
        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvProducts.setAdapter(adapter);
        adapter.setOnItemClickListener(new AllProductsAdapter.OnItemClickListener() {
            @Override
            public void onViewProductClick(int position) {
                ProductDetailActivity.startActivity(ProductsListActivity.this);
            }
        });

    }
}
