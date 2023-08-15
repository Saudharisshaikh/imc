package sa.med.imc.myimc.RequestAppointment.presenter;


/*
 * Guest Appointment Presenter interface to call API in Request Appointment Activity.
 */
public interface GuestAppointmentPresenter {

    void callGuestLoginApi(String phoneNumber,String docNumber,String docType,int hosp);
    void callRegisterLoginApi(String phoneNumber,String iqama_id,String type, int hosp);

}
