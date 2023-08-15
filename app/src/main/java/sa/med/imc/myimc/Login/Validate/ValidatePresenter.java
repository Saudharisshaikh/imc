package sa.med.imc.myimc.Login.Validate;


/*
 * Validate Presenter interface implement APIs in ValidateImpl.
 */
public interface ValidatePresenter {

    void callValidateApi(String otp, String userid, String user_type, String device_id, String conenst, int hosp);

    void callResendOtpApi(String id_type, String id_value, String user_type, int hosp);

    void callUpdateConsentApi(String mrNumber, String constantValue);

    void callGuestValidateApi(String phoneNumber, String otp);

    void callGuestResendOtpApi(String phoneNumber);

    void callRegisterValidateApi(String phoneNumber, String otp, int hosp);

    void callRegisterResendOtpApi(String phoneNumber, String iqama_id, String type, int hosp);

}
