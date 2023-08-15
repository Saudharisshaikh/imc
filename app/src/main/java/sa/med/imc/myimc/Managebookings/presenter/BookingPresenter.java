package sa.med.imc.myimc.Managebookings.presenter;


/*
 * BookingPresenter interface to call API in BookingImpl.
 */
public interface BookingPresenter {

    void callGetAllBookingsApi(int hosp, String token, String mrNumber, String itemCount, String page, String bookingStatus);

    void callGetAllBookingsHomeApi(String token, String mrNumber, String itemCount, String page, String bookingStatus,int hosp);

    void callGetAllBookingsApi(int hosp, String token, String mrNumber, String itemCount, String page, String bookingStatus, String clinicCode, String doc_code, String from, String to);

    void callCancelBooking(String id, String token, String mrn, int hosp);

    void callGetReportsCount(String token, String mrn, String espisode_id, int hosp);

    void callPrintSlipApi(String token, String mrn, Integer espisode_id, int hosp);

    void callGetAppointmentPrice(String token, String mrNumber, String sessionId, String date, int hosp);
}


