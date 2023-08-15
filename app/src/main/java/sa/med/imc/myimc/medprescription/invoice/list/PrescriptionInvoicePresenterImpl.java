package sa.med.imc.myimc.medprescription.invoice.list;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Base.GenericResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.medprescription.invoice.model.PrescriptiontInvoiceItem;
import sa.med.imc.myimc.medprescription.invoice.model.RequestPrescriptionInvoice;

public class PrescriptionInvoicePresenterImpl implements PrescriptionInvoicePresenter{
    private final PresenterInvoiceView view;

    public PrescriptionInvoicePresenterImpl(PresenterInvoiceView view) {
        this.view = view;
    }

    @Override
    public void getInvoiceList(String episodeType,String rxNo, Integer episodeNo, Integer orderNo) {
        view.showLoading(0);
        int hosp = SharedPreferencesUtils.getInstance().getValue(Constants.SELECTED_HOSPITAL,-1);
        String patId = SharedPreferencesUtils.getInstance().getValue(Constants.KEY_MRN,"");
        RequestPrescriptionInvoice requestPrescriptionInvoice = new RequestPrescriptionInvoice();
        requestPrescriptionInvoice.setEpisodeType(episodeType);
        requestPrescriptionInvoice.setHosp(String.valueOf(hosp));
        requestPrescriptionInvoice.setEpisodeNo(episodeNo);
        requestPrescriptionInvoice.setOrderNo(orderNo);
        requestPrescriptionInvoice.setRxNo(rxNo);
        requestPrescriptionInvoice.setPatId(String.valueOf(patId));
        Call<GenericResponse<List<PrescriptiontInvoiceItem>>> call = ServiceGenerator.createPharmacyService(WebService.class,SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN,""))
                .getPrescriptionInvoices(requestPrescriptionInvoice);
        call.enqueue(new Callback<GenericResponse<List<PrescriptiontInvoiceItem>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<PrescriptiontInvoiceItem>>> call, Response<GenericResponse<List<PrescriptiontInvoiceItem>>> response) {
                view.hideLoading(0);
                if (response.isSuccessful() && response.code()==200 && response.body()!=null &&response.body().getData()!=null ){
                    view.onInvoiceList(response.body().getData());
                }
                else if (response.body()!=null){
                    view.onFail(response.body().getMessage());
                }
                else{
                    view.onFail(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse<List<PrescriptiontInvoiceItem>>> call, @NonNull Throwable t) {
                view.hideLoading(0);
                view.onFail(t.getMessage());
            }
        });

    }


}
