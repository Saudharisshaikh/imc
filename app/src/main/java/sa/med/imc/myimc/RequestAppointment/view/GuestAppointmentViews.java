package sa.med.imc.myimc.RequestAppointment.view;


import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * GuestAppointment views interface to handle UI in Request Appointment Activity.
 */
public interface GuestAppointmentViews extends Loading {

    void onSuccessLogin(LoginResponse response);

}
