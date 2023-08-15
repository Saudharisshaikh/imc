package sa.med.imc.myimc.Profile.presenter;


/*
 * Profile Presenter interface to call API in ProfileImpl.
 */
public interface ProfilePresenter {

    void callGetUserProfileApi(String user_id, String token, int hosp);

    void callGetMedication(String token, String mrNumber, String episodeId, String itemCount, String page, int hosp);

    void callUpdateProfileApi(String token, String mrNumber, String email, String address, String addressAr, int hosp);

}