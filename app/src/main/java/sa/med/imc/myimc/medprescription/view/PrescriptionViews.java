package sa.med.imc.myimc.medprescription.view;


import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;
import sa.med.imc.myimc.medprescription.model.PrescriptionResponse;

/*
 * Prescription views interface to handle UI in Prescription fragment.
 */
public interface PrescriptionViews extends Loading {

    void onGetPrescription(PrescriptionResponse response);
    void onGeneratePrescriptionPdf(ResponseBody response, Headers headers);

}
