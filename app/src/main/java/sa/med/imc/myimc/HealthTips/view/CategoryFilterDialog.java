package sa.med.imc.myimc.HealthTips.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import sa.med.imc.myimc.HealthTips.model.CategoryResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class CategoryFilterDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.categories_spinner)
    Spinner categories_spinner;

    private BottomDailogListener mDialogListener;

    ArrayList<String> languages = new ArrayList<>();

    public static CategoryResponse response1;

    @NonNull
    public static CategoryFilterDialog newInstance() {
        CategoryFilterDialog astro = new CategoryFilterDialog();
        return astro;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // reusing...
        View view = inflater.inflate(R.layout.bottom_sheet_category_filters, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);

        if (response1 != null)
            setData();
        else
            callGetAllCategories(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""));


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mDialogListener = (BottomDailogListener) parent;
        } else {
            mDialogListener = (BottomDailogListener) context;
        }
    }

    @Override
    public void onDetach() {
        mDialogListener = null;
        super.onDetach();
    }


    @OnClick({R.id.iv_close, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                String id = "";

                if (languages.size() > 0)
                    if (categories_spinner.getSelectedItemPosition() > 0)
                        id = String.valueOf(response1.getData().get(categories_spinner.getSelectedItemPosition() - 1).getId());

                    else if (categories_spinner.getSelectedItemPosition() == 0)
                        id = "";

                mDialogListener.onDone(id);
                dismiss();

                break;

            case R.id.iv_clear:
                categories_spinner.setSelection(0);

                mDialogListener.onClear("clear");
                dismiss();

                break;
        }
    }


    public interface BottomDailogListener {
        void onDone(String cat_id);

        void onClear(String text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Get All languages doctors know
    public void callGetAllCategories(String token) {
//        Common.showDialog(getContext());
        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
        Call<CategoryResponse> xxx = webService.getAllCategoriesCMS();

        xxx.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
//                Common.hideDialog();
                if (response.isSuccessful()) {
                    response1 = response.body();
                    setData();
                }


            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Common.hideDialog();
            }
        });

    }

    void setData() {
        if (response1 != null) {
            if (response1.getStatus()) {
                languages = new ArrayList<>();

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {

                    for (int i = 0; i < response1.getData().size(); i++) {
                        if (response1.getData().get(i).getNameAr() != null)
                            languages.add(response1.getData().get(i).getNameAr());
                    }
                    languages.add(0, "الكل");
                } else {
                    for (int i = 0; i < response1.getData().size(); i++) {
                        if (response1.getData().get(i).getNameEn() != null)
                            languages.add(response1.getData().get(i).getNameEn());
                    }
                    languages.add(0, "All");
                }


                if (getActivity() != null) {


                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories_spinner.setAdapter(spinnerArrayAdapter);
                }
            }
        }
    }

}
