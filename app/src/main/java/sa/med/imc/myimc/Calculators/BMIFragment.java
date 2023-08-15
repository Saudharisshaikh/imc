package sa.med.imc.myimc.Calculators;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;


public class BMIFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    //    @BindView(R.id.weight_seek)
//    AppCompatSeekBar weightSeek;
//    @BindView(R.id.height_seek)
//    AppCompatSeekBar heightSeek;
//    @BindView(R.id.tv_weight)
//    TextView tvWeight;
//    @BindView(R.id.tv_height)
//    TextView tvHeight;
    @BindView(R.id.tv_result)
    TextView tvResult;
    //    @BindView(R.id.lay_result)
//    LinearLayout layResult;
    @BindView(R.id.ed_weight)
    EditText edWeight;
    @BindView(R.id.ed_height)
    EditText edHeight;
    @BindView(R.id.lay_btn_calculate)
    LinearLayout layBtnCalculate;

    private String mParam1;
    private String mParam2;
    int weight = 0, height = 0;
    FragmentListener fragmentAdd;

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    public static BMIFragment newInstance(String param1, String param2) {
        BMIFragment fragment = new BMIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmi1, container, false);
        ButterKnife.bind(this, view);
        weight = 0;
        height = 0;
        tvResult.setVisibility(View.GONE);

//        heightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvHeight.setText(getString(R.string.height) + ": " + progress + " cm");
//                height = progress;
//
//
//                if (height > 0 && weight > 0) {
//                    layResult.setVisibility(View.VISIBLE);
//
//                    float height1 = (float) height / 100;
//                    float result = (float) (weight / (height1 * height1));
//                    DecimalFormat format = new DecimalFormat("##.0");
//
//                    if (result < 18.5) {
//                        tvResult.setText(getString(R.string.bmi_under_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_blue));
//                    } else if (result <= 24.9) {
//                        tvResult.setText(getString(R.string.bmi_normal_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_green));
//                    } else if (result <= 29.9) {
//                        tvResult.setText(getString(R.string.bmi_over_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_yell));
//                    } else if (result >= 30.0) {
//                        tvResult.setText(getString(R.string.bmi_obese_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_red));
//                    }
//                } else {
//                    layResult.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        weightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvWeight.setText(getString(R.string.weight) + ": " + progress + " kg");
//                if (height > 0 && weight > 0) {
//                    layResult.setVisibility(View.VISIBLE);
//
//                    float height1 = (float) height / 100;
//                    float result = (float) (weight / (height1 * height1));
//                    DecimalFormat format = new DecimalFormat("##.0");
//
//                    if (result < 18.5) {
//                        tvResult.setText(getString(R.string.bmi_under_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_blue));
//                    } else if (result <= 24.9) {
//                        tvResult.setText(getString(R.string.bmi_normal_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_green));
//                    } else if (result <= 29.9) {
//                        tvResult.setText(getString(R.string.bmi_over_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_yell));
//                    } else if (result >= 30.0) {
//                        tvResult.setText(getString(R.string.bmi_obese_weight, format.format(result)));
//                        layResult.setBackgroundColor(getResources().getColor(R.color.app_red));
//                    }
//                } else {
//                    layResult.setVisibility(View.GONE);
//                }
//                weight = progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.lay_btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.lay_btn_calculate:
                calculateBMI();
                break;
        }
    }

    // calculate BMI
    void calculateBMI() {
        if (edWeight.getText().toString().length() == 0) {
            edWeight.setError(getString(R.string.required));
        }
        if (edHeight.getText().toString().length() == 0) {
            edHeight.setError(getString(R.string.required));
        }

        if (edWeight.getText().toString().trim().length() > 0 && edHeight.getText().toString().trim().length() > 0) {
            weight = Integer.parseInt(edWeight.getText().toString());
            height = Integer.parseInt(edHeight.getText().toString());

            if (height > 0 && weight > 0) {
                tvResult.setVisibility(View.VISIBLE);

                float height1 = (float) height / 100;
                float result = (float) (weight / (height1 * height1));
                DecimalFormat format = new DecimalFormat("##.0", new DecimalFormatSymbols(Locale.ENGLISH));

                if (result < 18.5) {
                    tvResult.setText(getString(R.string.bmi_under_weight, format.format(result)));
                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_blue));
                } else if (result <= 24.9) {
                    tvResult.setText(getString(R.string.bmi_normal_weight, format.format(result)));
                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_green));
                } else if (result <= 29.9) {
                    tvResult.setText(getString(R.string.bmi_over_weight, format.format(result)));
                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_yell));
                } else if (result >= 30.0) {
                    tvResult.setText(getString(R.string.bmi_obese_weight, format.format(result)));
                    tvResult.setBackgroundColor(getResources().getColor(R.color.app_red));
                }
            } else {
                tvResult.setVisibility(View.INVISIBLE);
            }
        } else {
            tvResult.setVisibility(View.INVISIBLE);
        }
    }
}
