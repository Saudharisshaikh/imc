package sa.med.imc.myimc.medprescription.invoice.list;

import java.util.List;

import okhttp3.ResponseBody;
import sa.med.imc.myimc.Utils.Loading;
import sa.med.imc.myimc.medprescription.invoice.model.PrescriptiontInvoiceItem;

public interface PresenterInvoiceView extends Loading {


    default void onInvoiceList(List <PrescriptiontInvoiceItem> invoices){

    }


}
