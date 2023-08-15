package sa.med.imc.myimc.Calculators;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class IdealBodyWeightFragment extends Fragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ed_height)
    EditText edHeight;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.ed_age)
    EditText edAge;
    @BindView(R.id.tv_result_ibw)
    TextView tvResult;
    @BindView(R.id.lay_result)
    LinearLayout layResult;
    @BindView(R.id.lay_btn_calculate)
    LinearLayout layBtnCalculate;
    FragmentListener fragmentAdd;

    double ibw = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    public IdealBodyWeightFragment() {
        // Required empty public constructor
    }


    public static IdealBodyWeightFragment newInstance() {
        IdealBodyWeightFragment fragment = new IdealBodyWeightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ideal_body_weight, container, false);
        ButterKnife.bind(this, view);
        layResult.setVisibility(View.GONE);
        ibw = 0;

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.tv_gender, R.id.lay_btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.tv_gender:
                showGenderListPopUp(tvGender, tvGender.getText().toString());
                break;

            case R.id.lay_btn_calculate:
                calculateIBW();
                break;
        }
    }

    // Pop up list to select gender
    public void showGenderListPopUp(TextView view, String lastValue) {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.male));
        list.add(getString(R.string.female));

        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_custom_list, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                view.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);

        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(getActivity(), list, lastValue);
        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_list.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            view.setEnabled(true);
            view.setText(list.get(position));
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, (int) view.getX(), 4);//5

    }

    // calculate IBW
    void calculateIBW() {

        if (edHeight.getText().toString().length() == 0) {
            edHeight.setError(getString(R.string.required));
        }

        if (edAge.getText().toString().length() == 0) {
            edAge.setError(getString(R.string.required));
        }

        if (edAge.getText().toString().trim().length() > 0
                && edHeight.getText().toString().trim().length() > 0
                && !tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {

            DecimalFormat format = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.ENGLISH));

            int height = Integer.parseInt(edHeight.getText().toString());

            if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.male))) {
                if (height <= 152.4)
                    ibw = 50.0;
                else
                    ibw = 50.0 + ((height - 152.4) / 2.54) * 2.3;
            }
            if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.female))) {
                if (height <= 152.4)
                    ibw = 45.5;
                else
                    ibw = 45.5 + ((height - 152.4) / 2.54) * 2.3;
            }

            Log.e("IBW=  ", " =======   " + format.format(ibw));


            layResult.setVisibility(View.VISIBLE);
            tvResult.setText(format.format(ibw) + " kgs");


        } else if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            Common.makeToast(getActivity(), getString(R.string.select_gender));
        }
    }


}
