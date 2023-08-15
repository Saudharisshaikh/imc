package sa.med.imc.myimc.Login;


import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Login views interface to handle UI in Login Activity.
 */
public interface LoginViews extends Loading {

    void onSuccessLogin(LoginResponse response);

}
