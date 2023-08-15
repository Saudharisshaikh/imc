package sa.med.imc.myimc.Clinics.presenter;


/*
 * Clinic Presenter interface to call API in ClinicImpl.
 */
public interface ClinicPresenter {

    void callGetAllClinics(String token, String physician_id, String isActive, String search_txt, String item_count, String page, int hosp);


    void callGetSearchClinics(String token, String physician_id, String isActive, String search_txt, String item_count, String page, int hosp);


}


