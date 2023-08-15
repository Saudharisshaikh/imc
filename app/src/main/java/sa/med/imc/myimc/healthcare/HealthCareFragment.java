package sa.med.imc.myimc.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import sa.med.imc.myimc.Base.BaseAdaptor;
import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.SingleFragmentActivity;
import sa.med.imc.myimc.Utils.ColorUtils;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.WelcomeActivity;
import sa.med.imc.myimc.databinding.FragmentHealthcareBinding;
import sa.med.imc.myimc.healthcare.form.HealthCareFormSubmissionFragment;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;
import sa.med.imc.myimc.healthcare.model.HealthCareListRequest;
import sa.med.imc.myimc.healthcare.model.MultiSelectModel;
import sa.med.imc.myimc.healthcare.tooltip.ToolTipBottomSheet;

public class HealthCareFragment extends BaseFragment implements HealthCareView{
    private FragmentHealthcareBinding binding;
    private HealthCareAdaptor adaptor;
    private final HealthCarePresenter presenter = new HealthCarePresenterImpl(this);


    public static HealthCareFragment newInstance() {
        return new HealthCareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHealthcareBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setBtnVisibility(true);
        adaptor = new HealthCareAdaptor(requireContext(), (action, healthCareItem) -> {
            ToolTipBottomSheet.newInstance(SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.KEY_LANGUAGE,"").equals("ar") ?
                    healthCareItem.getTooltipAr() : healthCareItem.getTooltipEn()).showNow(getChildFragmentManager(),"");
        });
        adaptor.setHasStableIds(true);
        //binding.recyclerView.setBackgroundColor(ColorUtils.getColorWithAlpha());
        binding.toolbar.setNavigationOnClickListener(v->requireActivity().onBackPressed());
        binding.recyclerView.setAdapter(adaptor);
        binding.recyclerView.setItemAnimator(null);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                binding.setBtnVisibility(newState == RecyclerView.SCROLL_STATE_IDLE);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    LinearLayoutManager manager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
//                    if (manager==null){
//                        return;
//                    }
//                    for (int  i = manager.findFirstVisibleItemPosition(); i< manager.findLastVisibleItemPosition();i ++){
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean(HealthCareAdaptor.RECYCLER_VIEW_VISIBILITY,true);
//                        adaptor.notifyItemChanged(i,bundle);
//                    }
//                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        presenter.onHealthCareRequest();
        binding.setSubmitClick(v -> {
            if (adaptor.getSelectionsCount()==0){
                Toast.makeText(requireContext(),getString(R.string.txt_form_select_something),Toast.LENGTH_LONG).show();
                return;
            }
            HealthCareFormSubmissionFragment.newInstance((s,s1, s2) -> {
                presenter.onHealthCareSubmit(s,s1,s2,new ArrayList<>(adaptor.getDataList()));
                return null;
            }).show(getChildFragmentManager(),"");
        });
       // adaptor.setData(MultiSelectModel.mocks(10,1));
    }

    @Override
    protected Runnable onBackPressedAction() {
        return () -> {
            Common.CONTAINER_FRAGMENT="HomeFragment";
            getCallBack().showToolbar();
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
        };
    }

    @Override
    public void onHealthCareListResponse(List<HealthCareItem> list) {
        adaptor.setData(list);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void onHealthCareSubmission() {
        if (requireActivity() instanceof SingleFragmentActivity) {
//            callback.setEnabled(false);
        }
        Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(refresh);
        Common.messageDailog(getActivity(), getString(R.string.txt_form_submitted), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        requireActivity().onBackPressed();

            }
        });
//        Toast.makeText(requireContext(), getString(R.string.txt_form_submitted),Toast.LENGTH_LONG).show();
    }
}
