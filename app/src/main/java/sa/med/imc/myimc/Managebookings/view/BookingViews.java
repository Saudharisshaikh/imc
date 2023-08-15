package sa.med.imc.myimc.Managebookings.view;


import okhttp3.Headers;
import okhttp3.ResponseBody;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Booking views interface to handle UI in Booking Activity.
 */
public interface BookingViews extends Loading {

    void onGetBookings(BookingResponse response);

    void onGetBookingsNew(BookingResponseNew response);

    void onReschedule(SimpleResponse response);

    void onCancel(SimpleResponse response);

    void onDownloadConfirmation(ResponseBody response, Headers headers);

    void onGetPrice(PriceResponse response);

//    void onCheckinButtonListener(int position);
}
