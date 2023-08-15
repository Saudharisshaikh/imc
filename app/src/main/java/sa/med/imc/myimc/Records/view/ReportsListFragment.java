package sa.med.imc.myimc.Records.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.ReportListAdapter;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.HealthSummary.model.SimpleNameIDModel;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.LabReportsActivity;
import sa.med.imc.myimc.Records.view.activity.RadiologyReportsActivity;
import sa.med.imc.myimc.Settings.ContactUsActivity;
import sa.med.imc.myimc.Utils.Common;


public class ReportsListFragment extends Fragment {

    @BindView(R.id.iv_back_list)
    ImageView ivBackList;
    @BindView(R.id.main_toolbar_list)
    RelativeLayout mainToolbarList;
    @BindView(R.id.toolbar_title_list)
    TextView toolbarTitleList;
    @BindView(R.id.lay_lab_reports)
    LinearLayout layLabReports;
    @BindView(R.id.lay_radiology)
    LinearLayout layRadiology;
    @BindView(R.id.lay_sick_leave)
    LinearLayout laySickLeave;
    @BindView(R.id.lay_request_report)
    LinearLayout layRequestReport;
    @BindView(R.id.lay_list_reports)
    RelativeLayout layListReports;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    FragmentListener fragmentAdd;

    public ReportsListFragment() {
        // Required empty public constructor
    }


    public static ReportsListFragment newInstance() {
        ReportsListFragment fragment = new ReportsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports_list, container, false);
        ButterKnife.bind(this, view);
        if (BuildConfig.DEBUG)
            layRequestReport.setVisibility(View.VISIBLE);
        setUpProductsRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    // Initialize Products list view and add adapter to display data,
    void setUpProductsRecyclerView() {
        List<SimpleNameIDModel> list = new ArrayList<>();
        list.add(new SimpleNameIDModel(getString(R.string.lab_reports), getString(R.string.view_lab_reports), R.drawable.ic_lab_icon));

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0) == 1) {
            list.add(new SimpleNameIDModel(getString(R.string.radiology_reports), getString(R.string.view_radiology_reports), R.drawable.ic_radiology_icon));
        }

        list.add(new SimpleNameIDModel(getString(R.string.sick_levae), getString(R.string.view_lab_reports), R.drawable.ic_sick_leave));

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0) == 1) {
            list.add(new SimpleNameIDModel(getString(R.string.request_for_report), getString(R.string.complete_form_request_for_report), R.drawable.ic_records));
            list.add(new SimpleNameIDModel(getString(R.string.operative_reports), getString(R.string.view_current_past_reports), R.drawable.ic_scalpel));
            list.add(new SimpleNameIDModel(getString(R.string.discharge_reports), getString(R.string.view_current_past_reports), R.drawable.ic_discharge));
            list.add(new SimpleNameIDModel(getString(R.string.cardio_reports), getString(R.string.view_current_past_reports), R.drawable.ic_cardio));
        }

//        if (BuildConfig.DEBUG) {
//            list.add(new SimpleNameIDModel(getString(R.string.request_for_report), getString(R.string.complete_form_request_for_report), R.drawable.ic_records));
//          //  list.add(new SimpleNameIDModel(getString(R.string.cardio_reports), getString(R.string.view_current_past_reports), R.drawable.ic_cardio));
//        }
        ReportListAdapter adapter = new ReportListAdapter(getActivity(), list);
        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReportListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String position) {
                if (position.equalsIgnoreCase(getString(R.string.lab_reports))) {
                    startActivity(new Intent(getContext(), LabReportsActivity.class));
//                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_LAB, "");
                }

                if (position.equalsIgnoreCase(getString(R.string.radiology_reports))) {
                    startActivity(new Intent(getContext(), RadiologyReportsActivity.class));
//                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_RADIO, "");

                }

                if (position.equalsIgnoreCase(getString(R.string.sick_levae))) {
                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_SICK, "");

                }

                if (position.equalsIgnoreCase(getString(R.string.request_for_report))) {
                    ContactUsActivity.startActivity(getActivity(), "request");
                }

                if (position.equalsIgnoreCase(getString(R.string.operative_reports))) {
                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_OPERATIVE, "");
                }

                if (position.equalsIgnoreCase(getString(R.string.cardio_reports))) {
                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_CARDIC, "");

                }

                if (position.equalsIgnoreCase(getString(R.string.discharge_reports))) {
                    SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_DISCHARGE, "");

                }
            }

        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_back_list, R.id.lay_lab_reports, R.id.lay_radiology, R.id.lay_sick_leave, R.id.lay_request_report, R.id.lay_operative, R.id.lay_discarge_report, R.id.lay_cardiology})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_list:
                if (fragmentAdd != null)
                    fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                break;

            case R.id.lay_lab_reports:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_LAB, "");
                break;

            case R.id.lay_radiology:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_RADIO, "");
                break;

            case R.id.lay_sick_leave:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_SICK, "");
                break;

            case R.id.lay_operative:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_OPERATIVE, "");
                break;

            case R.id.lay_cardiology:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_CARDIC, "");
                break;

            case R.id.lay_discarge_report:
                SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_DISCHARGE, "");
                break;

            case R.id.lay_request_report:
                ContactUsActivity.startActivity(getActivity(), "request");
                break;
        }
    }
}
