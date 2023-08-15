package sa.med.imc.myimc.Login;


/*
 * Login Presenter interface to call API in Login Activity.
 */
public interface LoginPresenter {

    void callLoginApi(String id_type, String id_value, String user_type, int hosp);
}
