package sa.med.imc.myimc.Login.Validate;


import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Validate views interface to handle UI in Verification Activity.
 */
public interface ValidateViews extends Loading {

    void onValidateOtp(ValidateResponse response);

    void onResend(LoginResponse response);

    void onConsentSuccess(SimpleResponse response);

    void onSuccessGuestValidate(SimpleResponse response);


}
