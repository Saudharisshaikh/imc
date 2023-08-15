package sa.med.imc.myimc.medprescription.invoice.download;

import okhttp3.ResponseBody;
import sa.med.imc.myimc.Utils.Loading;

public interface PrescriptionInvoiceDownloadView extends Loading {
    default void onDownload(ResponseBody responseBody){

    }

    default void onOpenFile(){

    }

    default void onCancelFile(){

    }

}
