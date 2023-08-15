package sa.med.imc.myimc.HealthSummary.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.HealthSummary.view.activity.VitalSignsActivity;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Questionaires.view.CompletedFormsListActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;


public class HealthSummaryFragment extends Fragment {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    FragmentListener fragmentAdd;
    @BindView(R.id.cardAssessment)
    CardView cardAssessment;


    public HealthSummaryFragment() {
        // Required empty public constructor
    }

    public static HealthSummaryFragment newInstance() {
        HealthSummaryFragment fragment = new HealthSummaryFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_summary, container, false);
        ButterKnife.bind(this, view);

//        receiveArguments();

        return view;
    }

    private void receiveArguments() {
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 1) == 4) {
            cardAssessment.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back, R.id.lay_item_allergies, R.id.lay_item_vital_signs, R.id.lay_item_assessment})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.lay_item_allergies:
                if (fragmentAdd != null)
                    fragmentAdd.openAllergies("AllergiesFragment");
                break;

            case R.id.lay_item_vital_signs:
                if (fragmentAdd != null)
                    startActivity(new Intent(getActivity(), VitalSignsActivity.class));
//                    fragmentAdd.openVitalSigns("VitalSignsFragment");
                break;

            case R.id.lay_item_assessment:
                CompletedFormsListActivity.startActivity(getActivity());
                break;
        }
    }
}
