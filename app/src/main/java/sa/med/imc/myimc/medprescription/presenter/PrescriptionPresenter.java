package sa.med.imc.myimc.medprescription.presenter;


/*
 * Prescription Presenter interface to call API in ProfileImpl.
 */
public interface PrescriptionPresenter {

    void callGetPrescriptionApi(String token, String patId, String epsoideNo, String fromDate, String toDate, String item_count, String page, int hosp);
    void callGeneratePrescriptionPdf(String token, String mr_number, String prespNumber,String epsoideNo, int hosp);

}