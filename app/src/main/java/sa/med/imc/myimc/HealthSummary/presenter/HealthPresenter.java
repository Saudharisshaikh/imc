package sa.med.imc.myimc.HealthSummary.presenter;


/*
 * Health Presenter interface to handle UI in Health Fragment.
 */
public interface HealthPresenter {

    void callGetAllAllergies(String token, String patientId, String mrn);

    void callGetAllReadings(String token, String mrn,String page);

}
