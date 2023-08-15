package sa.med.imc.myimc.pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.databinding.FragmentPharmacyBinding;
import sa.med.imc.myimc.pharmacy.model.PharmacyItem;
import sa.med.imc.myimc.pharmacy.model.PharmacyTelrResponse;
import sa.med.imc.myimc.webView.model.WebViewRequest;

public class PharmacyFragment extends BaseFragment implements PharmacyView {
    private FragmentPharmacyBinding binding;
    private PharmacyAdaptor adaptor;

    public static PharmacyFragment getInstance(String rxNo, String episodeNo){
        PharmacyFragment fragment = new PharmacyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.rxNo,rxNo);
        bundle.putString(Constants.Bundle.episodeNo,episodeNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    private PharmacyPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new PharmacyPresenterImpl(requireActivity(),this);
        binding = FragmentPharmacyBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null) {
           String rxNo = getArguments().getString(Constants.Bundle.rxNo);
            String  episodeNo = getArguments().getString(Constants.Bundle.episodeNo);
            presenter.callPrescriptionDetails(rxNo,episodeNo);
        }
        binding.toolbar.setNavigationOnClickListener(v->{
            getCallBack().backPressed("MedicinesFragment");
        });
        binding.tvBuy.setOnClickListener(v->{
            if (adaptor.getDataList().size()==0){
                return;
            }
            presenter.validate(adaptor.getDataList().get(0));
        });
        adaptor = new PharmacyAdaptor(requireActivity());
        binding.recyclerView.setAdapter(adaptor);


    }

    @Override
    public void onDetailResponse(List<PharmacyItem> response) {
        adaptor.setData(response);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void onPaymentValidation(PharmacyTelrResponse response) {
        WebViewRequest webViewRequest = new WebViewRequest();
        webViewRequest.setPaymentRef(response.getPaymentRef());
        webViewRequest.setTitle(getString(R.string.pharmacy));
        webViewRequest.setWebLink(response.getPaymentUrl());

        getCallBack().onWebView(webViewRequest);
//        getCallBack().onWebView(getString(R.string.pharmacy),response.getPaymentUrl(),response.getPaymentRef());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected Runnable onBackPressedAction() {
        return null;
    }
}
