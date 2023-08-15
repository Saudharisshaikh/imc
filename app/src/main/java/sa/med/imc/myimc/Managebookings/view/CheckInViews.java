package sa.med.imc.myimc.Managebookings.view;


import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 *  CheckIn views interface to handle UI in Booking Activity.
 */
public interface CheckInViews extends Loading {

    void onGetLocation(SimpleResponse response);

    void onGetPaymentSuccess(PaymentResponse response);

    void onArrived(SimpleResponse response);

}
