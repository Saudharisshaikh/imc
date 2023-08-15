package sa.med.imc.myimc.AddGuardian.presenter;


/*
 * Guardian Presenter interface to call API in GuardianImpl.
 */
public interface GuardianPresenter {

    void callAddGuardian(String token, String mrn, String g_mrn, int isActive, int n_days, int consent, int hosp);

    void callGetGuardian(String token, String mrn, int hosp);

    void callRemoveGuardian(String token, String mrn, String g_mrn, int hosp);

    void callLinkUnlinkGuardian(String token, String mrn, String g_mrn, int isActive, int hosp);

    void callUpdateGuardian(String token, String mrn, String g_mrn, int n_days, int hosp,int isActive, int consent);

}


