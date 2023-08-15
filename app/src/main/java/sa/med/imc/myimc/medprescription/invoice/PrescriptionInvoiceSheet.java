package sa.med.imc.myimc.medprescription.invoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import sa.med.imc.myimc.Base.BaseBottomSheet;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.databinding.FragmentPrescriptionInvoicesBinding;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadPresenter;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadPresenterImpl;
import sa.med.imc.myimc.medprescription.invoice.download.PrescriptionInvoiceDownloadView;
import sa.med.imc.myimc.medprescription.invoice.list.PrescriptionInvoicePresenter;
import sa.med.imc.myimc.medprescription.invoice.list.PrescriptionInvoicePresenterImpl;
import sa.med.imc.myimc.medprescription.invoice.list.PresenterInvoiceView;
import sa.med.imc.myimc.medprescription.invoice.model.PrescriptiontInvoiceItem;

public class PrescriptionInvoiceSheet extends BaseBottomSheet implements PresenterInvoiceView, PrescriptionInvoiceDownloadView {
    private FragmentPrescriptionInvoicesBinding binding;
    private final PrescriptionInvoicePresenter prescriptionInvoicePresenter = new PrescriptionInvoicePresenterImpl(this);
    private final PrescriptionInvoiceDownloadPresenter invoiceDownloadPresenter = new PrescriptionInvoiceDownloadPresenterImpl(this);
    private PrescriptionInvoiceAdaptor adaptor;

    public static PrescriptionInvoiceSheet newInstance(String rxNo, Integer episodeNo, Integer orderNo,String episodeType){
        PrescriptionInvoiceSheet fragment = new PrescriptionInvoiceSheet();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.rxNo,rxNo);
        bundle.putInt(Constants.Bundle.episodeNo,episodeNo);
        bundle.putInt(Constants.Bundle.orderNo,orderNo);
        bundle.putString(Constants.Bundle.episodeType,episodeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrescriptionInvoicesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptor = new PrescriptionInvoiceAdaptor(requireContext(), (actionType, prescriptiontInvoiceItem) -> {
            invoiceDownloadPresenter.setSelectedOrderId(prescriptiontInvoiceItem.getId());
            invoiceDownloadPresenter.createFile(this);
        });
        binding.recyclerView.setAdapter(adaptor);
        if(getArguments()!=null){
            prescriptionInvoicePresenter.getInvoiceList(
                    getArguments().getString(Constants.Bundle.episodeType,""),
                    getArguments().getString(Constants.Bundle.rxNo,""),
                    getArguments().getInt(Constants.Bundle.episodeNo,-1),
                    getArguments().getInt(Constants.Bundle.orderNo,-1));
        }
    }

    @Override
    public void onInvoiceList(List<PrescriptiontInvoiceItem> invoices) {
        adaptor.setAndClearData(invoices);
        adaptor.notifyDataSetChanged();
        if (invoices.size()==0){
            this.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        invoiceDownloadPresenter.onActivityResult(requireActivity(),getArguments(),requestCode,resultCode,data);
    }

    @Override
    public void showLoading(int i) {
            Common.showDialog(requireContext());
    }

    @Override
    public void hideLoading(int i) {
            Common.hideDialog();
    }

    @Override
    public void onOpenFile() {
        dismiss();
    }

    @Override
    public void onCancelFile() {
        dismiss();
    }
}
