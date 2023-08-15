package sa.med.imc.myimc.Managebookings.presenter;


/*
 * CheckInPresenter interface to call API in CheckInImpl.
 */
public interface CheckInPresenter {

    void callCheckLocation(String token, String lat, String lng, String date, int hosp);

    void callPayment(String token, String mrNumber, String sessionId, String timeSlot, String date, String bookingId,
                     String teleHealthFlag, String consent, String arrivalUpdate,int hosp);

    void callArrival(String token, String mrNumber, String bookingId, int hosp);


}


