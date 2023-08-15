package sa.med.imc.myimc.Calculators;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BMRFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ed_weight)
    EditText edWeight;
    @BindView(R.id.ed_height)
    EditText edHeight;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.ed_age)
    EditText edAge;
    @BindView(R.id.lay_btn_calculate)
    LinearLayout layBtnCalculate;
    @BindView(R.id.lay_result)
    LinearLayout lay_result;
    @BindView(R.id.tv_activity_level)
    TextView tvActivityLevel;
    @BindView(R.id.tv_result_maint)
    TextView tv_result_maint;
    @BindView(R.id.tv_result_bmr)
    TextView tv_result_bmr;

    List<String> list = new ArrayList<>();
    double BMR = 0;
    int weight = 0, height = 0;
    FragmentListener fragmentAdd;

    public BMRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    public static BMRFragment newInstance(String param1, String param2) {
        BMRFragment fragment = new BMRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmr, container, false);
        ButterKnife.bind(this, view);
        lay_result.setVisibility(View.GONE);
        BMR = 0;
        weight = 0;
        height = 0;
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.tv_gender, R.id.lay_btn_calculate, R.id.tv_activity_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.tv_gender:
                showGenderListPopUp(tvGender, tvGender.getText().toString());
                break;

            case R.id.tv_activity_level:
                showActivityListPopUp(tvActivityLevel, tvActivityLevel.getText().toString());
                break;

            case R.id.lay_btn_calculate:
                calculateBMR();
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


    // Pop up list to select gender
    public void showActivityListPopUp(TextView view, String lastValue) {
        list = new ArrayList<>();
        list.add(getString(R.string.level_sedentary));
        list.add(getString(R.string.level_lightly));
        list.add(getString(R.string.level_moderately));
        list.add(getString(R.string.level_active));
        list.add(getString(R.string.level_super));

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


    // calculate BMR
    void calculateBMR() {
        if (edWeight.getText().toString().length() == 0) {
            edWeight.setError(getString(R.string.required));
        }
        if (edHeight.getText().toString().length() == 0) {
            edHeight.setError(getString(R.string.required));
        }

        if (edAge.getText().toString().length() == 0) {
            edAge.setError(getString(R.string.required));
        }else if(Integer.parseInt(edAge.getText().toString())<15){
            edAge.setError(getString(R.string.age_validation));
        }


        if (edAge.getText().toString().trim().length() > 0
                && edWeight.getText().toString().trim().length() > 0
                && edHeight.getText().toString().trim().length() > 0
                && !tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.select))
                && !tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {


            weight = Integer.parseInt(edWeight.getText().toString());
            height = Integer.parseInt(edHeight.getText().toString());

            if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.male))) {
                BMR = 66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * Integer.parseInt(edAge.getText().toString()));
            }
            if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.female))) {
                BMR = 655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * Integer.parseInt(edAge.getText().toString()));
            }

            if (BMR > 0) {
                double total_cal = 0;

                if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.level_sedentary))) {
                    total_cal = BMR * 1.2;

                } else if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.level_lightly))) {
                    total_cal = BMR * 1.375;
                } else if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.level_moderately))) {
                    total_cal = BMR * 1.55;
                } else if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.level_active))) {
                    total_cal = BMR * 1.725;
                } else if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.level_super))) {
                    total_cal = BMR * 1.9;
                }
                lay_result.setVisibility(View.VISIBLE);
                tv_result_bmr.setText("" + Math.round(BMR));
                tv_result_maint.setText(Math.round(total_cal) + "");
            }

//
//            if (height > 0 && weight > 0) {
//                tvResult.setVisibility(View.VISIBLE);
//
//                float height1 = (float) height / 100;
//                float result = (float) (weight / (height1 * height1));
//                DecimalFormat format = new DecimalFormat("##.0");
//
//                if (result < 18.5) {
//                    tvResult.setText(getString(R.string.bmi_under_weight, format.format(result)));
//                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_blue));
//                } else if (result <= 24.9) {
//                    tvResult.setText(getString(R.string.bmi_normal_weight, format.format(result)));
//                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_green));
//                } else if (result <= 29.9) {
//                    tvResult.setText(getString(R.string.bmi_over_weight, format.format(result)));
//                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_yell));
//                } else if (result >= 30.0) {
//                    tvResult.setText(getString(R.string.bmi_obese_weight, format.format(result)));
//                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_red));
//                }
//            } else {
//                tvResult.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            tvResult.setVisibility(View.INVISIBLE);
        } else if (tvActivityLevel.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            Common.makeToast(getActivity(), getString(R.string.please_select_activity_level));

        } else if (tvGender.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            Common.makeToast(getActivity(), getString(R.string.select_gender));
        }
    }

}
